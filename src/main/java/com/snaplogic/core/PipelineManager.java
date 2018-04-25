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

package com.snaplogic.core;

import com.snaplogic.jpipe.core.graph.PipelineNode;

/**
 * PipelineManager exposes various pipeline management related api methods.
 *
 * @author ksubramanian
 * @since Aug, 2015
 */
public interface PipelineManager {
    /**
     * Prepares the given pipeline node for execution.
     *
     * @param pipelineNode
     */
    void prepare(PipelineNode pipelineNode);

    /**
     * Returns true if the pipeline is already prepared.
     *
     * @return true if prepare was called.
     */
    boolean isPrepared();

    /**
     * Starts the pipeline execution.
     */
    void start();

    /**
     * Waits for the completion of pipeline execution.
     */
    void waitForCompletion();

    /**
     * Stops the pipeline execution thread.
     */
    void stop();
}
