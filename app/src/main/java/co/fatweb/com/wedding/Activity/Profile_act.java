package co.fatweb.com.wedding.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

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
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_act extends AppCompatActivity {
    Button btn_pass;
    LinearLayout back;
    TextView tv_uploadimg;
    CircleImageView ivPhoto;
    Context context;
    String id, vehicle_id, suser, spass, address_id, strname, strlstname, str_web, str_about, str_email, name;
    FrameLayout refreshlayout;
    TextView tvindicator, txt_addres;
    EditText edt_name, edt_email, edt_phn, edt_lastname, edt_website, edt_about;
    Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_act);
        back = (LinearLayout) findViewById(R.id.back);
        edt_phn = findViewById(R.id.edt_phn);
        edt_name = findViewById(R.id.edt_firstname);
        edt_email = findViewById(R.id.edt_email);
        edt_lastname = findViewById(R.id.edt_lastame);
        edt_website = findViewById(R.id.edt_web);
        edt_about = findViewById(R.id.edt_about);
        btn_update = findViewById(R.id.btnUpdate);
        refreshlayout = findViewById(R.id.refreshOverlay);
        tvindicator = findViewById(R.id.tvIndicator);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Navigation_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btn_pass = findViewById(R.id.btn_pass);
        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChangePass_act.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        id = sp.getString("id", null);
        new GetProfileDetail().execute();
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    /*  if (str_farm.equals("")) {
                    Toast.makeText(context, "Please Enter your Farm Name", Toast.LENGTH_SHORT).show();
                } else {*/
                new UpdateUser().execute();
                /*}*/
            }
        });

    }

    class GetProfileDetail extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //   Toast.makeText(getApplicationContext(), "sas" + s, Toast.LENGTH_SHORT).show();
            Log.d("RESPONSE", s);

            try {
                JSONObject emp = new JSONObject(s);
                strname = emp.getString("first_name");
                strlstname = emp.getString("last_name");
                String stremail = emp.getString("email");
                str_about = emp.getString("about_us");

                String contact = emp.getString("phone_no");


                edt_name.setText(strname);
                edt_email.setText(stremail);
                edt_lastname.setText(strlstname);
                edt_phn.setText(contact);
                edt_about.setText(str_about);
                edt_email.setEnabled(false);



                SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("username", strname + " " + strlstname);


                editor.apply();


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
                String str_url = Profile_act.this.getString(R.string.url_live);
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

    public class UpdateUser extends AsyncTask<Void, Void, Boolean> {

        Boolean success = false;


        String responseString;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshlayout.setVisibility(View.VISIBLE);
            tvindicator.setText("Updating...");
            //refreshOverlay.setVisibility(View.VISIBLE);

        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {
               // https://wedding.geek.fatweb.co.nz/api/users/update_profile

                String normalUrl = getString(R.string.url_live) + "/users/update_profile";
                Log.e("Userdetail", normalUrl);


                JSONObject jObject = new JSONObject();
                jObject.accumulate("user_id", id);
                jObject.accumulate("first_name", edt_name.getText().toString());
                jObject.accumulate("last_name", edt_lastname.getText().toString());
                jObject.accumulate("nickname", "");

                jObject.accumulate("about_us", edt_about.getText().toString());
               // jObject.accumulate("phone", edt_phn.getText().toString());


                StringEntity entity = new StringEntity(jObject.toString());
                Log.e("test", jObject.toString());

                RestClient.post(Profile_act.this, normalUrl, entity, new JsonHttpResponseHandler() {


                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        responseString = response;
                        Log.d("String response error", responseString.toString());
                        if (responseString.replace("\"", "").equalsIgnoreCase("success")) {
                            success = true;
                        } else success = false;
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        responseString = errorResponse.toString();
                        if (responseString.equalsIgnoreCase("success")) {
                            success = true;
                        } else success = false;
                        Log.d("JSONObject response error", errorResponse.toString());
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("JSONArray response error", errorResponse.toString());
                        responseString = errorResponse.toString();
                        if (responseString.equalsIgnoreCase("success")) {
                            success = true;
                        } else success = false;

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
                            if (response.has("message")) {
                                if (response.getString("message").contains("fail")) {
                                    success = false;
                                } else {
                                    success = true;

                                    String firstname = edt_name.getText().toString();
                                    String phn = edt_phn.getText().toString();
                                    String id1 = id;

                                    SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();

                                    editor.putString("firstname", firstname);
                                    editor.putString("id", id1);
                                    editor.putString("username", phn);
                                    editor.apply();

//                                    updatedUser = new Gson().fromJson(response.toString(), User.class);
//                                    thisUser.setFirstName(updatedUser.getFirstName());
//                                    thisUser.setLastName(updatedUser.getLastName());
//                                    thisUser.setEmailAddress(updatedUser.getEmailAddress());
//                                    prefs.put(RestoAllergyApplication.thisUser, new Gson().toJson(thisUser));
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
            refreshlayout.setVisibility(View.GONE);
            //refreshOverlay.setVisibility(View.GONE);
            if (success) {
                // EventBus.getDefault().post(new Main2Activity.RefreshActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile_act.this);
                //builder.setTitle("Success!");
                builder.setMessage("Profile Updated!");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.show();


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile_act.this);
                builder.setTitle("error!");
                // builder.setMessage("error");
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
