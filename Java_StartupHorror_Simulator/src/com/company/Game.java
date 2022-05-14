package com.company;
import com.company.client.ClientDetails;
import com.company.client.ClientType;
import com.company.project.Project;
import com.company.project.ProjectComplexity;
import com.company.workers.Student;
import com.company.workers.Subcontractor;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    private ArrayList<Subcontractor> workersToHire;
    private ArrayList<Subcontractor> hiredWorkers = new ArrayList<>();
    private ArrayList<Student> students;
    private ArrayList<Student> hiredStudents;
    private ClientDetails clientDetails;
    private ArrayList<Project> availableProjects;
    private Project currentProject;
    private Double moneys = 0d;
    private Double startMoneys = 0d;
    private Double dailyCostsOfWorkers = 0d;
    private Double dailyCosts = 0d;

    private int bigProjectsQuantityWithoutOwner = 0;
    private int projectFoundedBySeller = 0;
    private boolean wasOwnerADevInBigProject = false;

    private int taxesDaysMade = 0;
    private int daysLeftToNewAvailableProject = 5;
    private int daysToFindNewClient = 5;
    private int daysToFinishCurrentProject = 0;

    private int projectSelectedNumber = 0;
    private int paymentDelayDaysLeft = 0;
    private int paymentDelayAmount = 0;
    private Calendar currentDate = new GregorianCalendar();

    private Project currentProjectByStudent;
    private Student currentProjectByStudentStudentDetails;
    private double daysToFinishCurrentProjectByStudent = 0d;

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

            if (this.paymentDelayDaysLeft > 0) {
                this.paymentDelayDaysLeft--;
            } else if (this.paymentDelayAmount > 0) {
                this.moneys += this.paymentDelayAmount;
                this.paymentDelayAmount = 0;
            }

            // Check if taxes were done & take 10% of your moneys if new month
            if (!checkForNextMonth(this.currentDate, tempCurrentDate)) {
                System.out.println("You lost (your money, your wife and your house).");
                break;
            }

            isOwnerWorking = this.isOwnerWorkingToday();

            // Check if weekend
            workOnlyAlone = (this.currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || this.currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
            if(!isOwnerWorking && this.hiredWorkers == null) {
                this.currentDate = addDay(this.currentDate);
                dayNumber++;
                tempCurrentDate = this.currentDate;
                continue;
            }

            // Main function of tour-based game
            this.readPlayerOption();

            // Count workers
            if (!workOnlyAlone && this.currentProject != null) {
                this.moneys -= this.dailyCostsOfWorkers;
                this.calculateProjectProgressByEmployees();
            }

            // Count student
            if (!workOnlyAlone && this.currentProjectByStudent != null) {
                this.moneys -= this.dailyCostsOfWorkers;
                this.calculateProjectProgressByStudent();
            }

            if (this.checkCondition(this.bigProjectsQuantityWithoutOwner, this.projectFoundedBySeller, this.moneys)) {
                System.out.println("YOU WON!");
                System.out.println("Your moneys: " + this.moneys);
                System.out.println("Your workers: " + this.hiredWorkers);
                System.out.println("Projects in your organization: " + projectsQuantity);
            }

            addDay(this.currentDate);
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
            this.moneys = (this.moneys - this.startMoneys) * 0.9;
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

    public boolean checkCondition(int bigProjectsQuantityWithoutOwner, int wasBigClientFromOurSeller, double moneys) {
        if (bigProjectsQuantityWithoutOwner >= 3 && wasBigClientFromOurSeller != 0 && moneys > this.startMoneys)
            return true;

        return false;
    }
    
    public boolean checkIfWeekend(Date currentDate) {
        if (currentDate.getDay() == 6 || currentDate.getDay() == 0)
            return true;

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
        System.out.println("");
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
        if (this.currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || this.currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            if (this.currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                System.out.println("It's weekend - Saturday. Do you want to work today? (0/1)");
            else
                System.out.println("It's weekend - Sunday. Do you want to work today? (0/1)");

            var scanner = new Scanner(System.in);
            var userInput = scanner.nextLine();
            var userChoice = Integer.parseInt(userInput);
            if (userChoice == 0) {
                System.out.println("You're not working today");
                return false;
            } else if (userChoice == 1) {
                System.out.println("You're working today");
                return true;
            } else {
                System.out.println("What?");
                this.isOwnerWorkingToday();
                return false;
            }
        } else {
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
        var isRepeat = true;
        while (isRepeat) {
            System.out.println("\nSelect option for the next day (1-10): ( 1-contract / 2-client / 3-program / 4-test / 5-handOver / 6-employee / 7-fire / 8-student / 9-fire / 10-tax / 11-showProject / 12-showCompany )");
            var scanner = new Scanner(System.in);
            var userInput = scanner.nextLine();
            var userChoice = 0;
            try {
                userChoice = Integer.parseInt(userInput);
            } catch(Exception e) {
                System.out.println("\n What? It's not a number." + e);
                System.out.println("Type number from 1 to 12 again.");
                continue;
            }

            if ( userChoice < 1 && userChoice > 12) {
                System.out.println("\n Wrong choice!");
                System.out.println("Type number from 1 to 12 again.");
                continue;
            }

            if (this.currentProject != null && this.daysToFinishCurrentProject == 0 && this.currentProject.chancesToBugs == 0) {
                System.out.println("Your program is ready to hand over! It's fully done & tested. You can firmly hand over a project.");
                continue;
            }

            if (this.currentProjectByStudent != null && this.daysToFinishCurrentProjectByStudent == 0 && this.currentProject.chancesToBugs == 0) {
                System.out.println("Student's program is ready to hand over! It's fully done & tested. You can firmly hand over a project.");
                continue;
            }

            if (userChoice == 1) {
                if (this.currentProject != null) {
                    System.out.println("First you must finish current project. Choose activity for today again.");
                    continue;
                }

                this.displayAvailableProjects();
                System.out.println("");
                System.out.println("Will you do it on your own or hire a student to do it? (me / student)");
                var userOrStudentDo = scanner.nextLine();
                userOrStudentDo = userOrStudentDo.toLowerCase(Locale.ROOT);
                if (!userOrStudentDo.equals("me") && !userOrStudentDo.equals("student")) {
                    System.out.println("Who? Choose again.");
                    continue;
                }

                System.out.println("Which contract do you want to get? (number)");
                userInput = scanner.nextLine();
                try {
                    userChoice = Integer.parseInt(userInput);
                } catch (Exception e) {
                    System.out.println(e);
                }

                if (userChoice > 0 && userChoice <= this.availableProjects.size())
                    this.projectSelectedNumber = userChoice - 1;
                else {
                    System.out.println("Project does not exists");
                    continue;
                }

                userOrStudentDo = userOrStudentDo.toLowerCase(Locale.ROOT);
                if (userOrStudentDo.equals("student")) {
                    if (this.currentProjectByStudent != null) {
                        System.out.println("You can't assign the next project for a student in the same time!");
                        continue;
                    }
                    this.assignProjectForStudent(this.projectSelectedNumber);
                } else {
                    this.assignProjectForYourself(this.projectSelectedNumber);
                }

                if (currentProject.complexity.getKey().equals(ProjectComplexity.HARD)) {
                    this.wasOwnerADevInBigProject = true;
                }
                System.out.println("You've signed new contract!");
                break;
            }
            if (userChoice == 2) {
                this.findNewClient();
                if (this.daysToFindNewClient >= 0) {
                    System.out.println("You're searching for a new client!");
                    System.out.println("Days needed to find new client: " + this.daysToFindNewClient);
                }
                break;

            }
            if (userChoice == 3) {
                if (currentProject == null) {
                    System.out.println("Currently you don't have any projects to do! Choose activity for today again.");
                    continue;
                }
                if (this.daysToFinishCurrentProject == 0) {
                    System.out.println("Your program is ready to last test or hand over!");
                    continue;
                }

                this.programDay();
                System.out.println("You're programmer today!");
                break;

            }
            if (userChoice == 4) {
                if (this.currentProject == null) {
                    System.out.println("Currently you don't have any projects to test! Choose activity for today again.");
                    continue;
                }
                if (this.currentProject.chancesToBugs <= 0) {
                    System.out.println("Your program doesn't have any bugs for sure! Choose activity for today again.");
                    continue;
                }

                this.testDay();
                System.out.println("You're tester today!");
                break;
            }
            if (userChoice == 5) {
                if (this.currentProject == null && this.currentProjectByStudent == null) {
                    System.out.println("Currently you don't have any projects to hand over! Choose activity for today again.");
                    continue;
                }

                var meOrStudent = "";

                if (this.currentProject != null && this.currentProjectByStudent != null) {
                    System.out.println("Do you want to hand your project or student's project over? (my/student)");
                    meOrStudent = scanner.nextLine();
                    meOrStudent = meOrStudent.toLowerCase(Locale.ROOT);
                }

                if (this.currentProject != null || meOrStudent.equals("my")) {
                    if (this.currentProject.chancesToBugs > 0) {
                        System.out.println("Your program has probably some bugs, are you sure you want to hand it over? (yes/no)");
                        var handProjectOver = scanner.nextLine();
                        handProjectOver = handProjectOver.toLowerCase(Locale.ROOT);
                        if (handProjectOver.equals("no")) {
                            continue;
                        }
                    }
                    this.handProjectOver();
                    System.out.println("You're handing over the project!");
                    break;
                } else if (this.currentProjectByStudent != null || meOrStudent.equals("my")) {
                    if (this.currentProjectByStudent.chancesToBugs > 0) {
                        System.out.println("This student's program has probably some bugs, are you sure you want to hand it over? (yes/no)");
                        var handProjectOver = scanner.nextLine();
                        handProjectOver = handProjectOver.toLowerCase(Locale.ROOT);
                        if (handProjectOver.equals("no")) {
                            continue;
                        }
                    }
                    this.handStudentProjectOver();
                    System.out.println("You're handing over the project made by student!");
                    break;
                }
            }
            if (userChoice == 6) {
                if (this.workersToHire == null) {
                    System.out.println("There are no new employees to hire! Choose activity for today again.");
                    continue;
                }
                this.hireEmployee();
                System.out.println("You've got new employee!");
                break;
            }
            if (userChoice == 7) {
                if (this.hiredWorkers == null) {
                    System.out.println("You don't have any employees. Choose activity for today again.");
                    continue;

                }
                this.fireEmployee();
                System.out.println("You fired old employee!");
                break;
            }
            if (userChoice == 8) {
                if (this.students == null) {
                    System.out.println("There are no new students to hire! Choose activity for today again.");
                    continue;
                }
                this.hireStudent();
                System.out.println("You've got new student!");
                break;
            }
            if (userChoice == 9) {
                if (this.hiredStudents == null) {
                    System.out.println("You don't have any students. Choose activity for today again.");
                    continue;
                }
                this.fireStudent();
                System.out.println("You've fired student!");
                break;
            }
            if (userChoice == 10) {
                if (this.taxesDaysMade > 2) {
                    System.out.println("You did all the taxes! Choose your task today again.");
                    continue;
                }
                System.out.println("You're spending this day only on taxes (ZUS).");
                this.taxesDaysMade++;
                break;
            }
            if (userChoice == 11) {
                if (this.currentProject == null) {
                    System.out.println("You don't have any project selected. Choose your task for today again.");
                    continue;
                }
                this.showCurrentProject();
            }
            if (userChoice == 12) {
                this.showPlayerDetails();
                continue;
            }
            isRepeat = false;
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
                continue;
            }
            if (startProjectsQuantity >= 5 && startProjectsQuantity <= 15) {
                this.availableProjects = this.initializeProjects(startDate, startProjectsQuantity);
                break;
            } else {
                System.out.println("Wrong number! Type number from 5 to 15 again");
                continue;
            }
        }
    }

    public void displayAvailableProjects() {
        int i = 1;
        var template = "%-12s %-30s %-20s %-20s %-70s %-15s %-16s %-32s %-32s";
        System.out.printf(template, "Project no.", "Name ", "Project complexity", "Project days to do",  "Needed technologies", "Project price", "Project penalty", "Project deadline", "Paycheck Deadline");
        for (var proj : this.availableProjects) {
            System.out.println("");
            System.out.printf(template, i++, proj.projectName, proj.complexity.getKey(), proj.complexity.getValue() + 1, proj.neededTechnologies, proj.price, proj.penalty, proj.deadline.getTime(), proj.paycheckDeadline.getTime());
        }
        System.out.println("");
    }

    public void hireEmployee() {
        while (true) {
            var scanner = new Scanner(System.in);
            System.out.print("Input name of the employee you want to hire: ");
            var hiredEmployeeName = scanner.nextLine();

            if (hiredEmployeeName.length() < 3) {
                System.out.println("Don't try me, please input his/her real name: ");
                continue;
            }
            if (this.workersToHire.stream().filter(s -> s.name.equals(hiredEmployeeName)).count() == 0) {
                System.out.println("Person doesn't exists. Type name again.");
            }

            var worker = new ArrayList<Subcontractor>();
            this.workersToHire.forEach(w -> {
                if (w.name.equals(hiredEmployeeName)) {
                    worker.add(w);
                }
            });

            if (worker.size() == 0) {
                System.out.println("Something went wrong. Type name again.");
                continue;
            }

            var selectedWorker = worker.get(0);
            if (worker.size() > 1) {
                System.out.println("There are " + worker.size() + " people with the same name. Enter which one do you want to hire (1 / 2 / 3 etc.)");
                var indexString = scanner.nextLine();
                try {
                    var employeeIndex = Integer.parseInt(indexString);
                    selectedWorker = worker.get(employeeIndex - 1);
                } catch(Exception e) {
                    System.out.println("Wrong number!" + e);
                    System.out.println("Type number again.");
                }
            }

            this.hiredWorkers.add(selectedWorker);

//        this.hiredWorkers.add(this.workersToHire.stream().filter(s -> s.name.equals(hiredEmployeeName)).findFirst().get());
            AtomicInteger indexOfWorkersToHire = new AtomicInteger();
            AtomicInteger i = new AtomicInteger();
            Subcontractor finalSelectedWorker = selectedWorker;
            this.workersToHire.forEach(w -> {
                if (!w.equals(finalSelectedWorker))
                    i.getAndIncrement();
                else
                    indexOfWorkersToHire.set(i.get());
            });
            this.workersToHire.remove(indexOfWorkersToHire.get());
            this.moneys -= 500;
            this.dailyCostsOfWorkers += selectedWorker.dailyCosts;
            break;
        }
    }

    public void fireEmployee() {
        while (true) {
            System.out.println("Enter employee name to fire: ");
            var scanner = new Scanner(System.in);
            var firedEmployeeName = scanner.nextLine();
            if (firedEmployeeName.length() < 3) {
                System.out.println("Don't try me, please input his/her real name: ");
                continue;
            }
            this.hiredWorkers.remove(this.workersToHire.stream().filter(s -> s.name.equals(firedEmployeeName)).findFirst().get());
            this.moneys -= 200;
            break;
        }
    }

    public void hireStudent() {
        while (true) {
            var scanner = new Scanner(System.in);
            System.out.println("Hire student to do this project. (skill - good/avg/bad)");
            var hiredStudentSkill = scanner.nextLine();
            String finalHiredStudentSkill = hiredStudentSkill.toUpperCase(Locale.ROOT);
            if (this.students.stream().filter(s -> s.studentSkill.equals(finalHiredStudentSkill)).count() < 0) {
                System.out.println("Student doesn't exists. Type his skill again.");
                continue;
            }
            this.hiredStudents.add(this.students.stream().filter(s -> s.studentSkill.equals(finalHiredStudentSkill)).findFirst().get());
            this.moneys -= 200;
            break;
        }
    }

    public void fireStudent() {
        while (true) {
            System.out.println("Enter student skill to fire: (good/avg/bad) ");
            var scanner = new Scanner(System.in);
            var firedStudentSkill = scanner.nextLine();
            String finalHiredStudentSkill = firedStudentSkill.toUpperCase(Locale.ROOT);
            if (finalHiredStudentSkill.length() < 3) {
                System.out.println("Don't try me, please input again his skill: ");
                continue;
            }
            this.hiredStudents.remove(this.students.stream().filter(s -> s.studentSkill.equals(finalHiredStudentSkill)).findFirst().get());
            this.moneys -= 100;
            break;
        }
    }

    public void assignProjectForYourself(int projectNumber) {
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

    public void assignProjectForStudent(int projectNumber) {
        var selectedProject = this.availableProjects.get(projectNumber);

        if (selectedProject.complexity.getKey() == ProjectComplexity.EASY) {
            System.out.println("Enter student skill to hire: (bad/avg/good)");
            var scanner = new Scanner(System.in);
            var studentSkills = scanner.nextLine();
            studentSkills = studentSkills.toUpperCase(Locale.ROOT);
            this.currentProjectByStudent = selectedProject;
            String finalStudentSkills = studentSkills;
            this.students.stream().forEach(t -> {
                if (t.studentSkill.equals(finalStudentSkills)) {
                    this.currentProjectByStudentStudentDetails = t;
                }
            });

            // TODO: Fix removing students by index
            if (this.currentProjectByStudentStudentDetails.studentSkill.equals("bad")){
                this.students.remove(0);
                this.currentProjectByStudent.chancesToBugs = 0.20;
                this.currentProjectByStudent.chancesToDelay = 0.20;
            } else if (this.currentProjectByStudentStudentDetails.studentSkill.equals("avg")){
                this.students.remove(1);
                this.currentProjectByStudent.chancesToBugs = 0.10;
                this.currentProjectByStudent.chancesToDelay = 0;
            } else {
                this.students.remove(2);
                this.currentProjectByStudent.chancesToBugs = 0;
                this.currentProjectByStudent.chancesToDelay = 0;
            }

            this.daysToFinishCurrentProjectByStudent = selectedProject.complexity.getValue();
            this.moneys -= 200;
        } else {
            System.out.println("Students can't do NORMAL and HARD projects. Choose easier project.");
            this.readPlayerOption();
        }
    }

    public void findNewClient() {
        if (this.daysToFindNewClient <= 1) {
            System.out.print(" You found new client / project!");
            var date = this.currentDate;
            this.availableProjects.add(new Project(date));
            this.displayAvailableProjects();
            this.availableProjects.get(this.availableProjects.size() - 1);
            this.daysToFindNewClient = 5;
            return;
        }
        this.daysToFindNewClient--;
    }

    public void handProjectOver() {
        // Count properties needed for winning the game
        if (this.currentProject.complexity.getKey().equals(ProjectComplexity.HARD) && this.wasOwnerADevInBigProject) {
            bigProjectsQuantityWithoutOwner++;
        }
        // TODO:
//        if (this.currentProject.foundBySeller) {
//            this.projectFoundBySeller++;
//        }

        // Hand project over
        var randomGenerator = new RandomGenerator();
        var random = randomGenerator.getRandomValue(1, 100);        // chances generator

        var currentClient = this.currentProject.clientDetails;
        System.out.println("Your client type was: " + currentClient.getClientType());
        if (this.currentProject.chancesToBugs == 0) {
            System.out.println(" Congratulations, your project is perfect, client is satisfied :)");

            if (currentClient.getClientType().equals(ClientType.GOOD)) {
                if (random <= 30) {
                    this.paymentDelayDaysLeft = 7;
                    this.paymentDelayAmount = this.currentProject.price;
                } else {
                    this.moneys += this.currentProject.price;
                }
            } else if (currentClient.getClientType().equals(ClientType.AVERAGE)) {
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
            if (currentClient.getClientType().equals(ClientType.GOOD)) {
                System.out.print(" Your project has bugs, but client is satisfied.");
                if (random <= 30) {
                    this.paymentDelayDaysLeft = 7;
                    this.paymentDelayAmount = this.currentProject.price;
                } else {
                    this.moneys += this.currentProject.price;
                }
            } else if (currentClient.getClientType().equals(ClientType.AVERAGE)) {
                System.out.print(" Your project has bugs, but client is satisfied.");
                this.moneys += this.currentProject.price;
                this.moneys -= this.currentProject.penalty;
                if (random <= 50) {
                    this.loseContact();
                }

            }
            else {
                System.out.print(" It will be a war.");
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

    public void handStudentProjectOver() {
        // Hand project over
        var randomGenerator = new RandomGenerator();
        var random = randomGenerator.getRandomValue(1, 100);        // chances generator

        var currentClient = this.currentProject.clientDetails;
        System.out.print("Your client type was: " + currentClient.getClientType());
        if (this.currentProjectByStudent.chancesToBugs == 0) {
            System.out.print(" Congratulations, student's project is perfect, client is satisfied :)");

            if (currentClient.getClientType().equals(ClientType.GOOD)) {
                if (random <= 30) {
                    this.paymentDelayDaysLeft = 7;
                    this.paymentDelayAmount = this.currentProject.price;
                } else {
                    this.moneys += this.currentProject.price;
                }
            } else if (currentClient.getClientType().equals(ClientType.AVERAGE)) {
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
            if (currentClient.getClientType().equals(ClientType.GOOD)) {
                System.out.print(" Your project has bugs, but client is satisfied");
                if (random <= 30) {
                    this.paymentDelayDaysLeft = 7;
                    this.paymentDelayAmount = this.currentProjectByStudent.price;
                } else {
                    this.moneys += this.currentProjectByStudent.price;
                }
            } else if (currentClient.getClientType().equals(ClientType.AVERAGE)) {
                System.out.print(" Your project has bugs, but client is satisfied");
                this.moneys += this.currentProjectByStudent.price;
                this.moneys -= this.currentProjectByStudent.penalty;
                if (random <= 50) {
                    this.loseContact();
                }

            }
            else {
                System.out.print(" It will be a war.");
                this.moneys -= currentProjectByStudent.penalty;

                if (random > 1) {
                    if (random <= 5) {
                        this.paymentDelayDaysLeft = 30;
                        this.paymentDelayAmount = this.currentProjectByStudent.price;
                    } else if (random <= 30) {
                        this.paymentDelayDaysLeft = 7;
                        this.paymentDelayAmount = this.currentProjectByStudent.price;
                    } else {
                        this.moneys += this.currentProjectByStudent.price;
                    }
                }

                this.loseContact();
            }
        }

        this.currentProjectByStudent = null;
        this.currentProjectByStudentStudentDetails = null;
    }

    public void calculateProjectProgressByEmployees() {
        var programmersQuantity = this.hiredWorkers.stream().map(w -> w.specialization.equals("Programmer")).count();
        var testersQuantity = this.hiredWorkers.stream().map(w -> w.specialization.equals("Tester")).count();
        var sellersQuantity = this.hiredWorkers.stream().map(w -> w.specialization.equals("Seller")).count();

        this.daysToFinishCurrentProject -= (int)programmersQuantity;
        this.daysToFindNewClient -= (int)sellersQuantity;

        this.currentProject.chancesToBugs -= 0.15 * testersQuantity;
    }

    public void calculateProjectProgressByStudent() {
        this.daysToFinishCurrentProjectByStudent -= 0.5;
        this.currentProjectByStudent.chancesToBugs -= 0.15;
    }

    public void programDay() {
        this.wasOwnerADevInBigProject = false;
        this.currentProject.chancesToBugs += 0.05;
        this.daysToFinishCurrentProject -= 1;

        // If it's "more" than the end, then programmers did testing
        if (this.daysToFinishCurrentProject < 0) {
            this.daysToFinishCurrentProject = 0;
            var programmersQuantity = this.hiredWorkers.stream().map(w -> w.specialization.equals("Programmer")).count();
            this.currentProject.chancesToBugs -= 0.1 * programmersQuantity;
        }
    }

    public void testDay() {
        this.wasOwnerADevInBigProject = false;
        this.currentProject.chancesToBugs -= 0.2;
    }

    public void loseContact() {
        this.availableProjects.remove(this.availableProjects.stream().filter(p -> p.clientDetails == this.clientDetails));
    }

    public void showPlayerDetails() {
        System.out.print("Your details are: ");
        System.out.print(" Moneys: " + this.moneys + " PLN");
        if (this.currentProject != null) {
            System.out.print(" Current project: " + this.currentProject.projectName + ", that will be ready for " + this.daysToFinishCurrentProject + 1 + " days.");
        }
        if (this.currentProjectByStudent != null) {
            System.out.print(" Current project make by student: " + this.currentProjectByStudent.projectName + ", that will be ready for " + this.daysToFinishCurrentProject + 1 + "days");
        }
        System.out.print(" Taxes days made: " + this.taxesDaysMade);
        System.out.print(" Your daily costs are: " + this.dailyCosts);
    }

    public void showCurrentProject() {
        var template = "%-30s %-20s %-20s %-20s %-15s %-16s %-32s %-32s %-80s";
        System.out.printf(template, "Name ", "Days left", "Chances to bugs", "Project complexity", "Project price", "Project penalty", "Project deadline", "Paycheck Deadline", "Needed technologies");
        System.out.println("");
        System.out.printf(template, this.currentProject.projectName, this.daysToFinishCurrentProject + 1, this.currentProject.chancesToBugs, this.currentProject.complexity, this.currentProject.price, this.currentProject.penalty, this.currentProject.deadline.getTime(), this.currentProject.paycheckDeadline.getTime(), this.currentProject.neededTechnologies);
    }
}
