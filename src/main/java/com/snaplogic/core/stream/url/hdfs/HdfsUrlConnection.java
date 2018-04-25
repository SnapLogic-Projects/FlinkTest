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

import com.snaplogic.common.services.Disconnectable;
import com.snaplogic.common.url.protocol.UrlUtils;
import com.snaplogic.common.utilities.URLUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.PrivilegedExceptionAction;

import static com.snaplogic.core.stream.url.hdfs.Messages.*;

/**
 * This is the hdfs connection object that is returned by the
 * {@link Handler#openConnection(URL)}
 *
 * @author psung (original author), ksubramanian
 */
public class HdfsUrlConnection extends URLConnection implements Disconnectable {
    public static final String DFS_NAMESERVICES = "dfs.nameservices";
    private final FileSystem hdfs;
    private final String host;
    private final int port;
    private final UserGroupInformation loginUser;

    /**
     * Checks if the given url contains a correct hdfs protocol.
     *
     * @param url
     */
    public HdfsUrlConnection(FileSystem fileSystem, Configuration configuration, URL url,
            UserGroupInformation loginUser) throws IOException {
        super(url);
        this.hdfs = fileSystem;
        this.loginUser = loginUser;
        host = url.getHost();
        port = url.getPort();
        // Check if url includes host name.
        if ((host == null)) {
            throw new MalformedURLException(String.format(ERR_INVALID_URL,
                    URLUtils.getFileNoAuth(url.toString())));
        }
        checkForNamenodeService(configuration, host);
    }

    @Override
    public void connect() throws IOException {
        // Do nothing.
    }

    @Override
    public InputStream getInputStream() throws IOException {
        // TODO psung: enforce access control to make sure user has permission to read from the file
        // Specify File Path of Hadoop File System
        final Path filePath = new Path(UrlUtils.toURI(url).getPath());
        FSDataInputStream fsDataInputStream;
        // Return HDFS file input stream
        try {
            fsDataInputStream = loginUser.doAs(
                    new PrivilegedExceptionAction<FSDataInputStream>() {
                        @Override
                        public FSDataInputStream run() throws IOException {
                            FSDataInputStream newStream;
                            try {
                                newStream = hdfs.open(filePath);
                            } catch (Exception e) {
                                // Add Tokens and Try once more
                                Handler.addTokensToRMNM(hdfs, hdfs.getConf());
                                newStream = hdfs.open(filePath);
                            }
                            if (newStream == null) {
                                throw new IOException(TOKEN_FILE_ERROR);
                            }
                            return newStream;
                        }
                    });
        } catch (InterruptedException e) {
            throw new IOException(INTERRUPTED_WHILE_OPENING_FILE, e);
        }

        if (fsDataInputStream == null) {
            throw new IOException(HDFS_OPEN_FAILURE);
        }
        return new BufferedInputStream(fsDataInputStream);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        final Path filePath = new Path(UrlUtils.toURI(url).getPath());
        BufferedOutputStream bufferedOutputStream;
        try {
            bufferedOutputStream = loginUser.doAs(
                    new PrivilegedExceptionAction<BufferedOutputStream>() {
                        @Override
                        public BufferedOutputStream run() throws IOException {
                            BufferedOutputStream bos;
                            try {
                                bos = attemptHdfsCreate(filePath);
                            } catch (Exception e) {
                                Handler.addTokensToRMNM(hdfs, hdfs.getConf());
                                bos = attemptHdfsCreate(filePath);
                            }
                            if (bos == null) {
                                throw new IOException(HDFS_OUTPUT_STREAM_SECURE);
                            }
                            return bos;
                        }
                    });
        } catch (InterruptedException e) {
            throw new IOException(INTERRUPTED_WHILE_DELETING_FILE, e);
        }

        if (bufferedOutputStream == null) {
            throw new IOException(HDFS_OUTPUT_STREAM);
        }
        return bufferedOutputStream;
    }

    private BufferedOutputStream attemptHdfsCreate(final Path path) throws IOException {
        if (hdfs.exists(path)) {
            hdfs.delete(path, true);
        }
        return new BufferedOutputStream(hdfs.create(path));
    }

    @Override
    public void disconnect() {
        // fs.close() is known to cause intermittent problems and no need to close it
    }

    public FileSystem getHadoopFileSystem() {
        return hdfs;
    }

    //----------------------------------- Private Methods --------------------------------------//
    private void checkForNamenodeService(Configuration configuration, String host) throws
            MalformedURLException {
        String nameservice = configuration.get(DFS_NAMESERVICES);


        if (nameservice == null) {
            return;
        }

        String[] nameserviceParts = nameservice.split(",");
        for (int i = 0; i < nameserviceParts.length; i++) {
            nameserviceParts[i] = nameserviceParts[i].trim();
        }

        final String hostTrimmed = host.trim();
        if (StringUtils.isBlank(nameservice) ||
                ArrayUtils.contains(nameserviceParts, hostTrimmed) == false) {
            throw new MalformedURLException(String.format(ERR_INVALID_URL,
                    URLUtils.getFileNoAuth(url.toString())));
        }
    }
}
