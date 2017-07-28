package com.febi.mfkids.datamodels;

/**
 * Created by flock on 28/7/17.
 */

public class KidsMilestones {
    private String key;
    private String kidKey;
    private String milestone;
    private String dateOfMilestone;

    public KidsMilestones() {
    }

    public KidsMilestones(String key, String kidKey, String milestone, String dateOfMilestone) {
        this.key = key;
        this.kidKey = kidKey;
        this.milestone = milestone;
        this.dateOfMilestone = dateOfMilestone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getDateOfMilestone() {
        return dateOfMilestone;
    }

    public void setDateOfMilestone(String dateOfMilestone) {
        this.dateOfMilestone = dateOfMilestone;
    }

    public String getKidKey() {
        return kidKey;
    }

    public void setKidKey(String kidKey) {
        this.kidKey = kidKey;
    }
}
