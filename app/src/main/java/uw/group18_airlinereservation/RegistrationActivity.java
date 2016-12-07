package uw.group18_airlinereservation;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity {

    private String REGISTER =
            "http://students.washington.edu/adi1996/airline.php?cmd=register_passenger";
    //so many fields!
    public static TextView myBirthDate;
    private TextView myEmail;
    private TextView myPassword;
    private TextView myConfirmPassword;
    private TextView myFirstName;
    private TextView myLastName;
    private TextView myCitizenship;
    private TextView myAddress;
    private TextView myCellPhone;
    private Spinner myGenderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        myBirthDate = (TextView) findViewById(R.id.dateOfBirth);
        myEmail = (TextView) findViewById(R.id.emailField);
        myPassword = (TextView) findViewById(R.id.passwordField);
        myConfirmPassword = (TextView) findViewById(R.id.confirmPassword);
        myFirstName = (TextView) findViewById(R.id.firstNameField);
        myLastName = (TextView) findViewById(R.id.lastNameField);
        myCitizenship = (TextView) findViewById(R.id.citizenshipField);
        myAddress = (TextView) findViewById(R.id.addressField);
        myCellPhone = (TextView) findViewById(R.id.cellphoneField);
        myGenderSpinner = (Spinner) findViewById(R.id.gender_spinner);
    }

    public void launchBirthdateDialog(View v) {
        DialogFragment fragment = new DatePickerDialogFragment();
        fragment.show(getSupportFragmentManager(), "launchBirthdateDialog");
    }

    public void registerButtonPress(View v) {
        if(myPassword.getText().toString().equals(myConfirmPassword.getText().toString())) {
            new RegisterTask().execute(buildURL());
        } else {
            Toast.makeText(getApplicationContext(),
                    "Your confirmation password did not match your password.", Toast.LENGTH_LONG).show();
            myPassword.setText("");
            myConfirmPassword.setText("");
        }
    }

    private String buildURL() {
        String result = "";
        try {
            result = REGISTER + "&Email=" + URLEncoder.encode(myEmail.getText().toString(), "UTF-8") +
                    "&Password=" + URLEncoder.encode(myPassword.getText().toString(), "UTF-8") +
                    "&Fname=" + URLEncoder.encode(myFirstName.getText().toString(), "UTF-8") +
                    "&Lname=" + URLEncoder.encode(myLastName.getText().toString(), "UTF-8") +
                    "&Address=" + URLEncoder.encode(myAddress.getText().toString(), "UTF-8") +
                    "&Citizenship=" + URLEncoder.encode(myCitizenship.getText().toString(), "UTF-8") +
                    "&DateOfBirth=" + myBirthDate.getText().toString() +
                    "&Gender=" + myGenderSpinner.getSelectedItem().toString();
            if(myCellPhone.getText().toString().length() >= 1) {
                result += "&cellphone=" + URLEncoder.encode(myCellPhone.getText().toString(), "UTF-8");
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "There was an error preparing the URL",
                    Toast.LENGTH_LONG).show();
        }
        return result;
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
                JSONObject json = new JSONObject(jsonString);
                if(json.has("registration")) {
                    if (json.getBoolean("registration")) {
                        Toast.makeText(getApplicationContext(),
                                "Registration successful!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Registration failed."
                                , Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            json.get("ERROR").toString()
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Unable to parse JSON: " + e.toString(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {

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
                    response = "Unable to register, reason: ";
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
                Toast.makeText(getApplicationContext(), "Registration failed, please try again.", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
}