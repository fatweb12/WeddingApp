package co.fatweb.com.wedding.Activity;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import co.fatweb.com.wedding.Connectivity.RestClient;
import co.fatweb.com.wedding.DataObject.User;
import co.fatweb.com.wedding.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ForgotPasswrd_act extends AppCompatActivity {
    Button btn_getnewpass;


    LinearLayout back;

    EditText edt_mail;
    boolean valid = false;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwrd_act);
        edt_mail = findViewById(R.id.edt_email);
        btn_getnewpass = findViewById(R.id.btn_getnewpass);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_getnewpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();

            }
        });

    }

    private void validate() {

        if (TextUtils.isEmpty(edt_mail.getText().toString())) {
            ((EditText) findViewById(R.id.edt_email)).setError("This field is required!");
            valid = false;
        } else valid = true;

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(edt_mail.getText().toString()).matches()) {
            valid = true;
        } else {
            ((EditText) findViewById(R.id.edt_email)).setError("Invalid e-mail address!");
            valid = false;
        }

        if (valid) {
            String email = edt_mail.getText().toString();
            new ResetPassword(email).execute();
        }
    }

    private class ResetPassword extends AsyncTask<String, Void, String> {
        Boolean success;
        String responseString;
        ProgressDialog pd;
        private final String mEmail;
        User newUser = new User();

        ResetPassword(String email) {
            mEmail = email;

            newUser.setEmailAddress(mEmail);

        }

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(ForgotPasswrd_act.this);
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
                params.put("email", newUser.getEmailAddress());
//                params.put("access_token", prefs.getString(HSApplication.ACCESS_TOKEN));
//                android.util.Log.i("token", prefs.getString(HSApplication.ACCESS_TOKEN));
                String str_url=  ForgotPasswrd_act.this.getString(R.string.url_live);
                String normalUrl = str_url+"users/forget";

              //  String normalUrl = "http://onestoptrade.co.nz/api/users/forget";
                android.util.Log.i("test", normalUrl);

                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", newUser.getEmailAddress());


                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(context, normalUrl, entity, new JsonHttpResponseHandler() {
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

                            responseString = response.getString("message");

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
            new MaterialDialog.Builder(ForgotPasswrd_act.this)
                    .content(s)
                    .positiveText("OK")
                    .positiveColorRes(R.color.colorAccent)

                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                            dialog.dismiss();
                        }
                    })
                    .show();


        }


    }

}


