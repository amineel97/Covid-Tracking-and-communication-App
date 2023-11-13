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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main_Covid_Dashboard extends AppCompatActivity {

    TextView TXT_new_cases, TXT_death_cases, TXT_recovered_cases, TXT_today_country, user_fullnameTXT, user_country_TXT;
    PieChart mPieChart;
    String country = null;
    Context context = this;
    private MapView mapView;

    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    private static final String TAG = Main_Covid_Dashboard.class.getSimpleName();

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    boolean isCalled=false;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userID;
    LinearLayout main_covid_info;
    LottieAnimationView lottie__data, lottie_map;

    MapMarker UsermapMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_main_dashboard);

        mapView = findViewById(R.id.mapViewMain);
        mapView.onCreate(savedInstanceState);

        user_fullnameTXT = (TextView)findViewById(R.id.user_fullname);
        user_country_TXT = (TextView)findViewById(R.id.user_country);


        mPieChart = (PieChart) findViewById(R.id.pie_chart);
        TXT_new_cases = (TextView)findViewById(R.id.new_cases);
        TXT_death_cases = (TextView)findViewById(R.id.death_cases);
        TXT_recovered_cases = (TextView)findViewById(R.id.recovered_cases);
        TXT_today_country = (TextView)findViewById(R.id.today_country);


        main_covid_info = (LinearLayout)findViewById(R.id.main_card_covid);
        main_covid_info.setVisibility(View.GONE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        lottie__data = (LottieAnimationView)findViewById(R.id.lottie_animation_loading_data);
        lottie__data.playAnimation();
        lottie__data.setVisibility(View.VISIBLE);

        lottie_map = (LottieAnimationView)findViewById(R.id.lottie_animation_map);
        lottie_map.playAnimation();
        lottie_map.setVisibility(View.VISIBLE);

        mapView.setVisibility(View.GONE);

      //  ImageView img_QRcode;

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  User userProfile = snapshot.getValue(User.class);


                  if(userProfile != null)
                  {
                      String name = (userProfile.name).toLowerCase();
                      String surname = (userProfile.surname).toLowerCase();
                      String UserCountry = (userProfile.country).toLowerCase();

                      country = UserCountry;
                      user_fullnameTXT.setText("hi! " + name + " " + surname);

                      user_country_TXT.setText(UserCountry);

                      while (true)
                      {
                          if(country != null)
                          {

                              lottie__data.cancelAnimation();
                              lottie__data.setVisibility(View.GONE);
                              main_covid_info.setVisibility(View.VISIBLE);

                              lottie_map.cancelAnimation();
                              lottie_map.setVisibility(View.GONE);
                              mapView.setVisibility(View.VISIBLE);

                              loadData();
                              break;
                          }

                      }

                  }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        LinearLayout BTN = (LinearLayout)findViewById(R.id.button_tst);
        BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main_Covid_Dashboard.this, currentStatus.class);
                startActivity(i);
            }
        });


        LinearLayout WorldUpdate = (LinearLayout)findViewById(R.id.world_update);
        WorldUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main_Covid_Dashboard.this, AffectedCountries.class);
                startActivity(i);
            }
        });


        LinearLayout contact_us = (LinearLayout)findViewById(R.id.contact_us);
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main_Covid_Dashboard.this, contact_us.class);
                startActivity(i);
            }
        });


        LinearLayout vaccination = (LinearLayout)findViewById(R.id.vaccination);
        vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main_Covid_Dashboard.this, vaccination.class);
                startActivity(i);
            }
        });

        LinearLayout reportCase = (LinearLayout)findViewById(R.id.reportCase);
        reportCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main_Covid_Dashboard.this, report_covid_case.class);
                startActivity(i);
            }
        });


        LinearLayout howToProtect = (LinearLayout)findViewById(R.id.howToProtect);
        howToProtect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main_Covid_Dashboard.this, check_advices.class);
                startActivity(i);
            }
        });

        LinearLayout LogoutBTN = (LinearLayout)findViewById(R.id.LogoutButton);
        LogoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finishAffinity();

            }
        });


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


    private void loadMapScene() {
        // Load a scene from the HERE SDK to render the map with a map scheme.
        mapView.getMapScene().loadScene(MapScheme.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapError mapError) {
                if (mapError == null) {
                    double distanceInMeters = 1000 * 10;
                    mapView.getCamera().lookAt(
                            new GeoCoordinates( mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()  ), distanceInMeters);


                    GeoCoordinates geoCoordinates = new GeoCoordinates( mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    //--------add user location-----------
                    MapImage UserMapImage = MapImageFactory.fromResource(context.getResources(), R.drawable.user_pin);
                    UsermapMarker = new MapMarker(geoCoordinates, UserMapImage);

                    mapView.getMapScene().addMapMarker(UsermapMarker);
                    //----------------------------------

                    stopLocationUpdates();


                } else {
                    //    Log.d(TAG, "Loading map failed: mapError: " + mapError.name());
                }
            }
        });
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

                      //  Toast.makeText(getApplicationContext(), "Map updated!", Toast.LENGTH_SHORT).show();

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
                                    rae.startResolutionForResult(Main_Covid_Dashboard.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(Main_Covid_Dashboard.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }


    private void updateLocationUI() {
        if (mCurrentLocation != null) {
       //     txtLocationResult.setText("Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude());

            loadMapScene();

        }

      //  toggleButtons();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
         //   startLocationUpdates();
        }

        init();

        startLocation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

    private void loadData()
    {
        String url = "https://disease.sh/v3/covid-19/countries/" + country;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            mPieChart.startAnimation();

                            JSONObject jsonObject = new JSONObject(response.toString());

                            String todayCases = jsonObject.getString("todayCases");
                            String todayDeaths = jsonObject.getString("todayDeaths");
                            String todayRecovered = jsonObject.getString("todayRecovered");

                            TXT_today_country.setText("today in " + jsonObject.getString("country").toLowerCase());
                            TXT_new_cases.setText("new cases " + todayCases);
                            TXT_death_cases.setText("deaths " + todayDeaths);
                            TXT_recovered_cases.setText("recovered " + todayRecovered);

                            mPieChart.addPieSlice(new PieModel("new cases", Integer.parseInt(todayCases), Color.parseColor("#FFD000")));
                            mPieChart.addPieSlice(new PieModel("death", Integer.parseInt(todayDeaths), Color.parseColor("#D9183B")));
                            mPieChart.addPieSlice(new PieModel("recovered", Integer.parseInt(todayRecovered), Color.parseColor("#29FF9F")));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Main_Covid_Dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);






    }


    @Override
    public void onBackPressed(){

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Exit!")
                .setMessage("Are you sure to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                })


                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

}