package com.example.myapplication;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Random;

import static java.lang.System.nanoTime;

public class MyThread extends Thread {

    private final Random random = new Random();

    public static final int FRACTION_TIME = 800000;

    private ArgbEvaluator argbEvaluator;

    private Paint paint;

    private SurfaceHolder surfaceHolder;

    private boolean flag;

    private long startTime;

    private long buffRedrawTime;
    MyThread(SurfaceHolder h) {
        flag = false;
        surfaceHolder = h;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        argbEvaluator = new ArgbEvaluator();
    }

    public void setRunning(boolean f) {
        this.flag = f;
    }

    @Override
    public void run() {
        Canvas canvas;
        startTime = getTime();
        while (flag) {
            long currTime = getTime();
            long elapsedTime = currTime - buffRedrawTime;
            if(elapsedTime < 1000){
                continue;
            }
            canvas = surfaceHolder.lockCanvas();

            drawCircles(canvas);

            surfaceHolder.unlockCanvasAndPost(canvas);

            buffRedrawTime = getTime();
        }
        //super.run();
    }
    public long getTime() {
        return System.nanoTime()/1000;
    }
    public void drawCircles(Canvas canvas) {
        long currentTime = getTime();
        int centerX = canvas.getWidth()/2;
        int centerY = canvas.getHeight()/2;
        canvas.drawColor(Color.BLACK); //фон
        float maxRadius = Math.min(canvas.getHeight(), canvas.getWidth())/2;
        Log.d("RRRR maxRadius=", Float.toString(maxRadius));
        float fraction = (float)(currentTime%FRACTION_TIME)/FRACTION_TIME;
        Log.d("RRRR  fraction=", Float.toString(fraction));
        int color = (int)argbEvaluator.evaluate(fraction, Color.RED, Color.BLACK);
        if(currentTime % 3 == 1){
            color = (int)argbEvaluator.evaluate(fraction, Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)), Color.BLACK);
        }
        else if(currentTime % 3 == 2){
            color = (int)argbEvaluator.evaluate(fraction, Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)), Color.BLACK);
        }
        else {
            color = (int)argbEvaluator.evaluate(fraction, Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)), Color.BLACK);
        }
        //int color = (int)argbEvaluator.evaluate(fraction, Color.RED, Color.BLACK);
        Log.d("RRRR  color=", Float.toString(color));
        paint.setColor(color);
        canvas.drawCircle(centerX, centerY, maxRadius*fraction, paint);

    }
}

