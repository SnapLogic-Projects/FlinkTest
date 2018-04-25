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
import com.snaplogic.core.stream.url.hdfs.Handler;
import com.snaplogic.schema.model.SchemaInfo;
import com.snaplogic.schema.resolver.SchemaHandler;

/**
 * Represents the handler to load snap schemas from HDFS path.
 *
 * @author ksubramanian
 */
public class SnapSchemaHandler extends DriverSchemaHandler implements SchemaHandler
        .SnapSchemaResolver {
    private static final String SCHEMA_FETCH_URL_FQID = "%s/snaps/%s.schema";

    @Inject
    public SnapSchemaHandler(CoreConfig coreConfig, Handler handler) {
        super(coreConfig, handler);
    }

    @Override
    public String createSchemaURL(final String hdfsUri, final SchemaInfo schemaInfo) {
        return String.format(SCHEMA_FETCH_URL_FQID, hdfsUri, schemaInfo.getSchemaFqid());
    }

    @Override
    protected String createSchemaURL(String hdfsUri, String fqid) {
        return String.format(SCHEMA_FETCH_URL_FQID, hdfsUri, fqid);
    }
}