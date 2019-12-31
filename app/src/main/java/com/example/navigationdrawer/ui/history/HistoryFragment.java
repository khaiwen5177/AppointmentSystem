package com.example.navigationdrawer.ui.history;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.navigationdrawer.AppointmentProfile;
import com.example.navigationdrawer.R;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HistoryFragment extends Fragment{

    TextView mHistory;
    //SimpleDateFormat simpleDateFormat;
    Calendar currentDate;
    private String strDate1;
    private String strDate2;
    private String strDate3;
    private FirebaseDatabase firebaseDatabase;
    private long count = 0;
    private long miliDate;

    private String strLocation;
    private String strTime;
    private long pos;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);

        mHistory = root.findViewById(R.id.history);
        currentDate = Calendar.getInstance();
        miliDate = currentDate.getTimeInMillis();
        strDate1 = getDate(miliDate, "dd_MM_yyyy");
        strDate2 = getDate(miliDate + 86400000, "dd_MM_yyyy");
        strDate3 = getDate(miliDate + 86400000 + 86400000, "dd_MM_yyyy");

        //do {
            firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + strDate1);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    count = dataSnapshot.getChildrenCount();


                    for (int i = 1; i <= count; i++) {
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + strDate1 + "/" + String.format("%03d", i));

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                AppointmentProfile appointmentProfile = dataSnapshot.getValue(AppointmentProfile.class);
                                if(appointmentProfile.getUserUid().equals(user.getUid())) {
                                    if(appointmentProfile.getAptLocation().equals("https://www.google.com/maps/search/?api=1&query=4.840149,100.745052")){
                                        strLocation = "In-Workshop Service";
                                    }else{
                                        strLocation = "On-Location Service";
                                    }

                                    pos = appointmentProfile.getAptSlot();

                                    if((pos == 0) || (pos==1) || (pos==2) || (pos==3)){
                                        strTime = "9:00am-11:00am";
                                    }
                                    else if((pos == 4) || (pos==5) || (pos==6) || (pos==7)){
                                        strTime = "11:00am-1:00pm";
                                    }
                                    else if((pos == 8) || (pos==9) || (pos==10) || (pos==11)){
                                        strTime = "2:00pm-4:00pm";
                                    }
                                    else
                                    {
                                        strTime = "4:00pm-6:00pm";
                                    }


                                    if(strLocation.equals("In-Workshop Service")) {
                                        mHistory.append("\nDate :" + appointmentProfile.getAptDate() + "\nNo : " + appointmentProfile.getAptNum() + "\nTime : " + strTime + "\nLocation : " + strLocation + "\nRemark : " + appointmentProfile.getAptRemark() + "\nStatus : " + appointmentProfile.getStatus() + "\nOur location : " + appointmentProfile.getAptLocation() + "\n");
                                    }else {
                                        mHistory.append("\nDate :" + appointmentProfile.getAptDate() + "\nNo : " + appointmentProfile.getAptNum() + "\nTime : " + strTime + "\nLocation : " + strLocation + "\nRemark : " + appointmentProfile.getAptRemark() + "\nStatus : " + appointmentProfile.getStatus() + "\nYour location : " + appointmentProfile.getAptLocation() + "\n");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        databaseReference = firebaseDatabase.getReference("Appointments/" + strDate2);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();


                for (int i = 1; i <= count; i++) {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + strDate2 + "/" + String.format("%03d", i));

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AppointmentProfile appointmentProfile = dataSnapshot.getValue(AppointmentProfile.class);
                            if(appointmentProfile.getUserUid().equals(user.getUid())) {
                                if(appointmentProfile.getAptLocation().equals("https://www.google.com/maps/search/?api=1&query=4.840149,100.745052")){
                                    strLocation = "In-Workshop Service";
                                }else{
                                    strLocation = "On-Location Service";
                                }


                                pos = appointmentProfile.getAptSlot();

                                if((pos == 0) || (pos==1) || (pos==2) || (pos==3)){
                                    strTime = "9:00am-11:00am";
                                }
                                else if((pos == 4) || (pos==5) || (pos==6) || (pos==7)){
                                    strTime = "11:00am-1:00pm";
                                }
                                else if((pos == 8) || (pos==9) || (pos==10) || (pos==11)){
                                    strTime = "2:00pm-4:00pm";
                                }
                                else
                                {
                                    strTime = "4:00pm-6:00pm";
                                }

                                if(strLocation.equals("In-Workshop Service")) {
                                    mHistory.append("\nDate :" + appointmentProfile.getAptDate() + "\nNo : " + appointmentProfile.getAptNum() + "\nTime : " + strTime + "\nLocation : " + strLocation + "\nRemark : " + appointmentProfile.getAptRemark() + "\nStatus : " + appointmentProfile.getStatus() + "\nOur location : " + appointmentProfile.getAptLocation() + "\n");
                                }else {
                                    mHistory.append("\nDate :" + appointmentProfile.getAptDate() + "\nNo : " + appointmentProfile.getAptNum() + "\nTime : " + strTime + "\nLocation : " + strLocation + "\nRemark : " + appointmentProfile.getAptRemark() + "\nStatus : " + appointmentProfile.getStatus() + "\nYour location : " + appointmentProfile.getAptLocation() + "\n");
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = firebaseDatabase.getReference("Appointments/" + strDate3);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();


                for (int i = 1; i <= count; i++) {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + strDate3 + "/" + String.format("%03d", i));

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AppointmentProfile appointmentProfile = dataSnapshot.getValue(AppointmentProfile.class);
                            if(appointmentProfile.getUserUid().equals(user.getUid())) {
                                if(appointmentProfile.getAptLocation().equals("https://www.google.com/maps/search/?api=1&query=4.840149,100.745052")){
                                    strLocation = "In-Workshop Service";
                                }else{
                                    strLocation = "On-Location Service";
                                }

                                pos = appointmentProfile.getAptSlot();

                                if((pos == 0) || (pos==1) || (pos==2) || (pos==3)){
                                    strTime = "9:00am-11:00am";
                                }
                                else if((pos == 4) || (pos==5) || (pos==6) || (pos==7)){
                                    strTime = "11:00am-1:00pm";
                                }
                                else if((pos == 8) || (pos==9) || (pos==10) || (pos==11)){
                                    strTime = "2:00pm-4:00pm";
                                }
                                else
                                {
                                    strTime = "4:00pm-6:00pm";
                                }

                                if(strLocation.equals("In-Workshop Service")) {
                                    mHistory.append("\nDate :" + appointmentProfile.getAptDate() + "\nNo : " + appointmentProfile.getAptNum() + "\nTime : " + strTime + "\nLocation : " + strLocation + "\nRemark : " + appointmentProfile.getAptRemark() + "\nStatus : " + appointmentProfile.getStatus() + "\nOur location : " + appointmentProfile.getAptLocation() + "\n");
                                }else {
                                    mHistory.append("\nDate :" + appointmentProfile.getAptDate() + "\nNo : " + appointmentProfile.getAptNum() + "\nTime : " + strTime + "\nLocation : " + strLocation + "\nRemark : " + appointmentProfile.getAptRemark() + "\nStatus : " + appointmentProfile.getStatus() + "\nYour location : " + appointmentProfile.getAptLocation() + "\n");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //}while(dayCount < 3);


       /* firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/05_12_2019/001");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppointmentProfile appointmentProfile = dataSnapshot.getValue(AppointmentProfile.class);
                mHistory.setText(appointmentProfile.getUserEmail() + "\n" + miliDate + "\n" + getDate(miliDate, "dd_MM_yyyy")+"\n"+getDate(miliDate+86400000, "dd_MM_yyyy"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        return root;

    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }




}