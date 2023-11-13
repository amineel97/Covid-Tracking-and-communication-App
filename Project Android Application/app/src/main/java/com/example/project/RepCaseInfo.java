package com.example.project;

public class RepCaseInfo {


    public String REP_name, REP_surname, Latitude, Longitude, FamilyMember, CaseStatus, CaseInfo;

    public RepCaseInfo(){}

    public RepCaseInfo(String REP_name, String REP_surname, String Latitude, String Longitude, String FamilyMember, String CaseStatus, String CaseInfo){

        this.REP_name = REP_name;
        this.REP_surname = REP_surname;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.FamilyMember = FamilyMember;
        this.CaseStatus = CaseStatus;
        this.CaseInfo = CaseInfo;

    }
}
