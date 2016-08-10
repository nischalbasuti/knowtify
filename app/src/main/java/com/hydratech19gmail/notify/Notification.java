package com.hydratech19gmail.notify;

/**
 * Created by Jaelse on 09-08-2016.
 */
public class Notification {

    String data1;
    String data2;

    public Notification(){

    }
    public Notification(String data1,String data2){
        this.data1 = data1;
        this.data2 = data2;
    }

    public String getData1() {
        return data1;
    }

    public String getData2() {
        return data2;
    }
}
