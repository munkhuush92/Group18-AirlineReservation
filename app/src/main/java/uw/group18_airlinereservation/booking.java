package uw.group18_airlinereservation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    ArrayList<CheckBox> myCheckedBoxList;
    ListView myBookFlights;
    ArrayAdapter<String> myAdapter;
    String[] myJsonDatas;

    /** Autocomplete textviews for FROM AND TO*/
    AutoCompleteTextView fromTextView;
    AutoCompleteTextView toTextView;
    Button searchButton;

    /** textview strings that are used for search results   */
    String depString;
    String arrString;
    /** DO-TO   */
    String depDateString;
    String arrDateString;



    /** Address of the server  */
    String myurl = "http://10.0.2.2/data/test/fetch.php";

    /** input stream for json data */
    InputStream myInputStream = null;

    /** Each line that is being read from database */
    String myLine = null;

    /** the result of converted string from the database */
    String result;

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


        addListenerOnButton();
        /** initiliaze list views with full of flight options
         *
         */
        myBookFlights = (ListView)findViewById(R.id.listViewflights);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        //initiaze auto text views
        toTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewTO);
        fromTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewFROM);
        updateJson();




    }



    private void updateJson(){
        //Retrieve our data
        getData();
        //Adapter
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myJsonDatas);
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

        }catch (Exception e){
            e.printStackTrace();
        }

        // parse json data
        try{
            JSONArray myJsonArr = new JSONArray(result);
            JSONObject jsonObject = null;
            myJsonDatas = new String[myJsonArr.length()];

            for(int i =0 ; i < myJsonArr.length(); i++){
                jsonObject = myJsonArr.getJSONObject(i);

                if(jsonObject.getString("dep").equals(depString) && jsonObject.getString("arr").equals(arrString)) {
                    String fullText = "" + jsonObject.getString("airplanename") +"/Departs From: "+ depString + "/Dep Date: " +
                            jsonObject.getString("deptime").substring(5,16)+"/Arrive To: "+arrString+"/Arrival Date: "
                            +jsonObject.getString("arrtime").substring(5,16)+"/ Price: "+jsonObject.getString("price").substring(0,5)+"$" ;

                    myJsonDatas[i] = fullText;
                }else {
                    myJsonDatas[i] = "";
                }

            }
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
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depString = fromTextView.getText().toString();

                arrString= toTextView.getText().toString();
                depDateString = depSpinnerDay.getSelectedItem().toString() + " / "+ depSpinnerMonth.getSelectedItem().toString()
                        +" "+ depSpinnerYear.getSelectedItem().toString();

                updateJson();
                myBookFlights.setAdapter(myAdapter);

            }
        });




    }





}
