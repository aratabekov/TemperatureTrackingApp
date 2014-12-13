package com.ksu.team.temperatureTrackingApp;

import java.util.Date;

/**
 * Created by Amir on 10/22/14.
 */
public class TempReading implements Comparable<TempReading>, Cloneable {

    private int id;
    private float temp;
    private Date date;
    public TempReading(int id, float temp, Date date) {
        this.id=id;
        this.temp=temp;
        this.date=date;
    }

    public int getId(){
        return this.id;
    }


    public void setTemp(float temp) {
        this.temp = temp;
    }
    public float getTemp(){
        return this.temp;
    }
    public Date getDate(){
        return this.date;
    }
    public void setDate(Date date){
         this.date=date;
    }

    @Override
    public String toString() {
        return this.getId()+"|"+this.date.toString()+"|"+this.temp;
    }

    @Override
    public int compareTo(TempReading tempReading) {
        if (this.getTemp() > tempReading.getTemp())
            return 1;
        else if (this.getTemp() == tempReading.getTemp())
            return 0;
        else
            return -1;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
