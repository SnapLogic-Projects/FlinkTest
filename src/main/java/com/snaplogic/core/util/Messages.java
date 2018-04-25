/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.core.util;

/**
 * Messages holds all the externalizable messages in this package
 *
 * @author ksubramanian
 * @since Jan, 2016
 */
public class Messages {
    static final String ERROR_CONVERTING_MAP_TO_STRING = "Error converting map to string";
    static final String COMPUTED_STATE = "Computed state: {}";
    static final String COMPUTING_STATE_FROM_JOB_COUNT = "Computing state from Job count " +
             "[active: {} failed: {} completed: {}]";
    static final String UNABLE_TO_COMPUTE_STATE_FROM_SPARK_CONTEXT = "Unable to compute " +
            "pipeline state from spark context";
    static final String LOADING_JDBC_DRIVER_FILES = "Loading jdbc driver files from {}";
    static final String FAILED_TO_LOAD_JDBC_DRIVER_FILES = "Failed to load jdbc driver files " +
            "from %s";
}
