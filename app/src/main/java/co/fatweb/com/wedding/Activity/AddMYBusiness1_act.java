package co.fatweb.com.wedding.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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

public class AddMYBusiness1_act extends AppCompatActivity {

    LinearLayout back;
    TextView txt_signin, txt1, txt_catname;
    EditText edt_businessphn, edt_address, edt_suburb, edt_city, edt_postcode, edt_firstname, edt_lastname,
            edt_phnno, edt_email, edt_password, edt_confrmpass, edt_detailpostcode;
    Button btn_savedata;
    String str_businessphn, str_address, str_suburb, str_city, str_bpostcode, str_name, cat_id, business_name,
            str_lname, str_phnnumber, str_email, str_postode, str_passwrd, str_confrmpass, suser, stremail, strname, strlstname, phnno;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mybusiness1_act);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_signin = findViewById(R.id.txt_signin);
        txt1 = findViewById(R.id.txt1);
        Intent i = getIntent();
        cat_id = i.getStringExtra("Cat_id");
        txt_catname = findViewById(R.id.txt_catname);
        business_name = i.getStringExtra("business_name");

        txt_catname.setText(business_name);

        // Toast.makeText(this, "" + cat_id + business_name, Toast.LENGTH_SHORT).show();
        edt_businessphn = (EditText) findViewById(R.id.edt_businessphn);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_suburb = (EditText) findViewById(R.id.edt_suberb);
        edt_city = (EditText) findViewById(R.id.edt_city);
        edt_postcode = (EditText) findViewById(R.id.edt_postcode);
        edt_firstname = (EditText) findViewById(R.id.edt_firstname);
        edt_lastname = (EditText) findViewById(R.id.edt_lastname);
        edt_phnno = (EditText) findViewById(R.id.edt_phnnumber);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_detailpostcode = (EditText) findViewById(R.id.edt_postcodedetail);
        edt_password = (EditText) findViewById(R.id.edt_passwrd);
        edt_confrmpass = (EditText) findViewById(R.id.edt_confrmpassword);
        btn_savedata = findViewById(R.id.btn_addbusiness);
        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        suser = sp.getString("id", null);
        String spass = sp.getString("password", null);
        new GetUserDetail(suser).execute();
        if (suser != null) {
            txt1.setVisibility(View.GONE);
            txt_signin.setVisibility(View.GONE);

            edt_password.setVisibility(View.GONE);
            edt_confrmpass.setVisibility(View.GONE);
            edt_detailpostcode.setVisibility(View.GONE);

        } else {

        }

        btn_savedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (suser != null) {
                    txt1.setVisibility(View.GONE);
                    txt_signin.setVisibility(View.GONE);
                    attemptLogin1();

                } else {
                    // Toast.makeText(AddMYBusiness1_act.this, "dadad", Toast.LENGTH_SHORT).show();
                    new MaterialDialog.Builder(AddMYBusiness1_act.this)
                            .content("You have to sign in first to Add business")
                            .positiveText("Sign In")
                            .negativeText("Cancel")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(AddMYBusiness1_act.this, SignIn_act.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finishAffinity();
                                    dialog.dismiss();

                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                }
                            }).show();

                }


            }
        });
        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignIn_act.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });


    }

    private void attemptLogin() {


        String password = edt_password.getText().toString();
        String email = edt_email.getText().toString().trim();
        String firstname = edt_firstname.getText().toString();
        String lastname = edt_lastname.getText().toString();
        phnno = edt_phnno.getText().toString();
        String postcode = edt_detailpostcode.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            ((EditText) findViewById(R.id.edt_passwrd)).setError("This field is required");
            focusView = edt_password;
            cancel = true;
        }

        // Check for a valid email address.


        if (cancel) {
            focusView.requestFocus();
        } else {

            new UserSignUp(password, email, firstname, lastname, phnno, postcode).execute();

        }
    }

    private void attemptLogin1() {


        String bphn = edt_businessphn.getText().toString();
        String baddress = edt_address.getText().toString().trim();
        String bsuburb = edt_suburb.getText().toString();
        String bcity = edt_city.getText().toString();
        String bpost = edt_postcode.getText().toString();
        phnno = edt_phnno.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(bphn)) {
            ((EditText) findViewById(R.id.edt_businessphn)).setError("This field is required");
            focusView = edt_password;
            cancel = true;
        }
        if (TextUtils.isEmpty(baddress)) {
            ((EditText) findViewById(R.id.edt_address)).setError("This field is required");
            focusView = edt_password;
            cancel = true;
        }


        // Check for a valid email address.


        if (cancel) {
            focusView.requestFocus();
        } else {

            new UserSignUp_business(bphn, baddress, bsuburb, bcity, bpost, suser, phnno).execute();

        }
    }

    public class UserSignUp_business extends AsyncTask<Void, Void, Boolean> {

        private final String mBadress;
        private final String mbSuburb;
        private final String mbCity;
        private final String mBpostcode;
        private final String mbphn;
        private final String mid;

        private final String muserphn;

        Boolean success = false;
        User newUser = new User();

        String responseString;

        UserSignUp_business(String bphn, String badress, String bsburb, String bcity, String bpostcode, String id, String user_phn) {

            mBadress = badress;
            mbSuburb = bsburb;
            mbCity = bcity;
            mBpostcode = bpostcode;
            mbphn = bphn;
            mid = id;
            muserphn = user_phn;

            newUser.setBaddress(mBadress);
            newUser.setBsuburb(mbSuburb);

            newUser.setBcity(mbCity);
            newUser.setBpostcode(mBpostcode);
            newUser.setUser_phn(muserphn);
            newUser.setBphn(mbphn);

            newUser.setId(mid);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddMYBusiness1_act.this);
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
            // pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();
               /* params.put("name", newUser.get());
                params.put("phone", newUser.getBphn());
*/
                String normalUrl = getString(R.string.url_live) + "business/add_business";
                android.util.Log.i("add busness", normalUrl);


                JSONObject jObject = new JSONObject();
                jObject.accumulate("name", business_name);
                jObject.accumulate("category", cat_id);
                jObject.accumulate("phone", newUser.getBphn());
                jObject.accumulate("address", newUser.getBaddress());
                jObject.accumulate("suburb", newUser.getBsuburb());
                jObject.accumulate("city", newUser.getBcity());
                jObject.accumulate("postcode", newUser.getBpostcode());
                jObject.accumulate("user_id", newUser.getId());
                jObject.accumulate("user_phone", newUser.getUser_phn());


                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(AddMYBusiness1_act.this, normalUrl, entity, new JsonHttpResponseHandler() {
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
                            if (response.has("message")) {
                                if (response.getString("message").contains("error: Invalid username/password")) {
                                    success = false;
                                } else {
                                    success = true;

                                }

                                newUser = new Gson().fromJson(response.toString(), User.class);

 /* prefs.put(RestoAllergyApplication.thisUser, new Gson().toJson(newUser));
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


                AlertDialog.Builder builder = new AlertDialog.Builder(AddMYBusiness1_act.this);
                builder.setMessage(newUser.getMessage());
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(getApplicationContext(), Business_Listing.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });
                builder.show();


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMYBusiness1_act.this);
                builder.setTitle(" error!");
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


    public class UserSignUp extends AsyncTask<Void, Void, Boolean> {

        private final String mPassword;
        private final String mEmail;
        private final String mFirstname;
        private final String mLastname;
        private final String mpostcode;
        private final String mphn;

        Boolean success = false;
        User newUser = new User();

        String responseString;

        UserSignUp(String password, String email, String firstname, String lastname, String postcode, String phn) {

            mPassword = password;
            mEmail = email;
            mFirstname = firstname;
            mLastname = lastname;
            mpostcode = postcode;
            mphn = phn;
            newUser.setEmailAddress(mEmail);
            newUser.setPassword(mPassword);

            newUser.setFirstname(mFirstname);
            newUser.setLastname(mLastname);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddMYBusiness1_act.this);
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

                String str_url = AddMYBusiness1_act.this.getString(R.string.url_live);
                String normalUrl = str_url + "users/register";

                //  String normalUrl = "https://newonestoptrade.gary.fatweb.co.nz/api/users/register";
                android.util.Log.i("login", normalUrl + "?" + params.toString());


                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", newUser.getEmailAddress());
                jObject.accumulate("password", newUser.getPassword());
                jObject.accumulate("first_name", newUser.getFirstname());
                jObject.accumulate("last_name", newUser.getLastname());
                jObject.accumulate("about_us", newUser.getAbout());
                jObject.accumulate("username", newUser.getEmailAddress());

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(AddMYBusiness1_act.this, normalUrl, entity, new JsonHttpResponseHandler() {
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
                            if (response.has("message")) {
                                if (response.getString("message").contains("error: Invalid username/password")) {
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


                AlertDialog.Builder builder = new AlertDialog.Builder(AddMYBusiness1_act.this);
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

                        //  Toast.makeText(AddMYBusiness1_act.this, "" + username + passwrd + email + firstname, Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("username", username);
                        editor.putString("password", passwrd);
                        editor.putString("email", email);
                        editor.putString("firstname", firstname);
                        editor.putString("id", userid);

                        editor.putString("lastname", lastname);
                        attemptLogin1();
                        editor.apply();
                        // new UserLogin(etUsername.getText().toString(), etPassword.getText().toString()).execute();
                       /* Intent in = new Intent(Register_employee_act.this, Home1_act.class);
                        startActivity(in);*/

                    }
                });
                builder.show();


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMYBusiness1_act.this);
                builder.setTitle(" error!");
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

    private class GetUserDetail extends AsyncTask<String, Void, String> {
        Boolean success;
        String responseString;
        ProgressDialog pd;
        private final String mEmail;
        User newUser = new User();

        GetUserDetail(String email) {
            mEmail = email;

            newUser.setEmailAddress(mEmail);

        }

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(AddMYBusiness1_act.this);
            pd.setMessage("Loading...");
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... v) {

            try {

              /*  RequestParams params = new RequestParams();
                params.put("email",v[0]);*/

                RequestParams params = new RequestParams();
                params.put("email", suser);
//                params.put("access_token", prefs.getString(HSApplication.ACCESS_TOKEN));
//                android.util.Log.i("token", prefs.getString(HSApplication.ACCESS_TOKEN));

                String str_url = AddMYBusiness1_act.this.getString(R.string.url_live);
                String normalUrl = str_url + "users/user_details";

                // String normalUrl = "http://newonestoptrade.gary.fatweb.co.nz/api/users/user_details";
                android.util.Log.i("test", normalUrl);

                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", suser);


                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(AddMYBusiness1_act.this, normalUrl, entity, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        success = false;
                        responseString = response;
                        android.util.Log.d("response", responseString);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        success = false;
                        //                       responseString = errorResponse.toString();
                        android.util.Log.d("response", errorResponse.toString());
                        try {
                            responseString = errorResponse.getString("message").toString();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        android.util.Log.d("response", errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        success = true;

//                        prefs.put(HSApplication.VERSION, newUser.getAndroidVersion());
//                        prefs.put(HSApplication.thisUser, new Gson().toJson(newUser));
                        android.util.Log.i("responseString", responseString);
                        responseString = responseString;

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        android.util.Log.i("JSONObject response", response.toString());
                        try {

                            // responseString = response.getString("message");

                            strname = response.getString("first_name");
                            strlstname = response.getString("last_name");
                            stremail = response.getString("email");
                            Log.e("name", stremail);

                            edt_firstname.setText(strname);
                            edt_lastname.setText(strlstname);
                            edt_email.setText(stremail);
                            edt_email.setEnabled(false);
                            edt_firstname.setEnabled(false);
                            edt_lastname.setEnabled(false);

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

                return responseString;
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  swipeContainer.setRefreshing(false);
            try {
                pd.dismiss();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            edt_firstname.setText(strname);
            edt_lastname.setText(strlstname);
            edt_email.setText(stremail);
//Log.e("Content",s);

        }


    }


}
