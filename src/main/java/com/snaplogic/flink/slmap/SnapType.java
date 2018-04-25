package com.snaplogic.flink.slmap;

public class SnapType {
    private SnapType(){}

    public static final String FILEREADER = "com-snaplogic-snaps-binary-simpleread";
    public static final String FILEWRITER = "com-snaplogic-snaps-binary-write";
    public static final String CSVPARSER = "com-snaplogic-snaps-transform-csvparser";
    public static final String CSVFORMATTER = "com-snaplogic-snaps-transform-csvformatter";
    public static final String ROUTER = "com-snaplogic-snaps-flow-router";
    public static final String SORT = "com-snaplogic-snaps-transform-sort";
    public static final String FILTER = "com-snaplogic-snaps-flow-filter";

}
