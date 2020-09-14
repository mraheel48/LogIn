package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Screen extends AppCompatActivity {

    ImageView imgWelcome;

    ////FirebaseAuth
    FirebaseAuth mAuth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);


        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();


        imgWelcome = findViewById(R.id.imgWelcome);


        // Animation myanim = AnimationUtils.loadAnimation(Splash_Screen.this, R.anim.mytransition);

        //imgWelcome.setAnimation(myanim);

        //imgWelcome.animate().alpha(1f).setDuration(2000);

        Thread thread = new Thread() {

            @Override
            public void run() {
                super.run();

                try {

                    imgWelcome.animate().alpha(1f).setDuration(2000);

                    sleep(2000);


                } catch (Exception ex) {

                    ex.printStackTrace();

                } finally {

                    ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo info = cm.getActiveNetworkInfo();

                    if (info != null) {

                        if (info.getType() == ConnectivityManager.TYPE_WIFI) {

                            if (user != null) {

                                startActivity(new Intent(Splash_Screen.this, Home.class));
                                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            } else {
                                startActivity(new Intent(Splash_Screen.this, Login.class));
                                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }

                            finish();

                        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {


                          /*  if (user != null) {
                                // User is signed in
                            } else {
                                // No user is signed in
                            }*/


                            if (user != null) {

                                startActivity(new Intent(Splash_Screen.this, Home.class));
                                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();

                            } else {
                                startActivity(new Intent(Splash_Screen.this, Login.class));
                                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            }


                            // Toast.makeText(Splash_Screen.this, "Your your connect to mobile network", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        // Toast.makeText(Splash_Screen.this, "Your not Connect ", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(Splash_Screen.this, NetWorkConnect.class));
                        finish();
                    }


                }
            }
        };
        thread.start();

        /*imgWelcome.setOnClickListener(view -> {

            //finish();
            startActivity(new Intent(Splash_Screen.this, Login.class));
            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });*/


    }
}
