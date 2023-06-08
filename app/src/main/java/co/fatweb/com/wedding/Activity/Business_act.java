package co.fatweb.com.wedding.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.fatweb.com.wedding.Adapter.RecyclerViewPositionHelper;
import co.fatweb.com.wedding.Connectivity.RestClient;
import co.fatweb.com.wedding.DataObject.Cat_Listing;
import co.fatweb.com.wedding.DataObject.User;
import co.fatweb.com.wedding.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Business_act extends AppCompatActivity {

    TextView txt_admybusiness, txt_signin, txt_signout, tvindicator, txtheading;
    Intent in;
    String catname, suburb, totallist, catheading;
    ProgressDialog dialog;
    int itemCount = 0;
    Cat_Listing newFeed;
    JSONArray jsonArray;
    JSONObject object;
    int firstVisibleItem, visibleItemCount, totalItemCount, count = 0;
    protected int m_PreviousTotalCount;
    RecyclerViewPositionHelper mRecyclerViewHelper;
    private LinearLayoutManager mLayoutManager;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    CatlistingAdapter cat_adapter;
    RecyclerView rvFeed;
    String totallisting;
    FrameLayout refreshlayout;
    LinearLayout back;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_act);
        refreshlayout = findViewById(R.id.refreshOverlay);
        tvindicator = findViewById(R.id.tvIndicator);
        rvFeed = findViewById(R.id.rvFeed);
        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String suser = sp.getString("email", null);
        String spass = sp.getString("password", null);
        in = getIntent();
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
        catname = in.getStringExtra("cat_name");
        suburb = in.getStringExtra("suburb");
        catheading = in.getStringExtra("catheading");

        Log.d("CATNAME######", catname + catheading);
        txtheading.setText(catheading);

        // Toast.makeText(Business_act.this, "" + catname + suburb, Toast.LENGTH_SHORT).show();
        attemptSearch();

        rvFeed.setHasFixedSize(true);


        rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();

                if (totalItemCount == 0 || cat_adapter == null)
                    return;
                if (m_PreviousTotalCount == totalItemCount)
                {
                    return;
                }
                else
                {
                    boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                    if (loadMore)
                    {
                        m_PreviousTotalCount = totalItemCount;
                        attemptSearch();
                    }
                }
            }
        });

     /* rvFeed.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(rvFeed);
                visibleItemCount = rvFeed.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();

                if (totalItemCount == 0 || cat_adapter == null)
                    return;
                if (m_PreviousTotalCount == totalItemCount) {
                    return;
                } else {
                    boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                    if (loadMore) {
                        m_PreviousTotalCount = totalItemCount;
                        attemptSearch();
                    }
                }

            }
        });*/

       /* rvFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(rvFeed);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();

                if (totalItemCount == 0 || cat_adapter == null)
                    return;
                if (m_PreviousTotalCount == totalItemCount) {
                    return;
                } else {
                    boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                    if (loadMore) {
                        m_PreviousTotalCount = totalItemCount;
                        attemptSearch();
                    }
                }
            }
        });*/
    }

    private void attemptSearch() {

        boolean cancel = false;
        View focusView = null;

        if (cancel) {
            focusView.requestFocus();
        } else {
            User newUser = new User();
            newUser.setBcategory(catname);
            newUser.setLocation("");


            new GetRecords(newUser).execute();


        }
    }

    public class GetRecords extends AsyncTask<Void, Void, Boolean> {

        Boolean success = false;
        User newUser = new User();
        List<Cat_Listing> feedList;
        String responseString;

        GetRecords(User user) {
            newUser = user;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            feedList = new ArrayList<>();
            refreshlayout.setVisibility(View.VISIBLE);

            tvindicator.setText("Please Wait");
            //  btnSubmit.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {
                itemCount++;
                String str_url = Business_act.this.getString(R.string.url_live);
                String normalUrl = str_url + "business/business_list";

                //  String normalUrl = "https://onestoptrade.co.nz/api/business/business_list";

                //  String normalUrl = /*context.getString(R.string.url_main)+"jobSearch"*/"https://dev.myjobspace.co.nz/webservice/job.php/jobSearch";
                Log.i("Search", normalUrl);

                JSONObject jObject = new JSONObject();
                jObject.accumulate("location", suburb);
                jObject.accumulate("category", catname);


                jObject.accumulate("page", itemCount);

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(Business_act.this, normalUrl, entity, new JsonHttpResponseHandler() {


                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        responseString = response;
                        Log.d("response", responseString.toString());
                        if (responseString.equalsIgnoreCase("success")) {
                            success = true;
                        } else success = false;
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        responseString = errorResponse.toString();
                        if (responseString.equalsIgnoreCase("success")) {
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
                        if (responseString.equalsIgnoreCase("success")) {
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
                            if (response.has("data")) {
                                if (response.getString("data").contains("error")) {
                                    success = false;
                                } else {
                                    success = true;

                                    android.util.Log.i("response", response.toString());
                                    JSONObject j = new JSONObject(response.toString());
                                    //    totaljobs = j.getString("total_count");

                                    totallisting = j.getString("total_value");
                                    jsonArray = j.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        object = jsonArray.getJSONObject(i);
                                        newFeed = new Gson().fromJson(object.toString(), Cat_Listing.class);


                                        feedList.add(newFeed);

                                        //  tottaljobs.setText(totaljobs+" Jobs");
                                        Log.d("test", " " + newFeed.getName() + " " + newFeed.getBusinessid());
                                    }
                                }
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
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);

            try {
                refreshlayout.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (s) {
                if (feedList.size() > 0) {
                    if (count == 0) {

                    } else {
                        cat_adapter = new CatlistingAdapter(feedList, Business_act.this);
                        rvFeed.setAdapter(cat_adapter);
                        newFeed = new Gson().fromJson(object.toString(), Cat_Listing.class);

                        feedList.add(newFeed);
                        rvFeed.setLayoutManager(new LinearLayoutManager(Business_act.this));
                        cat_adapter.notifyDataSetChanged();
                    }
                    if (jsonArray.length() == 0) {
                        count = 0;
                    } else {
                        count += jsonArray.length();
                        cat_adapter = new CatlistingAdapter(feedList, Business_act.this);
                        rvFeed.setAdapter(cat_adapter);
                        newFeed = new Gson().fromJson(object.toString(), Cat_Listing.class);
                        TextView txt_listing = findViewById(R.id.txt_totalpost);

                        txt_listing.setText("We found " + totallisting + " local business ");
                        feedList.add(newFeed);
                        rvFeed.setLayoutManager(new LinearLayoutManager(Business_act.this));
                        // feedList.add(newFeed);
                    }


                } else {

                    /* if (feedList.equals("[]")){*/
                    TextView txt_listing = findViewById(R.id.txt_totalpost);
                    txt_listing.setText("We found " + "0" + " local business ");


                }


            }
        }


    }


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
        public void onBindViewHolder(final PostViewHolder postViewHolder, final int position) {

            final Cat_Listing item = list.get(position);
            postViewHolder.tvName.setText(item.getName());

       /* try{
            postViewHolder.tvDateAdded.setText(TimeUtils.properDateFormatFeed(item.getDateAddedUtc(),context));

        }catch (Exception ex){
            ex.printStackTrace();
        }*/
            //  postViewHolder.tvaddress.setText(item.getAdress());
            postViewHolder.tvwebsite.setText(item.getWebsiteurl());
            postViewHolder.tvphn.setText(item.getPhone());

            String phn = postViewHolder.tvphn.getText().toString();

            postViewHolder.tv_id.setText(item.getBusinessid());
            postViewHolder.tvmobile.setText(item.getMobilenum());
            postViewHolder.tv_postviews.setText(" Post Views: " + item.getPostviews());
            String streetadres = item.getAdress();
            String suburb = item.getSuburb();
            String region = item.getRegion();
            String postcode = item.getPostcode();
            String cat_name = item.getCat_name();
            postViewHolder.tvaddress.setText(streetadres + ", " + suburb + ", " + region + ", " + postcode);

            // jobid = postViewHolder.tvReview.getText().toString();
            Glide.with(context).load(item.getImgUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)/*.placeholder(R.drawable.ic_account)*/.dontAnimate().timeout(60000)).into(postViewHolder.ivImage);
            // Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(postViewHolder.ivImage);
           /* postViewHolder.btn_savejob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toast.makeText(context, ""+jobid, Toast.LENGTH_SHORT).show();
                    if (id == null) {


                        new MaterialDialog.Builder(context)
                                .content("You have to sign in first to save job")
                                .positiveText("Sign In")
                                .negativeText("Cancel")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(Search_act.this, Home_act.class);
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

                    } else {
                        new SavedJob(id, jobid).execute();
                    }
                }
            });*/
            postViewHolder.btn_website.setOnClickListener(new View.OnClickListener() {
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
            });
            postViewHolder.btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String str_call = postViewHolder.tvphn.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", str_call, null));
                    startActivity(intent);

                }
            });
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
        public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_catlisting, viewGroup, false);


            return new PostViewHolder(itemView);
        }


        public class PostViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvName, tvaddress, tvphn, tvwebsite, tv_id, tvmobile, tv_postviews, tvmobilenum, tvuserid;
            protected ImageView ivImage;
            protected AppCompatRatingBar rbResto;
            protected CardView cvLayout;

            Button moreinfo, btn_cat, btn_viewjob, btn_website, btn_call;

            public PostViewHolder(View v) {
                super(v);
                tvName = (TextView) v.findViewById(R.id.service_title);
                tvaddress = (TextView) v.findViewById(R.id.service_address);
                tvphn = (TextView) v.findViewById(R.id.service_phn);
                tvmobile = (TextView) v.findViewById(R.id.service_mobile);
                tvwebsite = (TextView) v.findViewById(R.id.service_website);
                tv_id = (TextView) v.findViewById(R.id.business_id);
                tv_postviews = (TextView) v.findViewById(R.id.service_postviews);
                btn_website = v.findViewById(R.id.btn_website);
                moreinfo = v.findViewById(R.id.more_info);
                btn_cat = v.findViewById(R.id.btn_cat);
                btn_call = v.findViewById(R.id.btn_call);
                ivImage = v.findViewById(R.id.service_image);

                cvLayout = (CardView) v.findViewById(R.id.cvLayout);

            }
        }


    }


    public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int ITEM = 0;
        private static final int LOADING = 1;

        private List<Cat_Listing> movieResults;
        private Context context;

        private boolean isLoadingAdded = false;

        public PaginationAdapter(Context context) {
            this.context = context;
            movieResults = new ArrayList<>();
        }

        public List<Cat_Listing> getMovies() {
            return movieResults;
        }

        public void setMovies(List<Cat_Listing> movieResults) {
            this.movieResults = movieResults;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ITEM:
                    viewHolder = getViewHolder(parent, inflater);
                    break;
                case LOADING:
                    View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                    viewHolder = new LoadingVH(v2);
                    break;
            }
            return viewHolder;
        }

        @NonNull
        private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
            RecyclerView.ViewHolder viewHolder;
            View v1 = inflater.inflate(R.layout.item_feed, parent, false);
            viewHolder = new MovieVH(v1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            Cat_Listing result = movieResults.get(position); // Movie

            switch (getItemViewType(position)) {
                case ITEM:
                    final MovieVH movieVH = (MovieVH) holder;

                    movieVH.tvName.setText(result.getName());
                    movieVH.tvName.setText(result.getName());

                    movieVH.tvwebsite.setText(result.getWebsiteurl());
                    movieVH.tvphn.setText(result.getPhone());

                    String phn = movieVH.tvphn.getText().toString();

                    movieVH.tv_id.setText(result.getBusinessid());
                    movieVH.tvmobile.setText(result.getMobilenum());
                    movieVH.tv_postviews.setText(" Post Views: " + result.getPostviews());
                    String streetadres = result.getAdress();
                    String suburb = result.getSuburb();
                    String region = result.getRegion();
                    String postcode = result.getPostcode();
                    String cat_name = result.getCat_name();
                    movieVH.tvaddress.setText(streetadres + ", " + suburb + ", " + region + ", " + postcode);

                    // jobid = postViewHolder.tvReview.getText().toString();
                    Glide.with(context).load(result.getImgUrl()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)/*.placeholder(R.drawable.ic_account)*/.dontAnimate().timeout(60000)).into(movieVH.ivImage);


                    /**
                     * Using Glide to handle image loading.
                     * Learn more about Glide here:
                     * <a href="http://blog.grafixartist.com/image-gallery-app-android-studio-1-4-glide/" />
                     */
                   /* Glide.with(context)
                            .load(result.getImgUrl())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<Drawable> target, boolean isFirstResource) {
                                    // TODO: 08/11/16 handle failure
                                    movieVH.mProgress.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, String model, Target<Drawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    // image ready, hide progress now
                                    movieVH.mProgress.setVisibility(View.GONE);
                                    return false;   // return false if you want Glide to handle everything else.
                                }
                            })
                            .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                            .centerCrop()
                            .crossFade()
                            .into(movieVH.mPosterImg);

                    break;

                case LOADING:
//                Do nothing
                    break;*/
            }

        }

        @Override
        public int getItemCount() {
            return movieResults == null ? 0 : movieResults.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

        public void add(Cat_Listing r) {
            movieResults.add(r);
            notifyItemInserted(movieResults.size() - 1);
        }

        public void addAll(List<Cat_Listing> moveResults) {
            for (Cat_Listing result : moveResults) {
                add(result);
            }
        }

        public void remove(Cat_Listing r) {
            int position = movieResults.indexOf(r);
            if (position > -1) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            isLoadingAdded = false;
            while (getItemCount() > 0) {
                remove(getItem(0));
            }
        }

        public boolean isEmpty() {
            return getItemCount() == 0;
        }


        public void addLoadingFooter() {
            isLoadingAdded = true;
            add(new Cat_Listing());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movieResults.size() - 1;
            Cat_Listing result = getItem(position);

            if (result != null) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public Cat_Listing getItem(int position) {
            return movieResults.get(position);
        }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

        /**
         * Main list's content ViewHolder
         */
        protected class MovieVH extends RecyclerView.ViewHolder {
            protected TextView tvName, tvaddress, tvphn, tvwebsite, tv_id, tvmobile, tv_postviews, tvmobilenum, tvuserid;
            protected ImageView ivImage;
            protected AppCompatRatingBar rbResto;
            protected CardView cvLayout;

            Button moreinfo, btn_cat, btn_viewjob, btn_website, btn_call;

            public MovieVH(View itemView) {
                super(itemView);

                tvName = (TextView) findViewById(R.id.service_title);
                tvaddress = (TextView) findViewById(R.id.service_address);
                tvphn = (TextView) findViewById(R.id.service_phn);
                tvmobile = (TextView) findViewById(R.id.service_mobile);
                tvwebsite = (TextView) findViewById(R.id.service_website);
                tv_id = (TextView) findViewById(R.id.business_id);
                tv_postviews = (TextView) findViewById(R.id.service_postviews);
                btn_website = findViewById(R.id.btn_website);
                moreinfo = findViewById(R.id.more_info);
                btn_cat = findViewById(R.id.btn_cat);
                btn_call = findViewById(R.id.btn_call);
                ivImage = findViewById(R.id.service_image);

                cvLayout = (CardView) findViewById(R.id.cvLayout);
            }
        }


        protected class LoadingVH extends RecyclerView.ViewHolder {

            public LoadingVH(View itemView) {
                super(itemView);
            }
        }


    }


}
