package com.example.project;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapview.MapError;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapScene;
import com.here.sdk.mapview.MapScheme;
import com.here.sdk.mapview.MapView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;



public class report_covid_case extends AppCompatActivity {

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final String TAG = report_covid_case.class.getSimpleName();

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    FirebaseUser repUser;
    private DatabaseReference REPreference;
    private String mLastUpdateTime;
    private  String RepUserID, name, surname, FamilyMember, CaseStatus, caseInfo;
    TextView REP_TxtPosition;

    Button buttonConfirm;
    RadioButton familyYes, familyNo, Critical, Medium, Notsure;
    EditText InfoCase;
    CheckBox REPcheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_covid_case);

        FamilyMember = "no data";
        CaseStatus = "no data";
        caseInfo = "no data";


        repUser = FirebaseAuth.getInstance().getCurrentUser();
        REPreference = FirebaseDatabase.getInstance().getReference("ReportedCase");
        RepUserID = repUser.getUid();

        REP_TxtPosition = (TextView) findViewById(R.id.TxtPosition);

        buttonConfirm = (Button) findViewById(R.id.confirm);
        familyYes = (RadioButton)findViewById(R.id.family_yes);
        familyNo = (RadioButton)findViewById(R.id.family_no);
        Critical = (RadioButton)findViewById(R.id.critical);
        Medium = (RadioButton)findViewById(R.id.medium);
        Notsure = (RadioButton)findViewById(R.id.notsure);

        InfoCase = (EditText)findViewById(R.id.info_case);
        REPcheckBox = (CheckBox)findViewById(R.id.checkBox);

        REPreference.child(RepUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RepCaseInfo repCaseInfo = snapshot.getValue(RepCaseInfo.class);

                if (repCaseInfo != null) {
                    name = (repCaseInfo.REP_name);
                    surname = (repCaseInfo.REP_surname);

                    REP_TxtPosition.setText("Hi! " + name + " " + surname);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        init();
        startLocation();



        REPcheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(familyYes.isChecked()){
                    FamilyMember = "Yes";
                }else {
                    if (familyNo.isChecked()) {
                        FamilyMember = "No";
                    } else {
                        REPcheckBox.setChecked(false);
                        Toast.makeText(report_covid_case.this, "Family member answer missing!",Toast.LENGTH_LONG).show();
                    }
                }

                if(Critical.isChecked()){ CaseStatus = "Critical"; }
                else {
                    if(Medium.isChecked()){ CaseStatus = "Medium"; }else {
                        if(Notsure.isChecked()){ CaseStatus = "Not sure"; }else {
                            REPcheckBox.setChecked(false);
                            Toast.makeText(report_covid_case.this, "Case status missing!",Toast.LENGTH_LONG).show();

                        }
                    }
                }






            }
        });



        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(familyYes.isChecked()){
                    FamilyMember = "Yes";
                }else {
                    if (familyNo.isChecked()) {
                        FamilyMember = "No";
                    } else {
                        REPcheckBox.setChecked(false);
                        Toast.makeText(report_covid_case.this, "Family member answer missing!",Toast.LENGTH_LONG).show();
                    }
                }

                if(Critical.isChecked()){ CaseStatus = "Critical"; }
                else {
                    if(Medium.isChecked()){ CaseStatus = "Medium"; }else {
                        if(Notsure.isChecked()){ CaseStatus = "Not sure"; }else {
                            REPcheckBox.setChecked(false);
                            Toast.makeText(report_covid_case.this, "Case status missing!",Toast.LENGTH_LONG).show();

                        }
                    }
                }



                if(InfoCase.getText().toString() == ""){
                    Toast.makeText(report_covid_case.this, "Please add a short description!",Toast.LENGTH_LONG).show();

                }else

                if(REPcheckBox.isChecked())
                {
                    caseInfo = InfoCase.getText().toString();

                    REPreference.child(RepUserID).child("Latitude").setValue(String.valueOf(mCurrentLocation.getLatitude()));
                    REPreference.child(RepUserID).child("Longitude").setValue(String.valueOf(mCurrentLocation.getAltitude()));
                    REPreference.child(RepUserID).child("FamilyMember").setValue(FamilyMember);
                    REPreference.child(RepUserID).child("CaseStatus").setValue(CaseStatus);
                    REPreference.child(RepUserID).child("CaseInfo").setValue(caseInfo);




                    AlertDialog alertDialog = new AlertDialog.Builder(report_covid_case.this).setTitle("Thank you!")
                            .setMessage("Thank you for the information")
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(report_covid_case.this, Main_Covid_Dashboard.class);
                                    startActivity(intent);

                                }
                            })


                            .setNegativeButton("", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();


                }else {
                    Toast.makeText(report_covid_case.this, "Please accept our conditions!",Toast.LENGTH_LONG).show();

                }


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        init();
        startLocation();
    }


    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }


    public void startLocation() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }



    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //  Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        Toast.makeText(getApplicationContext(), "Map updated!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(report_covid_case.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(report_covid_case.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }


    private void updateLocationUI() {

            if (mCurrentLocation != null) {
                REP_TxtPosition.setText("current location loaded");

                stopLocationUpdates();


            //  toggleButtons();
        }
    }

    }

