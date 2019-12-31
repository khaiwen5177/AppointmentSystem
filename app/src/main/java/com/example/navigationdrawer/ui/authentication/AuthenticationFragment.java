package com.example.navigationdrawer.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.navigationdrawer.Login;
import com.example.navigationdrawer.MainActivity;
import com.example.navigationdrawer.R;
import com.example.navigationdrawer.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AuthenticationFragment extends Fragment {

    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;

    FirebaseAuth fAuth;

    ProgressBar progressBar;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_register, container, false);

        mFullName = root.findViewById(R.id.fullName);
        mEmail = root.findViewById(R.id.email);
        mPassword = root.findViewById(R.id.password);
        mPhone = root.findViewById(R.id.phone);
        mRegisterBtn = root.findViewById(R.id.registerBtn);
        mLoginBtn = root.findViewById(R.id.loginTxt);

        fAuth = FirebaseAuth.getInstance();
        progressBar = root.findViewById(R.id.progressBar);
        setHasOptionsMenu(true);

        if(fAuth.getCurrentUser() != null){
            //startActivity(new Intent(getContext(),MainActivity.class));
            //finish();

            Intent returnMain = new Intent(getContext(),MainActivity.class);
            returnMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(returnMain);
        }
        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final String email = mEmail.getText().toString().trim();
                final String fullName = mFullName.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();
                final String typeUser = "user";
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(fullName)){
                    mFullName.setError("Full Name is Required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    mPhone.setError("Phone number is Required.");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password must be more than or equal 6 characters");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the user in firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //
                            UserProfile userProfile= new UserProfile(email,phone,fullName,typeUser);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(userProfile);

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            Toast.makeText(getContext(), "User Created." +user.getEmail(), Toast.LENGTH_SHORT).show();
                            //
                            Intent returnMain = new Intent(getContext(),MainActivity.class);
                            returnMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(returnMain);
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getContext(), Login.class));
            }
        });

        return root;
    }



}