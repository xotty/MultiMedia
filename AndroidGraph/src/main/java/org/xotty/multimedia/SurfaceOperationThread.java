package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.util.Random;

// 绘制线程
public class SurfaceOperationThread extends Thread {
    private SurfaceHolder holder;
    private boolean run;
    private int screenWidth;
    private static final int  w=500;
    private static final int  h=300;


    public SurfaceOperationThread(SurfaceHolder holder,int width) {
        this.holder = holder;
        this.screenWidth=width;
        run = true;
    }

    @Override
    public void run() {
        int counter = 0;
        Canvas canvas = null;
        while (run) {
            // 具体绘制工作
            try {
                // 获取Canvas对象，并锁定之
                canvas = holder.lockCanvas();

                // 设定Canvas对象的背景颜色
                canvas.drawColor(Color.WHITE);

                // 创建画笔
                Paint p = new Paint();
                // 设置画笔颜色
                p.setColor(getRandColor());
                // 设置文字大小
                p.setTextSize(30);

                // 创建一个Rect对象rect
                Rect rect = new Rect((screenWidth-w)/2,200, (w+screenWidth)/2, 200+h);
                // 在canvas上绘制rect
                canvas.drawRect(rect, p);
                p.setTextSize(60);
                p.setTextAlign(Paint.Align.CENTER);
                // 在canvas上显示时间
                canvas.drawText("Interval = " + (counter++) + " seconds.", screenWidth/2, 200+h+100, p);

                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    // 解除Canvas锁定，并提交修改内容，将图形显示出来
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public int getRandColor() {
        Random mRandom = new Random();
        int ranColor = 0xff000000 | mRandom.nextInt(0x00ffffff);
        return ranColor;
    }
}

