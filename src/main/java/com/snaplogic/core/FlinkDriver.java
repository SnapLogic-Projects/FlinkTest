package com.snaplogic.core;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.snaplogic.flink.slmap.GraphBuildHelper;
import com.snaplogic.flink.slmap.Pipeline;
import com.snaplogic.flink.slmap.Snap;
import com.snaplogic.snap.schema.util.JsonSchemaConstants;
import com.snaplogic.util.YarnUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.commons.cli.*;
import org.apache.flink.table.sources.CsvTableSource;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.PropertyConfigurator;

import static com.snaplogic.core.Messages.*;
import static com.snaplogic.core.StringHelper.*;


import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlinkDriver {

    private static final Logger LOG = LoggerFactory.getLogger(FlinkDriver.class);
    private static final String LOG4J_CONF = "log4j-sl-driver.properties";

    private static final Options OPTIONS = FlinkOptions.getInstance();


    public static void main(String[] args) {
        Pipeline pipeline = null;
        //TODO: log4j
        PropertyConfigurator.configure(FlinkDriver.class.getClassLoader().getResourceAsStream(LOG4J_CONF));

        //TODO: parse and check parameters
        LOG.info(PARSING_COMMAND_LINE_ARGS, StringUtils.join(args, COMMA));
        CommandLine cliParser = parseArgs(args);
        checkArgs(cliParser);

        //TODO: prepare pipeline and guice dependencies
        try {
            pipeline = preparePipeline(cliParser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO: prepare flink
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //TODO: flink execution
        //flinkExeTest(env,"hdfs://localhost:9000/user/yliu/test_big.csv","hdfs://localhost:9000/user/yliu/testOutput.csv");
        PipelineManagerImpl pipelineManager = new PipelineManagerImpl(env);
        pipelineManager.prepare(pipeline);
        pipelineManager.start();

    }

    /*--------------------------------------Private Methods----------------------------------------*/

    private static Pipeline preparePipeline(CommandLine cliParser) throws IOException {
        //Get files from HDFS
        final String pipelineFile = cliParser.getOptionValue(PIPELINE);
        Path pipelinePath = new Path(pipelineFile);
        FileSystem fileSystem = FileSystem.get(pipelinePath.toUri(), YarnUtil.getYarnConfiguration());
        final FSDataInputStream inputStream = fileSystem.open(pipelinePath);
        //Map data into java object
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> executeData = mapper.readValue(inputStream, Map.class);
        //Graph build

        Map<String, Object > pipeDef = GraphBuildHelper.getPipeDef(executeData);
        Map<String, Object> snapMap = (Map<String,Object>)pipeDef.get(JsonSchemaConstants.SNAP_MAP);
        List<Snap> snaps = new ArrayList<>();
        for(Map.Entry<String,Object> e : snapMap.entrySet()){
            snaps.add(GraphBuildHelper.buildSnap((Map<String,Object>)e.getValue()));
        }
        return GraphBuildHelper.buildPipeline(snaps,(Map<String,Object>)pipeDef.get(GraphBuildHelper.LINK_MAP));

    }

    private static CommandLine parseArgs(final String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException(NO_ARGUMENT_PROVIDED_FOR_FLINK_DRIVER);
        }

        CommandLine cliParser = null;
        try {
            cliParser = new GnuParser().parse(OPTIONS, args);
        } catch (ParseException e) {
            throw new DriverException(e, ERROR_PARSING_COMMAND_LINE_OPTS, StringUtils.join(args, COMMA));
        }
        return cliParser;
    }

    private static void checkArgs(CommandLine cliParser) {
        if (cliParser.hasOption(HELP)) {
            new HelpFormatter().printHelp(FlinkDriver.class.getSimpleName(), OPTIONS);
            System.exit(0);
        }
        checkBlank(RUUID, PIPELINE_RUUID_NOT_PROVIDED);
        checkBlank(HDFS_HOME, SNAPLOGIC_DRIVER_HOME_NOT_PROVIDED);
        checkBlank(PIPELINE, PIPELINE_FILE_NOT_PROVIDED);
    }

    private static void checkBlank (String opt, String exception) {
        if (StringUtils.isBlank(opt)) {
            throw new DriverException(exception);
        }
    }

    /*--------------------------------------Flink Test----------------------------------------*/

    private static void flinkExeTest(ExecutionEnvironment env,String inputFile,String outputFile) {
        CsvTableSource csvTableSource = new CsvTableSource(inputFile, PipelineManagerImpl.RowInfo.getFileNames(), PipelineManagerImpl.RowInfo.fieldTypes,
                ",", "\n", '"', true, null, false);

        JExpression expression = JExpression.getInstance();
        expression.InitializtionEnvData(PipelineManagerImpl.RowInfo.getFieldMap());


        String expressionText = "$ProviderState == 'AL'";
        expression.setExpression(expressionText);

        DataSet<Row> data = csvTableSource.getDataSet(env);
        DataSet<Row> filterdata = data.filter(row -> {
            try {
                return expression.eval(row);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return false;
            }
        });
        filterdata.writeAsFormattedText(outputFile, org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE, new TextOutputFormat.TextFormatter<Row>(){
            @Override
            public String format(Row record) {
                return record.getField(0) + "|"
                        + record.getField(1) + "|"
                        + record.getField(2) + "|"
                        + record.getField(3) + "|"
                        + record.getField(4) + "|"
                        + record.getField(5) + "|"
                        + record.getField(6) + "|"
                        + record.getField(7) + "|"
                        + record.getField(8) + "|"
                        + record.getField(9) + "|"
                        + record.getField(10) + "|"
                        + record.getField(11);
            }
        }).setParallelism(1);
        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            // normalize and split the line
            String[] tokens = value.toLowerCase().split("\\W+");

            // emit the pairs
            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<String, Integer>(token, 1));
                }
            }
        }
    }
}
