package com.snaplogic.core;

import com.google.inject.Inject;
import com.snaplogic.flink.slmap.Pipeline;
import com.snaplogic.flink.slmap.Snap;
import com.snaplogic.flink.slmap.SnapType;
import com.snaplogic.jpipe.core.graph.PipelineNode;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.table.sources.CsvTableSource;
import org.apache.flink.types.Row;
//import org.apache.flink.table.sources.CsvTableSource;

import java.util.HashMap;
import java.util.Map;

public class PipelineManagerImpl implements PipelineManager{

    private final ExecutionEnvironment flinkEnv;

    public PipelineManagerImpl(ExecutionEnvironment flinkEnv){
        this.flinkEnv = flinkEnv;
    }

    public void prepare(Pipeline pipeline){
        DataSet<Row> data = null;
        Snap snap = pipeline.getHead();
        //Pipeline is empty
        if(snap == null) return;

        do{
            data = generate(snap,data);
            snap = snap.getNext();
        } while(snap.hasNext());
    }

    @Override
    public void prepare(PipelineNode pipelineNode) {

    }

    @Override
    public boolean isPrepared() {
        return false;
    }

    @Override
    public void start() {
        try {
            this.flinkEnv.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void waitForCompletion() {

    }

    @Override
    public void stop() {

    }

    /*************************************Private method****************************************/
    private DataSet<Row> generate(Snap snap,DataSet<Row> data){
        Map<String,Object> settings;
        switch (snap.getType()){
            case SnapType.FILEREADER:
                break;
            case SnapType.CSVPARSER:
                settings = snap.getPre().getSettings();
                Map<String,Object> filePath = (Map<String,Object>)settings.get("filePath");
                String file = (String)filePath.get("value");

                CsvTableSource csvTableSource = new CsvTableSource(file, RowInfo.getFileNames(), RowInfo.fieldTypes,
                        ",", "\n", '"', true, null, false);
                data = csvTableSource.getDataSet(flinkEnv);
                break;
            case SnapType.FILTER:
                JExpression expression = JExpression.getInstance();
                expression.InitializtionEnvData(RowInfo.getFieldMap());
                settings = snap.getSettings();
                Map<String,Object> expressionFilter = (Map<String,Object>)settings.get("filterExpression");
                String expressionText = (String)expressionFilter.get("value");
                expression.setExpression(expressionText);

                data = data.filter(row -> {
                    try {
                        return expression.eval(row);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        return false;
                    }
                });
                break;
            case SnapType.CSVFORMATTER:
                settings = snap.getNext().getSettings();
                Map<String,Object> fileName = (Map<String,Object>)settings.get("filename");
                String value = (String)fileName.get("value");
                data.writeAsFormattedText(value, FileSystem.WriteMode.OVERWRITE, new TextOutputFormat.TextFormatter<Row>(){
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
                });
                break;
            case SnapType.FILEWRITER:
                break;
        }
        return data;
    }

    static class RowInfo{
        public static TypeInformation<?>[] fieldTypes = {BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO};

        private static String headStr = "DRGDefinition,ProviderId,ProviderName,ProviderStreetAddress,ProviderCity," +
                "ProviderState,ProviderZipCode,HospitalReferralRegionDescription, TotalDischarges , " +
                "AverageCoveredCharges , AverageTotalPayments ,AverageMedicarePayments";

        public static String[] getFileNames(){
            String[] header = headStr.split(",");
            String[] fieldNames = new String[header.length];
            for (int i = 0; i < header.length; i++) {
                fieldNames[i] = header[i].trim();
            }
            return fieldNames;
        }

        public static HashMap<String,Object> getFieldMap(){
            String[] header = headStr.split(",");
            HashMap<String,Object> fieldMap = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                fieldMap.put(header[i].trim(), i);
            }
            return fieldMap;
        }
    }
}
