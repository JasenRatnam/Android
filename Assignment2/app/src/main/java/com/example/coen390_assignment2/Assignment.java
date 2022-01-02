package com.example.coen390_assignment2;

/**
 * assignment object of the program
 * object class to represent an assignment
 */
public class Assignment {

    //object parameters
    private long id;
    private long courseId;
    private String title;
    private float grade;

    //public constructor
    public Assignment(long id, long courseId, String title, float grade) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.grade = grade;
    }

    /**
     * Method that provides a print for the assignment
     * @return string print of assignment
     */
    public String printAssignment()
    {
        String print = null;

        print = getTitle() + "\n";
        print += getGrade() + " %\n";

        return print;
    }

    //getters
    public long getId() {
        return id;
    }
    public long getCourseId() {
        return courseId;
    }
    public String getTitle() {
        return title;
    }
    public float getGrade() {
        return grade;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setGrade(float grade) {
        this.grade = grade;
    }
}
