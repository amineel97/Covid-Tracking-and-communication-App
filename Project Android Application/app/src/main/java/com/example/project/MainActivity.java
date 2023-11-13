package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button createAccount, signin_BTN;
    TextView GuestBrowse;
    private FirebaseAuth mAuth;
    LottieAnimationView mainLoading;
    LinearLayout main_sign_in_reg_buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences loginPreferences = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        String email = loginPreferences.getString("LOGIN_email", null);
        String password = loginPreferences.getString("LOGIN_password", null);

        main_sign_in_reg_buttons = (LinearLayout)findViewById(R.id.sign_in_reg_buttons);

        main_sign_in_reg_buttons.setVisibility(View.GONE);
        main_sign_in_reg_buttons.setEnabled(false);

        mainLoading = (LottieAnimationView)findViewById(R.id.lottie_animation_loading_main);
        mainLoading.playAnimation();

        if(email != null && password != null)
        {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        // to user profile

                            Intent intent  = new Intent(MainActivity.this, Main_Covid_Dashboard.class);
                            startActivity(intent);
                            mainLoading.cancelAnimation();

                    }else{ Toast.makeText(MainActivity.this, "Login Failed! Please try again",Toast.LENGTH_LONG).show(); }
                }
            });
        }else{
            main_sign_in_reg_buttons.setVisibility(View.VISIBLE);
            main_sign_in_reg_buttons.setEnabled(true);
        }




        GuestBrowse = (TextView) findViewById(R.id.guestbrowse);
        GuestBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        createAccount = (Button)findViewById(R.id.create_account_button);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });

        signin_BTN = (Button)findViewById(R.id.signin_button);
        signin_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });

    }
}