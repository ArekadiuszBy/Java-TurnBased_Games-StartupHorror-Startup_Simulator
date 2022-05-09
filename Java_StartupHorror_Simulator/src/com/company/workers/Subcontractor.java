package com.company.workers;

public class Subcontractor {
    public String name;
    public double dailyCosts;
    public String specialization;
    public boolean isHired;

    public Subcontractor(String name, double dailyCosts, String specialization) {
        this.name = name;
        this.dailyCosts = dailyCosts;
        this.specialization = specialization;
        this.isHired = false;
    }
}
