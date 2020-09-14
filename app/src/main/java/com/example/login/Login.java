package com.example.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    EditText edUserName, edPassword;
    CardView cardLogin;
    TextView tvForget, tvSignUp, textView4;

    ImageView imageView;
    TextInputLayout textInputLayout, username_text_input_layout;

    //FirebaseAuth
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //get progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        progressDialog.setCancelable(false);

        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);

        cardLogin = findViewById(R.id.cardLogin);

        tvForget = findViewById(R.id.tvForget);
        tvSignUp = findViewById(R.id.tvSignUp);
        textView4 = findViewById(R.id.textView4);
        imageView = findViewById(R.id.imageView);

        textInputLayout = findViewById(R.id.textInputLayout);
        username_text_input_layout = findViewById(R.id.username_text_input_layout);

       /* Thread th = new Thread() {

            @Override
            public void run() {
                super.run();

                try {

                    imageView.animate().alpha(1f).setDuration(2000);
                    textInputLayout.animate().alpha(1f).setDuration(2500);
                    username_text_input_layout.animate().alpha(1f).setDuration(2800);
                    tvForget.animate().alpha(1f).setDuration(3100);
                    cardLogin.animate().alpha(1f).setDuration(3400);
                    tvSignUp.animate().alpha(1f).setDuration(3700);
                    textView4.animate().alpha(1f).setDuration(3700);

                    sleep(3000);

                } catch (Exception ex) {

                    ex.printStackTrace();

                }
            }
        };

        th.start();*/

        cardLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edUserName.getText().length() > 0) {

                    if (edPassword.getText().length() > 0) {

                        progressDialog.show();

                        /* Toast.makeText(Login.this, "calling login", Toast.LENGTH_SHORT).show();*/

                        mAuth.signInWithEmailAndPassword(edUserName.getText().toString().trim().toLowerCase(), edPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {

                                            progressDialog.dismiss();

                                            if (mAuth.getCurrentUser().isEmailVerified()) {

                                                HashMap<String, Object> data = new HashMap<>();
                                                data.put("userVerification", "true");
                                                data.put("password", edPassword.getText().toString().trim());

                                                db.collection("users").document(mAuth.getCurrentUser().getUid())
                                                        .update(data)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {

                                                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(Login.this, Home.class));
                                                                    finish();

                                                                } else {

                                                                    Log.e("myTag", task.getException().toString());
                                                                }
                                                            }
                                                        });


                                            } else {

                                                Toast.makeText(Login.this, "Your Email address not Verify", Toast.LENGTH_SHORT).show();
                                                mAuth.signOut();
                                            }


                                        } else {

                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, "Login Successful plz check your email or password ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {

                        edPassword.setError("Password must be enter");
                    }

                } else {

                    edUserName.setError("Email must be enter");
                }
            }
        });

        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(Login.this, "Calling forget password function", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this,ResetPassword.class));

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(Login.this, "Calling new User Created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, SignUp.class));

            }
        });

    }

    //on back press button code
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //Toast.makeText(this, "You Click the back button", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

        builder.setCancelable(false);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //super.onBackPressed();
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
