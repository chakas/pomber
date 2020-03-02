package com.chakas;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Calendar;
import java.util.Date;

@Named
@Singleton
public class DateFilterMapper implements DateFilter {

    @Override
    public Date dateFilter(String ts) {

        Calendar cal = Calendar.getInstance();
        String timeStr = ts.substring(0, ts.length() - 1);
        int timeInNumber;

        //
        try{
            timeInNumber = -1 * Integer.parseInt(timeStr);
        }catch (Exception ex) { return cal.getTime();}

        switch (ts.charAt(ts.length()-1)) {
            case 'y':
//                System.out.println(timeInNumber + " years back");
                cal.add(Calendar.YEAR, timeInNumber);
                break;
            case 'm':
//                System.out.println(timeInNumber + " months back");
                cal.add(Calendar.MONTH, timeInNumber);
                break;
            case 'd':
//                System.out.println( timeInNumber + " days back");
                cal.add(Calendar.DATE, timeInNumber);
                break;
            default:
                System.out.println("Default to data");
                break;
        }
        return cal.getTime();
    }
}
