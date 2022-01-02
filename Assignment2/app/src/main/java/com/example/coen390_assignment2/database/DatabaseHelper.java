package com.example.coen390_assignment2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.coen390_assignment2.Assignment;
import com.example.coen390_assignment2.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * database helper that controls the database using sqlite
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Tag for debugging
    private static final String TAG = "DatabaseHelper";

    //Database version used
    private static final int DATABASE_VERSION = 1;

    //context from where database is used
    private Context context = null;

    /**
     * public constructor
     * saves context
     * @param context of database
     */
    public DatabaseHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a course table
        String CREATE_COURSE_TABLE = "CREATE TABLE "+ Config.TABLE_COURSE + " ("
                + Config.COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_COURSE_TITLE + " TEXT NOT NULL, "
                + Config.COLUMN_COURSE_CODE + " TEXT NOT NULL" + ")";

        db.execSQL(CREATE_COURSE_TABLE);

        //Create a assignment table
        String CREATE_ASSIGNMENT_TABLE = "CREATE TABLE "+ Config.TABLE_ASSIGNMENT + " ("
                + Config.COLUMN_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_ASSIGNMENT_COURSEID + " TEXT NOT NULL, "
                + Config.COLUMN_ASSIGNMENT_TITLE + " TEXT NOT NULL, "
                + Config.COLUMN_ASSIGNMENT_GRADE + " TEXT NOT NULL" + ")";

        db.execSQL(CREATE_ASSIGNMENT_TABLE);

    }

    public Cursor raw() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + Config.TABLE_ASSIGNMENT , new String[]{});

        return res;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older tables
        db.execSQL("DROP TABLE IF EXISTS" + Config.TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS" + Config.TABLE_ASSIGNMENT);
        onCreate(db);

    }

    /**
     * method to add a course information to the database
     * @param course object to be saved in the database
     * @return  course id
     */
    public long insertCourse(Course course) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_COURSE_TITLE, course.getTitle());
        contentValues.put(Config.COLUMN_COURSE_CODE, course.getCode());

        try {
            id = db.insertOrThrow(Config.TABLE_COURSE, null, contentValues);
            Log.d(TAG, "Course Added");
        } catch (SQLiteException e) {
            Log.d(TAG, "Operation Failed at insertCourse: " + e.getMessage());
        } finally {
            db.close();
        }

        return id;
    }

    /**
     * method to add assignment information to the database
     * @param assignment object to be saved in the database
     * @return  assignment id
     */
    public long insertAssignment(Assignment assignment) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_ASSIGNMENT_COURSEID, assignment.getCourseId());
        contentValues.put(Config.COLUMN_ASSIGNMENT_TITLE, assignment.getTitle());
        contentValues.put(Config.COLUMN_ASSIGNMENT_GRADE, assignment.getGrade());

        try {
            id = db.insertOrThrow(Config.TABLE_ASSIGNMENT, null, contentValues);
            Log.d(TAG, "Assignment Added");
        } catch (SQLiteException e) {
            Log.d(TAG, "Operation Failed at insertAssignment: " + e.getMessage());
        } finally {
            db.close();
        }

        return id;
    }

    /**
     * method to get all the assignments of a course
     * @param selectedCourseID  id of course wanted
     * @return  list of assignments tied to the course id given
     */
    public List<Assignment> getAllAssignments(int selectedCourseID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            //get all assignments with mathcing courseId
            String selectQuery = "SELECT  * FROM " + Config.TABLE_ASSIGNMENT + " WHERE " + Config.COLUMN_ASSIGNMENT_COURSEID + " = " + selectedCourseID;
            cursor = db.rawQuery(selectQuery, null);

            if(cursor != null) {
                if(cursor.moveToFirst()) {
                    List<Assignment> assignmentList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ASSIGNMENT_ID));
                        long courseID = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_ASSIGNMENT_COURSEID));
                        String title = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ASSIGNMENT_TITLE));
                        float grade = cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_ASSIGNMENT_GRADE));

                        //create new assignments and add them to the list
                        assignmentList.add(new Assignment(id, courseID, title, grade));
                    } while (cursor.moveToNext());
                    //return all assignments of course
                    return assignmentList;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Operation Failed at getAllAssignments: " + e.getMessage());
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }
        return Collections.emptyList();
    }

    /**
     * method to get all the courses from the database
     * @return list of all courses
     */
    public List<Course> getAllCourses(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(Config.TABLE_COURSE, null, null, null, null, null, null, null);

            if(cursor != null) {
                if(cursor.moveToFirst()) {
                    List<Course> courseList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_COURSE_ID));
                        String title = cursor.getString(cursor.getColumnIndex(Config.COLUMN_COURSE_TITLE));
                        String code = cursor.getString(cursor.getColumnIndex(Config.COLUMN_COURSE_CODE));

                        //Create coruse objects and add them to the list
                        courseList.add(new Course(id, title, code));
                    } while (cursor.moveToNext());
                    //return the list of courses
                    return courseList;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Operation Failed at getAllCourses: " + e.getMessage());
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }
        return Collections.emptyList();
    }

    /**
     * method to deleted a specific course from the database
     * also delete all associated assignments
     * @param courseID of course wanted to be deleted
     */
    public void deleteCourse(int courseID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            //delete course with matching Id
            String selectQuery = "DELETE FROM " + Config.TABLE_COURSE + " WHERE " + Config.COLUMN_COURSE_ID + " = " + courseID;
            cursor = db.rawQuery(selectQuery, null);

            if(cursor != null) {
                cursor.moveToFirst();
            }

            //delete all assignments associated to the course
            selectQuery = "DELETE FROM " + Config.TABLE_ASSIGNMENT + " WHERE " + Config.COLUMN_ASSIGNMENT_COURSEID + " = " + courseID;
            cursor = db.rawQuery(selectQuery, null);

            if(cursor != null) {
                cursor.moveToFirst();
            }
        } catch (Exception e) {
            Log.d(TAG, "Operation Failed at deleteCourse: " + e.getMessage());
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }
    }

    /**
     * get a course with given id
     * @param id course id of course wanted
     * @return  course object with amthcing id
     */
    public Course getCourse(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            //get course from database
            String selectQuery = "SELECT  * FROM " + Config.TABLE_COURSE + " WHERE " + Config.COLUMN_COURSE_ID + " = " + id;
            cursor = db.rawQuery(selectQuery, null);

            if(cursor != null) {
                cursor.moveToFirst();

                int ID = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_COURSE_ID));
                String title = cursor.getString(cursor.getColumnIndex(Config.COLUMN_COURSE_TITLE));
                String code = cursor.getString(cursor.getColumnIndex(Config.COLUMN_COURSE_CODE));

                //create new course object to return
                return new Course(id, title, code);
            }
        } catch (Exception e) {
            Log.d(TAG, "Operation Failed at getCourse: " + e.getMessage());
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return null;
    }

    /**
     * get total average of all assignments from all courses
     * @return total average
     */
    public float getAssignmentAverage()
    {
        float average = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            //get total average
            String selectQuery = "SELECT AVG(" + Config.COLUMN_ASSIGNMENT_GRADE + ") FROM " + Config.TABLE_ASSIGNMENT;
            cursor = db.rawQuery(selectQuery, null);

            if(cursor != null) {
                if(cursor.moveToFirst()) {
                    average = cursor.getFloat(0);
                    return average;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Operation Failed at getAssignmentAverage: " + e.getMessage());
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return -1;
    }

    /**
     * get average of the wanted course
     * @param courseId of course the average is wanted
     * @return average of course
     */
    public float getCourseAverage(int courseId) {
        float average = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            //get average of assignments with wanted id
            String selectQuery = "SELECT AVG(" + Config.COLUMN_ASSIGNMENT_GRADE + ") FROM " + Config.TABLE_ASSIGNMENT
                    + " WHERE " + Config.COLUMN_ASSIGNMENT_COURSEID + " = " + courseId;
            cursor = db.rawQuery(selectQuery, null);

            if(cursor != null) {
                if(cursor.moveToFirst()) {
                        Log.d(TAG, "Average of the course " + courseId + " is " + cursor.toString());

                        average = cursor.getFloat(0);
                        return average;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Operation Failed at CourseAverage: " + e.getMessage());
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return -1;
    }
}
