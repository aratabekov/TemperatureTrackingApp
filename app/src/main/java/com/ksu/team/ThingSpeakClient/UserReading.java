package com.ksu.team.ThingSpeakClient;

import java.util.Date;
import java.util.List;

/**
 * Created by Amir on 3/18/15.
 */
public class UserReading {

    String card_key;

    public String getCard_key() {
        return card_key;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getMajor() {
        return major;
    }

    public String getAddress() {
        return address;
    }

    public String getIsfaculty() {
        return isfaculty;
    }

    String name;
    String gender;
    String email;
    String phone;
    String major;
    String address;
    String isfaculty;

    public void setScanReadings(List<ScanReading> scanReadings) {
        this.scanReadings = scanReadings;
    }

    public List<ScanReading> getScanReadings() {
        return scanReadings;
    }

    List<ScanReading> scanReadings;

    public List<Date> getUpReadings() {
        return upReadings;
    }

    public void setUpReadings(List<Date> upReadings) {
        this.upReadings = upReadings;
    }

    List<Date> upReadings;

    public UserReading(String card_key,String name,String gender,String email,String phone,
                       String major,String address,String isfaculty,List<ScanReading> list){
        this.card_key=card_key;
        this.name=name;
        this.gender=gender;
        this.email=email;
        this.phone=phone;
        this.major=major;
        this.address=address;
        this.isfaculty=isfaculty;
        this.scanReadings=list;
    }
    public String toString(){
        return name+" : "+email;
    }

}
