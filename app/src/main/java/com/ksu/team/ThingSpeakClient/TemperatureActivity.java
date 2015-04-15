package com.ksu.team.ThingSpeakClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TemperatureActivity extends Activity {

    //private static final String TAG_IP_ADDRESS="10.0.2.2";


    public ProgressBar progressBar;
    public TextView txtLoading;
    //final long oneHour = 60 * 60 * 1000;
    LinearLayout layout = null;

    Integer threshold = null;
    Integer temp_threshold = null;
    public static String path = null;
    //TempReading latestReading = null;
    public String username;
    public String password;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

       // graphView = new LineGraphView(this, "");
        ArrayList<ScanReading> result = new ArrayList<ScanReading>();
       // temperatureDataGraph = new GraphViewSeries(new GraphView.GraphViewData[]{});
        String ip_address;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                ip_address= null;
                username=null;
                password=null;
            } else {
                ip_address= extras.getString("IP_ADDRESS");
                username=extras.getString("USERNAME");
                password=extras.getString("PASSWORD");
                path =  "https://"+ip_address;
            }
        } else {
            ip_address= (String) savedInstanceState.getSerializable("IP_ADDRESS");
            username=(String) savedInstanceState.getSerializable("USERNAME");
            password=(String) savedInstanceState.getSerializable("PASSWORD");
            path = "https://"+ip_address;
        }

       // currentDay
        this.progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        this.txtLoading= (TextView) findViewById(R.id.loading_text);

        JsonTask task = new JsonTask(this,username,password);
        task.execute(path);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.temperature, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //result list contains new results only
    public void onTempResult(List<UserReading> rawResult) {
        Log.d("debug",rawResult.toString());

        String resultStr="\n\n";

        for(int i=0;i<rawResult.size();i++){
            UserReading read= rawResult.get(i);

            Date latestScan=read.getScanReadings().get(read.getScanReadings().size()-1).getDate();
            Date latestUp=read.getUpReadings().get(read.getUpReadings().size() - 1);


            Calendar start = Calendar.getInstance();
            start.setTime(latestScan);
            Calendar end = Calendar.getInstance();
            end.setTime(latestUp);

            Integer[] elapsed = new Integer[6];
            Calendar clone = (Calendar) start.clone(); // Otherwise changes are been reflected.
            elapsed[0] = elapsed(clone, end, Calendar.YEAR);
            clone.add(Calendar.YEAR, elapsed[0]);
            elapsed[1] = elapsed(clone, end, Calendar.MONTH);
            clone.add(Calendar.MONTH, elapsed[1]);
            elapsed[2] = elapsed(clone, end, Calendar.DATE);
            clone.add(Calendar.DATE, elapsed[2]);
            elapsed[3] = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 3600000;
            clone.add(Calendar.HOUR, elapsed[3]);
            elapsed[4] = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 60000;
            clone.add(Calendar.MINUTE, elapsed[4]);
            elapsed[5] = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 1000;

            //System.out.format("%d years, %d months, %d days, %d hours, %d minutes, %d seconds", elapsed);

            resultStr+=read.toString()+": \nLatest scan: "+read.getScanReadings().get(read.getScanReadings().size()-1).getDate().toString()+"\n"+
                    "Duration:"+elapsed[3] +" hours, "+elapsed[4]+" minutes, "+elapsed[5]+" seconds"+
                    "\n\n";
        }

        setProgressMessage(resultStr);

    }



    public void setProgressMessage(final String s){

        txtLoading.post(new Runnable() {
            public void run() {
                txtLoading.setText(s);
            }
        });
        //this.txtLoading.setText(s);
    }
    public static int elapsed(Calendar before, Calendar after, int field) {
        Calendar clone = (Calendar) before.clone(); // Otherwise changes are been reflected.
        int elapsed = -1;
        while (!clone.after(after)) {
            clone.add(field, 1);
            elapsed++;
        }
        return elapsed;
    }

}
