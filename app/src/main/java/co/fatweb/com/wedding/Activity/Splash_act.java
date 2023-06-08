package co.fatweb.com.wedding.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import co.fatweb.com.wedding.R;
import co.fatweb.com.wedding.utils.NetworkUtil;

public class Splash_act extends AppCompatActivity {

    ImageView imgSplash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_act);
        imgSplash = (ImageView) findViewById(R.id.imgSplash);
        if (NetworkUtil.getConnectivityStatusString(getApplicationContext()).equals("unavailable")) {
            Toast toast = Toast.makeText(Splash_act.this, "Internet Connection Not Available!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        } else {
            Thread timer = new Thread() {
                public void run() {
                    try {
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
                        imgSplash.startAnimation(animation);
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent i = new Intent(Splash_act.this, Navigation_activity.class);
                        startActivity(i);
                        finish();
                        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    }
                }
            };
            timer.start();
        }

    }}

