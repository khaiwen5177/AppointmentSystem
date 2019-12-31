package com.example.navigationdrawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppointmentStep3 extends AppCompatActivity {

    Button mConfirmBtn;
    TextView mAppointmentDetail;
    TextView mTesting;
    SimpleDateFormat simpleDateFormat;

    public static long pos;
    public static String date;
    public static String serviceLocation;
    public static String strMap;
    public static String remark = "";
    public static long num;
    private String timeStr;
    private String numStr;
    private String uidStr;
    private String strCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_step3);
        getSupportActionBar().setTitle("Appointment");

        numStr = String.format("%03d", num);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uidStr = user.getUid();

        mAppointmentDetail = findViewById(R.id.textView);
        mConfirmBtn = findViewById(R.id.confirmButton);
        mTesting = findViewById(R.id.testing);

        if((pos == 0) || (pos==1) || (pos==2) || (pos==3)){
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
        }

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy HH.mm.ss");
        Date currentTime = Calendar.getInstance().getTime();
        strCurrentDate = simpleDateFormat.format(currentTime);

        mAppointmentDetail.setText("Date : " + date + "\nNo : "+numStr + "\nTime : " + timeStr + "\nService Location : " + serviceLocation + "\nRemark : " + remark);

        mTesting.setText(strMap);

        mConfirmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AppointmentProfile appointmentProfile = new AppointmentProfile(numStr,date,pos,remark,strMap,
                        "Pending","","",uidStr,strCurrentDate);
                FirebaseDatabase.getInstance().getReference("Appointments")
                        .child(date)
                        .child(numStr)
                        .setValue(appointmentProfile);





                AlertDialog alertDialog = new AlertDialog.Builder(AppointmentStep3.this).create();
                alertDialog.setTitle("Notice");
                alertDialog.setMessage("Appointment has been pending.\nPlease wait our admin to contact you for confirmation.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                                pos = 0;
                                date = "";
                                serviceLocation = "";
                                remark = "";
                                num = 0;
                                timeStr = "";
                                numStr = "";
                                strMap ="";
                                strCurrentDate = "";
                                startActivity(new Intent(AppointmentStep3.this, MainActivity.class));
                            }
                        });
                alertDialog.show();

            }
        });
    }
}
