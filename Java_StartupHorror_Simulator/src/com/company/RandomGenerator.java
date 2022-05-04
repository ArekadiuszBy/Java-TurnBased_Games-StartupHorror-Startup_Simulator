package com.company;

public class RandomGenerator {

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

}
