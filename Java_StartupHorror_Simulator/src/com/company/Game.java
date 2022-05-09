package com.company;
import com.company.client.ClientDetails;
import com.company.workers.Subcontractor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Game {
    public ArrayList<Subcontractor> subcontractors;
    public ArrayList<Subcontractor> hiredWorkers;
    public ClientDetails clientDetails;
    private Double moneys;
    private Double costOfWorkers;
    private int taxesDaysMade;

    public void playGame() {
        // Game initialization
        var game = new Game(new ArrayList<>(), new ClientDetails());

        // Game start values
        var randomGenerator = new RandomGenerator();
        var startDate = getStartDate();
        var randomNumber1 = randomGenerator.getRandomValue(0, 500);
        var wholeGame = game.startGame();

        this.taxesDaysMade = 0;
        this.moneys = Double.valueOf(randomNumber1 * 100);
        if (this.moneys < 5000)
            this.moneys += 5000;

        System.out.println("\n\n You're starting at:" + startDate.getTime());
        System.out.println("Your moneys: " + moneys);
        System.out.println("Your workers: " + "null");
        System.out.println("\n\n");

        // Global variables to count also between days
        var allConditions = false;
        var dayNumber = 0;
        var projectsQuantity = 0;
        var tempCurrentDate = startDate;
        var workOnlyAlone = false;
        var isOwnerWorking = false;
        game.getSubcontractorsString();


        while (!allConditions) {
            System.out.println("\nToday is day no." + dayNumber + "    Date: " + startDate.getTime());
            checkForNextMonth(startDate, tempCurrentDate);

            if (moneys <= 0)
                System.out.println("You lost (your money, your wife and your house).");

            isOwnerWorking = this.isOwnerWorkingToday();
            workOnlyAlone = (startDate.get(Calendar.DAY_OF_WEEK) == 7 || startDate.get(Calendar.DAY_OF_WEEK) == 0);
            if(!isOwnerWorking) {
                startDate = addDay(startDate);
                dayNumber++;
                tempCurrentDate = startDate;
                continue;
            }

            var todayOption = this.readPlayerOption();

            if (todayOption == 1) {
                System.out.println("YOU WONNNN!");
            } else if (todayOption == 2) {
                System.out.println("YOU WONNNN!");
            } else if (todayOption == 3) {
                System.out.println("YOU WONNNN!");
            } else if (todayOption == 4) {
                System.out.println("YOU WONNNN!");
            } else if (todayOption == 5) {
                System.out.println("YOU WONNNN!");
            } else if (todayOption == 6) {
                this.hiredWorkers = (ArrayList<Subcontractor>) this.subcontractors.stream().filter(t -> t.isHired).toList();
                this.hiredWorkers.remove(hiredWorkers.size());
                this.subcontractors = hiredWorkers;
            } else if (todayOption == 7) {
                this.taxesDaysMade++;
            }

            if (workOnlyAlone) {

            }

            if (game.checkCondition(1, false, false, this.moneys)) {
                System.out.println("YOU WON!");
                System.out.println("Your moneys: " + this.moneys);
                System.out.println("Your workers: " + this.hiredWorkers);
                System.out.println("Projects in your organization: " + projectsQuantity);

            }

            if (dayNumber == 5)
                allConditions = true;

            for (var subcontractor : subcontractors) {
                if (subcontractor.isHired)
                    this.costOfWorkers += subcontractor.dailyCosts;
            }
            this.moneys -= costOfWorkers;

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

    public boolean isOwnerWorkingToday() {
        var scanner = new Scanner(System.in);
        var userInput = scanner.nextLine();
        var userChoice = Integer.parseInt(userInput);
        if (userChoice == 0) {
            System.out.println("You're not working today");
            return false;
        } else {
            System.out.println("You're working today");
            return true;
        }

    }

    public int readPlayerOption() {
        System.out.println("Select option for the next day (1-7): ");
        var scanner = new Scanner(System.in);
        var userInput = scanner.nextLine();
        var userChoice = Integer.parseInt(userInput);
        switch(userChoice) {
            case 1: {
                System.out.println("You've signed new contract!");
                return 1;
            }
            case 2: {
                System.out.println("You're searching for a new client!");
                return 2;
            }
            case 3: {
                System.out.println("You're programmer today!");
                return 3;
            }
            case 4: {
                System.out.println("You're tester today!");
                return 4;
            }
            case 5: {
                System.out.println("You're handing over the project!");
                return 5;
            }
            case 6: {
                System.out.println("You've got new employee!");
                return 6;
            }
            case 7: {
                System.out.println("You fired old employee!");
                return 6;
            }
            case 8: {
                System.out.println("You're spending this day only on taxes (ZUS).");
                return 8;
            }
            default: {
                System.out.println("You're choose nothing. Choose again.");
                return 0;
            }
        }
    }


}
