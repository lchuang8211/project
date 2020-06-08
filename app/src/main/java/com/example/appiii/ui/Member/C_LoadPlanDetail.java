package com.example.appiii.ui.Member;

public class C_LoadPlanDetail {
    private String pDate;
    private String pQueue;
    private String pNode;
    private String pLat;
    private  String pLong ;
    private String pDescribe;

    public C_LoadPlanDetail(String pDate, String pQueue, String pNode, String pLat, String pLong, String pDescribe) {
        this.pDate = pDate;
        this.pQueue = pQueue;
        this.pNode = pNode;
        this.pLat = pLat;
        this.pLong = pLong;
        this.pDescribe = pDescribe;
    }

    public String getpDate() {
        return pDate;
    }

    public String getpQueue() {
        return pQueue;
    }

    public String getpNode() {
        return pNode;
    }

    public String getpLat() {
        return pLat;
    }

    public String getpLong() {
        return pLong;
    }

    public String getpDescribe() {
        return pDescribe;
    }
}
