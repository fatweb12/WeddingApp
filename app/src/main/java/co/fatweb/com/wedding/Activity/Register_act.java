package co.fatweb.com.wedding.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import co.fatweb.com.wedding.Connectivity.RestClient;
import co.fatweb.com.wedding.DataObject.User;
import co.fatweb.com.wedding.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Register_act extends AppCompatActivity {

    EditText edt_username, edt_passwrd, edt_email, edt_website, edt_firstname, edt_lastname, edt_about;
    Button btn_register;
    ProgressDialog dialog;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_act);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_passwrd = (EditText) findViewById(R.id.edt_password);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_website = (EditText) findViewById(R.id.edt_website);
        btn_register = findViewById(R.id.btn_register);
        edt_firstname = (EditText) findViewById(R.id.edt_firstname);
        edt_lastname = (EditText) findViewById(R.id.edt_lastname);
        edt_about = (EditText) findViewById(R.id.edt_about);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignIn_act.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                attemptLogin();

            }
        });




    }



    private void attemptLogin() {

        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_passwrd = (EditText) findViewById(R.id.edt_pass);

        String username = edt_username.getText().toString().trim();
        String password = edt_passwrd.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        String website = edt_website.getText().toString().trim();
        String firstname = edt_firstname.getText().toString().trim();
        String lastname = edt_lastname.getText().toString().trim();
        String about = edt_about.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            ((EditText) findViewById(R.id.edt_pass)).setError("This field is required");
            focusView = edt_passwrd;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            ((EditText) findViewById(R.id.edt_username)).setError("This field is required");
            focusView = edt_username;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            new UserSignUp(username, password, email, firstname, lastname, website, about).execute();

        }
    }

    public class UserSignUp extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final String mEmail;
        private final String mFirstname;
        private final String mLastname;
        private final String mWebsite;
        private final String mAbout;

        Boolean success = false;
        User newUser = new User();

        String responseString;

        UserSignUp(String username, String password, String email, String firstname, String lastname, String website, String about) {
            mUsername = username;
            mPassword = password;
            mEmail = email;
            mFirstname = firstname;
            mLastname = lastname;
            mWebsite = website;
            mAbout = about;
            newUser.setEmailAddress(mEmail);
            newUser.setPassword(mPassword);
            newUser.setUsername(mUsername);
            newUser.setFirstname(mFirstname);
            newUser.setLastname(mLastname);
            newUser.setWebsite(mWebsite);
            newUser.setAbout(mAbout);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Register_act.this);
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
            // pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();
                params.put("email", newUser.getEmailAddress());
                params.put("password", newUser.getPassword());

                String str_url=  Register_act.this.getString(R.string.url_live);
                String normalUrl = str_url+"users/register";

              //  String normalUrl = "https://wedding.geek.fatweb.co.nz/api/users/register";
                android.util.Log.i("login", normalUrl);


                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", newUser.getEmailAddress());
                jObject.accumulate("password", newUser.getPassword());
                jObject.accumulate("username", newUser.getUsername());
                jObject.accumulate("first_name", newUser.getFirstname());
                jObject.accumulate("last_name", newUser.getLastname());
                jObject.accumulate("about_us", newUser.getAbout());
                jObject.accumulate("website", newUser.getWebsite());


                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(Register_act.this, normalUrl, entity, new JsonHttpResponseHandler() {
                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        success = false;
                        responseString = response;
                        android.util.Log.d("response", responseString.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        success = false;
                        //                       responseString = errorResponse.toString();
                        android.util.Log.d("response", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        android.util.Log.d("response", errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                        android.util.Log.d("response", responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        android.util.Log.i("responseString", responseString);
                        success = true;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        android.util.Log.i("JSONObject response", response.toString());
                        try {
                            //success = true;
                            if (response.has("status")) {
                                if (response.getString("status").contains("fail")) {
                                    success = false;
                                } else {
                                    success = true;

                                }

                                newUser = new Gson().fromJson(response.toString(), User.class);
                             /*   prefs.put(RestoAllergyApplication.thisUser, new Gson().toJson(newUser));
                                prefs.put(RestoAllergyApplication.VERSION, newUser.getAndroidVersion());
                                android.util.Log.i("prefs", prefs.getString(RestoAllergyApplication.thisUser));*/
                            } else {
                                success = false;

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        android.util.Log.i("JSONArray response", response.toString());
                        success = true;
                    }
                });

                return success;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            dialog.cancel();
            //pbLoading.setVisibility(View.GONE);
            if (success) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Register_act.this);
                //  builder.setTitle("Registration successful");
                builder.setMessage(newUser.getMessage());
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       /* String passwrd = newUser.getPassword();
                        String email = newUser.getEmailAddress();
                        String firstname = newUser.getFirstname();
                        String lastname = newUser.getLastname();
                        String phnnumber = newUser.getPhnnumber();
                        String userid = newUser.getId();
                        *//*String id = newUser.getId1();*//*
                        //      Toast.makeText(SignUp_act.this, "" +lastname + passwrd + email + firstname, Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("password", passwrd);
                        editor.putString("email", email);
                        editor.putString("firstname", firstname);
                        editor.putString("phone", phnnumber);
                        // editor.putString("id", id);
                        editor.putString("lastname", lastname);
                        editor.putString("password", passwrd);

                        editor.putString("id", userid);
                        editor.apply();*/


                        new UserLogin(edt_email.getText().toString(), edt_passwrd.getText().toString()).execute();
                       /* Intent in = new Intent(SignUp_act.this, Home_act.class);
                        startActivity(in);*/


                    }
                });
                builder.show();


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Register_act.this);
                builder.setTitle("Registration error!");
                builder.setMessage(newUser.getMessage());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();


            }
        }
    }

    public class UserLogin extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        Boolean success = false;
        User newUser = new User();

        String responseString;

        UserLogin(String username, String password) {
            mUsername = username;
            mPassword = password;
            newUser.setEmailAddress(mUsername);
            newUser.setPassword(mPassword);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Register_act.this);
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
            // pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();
                params.put("email", newUser.getEmailAddress());
                params.put("password", newUser.getPassword());

                String str_url=  Register_act.this.getString(R.string.url_live);
                String normalUrl = str_url+"users/login";

               // String normalUrl = "http://onestoptrade.co.nz/api/users/login";
                android.util.Log.i("login", normalUrl + "?" + params.toString());


                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", newUser.getEmailAddress());
                jObject.accumulate("password", newUser.getPassword());

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(Register_act.this, normalUrl, entity, new JsonHttpResponseHandler() {
                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        success = false;
                        responseString = response;
                        android.util.Log.d("response", responseString.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        success = false;
                        //                       responseString = errorResponse.toString();
                        android.util.Log.d("response", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        android.util.Log.d("response", errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                        android.util.Log.d("response", responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        android.util.Log.i("responseString", responseString);
                        success = true;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        android.util.Log.i("JSONObject response", response.toString());
                        try {
                            //success = true;
                            if (response.has("status")) {
                                if (response.getString("status").contains("fail")) {
                                    success = false;
                                } else {
                                    success = true;

                                }

                                newUser = new Gson().fromJson(response.toString(), User.class);
                             /*   prefs.put(RestoAllergyApplication.thisUser, new Gson().toJson(newUser));
                                prefs.put(RestoAllergyApplication.VERSION, newUser.getAndroidVersion());
                                android.util.Log.i("prefs", prefs.getString(RestoAllergyApplication.thisUser));*/
                            } else {
                                success = false;

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        android.util.Log.i("JSONArray response", response.toString());
                        success = true;
                    }
                });

                return success;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            dialog.cancel();
            //pbLoading.setVisibility(View.GONE);
            if (success) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Register_act.this);
                /* builder.setTitle("Login successful");*/
                builder.setMessage(newUser.getMessage());
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = newUser.getUsername();
                        String passwrd = newUser.getPassword();
                        String email = newUser.getEmailAddress();
                        String firstname = newUser.getFirstname();
                        String lastname = newUser.getLastname();
                        String userid = newUser.getId();

                        // Toast.makeText(SignIn_act.this, "" + username + passwrd + email + firstname, Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("username", username);
                        editor.putString("password", passwrd);
                        editor.putString("email", email);
                        editor.putString("firstname", firstname);
                        editor.putString("id", userid);

                        editor.putString("lastname", lastname);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), Navigation_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        edt_username.setText("");
                        edt_passwrd.setText("");
                        /* if (checkVersion(newUser.getAndroidVersion())) {*/
                        // navigate();
                        // }
                    }
                });
                builder.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Register_act.this);
                builder.setTitle("Login error!");
                builder.setMessage(newUser.getMessage());
               /* Toast.makeText(Home_act.this, "login : " +
                        ""+newUser.getMessage(), Toast.LENGTH_SHORT).show();*/
                // builder.setMessage(newUser.getMessage());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        }


    }

}
