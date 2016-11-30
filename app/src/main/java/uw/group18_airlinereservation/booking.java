package uw.group18_airlinereservation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by admin on 11/28/2016.
 */

public class booking extends AppCompatActivity {

    RadioGroup myButtonGroup;
    RadioButton myOnewayButton;
    RadioButton myTripButton;
    Spinner depSpinnerDay;
    Spinner depSpinnerMonth;
    Spinner depSpinnerYear;
    Spinner retSpinnerDay;
    Spinner retSpinnerMonth;
    Spinner retSpinnerYear;
    ArrayList<CheckBox> myCheckedBoxList;
    RadioGroup radioGroupFlights;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);
        //Initiliaze Dep and Return spinners
        retSpinnerDay = (Spinner)findViewById(R.id.daysSpinner2);
        retSpinnerMonth = (Spinner)findViewById(R.id.monthsSpinner2);
        retSpinnerYear = (Spinner)findViewById(R.id.yearsSpinner2);
        /** initially retrun date spinners are off because one-way selected */
        retSpinnerDay.setEnabled(false);
        retSpinnerMonth.setEnabled(false);
        retSpinnerYear.setEnabled(false);

        addListenerOnButton();
        /** initiliaze checkboxes given number of items */
        // change counter with size of given list of flights


    }
    public void addListenerOnButton() {

        myButtonGroup = (RadioGroup) findViewById(R.id.radioGroup);
        myOnewayButton = (RadioButton)findViewById(R.id.radioOneWay);
        myOnewayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retSpinnerDay.setEnabled(false);
                retSpinnerMonth.setEnabled(false);
                retSpinnerYear.setEnabled(false);
            }
        });
        myTripButton = (RadioButton) findViewById(R.id.radioTrip);
        myTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retSpinnerDay.setEnabled(true);
                retSpinnerMonth.setEnabled(true);
                retSpinnerYear.setEnabled(true);
            }
        });
        radioGroupFlights =(RadioGroup) findViewById(R.id.radioGroupFlights);






    }



}
