package com.snaplogic.flink.slmap;

import com.snaplogic.snap.schema.util.JsonSchemaConstants;

import java.util.List;
import java.util.Map;

public class GraphBuildHelper {

    public static final String SETTINGS = "settings";
    public static final String SRC_ID = "src_id";
    public static final String DST_ID = "dst_id";
    public static final String LINK_MAP = "link_map";
    public static final String INFO = "info";
    private GraphBuildHelper(){}

    /**
     * Give a Map definition of each snap, return a java object instance
     * @param snapInstance
     * @return
     */
    public static Snap buildSnap(Map<String, Object> snapInstance){
        Snap snap = new Snap();
        snap.setInstanceId((String)snapInstance.get(JsonSchemaConstants.SNAP_INSTANCE_ID));
        snap.setClassId((String)snapInstance.get(JsonSchemaConstants.SNAP_CLASS_ID));
        Map<String,Object> propertyMap = (Map<String,Object>)snapInstance.get(JsonSchemaConstants.SNAP_PROPERTY_MAP);
        snap.setPropertyMap(propertyMap);

        snap.setInfo((Map<String,Object>)propertyMap.get(INFO));
        snap.setInput((Map<String,Object>)propertyMap.get(JsonSchemaConstants.INPUT));
        snap.setSettings((Map<String,Object>)propertyMap.get(SETTINGS));
        snap.setError((Map<String,Object>)propertyMap.get(JsonSchemaConstants.ERROR));
        snap.setOutput((Map<String,Object>)propertyMap.get(JsonSchemaConstants.OUTPUT));

        return snap;
    }

    public static Pipeline buildPipeline(List<Snap> snaps,Map<String,Object> linkMap){
        Pipeline pipeline = new Pipeline();
        for(Snap s : snaps){
            pipeline.putSnap(s.getInstanceId(),s);
        }

        String index = "link10";
        for(int i = 0 ;i<linkMap.size();i++){
            Map<String, Object> link = (Map<String,Object>)linkMap.get(index+i);
            String src = (String)link.get(SRC_ID);
            String des = (String)link.get(DST_ID);

            Snap srcSnap = pipeline.getSnap(src);
            Snap desSnap = pipeline.getSnap(des);

            srcSnap.setNext(desSnap);
            desSnap.setPre(srcSnap);

            if(i == 0){
                pipeline.setHead(pipeline.getSnap(src));
            }
            else if(i ==linkMap.size()-1 ){
                pipeline.setTail(pipeline.getSnap(des));
            }
        }

        return pipeline;
    }

    /**
     * This works on standard pipeline json ONLY!
     * @param executeData
     * @return
     */
    public static Map<String,Object> getPipeDef(Map<String,Object> executeData){
        Map<String,Object> responseMap = (Map<String,Object>)executeData.get(JsonSchemaConstants.RESPONSE_MAP);
        Map<String, Object> pipe = (Map<String, Object>)responseMap.get(JsonSchemaConstants.PIPE_DEFS);
        Map<String,Object> pipeDef = (Map<String, Object>)pipe.values().toArray()[0];

        return pipeDef;
    }
}
