package com.company.workers;

public class Subcontractor {
    public String name = "";
    public double dailyCosts = 0;
    public String specialization = "";
    public boolean isHired = false;

    public Subcontractor(String name, double dailyCosts, String specialization) {
        this.name = name;
        this.dailyCosts = dailyCosts;
        this.specialization = specialization;
    }

    public Subcontractor() {};
}
