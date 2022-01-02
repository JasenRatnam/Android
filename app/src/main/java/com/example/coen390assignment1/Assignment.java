package com.example.coen390assignment1;

import java.util.Random;

/**
 * Class to create assignment objects
 * Base code given by Tawfiq
 * Modifed by Jasen Ratnam for it's use in the assignment
 */
public class Assignment {

    private static int assID = 1;       //static ID increments with every new assignment created
    private String assignmentTitle;     //title of assignment
    private int assignmentGrade;        //grade of assignmnent

    /**
     * Private constructor of class,
     * Creates a assignment object with its title and grade
     * Increments assID everytime a new assignment is created
     * @param title of assignment
     * @param grade of the assignment
     */
    private Assignment(String title, int grade)
    {
        assignmentTitle = title;
        assignmentGrade = grade;
        assID++;
    }

    /**
     * Create a assignment with a random grade (0-100)
     * @return an Assignment instance with random values
     */
    static public Assignment generateRandomAssignment()
    {
        Random rnd = new Random();
        String tempTitle = "Assignment " + assID;
        int tempGrade = rnd.nextInt(100)+1;

        return new Assignment(tempTitle, tempGrade);
    }

    //****Get Methods****//
    public String getAssignmentTitle()
    {
        return assignmentTitle;
    }
    public int getAssignmentGrade() {return assignmentGrade; }

    //****Set Methods****//
    static public void setAssID(int newAssID) {assID = newAssID;}
}
