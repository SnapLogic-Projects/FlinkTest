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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.snaplogic.cc.security.UserGroupInfoUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * CoreConfig is the class that holds all the core properties assigned to this driver.
 *
 * @author ksubramanian
 * @since Jul, 2015
 */
public class CoreConfig {
    private static final String RUN = "run";
    private static final String LIB = "lib";
    private static final String SNAPPACK = "snappack";
    private static final String SCHEMA = "schema";
    private static final String COMMON = "common";
    private static final String JDBC = "jdbc";

    private final String jccUrl;
    private final String driverHomeUrl;
    private final String ruuid;
    private final String hdfsSnappackPath;
    private final String hdfsSchemaPath;
    private final String hdfsJdbcStorePath;
    private final String hdfsCommonLibPath;
    private final String proxyUser;
    private final Configuration configuration;
    private final FileSystem fileSystem;

    private List<URL> driverUrls = new LinkedList<>();

    // We know the server uri only after the jetty server comes up. So, this one has to be set
    // lazily.
    private volatile String serverUri;

    @Inject
    public CoreConfig(@Named(DriverConstants.JCC_URL) String jccUrl,
            @Named(DriverConstants.DRIVER_HOME_URL) String driverHomeUrl,
            @Named(DriverConstants.RUUID) String ruuid,
            @Named(DriverConstants.PROXY_USER) String proxyUser,
            Configuration configuration, FileSystem fileSystem) {
        this.jccUrl = jccUrl;
        this.driverHomeUrl = driverHomeUrl;
        this.ruuid = ruuid;
        Path run = new Path(driverHomeUrl, RUN);
        Path lib = new Path(run, LIB);
        this.hdfsSnappackPath = new Path(lib, SNAPPACK).toString();
        this.hdfsSchemaPath = new Path(lib, SCHEMA).toString();
        this.hdfsCommonLibPath = new Path(lib, COMMON).toString();
        this.hdfsJdbcStorePath = new Path(lib, JDBC).toString();
        this.proxyUser = proxyUser;
        this.configuration = configuration;
        this.fileSystem = fileSystem;
    }

    public String getJccUrl() {
        return jccUrl;
    }

    public String getRuuid() {
        return ruuid;
    }

    public String getServerUri() {
        return serverUri;
    }

    public String getDriverHomeUrl() {
        return this.driverHomeUrl;
    }

    public String getHdfsSchemaPath() {
        return hdfsSchemaPath;
    }

    public String getHdfsJdbcStorePath() {
        return hdfsJdbcStorePath;
    }

    public String getHdfsSnappackPath() {
        return hdfsSnappackPath;
    }

    public String getHdfsCommonLibPath() {
        return hdfsCommonLibPath;
    }

    public boolean isProxyUser() {
        return StringUtils.isNotBlank(proxyUser);
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public UserGroupInformation getUserGroupInformation() throws IOException {
        final UserGroupInformation user;
        if (isProxyUser()) {
            user = UserGroupInfoUtil.getActiveUserGroupInfo(getProxyUser());
        } else {
            user = UserGroupInformation.getLoginUser();
        }
        return user;
    }

    public List<URL> getDriverUrls() {
        return driverUrls;
    }

    public void setDriverUrls(final List<URL> urls) {
        this.driverUrls.addAll(urls);
    }
}
