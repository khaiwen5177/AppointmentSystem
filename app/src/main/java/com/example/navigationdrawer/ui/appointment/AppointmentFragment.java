package com.example.navigationdrawer.ui.appointment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationdrawer.Adapter.MyTimeSlotAdapter;
import com.example.navigationdrawer.AppointmentStep2;
import com.example.navigationdrawer.AppointmentProfile;
import com.example.navigationdrawer.AppointmentStep3;
import com.example.navigationdrawer.Common.Common;
import com.example.navigationdrawer.Interface.ITimeSlotLoadListener;
import com.example.navigationdrawer.Model.TimeSlot;
import com.example.navigationdrawer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

import static com.example.navigationdrawer.ui.history.HistoryFragment.getDate;

public class AppointmentFragment extends Fragment implements ITimeSlotLoadListener {

    private boolean moreThanOneAppointment;
    Calendar currentDate;
    private String strDate1;
    private String strDate2;
    private String strDate3;
    private long miliDate;

    //private AppointmentViewModel appointmentViewModel;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;
    Calendar selected_date;
    Date tempDate;

    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleDateFormat1;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0); // add current date

        }
    };

    private FirebaseDatabase firebaseDatabase;
    private String aptNum = "";
    private String aptDate = "";
    private Long aptSlot;
    private String aptRemark = "";
    private String aptLocation = "";

    private long count = -1;
    private String strDate;
    public static Button mNextBtn;
    public static int pos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        checkAppointment();

        iTimeSlotLoadListener =this;
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot,new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy"); //28_03_2019
        simpleDateFormat1 = new SimpleDateFormat("HH");

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        selected_date = Calendar.getInstance();
        selected_date.add(Calendar.DATE, 0); //Init current date
        tempDate = selected_date.getTime();
        strDate = simpleDateFormat.format(tempDate);
        AppointmentStep2.date = strDate;
        List<TimeSlot> timeSlots = new ArrayList<>();

        Date currentTime = Calendar.getInstance().getTime();
        String strCurrentDate = simpleDateFormat.format(currentTime);
        String strCurrentTime = simpleDateFormat1.format(currentTime);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference0 = firebaseDatabase.getReference("Appointments/"+strDate);

        databaseReference0.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();
                AppointmentStep3.num = count + 1;

                if (strCurrentDate.equals(strDate)){
                    //iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                    if (strCurrentTime.equals("09") || strCurrentTime.equals("10")) {

                        for (long i = 0; i < 4; i++) {

                            TimeSlot timeSlot = new TimeSlot();
                            timeSlot.setSlot(i);
                            timeSlots.add(timeSlot);
                            iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                        }
                    } else if (strCurrentTime.equals("11") || strCurrentTime.equals("12") || strCurrentTime.equals("13")) {
                        for (long i = 0; i < 8; i++) {

                            TimeSlot timeSlot = new TimeSlot();
                            timeSlot.setSlot(i);
                            timeSlots.add(timeSlot);
                            iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                        }
                    } else if (strCurrentTime.equals("14") || strCurrentTime.equals("15")) {
                        for (long i = 0; i < 12; i++) {

                            TimeSlot timeSlot = new TimeSlot();
                            timeSlot.setSlot(i);
                            timeSlots.add(timeSlot);
                            iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                        }
                    } else if (strCurrentTime.equals("16") || strCurrentTime.equals("17") || strCurrentTime.equals("18") || strCurrentTime.equals("19") || strCurrentTime.equals("20") || strCurrentTime.equals("21") || strCurrentTime.equals("22") || strCurrentTime.equals("23")) {
                        for (long i = 0; i < 16; i++) {

                            TimeSlot timeSlot = new TimeSlot();
                            timeSlot.setSlot(i);
                            timeSlots.add(timeSlot);
                            iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                        }
                    }

                }

                if(count == 0) {
                    if (strCurrentDate.equals(strDate)){
                        //iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                        if (strCurrentTime.equals("09") || strCurrentTime.equals("10")) {

                            for (long i = 0; i < 4; i++) {

                                TimeSlot timeSlot = new TimeSlot();
                                timeSlot.setSlot(i);
                                timeSlots.add(timeSlot);
                                iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                            }
                        } else if (strCurrentTime.equals("11") || strCurrentTime.equals("12") || strCurrentTime.equals("13")) {
                            for (long i = 0; i < 8; i++) {

                                TimeSlot timeSlot = new TimeSlot();
                                timeSlot.setSlot(i);
                                timeSlots.add(timeSlot);
                                iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                            }
                        } else if (strCurrentTime.equals("14") || strCurrentTime.equals("15")) {
                            for (long i = 0; i < 12; i++) {

                                TimeSlot timeSlot = new TimeSlot();
                                timeSlot.setSlot(i);
                                timeSlots.add(timeSlot);
                                iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                            }
                        } else if (strCurrentTime.equals("16") || strCurrentTime.equals("17") || strCurrentTime.equals("18") || strCurrentTime.equals("19") || strCurrentTime.equals("20") || strCurrentTime.equals("21") || strCurrentTime.equals("22") || strCurrentTime.equals("23")) {
                            for (long i = 0; i < 16; i++) {

                                TimeSlot timeSlot = new TimeSlot();
                                timeSlot.setSlot(i);
                                timeSlots.add(timeSlot);
                                iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                            }
                        } else {
                            iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                        }

                }
                }
                for(int i = 1; i <= count; i++) {

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + strDate + "/" +String.format("%03d", i));

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            AppointmentProfile appointmentProfile = dataSnapshot.getValue(AppointmentProfile.class);
                            aptNum = appointmentProfile.getAptNum();
                            aptDate = appointmentProfile.getAptDate();
                            aptSlot = appointmentProfile.getAptSlot();
                            aptRemark = appointmentProfile.getAptRemark();
                            aptLocation = appointmentProfile.getAptLocation();

                            TimeSlot timeSlot = new TimeSlot();
                            timeSlot.setSlot(aptSlot);

                            timeSlots.add(timeSlot);
                            iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



