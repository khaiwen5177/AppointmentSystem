package com.example.navigationdrawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScheduleStep3 extends AppCompatActivity {

    CheckBox mc1;
    CheckBox mc2;
    CheckBox mc3;
    CheckBox mc4;
    Spinner mSpinnerWorker;
    Spinner mSpinnerStatus;

    Button mUpdateBtn;

    //private List<String> mServices = new ArrayList<>();

    private String strC1 = "Diagnostic and Troubleshoot\n";
    private String strC2 = "";
    private String strC3 = "";
    private String strC4 = "";
    private String strAllServices = "";
    private String strWorker;
    private String strStatus;

    public static AppointmentProfile appointmentProfile;
    private FirebaseDatabase firebaseDatabase;
    private String strTime;
    private long pos;
    private int choice;

    TextView mRecordDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_step3);
        getSupportActionBar().setTitle("Schedule");

        mRecordDetail = findViewById(R.id.text_record_detail);
        mc1 = findViewById(R.id.c1);
        mc2 = findViewById(R.id.c2);
        mc3 = findViewById(R.id.c3);
        mc4 = findViewById(R.id.c4);
        mSpinnerWorker = findViewById(R.id.spinnerWorker);
        mSpinnerStatus = findViewById(R.id.spinnerStatus);
        mUpdateBtn = findViewById(R.id.updateButton);

        if(appointmentProfile == null){
            Toast.makeText(ScheduleStep3.this, "Sorry, Invalid Record Number, Please Try Again", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(ScheduleStep3.this, ScheduleStep2.class));
        }else {
            //Toast.makeText(ScheduleStep3.this, "Hi HIIHIH : " + appointmentProfile.aptNum, Toast.LENGTH_LONG).show();
            if(appointmentProfile.getAptLocation().equals("https://www.google.com/maps/search/?api=1&query=4.840149,100.745052")){
                appointmentProfile.setAptLocation("In-Workshop Service");
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


            mRecordDetail.append("\nDate :" + appointmentProfile.getAptDate() + "\nNo : " + appointmentProfile.getAptNum() + "\nTime : " + strTime + "\nLocation : " + appointmentProfile.getAptLocation() + "\nRemark : " + appointmentProfile.getAptRemark()  + "\n");

            mc1.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(mc1.isChecked())
                    {
                        //Toast.makeText(ScheduleStep3.this,"First checkbox checked", Toast.LENGTH_SHORT).show();
                        strC1 = "Diagnostic and Troubleshoot\n";
                    }
                    else
                    {
                        //Toast.makeText(ScheduleStep3.this,"First checkbox Unchecked", Toast.LENGTH_SHORT).show();
                        strC1 = "";
                    }

                }
            });

            mc2.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(mc2.isChecked())
                    {
                        //Toast.makeText(ScheduleStep3.this,"Second checkbox checked", Toast.LENGTH_SHORT).show();
                        strC2 = "Maintenance Service\n";
                    }
                    else
                    {
                        //Toast.makeText(ScheduleStep3.this,"Second checkbox Unchecked", Toast.LENGTH_SHORT).show();
                        strC2 = "";
                    }

                }
            });

            mc3.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(mc3.isChecked())
                    {
                        //Toast.makeText(ScheduleStep3.this,"Third checkbox checked", Toast.LENGTH_SHORT).show();
                        strC3 = "Breaking System\n";
                    }
                    else
                    {
                        //Toast.makeText(ScheduleStep3.this,"Third checkbox Unchecked", Toast.LENGTH_SHORT).show();
                        strC3 = "";
                    }

                }
            });
            mc4.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(mc4.isChecked())
                    {
                        //Toast.makeText(ScheduleStep3.this,"Fourth checkbox checked", Toast.LENGTH_SHORT).show();
                        strC4 = "Suspension System\n";
                    }
                    else
                    {
                        //Toast.makeText(ScheduleStep3.this,"Fourth checkbox Unchecked", Toast.LENGTH_SHORT).show();
                        strC4 = "";
                    }

                }
            });

        }

        mSpinnerWorker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position == 0){
                    choice = 0;
                    strWorker = "Johnson Ooi";
                }
                if(position == 1){
                    choice = 1;
                    strWorker = "Ali";
                }
                if(position == 2){
                    choice = 2;
                    strWorker = "Darren Lim";
                }
                if(position == 3){
                    choice = 3;
                    strWorker = "Faiz";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                strWorker = "Johnson Ooi";
            }

        });


        mSpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position == 0){
                    strStatus = "Pending";
                }
                if(position == 1){
                    strStatus = "Accepted";
                }
                if(position == 2){
                    strStatus = "Cancelled";
                }
                if(position == 3){
                    strStatus = "Completed";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                strStatus = "Pending";
            }

        });


        //firebaseDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + appointmentProfile.getAptDate()+"/"+appointmentProfile.getAptNum());


        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strAllServices = strC1 + strC2 + strC3 + strC4;
                //Toast.makeText(ScheduleStep3.this,"Hi this is : " + strAllServices + "\n" + strWorker + "\n" + strStatus,Toast.LENGTH_LONG).show();

                appointmentProfile.setServices(strAllServices);
                appointmentProfile.setWorker(strWorker);
                appointmentProfile.setStatus(strStatus);

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                MainActivity.displayCount = MainActivity.displayCount - 1;
                                FirebaseDatabase.getInstance().getReference("Appointments")
                                        .child(appointmentProfile.getAptDate())
                                        .child(appointmentProfile.getAptNum())
                                        .setValue(appointmentProfile);

                                Toast.makeText(ScheduleStep3.this,"Record successfully updated", Toast.LENGTH_SHORT).show();
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                return;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleStep3.this);
                builder.setMessage("Are you sure to update?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


    }


}
