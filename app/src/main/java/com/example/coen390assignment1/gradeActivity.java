package com.example.coen390assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class for the grade activity page
 * Generates the courses and displays them with its assignments
 * Including its grades and average
 */
public class gradeActivity extends AppCompatActivity {

    private ListView gradesListView;                            //Listview object on activity
    private ArrayList<Course> courses = new ArrayList<>();      //Arraylist of courses
    private boolean letterGrades = false;                       //Letter grades or number grades
    private final int maxCourse = 5;                            //Maximum number of course

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        //Setup the activity
        setupUI();
    }

    /**
     * Make action button on action bar visible
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.grade_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle the action button on the action bar
     * Change the number grades to letter
     * vice-verse
     * Change the boolean letterGrades to correct option
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_gradeButton) {
            if(!letterGrades)
            {
                letterGrades = true;
            }
            else
            {
                letterGrades = false;
            }
             displayCourses();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Generate a random number of courses (1-5)
     * Maximum of 5 courses
     * @return an arraylist of courses generated
     */
    protected ArrayList<Course> generateCourses(){
        //Arraylist to save and return courses
        ArrayList<Course> courses = new ArrayList<>();

        Random rnd = new Random();
        int courseNo = rnd.nextInt(maxCourse)+1;

        //Create and save random number of courses
        //Reset assignment ID to 1 before generating new course
        for(int j=0;j<courseNo;j++){
            courses.add(Course.generateRandomCourse());
            Assignment.setAssID(1);
        }
        //Reset course ID to 1 for next set
        Course.setCourseID(1);
        return courses;
    }

    /**
     * Setup the activity page
     * Initilize the list view on the activity
     * Generate course
     * and Display the courses and tied assignments with grades
     */
    protected void setupUI()
    {
        gradesListView = findViewById(R.id.gradesListView);
        courses = generateCourses();
        displayCourses();
    }

    /**
     * Method to display the courses on the list view
     * Create an adapter and set it on the list view
     */
    protected void displayCourses()
    {
        //create arraylist of information of each course
        //formated in the way we want it to be displayed
        ArrayList<String> coursePrint = listPrint(courses);

        ArrayAdapter arrayadapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,coursePrint);

        gradesListView.setAdapter(arrayadapter);
    }

    /**
     * Method to format the courses and assignments in the wanted way to be displayed
     * @param courses list of all courses to be displayed
     * @return a arraylist of information formatted
     */
    protected ArrayList<String> listPrint(ArrayList<Course> courses)
    {
        ArrayList<String> coursePrint = new ArrayList<>();  //Arraylist to be returned
        String tmp = null;
        String averageText = null;

        //Go trough every course and format the output
        for(int j=0;j<courses.size();j++){
            tmp = courses.get(j).printCourse(letterGrades);
            coursePrint.add(tmp);
        }

        return coursePrint;
    }
}