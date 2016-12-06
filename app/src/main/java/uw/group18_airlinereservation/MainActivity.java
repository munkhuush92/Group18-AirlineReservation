package uw.group18_airlinereservation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    /** A button for directing to Booking layout*/
     Button bookButton;

    private TextView myLoginStatus; //displays who you're logged in as


    public void init(){
        bookButton = (Button) findViewById(R.id.bookButton);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent initialize = new Intent(MainActivity.this, booking.class);
                startActivity(initialize);

            }
        });
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
}
