package co.fatweb.com.wedding.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import co.fatweb.com.wedding.Connectivity.RestClient;
import co.fatweb.com.wedding.DataObject.RatingReview;
import co.fatweb.com.wedding.DataObject.User;
import co.fatweb.com.wedding.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Business_desc extends AppCompatActivity {

    String businessid, str_phnnum, empname, userid, str_businessid, str_commant, txtrate, strname, strlstname, stremail,buisness_userid;
    ProgressDialog pd;
    TextView txt_signin, txt_signout, tvindicator;
    ImageView img_comapny;
    Button tvwebsitelink, tv_call, btn_email;
    AppCompatButton btnback;
    FrameLayout refreshlayout;
    RecyclerView rvcomments;
    private List<RatingReview> feedList = new ArrayList<RatingReview>();
    CategoryListing catadapter;
    EditText edt_comment;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private AppCompatButton btnSubmit;
    User newUser;
LinearLayout lin_rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_desc);
        Intent i = getIntent();
       // txt_signin = findViewById(R.id.txt_signin);
        tvwebsitelink = findViewById(R.id.btn_website);
      //  txt_signout = findViewById(R.id.txt_signOut);
        tv_call = findViewById(R.id.btn_call);
        btnback = findViewById(R.id.btnhome);
        rvcomments = findViewById(R.id.rvFeedback);
        btn_email = findViewById(R.id.btn_email);
        edt_comment = findViewById(R.id.edt_conCommant);
        refreshlayout = findViewById(R.id.refreshOverlay);
        tvindicator = findViewById(R.id.tvIndicator);
        img_comapny = findViewById(R.id.comapnylogo);
        lin_rating=findViewById(R.id.lin_rating);
        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
        userid = sp.getString("id", null);
        SharedPreferences.Editor editor = sp.edit();
        rvcomments.setLayoutManager(new LinearLayoutManager(Business_desc.this));
        businessid = i.getStringExtra("b_id");

        new GetUserDetail(userid).execute();

        addListenerOnRatingBar();
        addListenerOnButton();
        //  Toast.makeText(Business_desc.this, "" + businessid, Toast.LENGTH_SHORT).show();
        new GetDescriptio().execute();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                /*Intent intent = new Intent(getApplicationContext(), Business_act_retro.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            }
        });

        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Business_desc.this, "Call"+str_phnnum, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", str_phnnum, null));
                startActivity(intent);
            }
        });
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Business_desc.this, "Call"+str_phnnum, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder bb = new AlertDialog.Builder(Business_desc.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.item_email, null);
                bb.setView(dialogView);


                final Button btn_positive = (Button) dialogView.findViewById(R.id.btn_submitemail);
                final Button btn_negative = (Button) dialogView.findViewById(R.id.btn_cancel);
                final EditText name = dialogView.findViewById(R.id.edt_conNAme);
                final EditText email = dialogView.findViewById(R.id.edt_conEmail);
                final EditText message = dialogView.findViewById(R.id.edt_conCommant);

                if (userid==null) {
                    name.setHint("Your Name");
                    email.setHint("Your Email");
                }
                else {
                    new GetUserDetail(userid).execute();
                    name.setText(strname + " " + strlstname);
                    email.setText(stremail);
                    name.setEnabled(false);
                    email.setEnabled(false);

                }


                final AlertDialog dialog = bb.create();
                /* title.setText("Type Your Allergy");*/

                btn_positive.setText("Submit");

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strmessage=message.getText().toString();
                        String stremail=email.getText().toString();

                        if(strmessage.equals("")){
                            Toast.makeText(Business_desc.this, "Message cann't be empty", Toast.LENGTH_SHORT).show();
                        } else if(stremail.equals("")){
                            Toast.makeText(Business_desc.this, "Email can't sent without emailid", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            new SendMessage(name.getText().toString(),email.getText().toString(),message.getText().toString(),userid,businessid).execute();

                            dialog.dismiss();
                        }}
                });

                btn_negative.setText("Cancel");

                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });


    }
    public class SendMessage extends AsyncTask<Void, Void, Boolean> {

        String email;
        String name;

        String message;
        String userid;
        String businessid;
        Boolean success = false;
        ProgressDialog pd;

        String responseString;
        SendMessage(String email, String name,String message,String userid,String businessid) {
            this.email = email;
            this.name = name;
            this.userid = userid;
            this.message = message;
            this.businessid = businessid;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Business_desc.this);
            pd.setMessage("Sending...");
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();
                String str_url=  Business_desc.this.getString(R.string.url_live);
                String normalUrl = str_url+"business/email_on_business";

              //  String normalUrl = "https://onestoptrade.co.nz/api/business/email_on_business";


                JSONObject jObject = new JSONObject();
                jObject.accumulate("user", email);
                jObject.accumulate("email", name);
                jObject.accumulate("query", message);
                jObject.accumulate("business_id",businessid);
                jObject.accumulate("user_id",userid);



                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test",jObject.toString());

                RestClient.post(Business_desc.this,normalUrl, entity, new JsonHttpResponseHandler() {


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        responseString = response;
                        Log.d("response",responseString.toString());
                        if(responseString.contains("success")){
                            success = true;
                        }else success = false;
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        responseString = errorResponse.toString();
                        if(responseString.contains("success")){
                            success = true;
                        }else success = false;
                        Log.d("response",errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode,Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("response",errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                        if(responseString.contains("success")){
                            success = true;
                        }else success = false;
                        Log.d("response",responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        Log.i("responseString",responseString);
                        success = true;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i("JSONObject response",response.toString());
                        try{
                            //success = true;
                            if(response.has("message")){
                                if(response.getString("message").contains("error")){
                                    success=false;
                                    responseString = response.getString("message");
                                }else{
                                    success =true;
                                }
                            }else{
                                success = false;
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Log.i("JSONArray response",response.toString());
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
          /*  etSubject.setText("");
            etMessage.setText("");*/
            if (success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Business_desc.this);
                builder.setTitle("Message sent!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.show();



            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Business_desc.this);
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

            pd = new ProgressDialog(Business_desc.this);
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
                params.put("email", userid);
//                params.put("access_token", prefs.getString(HSApplication.ACCESS_TOKEN));
//                android.util.Log.i("token", prefs.getString(HSApplication.ACCESS_TOKEN));

                String str_url=  Business_desc.this.getString(R.string.url_live);
                String normalUrl = str_url+"users/user_details";

              //  String normalUrl = "https://onestoptrade.co.nz/api/users/user_details";
                android.util.Log.i("test", normalUrl);

                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", userid);


                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(Business_desc.this, normalUrl, entity, new JsonHttpResponseHandler() {
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

                           /* edt_firstname.setText(strname);
                            edt_lastname.setText(strlstname);
                            edt_email.setText(stremail);
*/

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
           /* edt_firstname.setText(strname);
            edt_lastname.setText(strlstname);
            edt_email.setText(stremail);*/
//Log.e("Content",s);

        }


    }


    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.rbRating);
        txtRatingValue = (TextView) findViewById(R.id.rating_id);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.rbRating);
        btnSubmit = findViewById(R.id.btnsubmitcomment);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                str_commant = edt_comment.getText().toString();
                txtrate = txtRatingValue.getText().toString();

               /* Toast.makeText(Business_desc.this,
                        String.valueOf(ratingBar.getRating()) + userid,
                        Toast.LENGTH_SHORT).show();*/
                if (userid == null) {


                    new MaterialDialog.Builder(Business_desc.this)
                            .content("You have to sign in first to Review")
                            .positiveText("Sign In")
                            .negativeText("Cancel")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(Business_desc.this, SignIn_act.class);
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
                else if (txtrate.equals("")){
                    Toast.makeText(Business_desc.this, "Please Select Rating ", Toast.LENGTH_SHORT).show();
                }
                else if(str_commant.equals("")){
                    Toast.makeText(Business_desc.this, "Review can't be empty", Toast.LENGTH_SHORT).show();

                }
                else {
                    new AddComment(str_businessid, userid, str_commant, txtrate).execute();
                }

            }

        });
    }

    private class AddComment extends AsyncTask<String, Void, String> {
        Boolean success;
        String responseString;
        ProgressDialog pd;
        String mbusinessid;
        String muserid;
        String mcomment;
        String mrating;

        User newUser = new User();

        AddComment(String businessid, String userid, String coment, String rateing) {
            mbusinessid = businessid;
            muserid = userid;
            mcomment = coment;
            mrating = rateing;

            // newUser.setEmailAddress(mEmail);

        }

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(Business_desc.this);
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
                params.put("business_id", str_businessid);
                params.put("user_id", userid);
                params.put("comment", str_commant);
                params.put("rating", txtrate);
//                params.put("access_token", prefs.getString(HSApplication.ACCESS_TOKEN));
//                android.util.Log.i("token", prefs.getString(HSApplication.ACCESS_TOKEN));

                String str_url=  Business_desc.this.getString(R.string.url_live);
                String normalUrl = str_url+"business/add_rating";

                //String normalUrl = "http://onestoptrade.co.nz/api/business/add_rating";
                android.util.Log.i("test", normalUrl);

                JSONObject jObject = new JSONObject();
                jObject.accumulate("business_id", str_businessid);

                jObject.accumulate("user_id", userid);
                jObject.accumulate("comment", str_commant);
                jObject.accumulate("rating", txtrate);

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(Business_desc.this, normalUrl, entity, new JsonHttpResponseHandler() {
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
                            //JSONObject emp = (new JSONObject(response.toString())).getJSONObject("saveJobResult");
                            //String strjob = emp.getString("message");
                            responseString = response.getString("message");
                            // JSONObject emp = (new JSONObject(response.toString())).getJSONObject("message");
                            empname = response.getString("message");


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
            pd.dismiss();
            //  swipeContainer.setRefreshing(false);
            try {

                String strjob = newUser.getMessage();
                //  Toast.makeText(Business_desc.this, "" + s + strjob, Toast.LENGTH_SHORT).show();
                edt_comment.setText("");
                ratingBar.setRating(0F);
                /* pd.dismiss();*/

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            new MaterialDialog.Builder(Business_desc.this)
                    // .content(s)
                    .positiveText("OK")
                    .positiveColorRes(R.color.colorAccent)
                    .title(empname)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            new GetDescriptio().execute();
                          /*  Intent i = new Intent(Business_desc.this, Business_act.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);*/

                        }
                    })
                    .show();


        }


    }

    private void call() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(str_phnnum));
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            //Log.e("helloandroid dialing example", "Call failed", e);
        }
    }

    class GetDescriptio extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            refreshlayout.setVisibility(View.VISIBLE);

            tvindicator.setText("Please Wait");
          /*  pd = new ProgressDialog(Business_desc.this);
            pd.setMessage("Loading...");
            pd.show();*/
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            refreshlayout.setVisibility(View.GONE);
            feedList.clear();
            //  Toast.makeText(getApplicationContext(), "sas" + s, Toast.LENGTH_SHORT).show();
            Log.d("RESPONSE", String.valueOf(s));
            try {
                JSONObject emp = new JSONObject(s);
                String str_content = emp.getString("content");
                String str_adderss = emp.getString("street_addresses");
                String str_region = emp.getString("region");
                String str_postcode = emp.getString("postcodes");
                String str_phn = emp.getString("phone_number");
                String str_mobile = emp.getString("mobile_number");
                String str_website = emp.getString("website_url");
                str_businessid = emp.getString("business_id");
                String str_suburb = emp.getString("suburb1");
                String img_url = emp.getString("Img_url");
                String overallrating = emp.getString("overall_rating");
                String overallrating_count = emp.getString("rating_users_count");
                 buisness_userid = emp.getString("creator_id");
                Log.d("IDS",userid+"\n"+buisness_userid);
                if (buisness_userid.equals(userid)){
                    lin_rating.setVisibility(View.GONE);
                }
                else {

                }
                TextView txt_overall = findViewById(R.id.txt_overall);
                AppCompatRatingBar rbResto_overal = findViewById(R.id.rbResto_overall);
                try {
                    rbResto_overal.setRating(overallrating != null ? Float.valueOf(overallrating) : 0);
                    txt_overall.setText("Overall Rating : " + overallrating_count);
                    //  postViewHolder.tvRatingid.setText(item.reviews.reviews1);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                String strtitle = emp.getString("post_title");
                Glide.with(Business_desc.this).load(img_url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.ic_account).dontAnimate().timeout(60000)).into(img_comapny);

                //String strimg = emp.getString("CompanyLOGO");
                TextView txt_jobid = findViewById(R.id.txt_jobid);
                TextView txt_jobloca = findViewById(R.id.txt_loc);
                TextView txt_phn = findViewById(R.id.txt_phn);


                final TextView txt_website = findViewById(R.id.txt_website);
                TextView txt_jobdescp = findViewById(R.id.job_desc);
                TextView txt_name = findViewById(R.id.txt_name);
                TextView txt_suburb = findViewById(R.id.txt_suburb);

                txt_name.setText(strtitle);
                txt_jobid.setText(str_businessid);
                txt_jobloca.setText(str_adderss + ", " + str_suburb + ", " + str_region + ", " + str_postcode);
                txt_phn.setText(str_phn);
                txt_website.setText(str_website);

                txt_jobdescp.setText(str_content);
                str_phnnum = txt_phn.getText().toString();
                tvwebsitelink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = txt_website.getText().toString();
                        try {
                            if (url.equals("")) {
                                Toast.makeText(Business_desc.this, "No Website", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                                i.setData(Uri.parse(url));

                                startActivity(i);
                            }
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(Business_desc.this, "Url Incorrect", Toast.LENGTH_SHORT).show();
                            Log.d("error", e.getMessage());
                        }

                       /* Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);*/
                    }
                });
                JSONArray jsonArray1 = emp.getJSONArray("comments");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject object = jsonArray1.getJSONObject(i);
                    RatingReview newFeed = new Gson().fromJson(object.toString(), RatingReview.class);

                    feedList.add(newFeed);

                    if (feedList.size() > 0) {
                        TextView tvdata = findViewById(R.id.tvNoData);
                        tvdata.setText("User Reviews");
                    } else {

                    }

                    catadapter = new CategoryListing(feedList, Business_desc.this);
                    catadapter.notifyDataSetChanged();
                    rvcomments = findViewById(R.id.rvFeedback);


