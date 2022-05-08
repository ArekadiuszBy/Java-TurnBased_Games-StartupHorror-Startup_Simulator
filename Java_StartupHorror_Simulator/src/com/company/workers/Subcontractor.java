package com.company.workers;

public class Subcontractor {
    public String name;
    public boolean isStudent;
    public double dailyCosts;
    public String specialization;
    public boolean isHired;

    public Subcontractor(String name, boolean isStudent, double dailyCosts, String specialization) {
        this.name = name;
        this.isStudent = isStudent;
        this.dailyCosts = dailyCosts;
        this.specialization = specialization;
        this.isHired = false;
    }
}
