package com.company;
import com.company.client.ClientDetails;
import com.company.project.Project;
import com.company.project.ProjectComplexity;
import com.company.workers.Student;
import com.company.workers.Subcontractor;
import java.util.*;

public class Game {
    public ArrayList<Subcontractor> subcontractors;
    public ArrayList<Student> students;
    public ArrayList<Subcontractor> hiredWorkers;
    public ArrayList<Student> hiredStudents;
    public ClientDetails clientDetails;
    private ArrayList<Project> availableProjects;
    private Project currentProject;
    private Double moneys;
    private Double startMoneys;
    private Double costOfWorkers;
    private Double dailyCosts;
    public int bigProjectsQuantityWithoutOwner = 0;
    private int taxesDaysMade = 0;
    private int daysToNewAvailableProject = 0;
    private int projectSelected = 0;
    private int daysToFindNewClient = 0;
    private boolean isFixNeeded = false;

    public void playGame() {
        System.out.println("\n");

        // Game initialization
        var game = new Game(new ArrayList<>(), new ArrayList<>(), new ClientDetails());

        // Game start values
        var randomGenerator = new RandomGenerator();
        var startDate = getStartDate();
        var randomNumber1 = randomGenerator.getRandomValue(0, 500);
        var wholeGame = game.startGame();

        // Start game with 5-15 projects, selected by user
        this.startGameWithProjects(startDate);

        this.taxesDaysMade = 0;
        this.moneys = Double.valueOf(randomNumber1 * 100);
        if (this.moneys < 5000)
            this.moneys += 5000;
        this.startMoneys = this.moneys;

        System.out.println("\nYou're starting at:  " + startDate.getTime());
        System.out.println("Your moneys:         " + this.startMoneys + " PLN");
        System.out.println("Your workers:        " + "None.");
        System.out.println("Available projects:  ");
        this.displayAvailableProjects();
        System.out.println("\n");

        // Global variables to count also between days
        var allConditions = false;
        var dayNumber = 0;
        var projectsQuantity = 0;
        var tempCurrentDate = startDate;
        var workOnlyAlone = false;
        var isOwnerWorking = false;
        game.getSubcontractorsString();
        game.getStudentsString();

        // Play till win or lose
        while (!allConditions) {
            System.out.println("\nToday is day no." + dayNumber + "    Date: " + startDate.getTime());

            // Check if taxes were done & take 10% of your moneys if new month
            if (!checkForNextMonth(startDate, tempCurrentDate)) {
                System.out.println("You lost (your money, your wife and your house).");
                break;
            }

            if (moneys <= 0) {
                System.out.println("You lost (your money, your wife and your house).");
                break;
            }

            isOwnerWorking = this.isOwnerWorkingToday();
            workOnlyAlone = (startDate.get(Calendar.DAY_OF_WEEK) == 7 || startDate.get(Calendar.DAY_OF_WEEK) == 0);
            if(!isOwnerWorking) {
                startDate = addDay(startDate);
                dayNumber++;
                tempCurrentDate = startDate;
                continue;
            }

            var todayOption = 0;
            if (this.isFixNeeded) {
                todayOption = 3;
                System.out.println("Today you have no choice, you're fixing bugs.");
            } else {
                todayOption = this.readPlayerOption();
            }
            // new contract day
            if (todayOption == 1) {
                this.assignProject(this.projectSelected);


                // searching for new client day
            } else if (todayOption == 2) {
                this.findNewClient();

                // programming day
            } else if (todayOption == 3) {
                System.out.println("YOU WONNNN!");


                // testing day
            } else if (todayOption == 4) {
                System.out.println("YOU WONNNN!");

                // hand the project over day
            } else if (todayOption == 5) {
                System.out.println("YOU WONNNN!");

                // new employee day
            } else if (todayOption == 6) {
                this.hiredWorkers = (ArrayList<Subcontractor>) this.subcontractors.stream().filter(t -> t.isHired).toList();
                this.hiredWorkers.remove(hiredWorkers.size());
                this.subcontractors = hiredWorkers;

                // fire employee day
            } else if (todayOption == 7) {
                this.fireEmployee();

                // new student day
            } else if (todayOption == 8) {
                this.hiredStudents = (ArrayList<Student>) this.students.stream().filter(t -> t.isHired).toList();
                this.hiredStudents.remove(hiredStudents.size());
                this.subcontractors = hiredWorkers;
            }
                // fire student day
            else if (todayOption == 9) {

                // taxes day
            } else if (todayOption == 10) {
                if (this.taxesDaysMade > 2) {
                    System.out.println("You did all the taxes. So today you're doing nothing.");
                } else
                    this.taxesDaysMade++;
            }

            if (workOnlyAlone) {

            }

            if (game.checkCondition(bigProjectsQuantityWithoutOwner, false, false, this.moneys)) {
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

            this.hiredWorkers.stream().forEach(t -> this.dailyCosts += t.dailyCosts);
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

    private boolean checkForNextMonth(Calendar oldDate, Calendar tempCurrentMonth) {
        if (oldDate.get(Calendar.MONTH) != tempCurrentMonth.get(Calendar.MONTH)) {
            this.moneys *= 0.9;
            if (this.taxesDaysMade < 2) {
                System.out.println("YOU FORGOT TO DO TAXES! Tax clerks are coming, better run away 4ever!");
                return false;
            }
            return true;
        }
        return true;
    }

    public Game startGame() {
        var clientDetails = new ClientDetails();
        clientDetails.getClientDetails();
        this.subcontractors = this.setSubcontractors();
        this.students = this.setStudents();
        return new Game(this.subcontractors, this.students, clientDetails);
    }

    public Game(ArrayList<Subcontractor> subcontractors, ArrayList<Student> students, ClientDetails clientDetails) {
        this.subcontractors = subcontractors;
        this.students = students;
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

        for (int i = 0; i < 5; i++) {
            subcontractorsList.add(randomGenerator.getRandomSubcontractor());
        }

        return subcontractorsList;
    }

    public ArrayList<Subcontractor> getSubcontractors() {
        return this.subcontractors;
    }

    public void getSubcontractorsString() {
        var i = 1;
        for(var subcontractor : this.subcontractors) {
            System.out.println("Subcontractor no." + i +
                    " ->  name: " + subcontractor.name +
                    " / specialization: " + subcontractor.specialization +
                    " / dailyCosts: " + subcontractor.dailyCosts +
                    " / isHired: " + subcontractor.isHired);
            i++;
        }
    }

    public ArrayList<Student> setStudents() {
        var studentsList = new ArrayList<Student>();
        var randomGenerator = new RandomGenerator();

        studentsList.add(randomGenerator.getStudent("bad"));
        studentsList.add(randomGenerator.getStudent("avg"));
        studentsList.add(randomGenerator.getStudent("good"));

        return studentsList;
    }

    public ArrayList<Student> getStudents() {
        return this.students;
    }

    public void getStudentsString() {
        var i = 1;
        for(var student : this.students) {
            System.out.println("Student no." + i +
                    " ->  name: " + student.name +
                    " / specialization: " + student.specialization +
                    " / dailyCosts: " + student.dailyCosts +
                    " / isHired: " + student.isHired +
                    " / chancesToFail: " + student.chancesToFail + "%" +
                    " / chancesToDelay: " + student.chancesToDelay + "%");
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

    private ArrayList<Project> initializeProjects(Calendar currentDate, int projectsQuantityOnTheStart) {
        var project = new ArrayList<Project>();
        for (int i = 0; i < projectsQuantityOnTheStart; i++) {
            project.add(new Project(currentDate));
        }

        return project;
    }

    public int readPlayerOption() {
        System.out.println("Select option for the next day (1-10): ");
        var scanner = new Scanner(System.in);
        var userInput = scanner.nextLine();
        var userChoice = Integer.parseInt(userInput);
        switch(userChoice) {
            case 1: {
                System.out.println("Which contract do you want to get? (number)");
                userInput = scanner.nextLine();
                userChoice = Integer.parseInt(userInput);

                if (userChoice > 0 && userChoice < this.availableProjects.size())
                    this.projectSelected = userChoice - 1;
                else {
                    System.out.println("Project does not exists");
                    this.readPlayerOption();
                }

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
                return 7;
            }
            case 8: {
                System.out.println("You've got new student!");
                return 8;
            }
            case 9: {
                System.out.println("You've fired student!");
                return 9;
            }
            case 10: {
                System.out.println("You're spending this day only on taxes (ZUS).");
                return 10;
            }
            default: {
                System.out.println("You're choose nothing. Choose again.");
                return 0;
            }
        }
    }

    public void startGameWithProjects(Calendar startDate) {
        var scanner = new Scanner(System.in);
        var startProjectsQuantity = 0;

        while (startProjectsQuantity < 5 || startProjectsQuantity > 15) {
            System.out.println("How many available projects do you want to start with? (5 - 15)");
            var userInput = scanner.nextLine();
            startProjectsQuantity = Integer.parseInt(userInput);
            if (startProjectsQuantity >= 5 && startProjectsQuantity <= 15)
                this.availableProjects = this.initializeProjects(startDate, startProjectsQuantity);
        }
    }

    public void displayAvailableProjects() {
        int i = 1;
        for (var proj : this.availableProjects) {
            System.out.print("Project no +" + i++ + " name: " + proj.projectName);
            System.out.print(" Project complexity: " + proj.complexity);
            System.out.print(" Project name: " + proj.neededTechnologies);
            System.out.print(" Project price: " + proj.price);
            System.out.print(" Project penalty: " + proj.penalty);
            System.out.print(" Project deadline: " + proj.deadline);
            System.out.print(" Project name: " + proj.paycheckDeadline);
            System.out.print(" Client details : " + proj.clientDetails + "\n");
        }
    }

    public void hireEmployee() {
        var scanner = new Scanner(System.in);
        var hiredEmployeeName = scanner.nextLine();
        if (this.subcontractors.stream().filter(s -> s.name == hiredEmployeeName).count() < 0) {
            System.out.println("Person doesn't exists. Type name again.");
        }
        this.hiredWorkers.add(this.subcontractors.stream().filter(s -> s.name == hiredEmployeeName).findFirst().get());
        this.moneys -= 500;
    }

    public void fireEmployee() {
        var scanner = new Scanner(System.in);
        var firedEmployeeName = scanner.nextLine();
        this.hiredWorkers.remove(this.subcontractors.stream().filter(s -> s.name == firedEmployeeName).findFirst().get());
        this.moneys -= 200;
    }

    public void hireStudent() {
        var scanner = new Scanner(System.in);
        var hiredStudentSkill = scanner.nextLine();
        if (this.students.stream().filter(s -> s.studentSkill.equals(hiredStudentSkill)).count() < 0) {
            System.out.println("Student doesn't exists. Type his skill again.");
        }
        this.hiredStudents.add(this.students.stream().filter(s -> s.studentSkill.equals(hiredStudentSkill)).findFirst().get());
        this.moneys -= 200;
    }

    public void fireStudent() {
        var scanner = new Scanner(System.in);
        var firedStudentSkill = scanner.nextLine();
        this.hiredStudents.remove(this.students.stream().filter(s -> s.studentSkill.equals(firedStudentSkill)).findFirst().get());
        this.moneys -= 100;
    }

    public boolean assignProject(int projectNumber) {
        var projectAssigned = false;
        var selectedProject = this.availableProjects.get(projectNumber);

        if (selectedProject.complexity != ProjectComplexity.HARD) {
            projectAssigned = true;
        } else {
            if (this.hiredWorkers.stream().map(w -> w.specialization).anyMatch(w -> w == "Programmer"))
                projectAssigned = true;
        }

        return projectAssigned;
    }

    public void findNewClient() {
        if (this.daysToFindNewClient == 5) {
            System.out.print(" You found new client / project!");
            this.availableProjects.add(new Project(this.currentProject.currentDate));
            this.displayAvailableProjects();
            this.availableProjects.get(this.availableProjects.size() - 1);

        }
        this.daysToFindNewClient++;

    }
}
