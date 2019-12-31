package com.example.navigationdrawer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationdrawer.Common.Common;
import com.example.navigationdrawer.Interface.IRecyclerItemSelectedListener;
import com.example.navigationdrawer.Login;
import com.example.navigationdrawer.Model.TimeSlot;
import com.example.navigationdrawer.R;
import com.example.navigationdrawer.ui.appointment.AppointmentFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyTimeSlotAdapter(Context context){
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList){
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.setIsRecyclable(false);
        myViewHolder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(i)).toString());
        if(timeSlotList.size() == 0){

            myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
            myViewHolder.txt_time_slot_description.setText("Available");
            myViewHolder.txt_time_slot_description.setTextColor(Color.parseColor("#2F6C00"));
            myViewHolder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));


        }
        else //If have position is full (booked)
        {
            //Loop all time slot from server and set different color
            for(TimeSlot slotValue:timeSlotList){
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if(slot == i) //If slot == position
                {
                    myViewHolder.card_time_slot.setTag(Common.DISABLE_TAG);
                    myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    myViewHolder.txt_time_slot_description.setText("Full");
                    myViewHolder.txt_time_slot_description.setTextColor(context.getResources()
                            .getColor(android.R.color.white));
                    myViewHolder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));
                    myViewHolder.card_time_slot.setEnabled(false);

                }
            }
        }
        //Add all card to list ( 20 card because we have 20 time slot
        //No add card already in cardViewList
        if(!cardViewList.contains(myViewHolder.card_time_slot)) {
            //Toast.makeText(context, "This is my Toast message!", Toast.LENGTH_LONG).show();
            cardViewList.add(myViewHolder.card_time_slot);
        }
        //Check if card time slot is available
        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                Toast.makeText(context, "This is my Toast message! pos: " + pos, Toast.LENGTH_LONG).show();
                AppointmentFragment.pos = pos;
                //lopp all card in card list
                for(CardView cardView:cardViewList) {
                    if (cardView.getTag() == null) //only available card time slot be change
                        cardView.setCardBackgroundColor(context.getResources()
                                .getColor(android.R.color.white));
                }
                //our selected card will be change color
                myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources()
                .getColor(R.color.colorPink));
                AppointmentFragment.mNextBtn.setEnabled(true);

                //After that , send broadcase to enable button next


            }
        });
    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot, txt_time_slot_description;
        CardView card_time_slot;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener){
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)itemView.findViewById(R.id.text_time_slot);
            txt_time_slot_description = (TextView) itemView.findViewById(R.id.text_time_slot_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
