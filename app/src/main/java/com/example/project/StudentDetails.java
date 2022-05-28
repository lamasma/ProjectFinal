package com.example.project;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class StudentDetails implements Serializable {
    private String id;
    private String name, surName, fatherName, nationalId, dateOfBirth, gender;
    private String key;

    public StudentDetails(String id, String name, String surName, String fatherName, String nationalId, String dateOfBirth, String gender) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.fatherName = fatherName;
        this.nationalId = nationalId;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;

    }

    public StudentDetails() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }


}
