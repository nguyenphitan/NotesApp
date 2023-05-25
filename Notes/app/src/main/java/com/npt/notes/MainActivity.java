package com.npt.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mLoginEmail, mLoginPassword;
    private RelativeLayout mLogin, mGoToSignUp;
    private TextView mForgotPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBarMainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        initView();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }

        // Click goto sign up
        mGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });


        // Click forgot password
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });


        // Click login
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmail.getText().toString().trim();
                String password = mLoginPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please, enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // login

                    progressBarMainActivity.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                checkMailVerification();
                            } else {
                                Toast.makeText(getApplicationContext(), "Account doesn't exist!", Toast.LENGTH_SHORT).show();
                                progressBarMainActivity.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }

            }
        });

    }

    private void checkMailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()) {
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        } else {
            progressBarMainActivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify your email first!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    private void initView() {
        mLoginEmail = findViewById(R.id.loginemail);
        mLoginPassword = findViewById(R.id.loginpassword);
        mLogin = findViewById(R.id.login);
        mGoToSignUp = findViewById(R.id.gotosignup);
        mForgotPassword = findViewById(R.id.gotoforgotpassword);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBarMainActivity = findViewById(R.id.progressbarofmainactivity);
    }
}