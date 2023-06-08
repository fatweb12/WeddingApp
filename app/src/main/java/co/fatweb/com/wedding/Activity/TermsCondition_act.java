package co.fatweb.com.wedding.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import co.fatweb.com.wedding.R;

public class TermsCondition_act extends AppCompatActivity {


    FrameLayout refreshlayout;
    TextView tvindicator, txt_addres,txt_heading,txt_description;
    String str_heading,str_description;
    LinearLayout back;
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition_act);
        refreshlayout = findViewById(R.id.refreshOverlay);
        tvindicator = findViewById(R.id.tvIndicator);
        txt_heading=findViewById(R.id.txt_heading);
        txt_description=findViewById(R.id.txt_descr);
        webview = findViewById(R.id.webview);
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Navigation_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        new GetProfileDetail().execute();
        webview.setBackgroundColor(Color.TRANSPARENT);


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
                // str_heading = emp.getString("heading");
                str_description = emp.getString("text");

                // txt_heading.setText(str_heading);
                // txt_description.setText(str_description);

                webview = findViewById(R.id.webview);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.loadDataWithBaseURL("", emp.getString("text"), "text/html", "UTF-8", "");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //in this method we are fetching the json string
        @Override
        protected String doInBackground(Void... voids) {


            try {
                //creating a URL
                String str_url=  TermsCondition_act.this.getString(R.string.url_live);
                String normalUrl = str_url+"common/term_and_candition";

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

}
