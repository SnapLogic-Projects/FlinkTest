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

import com.google.inject.Inject;
import com.snaplogic.core.CoreConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * This class provides the url connection for the hdfs url.
 * Please note that the class name and the package name should remain the same. JVM expects the
 * url stream handler class to have the name Handler. Also the last part of the package name should
 * be the url protocol scheme.
 *
 * @author psung (original author), ksubramanian
 */
public class Handler extends URLStreamHandler {
    private static final Logger LOG = LoggerFactory.getLogger(HdfsUrlConnection.class);

    private final CoreConfig coreConfig;
    private final Configuration defaultConfiguration;

    @Inject
    public Handler(final CoreConfig coreConfig) {
        this.coreConfig = coreConfig;
        this.defaultConfiguration = setupTokens(coreConfig.getFileSystem(),
                coreConfig.getConfiguration());
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        UserGroupInformation user = coreConfig.getUserGroupInformation();
        return new HdfsUrlConnection(coreConfig.getFileSystem(), defaultConfiguration, url, user);
    }

    /**
     * Public method that allows creation of urlconnection object.
     *
     * @param url
     * @return urlConnection
     * @throws IOException
     */
    public URLConnection getConnection(URL url) throws IOException {
        return openConnection(url);
    }

    /**
     * Adds tokens for RM and NM.
     * @param hdfs
     * @param conf
     * @throws IOException
     */
    public static void addTokensToRMNM(FileSystem hdfs, Configuration conf)
            throws IOException {
        String tokenRenewer = conf.get(YarnConfiguration.RM_PRINCIPAL);
        if (StringUtils.isNotBlank(tokenRenewer)) {
            addTokensTo(hdfs, tokenRenewer);
        }
        tokenRenewer = conf.get(YarnConfiguration.NM_PRINCIPAL);
        if (StringUtils.isNotBlank(tokenRenewer)) {
            addTokensTo(hdfs, tokenRenewer);
        }
    }

    //---------------------------------- Private Methods ----------------------------------------//

    /**
     * Sets the default configuration for this handler.
     *
     * @param conf
     */
    private Configuration setupTokens(FileSystem hdfs, Configuration conf) {
        // If security is enabled we want to set delegation tokens in the configuration
        try {
            if (UserGroupInformation.isSecurityEnabled()) {
                addTokensToRMNM(hdfs, conf);
            }
        } catch (IOException e) {
            LOG.error(Messages.DELEGATION_TOKEN_RETRIEVAL_FAILED, e);
        }
        return conf;
    }

    private static void addTokensTo(FileSystem hdfs, String tokenRenewer) throws IOException {
        // For now, only getting tokens for the default file-system.
        UserGroupInformation loginUser = UserGroupInformation.getLoginUser();
        Credentials credentials = loginUser.getCredentials();
        final Token<?>[] tokens = hdfs.addDelegationTokens(tokenRenewer, credentials);
        if (tokens != null) {
            LOG.info(Messages.RENEWED_DELEGATION_TOKEN, tokenRenewer, tokens.length);
            for (Token<?> token : tokens) {
                LOG.info(Messages.GOT_DELEGATION_TOKEN, hdfs.getUri(), token);
                credentials.addToken(new Text(token.getIdentifier()), token);
            }
        } else {
            LOG.warn(Messages.UNABLE_TO_RENEW_DELEGATION_TOKEN, tokenRenewer);
        }
        loginUser.addCredentials(credentials);
    }
}
