package com.example.air.game2048;

import java.util.Calendar;

/**
 * Created by gcx on 16/8/18.
 */
public class Time {
    protected int currentDate;
    public void Time(){
        Calendar calendar = Calendar.getInstance();
        currentDate = calendar.get(Calendar.DATE);
    }
}
