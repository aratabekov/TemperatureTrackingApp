package com.ksu.team.temperatureTrackingApp;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Marcel on 30-Oct-14.
 */
public class DateReadingsUtil {
    public static final String FORMAT_BY_DAY = "dd MMM yyyy - EEEE";
    public static final String FORMAT_BY_MINUTE="dd MMM yyyy - HH:mm";
    public static final String FORMAT_BY_HOUR="dd MMM yyyy - HH";

    private static LinkedHashMap<String, ArrayList<TempReading>> readingsByDay = new LinkedHashMap<String, ArrayList<TempReading>>();
    private static Date currentDate;

    public static HashMap<String, ArrayList<TempReading>> getReadingsByDay() {
        return readingsByDay;
    }

    public static Date getCurrentDate() {
        return currentDate;
    }

    public static void setCurrentDate(Date date) {
        currentDate = date;
    }

    public static void buildReadingsByDay(TempReading tr) {
        String day = getFormattedDate(tr.getDate());
        ArrayList<TempReading> aux = null;

        if (readingsByDay.containsKey(day))
            aux = readingsByDay.get(day);
        else
            aux = new ArrayList<TempReading>();

        aux.add(tr);
        readingsByDay.put(day, aux);
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_BY_DAY);
        return format.format(date);
    }
    public static String getFormattedDate(Date date, String format_str) {
        SimpleDateFormat format = new SimpleDateFormat(format_str);
        return format.format(date);
    }

    public static Date getPreviousAvailable(Date date){
        Date prev=null;
        try
        {
            Iterator <Map.Entry <String,ArrayList<TempReading>>> it=readingsByDay.entrySet().iterator();
            while(it.hasNext() ){

                Map.Entry <String,ArrayList<TempReading>> entry=it.next();

                SimpleDateFormat format = new SimpleDateFormat(FORMAT_BY_DAY);
                Date current=format.parse(entry.getKey());

                if(entry.getKey().compareTo(getFormattedDate(date))==0){
                    break;
                }
                prev=current;
                Log.d("previous",current.toString());
            }
        }
        catch (Exception e){
            Log.d("just checking catch",e.toString());
            e.printStackTrace();
        }
        return prev;
    }
    public static Date getNextAvailableDate(Date date){
        Date res=null;
        try
        {
            Iterator <Map.Entry <String,ArrayList<TempReading>>> it=readingsByDay.entrySet().iterator();
            while(it.hasNext() ){

                Map.Entry <String,ArrayList<TempReading>> entry=it.next();
                if(entry.getKey().compareTo(getFormattedDate(date))==0){
                    if(it.hasNext()){
                        Map.Entry <String,ArrayList<TempReading>> next=it.next();
                        SimpleDateFormat format = new SimpleDateFormat(FORMAT_BY_DAY);
                        res=format.parse(next.getKey());
                        break;
                    }
                }
            }
        }
        catch (Exception e){
            Log.d("just checking catch",e.toString());
            e.printStackTrace();
        }
        return res;
    }
}
