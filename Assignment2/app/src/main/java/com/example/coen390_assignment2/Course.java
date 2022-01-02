package com.example.coen390_assignment2;

/**
 * course object of the program
 * object class to represent an course
 */
public class Course {
    //parameters of course
    private int id;
    private String title;
    private String code;

    //public constructor of course
    public Course(int id, String title, String code) {
        this.id = id;
        this.title = title;
        this.code = code;
    }

    /**
     * Method that provides a print for the course
     * @return string print of course
     */
    public String printCourse()
    {
        String print = null;

        print = getTitle() + "\n";
        print += getCode() + "\n";

        return print;
    }


    //Getters
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getCode() {
        return code;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
