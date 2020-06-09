package com.example.appiii;

public class C_PlanInfo {

    private int pNodeDate;
    private int pNodeQueue;
    private String pNodeName;
    private Double pNodeLat;
    private Double pNodeLong;
    private String pNodeDescribe;
    private String pNodeType;

    public C_PlanInfo(int pNodeDate, int pNodeQueue, String pNodeName, Double pNodeLat, Double pNodeLong, String pNodeDescribe, String pNodeType) {
        this.pNodeDate = pNodeDate;
        this.pNodeQueue = pNodeQueue;
        this.pNodeName = pNodeName;
        this.pNodeLat = pNodeLat;
        this.pNodeLong = pNodeLong;
        this.pNodeDescribe = pNodeDescribe;
        this.pNodeType=pNodeType;
    }

    public String getpNodeType() {
        return pNodeType;
    }

    public int getpNodeDate() {
        return pNodeDate;
    }

    public int getpNodeQueue() {
        return pNodeQueue;
    }

    public String getpNodeName() {
        return pNodeName;
    }

    public Double getpNodeLat() {
        return pNodeLat;
    }

    public Double getpNodeLong() {
        return pNodeLong;
    }

    public String getpNodeDescribe() {
        return pNodeDescribe;
    }
}
