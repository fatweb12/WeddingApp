package co.fatweb.com.wedding.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import co.fatweb.com.wedding.DataObject.Cat_Listing;
import co.fatweb.com.wedding.DataObject.User;
import co.fatweb.com.wedding.R;

public class Business_Listing extends AppCompatActivity {


    TextView tvindicator, txtheading;
    Intent in;

    ProgressDialog dialog;
    private LinearLayoutManager mLayoutManager;

    CatlistingAdapter catadapter;
    RecyclerView rvFeed;
    String  id;
    FrameLayout refreshlayout;
    LinearLayout back;

    private List<Cat_Listing> feedList = new ArrayList<>();
    private List<Cat_Listing> feedList1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__listing);
        refreshlayout = findViewById(R.id.refreshOverlay);
        tvindicator = findViewById(R.id.tvIndicator);
        rvFeed = findViewById(R.id.rvFeed);
        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String suser = sp.getString("email", null);
        String spass = sp.getString("password", null);
        id = sp.getString("id", null);

        txtheading = findViewById(R.id.txtheadimg);
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        rvFeed.setLayoutManager(mLayoutManager);


       // Log.d("CATNAME######", catname + catheading);
       // txtheading.setText(catheading);

        // Toast.makeText(Business_act.this, "" + catname + suburb, Toast.LENGTH_SHORT).show();
        new GetListing().execute();

        rvFeed.setHasFixedSize(true);


    }

    class GetListing extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshlayout.setVisibility(View.VISIBLE);

            tvindicator.setText("Please Wait");
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            refreshlayout.setVisibility(View.GONE);
            feedList.clear();
            //co.clear();

            try {
                // android.util.Log.i("response", s);

                if (!s.isEmpty()) {
                    // Do you work here on success

                    JSONObject object1=new JSONObject(s);
                   JSONArray jsonArray = object1.getJSONArray("data");


                   // JSONArray jsonArray = new JSONArray(s);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);


                            Cat_Listing newFeed= new Gson().fromJson(object.toString(), Cat_Listing.class);


                          //  feedList.add(newFeed);
                            feedList1.add(newFeed);


                          //str_driverRatimg = object.getString("status");


                           // catadapter = new CatlistingAdapter(feedList, Business_Listing.this);
                            catadapter = new CatlistingAdapter(feedList1, Business_Listing.this);
                            catadapter.notifyDataSetChanged();
                            rvFeed = findViewById(R.id.rvFeed);

                            rvFeed.setHasFixedSize(true);
                            // str_jobod1 = newFeed.getCvid();
                            rvFeed.setAdapter(catadapter);

                            Log.d("test", feedList + newFeed.getName() + " " +newFeed.getAdress()+"\n"+newFeed.getPosttitle());
                            Log.d("test11", s);
                        }
                    } else {
                       // txt_nobookimg.setVisibility(View.VISIBLE);
                        Toast.makeText(Business_Listing.this, "No Data", Toast.LENGTH_SHORT).show();

                        //  Toast.makeText(RideHistory_act.this, "No Detail Found", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(Business_Listing.this, "No Data", Toast.LENGTH_SHORT).show();
                    Log.d("test11", s + feedList);
                    //txt_nobookimg.setVisibility(View.VISIBLE);
                    // Toast.makeText(RideHistory_act.this, "No Detail Found", Toast.LENGTH_SHORT).show();
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
                // URL url = new URL("https://dev.myjobspace.co.nz/webservice/job.php/userJobAlertListing/" + id);

                URL url = new URL("https://ido.kiwi/api/business/business_list_by_user_id/" + id);
                Log.d("URl_cat", String.valueOf(url));
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
                return "";
            }

        }
    }


