package com.example.appiii;

public class C_NodeInfo {
    private String NodeName;
    private String NodeAddress;
    private Double NodeLat;
    private Double NodeLong;
    private String NodeDescribe;


    public C_NodeInfo(String nodeName, String nodeAddress, Double nodeLat, Double nodeLong, String nodeDescribe) {
        this.NodeName = nodeName;
        this.NodeAddress = nodeAddress;
        this.NodeLat = nodeLat;
        this.NodeLong = nodeLong;
        this.NodeDescribe = nodeDescribe;

    }

    public String getNodeName() {
        return NodeName;
    }

    public String getNodeAddress() {
        return NodeAddress;
    }

    public Double getNodeLat() {
        return NodeLat;
    }

    public Double getNodeLong() {
        return NodeLong;
    }

    public String getNodeDescribe() {
        return NodeDescribe;
    }

}
