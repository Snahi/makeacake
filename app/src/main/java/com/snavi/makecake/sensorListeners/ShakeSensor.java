package com.snavi.makecake.sensorListeners;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.snavi.makecake.Activities.MixActivity;

import java.util.ArrayList;

public class ShakeSensor implements SensorEventListener {


    // CONST //////////////////////////////////////////////////////////////////////////////////////
    private static final int MAX_NON_SHAKING_TIME = 300;
    private static final int MIN_ACC_DIFFERENCE   = 5;


    private SensorManager m_sensorManager;
    private Sensor        m_accelerometer;
    private boolean       m_isShaking;
    private long          m_timeOfLastShake;
    private float         m_lastRead0;
    private float         m_lastRead1;
    private float         m_lastRead2;

    private ArrayList<ShakeSensorListener> m_listeners;



    public ShakeSensor(Context context)
    {
        m_sensorManager = (SensorManager) context.getSystemService(Service.SENSOR_SERVICE);
        m_accelerometer = m_sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        m_sensorManager.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        m_listeners = new ArrayList<>();
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        float[] values = sensorEvent.values;
        float diff0 = diff(values[0], m_lastRead0);
        float diff1 = diff(values[1], m_lastRead1);
        float diff2 = diff(values[2], m_lastRead2);

        if (diff0 > MIN_ACC_DIFFERENCE || diff1 > MIN_ACC_DIFFERENCE || diff2 > MIN_ACC_DIFFERENCE)
        {
            m_isShaking = true;
            m_timeOfLastShake = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() - m_timeOfLastShake > MAX_NON_SHAKING_TIME)
        {
            m_isShaking = false;
        }

        notifyListeners();
    }



    private float diff(float read1, float read2)
    {
        if (read1 * read2 > 0)
            return Math.abs(read1 + read2);
        else
            return Math.abs(read1) + Math.abs(read2);
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }



    public boolean isShaking()
    {
        return m_isShaking;
    }



    public void registerListener(ShakeSensorListener listener)
    {
        m_listeners.add(listener);
    }



    public void unregisterListener(ShakeSensorListener listener)
    {
        m_listeners.remove(listener);
    }



    public void notifyListeners()
    {
        for (ShakeSensorListener listener : m_listeners)
        {
            listener.shakeRead(m_isShaking);
        }
    }
}
