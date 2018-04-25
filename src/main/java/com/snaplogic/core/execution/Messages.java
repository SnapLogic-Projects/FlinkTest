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

/**
 * Messages holds the externalizable messages in this package.
 *
 * @author ksubramanian
 * @since Jul, 2015
 */
public class Messages {
    static final String FETCHING_SPARK_CONTEXT_STATS = "Returning Spark context stats:%n%s%n";
    static final String BUILDING_PIPELINE_GRAPH = "Building pipeline graph";
    static final String PREPARE_WAS_NOT_CALLED_BEFORE_START = "Prepare was not called before start";
    static final String PREPARING_PIPELINE = "Preparing pipeline: {}";
    static final String EXECUTING_PIPELINE = "Executing pipeline: {}";
    static final String STOPPING_SPARK_CONTEXT = "Stopping spark context";
    static final String GENERATING_ERROR_RESPONSE = "Generating error response";
    static final String UNABLE_TO_LOG_SPARK_CONTEXT_STATISTICS = "Unable to log spark context " +
            "statistics";
    static final String PIPELINE_DEFINITION_DOES_NOT_CONTAIN_SNAP_DATA = "Pipeline definition " +
            "does not contain snap specific data under %s";
    static final String STOP_REQUESTED_THROUGH_SNAP_LOGIC_DRIVER_REST_API = "Stop requested " +
            "through SnapLogic driver rest api";
    static final String STOPPING_ACTIVE_STAGE = "Stopping active stage: {}";
    static final String STOPPING_ALL_ACTIVE_STAGES = "Stopping all active stages";
    static final String STOPPING_ALL_ACTIVE_JOBS = "Stopping all active jobs";
    static final String CANCELING_JOB = "Canceling job: {}";
    static final String KILLING_EXECUTORS_IF_ANY = "Killing executors if any";
    static final String FAILED_TO_KILL_EXECUTOR_JOB_STAGE = "Failed to kill executor/job/stage";
    static final String STOPPING_PIPELINE_MANAGER = "Stopping pipeline manager";
    static final String STATE_COULD_NOT_BE_COMPUTED_FROM_SPARK_CONTEXT = "State could not be " +
            "computed from spark context";
    static final String DRIVER_NOT_YET_PREPARED = "Driver not yet prepared";
    static final String BAD_REQUEST_FROM_SPARK_LOG_RESOURCE = "Bad request from jcc " +
            "SparkLogResource: applicationID is {}, logLevelStr is {}, offset is {}, limit is" +
            " {}";
    static final String GET_REQUEST_FROM_SPARK_LOG_RESOURCE = "Get request from jcc " +
            "SparkLogResource: applicationID is {}, logLevelStr is {}, offset is {}, limit is" +
            " {}";
    static final String LOG_DIRECTORY_IS = "Log Directory is {}; Driver log file is {}";
    static final String BAD_REQUEST_MISSING_APP_ID = "Missing Parameters: Application ID";
    static final String BAD_REQUEST_INVALID_PARAMETER = "Parameters offset and limit can't be " +
            "negative. offset: %s, limit: %s";
    static final String CAN_NOT_READ_DRIVER_LOG_FILE = "Can't read driver log file: {}";
    static final String CAN_NOT_FIND_LOG_FILE = "Can't find log file, the directory is : {}";
    static final String REVERTING_STATE_COMPLETE_TILL_ALL_SPARK_JOBS_FINISHES = "Reverting the " +
            "state from COMPLETED to STARTED as the graph visit is not completely done";

}
