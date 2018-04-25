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

import java.util.HashMap;
import java.util.Map;

/**
 * DriverException is the exception thrown by the driver when it encounters some issue.
 *
 * @author ksubramanian
 * @since Jul, 2015
 */
public class DriverExecutionException extends RuntimeException {

    private transient final Map<String, Object> errors = new HashMap<>();

    public DriverExecutionException(Throwable throwable, Map<String, Object> errorMap,
            String format, String... args) {
        super(format != null ? String.format(format, args) : "", throwable);
        this.errors.clear();
        this.errors.putAll(errorMap);
    }

    public DriverExecutionException(Map<String, Object> errorMap, String format, String... args) {
        super(format != null ? String.format(format, args) : "");
        this.errors.clear();
        this.errors.putAll(errorMap);
    }

    /**
     * Returns the error map.
     *
     * @return errorMap
     */
    public Map<String, Object> getErrors() {
        return errors;
    }
}
