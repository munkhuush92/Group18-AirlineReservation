package uw.group18_airlinereservation;

import java.io.Serializable;

/**
 * Created by Austin Ingraham on 12/4/2016.
 */
public class Passenger {
    private static final Passenger instance = new Passenger();

    public String myPassID = "NOT SET";
    public String myEmail = "NOT SET";
    public String myFirstName = "NOT SET";
    public String myLastName = "NOT SET";

    /**
     * private constructor of passenger
     */
    private Passenger(){
    }

    public static void setValues(String thePassID, String theEmail, String theFirstName, String theLastName) {
        instance.myPassID = thePassID;
        instance.myEmail = theEmail;
        instance.myFirstName = theFirstName;
        instance.myLastName = theLastName;
    }

    public static Passenger getPassengerObject() {
        return instance;
    }

    /**
     * get username
     * @return String PassID
     */
    String getPassID () { return myPassID; }

    /**
     * get email
     * @return String Email
     */
    String getEmail(){ return myEmail;  }

    /**
     * get first name
     * @return String FirstName
     */
    String getFirstName(){ return myFirstName;  }

    /**
     * get email
     * @return String LastName
     */
    String getLastName(){ return myLastName;  }
}
