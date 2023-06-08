package co.fatweb.com.wedding.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import co.fatweb.com.wedding.Connectivity.RestClient;
import co.fatweb.com.wedding.DataObject.User;
import co.fatweb.com.wedding.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class SignIn_act extends AppCompatActivity {

    TextView txt_lostpass, txt_register;
    EditText edt_username;
    ShowHidePasswordEditText edt_passwrd;
    Button btn_signin, btn_sign_out;
    LinearLayout back;
    ProgressDialog dialog;
    /*GMAIL CODE*/
    int RC_SIGN_IN = 0;
    Button signInButton, fb;
    GoogleSignInClient mGoogleSignInClient;

    /*FACEBOOK LOGIN*/
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    String gmail_personId, gmail_personName, gmail_personGivenName, personFamilyName,
            gmail_personEmail, fb_first_name, fb_last_name, fb_email, fb_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_act);
        txt_lostpass = (TextView) findViewById(R.id.txt_lostpass);
        txt_register = (TextView) findViewById(R.id.txt_register);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_passwrd = findViewById(R.id.etPassword);
        btn_signin = (Button) findViewById(R.id.btn_login);
        btn_sign_out = (Button) findViewById(R.id.log_out);
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Navigation_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                attemptLogin();

            }
        });
        txt_lostpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPasswrd_act.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register_act.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

         getHashkey();
      /*GMAIL CODE*/

        //Initializing Views
        signInButton = findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

 signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        /*FACEBOOK LOGIN*/

        fb = (Button) findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == fb) {
                    loginButton.performClick();
                }
            }
        });

       /* public void onClickFacebookButton(View view) {
            if (view == fb) {
                loginButton.performClick();
            }
        }*/

        FacebookSdk.sdkInitialize(SignIn_act.this);
        //FacebookSdk.sdkInitialize(SignIn_act);
        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        checkLoginStatus();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                checkLoginStatus();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    @SuppressLint("LongLogTag")
    public void getHashkey() {
        try {

            PackageInfo info = getPackageManager().getPackageInfo("myjobspace.admin.onestoptrade",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));//will give developer key hash
                Log.i("RELESSE_KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.i("ABC:","fgdfgfd");
                // Toast.makeText(getApplicationContext(),Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_LONG).show(); //will give app key hash or release key hash
// 31:89:68:D6:ED:9A:34:8E:1E:57:EF:EF:74:25:D7:8B:B4:0A:72:9E
                byte[] sha1 = {
                        0x31, (byte) 0x89, 0x68, (byte) 0xD6, (byte) 0xED, (byte) 0x9A, 0x34, (byte) 0x8E, (byte) 0x1E, (byte) 0x57, (byte) 0xEF, (byte) 0xEF, 0x74, 0x25, (byte) 0xD7, (byte) 0x8B, (byte) 0xB4, 0x0A, (byte) 0x72, (byte) 0x9E
                };
                Log.d("keyhashGooglePlaySignIn:", Base64.encodeToString(sha1, Base64.NO_WRAP));


            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void attemptLogin() {

        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_passwrd = findViewById(R.id.etPassword);

        String username = edt_username.getText().toString().trim();
        String password = edt_passwrd.getText().toString().trim();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            ((EditText) findViewById(R.id.etPassword)).setError("This field is required");
            focusView = edt_passwrd;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            ((EditText) findViewById(R.id.edt_username)).setError("This field is required");
            focusView = edt_username;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            new UserLogin(username, password).execute();

        }
    }

    public class UserLogin extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        Boolean success = false;
        User newUser = new User();

        String responseString;

        UserLogin(String username, String password) {
            mUsername = username;
            mPassword = password;
            newUser.setEmailAddress(mUsername);
            newUser.setPassword(mPassword);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SignIn_act.this);
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
            // pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();
                params.put("email", newUser.getEmailAddress());
                params.put("password", newUser.getPassword());
                String str_url=  SignIn_act.this.getString(R.string.url_live);
                String normalUrl = str_url+"users/login";
               // String normalUrl = "https://wedding.geek.fatweb.co.nz/api/users/login";
                android.util.Log.i("login", normalUrl + "?" + params.toString());


                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", newUser.getEmailAddress());
                jObject.accumulate("password", newUser.getPassword());

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(SignIn_act.this, normalUrl, entity, new JsonHttpResponseHandler() {
                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        success = false;
                        responseString = response;
                        android.util.Log.d("response", responseString.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        success = false;
                        //                       responseString = errorResponse.toString();
                        android.util.Log.d("response", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        android.util.Log.d("response", errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                        android.util.Log.d("response", responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        android.util.Log.i("responseString", responseString);
                        success = true;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        android.util.Log.i("JSONObject response", response.toString());
                        try {
                            //success = true;
                            if (response.has("status")) {
                                if (response.getString("status").contains("fail")) {
                                    success = false;
                                } else {
                                    success = true;

                                }

                                newUser = new Gson().fromJson(response.toString(), User.class);

                            } else {
                                success = false;

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
        protected void onPostExecute(final Boolean success) {
            dialog.cancel();
            //pbLoading.setVisibility(View.GONE);
            if (success) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn_act.this);
                /* builder.setTitle("Login successful");*/
                builder.setMessage(newUser.getMessage());
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = newUser.getUsername();
                        String passwrd = newUser.getPassword();
                        String email = newUser.getEmailAddress();
                        String firstname = newUser.getFirstname();
                        String lastname = newUser.getLastname();
                        String userid = newUser.getId();

                        // Toast.makeText(SignIn_act.this, "" + username + passwrd + email + firstname, Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("username", username);
                        editor.putString("password", passwrd);
                        editor.putString("email", email);
                        editor.putString("firstname", firstname);
                        editor.putString("id", userid);

                        editor.putString("lastname", lastname);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), Navigation_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        edt_username.setText("");
                        edt_passwrd.setText("");
                        /* if (checkVersion(newUser.getAndroidVersion())) {*/
                        // navigate();
                        // }
                    }
                });
                builder.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn_act.this);
                builder.setTitle("Login error!");
                builder.setMessage(newUser.getMessage());
               /* Toast.makeText(Home_act.this, "login : " +
                        ""+newUser.getMessage(), Toast.LENGTH_SHORT).show();*/
                // builder.setMessage(newUser.getMessage());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        }


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            gmailDetails();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(SignIn_act.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void gmailDetails() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SignIn_act.this);
        if (acct != null) {
            gmail_personName = acct.getDisplayName();
            gmail_personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            gmail_personEmail = acct.getEmail();
            gmail_personId = acct.getId();
            String gmail_token = acct.getIdToken();
            Uri personPhoto = acct.getPhotoUrl();
            Log.d("HMAIL DETAIL", gmail_personName + "\n" + gmail_personGivenName + "\n" + gmail_personEmail + "\n" + gmail_personId + "\n" + gmail_token);
            new GmailApi().execute();
        } else {
            btn_sign_out.setVisibility(View.VISIBLE);
            btn_sign_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // signOut();
                }
            });
        }
    }

    /* private void signOut() {
         mGoogleSignInClient.signOut()
                 .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         Toast.makeText(SignIn_act.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(SignIn_act.this, SignIn_act.class));
                         finish();
                     }
                 });
     }
 */
    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            gmailDetails();
            //startActivity(new Intent(SignIn_act.this, ProfileGmail.class));
        }
        super.onStart();
    }

    /*FACEBOOK LOGIN CODE*/

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                /*txtName.setText("");
                txtEmail.setText("");
                circleImageView.setImageResource(0);*/
                Log.d("FB DETAIL", "USER NOT LOGGED");
                Toast.makeText(SignIn_act.this, "User Logged out", Toast.LENGTH_LONG).

                        show();
            } else
                loadUserProfile(currentAccessToken);
        }
    };

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    fb_first_name = object.getString("first_name");
                    fb_last_name = object.getString("last_name");
                    fb_email = object.getString("email");
                    fb_id = object.getString("id");
                    //fb_username=object.getString("")
