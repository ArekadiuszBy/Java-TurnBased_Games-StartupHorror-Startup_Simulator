package com.company;
import com.company.client.ClientDetails;
import com.company.workers.Subcontractor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Game {
    public ArrayList<Subcontractor> subcontractors;
    public ClientDetails clientDetails;
    private Double moneys;

    public void playGame() {
        // Game initialization
        var game = new Game(new ArrayList<>(), new ClientDetails());

        // Game start values
        var randomGenerator = new RandomGenerator();
        var startDate = getStartDate();
        var randomNumber1 = randomGenerator.getRandomValue(0, 500);
        var wholeGame = game.startGame();

        this.moneys = Double.valueOf(randomNumber1 * 100);
        if (this.moneys < 5000)
            this.moneys += 5000;
        var workers = new ArrayList<Subcontractor>();

        System.out.println("\n\n You're starting at:" + startDate.getTime());
        System.out.println("Your moneys: " + moneys);
        System.out.println("Your workers: " + "null");
        System.out.println("\n\n");

        var allConditions = false;
        var dayNumber = 0;
        var projectsQuantity = 0;
        var tempCurrentDate = startDate;
        var workAlone = false;
        game.getSubcontractorsString();

        while (!allConditions) {
            System.out.println("\nToday is day no." + dayNumber + "    Date: " + startDate.getTime());
            checkForNextMonth(startDate, tempCurrentDate);

            if (moneys <= 0)
                System.out.println("You lost (your money, your wife and your house).");

            workAlone = (startDate.get(Calendar.DAY_OF_WEEK) == 7 || startDate.get(Calendar.DAY_OF_WEEK) == 0);

            if (workAlone) {

            }

            if (game.checkCondition(1, false, false, moneys)) {
                System.out.println("YOU WON!");
                System.out.println("Your moneys: " + moneys);
                System.out.println("Your workers: " + workers);
                System.out.println("Projects in your organization: " + projectsQuantity);

            }

            if (dayNumber == 5)
                allConditions = true;

            startDate = addDay(startDate);
            dayNumber++;
            tempCurrentDate = startDate;
        }
    }

    public static Calendar getStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);

        // 1.01.2020 - Wednesday
        return calendar;
    }

    //Incrementing the date by 1 day
    private static Calendar addDay(Calendar oldDate) {
        oldDate.add(Calendar.DAY_OF_MONTH, 1);
        return oldDate;
    }

    private void checkForNextMonth(Calendar oldDate, Calendar tempCurrentMonth) {
        if (oldDate.get(Calendar.MONTH) != tempCurrentMonth.get(Calendar.MONTH)) {
            this.moneys *= 0.9;
        }
    }

    public Game startGame() {
        var clientDetails = new ClientDetails();
        clientDetails.getClientDetails();
        this.subcontractors = this.setSubcontractors();
        return new Game(subcontractors, clientDetails);
    }

    public Game(ArrayList<Subcontractor> subcontractors, ClientDetails clientDetails) {
        this.subcontractors = subcontractors;
        this.clientDetails = clientDetails;
    }

    public Game() {};

    public boolean checkCondition(int bigProjectsQuantity, boolean wasOwnerABigProjectDev, boolean wasBigClientFromOurSeller, double moneys) {
        if (bigProjectsQuantity >= 3 && wasOwnerABigProjectDev && wasBigClientFromOurSeller)
            return true;
        return false;
    }
    
    public boolean checkIfWeekend(Date currentDate) {
        if (currentDate.getDay() == 6 || currentDate.getDay() == 0) {
            return true;
        }

        return false;
    }

    public int oneDay(Date currentDay) {
        var isWeekend = this.checkIfWeekend(currentDay);


        return 1;
    }

    public ArrayList<Subcontractor> setSubcontractors() {
        var subcontractorsList = new ArrayList<Subcontractor>();
        var randomGenerator = new RandomGenerator();
        var randomNumber = randomGenerator.getRandomValue(0,9);

        for (int i = 0; i < 5; i++) {
            subcontractorsList.add(randomGenerator.getRandomSubcontractor(randomNumber));
        }

        return subcontractorsList;
    }

    public ArrayList<Subcontractor> getSubcontractors() {
        return subcontractors;
    }

    public void getSubcontractorsString() {
        var i = 1;
        for(var subcontractor : subcontractors) {
            System.out.println("subcontractor no." + i +
                    " ->  name: " + subcontractor.name +
                    " / specialization: " + subcontractor.specialization +
                    " / isHired: " + subcontractor.isStudent +
                    " / dailyCosts: " + subcontractor.dailyCosts +
                    " / isHired: " + subcontractor.isHired);
            i++;
        }
    }


}