//                    rvcomments.setHasFixedSize(true);
                    // str_jobod1 = newFeed.getCvid();
                    rvcomments.setAdapter(catadapter);

                    Log.d("test", newFeed.getComment_author() + " " + newFeed.getCommentid() + " "/* + newFeed.getCat_image()*/);

                    // Glide.with(JobDescription.this).load(strimg).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.ic_account).dontAnimate().override(600, 200).timeout(60000)).into(ivImage);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //in this method we are fetching the json string
        @Override
        protected String doInBackground(Void... voids) {


            try {
                //creating a URL

                String str_url=  Business_desc.this.getString(R.string.url_live);
                String normalUrl = str_url+"business/business_details_by_id/" + businessid;

                URL url = new URL(normalUrl);
                Log.d("JOBDESCRIPTION", String.valueOf(url));
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


    public class CategoryListing extends RecyclerView.Adapter<CategoryListing.PostViewHolder> {

        private List<RatingReview> list;
        Context context;
        String jobid;
        User user;

        public CategoryListing(List<RatingReview> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(final CategoryListing.PostViewHolder postViewHolder, final int position) {

            final RatingReview item = list.get(position);
            postViewHolder.tvName.setText(item.getComment_author());
            String str1 = postViewHolder.tvName.getText().toString();


            final String catname = postViewHolder.tvName.getText().toString();
            postViewHolder.tvratingid.setText(item.getCommentid());
            try {
                postViewHolder.rbResto.setRating(item.getRatingcounnt() != null ? Float.valueOf(item.getRatingcounnt()) : 0);
                postViewHolder.tvDetails.setText(item.getCommentcontent());
                //  postViewHolder.tvRatingid.setText(item.reviews.reviews1);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

          /*  postViewHolder.tvname1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(Navigation_activity.this, "" + selectedtext_str, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), Business_act.class);
                    in.putExtra("cat_name", catname);
                    in.putExtra("suburb", "Manukau");
                    startActivity(in);
                }
            });
            postViewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(Navigation_activity.this, "" + selectedtext_str, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), Business_act.class);
                    in.putExtra("cat_name", catname);
                    in.putExtra("suburb", "Manukau");
                    startActivity(in);
                }
            });*/
            //  Glide.with(context).load(item.getCat_image()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.ic_account).dontAnimate().timeout(60000)).into(postViewHolder.ivImage);


        }

        @Override
        public CategoryListing.PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_reviews, viewGroup, false);


            return new CategoryListing.PostViewHolder(itemView);
        }


        public class PostViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvName, tvDetails, tvname1, tvratingid, tv_dateposted, tv_status;
            protected ImageView cvLayout;

            protected ImageView ivImage;
            Button btn_viewCVdetail, btn_editCV, btn_cvActive, btn_cvDeActive;
            protected AppCompatRatingBar rbResto;

            public PostViewHolder(View v) {
                super(v);
                tvName = (TextView) v.findViewById(R.id.tvUser);
                rbResto = v.findViewById(R.id.rbResto);
                tvratingid = (TextView) v.findViewById(R.id.tvratingid);
                tvDetails = (TextView) v.findViewById(R.id.tvDetails);
                ivImage = v.findViewById(R.id.ivImage);

            }
        }

    }

}
