package com.genxhire.opt.handler;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class LogText extends BaseObservable {
    //This will be the message you wanna print
    private String message;

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        //This would automatically update any binded views with this model whenever the message changes
        notifyPropertyChanged(BR._all);
    }
}