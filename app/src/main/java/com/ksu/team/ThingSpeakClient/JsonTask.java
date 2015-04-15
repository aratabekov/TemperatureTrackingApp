package com.ksu.team.ThingSpeakClient;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Amir on 10/22/14.
 */
public class JsonTask extends AsyncTask<String,Integer,List<UserReading>>{

    private static final String TAG_ARRAY = "feeds";
    private static final String TAG_KEY = "field1";
    private static final String TAG_DATE = "created_at";
    private static final String TAG_ENTRY = "entry_id";
    private static final int refreshRate = 180; //in seconds

    private String username=null;
    private String password=null;

    private TemperatureActivity activity;
    private static int latestId = 1;
    private static long iteration = 1;

    public JsonTask(TemperatureActivity activity, String username,String password){
        this.activity = activity;
        this.username=username;
        this.password=password;
    }

    public JsonTask(TemperatureActivity activity, int latestId, long iteration, String username, String password){
        this.activity = activity;
        this.latestId = latestId;
        this.iteration = iteration;
        this.username = username;
        this.password = password;
    }

    private HashMap<String,List<ScanReading>> getScanReadings(String url){


        HashMap<String,List<ScanReading>> scanMap=new HashMap<String, List<ScanReading>>();
        //List<ScanReading> scanList=new ArrayList<ScanReading>();

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);


        InputStream inputStream = null;
        String result = null;
        try {

            HttpResponse response = httpclient.execute(httpget);
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

            Log.d("doing",result);
            JSONObject jsonObj = new JSONObject(result);
            JSONArray temp_Array = jsonObj.getJSONArray(TAG_ARRAY);

            this.activity.setProgressMessage("Retrieving data...");

            for (int i = 0; i < temp_Array.length(); i++) {

                publishProgress((int) ((i / (float) temp_Array.length()) * 100));
                JSONObject reading = temp_Array.getJSONObject(i);
                String id = reading.getString(TAG_ENTRY);
                String key = reading.getString(TAG_KEY);
                // float temp = Float.valueOf(reading.getString(TAG_TEMP));
                String str_date = reading.getString(TAG_DATE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                //javax.xml.bind.DatatypeConverter.parseDateTime(str_date)

                Date utcdate = sdf.parse(str_date);
                Date date = new Date(utcdate.getTime() + TimeZone.getTimeZone("EST").getRawOffset());



                if(scanMap.containsKey(key)){
                    scanMap.get(key).add(new ScanReading(id, key, date));
                }
                else {
                    List<ScanReading> list=new ArrayList<ScanReading>() ;
                    list.add(new ScanReading(id, key, date));
                    scanMap.put(key, list);
                }
            }
        } catch (Exception e) {
            // Oops
            Log.d("Displaying","something went wrong "+e.toString());
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }
        return scanMap;
    }

    private HashMap<String,UserReading> getUserReadings(String url){


        HashMap<String,UserReading> userMap=new HashMap<String, UserReading>();
        //List<ScanReading> scanList=new ArrayList<ScanReading>();

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);


        InputStream inputStream = null;
        String result = null;
        try {

            HttpResponse response = httpclient.execute(httpget);
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

            Log.d("doing",result);
            JSONObject jsonObj = new JSONObject(result);
            JSONArray temp_Array = jsonObj.getJSONArray(TAG_ARRAY);

            this.activity.setProgressMessage("Retrieving data...");

            for (int i = 0; i < temp_Array.length(); i++) {

                publishProgress((int) ((i / (float) temp_Array.length()) * 100));
                JSONObject reading = temp_Array.getJSONObject(i);
                String id = reading.getString(TAG_ENTRY);
               // String key = reading.getString(TAG_KEY);
                // float temp = Float.valueOf(reading.getString(TAG_TEMP));
                String str_date = reading.getString(TAG_DATE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                //javax.xml.bind.DatatypeConverter.parseDateTime(str_date)
                Date utcdate = sdf.parse(str_date);
                Date date = new Date(utcdate.getTime() + TimeZone.getTimeZone("EST").getRawOffset());
                //Date date = sdf.parse(str_date);

                userMap.put(reading.getString("field1"),new UserReading(reading.getString("field1"),reading.getString("field2"),
                        reading.getString("field3"),reading.getString("field4"),reading.getString("field5"),reading.getString("field6"),
                        reading.getString("field7"),reading.getString("field8"),null));

            }
        } catch (Exception e) {
            // Oops
            Log.d("Displaying","something went wrong "+e.toString());
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }
        return userMap;
    }

