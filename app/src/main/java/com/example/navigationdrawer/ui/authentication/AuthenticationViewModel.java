package com.example.navigationdrawer.ui.authentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthenticationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AuthenticationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Login As Admin");
    }

    public LiveData<String> getText() {
        return mText;
    }
}