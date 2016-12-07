package uw.group18_airlinereservation;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_URL
            = "http://students.washington.edu/adi1996/airline.php?cmd=login";
    private Passenger myPassenger;
    public static boolean loggedIn = false; //used for when user backs out of main activity

    private EditText myEmailField;
    private EditText myPasswordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(loggedIn) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            myEmailField = (EditText) findViewById(R.id.emailField);
            myPasswordField = (EditText) findViewById(R.id.passwordField);
        }
    }

    public void loginButtonPress(View v) {
        //Toast.makeText(v.getContext(), "Logged in!", Toast.LENGTH_LONG).show();

        String email = myEmailField.getText().toString();
        String password = myPasswordField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(v.getContext(), "Enter Email", Toast.LENGTH_SHORT).show();
            myEmailField.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(v.getContext(), "Enter password", Toast.LENGTH_SHORT).show();
            myPasswordField.requestFocus();
            return;
        }
        //Toast.makeText(v.getContext(), "EMAIL: " + email + ", PASS: " + password, Toast.LENGTH_LONG).show();
        new LoginTask().execute(LOGIN_URL + "&Email=" + email + "&Password=" + password);
    }

    public void registerButtonPress(View v) {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
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
                if(!json.has("loggedin")) {
                    Passenger.setValues(json.getString("PassID"),
                            json.getString("Email"), json.getString("Fname"),
                            json.getString("Lname"), json.getString("Cellphone"),
                            json.getString("Address"));
                    Passenger myPassenger = Passenger.getPassengerObject();
                    loggedIn = true;
                } else {
                    loggedIn = false;
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Unable to parse JSON",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private class LoginTask extends AsyncTask<String, Void, String> {

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
                    response = "Unable to log in, Reason: ";
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

                if(loggedIn) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid login information.", Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Login failed, please try again.", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
}