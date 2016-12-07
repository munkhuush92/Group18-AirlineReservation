package uw.group18_airlinereservation;

/**
 * Created by Austin Ingraham on 12/4/2016.
 */
public class Passenger {
    private static final Passenger instance = new Passenger();
    //default fields
    public String myPassID = "NOT SET";
    public String myEmail = "NOT SET";
    public String myFirstName = "NOT SET";
    public String myLastName = "NOT SET";
    public String myCellphone = "NOT SET";
    public String myAddress = "NOT SET";

    /**
     * private constructor of passenger
     */
    private Passenger(){
    }

    public static void setValues(String thePassID, String theEmail,
                                 String theFirstName, String theLastName,
                                 String theCellphone, String theAddress) {
        instance.myPassID = thePassID;
        instance.myEmail = theEmail;
        instance.myFirstName = theFirstName;
        instance.myLastName = theLastName;
        instance.myCellphone = theCellphone;
        instance.myAddress = theAddress;
    }

    public static Passenger getPassengerObject() {
        return instance;
    }

    /**
     * get passenger ID
     * @return String PassID
     */
    public String getPassID () { return myPassID; }

    /**
     * get email
     * @return String Email
     */
    public String getEmail(){ return myEmail;  }

    /**
     * get first name
     * @return String FirstName
     */
    public String getFirstName(){ return myFirstName;  }

    /**
     * get last name
     * @return String LastName
     */
    public String getLastName(){ return myLastName;  }

    /**
     * get cellphone
     * @return String myCellphone
     */
    public String getCellphone(){ return myCellphone;  }

    /**
     * get address
     * @return String myAddress
     */
    public String getAddress(){ return myAddress;  }

    /**
     * set cellphone
     * @param  theCellphone
     */
    public void setCellphone(String theCellphone) { myCellphone = theCellphone; }

    /**
     * set address
     * @param theAddress
     */
    public void setAddress(String theAddress) { myAddress = theAddress; }

}