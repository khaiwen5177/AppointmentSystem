package com.example.navigationdrawer.ui.commission_calculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Commission_CalculatorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Commission_CalculatorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Commission Calculator fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}