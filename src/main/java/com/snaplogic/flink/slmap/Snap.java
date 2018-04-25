package com.snaplogic.flink.slmap;

import java.util.Map;

public class Snap {
    private String instanceId;
    private String classId;
    private Map<String,Object> propertyMap;
    private Map<String,Object> info;
    private Map<String,Object> input;
    private Map<String,Object> settings;
    private Map<String,Object> error;
    private Map<String,Object> output;

    private Snap Pre,Next;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Map<String, Object> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, Object> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }

    public Map<String, Object> getOutput() {
        return output;
    }

    public void setOutput(Map<String, Object> output) {
        this.output = output;
    }

    public Snap getPre() {
        return Pre;
    }

    public void setPre(Snap pre) {
        Pre = pre;
    }

    public Snap getNext() {
        return Next;
    }

    public void setNext(Snap next) {
        Next = next;
    }

    public boolean hasNext(){
        if(this.Next!=null) return true;
        return false;
    }

    public String getType(){
        return this.classId;
    }
}
