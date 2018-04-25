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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.snaplogic.core.Messages.REGISTERING_DRIVER_WITH_JCC;

/**
 * RegistrationManager handles the driver registration with the jcc.
 *
 * @author ksubramanian
 * @since Jul, 2015
 */
public class RegistrationManager {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationManager.class);
    protected static final String API_REGISTER_DRIVER = "%s/api/1/rest/spark/register-driver";
    protected static final String RUUID = "ruuid";
    protected static final String DRIVER_URL = "driver_url";
    protected static final String APPLICATION_JSON = "application/json";
    protected static final String APPLICATION_ID = "application_id";

    private final String jccUrl;
    private final String ruuid;
    private final HttpClient httpClient;
    private final CoreConfig coreConfig;
    private final ObjectMapper objectMapper;

    @Inject
    public RegistrationManager(@Named(DriverConstants.JCC_URL)String jccUrl,
            @Named(DriverConstants.RUUID) String ruuid, HttpClient httpClient,
            CoreConfig coreConfig, ObjectMapper objectMapper) {
        this.jccUrl = jccUrl;
        this.ruuid = ruuid;
        this.httpClient = httpClient;
        this.coreConfig = coreConfig;
        this.objectMapper = objectMapper;
    }

    /**
     * Registers the driver with the JCC.
     */
    public void register(String applicationID) {
        try {
            String apiUrl = String.format(API_REGISTER_DRIVER, jccUrl);
            HttpPost httpPost = new HttpPost(apiUrl);
            LOG.info(REGISTERING_DRIVER_WITH_JCC, apiUrl);
            Map<String, String> postData = new HashMap<>(3);
            postData.put(RUUID, ruuid);
            postData.put(DRIVER_URL, coreConfig.getServerUri());
            postData.put(APPLICATION_ID,applicationID);
            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(postData));
            entity.setContentType(APPLICATION_JSON);
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                throw new DriverException(Messages.DRIVER_REGISTRATION_FAILED, jccUrl,
                        statusLine.getReasonPhrase());
            }
            LOG.info(Messages.REGISTRATION_SUCCESSFUL);
        } catch (IOException e) {
            throw new DriverException(e, Messages.UNABLE_TO_REGISTER_DRIVER_WITH_JCC, jccUrl);
        }
    }
}
