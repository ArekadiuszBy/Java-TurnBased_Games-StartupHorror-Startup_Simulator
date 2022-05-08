package com.company;
import com.company.client.ClientDetails;
import com.company.workers.Subcontractor;
import java.util.ArrayList;
import java.util.Date;

public class Game {
    public ArrayList<Subcontractor> subcontractors;
    public ClientDetails clientDetails;

    public Game startGame(Date startDate, int randomNumber) {
        var clientDetails = new ClientDetails();
        clientDetails.getClientDetails();
        var randomGenerator = new RandomGenerator();
        var subcontractors = new ArrayList<Subcontractor>();
        for (int i = 0; i < 5; i++) {
            subcontractors.add(randomGenerator.getRandomSubcontractor(randomNumber));
        }

        return new Game(subcontractors, clientDetails);
    }

    public Game(ArrayList<Subcontractor> subcontractors, ClientDetails clientDetails) {
        this.subcontractors = subcontractors;
        this.clientDetails = clientDetails;
    }

    public boolean checkCondition(int bigProjectsQuantity, boolean wasOwnerABigProjectDev, boolean wasBigClientFromOurSeller, double moneys, double startMoneys) {
        if (bigProjectsQuantity >= 3 && wasOwnerABigProjectDev && wasBigClientFromOurSeller && moneys > startMoneys)
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
}
