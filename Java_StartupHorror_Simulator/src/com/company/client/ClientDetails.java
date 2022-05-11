package com.company.client;
import com.company.RandomGenerator;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ClientDetails implements IClientDetails {

    private final String[] names = { "Januszex Firma", "Microsoft", "Soczex",  "Klozetownia",  "WSB",  "TOP",  "Komputronik",  "Roleski",  "Pekao",  "TojletPapier" };
    private final String[] addresses = { "Gdańsk, Olinki 3", "Gdynia, Robótki 29", "Sopot, Przeróbki 92",  "Warszawa, Powstańców Wielkopolskich 123",  "Szczecin, Aleja Grundaldzka 300",  "Nowy Jork, Aleja Grundaldzka 300",  "Moskwa, Bursztynowa 50",  "Radom, Olinki 3",  "Hel, Bursztynowa 1",  "Gdańsk, Nowy Swiat 34" };
    String name;
    String address;
    String phoneNumber;
    int delayedPaymentOneWeekRisk;
    int delayedPaymentOneMonthRisk;
    int neverPayRisk;
    int penaltyAvoidChances;
    int loseContactChances;
    ClientType clientType;

    public ClientDetails setClientDetails() {
        var randomGenerator = new RandomGenerator();
        var randomNumber1 = randomGenerator.getRandomValue(0, 9);
        var randomNumber2 = randomGenerator.getRandomValue(0, 4);
        var randomNumberClientType = randomGenerator.getRandomValue(1, 3);

        this.name = this.names[randomNumber1];
        this.address = this.addresses[randomNumber2];
        this.phoneNumber = randomGenerator.getPhoneNumber(randomNumber1, randomNumber2);

        switch(randomNumberClientType) {
            // Good client
            case 1: {
                this.clientType = ClientType.GOOD;
                this.delayedPaymentOneWeekRisk = 30;
                this.delayedPaymentOneMonthRisk = 0;
                this.neverPayRisk = 0;
                this.penaltyAvoidChances = 20;
                this.loseContactChances = 0;
                break;
            }
            // Avg client
            case 2: {
                this.clientType = ClientType.AVERAGE;
                this.delayedPaymentOneWeekRisk = 0;
                this.delayedPaymentOneMonthRisk = 0;
                this.neverPayRisk = 0;
                this.penaltyAvoidChances = 0;
                this.loseContactChances = 50;

                break;
            }
            //Worst client
            case 3: {
                this.clientType = ClientType.SQUIRWIEL;
                this.delayedPaymentOneWeekRisk = 0;
                this.delayedPaymentOneMonthRisk = 5;
                this.neverPayRisk = 1;
                this.penaltyAvoidChances = 0;
                this.loseContactChances = 100;
                break;
            }
        }

        return this;
    }

    public boolean checkLeapYear(int year) {
        return year % 4 == 0;
    }

    public Date getDate(int randomNumber1, int randomNumber2) {
        var date =  new Date();
        var calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR) + randomNumber1 / 5;
        int month = calendar.get(Calendar.MONTH) + randomNumber1;
        int day = calendar.get(Calendar.DAY_OF_MONTH) + randomNumber2 * 3;

        var leapYear = checkLeapYear(year);
        if (month == 2 && day > 28 && leapYear)
            day -= 2;
        else
            day -= 1;

        return new GregorianCalendar(year, month - 1, day).getTime();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getDelayedPaymentOneWeekRisk() {
        return delayedPaymentOneWeekRisk;
    }

    public int getDelayedPaymentOneMonthRisk() {
        return delayedPaymentOneMonthRisk;
    }

    public int getNeverPayRisk() {
        return neverPayRisk;
    }

    public int getPenaltyAvoidChances() {
        return penaltyAvoidChances;
    }

    public int getLoseContactChances() {
        return loseContactChances;
    }

    public ClientType getClientType() {
        return clientType;
    }
}
