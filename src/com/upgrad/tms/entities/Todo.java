package com.upgrad.tms.entities;

public class Todo extends Task {
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void printSubTaskProperties() {
        System.out.println("Description: " + getDescription());
    }
}

