package com.example.navigationdrawer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.navigationdrawer.ui.appointment.AppointmentFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import java.util.Calendar;

import static com.example.navigationdrawer.ui.history.HistoryFragment.getDate;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    FirebaseAuth fAuth;
    FirebaseDatabase fDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;

    private String userType = "";
    private String userName = "";
    public static int displayCount;

    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        displayCount = 0;
        super.onCreate(savedInstanceState);
        //FirebaseAuth.getInstance().signOut();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Contact Us : 010-5630235\nEmail : khaiwen96@hotmail.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_appointment, R.id.nav_history,
                R.id.nav_authentication, R.id.nav_commission_calculator, R.id.nav_schedule)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//////////////////

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        if(fAuth.getCurrentUser() != null){

            databaseReference = fDatabase.getReference("Users").child(fAuth.getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                    userType = userProfile.getUserType();
                    userName = userProfile.getUserName();

                    if(userType.equals("user")){
                        navigationView.getMenu().findItem(R.id.nav_appointment).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_history).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

                    }
                    else if(userType.equals("admin")){
                        navigationView.getMenu().findItem(R.id.nav_commission_calculator).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_schedule).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        databaseReference1 = fDatabase.getReference("Appointments");
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                displayCount = displayCount + 1;
                                if(displayCount >= 2){
                                    displayCount = displayCount - 1;
                                    addNotification();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }



  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem logoutItem = menu.findItem(R.id.action_logout);
        if(fAuth.getCurrentUser() == null) {
            logoutItem.setEnabled(false);
        }
        else{
            logoutItem.setEnabled(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "You have Signed Out", Toast.LENGTH_SHORT).show();
        Intent returnMain = new Intent(this,MainActivity.class);
        returnMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(returnMain);
        System.exit(0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    //BELOW IS DOUBLE TAP TO EXIT
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }




    private void addNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.biglogo);
        builder.setContentTitle("New appointment just arrived");
        builder.setContentText("Tap to process");
        builder.setAutoCancel(true);

        //Uri alarmSound = RingtoneManager. getDefaultUri (RingtoneManager. TYPE_NOTIFICATION );
        //MediaPlayer mp = MediaPlayer.create(getApplicationContext(), alarmSound);
        //mp.start();
        Vibrator vibrator = (Vibrator) this .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);


        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}
