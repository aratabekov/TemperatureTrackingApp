package com.ksu.team.ThingSpeakClient;

import java.util.Date;

/**
 * Created by Amir on 10/22/14.
 */
public class ScanReading  {

    private String id;
    private float temp;
    private Date date;
    private String key;
    public ScanReading(String id,String key,  Date date) {
        this.id=id;
        this.temp=temp;
        this.date=date;
        this.key=key;
    }

    public String getId(){
        return this.id;
    }



    public Date getDate(){
        return this.date;
    }


    public String toString(){
        return getId()+" "+key+" "+getDate().toString()+"\n";
    }




}
