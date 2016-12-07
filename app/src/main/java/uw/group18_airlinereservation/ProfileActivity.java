package uw.group18_airlinereservation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private EditText myCellphoneField;
    private EditText myAddressField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Passenger aPassenger = Passenger.getPassengerObject();
        TextView firstNameField = (TextView) findViewById(R.id.firstNameField);
        TextView lastNameField = (TextView) findViewById(R.id.lastNameField);
        myCellphoneField = (EditText) findViewById(R.id.cellphoneField);
        myAddressField = (EditText) findViewById(R.id.addressField);

        firstNameField.setText(aPassenger.getFirstName());
        lastNameField.setText(aPassenger.getLastName());
        myCellphoneField.setText(aPassenger.getCellphone());
        myAddressField.setText(aPassenger.getAddress());
    }

    public void updatePassengerInfoButtonPress(View v) {
        Toast.makeText(getApplicationContext(), "Update not yet implemented.",
                Toast.LENGTH_LONG).show();
    }
}
