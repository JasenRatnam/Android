package com.example.coen390assignment1;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class to create course objects
 * Base code given by Tawfiq
 * Modifed by Jasen Ratnam for it's use in the assignment
 */
public class Course {
    private static int courseID = 1;    //Static ID increments with every new course created
    private String courseTitle;         //The Title of the course
    private ArrayList<Assignment> assignments; //Arraylist to store assignments tied with course


    /**
     * Constructor of class,
     * Creates a course object with its title and list of assignments
     * Increments courseID everytime a new course is created
     * @param title : title of course
     * @param assns : assignments of course
     */
    private Course(String title, ArrayList<Assignment> assns)
    {
        courseTitle = title;
        assignments = assns;
        courseID++;
    }

    /**
     * Create a random number of assignments (0-5)
     * 4 Maximum
     * Save them in a arraylist
     * Create a course object with course title and generates assignments
     * @return a Course instant with random assignment values
     */
    static public Course generateRandomCourse()
    {
        Random rnd = new Random();
        int assignmentNo = rnd.nextInt(5);
        ArrayList<Assignment> tempAssns = new ArrayList<>();

        for(int i=0; i<assignmentNo; i++)
        {
            tempAssns.add(Assignment.generateRandomAssignment());
        }
        return new Course("Course " + courseID, tempAssns);
    }

    /**
     * Method that provides a print for the course
     * with its assignments along with grades in wanted format
     * @param letterGrades boolean to know if the grade should be letter or number
     * @return string print of course
     */
    public String printCourse(boolean letterGrades)
    {
        String print = null;
        String averageText = null;

        print = "Course title: " + getCourseTitle() + "\n";
        print += "\nAssignments:\n";

        //Print assignments with its grades
        for(int i = 0; i<assignments.size(); i++){
            print += assignments.get(i).getAssignmentTitle()  + "     " ;

            //Get grade of assignment,
            //Letter or number depending on user choice
            if(letterGrades)
            {
                String letterGrade = convertGrade(assignments.get(i).getAssignmentGrade());
                print += letterGrade + "\n";
            }
            else
            {
                print += assignments.get(i).getAssignmentGrade() + "%\n";
            }
        }

        //average of the course
        averageText = averagePrint(letterGrades);

        print += "\nAverage:     " + averageText + "\n";

        return print;
    }

    /**
     * Method to convert a number grade to a letter grade
     * @param grade number grade
     * @return letter grade
     */
    protected String convertGrade(float grade)
    {
        //Uses Concordia's Grading system
        //https://www.concordia.ca/artsci/math-stats/programs/grading.html

        if (grade >= 90 && grade <= 100)
            return "A+";
        else if (grade >= 85 && grade <= 89)
            return "A";
        else if (grade >= 80 && grade <= 84)
            return "A-";
        else if (grade >= 77 && grade <= 79)
            return "B+";
        else if (grade >= 73 && grade <= 76)
            return "B";
        else if (grade >= 70 && grade <= 72)
            return "B-";
        else if (grade >= 67 && grade <= 69)
            return "C+";
        else if (grade >= 63 && grade <= 66)
            return "C";
        else if (grade >= 60 && grade <= 62)
            return "C-";
        else if (grade >= 57 && grade <= 59)
            return "D+";
        else if (grade >= 53 && grade <= 56)
            return "D";
        else if (grade >= 50 && grade <= 52)
            return "D-";
        else
            return "F";
    }

    /**
     * Get text for the average of the course
     * If the course has no assignments, average is "Not Available"
     * Give letter or number grade depending on user choice
     * @return the text of the average of the given course
     */
    protected String averagePrint(boolean letterGrades)
    {
        float  average = 0;
        String averageText = null;

        average = getAverage();

        if(Float.isNaN(average))
        {
            averageText = "Not Available";
        }
        else if(letterGrades)
        {
            String letterGrade = convertGrade(average);
            averageText = letterGrade + "\n";
        }
        else
        {
            averageText = average + "%\n";
        }

        return averageText;
    }


    //****Get Methods****//

    /**
     * calculate and return the total average of the course
     * @return the average grade of the assignments
     */
    public float getAverage()
    {
        float average = 0;
        for(int i=0; i<assignments.size(); i++)
        {
            average += assignments.get(i).getAssignmentGrade();
        }
        average = average/assignments.size();

        return average;
    }
    public String getCourseTitle() {return courseTitle; }
    public ArrayList<Assignment> getAssignments() { return assignments; }

    //setter method
    static public void setCourseID(int newCourseID)
    {
        courseID = newCourseID;
    }
}
