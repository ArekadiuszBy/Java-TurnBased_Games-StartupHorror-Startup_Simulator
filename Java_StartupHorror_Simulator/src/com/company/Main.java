package com.company;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {

    public static void main(String[] args) {
        var game = new Game();
        var startDate = getDate();
        game.startGame(startDate);
        System.out.println(startDate);

        var allConditions = false;
        while (allConditions) {

            game.checkCondition(1, false, false, 100, 10);
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
