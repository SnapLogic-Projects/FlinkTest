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

/**
 * Messages contains all the externalizable messages in this package.
 *
 * @author ksubramanian
 * @since Jul, 2015
 */
public class Messages {

    static final String RUN_THE_APPLICATION_PROXYING_USER = "Run the application proxying user";
    static final String SPARK_DRIVER_ARGUMENTS = "Spark driver arguments:";
    static final String INITIALIZING_SPARK_CONF_WITH_JARS = "Initializing spark conf with jars: {}";
    static final String URL_OF_THE_SPARK_MASTER = "URL of the spark master";
    static final String LOCATION_OF_THE_SPARK_HOME = "Location of the Spark home";
    static final String SPARK_APPLICATION_NAME = "Spark application name: {}";
    static final String LOG_SPARK_MASTER = "Spark master: {}";
    static final String LOG_SPARK_HOME = "Spark home: {}";
    static final String SETTING_SPARK_PROPERTY = "Setting spark property: {} -> {}";
    static final String PLEX_NODE_DID_NOT_PROVIDE_A_CALLBACK = "Plex node did not provide a " +
             "callback url";

    static final String INITIALIZING_SPARK_CONF = "Initializing spark conf";
    static final String INITIALIZING_SPARK_CONTEXT = "Initializing spark context";
    static final String DRIVER_INTERRUPTED = "Driver interrupted";
    static final String HEARTBEAT_CALLBACK_URL = "Callback url";
    static final String FAILED_TO_FIND_A_RANDOM_PORT = "Failed to find a random port for spark.ui";
    static final String INITIALIZING_SPARK_UI_AT = "Initializing spark UI at {}";

    static final String UNABLE_TO_REGISTER_DRIVER_WITH_JCC = "Unable to register driver with JCC" +
             " at %s";
    static final String DRIVER_REGISTRATION_FAILED = "Driver registration with jcc: %s has failed" +
            ". Error: %s";
    static final String FAILED_TO_REGISTER_DRIVER = "Failed to register driver. Stopping the " +
             "driver";
    static final String REGISTERING_DRIVER_WITH_JCC = "Registering driver with jcc using url: {}";
    static final String REGISTRATION_SUCCESSFUL = "Registration successful";
    static final String PORT_WHERE_THE_DRIVER_WILL_LISTEN = "port where the driver will listen";

    static final String YARN_CONF_DIR = "Yarn configuration directory";

    static final String PREPARING_PIPELINE = "Preparing pipeline: {}";
    static final String ERROR_PREPARING_PIPELINE = "Error preparing pipeline: %s";
    static final String STARTING_PIPELINE = "Starting pipeline: {}";
    static final String PERFORMING_MULTI_PHASE_PIPELINE_EXECUTION = "Performing multi-phase " +
             "pipeline execution";
    static final String EXECUTING_PHASE = "Executing phase: {}";
    static final String SETTING_UP_VIEWS_FOR_PIPELINE = "Setting up views for pipeline: {}";
    static final String ACCOUNT_NEEDS_TO_BE_SET_IN_THE_ACCOUNTS_TAB = "Account is required, " +
            "please set in Accounts tab";
    static final String CANNOT_START_PIPELINE = "Cannot start pipeline as the pipeline was not " +
            "prepared";
    static final String ADDED_HDFS_HANDLER_TO_DEFAULT_STREAM_HANDLER_FACTORY = "Added HDFS " +
             "handler to default stream handler factory";
    static final String ADDING_LIB_TO_SPARK_CONTEXT = "Adding lib {} to spark context";
    static final String ERROR_ADDING_COMMON_LIB_TO_SPARK_CONTEXT = "Error while adding common " +
             "libraries to spark context";
    static final String ENABLES_THE_SPARK_DRIVER_DEBUG_LOGS = "Enables the spark driver debug " +
             "logs";
    static final String STOPPING_PIPELINE_EXECUTOR_THREAD = "Stopping pipeline executor thread";
    static final String INTERRUPTED_WHILE_SHUTTING_DOWN_THREADPOOL = "Interrupted while shutting" +
             " down threadpool";
    static final String ERROR_WHILE_WAITING_FOR_PIPELINE_COMPLETION = "Error while waiting for " +
            "pipeline completion";
    static final String PIPELINE_NOT_STARTED_YET = "Pipeline not started yet";
    static final String STAGE_COMPLETED = "Stage completed: {}";
    static final String STAGE_SUBMITTED = "Stage submitted: {}";
    static final String TASK_STARTED = "Task started: {}";
    static final String TASK_END = "Task end: {}";
    static final String JOB_STARTED = "Job started: {}";
    static final String JOB_END = "Job end: {}";
    static final String APPLICATION_STARTED = "Application started: {}";
    static final String APPLICATION_ENDED = "Application ended: {}";
    static final String SENDING_NOTIFICATION_TO_JCC = "Sending notification to JCC: {}";
    static final String FAILED_TO_SEND_NOTIFICATION = "Failed to send notification to {}. Cause:" +
             " {}";
    static final String ERROR_SENDING_NOTIFICATION = "Error sending notification to %s";
    static final String REGISTERING_SPARK_LISTENERS = "Registering spark listeners";
    static final String NOTIFIED_JCC_ABOUT_JOB_COMPLETION = "Notified JCC about job " +
            "completion";
    static final String ALREADY_VISITED = "Already visited so nothing to do for node: {}";
    static final String TEMPORARILY_SKIPPING_NODE = "Temporarily skipping node: {} till all the " +
             "incoming links are visited";
    static final String VISITING_SNAP_NODE = "Visiting snap node: {}";
    static final String SENDING_STATS_MSG = "Sending stats message: %s";
    static final String ENCOUNTERED_ERROR = "Encountered error during visit:\n {}";
    static final String ERROR_DURING_VISIT = "Error during visit %s.";
    static final String LINE_MARKER = "======================================================" +
            "================";

