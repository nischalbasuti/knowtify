package com.hydratech19gmail.notify;

import java.util.Calendar;

/**
 * Created by nischal on 17/9/16.
 */
public class DateConvert {
    public static String timeStampToDate(long timeInMills){
        Calendar currCal = Calendar.getInstance();
        currCal.setTimeInMillis(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMills);

        int dateInt = calendar.get(Calendar.DATE);
        int monthInt = calendar.get(Calendar.MONTH);
        int yearInt = calendar.get(Calendar.YEAR);

        long dayInMills = (long)86400000;
        long hourInMills = dayInMills/(long)24;
        long minutesInMills = hourInMills/(long)60;

        long diff = currCal.getTimeInMillis() - calendar.getTimeInMillis();
        if(diff < dayInMills){
            if(diff < hourInMills) {
                return diff/minutesInMills+" minutes ago";
            }
            return diff/hourInMills+" hours ago";
        }

        String month="";

        switch(monthInt){
            case 0:
                month = "Jan";
                break;
            case 1:
                month = "Feb";
                break;
            case 2:
                month = "Mar";
                break;
            case 3:
                month = "Apr";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "Jun";
                break;
            case 6:
                month = "Jul";
                break;
            case 7:
                month = "Aug";
                break;
            case 8:
                month = "Sep";
                break;
            case 9:
                month = "Oct";
                break;
            case 10:
                month = "Nov";
                break;
            case 11:
                month = "Dec";
                break;
        }

        return dateInt+" "+month+", "+yearInt;
    }
}
