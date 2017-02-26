package com.fch.ball;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.fch.ball.Thread.GameThread;


public class GameActivity extends AppCompatActivity
{
    private Surface surface;
    private static int mark=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        surface = (Surface) findViewById(R.id.SurfaveView);//如果创建多个实例 指向谁
        surface.level=getIntent().getIntExtra("level",1);
        if(mark++==0)
        {
            surface.mode1Score = getData("mode1");
            surface.mode2Score = getData("mode2");
            getXY();
        }
    }


    @Override
    protected void onDestroy()
    { Log.d("111activityLifetime","stat");
        super.onDestroy();
        surface.destroyed();
        keepData();
        Exception e = new Exception();
        Log.d("111activityLifetime",""+e.getStackTrace()[0].getMethodName());
    }
    public void keepData()
    {
        SharedPreferences.Editor editor = getSharedPreferences("data",
                MODE_PRIVATE).edit();
        editor.putInt("mode1", surface.mode1Score);
        editor.putInt("mode2", surface.mode2Score);
        editor.commit();

    }
    public int  getData(String s)
    {
        SharedPreferences pref = getSharedPreferences("data",
                MODE_PRIVATE);
        int score = pref.getInt(s, 0);
        Log.d("555", "age is " + score);
        return score;

    }
    private void getXY()//取分辨率
    {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        GameThread.RESOLUTION_X= displayMetrics.widthPixels ;
        GameThread.RESOLUTION_Y=displayMetrics.heightPixels;
        Log.d("GameAty", "x=  " +  GameThread.RESOLUTION_X+ "  y= " + GameThread.RESOLUTION_Y);
    }

}
