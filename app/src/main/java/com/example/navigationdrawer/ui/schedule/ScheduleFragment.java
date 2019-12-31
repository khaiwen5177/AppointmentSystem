package com.example.navigationdrawer.ui.schedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.navigationdrawer.AppointmentStep2;
import com.example.navigationdrawer.R;
import com.example.navigationdrawer.ScheduleStep2;

import java.util.Calendar;

import static com.example.navigationdrawer.ui.history.HistoryFragment.getDate;

public class ScheduleFragment extends Fragment {

    CalendarView mCalendar;
    Button mShowBtn;
    private ScheduleViewModel scheduleViewModel;
    private String date;
    private long dateLong;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        //final TextView textView = root.findViewById(R.id.text_schedule);

        mCalendar = root.findViewById(R.id.calendar);
        mShowBtn = root.findViewById(R.id.showButton);

        dateLong = mCalendar.getDate();
        date = getDate(dateLong, "dd_MM_yyyy");


        mCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = String.format("%02d", i2) + "_" +  String.format("%02d", (i1 + 1)) + "_" + i;
            }
        });

        mShowBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                    ScheduleStep2.selected_date = date;
                    startActivity(new Intent(getContext(), ScheduleStep2.class));

            }
        });

        return root;
    }
}