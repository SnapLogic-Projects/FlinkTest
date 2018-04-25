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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import static com.snaplogic.core.schema.Messages.SCHEMA_FQID_URL;

/**
 * SchemaHandler for the driver that will load schemas from the HDFS location.
 *
 * @author ksubramanian
 */
public abstract class DriverSchemaHandler extends URLStreamHandler implements SchemaHandler {

    private static final Logger log = LoggerFactory.getLogger(DriverSchemaHandler.class);

    protected final CoreConfig coreConfig;
    private final Handler handler;

    @Inject
    public DriverSchemaHandler(CoreConfig coreConfig, Handler handler) {
        this.coreConfig = coreConfig;
        this.handler = handler;
    }

    @Override
    public URLConnection openConnection(final SchemaInfo schemaInfo) throws IOException {
        String fqUrl = createSchemaURL(coreConfig.getHdfsSchemaPath(), schemaInfo);
        return getUrlConnection(fqUrl);
    }

    public URLConnection openConnection(final String fqid) throws IOException {
        String fqUrl = createSchemaURL(coreConfig.getHdfsSchemaPath(), fqid);
        return getUrlConnection(fqUrl);
    }

    private URLConnection getUrlConnection(String fqUrl) throws IOException {
        log.debug(SCHEMA_FQID_URL, fqUrl);
        URL url = new URL(null, fqUrl);
        return openConnection(url);
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return url.openConnection();
    }

    /**
     * Creates the schema URL from the hdfs path and schema information
     *
     * @param hdfsUri    as the hdfs URI
     * @param schemaInfo as the schema info
     *
     * @return the URL
     */
    protected abstract String createSchemaURL(String hdfsUri, SchemaInfo schemaInfo);

    /**
     * Creates the schema URL from the hdfs path and fqid passed in
     *
     * @param hdfsUri as the hdfs URI
     * @param fqid    the fqid of the schema
     *
     * @return the URL
     */
    protected abstract String createSchemaURL(String hdfsUri, String fqid);

}