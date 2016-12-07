package uw.group18_airlinereservation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView myLoginStatus; //displays who you're logged in as


    public void init(){
        myLoginStatus = (TextView) findViewById(R.id.loginStatus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Passenger aPassenger = Passenger.getPassengerObject();
        myLoginStatus.setText("You are logged in as " +
                aPassenger.getFirstName() + " " +
                aPassenger.getLastName() + ".");
    }

    public void bookFlightButtonPress(View v) {
        Intent initialize = new Intent(MainActivity.this, BookingActivity.class);
        startActivity(initialize);
    }

    public void viewTripsButtonPress(View v) {
        Intent initialize = new Intent(MainActivity.this, BookingListActivity.class);
        startActivity(initialize);
    }

    public void viewProfileButtonPress(View v) {
        Intent initialize = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(initialize);
    }
}
