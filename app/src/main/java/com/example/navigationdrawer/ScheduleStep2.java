package com.example.navigationdrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScheduleStep2 extends AppCompatActivity {

    EditText mRecordNumber;
    Button mNextBtn;
    TextView mTextView2;

    public static String selected_date;
    private FirebaseDatabase firebaseDatabase;
    private long count = 0;
    private RecyclerView RecordRecyclerView;
    private List<AppointmentProfile> mAppointment = new ArrayList<>();
    private long pos;
    private String strTime;
    private boolean checkRecordAvailable;
    //private String strLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_step2);
        getSupportActionBar().setTitle("Schedule");

        mRecordNumber = findViewById(R.id.recordNumber);
        mNextBtn = findViewById(R.id.nextButton);
        mTextView2 = findViewById(R.id.textView2);

        RecordRecyclerView = (RecyclerView) findViewById(R.id.recycler_record);
        RecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTextView2.append("("+selected_date+")");


        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + selected_date);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();
                if(count == 0){
                    Toast.makeText(ScheduleStep2.this,"No record on this day",Toast.LENGTH_LONG).show();
                    finish();
                   /* AlertDialog alertDialog = new AlertDialog.Builder(ScheduleStep2.this).create();
                    alertDialog.setTitle("Notice");
                    alertDialog.setMessage("No record on this day");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                    //startActivity(new Intent(ScheduleStep2.this, MainActivity.class));
                                }
                            });
                    alertDialog.show();*/
                }else{
                    mRecordNumber.setEnabled(true);
                }
                for (int i = 1; i <= count; i++) {
                    firebaseDatabase = FirebaseDatabase.getInstance();

                    DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + selected_date + "/" +
                            String.format("%03d", i));


                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AppointmentProfile appointmentProfile = dataSnapshot.getValue(AppointmentProfile.class);
                            if(appointmentProfile.getAptLocation().equals("https://www.google.com/maps/search/?api=1&query=4.840149,100.745052")){
                                appointmentProfile.setAptLocation("In-Workshop Service");
                            }

                            mAppointment.add(appointmentProfile);
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


        RecordRecyclerView.setAdapter(new RecordAdapter(mAppointment));

        mNextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String recordNumber = mRecordNumber.getText().toString().trim();

                if(TextUtils.isEmpty(recordNumber)){
                    mRecordNumber.setError("Record Number is Required.");
                    return;
                }


                firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + selected_date + "/"
                        + recordNumber);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AppointmentProfile appointmentProfile = dataSnapshot.getValue(AppointmentProfile.class);
                        ScheduleStep3.appointmentProfile = appointmentProfile;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                    finish();
                    startActivity(new Intent(ScheduleStep2.this, ScheduleStep3.class));


               // AppointmentStep3.pos = pos;
                //AppointmentStep3.date = date;
               // AppointmentStep3.remark = mRemark.getText().toString().trim();

            }
        });


    }
    class RecordAdapter extends  RecyclerView.Adapter<RecordViewHolder>{
        private List<AppointmentProfile> mAppointment;
        public RecordAdapter(List<AppointmentProfile> mAppointment) {
            super();
            this.mAppointment = mAppointment;
        }

        @NonNull
        @Override
        public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecordViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
            holder.bind(this.mAppointment.get(position));
            holder.setIsRecyclable(false);
        }

        @Override
        public int getItemCount() {
            return this.mAppointment.size();
        }
    }
    class RecordViewHolder extends RecyclerView.ViewHolder{

        private TextView mNumber;
        private TextView mName;
        private TextView mContact;
        private TextView mTime;
        private TextView mLocation;
        private TextView mRemark;
        private TextView mWorker;
        private TextView mServices;
        private TextView mDatetime;
        private TextView mStatus;


        public RecordViewHolder(ViewGroup container){
            super(LayoutInflater.from(ScheduleStep2.this).inflate(R.layout.layout_record,container,false));
            mNumber = itemView.findViewById(R.id.text_number);
            mName = itemView.findViewById(R.id.text_name);
            mContact = itemView.findViewById(R.id.text_contact);
            mTime = itemView.findViewById(R.id.text_time);
            mLocation = itemView.findViewById(R.id.text_location);
            mRemark = itemView.findViewById(R.id.text_remark);
            mWorker = itemView.findViewById(R.id.text_worker);
            mServices = itemView.findViewById(R.id.text_services);
            mDatetime = itemView.findViewById(R.id.text_date_time);
            mStatus = itemView.findViewById(R.id.text_status);

        }
        public void bind(AppointmentProfile appointmentProfile){

            firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + appointmentProfile.getUserUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                    mName.append(userProfile.userName);
                    mContact.append(userProfile.userPhone);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

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

            mNumber.append(appointmentProfile.getAptNum());
            mTime.append(strTime);
            mLocation.append(appointmentProfile.getAptLocation());
            mRemark.append(appointmentProfile.getAptRemark());
            mWorker.append(appointmentProfile.getWorker());
            mServices.append(appointmentProfile.getServices());
            mDatetime.append( appointmentProfile.getBookingDateTime());
            mStatus.append(appointmentProfile.getStatus());
        }
    }

}






