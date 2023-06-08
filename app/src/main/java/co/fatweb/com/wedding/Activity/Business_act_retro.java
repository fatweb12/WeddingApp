package co.fatweb.com.wedding.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import co.fatweb.com.wedding.Adapter.PaginationAdapterCallback;
import co.fatweb.com.wedding.DataObject.Result;
import co.fatweb.com.wedding.DataObject.TopRatedMovies;
import co.fatweb.com.wedding.Model.PaginationScrollListener;
import co.fatweb.com.wedding.R;
import co.fatweb.com.wedding.api.MovieService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Business_act_retro extends AppCompatActivity {

    TextView tvindicator, txtheading;
    Intent in;
    String catname, suburb, catheading;
    ProgressDialog dialog;

    String totallisting;
    FrameLayout refreshlayout;
    LinearLayout back;

    /*PAGINATION_CODE*/
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    int total_count;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;

    private MovieService movieService;
    TextView txt_listing;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_act);
        refreshlayout = findViewById(R.id.refreshOverlay);
        tvindicator = findViewById(R.id.tvIndicator);
        rv = findViewById(R.id.rvFeed);
        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String suser = sp.getString("email", null);
        String spass = sp.getString("password", null);
        in = getIntent();
        txtheading = findViewById(R.id.txtheadimg);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        catname = in.getStringExtra("cat_name");
        suburb = in.getStringExtra("suburb");
        catheading = in.getStringExtra("catheading");
        txt_listing = findViewById(R.id.txt_totalpost);


        Log.d("CATNAME######", catname + catheading);
        txtheading.setText(catheading);

        rv = findViewById(R.id.rvFeed);
        progressBar = findViewById(R.id.main_progress);
        adapter = new PaginationAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);


        //  attemptSearch();

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadNextPage();
                    }
                }, 1500);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        //  Context context=Main2Activity.this;
        // movieService = MovieApi.getClient(this).create(MovieService.class);

        loadFirstPage();


    }

    private void loadFirstPage() {
        Log.d("WEDDINGAPP", "loadFirstPage: ");

        String url = "https://ido.kiwi/api/business/";
        Log.i("autolog", url);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = null;
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            Log.i("autolog", "build();");
        }

        // RequestInterface service = retrofit.create(RequestInterface.class);
        movieService = retrofit.create(MovieService.class);

        TopRatedMovies dataSet = new TopRatedMovies(suburb, catname, currentPage);

        final Call<TopRatedMovies> userCall = movieService.getUser(dataSet);
        Log.d("REQUEST", dataSet.toString());
        userCall.enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {


                List<Result> results = fetchResults(response);
                Log.d("RESULTS", String.valueOf(results));

                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                //    Toast.makeText(Business_act_retro.this, "jh" + totallisting, Toast.LENGTH_SHORT).show();



                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<TopRatedMovies> call, Throwable t) {
                t.printStackTrace();
                if (t.getMessage().equals("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $")){
                   // Toast.makeText(Business_act_retro.this, "No  record found!", Toast.LENGTH_SHORT).show();
                   // adapter.removeLoadingFooter();
                    txt_listing = findViewById(R.id.txt_totalpost);

                    txt_listing.setText("We found 0" + " local business ");
                    progressBar.setVisibility(View.GONE);

                }
                // TODO: 08/11/16 handle failure
            }
        });
    }

    private List<Result> fetchResults(Response<TopRatedMovies> response) {
        TopRatedMovies topRatedMovies = response.body();

        totallisting=topRatedMovies.getTotalResults();
        txt_listing = findViewById(R.id.txt_totalpost);

        txt_listing.setText("We found " + totallisting + " local business ");
        total_count = Integer.parseInt(totallisting);


        return topRatedMovies.getResults();


    }

    private void loadNextPage() {
        Log.d("BUSINESS_ACT", "loadNextPage: " + currentPage);

        try {
            String url = "https://ido.kiwi/api/business/";
            Log.i("autolog", url);
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();
            Retrofit retrofit = null;
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
                Log.i("autolog", "build();");
            }

            // RequestInterface service = retrofit.create(RequestInterface.class);
            movieService = retrofit.create(MovieService.class);


            TopRatedMovies dataSet = new TopRatedMovies(suburb, catname, currentPage);

            Call<TopRatedMovies> userCall = movieService.getUser(dataSet);
            Log.d("REQUEST", dataSet.toString());
            userCall.enqueue(new Callback<TopRatedMovies>() {
                @Override
                public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
                    if (response.isSuccessful()) {

                        adapter.removeLoadingFooter();
                        isLoading = false;

                        List<Result> results = fetchResults(response);

                        adapter.addAll(results);

                        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;

                    }
                    else{
                        isLoading=false;
                    }
                }

                @Override
                public void onFailure(Call<TopRatedMovies> call, Throwable t) {
                    if (t.getMessage().equals("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $")){
                        Toast.makeText(Business_act_retro.this, "No more record found!", Toast.LENGTH_SHORT).show();
                        adapter.removeLoadingFooter();


                    }
                  //Toast.makeText(Business_act_retro.this, ""+ t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("autologERR", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("autolog", "Exception");
        }


    }



    /*PAGINATION_ADAPTER*/

    public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        // View Types
        private static final int ITEM = 0;
        private static final int LOADING = 1;

        private List<Result> movieResults;
        private Context context;
        private boolean isLoadingAdded = false;
        private boolean retryPageLoad = false;

        private PaginationAdapterCallback mCallback;
        SwipeRefreshLayout swipeRefreshLayout;

        private String errorMsg;

        PaginationAdapter(Context context) {
            this.context = context;
//        this.mCallback = (PaginationAdapterCallback) context;
            movieResults = new ArrayList<>();
        }

        public List<Result> getMovies() {
            return movieResults;
        }

        public void setMovies(List<Result> movieResults) {
            this.movieResults = movieResults;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.item_catlisting, parent, false);
                    viewHolder = new MovieVH(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_progress1, parent, false);
                    viewHolder = new LoadingVH(viewLoading);
                    break;

            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Result result = movieResults.get(position); // Movie

            switch (getItemViewType(position)) {


                case ITEM:
                    final MovieVH movieVH = (MovieVH) holder;

                    movieVH.tvName.setText(result.getName());
                    movieVH.tvaddress.setText(result.getState());


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

                   /* totallisting = result.getTotalvalue();
                    txt_listing.setText("We found " + totallisting + " local business ");

                    total_count = Integer.parseInt(totallisting);*/


                    Log.d("Total_count", totallisting);

                    // Toast.makeText(Business_act_retro.this, "" + totallisting, Toast.LENGTH_SHORT).show();

                    // load movie thumbnail
                    Glide.with(context)
                            .load(result.getImgurl())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    // TODO: 2/16/19 Handle failure
                                    // movieVH.mProgress.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // image ready, hide progress now
                                    // movieVH.mProgress.setVisibility(View.GONE);
                                    return false;   // return false if you want Glide to handle everything else.
                                }
                            })
                            .into(movieVH.ivImage);

                    movieVH.btn_website.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String str_website = movieVH.tvwebsite.getText().toString();
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
                    movieVH.btn_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String str_call = movieVH.tvphn.getText().toString();
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", str_call, null));
                            startActivity(intent);

                        }
                    });
                    movieVH.moreinfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(context, Business_desc.class);
                            String jobid = movieVH.tv_id.getText().toString();
                            i.putExtra("b_id", jobid);
                            context.startActivity(i);
                        }
                    });

                    movieVH.cvLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, Business_desc.class);
                            String jobid = movieVH.tv_id.getText().toString();
                            i.putExtra("b_id", jobid);
                            context.startActivity(i);
                        }
                    });


                    break;

                case LOADING:
                    LoadingVH loadingVH = (LoadingVH) holder;

                    if (retryPageLoad) {
                        loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                        loadingVH.mProgressBar.setVisibility(View.GONE);

                        loadingVH.mErrorTxt.setText(
                                errorMsg != null ?
                                        errorMsg :
                                        context.getString(R.string.error_msg_unknown));

                    } else {
                        loadingVH.mErrorLayout.setVisibility(View.GONE);
                        //loadingVH.mProgressBar.setVisibility(View.GONE);
                    }
                    break;


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


        public void add(Result r) {
            movieResults.add(r);
            notifyItemInserted(movieResults.size() - 1);
        }

        public void addAll(List<Result> moveResults) {
            for (Result result : moveResults) {
                add(result);
            }
        }

        public void remove(Result r) {
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
            add(new Result());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movieResults.size() - 1;
            Result result = getItem(position);

            if (result != null) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public Result getItem(int position) {
            return movieResults.get(position);
        }

        /**
         * Displays Pagination retry footer view along with appropriate errorMsg
         *
         * @param show
         * @param errorMsg to display if page load fails
         */
        public void showRetry(boolean show, @Nullable String errorMsg) {
            retryPageLoad = show;
            notifyItemChanged(movieResults.size() - 1);

            if (errorMsg != null) this.errorMsg = errorMsg;
        }

        protected class MovieVH extends RecyclerView.ViewHolder {
            protected TextView tvName, tvaddress, tvphn, tvwebsite, tv_id, tvmobile, tv_postviews, tvmobilenum, tvuserid;
            protected ImageView ivImage;
            protected AppCompatRatingBar rbResto;
            protected CardView cvLayout;

            Button moreinfo, btn_cat, btn_viewjob, btn_website, btn_call;

            // private ProgressBar mProgress;

            public MovieVH(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.service_title);
                tvaddress = itemView.findViewById(R.id.service_address);
                tvphn = itemView.findViewById(R.id.service_phn);
                tvmobile = itemView.findViewById(R.id.service_mobile);
                tvwebsite = itemView.findViewById(R.id.service_website);
                tv_id = itemView.findViewById(R.id.business_id);
                tv_postviews = itemView.findViewById(R.id.service_postviews);
                btn_website = itemView.findViewById(R.id.btn_website);
                moreinfo = itemView.findViewById(R.id.more_info);
                btn_call = itemView.findViewById(R.id.btn_call);
                ivImage = itemView.findViewById(R.id.service_image);

                cvLayout = itemView.findViewById(R.id.cvLayout);
                // mProgress = itemView.findViewById(R.id.movie_progress);
            }
        }


        protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ProgressBar mProgressBar;
            private ImageButton mRetryBtn;
            private TextView mErrorTxt;
            private LinearLayout mErrorLayout;

            public LoadingVH(View itemView) {
                super(itemView);

                mProgressBar = itemView.findViewById(R.id.loadmore_progress);
                mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
                mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
                mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

                mRetryBtn.setOnClickListener(this);
                mErrorLayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.loadmore_retry:
                    case R.id.loadmore_errorlayout:

                        showRetry(false, null);
                        mCallback.retryPageLoad();

                        break;
                }
            }
        }

    }


}
