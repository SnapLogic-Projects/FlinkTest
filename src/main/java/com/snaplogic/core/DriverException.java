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

/**
 * DriverException is the exception thrown by the driver when it encounters some issue.
 *
 * @author ksubramanian
 * @since Jul, 2015
 */
public class DriverException extends RuntimeException {

    public DriverException(String format, String... args) {
        super(String.format(format, args));
    }

    public DriverException(Throwable cause, String format, String... args) {
        super(String.format(format, args), cause);
    }

    public DriverException(Throwable cause) {
        super(cause);
    }
}
