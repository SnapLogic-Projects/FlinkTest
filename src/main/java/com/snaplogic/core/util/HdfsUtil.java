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

import com.google.inject.Inject;
import com.snaplogic.core.CoreConfig;
import com.snaplogic.core.DriverException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.security.PrivilegedExceptionAction;
import java.util.LinkedList;
import java.util.List;

import static com.snaplogic.core.util.Messages.FAILED_TO_LOAD_JDBC_DRIVER_FILES;
import static com.snaplogic.core.util.Messages.LOADING_JDBC_DRIVER_FILES;

/**
 * Utility class for HDFS related methods.
 */
public class HdfsUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HdfsUtil.class);
    public static final String DELETING_HDFS_JDBC_STORE = "Deleting HDFS JDBC store {}";

    private final CoreConfig coreConfig;

    @Inject
    public HdfsUtil(final CoreConfig coreConfig) {
        this.coreConfig = coreConfig;
    }

    /**
     * Delete HDFS JDBC Store.
     */
    public void cleanupHdfsJdbcStore() {
        String hdfsJdbcStorePath = coreConfig.getHdfsJdbcStorePath();
        final String ruuid = coreConfig.getRuuid();
        final Path jdbcStore = new Path(hdfsJdbcStorePath, ruuid);
        try {
            UserGroupInformation userGroupInformation = coreConfig.getUserGroupInformation();
            userGroupInformation.doAs(new PrivilegedExceptionAction<Void>() {
                @Override
                public Void run() throws Exception {
                    LOG.info(DELETING_HDFS_JDBC_STORE, jdbcStore.toString());
                    coreConfig.getFileSystem().delete(jdbcStore, true);
                    return null;
                }
            });
        } catch (IOException | InterruptedException e) {
            throw new DriverException(e, FAILED_TO_LOAD_JDBC_DRIVER_FILES, jdbcStore.toString());
        }
    }
}
