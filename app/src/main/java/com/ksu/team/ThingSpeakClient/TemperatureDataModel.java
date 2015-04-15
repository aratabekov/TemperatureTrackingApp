package com.ksu.team.ThingSpeakClient;

import com.jjoe64.graphview.GraphViewDataInterface;

import java.math.BigDecimal;

/**
 * Created by Marcel on 31-Oct-14.
 */
public class TemperatureDataModel implements GraphViewDataInterface {
        private long time;
        private float temperature;

        public TemperatureDataModel(long time, float temp) {
            this.time = time;
            this.temperature = temp;
        }

        @Override
        public double getX() {
            //new BigDecimal(time).set
            return time;
        }

        @Override
        public double getY() {
            temperature = new BigDecimal(temperature).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

            return temperature;
        }
    }
