package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class currentStatus extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;

    TextView TXTstatus, TXT_days_quarantine, TXTmain_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_status);

        TXTstatus = (TextView)findViewById(R.id.status);
        TXT_days_quarantine = (TextView)findViewById(R.id.days_quarantine);
        TXTmain_info = (TextView)findViewById(R.id.main_info);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User_Status");
        userID = user.getUid();


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserStatus USTS = snapshot.getValue(UserStatus.class);

                if(USTS != null)
                {

                        TXTstatus.setText(USTS.Current_status);

                        TXT_days_quarantine.setText(USTS.Quarantine_days);

                        TXTmain_info.setText(USTS.Reminder_info);





                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}