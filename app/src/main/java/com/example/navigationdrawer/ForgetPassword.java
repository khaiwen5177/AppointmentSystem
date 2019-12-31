package com.example.navigationdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    ProgressBar progressBar;
    EditText mEmail;
    Button mResetBtn;

    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().setTitle("Forget Password");

        progressBar = findViewById(R.id.progressBar3);
        mEmail = findViewById(R.id.email);
        mResetBtn = findViewById(R.id.resetBtn);

        fAuth = FirebaseAuth.getInstance();

        mResetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String email = mEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.sendPasswordResetEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgetPassword.this,"Password reset link has sent to your email",Toast.LENGTH_LONG).show();

                            Intent returnMain = new Intent(ForgetPassword.this,MainActivity.class);
                            returnMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(returnMain);
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ForgetPassword.this,""+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
