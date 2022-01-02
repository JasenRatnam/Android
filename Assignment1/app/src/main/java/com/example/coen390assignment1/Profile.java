package com.example.coen390assignment1;

/**
 * Class to create a profile object
 * Contains name, age and ID of user
 */
public class Profile {

    private String name;        //Name of user
    private int age;            //Age of user
    private int ID;             //ID of user

    /**
     * Public Constructor to create a profile object
     */
    public Profile(String profileName, int profileAge, int profileID)
    {
        name = profileName;
        age = profileAge;
        ID = profileID;
    }


    //****Get Methods****//
    public String getName()
    {
        return name;
    }
    public int getAge()
    {
        return age;
    }
    public int getID()
    {
        return ID;
    }

    //****Set Methods****//
    public void setName(String newName)
    {
        name = newName;
    }
    public void setAge(int newAge)
    {
        age = newAge;
    }
    public void setID(int newID)
    {
        ID = newID;
    }
}
