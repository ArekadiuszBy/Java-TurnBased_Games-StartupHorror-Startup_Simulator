package com.company;
import com.company.client.ClientDetails;
import com.company.workers.Subcontractor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {

    public static void main(String[] args) {
        // Game initialization
        var game = new Game(new ArrayList<>(), new ClientDetails());

        // Game start values
        var randomGenerator = new RandomGenerator();
        var startDate = getDate();
        var randomNumber1 = randomGenerator.getRandomValue(0, 500);
        var wholeGame = game.startGame(startDate, randomNumber1);

        var moneys = randomNumber1 * 100;
        if (moneys < 5000)
            moneys += 5000;
        var workers = new ArrayList<Subcontractor>();

        System.out.println("You're starting at:" + startDate);
        System.out.println("Your moneys: " + moneys);
        System.out.println("Your workers: " + "null");

        var allConditions = false;
        while (allConditions) {
            if (moneys <= 0)
                System.out.println("LOSER!");

            if (game.checkCondition(1, false, false, 100, 10))
                System.out.println("YOU WON!");
                System.out.println("Your moneys: " + moneys);
                System.out.println("Your workers: " + workers);
                System.out.println("Your moneys: " + moneys);
                System.out.println("Your moneys: " + moneys);
        }
    }

    public static Date getDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 1.01.2020 - środa
        return new GregorianCalendar(year, month - 1, day).getTime();
    }
}