    private HashMap<String,List<Date>> getUpReadings(String url){


        HashMap<String,List<Date>> upMap=new HashMap<String, List<Date>>();
        //List<ScanReading> scanList=new ArrayList<ScanReading>();

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);


        InputStream inputStream = null;
        String result = null;
        try {

            HttpResponse response = httpclient.execute(httpget);
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

            Log.d("doing",result);
            JSONObject jsonObj = new JSONObject(result);
            JSONArray temp_Array = jsonObj.getJSONArray(TAG_ARRAY);

            this.activity.setProgressMessage("Retrieving data...");

            for (int i = 0; i < temp_Array.length(); i++) {

                publishProgress((int) ((i / (float) temp_Array.length()) * 100));
                JSONObject reading = temp_Array.getJSONObject(i);
                String id = reading.getString(TAG_ENTRY);
                String key = reading.getString(TAG_KEY);
                // float temp = Float.valueOf(reading.getString(TAG_TEMP));
                String str_date = reading.getString(TAG_DATE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                //javax.xml.bind.DatatypeConverter.parseDateTime(str_date)
                Date utcdate = sdf.parse(str_date);
                Date date = new Date(utcdate.getTime() + TimeZone.getTimeZone("EST").getRawOffset());
                //Date date = sdf.parse(str_date);

                if(upMap.containsKey(key)){
                    upMap.get(key).add( date);
                }
                else {
                    List<Date> list=new ArrayList<Date>() ;
                    list.add(date);
                    upMap.put(key, list);
                }
            }
        } catch (Exception e) {
            // Oops
            Log.d("Displaying","something went wrong "+e.toString());
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }
        return upMap;
    }

    protected List<UserReading> doInBackground(String... urls)  {
        //if not first iteration, wait for a while to get new data
        if (iteration > 1) {
            try {
                Thread.sleep(refreshRate*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<UserReading> list=new ArrayList<UserReading>();
        //channels/27566/feed.json?key=AKP4YW7BF2HAOPFW&results=100
        HashMap<String,UserReading> userMap=getUserReadings(urls[0]+"/channels/27566/feed.json?key=AKP4YW7BF2HAOPFW&results=100");
        HashMap<String,List<ScanReading>> scanMap=getScanReadings(urls[0]+"/channels/27574/feeds.json?api_key=99TB7WFLZWKPMEGP&results=100");
        HashMap<String,List<Date>> upMap=getUpReadings(urls[0] + "/channels/33879/feed.json?key=UT7G2FPBSR54UMQ9&results=100");


        for(Map.Entry<String, UserReading> entry : userMap.entrySet()){

            UserReading reading=entry.getValue();

            reading.setScanReadings(scanMap.get(reading.getCard_key()));

            reading.setUpReadings(upMap.get(reading.getCard_key()));

            list.add(reading);
        }


        publishProgress(100);


        Log.d("Displaying","displaying the "+list);

        return list;
    }

    protected void onProgressUpdate(Integer... progress) {
        activity.progressBar.setProgress(progress[0]);
    }

    //will pass only latest results to onTempResult. First time latestId is 0
    protected void onPostExecute(List<UserReading> result) {
        this.activity.setProgressMessage("Processing data...");
        activity.onTempResult(result);
    }

}
