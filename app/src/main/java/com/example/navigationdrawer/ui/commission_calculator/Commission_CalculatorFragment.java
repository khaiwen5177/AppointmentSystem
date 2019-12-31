package com.example.navigationdrawer.ui.commission_calculator;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.navigationdrawer.AppointmentProfile;
import com.example.navigationdrawer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Commission_CalculatorFragment extends Fragment  {

    Button mShowBtn;
    TextView mTextView;
    FirebaseDatabase firebaseDatabase;
    Spinner mSpinnerWorker;


    SimpleDateFormat simpleDateFormat;
    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    Calendar selected_date;
    Date tempDate;
    private String strSelectedDate;
    private int commision;
    private String strWorker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_commission_calculator, container, false);
        //final TextView textView = root.findViewById(R.id.text_commission_calculator);


        mShowBtn = root.findViewById(R.id.showButton);
        mTextView = root.findViewById(R.id.testtxt);
        mSpinnerWorker = root.findViewById(R.id.spinnerWorker);
        simpleDateFormat = new SimpleDateFormat("MM_yyyy");

        //Calendar
        selected_date = Calendar.getInstance();
        selected_date.add(Calendar.MONTH, -3);
        tempDate = selected_date.getTime();
        strSelectedDate = simpleDateFormat.format(tempDate);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH,-3);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH,0);

        //dialog.show();
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(root,R.id.calendarView)
                .range(startDate,endDate)
                .datesNumberOnScreen(1)
                .configure()
                    .showTopText(false)
                    .formatMiddleText("MMM")
                    .formatBottomText("yyyy")
                .end()
                .mode(HorizontalCalendar.Mode.MONTHS)
                .defaultSelectedDate(endDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {

                                                   @Override
                                                   public void onDateSelected(Calendar date, int position) {
                                                       if (selected_date.getTimeInMillis() != date.getTimeInMillis()) {
                                                           selected_date = date;
                                                           tempDate = selected_date.getTime();
                                                           strSelectedDate = simpleDateFormat.format(tempDate);
                                                       }
                                                   }
                                               });


        mSpinnerWorker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position == 0){
                    strWorker = "Johnson Ooi";
                }
                if(position == 1){
                    strWorker = "Ali";
                }
                if(position == 2){
                    strWorker = "Darren Lim";
                }
                if(position == 3){
                    strWorker = "Faiz";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                strWorker = "Johnson Ooi";
            }

        });



        mShowBtn.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText("");
                commision =0;


                firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Appointments");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                            for (DataSnapshot postpostSnapshot: postSnapshot.getChildren()) {

                                AppointmentProfile appointmentProfile = postpostSnapshot.getValue(AppointmentProfile.class);


                                String dateStr = appointmentProfile.getAptDate();
                                dateStr = dateStr.substring(3);
                                //mTextView.append(dateStr);

                                    if(appointmentProfile.getWorker().equals(strWorker)
                                            && dateStr.equals(strSelectedDate)
                                            && appointmentProfile.getStatus().equals("Completed")){

                                        mTextView.append("Date : " + appointmentProfile.getAptDate() + "\nNo : " + appointmentProfile.getAptNum() + "\n");

                                        String strServices = appointmentProfile.getServices();
                                        String strAptLocation = appointmentProfile.getAptLocation();
                                        String[] arrOfStr = strServices.split("\n", 0);
                                        int dayTotal = 0;

                                        for (String a : arrOfStr) {
                                            if(a.equals("Diagnostic and Troubleshoot")){
                                                    a = "Diagnostic and Troubleshoot (RM5)\n";
                                                dayTotal = dayTotal + 5;
                                                }
                                            if(a.equals("Maintenance Service")){
                                                a = "Maintenance Service (RM15)\n";
                                                dayTotal = dayTotal + 15;
                                            }
                                            if(a.equals("Breaking System")){
                                                a = "Breaking System (RM10)\n" ;
                                                dayTotal = dayTotal + 10;
                                            }
                                            if(a.equals("Suspension System")){
                                                a = "Suspension System (RM20)\n" ;
                                                dayTotal = dayTotal + 20;
                                            }
                                            mTextView.append(a);
                                        }

                                        if(!strAptLocation.equals("In-Workshop Service")){
                                            dayTotal = dayTotal + 8;
                                            mTextView.append("On-Locatioin Service (RM8)\n");
                                        }

                                        mTextView.append("COMMISSION : RM"+ dayTotal + "\n\n");
                                        commision = commision + dayTotal;

                                }

                            }
                        }

                        if(commision == 0){
                            mTextView.append("NO RECORD");
                        }else{
                            mTextView.append("TOTAL COMMISSION : RM" + commision);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        return root;
    }
}