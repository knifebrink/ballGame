package com.fch.ball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathDashPathEffect;


public class Ball
{
    public float x;
    public float y;
    public  static int  R=50;
    public final  static int  r=5;
    public boolean life;
    public boolean next;
    public float a;
    public float a2;
    public final float G= (float) 2/2;
    public float angle;
    public float t;
    public float t2;
    public float v0;
    public float vt;
    public Paint paint;
    private Paint paint2;
    public Ball(float x,float y)
    {
        this.x=x;
        this.y=y;
        t=0;
        t2=0;
        v0=0;
        life=true;
        next=false;
        paint = new Paint();
        paint2 = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(80);


    }
    public void setBall(float x,float y)
    {
        this.x=x;
        this.y=y;
    }
    public void drawBall(Canvas canvas)
    {
        canvas.drawCircle(x,y,R,paint);
        canvas.drawCircle(x,y,r,paint2);
    }
    public void setR(int R)
    {
        this.R=R;
    }
}
