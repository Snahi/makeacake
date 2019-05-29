package com.snavi.makecake.cooking;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {


    // CONST ///////////////////////////////////////////////////////////////////////////////////////
    private static final int SLEEP_TIME = 20;


    private boolean                 m_isRunning;
    private ChooseIngredientsView   m_gameSurface;
    private SurfaceHolder           m_surfaceHolder;


    GameThread(ChooseIngredientsView gameSurface)
    {
        this.m_gameSurface   = gameSurface;
        this.m_surfaceHolder = gameSurface.getHolder();
    }



    @Override
    public void run()
    {
        while(m_isRunning)
        {
            Canvas canvas= null;
            try
            {
                canvas = m_surfaceHolder.lockCanvas();

                synchronized (this)
                {
                    this.m_gameSurface.tick();
                    this.m_gameSurface.render(canvas);
                }
            }
            catch(Exception e) {
                // do nothing}
            }
            finally
            {
                if(canvas!= null)
                    this.m_surfaceHolder.unlockCanvasAndPost(canvas);
            }

            try
            {
                sleep(SLEEP_TIME);
            } catch(InterruptedException e) {
                // do nothing
            }
        }
    }



    void setRunning(boolean running)  {
        m_isRunning = running;
    }
}