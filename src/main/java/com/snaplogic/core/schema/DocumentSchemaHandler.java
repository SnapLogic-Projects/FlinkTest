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
import com.snaplogic.schema.model.SchemaInfo;
import com.snaplogic.schema.resolver.SchemaHandler;

import java.io.IOException;
import java.net.URLConnection;

/**
 * Suggest operation is not supported by spark driver. This implementation will just throw an
 * exception if used.
 *
 * @author ksubramanian
 */
public class DocumentSchemaHandler implements SchemaHandler.DocumentSchemaResolver {

    @Inject
    public DocumentSchemaHandler() {
    }

    @Override
    public URLConnection openConnection(SchemaInfo schemaInfo)
            throws IOException {
        throw new IOException(Messages.UNSUPPORTED_SCHEMA_OPERATION);
    }
}