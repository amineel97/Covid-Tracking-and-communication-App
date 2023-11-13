package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class vaccination extends AppCompatActivity {

    Button btn_confirm;
    RadioButton r1, r2, r3;
    String name, surname, app_Date, hour, phone, VACC_userID, appointment_accepted;
    EditText MYphoneNumber;
    TextView txtFullname, appointStatus;
    private FirebaseUser user;
    private DatabaseReference Vacc_reference;
    LinearLayout appStatusViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);

        appStatusViewer = (LinearLayout)findViewById(R.id.appStatusViewer);

        appointStatus = (TextView)findViewById(R.id.appointmentStatus);

        hour = "00:00 XX";

        user = FirebaseAuth.getInstance().getCurrentUser();
        Vacc_reference = FirebaseDatabase.getInstance().getReference("vaccination_appointment");
        VACC_userID = user.getUid();




        txtFullname = (TextView)findViewById(R.id.vacc_fullname);



        Vacc_reference.child(VACC_userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Appointment appointment = snapshot.getValue(Appointment.class);




                if(appointment != null) {
                    name = (appointment.name);
                    surname = (appointment.surname);
                    appointment_accepted = (appointment.appointment_accepted);
                    txtFullname.setText("Hi! " + name + " " + surname);

                    //---------------------
                    String appointment_Date = (appointment.appointment_Date);
                    String hour = (appointment.hour);
                    //---------------


                    if(appointment.appointment_accepted == "no booked appointment!")
                    {
                        appointStatus.setText(appointment_accepted);
                    }
                    else {
                        String StatusTotal = appointment_accepted;
                        appointStatus.setText(StatusTotal);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn_confirm = (Button)findViewById(R.id.confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                CalendarView calendarView = (CalendarView)findViewById(R.id.calendarView);



                r1 = (RadioButton)findViewById(R.id.radioButton1);
                r2 = (RadioButton)findViewById(R.id.radioButton2);
                r3 = (RadioButton)findViewById(R.id.radioButton3);

                MYphoneNumber = (EditText)findViewById(R.id.phoneNumber);
                phone = MYphoneNumber.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                app_Date = sdf.format(new Date(calendarView.getDate()));

                if(r1.isChecked()){hour = "10:30 AM";}
                else {
                    if (r2.isChecked()) {
                        hour = "13:00 PM";
                    } else {
                        if (r3.isChecked()) {
                            hour = "16:30 PM";
                        }else{Toast.makeText(vaccination.this, "Please select the Hour",Toast.LENGTH_LONG).show();
                        }
                    }
                }

            //    Appointment appointment = new Appointment(name, surname, app_Date, hour, phone);




                Vacc_reference.child(VACC_userID).child("appointment_Date").setValue(app_Date);
                Vacc_reference.child(VACC_userID).child("hour").setValue(hour);
                Vacc_reference.child(VACC_userID).child("phone").setValue(phone);




                Vacc_reference.child(VACC_userID).child("appointment_accepted").setValue("pending approval");








                MYphoneNumber.setText("");












                AlertDialog alertDialog = new AlertDialog.Builder(vaccination.this).setTitle("Appointment!")
                        .setMessage("Please wait for your appointment approval")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(vaccination.this, Main_Covid_Dashboard.class);
                                startActivity(intent);

                            }
                        })


                        .setNegativeButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });




/*
        Vacc_reference.child(VACC_userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Appointment userProfileAppointment = snapshot.getValue(Appointment.class);







            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/





    }


}