package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetAddress;

public class contact_us extends AppCompatActivity {

    Button btnSend;
    EditText edit_to_send;
    TextView TextMainChat;

    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;
    String uFull_name = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        btnSend = (Button) findViewById(R.id.button_send);
        edit_to_send = (EditText) findViewById(R.id.text_to_send);
        TextMainChat = (TextView) findViewById(R.id.main_text_chat);

        TextMainChat.setMovementMethod(new ScrollingMovementMethod());

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Contact_Box");
        userID = user.getUid();

        try {
            loadMessageMain();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reference.child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                //    Toast.makeText(contact_us.this, "added", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //     Toast.makeText(contact_us.this, "changed", Toast.LENGTH_LONG).show();
                try {
                    loadMessageMain();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                refreshTextView();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //    Toast.makeText(contact_us.this, "removed", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //    Toast.makeText(contact_us.this, "moved", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //    Toast.makeText(contact_us.this, "cancel", Toast.LENGTH_LONG).show();

            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MessageTXT = TextMainChat.getText().toString() + "\n \n" + " - " + uFull_name + " : " + edit_to_send.getText().toString();
                reference.child(userID).child("Message").setValue(MessageTXT)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(contact_us.this, "Error Message Submission!!", Toast.LENGTH_LONG).show();

                            }
                        });

                edit_to_send.setText("");
                refreshTextView();
            }
        });

        refreshTextView();
    }

    private void loadMessageMain() throws IOException, InterruptedException {
        if (isConnected())
        {
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    contact_Message cMessage = snapshot.getValue(contact_Message.class);

                    while (true) {
                        if (cMessage != null) {
                            uFull_name = (cMessage.Full_name);
                        }

                        String Message = (cMessage.Message);

                        TextMainChat.setText(Message);


                        if (TextMainChat.getText() != "") {

                            break;

                        }
                    }


                    refreshTextView();

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }else{
            Toast.makeText(contact_us.this, "no internet", Toast.LENGTH_LONG).show();

        }

        refreshTextView();

    }


    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    private void refreshTextView()
    {


      //  int y = (TextMainChat.getLineCount() - 1) * TextMainChat.getLineHeight(); // the " - 5" should send it to the TOP of the last line, instead of the bottom of the last line
      //  int visible = TextMainChat.getHeight() - (2* TextMainChat.getLineHeight());
      //  if( visible < y ) y -= visible;
     //   TextMainChat.scrollTo(0, y);



    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshTextView();
    }
}