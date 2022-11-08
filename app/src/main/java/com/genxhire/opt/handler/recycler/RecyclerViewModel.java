package com.genxhire.opt.handler.recycler;

public class RecyclerViewModel {
    String LogText;
    int ColorInt;

    public int getColorInt() {
        return ColorInt;
    }

    public void setColorInt(int colorInt) {
        ColorInt = colorInt;
    }

    public String getLogText() {
        return LogText;
    }

    public void setLogText(String logText) {
        LogText = logText;
    }

    public RecyclerViewModel() {
    }

    public RecyclerViewModel(String logText, int color) {
        LogText = logText;
        ColorInt = color;
    }

    public RecyclerViewModel(String name) {
        LogText = name;
    }
}
