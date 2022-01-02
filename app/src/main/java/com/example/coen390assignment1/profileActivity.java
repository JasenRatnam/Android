package com.example.coen390assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Class for the profile activity page
 * Display profile information of user
 * The Profile has the following information: Name, Age, Student ID.
 * Two modes are available on this activity Display mode and edit mode
 * SWitch modes using action from action bar
 */
public class profileActivity extends AppCompatActivity {

    private Button saveButton;          //Button object to save user profile
    private EditText nameEditText;      //Edit text box to edit user name
    private EditText ageEditText;       //Edit text box to edit age of user
    private EditText IDEditText;        //Edit text box to ID of user
    private boolean editModeEnabled = false;    //boolean to set the activity on edit or display mode
    private final int minimumAge = 18;  //Min age user can be
    private final int maximumAge = 99;  //Max age user can be
    protected SharedPreferenceHelper sharedPreferenceHelper;    //SharedPreferencesHelper object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initialize shared preference controller
        sharedPreferenceHelper = new SharedPreferenceHelper(profileActivity.this);

        //setup activity page
        setupUI();
    }

    /**
     * Make action button on action bar visible
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle the action button on the action bar
     * enable editing the profile
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profileButton) {
            //Change between display and edit modes
            if(!editModeEnabled)
            {
                editModeEnabled = true;
                editMode();
            }
            else
            {
                editModeEnabled = false;
                displayMode();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //display saved profile information on texxt boxes
        setTextBoxes();

        //if user profile is empty, go to edit mode
        //else go to display mode
        if(sharedPreferenceHelper.emptyProfile()) {

            Toast toast = Toast.makeText(getApplicationContext(), R.string.createProfileToast, Toast.LENGTH_LONG);
            toast.show();

            editModeEnabled = true;
            editMode();
        }
        else
        {
            displayMode();
        }
    }

    /**
     * Setup the activity page
     * Initialize the buttons and edit text
     */
    protected void setupUI()
    {
        saveButton = findViewById(R.id.saveButton);
        nameEditText = findViewById(R.id.editNameBox);
        ageEditText = findViewById(R.id.editAgeBox);
        IDEditText = findViewById(R.id.editIDBox);
    }

    /**
     * display mode
     * display user information
     * uneditable
     * hide save button
     */
    protected void displayMode()
    {
        saveButton.setVisibility(View.GONE);
        nameEditText.setEnabled(false);
        ageEditText.setEnabled(false);
        IDEditText.setEnabled(false);
    }

    /**
     * edit mode
     * display user information
     * editable
     * Save button displayed
     * when save button is clicked try and save information
     */
    protected void editMode()
    {
        saveButton.setVisibility(View.VISIBLE);
        nameEditText.setEnabled(true);
        ageEditText.setEnabled(true);
        IDEditText.setEnabled(true);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //try to save information
                saveEdit();
            }
        });
    }

    /**
     * display saved user information on the text boxes
     * if user profile is not empty, display info
     */
    protected void setTextBoxes()
    {
        if(!sharedPreferenceHelper.emptyProfile()) {
            String name = sharedPreferenceHelper.getProfileName();
            nameEditText.setText(name);

            int age = sharedPreferenceHelper.getProfileAge();
            ageEditText.setText(Integer.toString(age));

            int ID = sharedPreferenceHelper.getProfileID();
            IDEditText.setText(Integer.toString(ID));
        }
    }

    /**
     * try and save information entered by user
     */
    protected void saveEdit()
    {
        //if text boxes are empty
        if(nameEditText.getText().toString().isEmpty() || ageEditText.getText().toString().isEmpty()
                || IDEditText.getText().toString().isEmpty()) {

            // error message: fill in every box
            Toast toast = Toast.makeText(getApplicationContext(), R.string.fillTextToast, Toast.LENGTH_LONG);
            toast.show();
        }
        //if user is out of age range
        else if(Integer.parseInt(ageEditText.getText().toString()) < minimumAge || Integer.parseInt(ageEditText.getText().toString())>maximumAge)
        {
            //error message: user must be between 18 and 99
            Toast toast = Toast.makeText(getApplicationContext(), R.string.ageErrorToast, Toast.LENGTH_LONG);
            toast.show();
        }
        //data entered correctly; save it
        else
        {
            //get profile information entered
            int age = Integer.parseInt(ageEditText.getText().toString());
            int ID = Integer.parseInt(IDEditText.getText().toString());
            String name = nameEditText.getText().toString();

            //create and save profile
            Profile profile = new Profile(name,age,ID);
            sharedPreferenceHelper.saveProfile(profile);

            //saved message
            Toast toast = Toast.makeText(getApplicationContext(), R.string.saveToast, Toast.LENGTH_LONG);
            toast.show();

            //go to display mode
            editModeEnabled = false;
            displayMode();
        }
    }
}