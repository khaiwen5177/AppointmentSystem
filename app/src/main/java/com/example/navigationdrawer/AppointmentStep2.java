package com.example.navigationdrawer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class AppointmentStep2 extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_REQUEST_INT = 177;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    EditText mRemark;
    Button mNextBtn;
    Spinner mSpinner;
    //TextView mAppointmentDetail;
    //ProgressBar progressBar;
    public static int pos;
    public static String date;
    //private String serviceLocation;
    private int choice;
    private String strLat;
    private String strLon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_step2);
        getSupportActionBar().setTitle("Appointment");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mRemark = findViewById(R.id.remark);
        //progressBar = findViewById(R.id.progressBar2);
        mNextBtn = findViewById(R.id.nextButton);
        mSpinner = findViewById(R.id.spinner);
        //mAppointmentDetail = findViewById(R.id.textView2);

        mRemark.setHint("Leave a remark regarding your appointment");

        AlertDialog alertDialog = new AlertDialog.Builder(AppointmentStep2.this).create();
        alertDialog.setTitle("Notice");
        alertDialog.setMessage("Please select SERVICE LOCATION");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NEXT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        /*if((pos == 0) || (pos==1) || (pos==2) || (pos==3)){
            timeStr = "9:00am-11:00am";
        }
        else if((pos == 4) || (pos==5) || (pos==6) || (pos==7)){
            timeStr = "11:00am-1:00pm";
        }
        else if((pos == 8) || (pos==9) || (pos==10) || (pos==11)){
            timeStr = "2:00pm-4:00pm";
        }
        else
          {
            timeStr = "4:00pm-6:00pm";
        }*/

        //mAppointmentDetail.setText("Date: " + date + "\nTime: "+timeStr);
        //mAppointmentDetail.setGravity(Gravity.CENTER);
        mRemark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position == 0){
                    choice = 0;
                    AppointmentStep3.serviceLocation = "In-Workshop Service";
                    fetchLastLocation();
                }
                if(position == 1){
                    choice = 1;
                    AppointmentStep3.serviceLocation = "On-Location Service";
                    fetchLastLocation();
                }
            }





            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                AppointmentStep3.serviceLocation = "In-Workshop Service";
                fetchLastLocation();
            }

        });
        mNextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AppointmentStep3.pos = pos;
                AppointmentStep3.date = date;
                AppointmentStep3.remark = mRemark.getText().toString().trim();
                finish();
                startActivity(new Intent(AppointmentStep2.this, AppointmentStep3.class));
            }
        });

    }

    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(AppointmentStep2.this);
                }
            }
        });

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng;
        if(choice == 0){
            latLng = new LatLng(4.840149,100.745052);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("We are Here");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera((CameraUpdateFactory.newLatLngZoom(latLng,13)));
            googleMap.addMarker(markerOptions);
            strLat = "4.840149";
            strLon = "100.745052";
            AppointmentStep3.strMap = "https://www.google.com/maps/search/?api=1&query=" + strLat + "," + strLon;
        }
        else if(choice == 1) {
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are Here");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera((CameraUpdateFactory.newLatLngZoom(latLng, 13)));
            googleMap.addMarker(markerOptions);
            strLat = String.valueOf(currentLocation.getLatitude());
            strLon = String.valueOf(currentLocation.getLongitude());
            AppointmentStep3.strMap = "https://www.google.com/maps/search/?api=1&query=" + strLat + "," + strLon;
        }
        else{
            latLng = new LatLng(4.840149,100.745052);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("We are Here");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera((CameraUpdateFactory.newLatLngZoom(latLng,13)));
            googleMap.addMarker(markerOptions);
            strLat = "4.840149";
            strLon = "100.745052";
            AppointmentStep3.strMap = "https://www.google.com/maps/search/?api=1&query=" + strLat + "," + strLon;
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

}
