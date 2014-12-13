package com.ksu.team.temperatureTrackingApp;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 11/26/14.
 */
public class UserService {
    public static int AUTH_SUCCESS=0;
    public static int AUTH_INCORRECT_PWD=1;
    public static int AUTH_DOESNT_EXIST=2;
    public static int AUTH_FAILED=-1;
    public static int CREATE_SUCCESS=0;
    public static int CREATE_EXISTS=1;
    public static int CREATE_FAILED=-1;

    public String URL=null;
    public UserService(String url){
        //this.URL=url;

        this.URL="http://"+url+"/RaspberryPi/";
    }

    public int authenticate(String username,String password){
        int RESULT=AUTH_FAILED;

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL+"Authenticate");


        InputStream inputStream = null;
        String result = null;
        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();

            JSONObject jsonObj = new JSONObject(result);
            int code = jsonObj.getInt("code");

            RESULT=code;


        } catch (Exception e) {
            // Oops
            Log.d("Displaying", "something went wrong " + e.toString());
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }




        return RESULT;
    }

    public int create(String username,String password){
        int RESULT=CREATE_FAILED;

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL+"CreateUser");


        InputStream inputStream = null;
        String result = null;
        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();

            JSONObject jsonObj = new JSONObject(result);
            int code = jsonObj.getInt("code");

            RESULT=code;


        } catch (Exception e) {
            // Oops
            Log.d("Displaying", "something went wrong " + e.toString());
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }




        return RESULT;
    }

}
