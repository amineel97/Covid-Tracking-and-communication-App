package com.example.project;

public class UserStatus {

    public  String name , surname, Current_status, Quarantine_days, StartingDate, Reminder_info;

    public UserStatus(){}

    public UserStatus(String name, String surname, String Current_status, String Quarantine_days, String StartingDate, String Reminder_info){

        this.name = name;
        this.surname = surname;
        this.Current_status = Current_status;
        this.Quarantine_days = Quarantine_days;
        this.Quarantine_days = Quarantine_days;
        this.StartingDate = StartingDate;
        this.Reminder_info = Reminder_info;
    }
}
