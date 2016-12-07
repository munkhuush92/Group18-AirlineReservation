package uw.group18_airlinereservation;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private final String UPDATE_URL =
            "http://students.washington.edu/adi1996/airline.php?cmd=update_profile";
    private EditText myCellphoneField;
    private EditText myAddressField;
    private Passenger myPassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myPassenger = Passenger.getPassengerObject();

        TextView firstNameField = (TextView) findViewById(R.id.firstNameField);
        TextView lastNameField = (TextView) findViewById(R.id.lastNameField);
        myCellphoneField = (EditText) findViewById(R.id.cellphoneField);
        myAddressField = (EditText) findViewById(R.id.addressField);

        firstNameField.setText(myPassenger.getFirstName());
        lastNameField.setText(myPassenger.getLastName());
        myAddressField.setText(myPassenger.getAddress());
        if(myPassenger.getCellphone().equals("null")) {
            myCellphoneField.setText("");
        } else {
            myCellphoneField.setText(myPassenger.getCellphone());
        }
    }

    public void updatePassengerInfoButtonPress(View v) {
        /*Toast.makeText(getApplicationContext(), "Update not yet implemented.",
                Toast.LENGTH_LONG).show(); */
        String theURL = UPDATE_URL + "&PassID=" + myPassenger.getPassID();
        try {
            if (!myCellphoneField.getText().toString().equals(myPassenger.getCellphone())) {
                theURL += "&Cellphone=" + URLEncoder.encode(myCellphoneField.getText().toString(), "UTF-8");
                myPassenger.setCellphone(myCellphoneField.getText().toString());
            }
            if (!myAddressField.getText().toString().equals(myPassenger.getAddress())) {
                theURL += "&Address=" + URLEncoder.encode(myAddressField.getText().toString(), "UTF-8");
                myPassenger.setAddress(myAddressField.getText().toString());
            }
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(),
                    "Unsupported encoding exception when preparing input for query",
                    Toast.LENGTH_LONG).show();
        }
        new UpdateTask().execute(theURL);
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
                JSONObject status = new JSONObject(jsonString);
                if(status.has("ERROR")) {
                    Toast.makeText(getApplicationContext(), status.get("ERROR").toString(),
                            Toast.LENGTH_LONG).show();
                } else {
                    if(status.getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), "Profile updated.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to update profile",
                                Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Unable to parse JSON", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    private class UpdateTask extends AsyncTask<String, Void, String> {

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
                Toast.makeText(getApplicationContext(), "Unable to update profile, please try again.", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
