package com.example.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResetPassword extends AppCompatActivity {

    CardView cardReset;
    EditText edEmail;

    //FirebaseAuth
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();

        //get progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        progressDialog.setCancelable(false);

        edEmail = findViewById(R.id.edEmail);
        cardReset = findViewById(R.id.cardReset);

        cardReset.setOnClickListener(view -> {

            progressDialog.show();

            if (edEmail.getText().toString().isEmpty()) {
                edEmail.setError("Required Field...");
                progressDialog.dismiss();
                return;
            }

            mAuth.sendPasswordResetEmail(edEmail.getText().toString().trim().toLowerCase())
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            Toast.makeText(ResetPassword.this, "Password send your Email plz check your email and sign in again!", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ResetPassword.this, "Some thing is wrong plz try later!", Toast.LENGTH_SHORT).show();
                            Log.e("myTag", task.getException().getMessage());
                            finish();
                        }
                    });

        });
    }
}