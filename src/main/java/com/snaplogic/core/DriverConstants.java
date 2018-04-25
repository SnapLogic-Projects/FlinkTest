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
 * DriverConstants holds all the constants used by the driver.
 *
 * @author ksubramanian
 * @since Jul, 2015
 */
public class DriverConstants {
    // Guice related constants
    public static final String DRIVER_HOME_URL = "driver_home_url";
    public static final String JCC_URL = "jcc_url";
    public static final String RUUID = "ruuid";

    // Visitors
    public static final String VIEW_SETUP_VISITOR = "view_setup_visitor";
    public static final String PREPARE_VISITOR = "prepare_visitor";
    public static final String EXECUTION_VISITOR = "execution_visitor";

    // Other constants
    public static final String FLINK_MODE = "flink_mode";
    public static final String PROXY_USER = "proxy_user";
    public static final String STATE = "state";
    public static final String ERROR_MSG = "error_msg";
    public static final String REASON = "reason";
    public static final String RESOLUTION = "resolution";
    public static final String SNAP_MAP = "snap_map";
}
