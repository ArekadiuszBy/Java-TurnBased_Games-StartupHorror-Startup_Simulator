package com.company.client;
import com.company.RandomGenerator;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ClientDetails implements IClientDetails {

    private final String[] names = { "Januszex Firma", "Microsoft", "Soczex",  "Klozetownia",  "WSB",  "TOP",  "Komputronik",  "Roleski",  "Pekao",  "TojletPapier" };
    private final String[] addresses = { "Gdańsk, Olinki 3", "Gdynia, Robótki 29", "Sopot, Przeróbki 92",  "Warszawa, Powstańców Wielkopolskich 123",  "Szczecin, Aleja Grundaldzka 300",  "Nowy Jork, Aleja Grundaldzka 300",  "Moskwa, Bursztynowa 50",  "Radom, Olinki 3",  "Hel, Bursztynowa 1",  "Gdańsk, Nowy Swiat 34" };
    String name = "";
    String address = "";
    String phoneNumber = "";
    Date insolvencyRisk = new Date();

    IClientType clientType = new IClientType() {};

    public Object[] getClientDetails() {
        var randomGenerator = new RandomGenerator();
        var randomNumber1 = randomGenerator.getRandomValue(0, 9);
        var randomNumber2 = randomGenerator.getRandomValue(0, 4);

        this.name = this.names[randomNumber1];
        this.address = this.addresses[randomNumber2];
        this.phoneNumber = randomGenerator.getPhoneNumber(randomNumber1, randomNumber2);
        this.insolvencyRisk = getDate(randomNumber1, randomNumber2);

        Object[] array = new Object[10];
        array[0] = this.name;
        array[1] = this.address;
        array[2] = this.phoneNumber;
        array[3] = this.insolvencyRisk;
        return array;
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
}
