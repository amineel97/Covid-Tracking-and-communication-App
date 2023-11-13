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

public class check_advices extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;

    TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, TXT_UPDATE_TITLE, UpdateTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_advices);


        TXT_UPDATE_TITLE = (TextView)findViewById(R.id.textViewUpdates);
        UpdateTXT = (TextView)findViewById(R.id.textViewUpdateTXT);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("updates");
       // userID = user.getUid();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updates UP = snapshot.getValue(updates.class);

                if(UP != null)
                {
                    TXT_UPDATE_TITLE.setText(UP.Title);
                    UpdateTXT.setText(UP.Info);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











        txt1 = (TextView)findViewById(R.id.textView1);
        txt2 = (TextView)findViewById(R.id.textView2);
        txt3 = (TextView)findViewById(R.id.textView3);
        txt4 = (TextView)findViewById(R.id.textView4);
        txt5 = (TextView)findViewById(R.id.textView5);
        txt6 = (TextView)findViewById(R.id.textView6);
        txt7 = (TextView)findViewById(R.id.textView7);
        txt8 = (TextView)findViewById(R.id.textView8);

        txt1.setText("If you are not fully vaccinated and aged 2 or older, you should wear a mask in indoor public places.\n\nIn general, you do not need to wear a mask in outdoor settings. \n In areas with high numbers of COVID-19 cases, consider wearing a mask in crowded outdoor settings and for activities with close contact with others who are not fully vaccinated.\n\nIf you are fully vaccinated and have a condition or are taking medications that weaken your immune system, you may need to keep taking steps to protect yourself, like wearing a mask. Talk to your healthcare provider about steps you can take to manage your health and risks.");
        txt2.setText("Inside your home: Avoid close contact with people who are sick.\n" +
                "If possible, maintain 6 feet between the person who is sick and other household members.\n" +
                "Outside your home: Put 6 feet of distance between yourself and people who don’t live in your household.\n" +
                "Remember that some people without symptoms may be able to spread virus.\n" +
                "Stay at least 6 feet (about 2 arm lengths) from other people.\n" +
                "Keeping distance from others is especially important for people who are at higher risk of getting very sick.");
        txt3.setText("Authorized COVID-19 vaccines can help protect you from COVID-19.\n" +
                "You should get a COVID-19 vaccine when it is available to you.\n" +
                "Once you are fully vaccinated, you may be able to start doing some things that you had stopped doing because of the pandemic.");
        txt4.setText("Being in crowds like in restaurants, bars, fitness centers, or movie theaters puts you at higher risk for COVID-19.\n" +
                "Avoid indoor spaces that do not offer fresh air from the outdoors as much as possible.\n" +
                "If indoors, bring in fresh air by opening windows and doors, if possible.");
        txt5.setText("Wash your hands often with soap and water for at least 20 seconds especially after you have been in a public place, or after blowing your nose, coughing, or sneezing.\n" +
                "It’s especially important to wash:\n" +
                "Before eating or preparing food\n" +
                "Before touching your face\n" +
                "After using the restroom\n" +
                "After leaving a public place\n" +
                "After blowing your nose, coughing, or sneezing\n" +
                "After handling your mask\n" +
                "After changing a diaper\n" +
                "After caring for someone sick\n" +
                "After touching animals or pets\n" +
                "If soap and water are not readily available, use a hand sanitizer that contains at least 60% alcohol. Cover all surfaces of your hands and rub them together until they feel dry.\n" +
                "Avoid touching your eyes, nose, and mouth with unwashed hands.");

        txt6.setText("If you are wearing a mask: You can cough or sneeze into your mask. Put on a new, clean mask as soon as possible and wash your hands.\n" +
                "If you are not wearing a mask:\n" +
                "Always cover your mouth and nose with a tissue when you cough or sneeze, or use the inside of your elbow and do not spit.\n" +
                "Throw used tissues in the trash.\n" +
                "Immediately wash your hands with soap and water for at least 20 seconds. If soap and water are not readily available, clean your hands with a hand sanitizer that contains at least 60% alcohol.");

        txt7.setText("Clean high touch surfaces daily. This includes tables, doorknobs, light switches, countertops, handles, desks, phones, keyboards, toilets, faucets, and sinks.\n" +
                "If someone is sick or has tested positive for COVID-19, disinfect frequently touched surfaces. Use a household disinfectant product from EPA’s List N: Disinfectants for Coronavirus (COVID-19)external icon according to manufacturer’s labeled directions.\n" +
                "If surfaces are dirty, clean them using detergent or soap and water prior to disinfection.");

        txt8.setText("Be alert for symptoms. Watch for fever, cough, shortness of breath, or other symptoms of COVID-19.\n" +
                "Especially important if you are running essential errands, going into the office or workplace, and in settings where it may be difficult to keep a physical distance of 6 feet.\n" +
                "Take your temperature if symptoms develop.\n" +
                "Don’t take your temperature within 30 minutes of exercising or after taking medications that could lower your temperature, like acetaminophen.\n" +
                "Follow CDC guidance if symptoms develop.");
    }










}