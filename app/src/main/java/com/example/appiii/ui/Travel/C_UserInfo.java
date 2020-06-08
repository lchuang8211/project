package com.example.appiii.ui.Travel;

public class C_UserInfo {
    private String userAccount;
    private String userNickName;
    private String userSchedule;
    private String userUID;
    private String userHeadImg;
    private String userPlan;

    public C_UserInfo(String userUID, String userAccount, String userNickName, String userPlan, String userSchedule , String userHeadImg){
        this.userAccount = userAccount;
        this.userNickName = userNickName;
        this.userSchedule = userSchedule;
        this.userPlan = userPlan;
        this.userUID = userUID;
        this.userHeadImg = userHeadImg;
    }

    public String getUserPlan() {
        return userPlan;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public String getUserSchedule() {
        return userSchedule;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }
}
