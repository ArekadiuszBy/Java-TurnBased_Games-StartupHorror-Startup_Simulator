package com.company;
import com.company.client.ClientDetails;
import java.util.Date;

public class Game {
    public void startGame(Date startDate) {
        var clientDetails = new ClientDetails();
        clientDetails.getClientDetails();
    }

    public boolean checkCondition(int bigProjectsQuantity, boolean wasOwnerABigProjectDev, boolean wasBigClientFromOurSeller, double moneys, double startMoneys) {
        if (bigProjectsQuantity >= 3 && wasOwnerABigProjectDev && wasBigClientFromOurSeller && moneys > startMoneys)
            return true;
        return false;
    }
}