//                    String token=object.getString("token");

                    String image_url = "https://graph.facebook.com/" + fb_id + "/picture?type=normal";
                    Log.d("FACEBOOK DETAIL", fb_email + " " + fb_first_name + " " + image_url + "\n" + fb_id + "\n");
                    /*txtEmail.setText(email);
                    txtName.setText(first_name +" "+last_name);*/
                   new FacebookApi().execute();
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    //   Glide.with(SignIn_act.this).load(image_url).into(circleImageView);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }


    /*GMAIL API*/

    public class GmailApi extends AsyncTask<Void, Void, Boolean> {

        /* private final String mUsername;
         private final String nemail;
         private final String msocial;
         private final String mgmail;
         private final String mname;
         private final String mnickname;
         private final String musername;*/
        Boolean success = false;
        User newUser = new User();

        String responseString;

        /*GmailApi(String email, String socail, String gmail_id, String name, String nickname, String username) {
            mUsername = username;
            nemail = email;
            newUser.setEmailAddress(mUsername);
            newUser.setPassword(mPassword);*/


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SignIn_act.this);
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
            // pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();
                params.put("email", gmail_personEmail);
                params.put("social", "google");
                params.put("identifier_id", gmail_personId);
                params.put("name", gmail_personName);
                params.put("nickname", "");
                params.put("username", gmail_personGivenName);

                String str_url=  SignIn_act.this.getString(R.string.url_live);
                String normalUrl = str_url+"users/login_social";

               // String normalUrl = "https://onestoptrade.co.nz/api/users/login_social";
                android.util.Log.i("login", normalUrl + "?" + params.toString());


                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", gmail_personEmail);
                jObject.accumulate("social", "google");
                jObject.accumulate("identifier_id", gmail_personId);
                jObject.accumulate("name", gmail_personName);
                jObject.accumulate("nickname", "");
                jObject.accumulate("username", gmail_personGivenName);

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(SignIn_act.this, normalUrl, entity, new JsonHttpResponseHandler() {
                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        success = false;
                        responseString = response;
                        android.util.Log.d("response", responseString.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        success = false;
                        //                       responseString = errorResponse.toString();
                        android.util.Log.d("response", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        android.util.Log.d("response", errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                        android.util.Log.d("response", responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        android.util.Log.i("responseString", responseString);
                        success = true;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        android.util.Log.i("JSONObject response", response.toString());
                        try {
                            //success = true;
                            if (response.has("status")) {
                                if (response.getString("status").contains("fail")) {
                                    success = false;
                                } else {
                                    success = true;

                                }

                                newUser = new Gson().fromJson(response.toString(), User.class);

                            } else {
                                success = false;

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
        protected void onPostExecute(final Boolean success) {
            dialog.cancel();
            //pbLoading.setVisibility(View.GONE);
            if (success) {

                /*AlertDialog.Builder builder = new AlertDialog.Builder(SignIn_act.this);
                 *//* builder.setTitle("Login successful");*//*
                builder.setMessage(newUser.getMessage());
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {*/
                String username = newUser.getUsername();
                String passwrd = newUser.getPassword();
                String email = newUser.getEmailAddress();
                String firstname = newUser.getFirstname();
                String lastname = newUser.getLastname();
                String userid = newUser.getId();

                // Toast.makeText(SignIn_act.this, "" + username + passwrd + email + firstname, Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("username", username);
                editor.putString("password", passwrd);
                editor.putString("email", email);
                editor.putString("firstname", firstname);
                editor.putString("id", userid);

                editor.putString("lastname", lastname);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), Navigation_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                edt_username.setText("");
                edt_passwrd.setText("");
                /* if (checkVersion(newUser.getAndroidVersion())) {*/
                // navigate();
                // }
//                    }
//                });
//                builder.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn_act.this);
                builder.setTitle("Login error!");
                builder.setMessage(newUser.getMessage());
               /* Toast.makeText(Home_act.this, "login : " +
                        ""+newUser.getMessage(), Toast.LENGTH_SHORT).show();*/
                // builder.setMessage(newUser.getMessage());
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        }


    }

    /*FACEBOOK API */

    public class FacebookApi extends AsyncTask<Void, Void, Boolean> {

        /* private final String mUsername;
         private final String nemail;
         private final String msocial;
         private final String mgmail;
         private final String mname;
         private final String mnickname;
         private final String musername;*/
        Boolean success = false;
        User newUser = new User();

        String responseString;

        /*GmailApi(String email, String socail, String gmail_id, String name, String nickname, String username) {
            mUsername = username;
            nemail = email;
            newUser.setEmailAddress(mUsername);
            newUser.setPassword(mPassword);*/


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog = new ProgressDialog(SignIn_act.this);
                dialog.setMessage("Loading, please wait");
                dialog.show();
                dialog.setCancelable(false);            }
            catch (WindowManager.BadTokenException e) {
                //use a log message
            }

            // pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();
                params.put("email", fb_email);
                params.put("social", "fb");
                params.put("identifier_id", fb_id);
                params.put("name", fb_first_name + fb_last_name);
                params.put("nickname", "");
                params.put("username", "");
                String str_url=  SignIn_act.this.getString(R.string.url_live);
                String normalUrl = str_url+"users/login_social";

                //String normalUrl = "https://onestoptrade.co.nz/api/users/login_social";
                android.util.Log.i("login", normalUrl + "\n" + params.toString());


                JSONObject jObject = new JSONObject();
                jObject.accumulate("email", fb_email);
                jObject.accumulate("social", "fb");
                jObject.accumulate("identifier_id", fb_id);
                jObject.accumulate("name", fb_first_name);
                jObject.accumulate("nickname", "");
                jObject.accumulate("username", "");

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(SignIn_act.this, normalUrl, entity, new JsonHttpResponseHandler() {
                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        success = false;
                        responseString = response;
                        android.util.Log.d("response", responseString.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        success = false;
                        //                       responseString = errorResponse.toString();
                        android.util.Log.d("response", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        android.util.Log.d("response", errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                        android.util.Log.d("response", responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        android.util.Log.i("responseString", responseString);
                        success = true;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        android.util.Log.i("JSONObject response", response.toString());
                        try {
                            //success = true;
                            if (response.has("status")) {
                                if (response.getString("status").contains("fail")) {
                                    success = false;
                                } else {
                                    success = true;

                                }

                                newUser = new Gson().fromJson(response.toString(), User.class);

                            } else {
                                success = false;

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
        protected void onPostExecute(final Boolean success) {
            try {
                dialog.cancel();


                //pbLoading.setVisibility(View.GONE);
                if (success) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignIn_act.this);
                    //  builder.setTitle("Login successful");
                    builder.setMessage(newUser.getMessage());
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String username = newUser.getUsername();
                            String passwrd = newUser.getPassword();
                            String email = newUser.getEmailAddress();
                            String firstname = newUser.getFirstname();
                            String lastname = newUser.getLastname();
                            String userid = newUser.getId();

                            // Toast.makeText(SignIn_act.this, "" + username + passwrd + email + firstname, Toast.LENGTH_SHORT).show();
                            SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("username", username);
                            editor.putString("password", passwrd);
                            editor.putString("email", email);
                            editor.putString("firstname", firstname);
                            editor.putString("id", userid);

                            editor.putString("lastname", lastname);
                            editor.apply();
                            Intent intent = new Intent(getApplicationContext(), Navigation_activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            edt_username.setText("");
                            edt_passwrd.setText("");
                            /* if (checkVersion(newUser.getAndroidVersion())) {*/
                            // navigate();
                            // }
                        }
                    });
                    builder.show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignIn_act.this);
                    builder.setTitle("Login error!");
                    builder.setMessage(newUser.getMessage());
               /* Toast.makeText(Home_act.this, "login : " +
                        ""+newUser.getMessage(), Toast.LENGTH_SHORT).show();*/
                    // builder.setMessage(newUser.getMessage());
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();

                }
            }
            catch (WindowManager.BadTokenException e) {
                //use a log message
            }
        }


    }


}
