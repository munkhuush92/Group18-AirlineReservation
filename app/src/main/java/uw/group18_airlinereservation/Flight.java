package uw.group18_airlinereservation;

/**
 * Created by Austin Ingraham on 12/6/2016.
 */

public class Flight {
    private String myAirplaneName;
    private String myDeparturePoint;
    private String myDepartureTime;
    private String myArrivalPoint;
    private String myArrivalTime;
    private String myPrice;
    //need id!

    public Flight(String theAirplaneName, String thePrice,
                  String theDeparturePoint, String theDepartureTime,
                  String theArrivalPoint, String theArrivalTime) {
        myAirplaneName = theAirplaneName;
        myDeparturePoint = theDeparturePoint;
        myDepartureTime = theDepartureTime;
        myArrivalPoint = theArrivalPoint;
        myArrivalTime = theArrivalTime;
        myPrice = "$" + thePrice;
    }

    public String getAirplaneName() {
        return myAirplaneName;
    }

    public String getDepartureTime() {
        return myDepartureTime;
    }

    public String getDepartureLocation() {
        return myDeparturePoint;
    }

    public String getArrivalTime() {
        return myArrivalTime;
    }

    public String getArrivalLocation() {
        return myArrivalPoint;
    }

    public String getPrice() {
        return myPrice;
    }

    @Override
    public String toString() {
        return myDepartureTime + ": " + myDeparturePoint + " to " + myArrivalPoint;
    }
}
