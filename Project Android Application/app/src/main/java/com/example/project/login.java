package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    
    EditText email_login_TXT, password_login_TXT;
    Button signIn_button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email_login_TXT = (EditText)findViewById(R.id.email_login);
        password_login_TXT = (EditText)findViewById(R.id.password_login);

        signIn_button = (Button)findViewById(R.id.signin_button);
        signIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }


    private void Login(){
        String email = email_login_TXT.getText().toString().trim();
        String password = password_login_TXT.getText().toString().trim();

        if(email.isEmpty())
        {
            email_login_TXT.setError("Email is required!");
            email_login_TXT.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_login_TXT.setError("Please enter valid email!");
            email_login_TXT.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            password_login_TXT.setError("Password is required!");
            password_login_TXT.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            password_login_TXT.setError("Min password length should be 6 characters!");
            password_login_TXT.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // to user profile
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        SharedPreferences loginPreferences = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = loginPreferences.edit();

                        editor.putString("LOGIN_email", email);
                        editor.putString("LOGIN_password", password);
                        editor.apply();


                        Intent intent  = new Intent(login.this, Main_Covid_Dashboard.class);
                        startActivity(intent);

                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(login.this, "Check your email and verify your account!",Toast.LENGTH_LONG).show();}

                }else{ Toast.makeText(login.this, "Login Failed! Please try again",Toast.LENGTH_LONG).show(); }
            }
        });


    }
}