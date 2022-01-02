package com.example.coen390_assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.DecimalFormat;
import android.icu.text.Transliterator;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coen390_assignment2.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * main activity of programm
 * display courses and total average
 * capable of adding a new course
 */
public class MainActivity extends AppCompatActivity {

    //Tag for debugging
    private static final String TAG = "MainActivity";

    //used to format output to two decimal point
    private static final DecimalFormat df = new DecimalFormat("0.00");

    //database controller
    private DatabaseHelper dbHelper;

    private static Context context;

    //items on activity
    private FloatingActionButton addCourseButton;
    private ListView courseListView;
    private TextView assignmentAverageTextView;
    private Button restore;

    //all courses saved
    private List<Course> courses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        //Setup activity page
        setupUI();
        MainActivity.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    /**
     * Setup the activity page
     * Initialize the buttons on the page
     * on click listeners to go to another activity when a list view activity is pressed
     * on click listerner to add a new course
     */
    protected void setupUI()
    {
        //initalize
        addCourseButton = findViewById(R.id.addCourseFloatingButton);
        courseListView = findViewById(R.id.courseListView);
        assignmentAverageTextView = findViewById(R.id.assignmentAverageTextView);
        restore = findViewById(R.id.btn_export);

        //set total average of assignment and update the display all courses
        setTotalAverage();
        loadListView();

        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                    new ExportDatabaseCSVTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else {

                    new ExportDatabaseCSVTask().execute();
                }
            }
        });

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addCourse();
            }
        });

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToAssignmentActivity(position);
            }
        });
    }

    /**
     * method to set the total average of all assignments on top of the activity
     * get average from database
     * formated to have 2 decimal points
     * Display NA if no assignments
     */
    protected void setTotalAverage()
    {
        String averageText = "Average of All Assignments: ";
        float average = dbHelper.getAssignmentAverage();
        String averageString = df.format(average);
        if(average <= 0)
        {
            assignmentAverageTextView.setText(averageText + "NA");
        }
        else {
            assignmentAverageTextView.setText(averageText + averageString + "%");
        }

        Log.d(TAG, "Total Average Set");
    }


    /**
     * Create a fragment dialogue to get course information
     */
    protected void addCourse()
    {
        FragmentManager fm = getSupportFragmentManager();
        AddDialog courseDialog = AddDialog.newInstance(getString(R.string.courseDialog));
        courseDialog.show(fm, "NewCourseFragment");
        Log.d(TAG, "Course Dialog Opened");
    }

    /**
     * method to go to the assignment activity page
     * sends the course id of the selected course with it
     * @param position on the list view
     */
    protected void goToAssignmentActivity(int position)
    {
        int id = courses.get(position).getId();

        Log.d(TAG, "Position of Chosen Course on ListView: " + String.valueOf(position));
        Log.d(TAG, "ID of Chosen Course on ListView: " + String.valueOf(id));

        Intent intent = new Intent(this, assignmentActivity.class);
        intent.putExtra(getString(R.string.course_ID),id);  //send course id
        startActivity(intent);
    }

    /**
     * Method to display the courses on the list view
     * Create an adapter and set it on the list view
     */
    protected void loadListView() {
        courses = dbHelper.getAllCourses();

        ArrayList<String> coursePrint = listPrint(courses);

        ArrayAdapter<String> arrayadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,coursePrint);

        courseListView.setAdapter(arrayadapter);
        Log.d(TAG, "ListView has been updated");
    }

    /**
     * Method to format the courses in the wanted way to be displayed
     * @param courses list of all courses to be displayed
     * @return a arraylist of information formatted
     */
    protected ArrayList<String> listPrint(List<Course> courses)
    {
        ArrayList<String> coursePrint = new ArrayList<>();  //Arraylist to be returned
        String tmp = null;

        //Go trough every course and format the output
        for(int j=0;j<courses.size();j++){
            tmp = courses.get(j).printCourse();
            //get average of the course
            float average = dbHelper.getCourseAverage(courses.get(j).getId());

            tmp += "\nAssignment Average: ";
            if(average <= 0)    // if no grades
            {
                tmp += "NA";
            }
            else {
                String averageString = df.format(average);  //format average
                tmp += averageString + "%";
            }
            coursePrint.add(tmp);
        }
        return coursePrint;
    }
}


class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {

    private final ProgressDialog dialog = new ProgressDialog(MainActivity.getAppContext());
    DatabaseHelper dbhelper;
    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Exporting database...");
        this.dialog.show();
        dbhelper = new DatabaseHelper(MainActivity.getAppContext());
    }

    protected Boolean doInBackground(final String... args) {

        File exportDir = new File(Environment.getExternalStorageDirectory(), "/codesss/");
        if (!exportDir.exists()) { exportDir.mkdirs(); }

        File file = new File(exportDir, "person.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            Cursor curCSV = dbhelper.raw();
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                String arrStr[]=null;
                String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                for(int i=0;i<curCSV.getColumnNames().length;i++)
                {
                    mySecondStringArray[i] =curCSV.getString(i);
                }
                csvWrite.writeNext(mySecondStringArray);
            }
            csvWrite.close();
            curCSV.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    protected void onPostExecute(final Boolean success) {
        if (this.dialog.isShowing()) { this.dialog.dismiss(); }
        if (success) {
            Toast.makeText(MainActivity.getAppContext(), "Export successful!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity.getAppContext(), "Export failed", Toast.LENGTH_SHORT).show();
        }
    }
}