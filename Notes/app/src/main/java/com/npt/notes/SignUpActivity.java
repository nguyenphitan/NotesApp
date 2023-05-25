package com.npt.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText mSignupEmail, mSignupPassword;
    private RelativeLayout mSignup;
    private TextView mGoToLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        initView();

        // Click go to login
        mGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        // Click sign up
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mSignupEmail.getText().toString().trim();
                String password = mSignupPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty())  {
                    Toast.makeText(getApplicationContext(), "Please, enter email and password!", Toast.LENGTH_SHORT).show();
                } else if(email.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Please, enter password larger than 8 character!", Toast.LENGTH_SHORT).show();
                } else {
                    // Register to firebase
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                                sendMailVerification();
                            } else {
                                Toast.makeText(getApplicationContext(), "Fail to register!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }

    // Send mail verify
    private void sendMailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Verification email is sent, Verify and Login again!", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Fail to send verification email.", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        mSignupEmail = findViewById(R.id.signupemail);
        mSignupPassword = findViewById(R.id.signuppassword);
        mSignup = findViewById(R.id.signup);
        mGoToLogin = findViewById(R.id.gotologin);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}