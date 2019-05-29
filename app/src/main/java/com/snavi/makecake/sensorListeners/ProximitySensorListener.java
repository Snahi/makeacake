package com.snavi.makecake.sensorListeners;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ProximitySensorListener implements SensorEventListener {


    private SensorManager m_sensorManager;
    private Sensor        m_sensor;
    private boolean       m_isClose;



    public ProximitySensorListener(Context context)
    {
        m_sensorManager = (SensorManager) context.getSystemService(Service.SENSOR_SERVICE);
        m_sensor        = m_sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        m_sensorManager.registerListener(this, m_sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        m_isClose = sensorEvent.values[0] < 1;
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }



    public boolean isClose()
    {
        return m_isClose;
    }
}
