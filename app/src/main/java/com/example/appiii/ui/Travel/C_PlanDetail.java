package com.example.appiii.ui.Travel;

public class C_PlanDetail {
    private int NodeDate;
    private int NodeQueue;
    private String NodeName;
    private Double NodeLat;
    private Double NodeLong;
    private String NodeDescribe;

    public C_PlanDetail(int nodeDate, int nodeQueue, String nodeName, Double nodeLat, Double nodeLong, String nodeDescribe) {
        NodeDate = nodeDate;
        NodeQueue = nodeQueue;
        NodeName = nodeName;
        NodeLat = nodeLat;
        NodeLong = nodeLong;
        NodeDescribe = nodeDescribe;
    }

    public int getNodeDate() {
        return NodeDate;
    }

    public void setNodeDate(int nodeDate) {
        NodeDate = nodeDate;
    }

    public int getNodeQueue() {
        return NodeQueue;
    }

    public void setNodeQueue(int nodeQueue) {
        NodeQueue = nodeQueue;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }

    public Double getNodeLat() {
        return NodeLat;
    }

    public void setNodeLat(Double nodeLat) {
        NodeLat = nodeLat;
    }

    public Double getNodeLong() {
        return NodeLong;
    }

    public void setNodeLong(Double nodeLong) {
        NodeLong = nodeLong;
    }

    public String getNodeDescribe() {
        return NodeDescribe;
    }

    public void setNodeDescribe(String nodeDescribe) {
        NodeDescribe = nodeDescribe;
    }
}
