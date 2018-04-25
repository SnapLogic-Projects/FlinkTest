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
package com.snaplogic.core.schema;

import com.google.inject.Inject;
import com.snaplogic.core.CoreConfig;
import com.snaplogic.core.DriverException;
import com.snaplogic.core.stream.url.hdfs.Handler;
import com.snaplogic.schema.model.SchemaInfo;
import com.snaplogic.schema.resolver.SchemaHandler;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;

/**
 * Provides functionality to load common schemas from HDFS
 *
 * @author ksubramanian
 */
public class CommonSchemaHandler extends DriverSchemaHandler implements SchemaHandler
        .CommonSchemaResolver {
    private static final String SCHEMA_FETCH_URL = "%s/common/%s_%d.schema";
    private static final String SCHEMA_FETCH_URL_FQID = "%s/common/%s.schema";

    @Inject
    public CommonSchemaHandler(CoreConfig coreConfig, Handler handler) {
        super(coreConfig, handler);
    }

    @Override
    protected String createSchemaURL(String hdfsUri, SchemaInfo schemaInfo) {
        try {
            return String.format(SCHEMA_FETCH_URL, hdfsUri,
                    URIUtil.encodePath(schemaInfo.getSchemaId()), schemaInfo.getSchemaVersion());
        } catch (URIException e) {
            throw new DriverException(e, Messages.INVALID_PATH, schemaInfo.getSchemaFqid());
        }
    }

    @Override
    protected String createSchemaURL(String hdfsUri, String fqid) {
        return String.format(SCHEMA_FETCH_URL_FQID, hdfsUri, fqid);
    }

}