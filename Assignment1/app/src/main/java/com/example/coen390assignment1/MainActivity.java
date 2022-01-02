package com.example.coen390assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Class for the main activity page
 * Have buttons to go to the grades and profile activties
 */
public class MainActivity extends AppCompatActivity {

    private Button profileButton;       //Button object for the profile activity
    private Button showGradesButton;    //Button object for the grades activity
    private Profile profile;            //Profile of user
    protected SharedPreferenceHelper sharedPreferenceHelper;    //SharedPreferencesHelper object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize shared preference controller
        sharedPreferenceHelper = new SharedPreferenceHelper(MainActivity.this);

        //Setup activity page
        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Get user name
        String name = sharedPreferenceHelper.getProfileName();

        //for debug
        //name = "jasen";

        //If no user profile name is available go to the activity page to create a profile
        //else set the user name on the profile button
        if(name == null)
        {
            goToProfileActivity();
        }
        else
        {
            profileButton.setText(name);
        }
    }

    /**
     * Setup the activity page
     * Initialize the buttons on the page
     * on click listeners to go to another activity when a button is pressed
     */
    protected void setupUI()
    {
        profileButton = findViewById(R.id.profileButton);
        showGradesButton = findViewById(R.id.showGradesButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfileActivity();
            }
        });

        showGradesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGradesActivity();
            }
        });
    }

    /**
     * method to go to the profile activity page
     */
    protected void goToProfileActivity()
    {
        Intent intent = new Intent(this, profileActivity.class);
        startActivity(intent);
    }

    /**
     * method to go to the grades activity page
     */
    protected void goToGradesActivity()
    {
        Intent intent = new Intent(this, gradeActivity.class);
        startActivity(intent);
    }
}