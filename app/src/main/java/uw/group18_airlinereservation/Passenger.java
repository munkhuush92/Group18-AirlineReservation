package uw.group18_airlinereservation;

/**
 * Created by Austin Ingraham on 12/4/2016.
 */
public class Passenger {

    public String myPassID;
    public String myEmail;

    public static final String PASSID ="PassID", EMAIL ="Email";

    /**
     * constructor of user
     * @param thePassID
     * @param theEmail
     */
    public Passenger(String thePassID, String theEmail){
        this.myPassID = thePassID;
        this.myEmail = theEmail;
    }

    /**
     * get username
     * @return String username
     */
    String getPassID () {
        return myPassID;
    }

    /**
     * get email
     * @return String email
     */
    String getEmail(){ return myEmail;  }
}
