package com.coreApplication.Model;

public class Patient {
    private Long id;
    private String name;
    private int age;
    private String notes;

    public Patient() {}

    public Patient(Long id, String name, int age, String notes) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
