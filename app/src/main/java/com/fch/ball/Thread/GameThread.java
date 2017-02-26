package com.fch.ball.Thread;

/**
 * Created by mind on 2017/2/13.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.SurfaceHolder;

import com.fch.ball.Ball;
import com.fch.ball.Surface;
import com.fch.ball.Wall;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

/**
2/13 重新创建一个类，让其他Thread继承
 */

public class  GameThread extends Thread {
    public static  float RESOLUTION_X = 1920;
    public static  float RESOLUTION_Y = 1080;
    public boolean draw;
    private Paint paint;
    private Paint paintBack;

    private Surface surface;
    public float[] WallLines = new float[13 * 4];
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    private Wall wall;
    private Ball ball;
    public boolean alive;
    private boolean life = true;

    @ColorInt
    private int colorDieBack = 0x44444444;
    private Paint paintEndBack ;
    Rect rect[] = new Rect[4];

    private ArrayList<float[]> lines;
    ListIterator<float[]> it;
    private Paint paintEndText;
    private   int lineSpeed = 10;
    private   int lineTime = 50;
    private int score=0;
    private float ratio= (float) 1.0;//系数
    private int mode =1;
    public GameThread(Surface surface, SurfaceHolder holder,int mode) {
        super();
        draw = true;
        alive = true;
        paintBack = new Paint();
        paintBack.setColor(0xfdfcfcfc);
        surfaceHolder = holder;
        this.surface = surface;
        writeWallLines();
        wall = new Wall(WallLines);
        ball = new Ball(RESOLUTION_X / 2, RESOLUTION_Y - 80);

        Log.d("ddd","ddd");
        setEnd();
        setList();
        this.mode=mode;
        setMode(mode);





    }

