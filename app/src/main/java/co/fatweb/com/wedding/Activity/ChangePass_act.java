package co.fatweb.com.wedding.Activity;

import android.app.ProgressDialog;
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

public class ChangePass_act extends AppCompatActivity {

    EditText edt_oldpass, edt_newpass, edt_confrmpass;
    Button btn_chnagepass;
    LinearLayout back;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_act);
        edt_oldpass = findViewById(R.id.edt_oldpass);
        edt_newpass = findViewById(R.id.edt_newpas);
        edt_confrmpass = findViewById(R.id.edt_confrmpass);
        btn_chnagepass = findViewById(R.id.btn_passChange);
        back=findViewById(R.id.back);
        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        user_id = sp.getString("id", null);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile_act  .class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btn_chnagepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }
    private void validate() {

        edt_oldpass = findViewById(R.id.edt_oldpass);
        edt_newpass = findViewById(R.id.edt_newpas);
        edt_confrmpass = findViewById(R.id.edt_confrmpass);

        String oldpass = edt_oldpass.getText().toString().trim();
        String newpass = edt_newpass.getText().toString().trim();
        String confrm_pas = edt_newpass.getText().toString().trim();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(oldpass)) {
            ((EditText) findViewById(R.id.edt_oldpass)).setError("This field is required");
            focusView = edt_oldpass;
            cancel = true;
        }


        if (TextUtils.isEmpty(newpass)) {
            ((EditText) findViewById(R.id.edt_newpas)).setError("This field is required");
            focusView = edt_newpass;
            cancel = true;
        }
        if (TextUtils.isEmpty(confrm_pas)) {
            ((EditText) findViewById(R.id.edt_confrmpass)).setError("This field is required");
            focusView = edt_confrmpass;
            cancel = true;
        }
        if (!newpass.equalsIgnoreCase(confrm_pas)) {
            ((EditText) findViewById(R.id.edt_newpas)).setError("Password does not match!");
            focusView = edt_newpass;
            cancel = true;
        }

        if (!confrm_pas.equalsIgnoreCase(newpass)) {
            ((EditText) findViewById(R.id.edt_confrmpass)).setError("Password does not match!");
            focusView = edt_confrmpass;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            new ResetPassword(user_id,oldpass, newpass).execute();

        }
    }
    private class ResetPassword extends AsyncTask<String, Void, String> {
        Boolean success;
        String responseString;
        ProgressDialog pd;
        private final String mold;
        private final String mnew;
        private final String mid;
        User newUser = new User();

        ResetPassword(String userid,String old,String newpas) {
            mold = old;
            mnew = newpas;
            mid = userid;

            newUser.setOld_pas(mold);
            newUser.setNew_pas(mnew);
            newUser.setId_pas(mid);

        }

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(ChangePass_act.this);
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
                params.put("ID", newUser.getId_pas());
                params.put("old_password", newUser.getOld_pas());
                params.put("new_password", newUser.getNew_pas());
//                params.put("access_token", prefs.getString(HSApplication.ACCESS_TOKEN));
//                android.util.Log.i("token", prefs.getString(HSApplication.ACCESS_TOKEN));
              String str_url=  ChangePass_act.this.getString(R.string.url_live);
                String normalUrl = str_url+"Users/change_password";
                android.util.Log.i("test", normalUrl);

                JSONObject jObject = new JSONObject();
                jObject.accumulate("ID", newUser.getId_pas());
                jObject.accumulate("old_password", newUser.getOld_pas());
                jObject.accumulate("new_password", newUser.getNew_pas());

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(ChangePass_act.this, normalUrl, entity, new JsonHttpResponseHandler() {
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
            new MaterialDialog.Builder(ChangePass_act.this)
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
