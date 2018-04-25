/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2015, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.core.execution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.snaplogic.common.jsonpath.JsonPath;
import com.snaplogic.core.DriverConstants;
import com.snaplogic.core.DriverException;
import com.snaplogic.core.PipelineManager;
import com.snaplogic.core.util.HdfsUtil;
import com.snaplogic.jpipe.core.GraphBuilder;
import com.snaplogic.jpipe.core.graph.Constants;
import com.snaplogic.jpipe.core.graph.PipelineNode;
import com.snaplogic.jsonpath.JsonPathImpl;
import com.snaplogic.snap.schema.util.JsonSchemaConstants;
import com.snaplogic.util.JsonPathUtil;
import com.snaplogic.util.State;
import com.snaplogic.util.StatisticsUtil;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static com.snaplogic.core.execution.Messages.*;

/**
 * PipelineExecutor is the rest resource that is used by JCC to manage the flink driver program.
 *
 * @author yiding liu
 * @since Mar, 2018
 */
public class PipelineExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(PipelineExecutor.class);
    private static final String STATE = "state";
    private static final JsonPath SNAP_PATH = JsonPathImpl.compileStatic(Constants.SNAP_MAP);
    private final JsonPath RUUID_PATH = JsonPathImpl.compileStatic(Constants.RUUID);
    private final ExecutionEnvironment flinkEnv;
    private final PipelineManager pipelineManager;
    private final ObjectMapper objectMapper;
    private final HdfsUtil hdfsUtil;
    private final AtomicReference<Map<String, Object>> endStateRef = new AtomicReference<>();
    private final StatisticsUtil statisticsUtil;
    private volatile boolean isPrepared = false;
    private volatile boolean isStarted = false;
    private volatile boolean isStopInitiated = false;
    private volatile boolean isStopped = false;
    private volatile boolean isFailed = false;
    private volatile boolean isCompleted = false;
    private volatile Instant finishTime = Instant.MIN;
    private volatile PipelineNode pipelineNode;
    private Map<String, Object> errorMap;


    @Inject
    public PipelineExecutor(ExecutionEnvironment flinkEnv, HdfsUtil hdfsUtil,
                            PipelineManager pipelineManager, ObjectMapper objectMapper,
                            StatisticsUtil statisticsUtil) {
        this.flinkEnv = flinkEnv;
        this.hdfsUtil = hdfsUtil;
        this.pipelineManager = pipelineManager;
        this.objectMapper = objectMapper;
        this.statisticsUtil = statisticsUtil;
    }

    /**
     * Returns a stats Info as a map that will be serialized into json format.
     *
     * @return statsMap
     */
    public Map<String, Object> getStats() {
        return new HashMap<>();
    }

    /**
     * Prepares the given flink pipeline and returns the status
     * @param executeData
     */
    public void preparePipeline(Map<String, Object> executeData) {
        LOG.info(BUILDING_PIPELINE_GRAPH);
        GraphBuilder graphBuilder = new GraphBuilder();
        Map<String, Object> pipeDefs = getPipeDefs(executeData);
        Map<String, Map<String, Object>> acctDefs = (Map<String, Map<String, Object>>)
                executeData.get(JsonSchemaConstants.ACCOUNT_DEFS);
        String ruuid = (String) executeData.get(JsonSchemaConstants.MAIN);
        graphBuilder.merge(pipeDefs, acctDefs, null);
        this.pipelineNode = graphBuilder.buildGraph(ruuid, DriverConstants.FLINK_MODE);
        LOG.info(PREPARING_PIPELINE, pipelineNode.getRuuid());
        this.pipelineManager.prepare(pipelineNode);
        isPrepared = true;
    }

    /**
     * Starts the given flink pipeline
     *
     * @return statsMap
     */
    public void start() {
        if (this.pipelineManager.isPrepared()) {
            LOG.info(EXECUTING_PIPELINE, pipelineNode.getRuuid());
            this.pipelineManager.start();
            isStarted = true;
            this.pipelineManager.waitForCompletion();
        } else {
            LOG.error(PREPARE_WAS_NOT_CALLED_BEFORE_START);
            throw new DriverException(PREPARE_WAS_NOT_CALLED_BEFORE_START);
        }
    }

    /**
     * Stops the Driver process, which includes the flink context and web server.
     */
    public void stop() {
        if (!isStopped) {
            isStopInitiated = true;
        }

        // Run the shutdown in a separate thread, so that the HTTP request can return.
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                shutDown();
            }
        });
    }

    private void shutDown() { }

    public boolean isPrepared() {
        return isPrepared;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public Instant getFinishTime() {
        return finishTime;
    }

    public void markAsFailed() {
        this.finishTime = Instant.now();
        this.isFailed = true;
    }

    public void markAsCompleted() {
        this.finishTime = Instant.now();
        this.isCompleted = true;
    }
    public void setErrorMap(Map<String, Object> errorMap){
        LOG.info("Set the errorMap {}", errorMap);
        this.errorMap = errorMap;
    }

    //------------------------------------ Private methods ---------------------------------------//
    private Map<String, Object> getPipeDefs(final Map<String, Object> executeData) {
        Map<String, Object> pipeDefs = (Map<String, Object>) executeData.get(
                JsonSchemaConstants.PIPE_DEFS);
        Map<String, Object> rtMap = (Map<String, Object>) executeData.get(JsonSchemaConstants.RT);
        Map<String, Object> overrideEnvMap = null;
        if (rtMap != null) {
            overrideEnvMap = (Map<String, Object>) rtMap.get(JsonSchemaConstants.ENV_MAP);
        }
        for (Map.Entry<String, Object> pipeDefEntry : pipeDefs.entrySet()) {
            // Add the missing RUUID field to the pipe definition
            Object value = pipeDefEntry.getValue();
            if (value instanceof Map) {
                Map<String, Object> pipeDef = (Map<String, Object>) value;
                JsonPathUtil.addKeyValueForPath(pipeDef, RUUID_PATH.toString(),
                        pipeDefEntry.getKey());
                // Set pipeline level properties in each snap map
                Map<String, Map<String, Object>> snaps = SNAP_PATH.readStatic(pipeDef,
                        Collections.EMPTY_MAP);
                for (Map.Entry<String, Map<String, Object>> snapEntry : snaps.entrySet()) {
                    Map<String, Object> snapDataMap = snapEntry.getValue();
                    snapDataMap.put(JsonSchemaConstants.PATH_ID,
                            pipeDef.get(JsonSchemaConstants.PATH_ID));
                    snapDataMap.put(JsonSchemaConstants.ORG_SNODE_ID,
                            pipeDef.get(JsonSchemaConstants.ORG_SNODE_ID));
                    snapDataMap.put(JsonSchemaConstants.PROJECT_SNODE_ID,
                            pipeDef.get(JsonSchemaConstants.PROJECT_SNODE_ID));
                    // Add any overriding properties (such as task properties) to each snap
                    if (overrideEnvMap != null) {
                        snapDataMap.put(JsonSchemaConstants.ENV_MAP, overrideEnvMap);
                    }
                    snapDataMap.put(JsonSchemaConstants.SNAP_DATA_MAP, snapDataMap);
                }
            } else {
                throw new DriverException(String.format(
                        PIPELINE_DEFINITION_DOES_NOT_CONTAIN_SNAP_DATA, SNAP_PATH.toString()));
            }
        }
        return pipeDefs;
    }
}