/////////////////////////////////////////
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_appointment, container, false);
        mNextBtn = itemView.findViewById(R.id.nextButton);
        unbinder = ButterKnife.bind(this,itemView);

        init(itemView);


        mNextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(moreThanOneAppointment == true){
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Notice");
                    alertDialog.setMessage("You can only make one appointment before services completed\n You can call us for cancellation");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }else {
                    AppointmentStep2.pos = pos;
                    startActivity(new Intent(getContext(), AppointmentStep2.class));
                }
            }
        });


        return itemView;
    }



    private void init(View itemView) {

        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);



        //Calendar
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,2); //2 day left

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView,R.id.calendarView)
                .range(startDate,endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(selected_date.getTimeInMillis() != date.getTimeInMillis())
                {
                    mNextBtn.setEnabled(false);
                    //timeSlots.clear();
                    List<TimeSlot> timeSlotsNew = new ArrayList<>();
                    simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy"); //28_03_2019
                    selected_date = date; //This code will not load again if you seected new dat same with day selected
                    tempDate = selected_date.getTime();
                    strDate = simpleDateFormat.format(tempDate);
                    AppointmentStep2.date = strDate;

                    Date currentTime = Calendar.getInstance().getTime();
                    String strCurrentDate = simpleDateFormat.format(currentTime);
                    String strCurrentTime = simpleDateFormat1.format(currentTime);

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference0 = firebaseDatabase.getReference("Appointments/"+strDate);

                    databaseReference0.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            count = dataSnapshot.getChildrenCount();
                            AppointmentStep3.num = count + 1;

                            if (strCurrentDate.equals(strDate)){

                                if (strCurrentTime.equals("09") || strCurrentTime.equals("10")) {

                                    for (long i = 0; i < 4; i++) {

                                        TimeSlot timeSlot = new TimeSlot();
                                        timeSlot.setSlot(i);
                                        timeSlotsNew.add(timeSlot);
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlotsNew);
                                    }
                                } else if (strCurrentTime.equals("11") || strCurrentTime.equals("12") || strCurrentTime.equals("13")) {
                                    for (long i = 0; i < 8; i++) {

                                        TimeSlot timeSlot = new TimeSlot();
                                        timeSlot.setSlot(i);
                                        timeSlotsNew.add(timeSlot);
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlotsNew);
                                    }
                                } else if (strCurrentTime.equals("14") || strCurrentTime.equals("15")) {
                                    for (long i = 0; i < 12; i++) {

                                        TimeSlot timeSlot = new TimeSlot();
                                        timeSlot.setSlot(i);
                                        timeSlotsNew.add(timeSlot);
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlotsNew);
                                    }
                                } else if (strCurrentTime.equals("16") || strCurrentTime.equals("17") || strCurrentTime.equals("18") || strCurrentTime.equals("19") || strCurrentTime.equals("20") || strCurrentTime.equals("21") || strCurrentTime.equals("22") || strCurrentTime.equals("23")) {
                                    for (long i = 0; i < 16; i++) {

                                        TimeSlot timeSlot = new TimeSlot();
                                        timeSlot.setSlot(i);
                                        timeSlotsNew.add(timeSlot);
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlotsNew);
                                    }
                                }

                            }

                            if(count == 0){
                                if(strCurrentDate.equals(strDate)) {

                                    if (strCurrentTime.equals("09") || strCurrentTime.equals("10")) {

                                        for (long i = 0; i < 4; i++) {

                                            TimeSlot timeSlot = new TimeSlot();
                                            timeSlot.setSlot(i);
                                            timeSlotsNew.add(timeSlot);
                                            iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlotsNew);
                                        }
                                    } else if (strCurrentTime.equals("11") || strCurrentTime.equals("12") || strCurrentTime.equals("13")) {
                                        for (long i = 0; i < 8; i++) {

                                            TimeSlot timeSlot = new TimeSlot();
                                            timeSlot.setSlot(i);
                                            timeSlotsNew.add(timeSlot);
                                            iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlotsNew);
                                        }
                                    } else if (strCurrentTime.equals("14") || strCurrentTime.equals("15")) {
                                        for (long i = 0; i < 12; i++) {

                                            TimeSlot timeSlot = new TimeSlot();
                                            timeSlot.setSlot(i);
                                            timeSlotsNew.add(timeSlot);
                                            iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlotsNew);
                                        }
                                    } else if (strCurrentTime.equals("16") || strCurrentTime.equals("17")  || strCurrentTime.equals("18")  || strCurrentTime.equals("19")  || strCurrentTime.equals("20") || strCurrentTime.equals("21")  || strCurrentTime.equals("22") || strCurrentTime.equals("23")){
                                        for (long i = 0; i < 16; i++) {

                                            TimeSlot timeSlot = new TimeSlot();
                                            timeSlot.setSlot(i);
                                            timeSlotsNew.add(timeSlot);
                                            iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlotsNew);
                                        }
                                    }
                                }
                                else{
                                    iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                }
                            }

                            for(int i = 1; i <= count; i++) {

                                firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + strDate + "/" +String.format("%03d", i));

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        AppointmentProfile appointmentProfile = dataSnapshot.getValue(AppointmentProfile.class);
                                        aptNum = appointmentProfile.getAptNum();
                                        aptDate = appointmentProfile.getAptDate();
                                        aptSlot = appointmentProfile.getAptSlot();
                                        aptRemark = appointmentProfile.getAptRemark();
                                        aptLocation = appointmentProfile.getAptLocation();

                                        TimeSlot timeSlot = new TimeSlot();
                                        timeSlot.setSlot(aptSlot);

                                        timeSlotsNew.add(timeSlot);
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlotsNew);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getContext(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(),timeSlotList);
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();

    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();

    }

    public void checkAppointment(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentDate = Calendar.getInstance();
        miliDate = currentDate.getTimeInMillis();
        strDate1 = getDate(miliDate, "dd_MM_yyyy");
        strDate2 = getDate(miliDate + 86400000, "dd_MM_yyyy");
        strDate3 = getDate(miliDate + 86400000 + 86400000, "dd_MM_yyyy");

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + strDate1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();

                for (int i = 1; i <= count; i++) {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments/" + strDate1 +
                            "/" + String.format("%03d", i));

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AppointmentProfile appointmentProfile = dataSnapshot.getValue(AppointmentProfile.class);
                            if(appointmentProfile.getUserUid().equals(user.getUid()) &&
                                    appointmentProfile.getStatus().equals("Pending")
                                    || appointmentProfile.getStatus().equals("Accepted")) {
                                moreThanOneAppointment = true;
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
                            if(appointmentProfile.getUserUid().equals(user.getUid()) && appointmentProfile.getStatus().equals("Pending") || appointmentProfile.getStatus().equals("Accepted")) {
                                moreThanOneAppointment = true;
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
                            if(appointmentProfile.getUserUid().equals(user.getUid()) && appointmentProfile.getStatus().equals("Pending") || appointmentProfile.getStatus().equals("Accepted")) {
                                moreThanOneAppointment = true;
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

    }

}