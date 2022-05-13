package com.company;
import com.company.client.ClientDetails;
import com.company.client.ClientType;
import com.company.project.Project;
import com.company.project.ProjectComplexity;
import com.company.workers.Student;
import com.company.workers.Subcontractor;
import java.util.*;

public class Game {
    public ArrayList<Subcontractor> workersToHire;
    public ArrayList<Subcontractor> hiredWorkers = new ArrayList<>();
    public ArrayList<Student> students;
    public ArrayList<Student> hiredStudents;
    public ClientDetails clientDetails;
    private ArrayList<Project> availableProjects;
    private Project currentProject;
    private Double moneys = 0d;
    private Double startMoneys = 0d;
    private Double costOfWorkers = 0d;
    private Double dailyCosts = 0d;
    public int bigProjectsQuantityWithoutOwner = 0;
    private int taxesDaysMade = 0;
    private int daysToNewAvailableProject = 0;
    private int projectSelectedNumber = 0;
    private int daysToFindNewClient = 0;
    private int daysToFinishCurrentProject = 0;
    private int paymentDelayDaysLeft = 0;
    private int paymentDelayAmount = 0;
    private boolean isFixNeeded = false;
    private Calendar currentDate = new GregorianCalendar();

    public void playGame() {
        System.out.println("\n");

        // Game initialization
        this.startGame();

        // Global variables to count also between days
        var allConditions = false;
        var dayNumber = 1;
        var projectsQuantity = 0;
        var tempCurrentDate = this.currentDate;
        var workOnlyAlone = false;
        var isOwnerWorking = false;
        this.getEmployeesToHireString();
        this.getStudentsString();

        // Play till win or lose
        while (!allConditions) {
            System.out.println("\n\nToday is day no." + dayNumber + "    Date: " + this.currentDate.getTime());

            // Check if taxes were done & take 10% of your moneys if new month
            if (!checkForNextMonth(this.currentDate, tempCurrentDate)) {
                System.out.println("You lost (your money, your wife and your house).");
                break;
            }

            isOwnerWorking = this.isOwnerWorkingToday();
            workOnlyAlone = (this.currentDate.get(Calendar.DAY_OF_WEEK) == 7 || this.currentDate.get(Calendar.DAY_OF_WEEK) == 0);
            if(!isOwnerWorking) {
                this.currentDate = addDay(this.currentDate);
                dayNumber++;
                tempCurrentDate = this.currentDate;
                continue;
            }

            this.readPlayerOption();

//                this.currentProject.complexity.getValue();

            if (workOnlyAlone) {

            }

            if (this.checkCondition(bigProjectsQuantityWithoutOwner, false, false, this.moneys)) {
                System.out.println("YOU WON!");
                System.out.println("Your moneys: " + this.moneys);
                System.out.println("Your workers: " + this.hiredWorkers);
                System.out.println("Projects in your organization: " + projectsQuantity);
            }

            if (this.workersToHire != null) {
                for (var subcontractor : this.workersToHire) {
                    if (subcontractor.isHired)
                        this.costOfWorkers += subcontractor.dailyCosts;
                }
            }

            this.moneys -= this.costOfWorkers;

            if (this.hiredWorkers != null) {
                this.hiredWorkers.stream().forEach(t -> this.dailyCosts += t.dailyCosts);
            }
            this.currentDate = addDay(this.currentDate);
            dayNumber++;
            tempCurrentDate = this.currentDate;
        }

    }

    public static Calendar getStartDate() {
        var calendar = Calendar.getInstance();
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
        if (moneys <= 0) {
            return false;
        }
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
        this.workersToHire = this.setSubcontractors();
        this.students = this.setStudents();

        // Start game with 5-15 projects, selected by user
        var startDate = getStartDate();
        this.startGameAddProjects(startDate);
        var randomGenerator = new RandomGenerator();
        var randomNumber = randomGenerator.getRandomValue(0, 500);
        this.moneys = Double.valueOf(randomNumber * 100);
        if (this.moneys < 5000)
            this.moneys += 5000;
        this.startMoneys = this.moneys;
        this.currentDate = this.getStartDate();

        System.out.println("\nYou're starting at:  " + this.currentDate.getTime());
        System.out.println("Your moneys:         " + this.startMoneys + " PLN");
        System.out.println("Your workers:        " + "None.");
        System.out.println("\nAvailable projects:  ");
        this.displayAvailableProjects();
        System.out.println("\n");

        return new Game(this.workersToHire, this.students, clientDetails);
    }

