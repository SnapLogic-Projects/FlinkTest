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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.snaplogic.schema.resolver.SchemaHandler;
import com.snaplogic.schema.validator2.SchemaRegistry;
import com.snaplogic.schema.validator2.ValidatorRegistry;

/**
 * Provides schema handler which loads schema from the snaplogic HDFS home directory.
 *
 * @author ksubramanian
 */
public class SchemaLocatorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SchemaHandler.CommonSchemaResolver.class).to(CommonSchemaHandler.class);
        bind(SchemaHandler.DocumentSchemaResolver.class).to(DocumentSchemaHandler.class);
        bind(SchemaHandler.SnapSchemaResolver.class).to(SnapSchemaHandler.class);
        bind(SchemaHandler.AccountSchemaResolver.class).to(AccountSchemaHandler.class);
        bind(DriverSchemaSource.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public SchemaRegistry provideSchemaRegistry(DriverSchemaSource source) {
        return new SchemaRegistry()
                .withSchemaSource(source);
    }

    /**
     * Provides a ValidatorRegistry that reads schemas from the HDFS location.
     *
     * @param registry The source for JSON Schemas.
     * @return An initialized ValidatorRegistry.
     */
    @Provides
    @Singleton
    public ValidatorRegistry provideValidatorRegistry(SchemaRegistry registry) {
        return new ValidatorRegistry()
                .withSchemaRegistry(registry);
    }
}