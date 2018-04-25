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

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.snaplogic.snap.api.impl.PropertyValuesImpl;


/**
 * PropertyValuesProvider is the provider implementation that provides {@link PropertyValuesImpl}.
 *
 * @author ksubramanian
 * @since Jul, 2015
 */
public class PropertyValuesProvider implements Provider<PropertyValuesImpl> {

    @Inject
    private Injector injector;

    public PropertyValuesProvider() {}

    @Override
    public PropertyValuesImpl get() {
        return injector.getInstance(PropertyValuesImpl.class);
    }
}