    public Game(ArrayList<Subcontractor> subcontractors, ArrayList<Student> students, ClientDetails clientDetails) {
        this.workersToHire = subcontractors;
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

    public ArrayList<Subcontractor> getWorkersToHire() {
        return this.workersToHire;
    }

    public void getEmployeesToHireString() {
        var i = 1;
        var template = "%-10s %-15s %-15s %-15s %-10s";
        System.out.println("Workers to employ:");
        System.out.printf(template, "Worker no.", "Worker name", "Specialization", "Daily Costs", "Is hired?");
        for(var workerToHire : this.workersToHire) {
            System.out.println("");
            var isHired = workerToHire.isHired ? "Yes" : "No";
            System.out.printf(template, i, workerToHire.name, workerToHire.specialization, workerToHire.dailyCosts, isHired);
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
        if (this.currentDate.get(Calendar.DAY_OF_WEEK) == 7 || this.currentDate.get(Calendar.DAY_OF_WEEK) == 0) {
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
        else {
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

    public void readPlayerOption() {
        System.out.println("Select option for the next day (1-10): (1-contract/2-client/3-program/4-test/5-handOver/6-employee/7-fire/8-student/9-fire/10-tax/11-showProject/12-showYour");
        var scanner = new Scanner(System.in);
        var userInput = scanner.nextLine();
        var userChoice = 0;
        try {
            userChoice = Integer.parseInt(userInput);
        } catch(Exception e) {
            System.out.println("\n Wrong choice! " + e);
            System.out.println("Type number from 1 to 10 again.");
            this.readPlayerOption();
        }

        if ( userChoice < 1 && userChoice> 10) {
            System.out.println("\n Wrong choice!");
            System.out.println("Type number from 1 to 10 again.");
            this.readPlayerOption();
        }

        if (this.isFixNeeded) {
            userChoice = 3;
            System.out.println("Today you have no choice, you're fixing bugs.");
        }

        switch(userChoice) {
            case 1: {
                if (this.currentProject != null) {
                    System.out.println("First you must finish current project.");
                    this.readPlayerOption();
                }

                this.displayAvailableProjects();
                System.out.println("");
                System.out.println("Which contract do you want to get? (number)");
                userInput = scanner.nextLine();
                userChoice = Integer.parseInt(userInput);

                if (userChoice > 0 && userChoice < this.availableProjects.size())
                    this.projectSelectedNumber = userChoice - 1;
                else {
                    System.out.println("Project does not exists");
                    this.readPlayerOption();
                }
                this.assignProject(this.projectSelectedNumber);

                System.out.println("You've signed new contract!");
                break;
            }
            case 2: {
                this.findNewClient();
                System.out.println("You're searching for a new client!");
                break;

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
                break;

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
                break;
            }
            case 5: {
                if (this.currentProject == null) {
                    System.out.println("Currently you don't have any projects to hand over!");
                    this.readPlayerOption();
                }
                this.handProjectOver();
                System.out.println("You're handing over the project!");
                break;
            }
            case 6: {
                if (this.workersToHire == null) {
                    System.out.println("There are no new employees to hire!");
                    this.readPlayerOption();
                }
                this.hireEmployee();
                System.out.println("You've got new employee!");
                break;
            }
            case 7: {
                if (this.hiredWorkers == null) {
                    System.out.println("You don't have any employees");
                    this.readPlayerOption();
                }
                this.fireEmployee();
                System.out.println("You fired old employee!");
                break;
            }
            case 8: {
                if (this.hiredStudents == null) {
                    System.out.println("There are no new students to hire!");
                    this.readPlayerOption();
                }
                System.out.println("You've got new student!");
                this.hiredStudents = (ArrayList<Student>) this.students.stream().filter(t -> t.isHired).toList();
                this.hiredStudents.remove(hiredStudents.size());
                this.workersToHire = hiredWorkers;
                break;
            }
            case 9: {
                if (this.hiredWorkers == null) {
                    System.out.println("You don't have any students");
                    this.readPlayerOption();
                }
                System.out.println("You've fired student!");
                break;
            }
            case 10: {
                if (this.taxesDaysMade > 2) {
                    System.out.println("You did all the taxes! Choose your task today again.");
                    this.readPlayerOption();
                }
                System.out.println("You're spending this day only on taxes (ZUS).");
                this.taxesDaysMade++;
                break;
            }
            case 11: {
                if (this.currentProject == null) {
                    System.out.println("You don't have any project selected. Choose your task for today again.");
                    this.readPlayerOption();
                }
                this.showCurrentProject();
            }
            case 12: {
                this.showPlayerDetails();
            }
            default: {
                System.out.println("You're choose nothing. Choose again.");
                this.readPlayerOption();
            }
        }
    }

    public void startGameAddProjects(Calendar startDate) {
        var scanner = new Scanner(System.in);
        var startProjectsQuantity = 0;

        while (startProjectsQuantity < 5 || startProjectsQuantity > 15) {
            System.out.println("How many available projects do you want to start with? (5 - 15)");
            var userInput = scanner.nextLine();
            try {
                startProjectsQuantity = Integer.parseInt(userInput);
            } catch(Exception e) {
                System.out.println("Wrong number!" + e);
                System.out.println("Type number from 5 to 15 again.");
                this.startGameAddProjects(startDate);
            }
            if (startProjectsQuantity >= 5 && startProjectsQuantity <= 15) {
                this.availableProjects = this.initializeProjects(startDate, startProjectsQuantity);
            } else {
                System.out.println("Wrong number! Type number from 5 to 15 again");
                this.startGameAddProjects(startDate);
            }
        }
    }

    public void displayAvailableProjects() {
        int i = 1;
        var template = "%-12s %-30s %-20s %-70s %-15s %-16s %-32s %-32s";
        System.out.printf(template, "Project no.", "Name ", "Project complexity",  "Needed technologies", "Project price", "Project penalty", "Project deadline", "Paycheck Deadline");
        for (var proj : this.availableProjects) {
            System.out.println("");
            System.out.printf(template, i++, proj.projectName, proj.complexity, proj.neededTechnologies, proj.price, proj.penalty, proj.deadline.getTime(), proj.paycheckDeadline.getTime());
        }
        System.out.println("");
    }

    public void hireEmployee() {
        var scanner = new Scanner(System.in);
        System.out.print("Input name of the employee you want to hire: ");
        var hiredEmployeeName = scanner.nextLine();
        if (hiredEmployeeName.length() < 3) {
            System.out.println("Don't try me, please input his/her real name: ");
            this.hireEmployee();
        }
        if (this.workersToHire.stream().filter(s -> s.name.equals(hiredEmployeeName)).count() < 0) {
            System.out.println("Person doesn't exists. Type name again.");
        }
        this.hiredWorkers.add(this.workersToHire.stream().filter(s -> s.name.equals(hiredEmployeeName)).findFirst().get());
        this.workersToHire.remove(hiredWorkers.size());
        this.moneys -= 500;
    }

    public void fireEmployee() {
        System.out.println("Enter employee name to fire: ");
        var scanner = new Scanner(System.in);
        var firedEmployeeName = scanner.nextLine();
        if (firedEmployeeName.length() < 3) {
            System.out.println("Don't try me, please input his/her real name: ");
            this.fireEmployee();
        }
        this.hiredWorkers.remove(this.workersToHire.stream().filter(s -> s.name.equals(firedEmployeeName)).findFirst().get());
        this.moneys -= 200;
    }

    public void hireStudent() {
        var scanner = new Scanner(System.in);
        System.out.println("Hire student to do this project.");
        var hiredStudentSkill = scanner.nextLine();
        if (this.students.stream().filter(s -> s.studentSkill.equals(hiredStudentSkill)).count() < 0) {
            System.out.println("Student doesn't exists. Type his skill again.");
        }
        this.hiredStudents.add(this.students.stream().filter(s -> s.studentSkill.equals(hiredStudentSkill)).findFirst().get());
        this.moneys -= 200;
    }

    public void assignProject(int projectNumber) {
        var selectedProject = this.availableProjects.get(projectNumber);

        if (selectedProject.complexity.getKey() != ProjectComplexity.HARD) {
            this.currentProject = selectedProject;
            this.daysToFinishCurrentProject = selectedProject.complexity.getValue();
        } else {
            if (this.hiredWorkers != null) {
                if (this.hiredWorkers.stream().map(w -> w.specialization).anyMatch(w -> w == "Programmer"))
                {
                    this.currentProject = selectedProject;
                } else {
                    System.out.println("You don't have any programmers to do HARD tasks. Choose easier project.");
                    this.readPlayerOption();
                }
            } else {
                System.out.println("No employees. You can't assign HARD projects now. Choose easier project.");
                this.readPlayerOption();
            }
        }
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

    public void showPlayerDetails() {
        System.out.print("Your details are: ");
        System.out.print("Moneys: " + this.moneys);
        System.out.print("Current project: " + this.currentProject.projectName + ", that will be ready for " + this.daysToFinishCurrentProject + "days");
        System.out.print("Taxes days made: " + this.taxesDaysMade);
        System.out.print("Your daily costs are: : " + this.dailyCosts);
    }

    public void showCurrentProject() {
        var template = "%-30s %-20s %-15s %-16s %-32s %-32s %-80s";
        System.out.printf(template, "Name ", "Project complexity", "Project price", "Project penalty", "Project deadline", "Paycheck Deadline", "Needed technologies");
        System.out.println("");
        System.out.printf(template, this.currentProject.projectName, this.currentProject.complexity, this.currentProject.price, this.currentProject.penalty, this.currentProject.deadline.getTime(), this.currentProject.paycheckDeadline.getTime(), this.currentProject.neededTechnologies);
    }
}
