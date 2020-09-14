package com.example.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    CardView cardSave;

    EditText edFName, edLname, edEmail, edFPassword, edLPassword;

    ////FirebaseAuth
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //get progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();

        //find id of the view
        cardSave = findViewById(R.id.cardView);
        edFName = findViewById(R.id.edFirstName);
        edLname = findViewById(R.id.edLastName);
        edEmail = findViewById(R.id.edEmail);
        edFPassword = findViewById(R.id.edFirstPassWord);
        edLPassword = findViewById(R.id.edLastPassWord);

        //Click the Save button 
        cardSave.setOnClickListener(v -> {

            //Toast.makeText(SignUp.this, "Click the Save button", Toast.LENGTH_SHORT).show();

            if (edFName.getText().toString().isEmpty()) {

                edFName.setError("Required Field...");
                return;
            }

            if (edLname.getText().toString().isEmpty()) {
                edLname.setError("Required Field...");
                return;

            }

            if (edEmail.getText().toString().isEmpty()) {

                edEmail.setError("Required Field...");
                return;
            }

            if (edFPassword.getText().toString().isEmpty()) {

                edFPassword.setError("Required Field...");
                return;
            }

            if (edLPassword.getText().toString().isEmpty()) {

                edLPassword.setError("Required Field...");
                return;
            }

            if (!edFPassword.getText().toString().equals(edLPassword.getText().toString())) {

                edFPassword.setError("Password not same");
                edLPassword.setError("Password not same");
                edFPassword.requestFocus();
                edLPassword.requestFocus();
                return;

            }


            if (edFPassword.getText().toString().equals(edLPassword.getText().toString())) {


                progressDialog.show();

                email = edEmail.getText().toString().trim().toLowerCase();
                password = edFPassword.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email, password)

                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {


                                    mAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {

                                                    if (task.isSuccessful()) {

                                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                                    @SuppressLint("ShowToast")
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                                                                        if (task.isSuccessful()) {

                                                                            String token = task.getResult().getToken();

                                                                            HashMap<String, Object> data = new HashMap<>();
                                                                            data.put("FName", edFName.getText().toString().trim());
                                                                            data.put("LName", edLname.getText().toString().trim());
                                                                            data.put("email", email);
                                                                            data.put("password", password);
                                                                            data.put("userVerification", "false");
                                                                            data.put("uid", mAuth.getCurrentUser().getUid());
                                                                            data.put("token", token);

                                                                            db.collection("users").document(mAuth.getCurrentUser().getUid())
                                                                                    .set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    if (task.isSuccessful()) {

                                                                                        mAuth.getCurrentUser().sendEmailVerification()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                                        if (task.isSuccessful()) {

                                                                                                            progressDialog.dismiss();
                                                                                                            Toast.makeText(SignUp.this, "Check your Email address for Verification", Toast.LENGTH_SHORT).show();
                                                                                                            mAuth.signOut();
                                                                                                            //startActivity(new Intent(SignUp.this, Login.class));
                                                                                                            finish();

                                                                                                        } else {

                                                                                                            Log.e("myTag", task.getException().toString());
                                                                                                        }

                                                                                                    }
                                                                                                });

                                                                                    } else {

                                                                                        Log.e("myTag", task.getException().toString());
                                                                                        progressDialog.dismiss();

                                                                                        Toast.makeText(SignUp.this, "User data can't upload in firestore",
                                                                                                Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                }
                                                                            });

                                                                        } else {

                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                                                                        }
                                                                    }
                                                                });


                                                    } else {

                                                        progressDialog.dismiss();
                                                        Toast.makeText(SignUp.this, "Login Successful plz check your email or password ", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                } else {

                                    progressDialog.dismiss();

                                    //Log.e("myTag",task.toString());

                                    Toast.makeText(SignUp.this, "Authentication failed user email already create",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }


        });

    }
}
