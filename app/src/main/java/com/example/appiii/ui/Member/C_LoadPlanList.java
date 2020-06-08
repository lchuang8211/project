package com.example.appiii.ui.Member;

public class C_LoadPlanList {
    private String planName;
    private String PlanDate;
    private int PlanMaxDay;

    public C_LoadPlanList(String planName, String planDate, int planMaxDay) {
        this.planName = planName;
        this.PlanDate = planDate;
        this.PlanMaxDay = planMaxDay;
    }
    public String getPlanName() {
        return planName;
    }
    public String getPlanDate() {
        return PlanDate;
    }
    public int getPlanMaxDay() {
        return PlanMaxDay;
    }

}
