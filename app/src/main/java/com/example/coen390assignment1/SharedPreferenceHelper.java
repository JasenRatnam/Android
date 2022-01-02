package com.example.coen390assignment1;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Controller for the SharedPreference.
 * Base code given by Tawfiq
 * Modifed by Jasen Ratnam for it's use in the assignment
 */
public class SharedPreferenceHelper {

    private SharedPreferences sharedPreferences;    //SharedPreferences of the project
    private Context context;                        //context called from
    
    /**
     * Constructor of class
     * creates a SharedPreferenceHelper object
     */
    public SharedPreferenceHelper(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.profilePreferencesKey), Context.MODE_PRIVATE);
    }

    /**
     * Save the profile of the user
     * @param profile to be saved
     */
    public void saveProfile(Profile profile)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.profileName),profile.getName());
        editor.putInt(context.getString(R.string.profileAge),profile.getAge());
        editor.putInt(context.getString(R.string.profileID),profile.getID());
        editor.apply();
    }

    /**
     * check if the user profile is empty
     * @return
     */
    public boolean emptyProfile()
    {
        if(getProfileName() == null || getProfileAge() == -1 || getProfileID() == -1)
            return true;
        else
            return false;
    }

    // Getter Methods
    public String getProfileName()
    {
        return sharedPreferences.getString(context.getString(R.string.profileName), null);
    }
    public int getProfileAge()
    {
        return sharedPreferences.getInt(context.getString(R.string.profileAge), -1);
    }
    public int getProfileID()
    {
        return sharedPreferences.getInt(context.getString(R.string.profileID), -1);
    }
}
