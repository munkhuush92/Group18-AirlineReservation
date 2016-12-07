package uw.group18_airlinereservation;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by admin on 12/6/2016.
 */
public class BookingListActivity extends AppCompatActivity {

    //array adapter which holds json data
    ArrayAdapter<String> myBookingAdapter;
    //Json Data
    String[] myJsonData;

    //address of the server + myPassID
    String myurl = "http://students.washington.edu/adi1996/airline.php?cmd=get_user_bookings&id=";
    //Passenger ID
    int myPassID;

    ListView listviewofBookings;
    private BufferedInputStream myInputSteam= null;
    String myLineResult;
    String myFullResult;
    TextView myinstructionTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinglist);
         listviewofBookings = (ListView) findViewById(R.id.bookinglistView);
        myinstructionTextView = (TextView) findViewById(R.id.insttextview);
        myPassID = Integer.parseInt(Passenger.getPassengerObject().getPassID());
        //ALLOW NETWORK IN MAIN THREAD
         StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        //RETRIEVE DATA
        getData();
        if(myJsonData.length>=1){
            myBookingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myJsonData );
            listviewofBookings.setAdapter(myBookingAdapter);
        }else{
            // if the user has no booking then display
            Toast.makeText(this," You have no booking \n You have not booked a flight!", Toast.LENGTH_LONG).show();
        }

    }



    private void getData()
    {
        try {
            myurl = myurl + myPassID;
            URL url = new URL(myurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            myInputSteam = new BufferedInputStream(con.getInputStream());


        } catch (Exception e) {
            e.printStackTrace();
        }
        //Read context into string
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(myInputSteam));
            StringBuilder sb = new StringBuilder();

            while((myLineResult=br.readLine())!=null){
                sb.append(myLineResult+"\n");

            }
            myInputSteam.close();
            myFullResult = sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        //Parse json data
        try{
            JSONArray js = new JSONArray(myFullResult);
            JSONObject jo = null;
            myJsonData = new String[js.length()];

            for( int i= 0; i<js.length(); i++){
                jo = js.getJSONObject(i);
                myJsonData[i] = "Booking ID: "+ jo.getString("BookingID") + " | Flight ID: "+jo.getString("FlightID")
                                +"| Total Price: " +jo.getString("PricePaid")+ "$ |\n Seat Number: "+ jo.getString("SeatAssigned");

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
