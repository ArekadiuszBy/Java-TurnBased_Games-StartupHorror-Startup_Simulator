package com.company;

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

    public Subcontractor getRandomSubcontractor(int random) {
        var name = this.names[random % 10];
        var specialization = this.names[random % 3];
        var dailyCosts = 0;
        if (random < 10)
            dailyCosts = random*5 + 100;
        else if (random < 100)
            dailyCosts = random + 100;
        else
            dailyCosts = random % 20 * 4 + 100;

        if (specialization.equals("Programmer"))
            dailyCosts += 50;
        else if (specialization.equals("Tester"))
            dailyCosts -= 50;

        return new Subcontractor(name, false, dailyCosts, specialization);
    }

}
