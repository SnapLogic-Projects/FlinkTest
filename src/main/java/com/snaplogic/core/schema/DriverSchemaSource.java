/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2017, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.core.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.schema.resolver.SchemaHandler;
import com.snaplogic.schema.validator2.SchemaSource;
import com.snaplogic.snap.properties.provider.SnapPropertiesReaderProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;


/**
 * Looks up the a schema by fqid and returns the deserialized version.
 */
public class DriverSchemaSource implements SchemaSource {

    private static final Logger log = LoggerFactory.getLogger(DriverSchemaSource.class);
    private static final int TYPE_PART = 0;
    private static final int ID_PART = 1;
    private static final String SNAP_PREFIX = "snap";
    private static final String ACCOUNT_PREFIX = "account";
    private static final String SHORT_SEPARATOR = "/";
    private static final int EXPECTED_PARTS = 2;
    private static final String SNAP_PREFIX_LONG = "com-snaplogic-snaps";
    private static final String ACCOUNT_PREFIX_LONG = "com-snaplogic-account";

    private static final String INVALID_FQID_LENGTH = "Invalid fqid: %s length was %d";
    private static final String USING_RESOLVER_AND_SCHEMA_FQID =
            "Using resolver {} and schema fqid {}";

    private final DriverSchemaHandler snapSchemaResolver;
    private DriverSchemaHandler accountSchemaResolver;
    private DriverSchemaHandler commonSchemaResolver;
    private final ObjectMapper mapper;

    @Inject
    public DriverSchemaSource(SchemaHandler.SnapSchemaResolver snapSchemaResolver,
                              SchemaHandler.AccountSchemaResolver accountSchemaResolver,
                              SchemaHandler.CommonSchemaResolver commonSchemaResolver,
                              SnapPropertiesReaderProvider mapperProvider) {
        this.snapSchemaResolver = (DriverSchemaHandler) snapSchemaResolver;
        this.accountSchemaResolver = (DriverSchemaHandler) accountSchemaResolver;
        this.commonSchemaResolver = (DriverSchemaHandler) commonSchemaResolver;
        this.mapper = mapperProvider.get();
    }

    @Override
    public JsonNode lookupSchema(String fqid) {
        String schemaId;
        DriverSchemaHandler resolver;
        if (fqid.startsWith(SNAP_PREFIX) || fqid.startsWith(ACCOUNT_PREFIX)) {
            String[] fqidParts = fqid.split(SHORT_SEPARATOR, EXPECTED_PARTS);
            if (fqidParts.length != EXPECTED_PARTS) {
                String message = String.format(INVALID_FQID_LENGTH, fqid, fqidParts.length);
                throw new ExecutionException(message).withResolutionAsDefect();
            }
            schemaId = fqidParts[ID_PART];

            if (fqidParts[TYPE_PART].equals(SNAP_PREFIX)) {
                resolver = this.snapSchemaResolver;
            } else {
                resolver = this.accountSchemaResolver;
            }
        } else if (fqid.startsWith(SNAP_PREFIX_LONG)) {
            schemaId = fqid;
            resolver = this.snapSchemaResolver;
        } else if (fqid.startsWith(ACCOUNT_PREFIX_LONG)) {
            schemaId = fqid;
            resolver = this.accountSchemaResolver;
        } else {
            schemaId = fqid;
            resolver = this.commonSchemaResolver;
        }

        log.debug(USING_RESOLVER_AND_SCHEMA_FQID, resolver, schemaId);

        try (InputStream is = resolver.openConnection(schemaId).getInputStream()) {
            return mapper.readTree(is);
        } catch (IOException e) {
              throw new ExecutionException(
                      e, com.snaplogic.snap.schema.util.Messages.IO_ERROR, fqid)
                      .withReason(Throwables.getRootCause(e).getMessage())
                      .withResolutionAsDefect();
          }
    }
}
