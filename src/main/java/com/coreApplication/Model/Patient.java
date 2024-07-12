package com.coreApplication.Model;

public class Patient {
    private Long id;
    private String patientName;
    private int age;
    private String notes;

    public Patient() {}

    public Patient(Long id, String patientName, int age, String notes) {
        this.id = id;
        this.patientName = patientName;
        this.age = age;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
