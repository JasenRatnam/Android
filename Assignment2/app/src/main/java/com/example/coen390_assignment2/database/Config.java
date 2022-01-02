package com.example.coen390_assignment2.database;

/**
 * Configuration class
 * holds names to configure the database
 */
public class Config {
    //Database
    public static final String DATABASE_NAME = "database";

    //Course Table
    public static final String TABLE_COURSE = "course";
    public static final String COLUMN_COURSE_ID = "_id";
    public static final String COLUMN_COURSE_TITLE = "title";
    public static final String COLUMN_COURSE_CODE = "code";

    //Assignment Table
    public static final String TABLE_ASSIGNMENT = "assignment";
    public static final String COLUMN_ASSIGNMENT_ID = "_id";
    public static final String COLUMN_ASSIGNMENT_COURSEID = "courseID";
    public static final String COLUMN_ASSIGNMENT_TITLE = "title";
    public static final String COLUMN_ASSIGNMENT_GRADE = "grade";
}
