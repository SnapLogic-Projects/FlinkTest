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

package com.snaplogic.core.execution;

import com.google.common.base.Throwables;
import com.google.inject.Singleton;
import com.snaplogic.core.DriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.snaplogic.core.execution.Messages.GENERATING_ERROR_RESPONSE;

/**
 * DriverExceptionMapper maps the exception to the response message.
 *
 * @author ksubramanian
 * @since Jul, 2015
 */
@Provider
@Singleton
public class DriverExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOG = LoggerFactory.getLogger(DriverExceptionMapper.class);

    @Override
    public Response toResponse(Throwable th) {
        LOG.error(GENERATING_ERROR_RESPONSE, th);
        if (th instanceof ClientErrorException) {
            ClientErrorException ex = (ClientErrorException) th;
            return Response.fromResponse(ex.getResponse())
                    .entity(th.getMessage())
                    .build();
        } else if (th instanceof DriverExecutionException) {
            DriverExecutionException ex = (DriverExecutionException) th;
            return Response.serverError()
                    .entity(ex.getErrors())
                    .build();
        } else if (th instanceof DriverException) {
            DriverException ex = (DriverException) th;
            return Response.serverError()
                    .entity(ex.getMessage())
                    .build();
        }
        return Response.serverError()
                .entity(Throwables.getRootCause(th).getLocalizedMessage())
                .build();
    }
}
