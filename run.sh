#!/bin/bash

flink run
    -c com.snaplogic.core.FlinkDriver \
    target/flink-driver-1.0-SNAPSHOT-DEV.jar \
    -ruuid 04d7be03-68cb-4e59-9429-2499fc070e3d \
    -hdfs_home hdfs:///user/cwang/snaplogic/ \
    -pipeline hdfs:///user/cwang/pipeSpark.json \

