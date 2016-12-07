package uw.group18_airlinereservation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import static android.R.attr.button;
import static android.R.attr.data;

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
    ArrayAdapter<String> myAdapter;
    ArrayAdapter<String> myDepAdapter;
    ArrayAdapter<String> myArrAdapter;
    String[] myJsonDatas;
    String[] myDepLocations;
    String[] myArrLocations;
    //test
    Spinner myFromSpinner;
    Spinner myToSpinner;
    Button searchButton;
    /** textview strings that are used for search results   */
    String depString;
    String arrString;
    /** DO-TO   */
    String depDateString;
    String arrDateString;
    private boolean isRoundTrip = false;
    Spinner myFlightsSpinner;
    /** Address of the server  */
    String myurl = "http://students.washington.edu/adi1996/airline.php?cmd=all_flights";
    /** input stream for json data */
    InputStream myInputStream = null;
    /** Each line that is being read from database */
    String myLine = null;
    /** the result of converted string from the database */
    String result;
    Button mybookaddButton;
    // ListView myBookFlights;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);
        //Initiliaze Dep and Return spinners
        depSpinnerDay = (Spinner) findViewById(R.id.daysSpinner);
        depSpinnerMonth = (Spinner)findViewById(R.id.monthsSpinner);
        depSpinnerYear = (Spinner)findViewById(R.id.yearsSpinner);
        retSpinnerDay = (Spinner)findViewById(R.id.daysSpinner2);
        retSpinnerMonth = (Spinner)findViewById(R.id.monthsSpinner2);
        retSpinnerYear = (Spinner)findViewById(R.id.yearsSpinner2);
        /** initially retrun date spinners are off because one-way selected */
        retSpinnerDay.setEnabled(false);
        retSpinnerMonth.setEnabled(false);
        retSpinnerYear.setEnabled(false);
        myFlightsSpinner = (Spinner) findViewById(R.id.flightsSpinner);
        addListenerOnButton();
        /** initiliaze list views with full of flight options
         *
         */
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        //initiaze auto text views
        myToSpinner = (Spinner) findViewById(R.id.spinnerarrlocations);
        myFromSpinner = (Spinner) findViewById(R.id.spinnerlocations);
        updateJson();
        myFromSpinner.setAdapter(myDepAdapter);
        myToSpinner.setAdapter(myArrAdapter);
    }
    private void updateJson(){
        //Retrieve our data
        getData();
        //Adapter
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myJsonDatas);
        myDepAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myDepLocations);
        myArrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myArrLocations);
    }
    private void getData(){
        try {
            URL url = new URL(myurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            myInputStream = new BufferedInputStream(con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // CONVERT INTO STRING
        try{
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(myInputStream));
            StringBuilder sb  = new StringBuilder();
            while((myLine=buffReader.readLine())!=null){
                sb.append(myLine+"\n");
            }
            buffReader.close();
            result = sb.toString();
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        // parse json data
        try{
            JSONArray myJsonArr = new JSONArray(result);
            JSONObject jsonObject = null;
            myJsonDatas = new String[myJsonArr.length()];
            myDepLocations = new String[myJsonArr.length()];
            myArrLocations = new String[myJsonArr.length()];
            for(int i =0 ; i < myJsonArr.length(); i++){
                jsonObject = myJsonArr.getJSONObject(i);
                //if one way, then we choose this code
                if(jsonObject.getString("dep").equals(depString) && jsonObject.getString("arr").equals(arrString)) {
                    if(depDateString.equals(jsonObject.getString("deptime").substring(5, 10))) {
//                        if(isRoundTrip) {
//                            if(arrDateString.equals(jsonObject.getString("arrtime").substring(5, 10))) {
//                                //if it is roundtrip
//                                String fullText = "" + jsonObject.getString("airplanename") + "/Departs From: " + depString + "/Dep Date: " +
//                                        jsonObject.getString("deptime").substring(5, 16) + "/Arrive To: " + arrString + "/Arrival Date: "
//                                        + jsonObject.getString("arrtime").substring(5, 16) + "/ Price: " + jsonObject.getString("price").substring(0, 5) + "$";
//
//                                myJsonDatas[i] = fullText;
//                                myDepLocations[i] = jsonObject.getString("dep");
//                                myArrLocations[i] = jsonObject.getString("arr");
//                            }else{
//                                myJsonDatas[i] = "NO FLIGHTS";
//                                myDepLocations[i]= jsonObject.getString("dep");
//                                myArrLocations[i] = jsonObject.getString("arr");
//                            }
//                        }else{
                        //if it is roundtrip
                        String fullText = "" + jsonObject.getString("airplanename") + "/Departs From: " + depString + "/Dep Date: " +
                                jsonObject.getString("deptime").substring(5, 16) + "/Arrive To: " + arrString + "/Arrival Date: "
                                + jsonObject.getString("arrtime").substring(5, 16) + "/ Price: " + jsonObject.getString("price").substring(0, 5) + "$";
                        myJsonDatas[i] = fullText;
                        myDepLocations[i] = jsonObject.getString("dep");
                        myArrLocations[i] = jsonObject.getString("arr");
                        //  }
                    }else{
                        myJsonDatas[i] = "NO FLIGHTS";
                        myDepLocations[i]= jsonObject.getString("dep");
                        myArrLocations[i] = jsonObject.getString("arr");
                    }
                }else {
                    myJsonDatas[i] = "";
                    myDepLocations[i]= jsonObject.getString("dep");
                    myArrLocations[i] = jsonObject.getString("arr");
                }
                //if roundtrip then
            }
            System.out.println(myJsonArr.length());
        }catch(Exception e){
            e.printStackTrace();
        }
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
                isRoundTrip = false;
            }
        });
        myTripButton = (RadioButton) findViewById(R.id.radioTrip);
        myTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRoundTrip = true;
                retSpinnerDay.setEnabled(true);
                retSpinnerMonth.setEnabled(true);
                retSpinnerYear.setEnabled(true);
            }
        });
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test
                depString = myFromSpinner.getSelectedItem().toString();
                arrString= myToSpinner.getSelectedItem().toString();
                String dayData = depSpinnerDay.getSelectedItem().toString();
                if(dayData.length()!=1){
                    depDateString = depSpinnerMonth.getSelectedItem().toString() + "-"+ depSpinnerDay.getSelectedItem().toString();
//                    if(isRoundTrip){
//                        arrDateString = retSpinnerMonth.getSelectedItem().toString()+"-" + retSpinnerDay.getSelectedItem().toString();
//                    }
                }else{
                    depDateString = depSpinnerMonth.getSelectedItem().toString() + "-0"+ depSpinnerDay.getSelectedItem().toString();
//                    if(isRoundTrip){
//
//                        arrDateString = retSpinnerMonth.getSelectedItem().toString()+"-0" + retSpinnerDay.getSelectedItem().toString();
//                    }
                }
                updateJson();
//                mybookaddButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
                myFlightsSpinner.setAdapter(myAdapter);
            }
        });
    }
}