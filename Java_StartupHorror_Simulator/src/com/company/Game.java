package com.company;
import com.company.client.ClientDetails;
import com.company.client.ClientType;
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
    private Double costOfWorkers = 0d;
    private Double dailyCosts = 0d;
    public int bigProjectsQuantityWithoutOwner = 0;
    private int taxesDaysMade = 0;
    private int daysToNewAvailableProject = 0;
    private int projectSelected = 0;
    private int daysToFindNewClient = 0;
    private int daysToFinishCurrentProject;
    private int paymentDelayDaysLeft;
    private int paymentDelayAmount;
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
        this.startGameAddProjects(startDate);

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
            if (!checkForNextMonth(startDate, tempCurrentDate) || moneys <= 0) {
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
                this.currentProject.complexity.getValue();


                // testing day
            } else if (todayOption == 4) {
                System.out.println("YOU WONNNN!");

                // hand the project over day
            } else if (todayOption == 5) {
                this.handProjectOver();

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

            if (this.subcontractors != null) {
                for (var subcontractor : this.subcontractors) {
                    if (subcontractor.isHired)
                        this.costOfWorkers += subcontractor.dailyCosts;
                }
            }

            this.moneys -= this.costOfWorkers;

            if (this.hiredWorkers != null) {
                this.hiredWorkers.stream().forEach(t -> this.dailyCosts += t.dailyCosts);
            }
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
        clientDetails.setClientDetails();
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
        System.out.println("Do you want to work today? (0/1)");
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
        System.out.println("Select option for the next day (1-10): (1-contract/2-client/3-program/4-test/5-handOver/6-employee/7-fire/8-student/9-fire/10-tax");
        var scanner = new Scanner(System.in);
        var userInput = scanner.nextLine();
        var userChoice = Integer.parseInt(userInput);
        switch(userChoice) {
            case 1: {
                if (this.currentProject != null) {
                    System.out.println("First you must finish current project.");
                    this.readPlayerOption();
                }
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
                if (currentProject == null) {
                    System.out.println("Currently you don't have any projects to do!");
                    this.readPlayerOption();
                }
                if (this.daysToFinishCurrentProject == 0) {
                    System.out.println("Your program is ready to last test or hand over!");
                    this.readPlayerOption();
                }
                System.out.println("You're programmer today!");
                return 3;
            }
            case 4: {
                if (this.currentProject == null) {
                    System.out.println("Currently you don't have any projects to test!");
                    this.readPlayerOption();
                }
                if (this.currentProject.chancesToBugs == 0) {
                    System.out.println("Your program doesn't have any bugs for sure!");
                    this.readPlayerOption();
                }
                System.out.println("You're tester today!");
                return 4;
            }
            case 5: {
                if (this.currentProject == null) {
                    System.out.println("Currently you don't have any projects to hand over!");
                    this.readPlayerOption();
                }
                System.out.println("You're handing over the project!");
                return 5;
            }
            case 6: {
                if (this.subcontractors == null) {
                    System.out.println("There are no new employees to hire!");
                    this.readPlayerOption();
                }
                System.out.println("You've got new employee!");
                return 6;
            }
            case 7: {
                if (this.hiredWorkers == null) {
                    System.out.println("You don't have any employees");
                    this.readPlayerOption();
                }
                System.out.println("You fired old employee!");
                return 7;
            }
            case 8: {
                if (this.hiredStudents == null) {
                    System.out.println("There are no new students to hire!");
                    this.readPlayerOption();
                }
                System.out.println("You've got new student!");
                return 8;
            }
            case 9: {
                if (this.hiredWorkers == null) {
                    System.out.println("You don't have any students");
                    this.readPlayerOption();
                }
                System.out.println("You've fired student!");
                return 9;
            }
            case 10: {
                if (this.taxesDaysMade > 2) {
                    System.out.println("You did all the taxes!");
                    this.readPlayerOption();
                }
                System.out.println("You're spending this day only on taxes (ZUS).");
                return 10;
            }
            default: {
                System.out.println("You're choose nothing. Choose again.");
                this.readPlayerOption();
                return 0;
            }
        }
    }

    public void startGameAddProjects(Calendar startDate) {
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

        if (selectedProject.complexity.getKey() != ProjectComplexity.HARD) {
            projectAssigned = true;
            this.daysToFinishCurrentProject = selectedProject.complexity.getValue();
        } else {
            if (this.hiredWorkers != null) {
                if (this.hiredWorkers.stream().map(w -> w.specialization).anyMatch(w -> w == "Programmer"))
                    projectAssigned = true;
            }
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

    public void handProjectOver() {
        var randomGenerator = new RandomGenerator();
        var random = randomGenerator.getRandomValue(1, 100);        // chances generator

        var currentClient = this.currentProject.clientDetails;
        System.out.print("Your client type was: " + currentClient.getClientType());
        if (this.currentProject.chancesToBugs == 0) {
            System.out.print("Congratulations, your project is perfect, client is satisfied");

            if (currentClient.getClientType() == ClientType.GOOD) {
                if (random <= 30) {
                    this.paymentDelayDaysLeft = 7;
                    this.paymentDelayAmount = this.currentProject.price;
                } else {
                    this.moneys += this.currentProject.price;
                }
            } else if (currentClient.getClientType() == ClientType.AVERAGE) {
                this.moneys += this.currentProject.price;
            }
            else {
                if (random <= 5) {
                    this.paymentDelayDaysLeft = 30;
                    this.paymentDelayAmount = this.currentProject.price;
                } else if (random <= 30) {
                    this.paymentDelayDaysLeft = 7;
                    this.paymentDelayAmount = this.currentProject.price;
                } else {
                    this.moneys += this.currentProject.price;
                }
            }

        } else {
            if (currentClient.getClientType() == ClientType.GOOD) {
                System.out.print("Your project has bugs, but client is satisfied");
                if (random <= 30) {
                    this.paymentDelayDaysLeft = 7;
                    this.paymentDelayAmount = this.currentProject.price;
                } else {
                    this.moneys += this.currentProject.price;
                }
            } else if (currentClient.getClientType() == ClientType.AVERAGE) {
                System.out.print("Your project has bugs, but client is satisfied");
                this.moneys += this.currentProject.price;
                this.moneys -= this.currentProject.penalty;
                if (random <= 50) {
                    this.loseContact();
                }

            }
            else {
                System.out.print("It will be a war.");
                this.moneys -= currentProject.penalty;

                if (random > 1) {
                    if (random <= 5) {
                        this.paymentDelayDaysLeft = 30;
                        this.paymentDelayAmount = this.currentProject.price;
                    } else if (random <= 30) {
                        this.paymentDelayDaysLeft = 7;
                        this.paymentDelayAmount = this.currentProject.price;
                    } else {
                        this.moneys += this.currentProject.price;
                    }
                }

                this.loseContact();
            }

        }
    }

    public void programDay() {
        this.currentProject.chancesToBugs += 0.05;
        this.daysToFinishCurrentProject -= 1;
    }

    public void testDay() {
        this.currentProject.chancesToBugs -= 0.2;
    }

    public void loseContact() {
        this.availableProjects.remove(this.availableProjects.stream().filter(p -> p.clientDetails == this.clientDetails));
    }
}