    @Override
    public void run()
    {
        float R[] = new float[9];//传感器中间
        float Values[] = new float[3];//传感器最终数据
        score=0;
        // 重复绘图循环，直到线程停止
        while (alive)
        {
            if (draw)
            {
                SensorManager.getRotationMatrix(R, null, surface.accelerometerValues, surface.magneticFieldValues);
                SensorManager.getOrientation(R, Values);
                try//排除canvas异常，但是里面要避免除canvas不可视异常外的异常产生
                {
                    canvas = surfaceHolder.lockCanvas();
                    if(canvas==null)//当Surface被破坏时返回null，导致下面异常
                        continue;
                } catch (NullPointerException e) {e.printStackTrace();}
                try
                {
                    canvas.drawRect(0, 0,RESOLUTION_X, RESOLUTION_Y, paintBack);//清屏
                    canvas.drawBitmap(surface.bitmap[2],null,rect[2],null);
                    write(canvas, wall);
                    if (life)
                    {

                        rule(Values[1]);//根据小球之前位置推出现在位置
                    }
                    ball.drawBall(canvas);//更新小球
                    life = isBallAlive();
                    end(life);
                }
                catch (NullPointerException e) {e.printStackTrace();}
                try//排除canvas异常，但是里面要避免除canvas不可视异常外的异常产生
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } catch (NullPointerException e) {}


            }
            try
            {
                Thread.sleep(15);//当前线程还是全部线程？
            } catch (InterruptedException e) {}

        }
    }

    public void requestExitAndWait() {
        draw = false;
    }

    private void rule(double values)//小球行动方式
    {

        values = Math.toDegrees(values);
        if (true)
        {
            ball.t2 = 0;
            if (Math.abs(values) > 7 && Math.abs(values) < 70) {
                ball.vt = (float) ((values)*ratio);
            }
            else if(Math.abs(values)<4)
                ball.vt=0;

            ball.x -= ball.vt;

        }

    }


    public void writeWallLines() {
    }

    public void write(Canvas canvas, Wall wall)//画线，被继承,float[]0-3,正常线，4-5缺口
    {
        if(life) {
            wall.t++;
            if (wall.t > lineTime)
            {
                Random random = new Random();
                int gapStart = random.nextInt((int) (RESOLUTION_X) - Wall.gap);
                if(mode==2&&score<=4000&&RESOLUTION_X>1700)
                {
                    gapStart=random.nextInt(1500)+200;
                }
                lines.add(new float[]{0, 0, RESOLUTION_X, 0, gapStart, gapStart + Wall.gap});
                wall.t = 0;
//            Log.d("111",""+lines.size());
            }
            changeLines();
        }
        drawLines(canvas);

    }
    private void setList()
    {
        lines = new ArrayList<>();
    }

    public void changeLines()//list里面的线向下移动
    {
        for(it= lines.listIterator(); it.hasNext();)//让在list里的float 改变
        {
            float[] get=it.next();
            get[3]= get[1]=get[1]+lineSpeed;//类似于指针
            if(get[3]>RESOLUTION_Y)
            {
                it.remove();

                mode(mode);

            }
        }
    }
    public void setMode(int mode)
    {
        if(mode==1)
        {
            lineSpeed = 10;
            lineTime = 50;
            score = 0;
            ratio = (float) 1.0;//系数
            ball.setR(50);
            Wall.gap =50*7;
        }
        else if(mode==2)
        {
            lineSpeed = 4;
            lineTime = 50;
            score=0;
            ratio= (float)1;//系数
            ball.setR(10);
            Wall.gap =50*3;
        }
    }
    public void mode(int mode)
    {
        if(mode==1)
        {
            score+=100;
            if(score<=4000) {
                lineSpeed = score / 1000 + 10;//根据score改变
                lineTime = 500 / lineSpeed;
                ratio = (float) (lineSpeed / 10.0);
            }
            else if(score<=8000)
            {
                lineSpeed = 6+ 10+(score-4000) / 1500 ;//根据score改变
                lineTime = 500 / lineSpeed;
                ratio = (float) (lineSpeed / 10.0);
            }
            else if (score<=9000)
            {
                lineSpeed = 20+(score-8000) / 2500;//根据score改变
                lineTime = 500 / lineSpeed;
                ratio = (float) (lineSpeed / 10.0);
            }
            else
                ;
        }
        if(mode==2)
        {
            score+=50;
            if(score<4000)
                Wall.gap =50*3;
            else if (score<5000)
            {
                Wall.gap =50*2;
                ratio= (float) 1.5;
            }
            else if (score<6000)
                Wall.gap =50*1;
        }
    }
    public void drawLines(Canvas canvas)
    {
        for(int i = 0; i< lines.size(); i++)
        {
            wall.drawLine(canvas, lines.get(i));
        }
    }
    private boolean isBallAlive()
    {
        for(it=lines.listIterator();it.hasNext();)
        {
            float[] get=it.next();
            if(Math.abs(ball.y-get[1])<ball.R) //判断是否和整条线相交
            {
                double l= Math.sqrt(ball.R*Ball.R-(ball.y-get[1])*(ball.y-get[1]));//l=l=r*r-d*d
                if (ball.x-l>get[4]&&ball.x+l<get[5])//线段在缺口里面
                    ;
                else
                    return false;
            }
        }
        return true;
    }

    //2/16 3小时
    private void  setEnd()
    {
        paintEndBack=new Paint();
        paintEndText=new Paint();
        paintEndBack.setColor(Color.WHITE);
        paintEndText.setStyle(Paint.Style.STROKE);
        paintEndText.setStrokeWidth(3);
        paintEndText.setTextSize(80);
        paintEndText.setColor(Color.BLACK);
        rect[0] = new Rect((int)(RESOLUTION_X*3/10),(int)RESOLUTION_Y*5/10,(int)(RESOLUTION_X*3/10)+200,(int)RESOLUTION_Y*5/10+200);
        rect[1] = new Rect((int)(RESOLUTION_X*6/10),(int)RESOLUTION_Y*5/10+20,(int)(RESOLUTION_X*6/10)+200,(int)RESOLUTION_Y*5/10+200);
        rect[2] = new Rect((int)RESOLUTION_X-120,0,(int)RESOLUTION_X,120);
    }
    private void drawEnd()
    {



        canvas.drawRect((float) (RESOLUTION_X*0.2),(float) (RESOLUTION_Y*0.2),(float) (RESOLUTION_X*0.8),(float) (RESOLUTION_Y*0.7),paintEndBack);
        canvas.drawBitmap(surface.bitmap[0],null,rect[0],null);
        canvas.drawBitmap(surface.bitmap[1],null,rect[1],null);
        canvas.drawText("您的分数是： "+score,(float) (RESOLUTION_X*0.3),(float) (RESOLUTION_Y*0.3),paintEndText);
        canvas.drawText("最高分： "+(mode==1?surface.mode1Score :surface.mode2Score),(float) (RESOLUTION_X*0.3),(float) (RESOLUTION_Y*0.3)+80,paintEndText);

    }

    private int isTouchBtn()//是否触碰按键
    {
        for(int i=0;i<3;i++)//i的参数不能是rect.length,会产生异常
        {
            if(isInRect(surface.touchX,surface.touchY,rect[i]))
            {
                surface.touchX=0;
                surface.touchY=0;
                return i;
            }

        }
        return rect.length+1;
    }
    private boolean isInRect(float x,float y,Rect rect)//判断是否在矩形内
    {
        if(x>rect.left&&x<rect.right&&y>rect.top&&y<rect.bottom)
            return true;
        else
            return false;
    }
    private Handler handler = new Handler() {//非接口
        @Override
        public void handleMessage(Message msg) {//回主线？然后根据按键执行
            switch (msg.what) {
                case 0:
                case 2:
                    surface.startAty();
                    break;
                case 1:ball.setBall(RESOLUTION_X / 2, RESOLUTION_Y - 100);lines= new ArrayList<>();score=0;
                    break;
                default:
                    break;
            }
        }
    };
    private void end(Boolean life)
    {
        Message msg = new Message();//用于返回主进程?
        if(!life)
        {
            if(mode==1)
            {

                if(score>surface.mode1Score)
                {
                    surface.mode1Score=score;
                }
            }
            else if(mode==2)
            {
                if(score>surface.mode2Score)
                {
                    surface.mode2Score=score;
                }
            }

            drawEnd();
            switch (isTouchBtn())
            {
                case 0:
                    msg.what=0;
                    handler.sendMessage(msg);
                    break;
                case 1:
                    msg.what=1;
                    handler.sendMessage(msg);
                    break;
                case 2:
                    msg.what=2;
                    handler.sendMessage(msg);
                    break;
                default:
            }
        }
        if(isTouchBtn()==2)
        {
            msg.what=2;
            handler.sendMessage(msg);
        }


    }
}