    static final String DETECTED_SPARK_VERSION = "Detected spark version: {}";
    static final String CHANGED_EXECUTOR_HEART_BEAT = "Changed executor heart beat to {}";
    static final String ADVANCING_TO_NEXT_PHASE = "Advancing to next phase";
    static final String FLINK_PIPELINE_EXECUTION_FAILED_WITH_ERROR = "Spark pipeline execution " +
             "failed with error";
    static final String SKIPPING_EXECUTOR_ONLY_LIBRARY = "Skipping executor only library: {}";
    static final String LOADING_COMMON_LIBRARIES = "Loading common libraries from {}";
    static final String LOADING_JDBC_DRIVER_FILES_FROM_STORE = "Loading JDBC driver files from " +
             "store {}";
    static final String STATE_COULD_NOT_BE_COMPUTED_FROM_SPARK_CONTEXT = "State could not be " +
            "computed from spark context";
    static final String DRIVER_NOT_YET_PREPARED = "Driver not yet prepared";
    static final String REVERTING_STATE_COMPLETE_TILL_ALL_SPARK_JOBS_FINISHES = "Reverting the " +
            "state from COMPLETED to STARTED as the graph visit is not completely done";
    static final String FETCHING_SPARK_CONTEXT_STATS = "Returning Spark context statistics: %n%s%n";
    static final String UNABLE_TO_LOG_SPARK_CONTEXT_STATISTICS = "Unable to log spark context " +
            "statistics";

    // LOG
    static final String PARSING_COMMAND_LINE_ARGS = "Parsing command line arguments: {}";
    static final String UNCAUGHT_EXCEPTION_IN_THREAD = "Uncaught exception in thread: %s.%s";

    // Params
    static final String NO_ARGUMENT_PROVIDED_FOR_FLINK_DRIVER = "No argument provided for flink driver";
    static final String ERROR_PARSING_COMMAND_LINE_OPTS = "Error parsing command line options: %s";

    static final String JAR_FILES_FOR_THE_FLINK_APPLICATION = "Jar files for the flink application";
    static final String NAME_OF_THE_FLINK_APPLICATION = "Name of the flink application";

    static final String PIPELINE_RUUID_FOR_THIS_DRIVER = "Pipeline ruuid for this driver";
    static final String PIPELINE_RUUID_NOT_PROVIDED = "Pipeline ruuid not provided";

    static final String SNAPLOGIC_HDFS_HOME = "Snaplogic HDFS home path";
    static final String SNAPLOGIC_DRIVER_HOME_NOT_PROVIDED = "Snaplogic driver home path was not provided";

    static final String PIPELINE_DEFINITION = "Pipeline definition";
    static final String PIPELINE_FILE_NOT_PROVIDED = "Pipeline file not provided";

}
