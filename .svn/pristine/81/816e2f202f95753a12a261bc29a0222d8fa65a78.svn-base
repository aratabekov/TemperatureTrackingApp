package com.ksu.team.temperatureTrackingApp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Amir on 10/22/14.
 */
public class JsonTask extends AsyncTask<String,Integer,List<TempReading>>{

    private static final String TAG_ARRAY = "temperature";
    private static final String TAG_ID = "id";
    private static final String TAG_TEMP = "temp";
    private static final String TAG_DATE = "date";
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

    protected List<TempReading> doInBackground(String... urls)  {
        //if not first iteration, wait for a while to get new data
        if (iteration > 1) {
            try {
                Thread.sleep(refreshRate*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<TempReading> list=new ArrayList<TempReading>();

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(urls[0]);


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
            JSONArray temp_Array = jsonObj.getJSONArray(TAG_ARRAY);

            this.activity.setProgressMessage("Retrieving data...");

            for (int i = 0; i < temp_Array.length(); i++) {

                publishProgress((int) ((i / (float) temp_Array.length()) * 100));

                JSONObject reading = temp_Array.getJSONObject(i);

                int id = Integer.valueOf(reading.getString(TAG_ID));
                float temp = Float.valueOf(reading.getString(TAG_TEMP));

                String str_date = reading.getString(TAG_DATE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(str_date);

                list.add(new TempReading(id,temp,date));

            }


        } catch (Exception e) {
            // Oops
            Log.d("Displaying","something went wrong "+e.toString());
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }

        Log.d("Displaying","displaying the "+result);

        return list;
    }

    protected void onProgressUpdate(Integer... progress) {
        activity.progressBar.setProgress(progress[0]);
    }

    //will pass only latest results to onTempResult. First time latestId is 0
    protected void onPostExecute(List<TempReading> result) {
        this.activity.setProgressMessage("Processing data...");

        Log.i("JsonTaskTAG", "Iteration: " + iteration + " Latest id: " + latestId);

        if (iteration > 1) {
            int lastIndex = result.size() - 1;
            for (TempReading tr : result) {
                if (latestId == tr.getId()) {
                    int index = result.indexOf(tr);

                    if (index == lastIndex)
                        result = new ArrayList<TempReading>();//no new results, just send an empty array then
                    else
                        result = result.subList(index + 1, result.size() - 1);

                    break;
                }
            }
        }

        activity.onTempResult(result, iteration);
    }

}
