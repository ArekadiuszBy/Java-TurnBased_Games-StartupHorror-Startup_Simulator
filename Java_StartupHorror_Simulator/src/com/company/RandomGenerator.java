package com.company;
import com.company.workers.Student;
import com.company.workers.Subcontractor;

public class RandomGenerator {
    private final String[] names = { "Andrzej", "Janusz", "Halina",  "Miłonica",  "Arek",  "Monika",  "Olek",  "Roland",  "Bartek",  "Kamil" };
    private final String[] specializations = { "Programmer", "Tester", "Seller" };

    public int getRandomValue(int start, int end) {
	    return (int)(Math.random() * (end - start + 1) + start);
    }

    public String getPhoneNumber(int random1, int random2) {
        StringBuilder sb = new StringBuilder();
        sb.append("+48");
        sb.append(random1*5);
        sb.append(random2*3);
        sb.append(random1*1);
        sb.append(random2*53);
        sb.append(random2*8);
        sb.append(random1*12);

        return sb.toString();
    }

    public Subcontractor getRandomSubcontractor() {
        var random = this.getRandomValue(0,99);
        var name = this.names[random % 10];
        var specialization = this.specializations[random % 3];
        var dailyCosts = 0;
        if (random < 10)
            dailyCosts = random*5 + 100;
        else if (random < 100)
            dailyCosts = random + 100;
        else
            dailyCosts = random % 20 * 4 + 100;

        if (specialization.equals("Programmer"))
            dailyCosts += 50;
        else if (specialization.equals("Seller"))
            dailyCosts -= 50;

        return new Subcontractor(name, dailyCosts, specialization);
    }

    public Student getStudent(String whichStudent) {
        var random = this.getRandomValue(0,99);
        var name = "Student." + this.names[random % 10];
        var specialization = this.specializations[0];
        var chancesToFail = 0;
        var chancesToDelay = 0;
        var dailyCosts = 0;

        if (whichStudent.equals("bad")) {
            dailyCosts = 20;
            chancesToFail = 20;
            chancesToDelay = 20;
        } else if (whichStudent.equals("avg")) {
            dailyCosts = 30;
            chancesToFail = 10;
        } else {
            dailyCosts = 40;

        }


        return new Student(name, dailyCosts, specialization, chancesToFail, chancesToDelay);
    }

}
