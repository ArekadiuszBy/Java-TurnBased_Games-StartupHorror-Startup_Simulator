package com.company.workers;

public class Student {
    public String name;
    public double dailyCosts;
    public String specialization;
    public boolean isHired;
    public int chancesToFail;
    public int chancesToDelay;
    public StudentSkill studentSkill;

    public Student(String name, double dailyCosts, String specialization, int chancesToFail, int chancesToDelay) {
        this.name = name;
        this.dailyCosts = dailyCosts;
        this.specialization = specialization;
        this.isHired = false;
        this.chancesToFail = chancesToFail;
        this.chancesToDelay = chancesToDelay;

        if (chancesToFail == 0)
            this.studentSkill = StudentSkill.GOOD;
        else if (chancesToFail == 0)
            this.studentSkill = StudentSkill.AVG;
        else
            this.studentSkill = StudentSkill.BAD;

    }
}
