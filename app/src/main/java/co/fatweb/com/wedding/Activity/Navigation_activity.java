package co.fatweb.com.wedding.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import co.fatweb.com.wedding.Adapter.ItemAdapter;
import co.fatweb.com.wedding.Connectivity.RestClient;
import co.fatweb.com.wedding.DataObject.Allergy;
import co.fatweb.com.wedding.DataObject.User;
import co.fatweb.com.wedding.Model.Config;
import co.fatweb.com.wedding.Model.Config1;
import co.fatweb.com.wedding.R;
import co.fatweb.com.wedding.api.RequestInterface;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Navigation_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    Button btn_startbusines;

    ScrollView sccat, lingrid1;

    CategoryListing catadapter;
    RecyclerView rvFeed, mRecycleview;
    ProgressDialog dialog;
    TextView tvindicator;
    TextView tvusername;
    String selectedtext_str, suser, spass, strname, strlstname, selectedtext_str1, user_id, str_selectCAT;
    private List<Allergy> feedList = new ArrayList<>();

    Button btn_viewall;

    Button toolBarBtn;
    String sname;
    FrameLayout refreshlayout;

    private ArrayList<String> students;
    private ArrayList<String> students1;
    String name = "";
    JSONArray result,result1;
    TextView textViewName,textViewName1;
    AutoCompleteTextView autoCompleteTextView, autoCompleteTextView_SUBURB;
    TextView tvDisplay, TvDisplay1;

    private List<Allergy> feedList1 = new ArrayList<>();
    private List<Allergy> feedList2 = new ArrayList<>();
    GoogleSignInClient mGoogleSignInClient;

    TextView edt_catTOP;
    List<Allergy> allergyList;

    private Spinner spinner,spinner1;

    MaterialBetterSpinner materialBetterSpinner ;
    LinearLayout lingrid;
    ListAdapter mAdapter;
    private List<ItemAdapter> mList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_startbusines = findViewById(R.id.btn_getstarted);
        refreshlayout = findViewById(R.id.refreshOverlay);
        edt_catTOP = findViewById(R.id.edt_cat);
        tvindicator = findViewById(R.id.tvIndicator);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName1 = (TextView) findViewById(R.id.textViewName1);
        btn_viewall = findViewById(R.id.Viewall);
        lingrid1 = findViewById(R.id.lingrid1);
        lingrid = findViewById(R.id.lingrid);
        mRecycleview = findViewById(R.id.listView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        addList();
        new GetAllCategoryTOP2().execute();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        suser = sp.getString("email", null);
        spass = sp.getString("password", null);
        name = sp.getString("NAME", null);

        user_id = sp.getString("id", null);
        new GetUserDetail(user_id).execute();
        getData();
        getData1();

        Log.d("USERDETAIL", suser + spass + sname + user_id);
        mAdapter = new ListAdapter(mList, getApplicationContext());

        mRecycleview.setAdapter(mAdapter);
        mRecycleview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        mRecycleview.setHasFixedSize(true);
        mAdapter.notifyDataSetChanged();
        btn_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                lingrid1.setVisibility(View.GONE);
                TranslateAnimation animate = new TranslateAnimation(
                        0,
                        0,
                        0,
                        lingrid1.getHeight());
                animate.setDuration(100);
                animate.setFillAfter(true);
                lingrid1.startAnimation(animate);

                lingrid.setVisibility(View.VISIBLE);


            }
        });
        students = new ArrayList<String>();
        students1 = new ArrayList<String>();
        if (sname == null) {
//            tvusername.setText("One Stop Trade");
        } else {
            // tvusername.setText(sname);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //  Toast.makeText(Navigation_activity.this, "" + suser + spass, Toast.LENGTH_SHORT).show();
        if ((suser == null) && (spass == null)) {

            Menu nav_Menu = navigationView.getMenu();
            /*  nav_Menu.findItem(R.id.nav_register).setVisible(true);*/
            nav_Menu.findItem(R.id.nav_signin).setVisible(true);


        } else {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_signout).setVisible(true);
            nav_Menu.findItem(R.id.nav_profile).setVisible(true);
            nav_Menu.findItem(R.id.nav_businessP).setVisible(true);

        }
        /*GMAIL*/

        /*SPINNER CAT*/
        spinner = (Spinner) findViewById(R.id.spinner);
        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);

     spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                 // Notify the selected item text

                 textViewName.setText(getName(position));
                 selectedtext_str = textViewName.getText().toString(); }



         @Override
         public void onNothingSelected(AdapterView<?> parent) {


         }

     });

        spinner1 = findViewById(R.id.spinner1);
        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textViewName1.setText(getName1(position));
                selectedtext_str1=textViewName1.getText().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //  tvusername.setText(strname+" "+strlstname);
        rvFeed = findViewById(R.id.rvFeed);
        rvFeed.setLayoutManager(new GridLayoutManager(this, 3));
        rvFeed.setHasFixedSize(true);
       // new GetAllCategory().execute();
        getUserList();
        toolBarBtn = (Button) findViewById(R.id.toolbarbtn);
        toolBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user_id != null) {

                    Intent i = new Intent(getApplicationContext(), Addmybusiness_act.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                } else {
                    Intent intent = new Intent(Navigation_activity.this, SignIn_act.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();

                    // Toast.makeText(AddMYBusiness1_act.this, "dadad", Toast.LENGTH_SHORT).show();
                    /*new MaterialDialog.Builder(Navigation_activity.this)
                            .content("You have to sign in first to Add business")
                            .positiveText("Sign In")
                            .negativeText("Cancel")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(Navigation_activity.this, SignIn_act.class);
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
*/

                }

            }
        });

        btn_startbusines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String suburb = textViewName.getText().toString();
              /*  if (selectedtext_str == null) {
                    Toast.makeText(Navigation_activity.this, "Please Select Category", Toast.LENGTH_SHORT).show();
                } else {*/
             //  Toast.makeText(Navigation_activity.this, "" + selectedtext_str+suburb, Toast.LENGTH_SHORT).show();
              Intent in = new Intent(getApplicationContext(), Business_act_retro.class);
                in.putExtra("cat_name", selectedtext_str);
                in.putExtra("suburb", selectedtext_str1);
                in.putExtra("catheading", selectedtext_str);
                Log.d("CATNAME", selectedtext_str + "  " + selectedtext_str1);
                startActivity(in);
                Log.d("CATNAME", selectedtext_str + "  " + selectedtext_str1+ "  " + selectedtext_str);


            }
        });

        allergyList = new ArrayList<>();
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.actv);
        tvDisplay = (TextView) findViewById(R.id.tvDisplay);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedtext_str = parent.getItemAtPosition(position).toString();


                //  Toast.makeText(Navigation_activity.this, "Selected Item is: \t" + selectedtext_str, Toast.LENGTH_LONG).show();
                tvDisplay.setText(selectedtext_str);
            }
        });
        new GetAllCategoryTOP().execute();
        autoCompleteTextView_SUBURB = (AutoCompleteTextView) findViewById(R.id.suburb_listing);
        TvDisplay1 = (TextView) findViewById(R.id.tvDisplay1);
        autoCompleteTextView_SUBURB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedtext_str1 = parent.getItemAtPosition(position).toString();
                // Toast.makeText(Navigation_activity.this, "Selected Item is: \t" + selectedtext_str1, Toast.LENGTH_LONG).show();
                TvDisplay1.setText(selectedtext_str1);
            }
        });
        new GetAllCategoryTOP1().execute();

    }

    private void getData() {
        //Creating a string request
        Log.e("URL", Config.DATA_URL);
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //Parsing the fetched Json String to JSON Object

                            Log.e("RESPONSE", response);
                            //Storing the Array of JSON String to our JSON Array
                            result = new JSONArray(response);

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getStudents(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                students.add(json.getString(Config.TAG_NAME));
                Log.d("STUDENTS", String.valueOf(students));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        spinner.setAdapter(new ArrayAdapter<>(Navigation_activity.this, R.layout.spinner,R.id.tct, students));

      /*  materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Navigation_activity.this, android.R.layout.simple_dropdown_item_1line, students);
*/
//        materialBetterSpinner.setAdapter(adapter);

    }
    private String getName(int position) {
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);
            // String str_content = json.getString("id");
            // Toast.makeText(this, ""+str_content, Toast.LENGTH_SHORT).show();

            Log.e("result", String.valueOf(result));
            //Fetching name from that object
            name = json.getString("name");
            // Toast.makeText(this, "NAME :  " + name, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    private void getData1() {
        //Creating a string request
        Log.e("URL1", Config1.DATA_URL);
        StringRequest stringRequest = new StringRequest(Config1.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //Parsing the fetched Json String to JSON Object

                            Log.e("RESPONSE", response);
                            //Storing the Array of JSON String to our JSON Array
                            result1 = new JSONArray(response);

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents1(result1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getStudents1(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                students1.add(json.getString(Config1.TAG_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinner1.setAdapter(new ArrayAdapter<String>(Navigation_activity.this, R.layout.spinner,R.id.tct, students1));

      /*  materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Navigation_activity.this, android.R.layout.simple_dropdown_item_1line, students);
*/
//        materialBetterSpinner.setAdapter(adapter);

    }
    private String getName1(int position) {
        try {
            //Getting object of given index
            JSONObject json = result1.getJSONObject(position);
            // String str_content = json.getString("id");
            // Toast.makeText(this, ""+str_content, Toast.LENGTH_SHORT).show();

            Log.e("result", String.valueOf(result1));
            //Fetching name from that object
            name = json.getString("name");
            // Toast.makeText(this, "NAME :  " + name, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    private void addList() {
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.list1);
        itemAdapter.setText("Accommodation");
        itemAdapter.setId("accommodation");
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.list2);
        itemAdapter.setText("Bands");
        itemAdapter.setId("bands");
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.list3);
        itemAdapter.setText("Books");
        itemAdapter.setId("books");
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.list4);
        itemAdapter.setText("Bridal Dresses and Wedding Accessories");
        itemAdapter.setId("bridal-dresses-and-wedding-accessories");
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.list5);
        itemAdapter.setText("Bridal Hair and Wedding Makeup");
        itemAdapter.setId("bridal-hair-and-wedding-makeup");
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.list6);
        itemAdapter.setText("Catering Companies");
        itemAdapter.setId("catering-companies");
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.list7);
        itemAdapter.setText("Decorations and Favours");
        itemAdapter.setId("decorations-and-favours");
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.list8);
        itemAdapter.setText("Elopements");
        itemAdapter.setId("elopements");
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.list9);
        itemAdapter.setText("Entertainment");
        itemAdapter.setId("entertainment");
        mList.add(itemAdapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            myCustomSnackbar();
            try {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            super.onBackPressed();
        }
    }

    public void myCustomSnackbar() {
        LinearLayout llShow = findViewById(R.id.lin);
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Snackbar snackbar = Snackbar.make(llShow, "", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setPadding(0, 0, 0, 0);
        // Hide the text
        TextView textView = layout.findViewById(R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.my_snackbar, null);
        // Configure the view
        TextView textViewNo = snackView.findViewById(R.id.txtNo);

        textViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        TextView textViewYes = snackView.findViewById(R.id.txtYes);
        textViewYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Add the view to the Snackbar's layout
        layout.addView(snackView, objLayoutParams);
        // Show the Snackbar
        snackbar.show();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_activity, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signin) {
            Intent i = new Intent(getApplicationContext(), SignIn_act.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            // Handle the camera action
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(getApplicationContext(), Navigation_activity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        } else if (id == R.id.nav_signout) {

            new MaterialDialog.Builder(Navigation_activity.this)
                    .content("Are you sure to sign out")
                    .positiveText("Yes")
                    .negativeText("No")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            SharedPreferences preferences = getSharedPreferences("save_data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            signOut();
                            fb_signOut();
                            Intent intent = new Intent(getApplicationContext(), Splash_act.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            startActivity(intent);


                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    }).show();

        } else if (id == R.id.nav_register) {
            Intent i = new Intent(getApplicationContext(), Register_act.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        } else if (id == R.id.nav_about_us) {
            Intent i = new Intent(getApplicationContext(), About_us.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        } else if (id == R.id.nav_Contact_us) {
            Intent i = new Intent(getApplicationContext(), Conatct_us.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        } else if (id == R.id.nav_terms) {
            Intent i = new Intent(getApplicationContext(), TermsCondition_act.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(getApplicationContext(), Profile_act.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        } else if (id == R.id.nav_businessP) {
            Intent i = new Intent(getApplicationContext(), Business_Listing.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Toast.makeText(Navigation_activity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Navigation_activity.this, Splash_act.class));
                        finish();
                    }
                });
    }

    private void fb_signOut() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }
    }


//    RTEROFIT API CALL

    private void getUserList() {
        refreshlayout.setVisibility(View.VISIBLE);

        tvindicator.setText("Please Wait");
        Log.i("autolog", "getUserList");
        try {
            String url = "https://ido.kiwi/api/common/";
            Log.i("autolog", url);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestInterface request = retrofit.create(RequestInterface.class);
            Call<List<Allergy>> call = request.getUserData();

            call.enqueue(new Callback<List<Allergy>>() {
                @Override
                public void onResponse(Call<List<Allergy>> call, retrofit2.Response<List<Allergy> >response) {

                    refreshlayout.setVisibility(View.GONE);
                    //Log.i("onResponse", response.message());
                    try {
                        if (response != null) {
                            Log.i("autolog", "onResponse");
                            // Log.w("JSON RESPONSE> ", new Gson().toJson(response));
                            feedList = response.body();
                            catadapter = new CategoryListing(feedList, Navigation_activity.this);
                            catadapter.notifyDataSetChanged();
                            rvFeed = findViewById(R.id.rvFeed);


                            rvFeed.setHasFixedSize(true);
                            // str_jobod1 = newFeed.getCvid();
                            rvFeed.setAdapter(catadapter);


                        } else {
                            Toast.makeText(Navigation_activity.this, "Plasback", Toast.LENGTH_SHORT).show();

                        }
                    }catch (Exception e){

                    }
                }

                @Override
                public void onFailure(Call<List<Allergy>> call, Throwable t) {
                    refreshlayout.setVisibility(View.GONE);
                    Log.i("autolog", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("autolog", "Exception");
        }
    }


    class GetAllCategory extends AsyncTask<Void, Void, String> {
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

                if (s != null) {
                    // Do you work here on success

                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Allergy newFeed = new Gson().fromJson(object.toString(), Allergy.class);


                        feedList.add(newFeed);


                        catadapter = new CategoryListing(feedList, Navigation_activity.this);
                        catadapter.notifyDataSetChanged();
                        rvFeed = findViewById(R.id.rvFeed);


                        rvFeed.setHasFixedSize(true);
                        // str_jobod1 = newFeed.getCvid();
                        rvFeed.setAdapter(catadapter);

                        Log.d("test", newFeed.getId() + " " + newFeed.getName() + " " + newFeed.getSlug()/* + newFeed.getCat_image()*/);
                    }
                } else {

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

                URL url = new URL("https://ido.kiwi/api/common/category");
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

            /*pd = new ProgressDialog(Navigation_activity.this);
            pd.setMessage("Loading...");
            pd.show();*/
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

                String normalUrl = "https://ido.kiwi/api/users/user_details";
                android.util.Log.i("test", normalUrl);

                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", suser);


                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(Navigation_activity.this, normalUrl, entity, new JsonHttpResponseHandler() {
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
                            tvusername = findViewById(R.id.username);
                            tvusername.setText(strname + " " + strlstname);
                            Log.d("USER NAME WITH API", strname);
                            // Log.e("name", stremail);

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
                /*   pd.dismiss();*/

            } catch (Exception ex) {
                ex.printStackTrace();
            }
           /* edt_firstname.setText(strname);
            edt_lastname.setText(strlstname);
            edt_email.setText(stremail);*/
//Log.e("Content",s);

        }


    }

    class GetAllCategoryTOP extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            feedList1.clear();
            //co.clear();
            android.util.Log.i("response", s);
            try {

                if (s != null) {

                    JSONArray jsonarray = new JSONArray(s);
                    ArrayList<String> arrList = new ArrayList<String>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obl = jsonarray.getJSONObject(i);


                        String strValue = obl.getString("name");
                        arrList.add(strValue);
                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(
                            Navigation_activity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            arrList);

                    autoCompleteTextView.setAdapter(adapter);


                }

            } catch (
                    JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {


            try {
                //creating a URL
                // URL url = new URL("https://dev.myjobspace.co.nz/webservice/job.php/userJobAlertListing/" + id);

                URL url = new URL("https://ido.kiwi/api/common/category");
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

    class GetAllCategoryTOP1 extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            feedList2.clear();
            //co.clear();
            android.util.Log.i("response", s);
            try {

                if (s != null) {

                    JSONArray jsonarray = new JSONArray(s);
                    ArrayList<String> arrList = new ArrayList<String>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obl = jsonarray.getJSONObject(i);


                        String strValue = obl.getString("name");
                        arrList.add(strValue);
                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(
                            Navigation_activity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            arrList);

                    autoCompleteTextView_SUBURB.setAdapter(adapter);


                } else {

                }

            } catch (
                    JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {


            try {
                //creating a URL
                // URL url = new URL("https://dev.myjobspace.co.nz/webservice/job.php/userJobAlertListing/" + id);

                URL url = new URL("https://ido.kiwi/api/common/suburbs");
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
    public class CategoryListing extends RecyclerView.Adapter<CategoryListing.PostViewHolder> {

        private List<Allergy> list;
        Context context;
        String jobid;
        User user;

        public CategoryListing(List<Allergy> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getItemCount() {


            return list.size();
        }

        @Override
        public void onBindViewHolder(final CategoryListing.PostViewHolder postViewHolder, final int position) {

            final Allergy item = list.get(position);
            postViewHolder.tvName.setText(item.getName());
            postViewHolder.tvName_Slug.setText(item.getSlug());

            final String catheadingname = postViewHolder.tvName.getText().toString();
            final String catname = postViewHolder.tvName_Slug.getText().toString();
            postViewHolder.tvReview.setText(item.getId());

            postViewHolder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Toast.makeText(Navigation_activity.this, "" + catname, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), Business_act_retro.class);
                    in.putExtra("cat_name", catname);
                    in.putExtra("suburb", "");
                    in.putExtra("catheading", catheadingname);
                    startActivity(in);
                    Log.d("CATNAME", catname + catheadingname);
                }
            });
            postViewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(Navigation_activity.this, "" + selectedtext_str, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), Business_act_retro.class);
                    in.putExtra("cat_name", catname);
                    in.putExtra("suburb", "");
                    in.putExtra("catheading", catheadingname);
                    startActivity(in);
                }
            });
            //Glide.with(context).load(item.getCat_image()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).dontAnimate()).into(postViewHolder.ivImage);

            Glide.with(context).load(item.getCat_image()).into(postViewHolder.ivImage);
        }

        @Override
        public CategoryListing.PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_feed, viewGroup, false);


            return new CategoryListing.PostViewHolder(itemView);
        }


        public class PostViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvName, tvReview, tvName_Slug, tvname1, tv_dateposted, tv_status;
            protected ImageView cvLayout;

            protected ImageView ivImage;
            Button btn_cvActive;
            Button btn_cvDeActive;

            public PostViewHolder(View v) {
                super(v);
                tvName = (TextView) v.findViewById(R.id.tvName);
                tvName_Slug = (TextView) v.findViewById(R.id.tvName_Slug);
                /*  tvname1 = (TextView) v.findViewById(R.id.tvName1);*/
                tvReview = (TextView) v.findViewById(R.id.tvReview);
                ivImage = v.findViewById(R.id.ivImage);

            }
        }

    }

    class GetAllCategoryTOP2 extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            feedList2.clear();
            //co.clear();
            android.util.Log.i("@@response", s);
            try {

                if (s != null) {


                   /* android.util.Log.i("@@@response", response.toString());
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Allergy newAllergy = new Gson().fromJson(object.toString(), Allergy.class);
                        allergyList.add(newAllergy);
                        Log.d("test", newAllergy.getId() + " " + object.getString("category_name") + " " + object.getString("image"));
*/


                    JSONArray jsonarray = new JSONArray(s);
                    ArrayList<String> arrList = new ArrayList<String>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject object = jsonarray.getJSONObject(i);
                        Allergy newAllergy = new Gson().fromJson(object.toString(), Allergy.class);
                        allergyList.add(newAllergy);
                        Log.d("cat_test", newAllergy.getId() + " " + object.getString("name") + " " + object.getString("image"));

                    }
                /*    ArrayAdapter adapter = new ArrayAdapter<String>(
                            Navigation_activity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            arrList);

                    autoCompleteTextView_SUBURB.setAdapter(adapter);*/


                } else {

                }

            } catch (
                    JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {


            try {
                //creating a URL
                // URL url = new URL("https://dev.myjobspace.co.nz/webservice/job.php/userJobAlertListing/" + id);

                URL url = new URL("https://ido.kiwi/api/common/category");
                Log.d("URl_cat####", String.valueOf(url));
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
    public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<ItemAdapter> mList;
        private Context mContext;

        public ListAdapter(List<ItemAdapter> list, Context context) {
            super();
            mList = list;
            mContext = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
            ListAdapter.ViewHolder viewHolder = new ListAdapter.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            ItemAdapter itemAdapter = mList.get(position);
            ((ListAdapter.ViewHolder) viewHolder).mTv_name.setText(itemAdapter.getText());
            ((ListAdapter.ViewHolder) viewHolder).tv_id.setText(itemAdapter.getId());

            ((ListAdapter.ViewHolder) viewHolder).mImg.setImageResource(itemAdapter.getImage());
            final String catheadingname = ((ListAdapter.ViewHolder) viewHolder).mTv_name.getText().toString();
            final String catname = ((ListAdapter.ViewHolder) viewHolder).tv_id.getText().toString();
            ((ListAdapter.ViewHolder) viewHolder).mTv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Toast.makeText(Navigation_activity.this, "" + catname, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), Business_act_retro.class);
                    in.putExtra("cat_name", catname);
                    in.putExtra("suburb", "");
                    in.putExtra("catheading", catheadingname);
                    startActivity(in);
                    Log.d("CATNAME", catname + catheadingname);
                }
            });
            ((ListAdapter.ViewHolder) viewHolder).mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(Navigation_activity.this, "" + selectedtext_str, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), Business_act_retro.class);
                    in.putExtra("cat_name", catname);
                    in.putExtra("suburb", "");
                    in.putExtra("catheading", catheadingname);
                    startActivity(in);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTv_name, tv_id;
            public ImageView mImg;

            public ViewHolder(View itemView) {
                super(itemView);
                mTv_name = itemView.findViewById(R.id.tvName);
                tv_id = itemView.findViewById(R.id.tvReview);

                mImg = itemView.findViewById(R.id.ivImage);
            }
        }
    }

}