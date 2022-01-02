package com.example.coen390_assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.coen390_assignment2.database.DatabaseHelper;

/**
 * Dialog Fragment for adding a course or assignment
 * create dialogs for both course and assingment adding
 * Save the result directly to the databse
 */
public class AddDialog extends DialogFragment {

    //Tag for debugging
    private static final String TAG = "AddDialog";

    //database controller
    private DatabaseHelper dbHelper;

    //items on activity
    private EditText nameEditText;
    private EditText codeGradeEditText;

    //mode of dialogue wanted
    private String mode = null;

    private AlertDialog.Builder dialogBuilder;

    public AddDialog() {
        // Empty constructor required for DialogFragment
    }

    /**
     * Called from other activities with the mode of dialogue wanted
     * save mode in args
     * @param mode wanted: assignment or course adding
     * @return
     */
    public static AddDialog newInstance(String mode) {
        AddDialog frag = new AddDialog();
        Bundle args = new Bundle();
        args.putString("Dialog_Mode", mode);
        frag.setArguments(args);
        Log.d(TAG, "Dialogue Mode used: " +  mode);
        return frag;
    }

    /**
     * create a dialog
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dbHelper = new DatabaseHelper(getContext());
        setupUI();

        return dialogBuilder.create();
    }

    /**
     * Setup the activity page
     * Initialize the buttons on the page
     * on click listeners to go to another activity when a list view activity is pressed
     * on click listerner to add a new course
     */
    protected void setupUI()
    {
        //Get mode wanted
        mode = getArguments().getString("Dialog_Mode");

        dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        //load custom layout of dialogue with 2 editTexts
        View customLayout = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(customLayout);


        //initalize
        nameEditText = customLayout.findViewById(R.id.nameEditText);
        codeGradeEditText = customLayout.findViewById(R.id.IDGradeEditText);

        //set titles
        if(mode.equals(getString(R.string.courseDialog))) {
            dialogBuilder.setTitle("Add a new course:");
            nameEditText.setHint("Course Name");
            codeGradeEditText.setHint("Course Code");
        }
        else if(mode.equals(getString(R.string.assignmentDialog))){
            dialogBuilder.setTitle("Add a new assignment:");
            nameEditText.setHint("Assignment Name");
            codeGradeEditText.setHint("Assignment Grade");
            codeGradeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }


        dialogBuilder.setPositiveButton("Save",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
                Log.d(TAG, "Dialogue Saving pressed");
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    String cancelled = "Cancelled";
                    makeToast(cancelled);
                    dialog.dismiss();
                    Log.d(TAG, "Dialogue Cancelled");
                }
            }

        });

    }

    /**
     * method to save the information from the dialogue
     */
    private void save() {
        if(nameEditText.getText().toString().isEmpty() || codeGradeEditText.getText().toString().isEmpty())
        {
            //if empty cell
            String error = "Please fill up all cells before saving";
            makeToast(error);
            Log.d(TAG, "Attempted to save without filling up cells");
        }
        else if(mode.equals(getString(R.string.courseDialog))){
            //if course
            addCourse();
        }
        else if(mode.equals(getString(R.string.assignmentDialog))){
            //if assignment
            if(Integer.parseInt(codeGradeEditText.getText().toString()) < 0 ||Integer.parseInt(codeGradeEditText.getText().toString()) > 100)
            {
                String error = "Please enter a grade between 0 and 100";
                makeToast(error);
                Log.d(TAG, "Attempted to enter a grade out of rang: 0-100");
            }
            else{
                addAssignment();
            }
        }
    }

    /**
     * method to add course
     * get course information from dialogue and create a course object
     * add course into the database
     * refresh the lsitview
     */
    private void addCourse() {
        //get new course info
        String courseName = nameEditText.getText().toString();
        String courseCode = codeGradeEditText.getText().toString();

        //create new course
        Course newCourse = new Course(-1, courseName, courseCode);
        //insert course in database
        dbHelper.insertCourse(newCourse);
        //refresh listview
        ((MainActivity)getActivity()).loadListView();

        makeToast(courseCode + " added");
        Log.d(TAG, "Course: " + courseCode + " Added");
    }

    /**
     * method to add assignemnt
     * get assignemnt information from dialogue and create a assignemnt object
     * add assignemnt into the database
     * refresh the lsitview
     */
    private void addAssignment() {
        //get new assignment info
        String assignmentName = nameEditText.getText().toString();
        float assignmentGrade = Integer.parseInt(codeGradeEditText.getText().toString());
        int courseId = ((assignmentActivity)getActivity()).getCourseID();

        //create new assignment
        Assignment newAssignment = new Assignment(-1, courseId, assignmentName, assignmentGrade);

        //add assignment to database
        dbHelper.insertAssignment(newAssignment);
        //refresh listview
        ((assignmentActivity)getActivity()).loadListView();

        makeToast(assignmentName + " added");
        Log.d(TAG, "Assignment: " + assignmentGrade + " Added to Course " + courseId);
    }

    /**
     * Method to create toast messages
     * @param data to write in toast
     */
    private void makeToast(String data) {
        Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
    }
}
