package com.ksu.team.temperatureTrackingApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;
import com.ksu.team.temperatureTrackingApp.utils.DateReadingsUtil;
import com.ksu.team.temperatureTrackingApp.utils.OnSwipeTouchListener;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TemperatureActivity extends Activity {

    //private static final String TAG_IP_ADDRESS="10.0.2.2";

    private GraphViewSeries temperatureDataGraph = null;
    LineGraphView graphView = null;
    public ProgressBar progressBar;
    public TextView txtLoading;
    //final long oneHour = 60 * 60 * 1000;
    LinearLayout layout = null;
    public enum Scale {F, C};
    public Scale scale = Scale.F;
    Integer threshold = null;
    Integer temp_threshold = null;
    public static String path = null;
    TempReading latestReading = null;
    public String username;
    public String password;
    
    public static double myFloor(double num, int multipleOf) {
        return ( Math.floor(num / multipleOf) * multipleOf );
    }
    public static double myCeil (double num, int multipleOf) {
        return ( Math.ceil(num / multipleOf) * multipleOf );
    }

    public void setCustomVerticalIntervals(ArrayList<TempReading> temperatures) {
        Comparator<TempReading> customTemperatureComparator = new Comparator<TempReading>() {
            @Override
            public int compare(TempReading temp1, TempReading temp2) {
                if (temp1.getTemp() > temp2.getTemp())
                    return 1;
                else if (temp1.getTemp() == temp2.getTemp())
                    return 0;
                else
                    return -1;
            }
        };
        double max = Collections.max(temperatures, customTemperatureComparator).getTemp();
        double min = Collections.min(temperatures, customTemperatureComparator).getTemp();

        int interval;
        int rawRange = (int) (max - min);

        if (rawRange <= 5) {
            interval = 1;
        } else
        if (rawRange <= 10) {
            interval = 2;
        } else
        if (rawRange <= 80) {
            interval = 5; // increment of 5 between each label
        } else
            interval = 10;

        int minValue = (int) myFloor(min, interval);
        int maxValue = (int) myCeil(max, interval);

        int range = maxValue - minValue;

        // set manual bounds
        graphView.setManualYAxisBounds(maxValue, minValue);
        // indicate number of vertical labels
        graphView.getGraphViewStyle().setNumVerticalLabels(range / interval + 1);

    }

    public void setCustomHorizontalIntervals(ArrayList<TempReading> temperatures) {
        Comparator<TempReading> customComparator =
                new Comparator<TempReading>() {
                    @Override
                    public int compare(TempReading temp1, TempReading temp2) {
                        if (temp1.getDate().after(temp2.getDate()))
                            return 1;
                        else if (temp1.getDate().before(temp2.getDate()))
                            return -1;
                        else
                            return 0;
                    }};

        Calendar maxCalendarDate = new GregorianCalendar();
        maxCalendarDate.setTime(Collections.max(temperatures, customComparator).getDate());

        Calendar minCalendarDate = new GregorianCalendar();
        minCalendarDate.setTime(Collections.min(temperatures, customComparator).getDate());

        // search the interval between 2 horizontal labels
        int interval;
        int minHour = minCalendarDate.get(Calendar.HOUR_OF_DAY);
        int maxHour = maxCalendarDate.get(Calendar.HOUR_OF_DAY);
        int rawHourRange =  maxHour - minHour;

        if (rawHourRange == 0) {

            int minMinute = minCalendarDate.get(Calendar.MINUTE);
            int maxMinute = maxCalendarDate.get(Calendar.MINUTE);
            int rawMinutesRange = maxMinute - minMinute;
            if (rawMinutesRange <= 15)
                interval = 5;
            else
                interval = 15;

            int minValue = (int) myFloor(minMinute, interval);
            int maxValue = (int) myCeil(maxMinute, interval);
            int minutesRange = maxValue - minValue;
            graphView.getGraphViewStyle().setNumHorizontalLabels(minutesRange / interval + 1);
            Log.i("HourRangeTAG", minMinute+" "+maxMinute+" "+minValue+" "+maxValue);
        } else {
            if (rawHourRange <= 4)
                interval = 1;
            else if (rawHourRange <= 8)
                interval = 2;// 8 / 4
            else if (rawHourRange <= 16)
                interval = 4;// 16 / 4
            else
                interval = 6; // 24 / 4

            int minValue = (int) myFloor(minHour, interval);
            int maxValue = (int) myCeil(maxHour, interval);

            int hourRange = maxValue - minValue;

            ((LineGraphView) graphView).getGraphViewStyle().setNumHorizontalLabels(hourRange / interval + 1);
        }
    }

    public void setCustomIntervals (ArrayList<TempReading> temperatures) {
        //Custom vertical Axis
        setCustomVerticalIntervals(temperatures);

        //Custom horizontal Axis
        setCustomHorizontalIntervals(temperatures);
    }

    public void displayGraph(GraphViewSeries temperatureReadings) {
        //((LineGraphView) graphView).setScrollable(true);
        ((LineGraphView) graphView).setDrawDataPoints(true);
        ((LineGraphView) graphView).setDataPointsRadius(6.5f);

        graphView.getGraphViewStyle().setGridColor(Color.GRAY);
        //graphView.getGraphViewStyle().setGridStyle(GraphViewStyle.GridStyle.NONE);
        graphView.getGraphViewStyle().setGridStyle(GraphViewStyle.GridStyle.BOTH);
        graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.WHITE);
        graphView.getGraphViewStyle().setVerticalLabelsColor(Color.WHITE);

        graphView.addSeries(temperatureReadings); // data

        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Date d = new Date((long) value);
                    String hour = DateReadingsUtil.getFormattedDate(d, "HH:mm a");

                    return hour;
                } else {
                    if (scale == Scale.C)
                        return ((int) value) + " C";
                    else
                        return ((int) value) + " F";
                }
            }
        });

        layout = (LinearLayout) findViewById(R.id.temperatureGraph);
        layout.addView(graphView);
    }
    
    public boolean updateGraph() {
        String day = DateReadingsUtil.getFormattedDate(DateReadingsUtil.getCurrentDate(), DateReadingsUtil.FORMAT_BY_DAY);
        boolean b = false;

        ArrayList<TempReading> dayReadings = DateReadingsUtil.getReadingsByDay(day, scale);

        temperatureDataGraph.resetData(new GraphView.GraphViewData [] {});
        if (dayReadings != null) {
            for (TempReading tr : dayReadings) {
                long time = tr.getDate().getTime();
                float temperature = tr.getTemp();
                try {

                    temperatureDataGraph.appendData(new TemperatureDataModel(time, temperature), true, 1000);

                } catch (Exception e) {
                    Log.i("ExceptionTAG", e.toString());
                    e.printStackTrace();
                }

            }
            setCustomIntervals(dayReadings);
            b = true;
        }

        return b;
    }

    public void updateTitle() {
        String day = DateReadingsUtil.getFormattedDate(DateReadingsUtil.getCurrentDate(), DateReadingsUtil.FORMAT_BY_DAY);

        //updates title of the graph
        graphView.setTitle(day);
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setNumberPickerTextColor", e);
                }
            }
        }
        return false;
    }

    public void displayButtons() {
        final Button btn_convert = new Button(this);
        btn_convert.setText("Change Scale");
        Button btn_threshold = new Button(this);
        btn_threshold.setText("Set Threshold");

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scale == Scale.F) {
                    scale = Scale.C;
                } else
                if (scale == Scale.C) {
                    scale = Scale.F;
                }

                updateGraph();
            }
        });

        btn_threshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    AlertDialog.Builder alert = new AlertDialog.Builder(TemperatureActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

                    View view = View.inflate(TemperatureActivity.this, R.layout.threshold_dialog, null);
                    //final EditText inputText = (EditText) TemperatureActivity.view.findViewById(R.id.threshold);
                    //final TextView numberView = (TextView) view.findViewById(R.id.numberview);

                    NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberpicker);
                    String[] values=new String[27];
                    values[0] = "Reset";
                    for(int i=1;i<values.length;i++){
                        values[i]=Integer.toString(i*5)+" F";
                    }
                    numberPicker.setDisplayedValues(values);
                    numberPicker.setMaxValue(values.length - 1);
                    numberPicker.setMinValue(0);

                    numberPicker.setWrapSelectorWheel(true);
                    setNumberPickerTextColor(numberPicker, Color.WHITE);
                    numberPicker.setOnValueChangedListener(new NumberPicker.
                            OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker picker, int
                                oldVal, int newVal) {
                            if (newVal == 0) {
                                temp_threshold = null;
                                Log.i("ThresholdTag", "Temp Threshold null");
                            }
                            else {
                                temp_threshold = newVal * 5;
                                Log.i("ThresholdTag", "Temp Threshold set: "+temp_threshold);
                            }

                        }
                    });


                    //TextView label = (TextView) view.findViewById(R.id.labelCF);
                    //label.setText(" F");

                    alert.setTitle("Set Threshold");
                    alert.setView(view);

                    AlertDialog.Builder ok = alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (temp_threshold != null)
                                threshold = temp_threshold.intValue();
                            else
                                threshold = null;

                            if (threshold != null)
                                Log.i("ThresholdTag", "Definite Threshold set: "+threshold);
                            else
                                Log.i("ThresholdTag", "Definite Threshold null");
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.buttons);
        layout.addView(btn_convert);
        layout.addView(btn_threshold);
    }

    public void switchGraph(Date res) {
        if(res != null){
            DateReadingsUtil.setCurrentDate(res);
            updateGraph();
            updateTitle();
        }
        else if (res == null){
            Toast.makeText(TemperatureActivity.this, "End of readings", Toast.LENGTH_SHORT).show();
        }
    }

    public void detectSwipes() {
        layout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                Log.i("SwipeTest","swiped left");
                //Toast.makeText(TemperatureActivity.this, "Left", Toast.LENGTH_SHORT).show();

                Date res = DateReadingsUtil.getNextAvailableDate();
                switchGraph(res);
            }

            @Override
            public void onSwipeRight() {
                Log.i("SwipeTest","swiped right");
                //Toast.makeText(TemperatureActivity.this, "Right", Toast.LENGTH_SHORT).show();

                Date res = DateReadingsUtil.getPreviousAvailableDate();
                switchGraph(res);
            }

            @Override
            public void onSwipeDown() {
                //Toast.makeText(TemperatureActivity.this, "Down", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeUp() {
                //Toast.makeText(TemperatureActivity.this, "Up", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        graphView = new LineGraphView(this, "");
        ArrayList<TempReading> result = new ArrayList<TempReading>();
        temperatureDataGraph = new GraphViewSeries(new GraphView.GraphViewData[]{});
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
                path = "http://"+ip_address+"/RaspberryPi/Temperature.json";
            }
        } else {
            ip_address= (String) savedInstanceState.getSerializable("IP_ADDRESS");
            username=(String) savedInstanceState.getSerializable("USERNAME");
            password=(String) savedInstanceState.getSerializable("PASSWORD");
            path = "http://"+ip_address+"/RaspberryPi/Temperature.json";
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

    public List<String> detectThreshold(List<TempReading> latestReadings) {

        List<String> ocurrences = new ArrayList<String>();

        for (TempReading reading : latestReadings) {
            if (reading.getTemp() >= threshold) {
                Date date = reading.getDate();
                String formattedDate = DateReadingsUtil.getFormattedDate(date, DateReadingsUtil.FORMAT_TO_DISPLAY);
                Log.i("ThresholdTag", "Threshold reached at: " + formattedDate);
                ocurrences.add(formattedDate);
            }
        }

        return ocurrences;
    }

    public void displayOcurrences (List<String> ocurrences) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Threshold Reached")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do something
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert);

        String message = "Maximum temperature of " + threshold + " reached at: \n\n";
        for (String o : ocurrences)
            message += o +"\n";

        Log.i("ThresholdTag", "Message: " + message);
        alert.setMessage(message);
        alert.show();
    }

    //result list contains new results only
    public void onTempResult(List<TempReading> rawResult, long iteration) {

        if (rawResult != null && !rawResult.isEmpty()) {
            List<TempReading> roundedResult = DateReadingsUtil.roundToXMinutes(rawResult, 10, DateReadingsUtil.FORMAT_BY_MINUTE);

            //update latestReading
            latestReading = rawResult.get(rawResult.size() - 1);

            for (TempReading tr : roundedResult) {
                DateReadingsUtil.buildReadingsByDay(tr);
            }
            Log.i("onTempResultTAG", "Raw Result: " + Arrays.asList(rawResult).toString());
            Log.i("onTempResultTAG", "Latest final reading: " + latestReading.getId() + "||" + latestReading.getDate());

            //if it is the first time data has been gathered
            if (iteration == 1) {
                DateReadingsUtil.setCurrentDate(latestReading.getDate());

                progressBar.setVisibility(View.INVISIBLE);
                txtLoading.setVisibility(View.INVISIBLE);

                displayGraph(temperatureDataGraph);
                displayButtons();
                detectSwipes();

            }
            else
            if (threshold != null) {
                Log.i("ThresholdTag", "Threshold set is : " + threshold);
                List<String> thresholdOcurrences = detectThreshold(rawResult);
                if (!thresholdOcurrences.isEmpty()) {
                    //display dialog
                    Log.i("ThresholdTag", "Threshold reached! Ocurrences are: " + Arrays.asList(thresholdOcurrences).toString());

                    displayOcurrences(thresholdOcurrences);
                } else
                    Log.i("ThresholdTag", "Threshold of " + threshold + " was not reached yet");
            } else
                Log.i("ThresholdTag", "Threshold null");

            updateGraph();
            updateTitle();
        }
        else
            Log.i("ThresholdTAG","There are no new readings yet");

        //run again, passing the latestReading id as parameter
        runAgain(this, latestReading.getId(), ++iteration);
    }

    public void runAgain(TemperatureActivity activity, int latestReadingId, long iteration) {
        JsonTask task = new JsonTask(activity, latestReadingId, iteration, username, password);
        task.execute(TemperatureActivity.path);
    }

    public void setProgressMessage(final String s){

        txtLoading.post(new Runnable() {
            public void run() {
                txtLoading.setText(s);
            }
        });
        //this.txtLoading.setText(s);
    }

}
