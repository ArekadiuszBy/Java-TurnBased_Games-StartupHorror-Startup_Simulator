package com.company.client;
import java.util.Date;

public interface IClientDetails {
    String name = "";
    String address = "";
    String phoneNumber = "";
    Date insolvencyRisk = new Date();

    IClientType clientType = new IClientType() {};
}

interface IClientType {
    int oneWeekPaymentDelay = 0;
    int penaltyChance = 0;
    boolean isProjectWorking = true;
}