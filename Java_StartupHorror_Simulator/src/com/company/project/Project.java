package com.company.project;
import com.company.RandomGenerator;
import com.company.client.ClientDetails;
import java.util.ArrayList;
import java.util.Calendar;

public class Project {

    public Calendar currentDate;
    public ArrayList<String> neededTechnologies;
    private String[] technologies = new String[]{"FRONTEND", "BACKEND", "DATABASE", "MOBILE", "WORDPRESS", "PRESTASHOP"};
    private String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    private String[] loremSeperated = loremIpsum.split(" ");
    public String projectName = "";
    public Object[] clientDetails;
    public Calendar deadline;
    public Calendar paycheckDeadline;
    public int price;
    public int penalty;
    public projectComplexity complexity;

    public Project(Calendar currentDate) {
        var random = new RandomGenerator();
        var clientDetails = new ClientDetails();
        var tech = random.getRandomValue(0,5);
        var complex = random.getRandomValue(1,3);

        // Randomize project name
        this.projectName = this.loremSeperated[tech] + this.loremSeperated[tech % 6] + this.loremSeperated[(tech + 1) % 2];
        for (int i = 0; i < complex*2; i++) {
            this.neededTechnologies.add(this.technologies[i]);
        }

        // Initialize complexity
        if (complex == 1)
            this.complexity = projectComplexity.EASY;
        else if (complex == 2)
            this.complexity = projectComplexity.NORMAL;
        else
            this.complexity = projectComplexity.HARD;

        // Get client details
        this.clientDetails = clientDetails.getClientDetails();

        // Initialize dates
        this.currentDate = currentDate;
        this.currentDate.add(Calendar.DAY_OF_MONTH, (tech + 1) * complex^2 + 5);
        this.deadline = this.currentDate;
        this.currentDate.add(Calendar.DAY_OF_MONTH, 14);
        this.paycheckDeadline = this.currentDate;

        // Set prices
        this.price = ((complex + 5)^3)*20 + 10000;
        this.penalty = this.price / 10;
    }
}
