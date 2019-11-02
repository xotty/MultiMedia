/*Canvas对象的获取方式有三种：
  1.重写View.onDraw方法，View中的Canvas对象会被当做参数传递过来
  2.new Canvas(),将其设置到一个Bitmap上去
  3.调用SurfaceHolder.lockCanvas()，返回一个Canvas对象
 */
package org.xotty.multimedia;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class GraphUtil  {


    private static final float SIZE = 1000;
    private static final int SEGS = 32;
    private static final int X = 0;
    private static final int Y = 1;

    public static void drawGraph(Canvas canvas,float[] mPts){
        //使用Canvas绘图
        //画布移动到（10，10）位置
        canvas.translate(10, 10);
        //画布使用灰色填充背景
        canvas.drawColor(Color.LTGRAY);
        //创建红色画笔，使用带像素宽度，绘制直线
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(0);
        canvas.drawLines(mPts, paint);
        //创建蓝色画笔，宽度为 3，绘制相关点
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        canvas.drawPoints(mPts, paint);
        //创建 Path,并沿着 path 显示文字信息
        RectF rect = new RectF(10, 1000, 1000, 1500);
        Path path = new Path();
        path.addArc(rect, -180, 180);
        paint.setTextSize(40);
        paint.setColor(Color.BLUE);
        canvas.drawTextOnPath("行到水穷处，坐看云起时", path, 0, 0, paint);
    }


   public static float[] buildPoints() {
        //生成一系列的点
        final int ptCount = (SEGS + 1) * 2;
        float[] mPts = new float[ptCount * 2];
        float value = 0;
        final float delta = SIZE / SEGS;
        for (int i = 0; i <= SEGS; i++) {
            mPts[i * 4 + X] = SIZE - value;
            mPts[i * 4 + Y] = 0;
            mPts[i * 4 + X + 2] = 0;
            mPts[i * 4 + Y + 2] = value;
            value += delta;
        }
        return mPts;
    }
}


