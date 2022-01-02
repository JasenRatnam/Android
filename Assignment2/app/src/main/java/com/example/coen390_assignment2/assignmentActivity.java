package com.example.coen390_assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coen390_assignment2.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * assignment activtiy of the program
 * displays assignments and grades of the selected course
 * capable of adding a new assignment
 */
public class assignmentActivity extends AppCompatActivity {

    //Tag for debugging
    private static final String TAG = "AssignmentActivity";

    //items on the activity
    private FloatingActionButton addAssignmentButton;
    private ListView assignmentListView;
    private TextView courseNameTextView;
    private TextView courseCodeTextView;
    private Button removeCourseButton;

    //course info chosen by user
    private Course course;
    private int courseID;

    //Assignments of the course
    private List<Assignment> assignments = null;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        dbHelper = new DatabaseHelper(this);
        //Setup activity page
        setupUI();
    }

    /**
     * Setup the activity page
     */
    protected void setupUI()
    {
        //initialize
        addAssignmentButton = findViewById(R.id.addAssignmentFloatingButton);
        assignmentListView = findViewById(R.id.assignmentListView);
        courseNameTextView = findViewById(R.id.courseNameTextView);
        courseCodeTextView = findViewById(R.id.courseCodeTextView);
        removeCourseButton = findViewById(R.id.deleteCourseButton);

        //Set information on the activity
        setCourse();
        setHeaderTitle();
        loadListView();

        //on click listeners
        addAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAssignment();
            }
        });

        removeCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse();
            }
        });
    }

    /**
     * method to delete the selected course from the databases
     * delete all associated assignments
     * Go back to main activity after deleting
     */
    protected void deleteCourse()
    {
        dbHelper.deleteCourse(courseID);
        goToMainActivity();
        Log.d(TAG, "Course: " + courseID + " Deleted" );
    }

    /**
     * get the chosen course from the database
     * using the course id gotten from main activity
     * find course with matching id
     */
    protected void setCourse(){
        setCourseID();
        course = dbHelper.getCourse(courseID);
    }

    /**
     * Get course id of selected
     * From intent sent from Main Activity
     */
    protected void setCourseID() {
        Intent intent = getIntent();
        courseID = intent.getIntExtra(getString(R.string.course_ID),-1);
        Log.d(TAG, "Course selected:" + courseID);
    }

    /**
     * set the header of the activity
     * display course name and code
     */
    protected void setHeaderTitle() {
        String courseTitle = course.getTitle();
        String courseCode = course.getCode();

        courseNameTextView.setText(courseTitle);
        courseCodeTextView.setText(courseCode);
        Log.d(TAG, "Header Set");
    }

    /**
     * Create a fragment dialogue to get assignment information
     */
    protected void addAssignment()
    {
        FragmentManager fm = getSupportFragmentManager();
        AddDialog courseDialog = AddDialog.newInstance(getString(R.string.assignmentDialog));
        courseDialog.show(fm, "NewAssignmentFragment");
        Log.d(TAG, "Assignment dialogue opened");
    }

    /**
     * Method to display the assignments on the list view
     * Create an adapter and set it on the list view
     */
    protected void loadListView() {
        DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
        assignments = dbHelper.getAllAssignments(courseID);

        ArrayList<String> assignmentPrint = listPrint(assignments);

        ArrayAdapter arrayadapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,assignmentPrint);

        assignmentListView.setAdapter(arrayadapter);
        Log.d(TAG, "ListView has been updated");
    }

    /**
     * Method to format the assignments in the wanted way to be displayed
     * @param assignments list of all courses to be displayed
     * @return a arraylist of information formatted
     */
    protected ArrayList<String> listPrint(List<Assignment> assignments)
    {
        ArrayList<String> assignmentPrint = new ArrayList<>();  //Arraylist to be returned
        String tmp = null;

        //Go trough every course and format the output
        for(int j=0;j<assignments.size();j++){
            tmp = assignments.get(j).printAssignment();
            assignmentPrint.add(tmp);
        }

        return assignmentPrint;
    }

    /**
     * method to go to the Main activity page
     */
    protected void goToMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Getters
    protected int getCourseID() { return courseID; }
    protected Course getCourse() { return course; }
}