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

package com.snaplogic.core.stream.url.hdfs;

import java.net.URLClassLoader;

/**
 * ClassLoaderFactory is the factory that helps in assisted injection of
 * {@link URLClassLoader} instances.
 *
 * @author ksubramanian
 * @since Aug, 2015
 */
public interface ClassLoaderFactory {

    /**
     * Creates a HDFS classloader for the given snappack.
     *
     * @param snappackFqid
     * @return classloader
     */
    ClassLoader create(String snappackFqid);
}
