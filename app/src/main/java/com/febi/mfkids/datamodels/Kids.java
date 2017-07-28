package com.febi.mfkids.datamodels;

/**
 * Created by flock on 28/7/17.
 */

public class Kids {
    private String key;
    private String name;
    private String gender;
    private String dob;

    public Kids() {
    }

    public Kids(String key, String name, String gender, String dob) {
        this.key    = key;
        this.name   = name;
        this.gender = gender;
        this.dob    = dob;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
