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
package com.snaplogic.core.stream.url.hdfs;

/**
 * Messages is the container for all the externalized messages used by the classes in this
 * package.
 *
 * @author psung (original author), ksubramanian
 */
public class Messages {
    // error messages
    static final String ERR_INVALID_URL = "Invalid HDFS URL: %s";
    static final String INTERRUPTED_WHILE_OPENING_FILE = "Interrupted while opening hdfs " +
            "file";
    static final String INTERRUPTED_WHILE_DELETING_FILE = "Interrupted while deleting hdfs " +
            "file";
    static final String RENEWED_DELEGATION_TOKEN = "Renewed delegation token as {} and got token" +
            " of size: {}";
    static final String GOT_DELEGATION_TOKEN = "Got delegation token from {} as {}";
    static final String UNABLE_TO_RENEW_DELEGATION_TOKEN = "Unable to renew delegation token as " +
            "{}";
    static final String DELEGATION_TOKEN_RETRIEVAL_FAILED = "Failed to add delegation tokens to" +
            " credentials";
    static final String TOKEN_FILE_ERROR = "Secure file could not be opened for " +
            "token retrieval";
    static final String HDFS_OUTPUT_STREAM = "Unable to create output in HDFS";
    static final String HDFS_OUTPUT_STREAM_SECURE = "Unable to create output in HDFS with" +
            " security enabled";
    static final String HDFS_OPEN_FAILURE = "Failed to open file in hdfs";
    static final String UNABLE_TO_GET_USER_INFORMATION = "Unable to get current user " +
             "information";
    static final String INTERRUPTED_WHILE_LOADING_LIBRARIES = "Interrupted while " +
             "loading libraries from: %s";
    static final String LOADING_SNAPPACK_LIBRARIES_FROM = "Loading libraries for snappack {} " +
             "from {}";
    static final String ADDING_TO_CLASSLOADER = "Adding {} to classloader";
    static final String SKIPPING_JAR_FILE = "Skipping jar file: {} as it is already available in" +
             " spark context";
    static final String ADDING_JAR_FILE_TO_SPARK_CONTEXT = "Adding {} from {} to spark context";
    static final String SKIPPING_NON_EXISTENT_FILE = "Skipping non-existent file: {}";
    static final String UNABLE_TO_INSTANTIATE_WEBAPP_CLASSLOADER = "Unable to instantiate webapp" +
             " classloader";
    static final String SKIPPING_HADOOP_LIBRARY_BROUGHT_IN_BY_THE_SNAP = "Skipping hadoop " +
            "library brought in by the snap: {}";
    static final String UNABLE_TO_CREATE_WEBAPP_CLASSLOADER = "Unable to create webapp " +
            "classloader";
    static final String UNABLE_TO_EXTRACT_FILE_NAME = "Unable to extract file name from {}";

}