package uw.group18_airlinereservation;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private final String FLIGHT_URL =
            "http://students.washington.edu/adi1996/airline.php?cmd=all_flights";
    private Spinner myFlightSpinner;
    private TextView myPriceField;
    private EditText myMealPlanField;

    private List<Flight> myFlightArray = new ArrayList<>();
    private boolean getFlights = true;
    //TODO Round trips!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        myFlightSpinner = (Spinner) findViewById(R.id.flight_spinner);
        myPriceField = (TextView) findViewById(R.id.priceField);
        myMealPlanField = (EditText) findViewById(R.id.mealPlanField);
        populateSpinner();
        myFlightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                myPriceField.setText(myFlightArray.get(position).getPrice());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void populateSpinner() {
        new RetrieveFlightsTask().execute(FLIGHT_URL);
    }

    public void completeReservationButtonPress(View v) {
        String mealPlan = myMealPlanField.getText().toString();
        if(mealPlan.length() >= 1) {
            Flight chosenFlight = myFlightArray.get(myFlightSpinner.getSelectedItemPosition());

        } else {
            Toast.makeText(getApplicationContext(), "Please choose a meal.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param jsonString
     * @return reason or null if successful.
     */
    private void parseJSON(String jsonString) {
        if (jsonString != null) {
            try {
                if(getFlights) {
                    List<String> spinnerArray =  new ArrayList<>();
                    JSONArray flights = new JSONArray(jsonString);
                    for(int i = 0; i < flights.length(); i++) {
                        JSONObject aFlight = flights.getJSONObject(i);
                        myFlightArray.add(new Flight(aFlight.getString("airplanename"),
                                                    aFlight.getString("price"),
                                                    aFlight.getString("id"),
                                                    aFlight.getString("dep"),
                                                    aFlight.getString("deptime"),
                                                    aFlight.getString("arr"),
                                                    aFlight.getString("arrtime")));
                        spinnerArray.add(myFlightArray.get(i).toString());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this, android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    myFlightSpinner.setAdapter(adapter);

                    myPriceField.setText("$" + myFlightArray.get(0).getPrice());

                    getFlights = false;
                } else {

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Unable to parse JSON", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    private class RetrieveFlightsTask extends AsyncTask<String, Void, String> {

        /**
         * perform downloading username and password in the background
         * @param urls
         * @return success if download is successful.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to access database, Reason: ";
                    if (e.getMessage().startsWith("Unable to resolve host")){
                        response += "Could not contact remote server.  Check your internet connection.";
                    } else {
                        response += e.getMessage();
                    }
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * check if download is successful
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            if(!result.contains("Unable to")) {
                parseJSON(result);
            } else {
                if(getFlights) {
                    Toast.makeText(getApplicationContext(), "Could not get flight listing, please try again.", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Could not book flight, please try again.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
