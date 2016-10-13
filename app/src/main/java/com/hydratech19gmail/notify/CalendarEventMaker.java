package com.hydratech19gmail.notify;

import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by zappereton on 13/10/16.
 */

public class CalendarEventMaker {

    Context ctx;
    long beginTime;
    long endTime;
    String title;

    CalendarEventMaker(Context ctx,long beginTime,long endTime,String title){

        this.ctx = ctx;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.title = title;
    }

    public void makeEvent(){
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", beginTime);
        intent.putExtra("allDay", false);
        intent.putExtra("endTime", beginTime);
        intent.putExtra("title", title);
        ctx.startActivity(intent);
    }
    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
