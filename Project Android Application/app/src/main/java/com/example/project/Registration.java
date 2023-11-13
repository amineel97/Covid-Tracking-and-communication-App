package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {

    Calendar myCalendar;
    EditText surnameEditTXT, nameEditTXT, emailEditTXT, birthdayTXT, passwordEditTXT, passwordEditValidTXT;
    DatePickerDialog.OnDateSetListener date;
    CountryCodePicker myCcp;
    Button createAccountButton;
    String full_name, message, NewMsg;

    private FirebaseAuth mAuth;

    String name, surname, email, birthday, password, passwordValidation, country, hour, phone, appointment_Date, appointment_accepted, current_status, quarantine_days, reminder_info;
    String REP_name, REP_surname, Latitude, Longitude, FamilyMember, CaseStatus, CaseInfo, startingDate, Info, newUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        nameEditTXT = (EditText)findViewById(R.id.nameEdit);
        surnameEditTXT = (EditText)findViewById(R.id.surnameEdit);
        emailEditTXT = (EditText)findViewById(R.id.emailEdit);
        birthdayTXT= (EditText) findViewById(R.id.birthday_text);
        passwordEditTXT = (EditText) findViewById(R.id.passwordEdit);
        passwordEditValidTXT = (EditText) findViewById(R.id.passwordValidEdit);
        myCcp = (CountryCodePicker)findViewById(R.id.countrycodepickerEdit);


        createAccountButton = (Button)findViewById(R.id.create_account_button);


        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        birthdayTXT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Registration.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataValidation();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthdayTXT.setText(sdf.format(myCalendar.getTime()));
    }

    private void dataValidation(){

        name = nameEditTXT.getText().toString().trim();
        surname = surnameEditTXT.getText().toString().trim();
        email = emailEditTXT.getText().toString().trim();
        birthday = birthdayTXT.getText().toString().trim();
        password = passwordEditTXT.getText().toString().trim();
        passwordValidation = passwordEditValidTXT.getText().toString().trim();
        country = myCcp.getSelectedCountryName().trim();




        if(name.isEmpty())
        {
            nameEditTXT.setError("Name is required!");
            nameEditTXT.requestFocus();
            return;
        }

        if(surname.isEmpty())
        {
            surnameEditTXT.setError("Surname is required!");
            surnameEditTXT.requestFocus();
            return;
        }

        if(email.isEmpty())
        {
            emailEditTXT.setError("Email is required!");
            emailEditTXT.requestFocus();
            return;
        }

        if(birthday.isEmpty())
        {
            birthdayTXT.setError("Birthday is required!");
            birthdayTXT.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditTXT.setError("Please enter valid email!");
            emailEditTXT.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            passwordEditTXT.setError("Password is required!");
            passwordEditTXT.requestFocus();
            return;
        }

        if(passwordValidation.isEmpty())
        {
            passwordEditValidTXT.setError("Password is required!");
            passwordEditValidTXT.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            passwordEditTXT.setError("Min password length should be 6 characters!");
            passwordEditTXT.requestFocus();
            return;
        }

        if(!passwordEditTXT.getText().toString().equals(passwordEditValidTXT.getText().toString()))
        {
            passwordEditValidTXT.setError("Please verify your password!");
            passwordEditValidTXT.requestFocus();
            return;
        }


       mAuth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           User user = new User(name, surname, email, birthday, country);

                           FirebaseDatabase.getInstance().getReference("Users")
                                   .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                   .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful())
                                   {
                                       Toast.makeText(Registration.this, "User has been registered successfully!",Toast.LENGTH_LONG).show();
                                       full_name = user.name + " " + user.surname;

                                       message = "---------------------";

                                       contactCreation();
                                       vaccination_Appointment();
                                       UserStatus();
                                       reportCase();

                                   }
                                   else
                                       {
                                           Toast.makeText(Registration.this, "Failed to Register!",Toast.LENGTH_LONG).show();
                                       }

                               }

                           });
                       }else
                       {
                           Toast.makeText(Registration.this, "Failed to Register!",Toast.LENGTH_LONG).show();
                       }
                   }
               });


    }

    private void contactCreation()
    {
        NewMsg = "No";
        contact_Message MSG = new contact_Message(full_name, message, NewMsg);

        FirebaseDatabase.getInstance().getReference("Contact_Box")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(MSG).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                //    Toast.makeText(Registration.this, "Contact Box well created",Toast.LENGTH_LONG).show();

                }

                else
                {
                    Toast.makeText(Registration.this, "Error Contact Box creation!",Toast.LENGTH_LONG).show();
                }

            }

        });
    }



    private void vaccination_Appointment()
    {
        appointment_Date = "00/00/0000";
        hour = "00:00";
        phone = "+4800000000";
        appointment_accepted = "no booked appointment";
        Appointment appointment = new Appointment(name, surname, appointment_Date, hour, phone, appointment_accepted);

        FirebaseDatabase.getInstance().getReference("vaccination_appointment")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
             //       Toast.makeText(Registration.this, "Appointments Box well created",Toast.LENGTH_LONG).show();
                }

                else
                {
                    Toast.makeText(Registration.this, "Error Appointments Box creation!",Toast.LENGTH_LONG).show();
                }

            }

        });
    }






    private void UserStatus()
    {

        current_status = "No Quarantine";
        quarantine_days = "0";
        reminder_info = "...";
        startingDate = "00/00/0000";

        UserStatus UST = new UserStatus(name, surname, current_status, quarantine_days, startingDate, reminder_info);

        FirebaseDatabase.getInstance().getReference("User_Status")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(UST).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Registration.this, "User Status Box well created",Toast.LENGTH_LONG).show();

                }

                else
                {
                    Toast.makeText(Registration.this, "Error User Status Box creation!",Toast.LENGTH_LONG).show();
                }

            }

        });
    }



    private void reportCase()
    {
        Latitude = "lat";
        Longitude = "long";
        FamilyMember = "none";
        CaseStatus = "none";
        CaseInfo = "no data";

        RepCaseInfo repCaseInfo = new RepCaseInfo(name, surname, Latitude, Longitude, FamilyMember, CaseStatus, CaseInfo);

        FirebaseDatabase.getInstance().getReference("ReportedCase")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(repCaseInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Registration.this, "Appointments Box well created",Toast.LENGTH_LONG).show();
                }

                else
                {
                    Toast.makeText(Registration.this, "Error Appointments Box creation!",Toast.LENGTH_LONG).show();
                }

            }

        });
    }

}