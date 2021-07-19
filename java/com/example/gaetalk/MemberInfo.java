package com.example.gaetalk;

public class MemberInfo {
    private String humanName;
    private String name;
    private String gender;
    private String birth;
    private String breed;
    private String address;
    private String personality;
    private String photoUrl;

    public MemberInfo(String humanName, String name, String gender, String birth, String breed, String address, String personality) {
        this.humanName = humanName;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.breed = breed;
        this.address = address;
        this.personality = personality;
    }

    public MemberInfo(String humanName, String name, String gender, String birth, String breed, String address, String personality, String photoUrl) {
        this.humanName = humanName;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.breed = breed;
        this.address = address;
        this.personality = personality;
        this.photoUrl = photoUrl;
    }

    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}