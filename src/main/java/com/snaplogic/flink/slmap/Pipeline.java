package com.snaplogic.flink.slmap;

import java.util.HashMap;
import java.util.Map;

public class Pipeline {
    private Snap head;
    private Snap tail;
    private Map<String, Snap> snaps = new HashMap<>();


    public void putSnap(String snapId,Snap snap){
        snaps.put(snapId,snap);
    }

    public Snap getSnap(String snapId){
        return snaps.get(snapId);
    }

    public Snap getHead() {
        return head;
    }

    public void setHead(Snap head) {
        this.head = head;
    }

    public Snap getTail() {
        return tail;
    }

    public void setTail(Snap tail) {
        this.tail = tail;
    }
}
