package com.fch.ball;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.fch.ball.Thread.GameThread;

public class Surface extends SurfaceView
        implements SurfaceHolder.Callback,SensorEventListener
{

    private SurfaceHolder holder;
    private GameThread updateThread;
    private SensorManager sensorManager;
    public int level =1;
    public float[] accelerometerValues = new float[3];
    public float[] magneticFieldValues = new float[3];
    public Bitmap bitmap[]=new Bitmap[5];
    // 下面定义2个变量，记录鱼的初始位置


    public float touchX;
    public float touchY;

    private Context ctx;
    public static int mode1Score =0;
    public static int mode2Score =0;

    public Surface(Context ctx, AttributeSet set)
    {
        super(ctx, set);
        this.ctx=ctx;
        holder = getHolder();
        holder.addCallback(this);
        sensorManager= (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        bitmap[0] =BitmapFactory.decodeResource(getResources(),R.drawable.ic_menu_home);
        bitmap[1] =BitmapFactory.decodeResource(getResources(),R.drawable.ic_media_embed_play);
        bitmap[2] =BitmapFactory.decodeResource(getResources(),R.drawable.ic_menu_home);
        Log.d("11","222");



    }

    public void resume()
    {
        // 创建和启动图像更新线程
        if (updateThread == null)
        {

            switch(level)
            {
                case 1:updateThread = new GameThread(this,holder,1);break;
                case 2:updateThread = new GameThread(this,holder,2);break;
                default:updateThread = new GameThread(this,holder,1);break;
            }
            updateThread.start();

        }
        updateThread.draw =true;
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);


    }
    // 当SurfaceView被创建时回调该方法
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {

        resume();
        Log.d("111","surfaceCreated()");
    }

    // 当SurfaceView将要被销毁时回调该方法
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        updateThread.requestExitAndWait();
///////////////////////////////
        Log.d("111","surfaceDestroyed()");
    }

    // 当SurfaceView发生改变时回调该方法
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
    }

    @Override//传感器
    public void onSensorChanged(SensorEvent event) {
    if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
        accelerometerValues=event.values.clone();
    else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
        magneticFieldValues=event.values.clone();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    public void destroyed()
    {
        updateThread.alive =false;//ok
        sensorManager.unregisterListener(this);
    }
    /**creak by 2/16**/
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                touchX =event.getX();
                touchY =event.getY();
                break;
        }
        return true;
    }
    //在线程里面启动activity一直卡死？
    public void startAty()
    {
        Intent intent = new Intent(ctx,MainActivity.class);
        ctx.startActivity(intent);
    }

}
