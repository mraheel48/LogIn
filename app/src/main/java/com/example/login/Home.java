package com.example.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Home extends AppCompatActivity {

    Button btnSignOut;

    ////FirebaseAuth
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText currentPassword, newPassword, confirmNewPassword;

    CardView cardUpdatePassword;

    String oldPassword = null;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mAuth = FirebaseAuth.getInstance();

        //get progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        progressDialog.setCancelable(false);

        btnSignOut = findViewById(R.id.btnSignOut);

        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        cardUpdatePassword = findViewById(R.id.cardUpdatePassword);


        btnSignOut.setOnClickListener(view -> {

            mAuth.signOut();
            startActivity(new Intent(Home.this, Login.class));
            finish();
        });

        cardUpdatePassword.setOnClickListener(view -> {

            progressDialog.show();

            if (!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())) {

                newPassword.setError("Password not same");
                newPassword.setError("Password not same");
                newPassword.requestFocus();
                newPassword.requestFocus();

                progressDialog.dismiss();
                return;

            }

            if (oldPassword.equals(currentPassword.getText().toString())) {

                mAuth.getCurrentUser().updatePassword(newPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    // progressDialog.dismiss();

                                    HashMap<String, Object> data = new HashMap<>();
                                    data.put("password", newPassword.getText().toString().trim());

                                    db.collection("users").document(mAuth.getCurrentUser().getUid())
                                            .update(data)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();

                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(Home.this, "Your password is update", Toast.LENGTH_SHORT).show();
                                                        currentPassword.setText("");
                                                        newPassword.setText("");
                                                        confirmNewPassword.setText("");

                                                        confirmNewPassword.setFocusable(false);
                                                        newPassword.setFocusable(false);
                                                        confirmNewPassword.setFocusable(false);

                                                    }
                                                }
                                            });

                                } else {

                                    progressDialog.dismiss();
                                    Log.e("myTag", task.getException().getMessage());
                                }
                            }
                        });


            } else {

                Toast.makeText(this, "Current password is wrong ", Toast.LENGTH_SHORT).show();
                currentPassword.setError("Password not same");
                currentPassword.requestFocus();

                progressDialog.dismiss();
                return;
            }


        });

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    oldPassword = documentSnapshot.getString("password");
                    //Toast.makeText(Home.this, oldPassword, Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                e.printStackTrace();
            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

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