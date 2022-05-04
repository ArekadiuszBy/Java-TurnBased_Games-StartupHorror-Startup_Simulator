package com.company.project;
import com.company.client.ClientDetails;
import java.util.Date;

enum projectComplexity {
    EASY,
    NORMAL,
    HARD;
}

enum departments {
    FRONTEND,
    BACKEND,
    DATABASE,
    MOBILE,
    WORDPRESS,
    PRESTASHOP;
}

public interface IProject {
    String projectName = "";
    ClientDetails clientDetails = new ClientDetails();
    Date deadline = new Date();
    Date paycheckDeadline = new Date();
    Double price = 0d;
    Double penalty = 0d;
    projectComplexity complexity = null;
    departments workHoursByDepartments = departments.FRONTEND;

    boolean checkTechnologiesQuantity();
}

;