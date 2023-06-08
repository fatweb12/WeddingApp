package co.fatweb.com.wedding.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import co.fatweb.com.wedding.Connectivity.RestClient;
import co.fatweb.com.wedding.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Conatct_us extends AppCompatActivity {

    LinearLayout back;

    Button submit;
    EditText etName, etEmail, etSubject, etMessage;

    FrameLayout refreshlayout;
    TextView tvindicator;
    String strname, strlstname, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conatct_us);
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit = findViewById(R.id.btnSubmit);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etSubject = findViewById(R.id.etSubject);
        etMessage = findViewById(R.id.etMessage);
        refreshlayout = findViewById(R.id.refreshOverlay);
        tvindicator = findViewById(R.id.tvIndicator);
        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        id = sp.getString("id", null);
        new GetProfileDetail().execute();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {

                    //new SendMessage(edt_email.getText().toString(), edt_name.getText().toString(), edt_comment.getText().toString()).execute();
                    new SendMessage(etName.getText().toString(), etEmail.getText().toString(), etSubject.getText().toString(), etMessage.getText().toString()).execute();
                    //Toast.makeText(context, "Captcha  !", Toast.LENGTH_SHORT).show();


                } else {

                }
            }
        });

    }

    boolean validate() {
        boolean valid = true;

        if (etName.getText().toString().isEmpty()) {
            ((EditText) Conatct_us.this.findViewById(R.id.etName)).setError("This field is required");
            valid = false;
        }


        if (etEmail.getText().toString().isEmpty()) {
            ((EditText) Conatct_us.this.findViewById(R.id.etEmail)).setError("This field is required");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            ((EditText) Conatct_us.this.findViewById(R.id.etEmail)).setError("Invalid email address");
            valid = false;
        }


        if (etSubject.getText().toString().isEmpty()) {
            ((EditText) Conatct_us.this.findViewById(R.id.etSubject)).setError("This field is required");
            valid = false;
        }


        if (etMessage.getText().toString().isEmpty()) {
            ((EditText) Conatct_us.this.findViewById(R.id.etMessage)).setError("This field is required");
            valid = false;
        }


        return valid;


    }

    class GetProfileDetail extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            refreshlayout.setVisibility(View.VISIBLE);

            tvindicator.setText("Please Wait");
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            refreshlayout.setVisibility(View.GONE);
            //   Toast.makeText(getApplicationContext(), "sas" + s, Toast.LENGTH_SHORT).show();
            Log.d("RESPONSE", s);
            try {
                JSONObject emp = new JSONObject(s);
                strname = emp.getString("first_name");
                strlstname = emp.getString("last_name");
                String stremail = emp.getString("email");


                etName.setText(strname );
                etEmail.setText(stremail);
                etEmail.setEnabled(false);

                Log.d("USR DETAIL", strlstname + " " + strname + " " + stremail);
                //   Glide.with(Profile_act.this).load(image).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.ic_account).dontAnimate().timeout(60000)).into(ivPhoto);

                //  String str = "Job Count:" + strjob + "\n" + "Cv Count:" + strcv;
                //   Toast.makeText(Pr.this, "asa" + str, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //in this method we are fetching the json string
        @Override
        protected String doInBackground(Void... voids) {


            try {
                //creating a URL
                String str_url = Conatct_us.this.getString(R.string.url_live);
                String normalUrl = str_url + "users/user_details_by_id/" + id;

                URL url = new URL(normalUrl);
                Log.d("GEt Detail url", String.valueOf(url));
                //Opening the URL using HttpURLConnection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                //StringBuilder object to read the string from the service
                StringBuilder sb = new StringBuilder();

                //We will use a buffered reader to read the string from service
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                //A simple string to read values from each line
                String json;

                //reading until we don't find null
                while ((json = bufferedReader.readLine()) != null) {

                    //appending it to string builder
                    sb.append(json + "\n");
                }

                //finally returning the read string
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }

        }
    }

    public class SendMessage extends AsyncTask<Void, Void, Boolean> {
        String name;
        String email;

        String subject;
        String message;
        Boolean success = false;
        ProgressDialog pd;

        String responseString;

        SendMessage(String name, String email, String subject, String message) {
            this.name = name;
            this.email = email;

            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Conatct_us.this);
            pd.setMessage("Sending...");
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();


                String normalUrl = Conatct_us.this.getString(R.string.url_live) + "users/contact_us";


                JSONObject jObject = new JSONObject();
                jObject.accumulate("name", name);
                jObject.accumulate("email", email);

                jObject.accumulate("subject", subject);
                jObject.accumulate("message", message);



                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(Conatct_us.this, normalUrl, entity, new JsonHttpResponseHandler() {


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        responseString = response;
                        Log.d("response", responseString.toString());
                        if (responseString.contains("success")) {
                            success = true;
                        } else success = false;
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        responseString = errorResponse.toString();
                        if (responseString.contains("success")) {
                            success = true;
                        } else success = false;
                        Log.d("response", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("response", errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                        if (responseString.contains("success")) {
                            success = true;
                        } else success = false;
                        Log.d("response", responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        Log.i("responseString", responseString);
                        success = true;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i("JSONObject response", response.toString());
                        try {
                            //success = true;
                            if (response.has("status")) {
                                if (response.getString("status").contains("fail")) {
                                    success = false;
                                    responseString = response.getString("status");
                                } else {
                                    success = true;
                                }
                            } else {
                                success = false;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Log.i("JSONArray response", response.toString());
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
            pd.dismiss();
            etSubject.setText("");
            etMessage.setText("");
            if (success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Conatct_us.this);
                builder.setTitle("Message sent!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.show();


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Conatct_us.this);
                builder.setTitle("Sending failed!");
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