/*
    class GeteDetail extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null){
                // Do you work here on success
                Toast.makeText(Business_Listing.this, "DATA", Toast.LENGTH_SHORT).show();
            //   Toast.makeText(getApplicationContext(), "sas" + s, Toast.LENGTH_SHORT).show();

            try {

                Log.d("RESPONSE", s);
                JSONObject emp = new JSONObject(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            }else{
                Toast.makeText(Business_Listing.this, "EMPTY", Toast.LENGTH_SHORT).show();

                // null response or Exception occur
            }
        }

        //in this method we are fetching the json string
        @Override
        protected String doInBackground(Void... voids) {


            try {
                //creating a URL
                String str_url = Business_Listing.this.getString(R.string.url_live);
                String normalUrl = str_url + "business/business_list_by_user_id/" + id;

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
*/


    public class CatlistingAdapter extends RecyclerView.Adapter<CatlistingAdapter.PostViewHolder> {

        private List<Cat_Listing> list;
        Context context;

        User user;

        public CatlistingAdapter(List<Cat_Listing> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(final CatlistingAdapter.PostViewHolder postViewHolder, final int position) {

            final Cat_Listing item = list.get(position);
            postViewHolder.tvName.setText("Business Name : "+item.getPosttitle());

           // postViewHolder.tvt.setText(item.getName());
/*
            postViewHolder.tvwebsite.setText(item.getWebsiteurl());
            postViewHolder.tvphn.setText(item.getPhone());*/

          //  String phn = postViewHolder.tvphn.getText().toString();

            postViewHolder.tv_id.setText(item.getBusinessid1());
         //   postViewHolder.tvmobile.setText(item.getMobilenum());
            String streetadres = item.getAdress();
            String suburb = item.getSuburb();
            String region = item.getRegion();
            String postcode = item.getPostcode();
            String cat_name = item.getCat_name();
            postViewHolder.tvaddress.setText("Business Address : "+streetadres + ", " + suburb + ", " + region + ", " + postcode);

            // jobid = postViewHolder.tvReview.getText().toString();
            Glide.with(context).load(item.getImgUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)/*.placeholder(R.drawable.ic_account)*/.dontAnimate().timeout(60000)).into(postViewHolder.ivImage);

         /*   postViewHolder.btn_website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String str_website = postViewHolder.tvwebsite.getText().toString();
                    try {
                        if (str_website.equals("")) {
                            Toast.makeText(context, "No Website", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                            i.setData(Uri.parse(str_website));

                            startActivity(i);
                        }
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "Url Incorrect", Toast.LENGTH_SHORT).show();
                        Log.d("error", e.getMessage());
                    }


                }
            });*/
           /* postViewHolder.btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String str_call = postViewHolder.tvphn.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", str_call, null));
                    startActivity(intent);

                }
            });*/
            postViewHolder.moreinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, Business_desc.class);
                    String jobid = postViewHolder.tv_id.getText().toString();
                    i.putExtra("b_id", jobid);
                    context.startActivity(i);
                }
            });
         /*     postViewHolder.tvRestaurant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, JobDescription.class);
                    jobid = postViewHolder.tvReview.getText().toString();
                    i.putExtra("restaurant", jobid);
                    context.startActivity(i);
                }
            });
*/
            postViewHolder.cvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, Business_desc.class);
                    String jobid = postViewHolder.tv_id.getText().toString();
                    i.putExtra("b_id", jobid);
                    context.startActivity(i);
                }
            });
          /*  postViewHolder.ivImage .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog builder = new Dialog(Business_act.this);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.setCancelable(true);
                    builder.setCanceledOnTouchOutside(true);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            //nothing;
                        }
                    });

                    ImageView imageView = new ImageView(Business_act.this);
                    Glide.with(context).load(list.get(position)).apply(new RequestOptions().fitCenter()).into(imageView);
                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    builder.show();
                }
            });
*/
        }

        @Override
        public CatlistingAdapter.PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mybusniness, viewGroup, false);


            return new CatlistingAdapter.PostViewHolder(itemView);
        }


        public class PostViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvName, tvaddress,/*, tvphn, tvwebsite,*/ tv_id,/* tvmobile,*/ tvmobilenum, tvuserid;
            protected ImageView ivImage;
            protected AppCompatRatingBar rbResto;
            protected CardView cvLayout;

            Button moreinfo/*, btn_cat, btn_viewjob, btn_website, btn_call*/;

            public PostViewHolder(View v) {
                super(v);
                tvName = (TextView) v.findViewById(R.id.service_title);
                tvaddress = (TextView) v.findViewById(R.id.address);
               /* tvphn = (TextView) v.findViewById(R.id.service_phn);
                tvmobile = (TextView) v.findViewById(R.id.service_mobile);
                tvwebsite = (TextView) v.findViewById(R.id.service_website);*/
                tv_id = (TextView) v.findViewById(R.id.business_id);

              /*  btn_website = v.findViewById(R.id.btn_website);*/
                moreinfo = v.findViewById(R.id.more_info);
               /* btn_cat = v.findViewById(R.id.btn_cat);
                btn_call = v.findViewById(R.id.btn_call);*/
                ivImage = v.findViewById(R.id.service_image);

                cvLayout = (CardView) v.findViewById(R.id.cvLayout);

            }
        }


    }


}

