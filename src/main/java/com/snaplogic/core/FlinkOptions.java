package com.snaplogic.core;

import org.apache.commons.cli.Options;

import static com.snaplogic.core.Messages.*;
import static com.snaplogic.core.Messages.PIPELINE_DEFINITION;
import static com.snaplogic.core.StringHelper.*;

public class FlinkOptions {

    public static Options getInstance() {
        Options options = new Options();
        options.addOption(APP_NAME, true, NAME_OF_THE_FLINK_APPLICATION)
                .addOption(JARS, true, JAR_FILES_FOR_THE_FLINK_APPLICATION)
                .addOption(HDFS_HOME, true, SNAPLOGIC_HDFS_HOME)
                .addOption(RUUID, true, PIPELINE_RUUID_FOR_THIS_DRIVER)
                .addOption(PIPELINE, true, PIPELINE_DEFINITION);
        return options;
    }
}
