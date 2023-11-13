package com.example.project;

public class Appointment {

    public String name, surname, appointment_Date, hour, phone, appointment_accepted;

    public Appointment(){}

    public Appointment(String name, String surname, String appointment_Date, String hour, String phone, String appointment_accepted){

        this.name = name;
        this.surname = surname;
        this.appointment_Date = appointment_Date;
        this.hour = hour;
        this.phone = phone;
        this.appointment_accepted = appointment_accepted;
    }
}
