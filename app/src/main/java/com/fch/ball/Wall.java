package com.fch.ball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Wall
{

    public float[] wallLines =new float[]{};
    public Paint paint;
    public Paint paintDynamic;
    public float v;
    public int t;
    private int color=0xff0080ff;
    public static int gap = Ball.R*7;
    public Wall()
    {
        setPaint();
        setPaintDynamic();
    }
    public  Wall(float[] wallLines)
    {
        this.wallLines=wallLines;
        setPaint();
        setPaintDynamic();
        t=0;
    }
    public void writeWall(float[] wallLines)
    {
        this.wallLines=wallLines;
    }
    private void   setPaintDynamic()
    {
        paintDynamic=new Paint();
        paintDynamic.setColor(Color.BLUE);
        paintDynamic.setStyle(Style.STROKE);
    }
    private void setPaint()
    {
        paint =new Paint();
        paint.setColor(color);

        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(7);
    }
   public void drawLine(Canvas canvas,float[] line)
    {
        canvas.drawLine(line[0],line[1],line[4],line[3],paint);
        canvas.drawLine(line[5],line[1],line[2],line[3],paint);
    }
}