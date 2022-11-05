package com.genxhire.opt.handler.recycler;

import android.util.Log;

public class RecyclerViewModel {
    String LogText;

    public String getLogText() {
        return LogText;
    }

    public void setLogText(String logText) {
        LogText = logText;
    }

    public RecyclerViewModel() {
    }

    public RecyclerViewModel(String name) {
        LogText = name;
    }
}
