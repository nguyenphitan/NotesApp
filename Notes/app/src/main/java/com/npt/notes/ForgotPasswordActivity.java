package com.npt.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView mGobackToLogin;
    private EditText mForgotPassword;
    private Button mPasswordRecover;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().hide();
        initView();

        // Click back to login
        mGobackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        // Click password recover
        mPasswordRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mForgotPassword.getText().toString().trim();
                if(mail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please, enter your email!", Toast.LENGTH_SHORT).show();
                } else {
                    // Send mail recover password
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Mail sent, you can recover password using mail", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Can't send mail.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void initView() {
        mGobackToLogin = findViewById(R.id.gobacktologin);
        mForgotPassword = findViewById(R.id.forgotpassword);
        mPasswordRecover = findViewById(R.id.passwordrecoverbutton);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}