/*射者背景色的4种方法：
 drawARGB(int a, int r, int g, int b)
 drawColor(int color)
 drawRGB(int r, int g, int b)
 drawPaint(Paint paint)
 可以画的几何图形：
    canvas.drawPoint（点）
    canvas.drawLine（线）
    canvas.drawRect（矩形）
    canvas.drawRoundRect（圆角矩形）
    canvas.drawCircle（圆）
    canvas.drawOval（椭圆）
    canvas.drawArc （扇形）
    canvas.drawVertices（顶点）
    cnavas.drawPath（路径）
 */
package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PlotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout root = (LinearLayout) findViewById(R.id.rt);
        TextView tv = new TextView(this);
        tv.setText("点");
        DotView dv = new DotView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                300);

        root.addView(dv, params);
        params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                300);
        LineView lv = new LineView(this);
        root.addView(lv, params);

        RectView rv = new RectView(this);
        root.addView(rv, params);

        CircleView cv = new CircleView(this);
        root.addView(cv, params);

        PathView pv = new PathView(this);
        params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                800);
        root.addView(pv, params);

        PathMiscView pmv = new PathMiscView(this);
        params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                750);
        root.addView(pmv, params);

        RegionView rgnv = new RegionView(this);
        params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                1100);
        root.addView(rgnv, params);
    }

    public class DotView extends View {

        Context m_context;

        public DotView(Context context) {
            super(context);
            m_context = context;
        }

        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //设置画笔基本属性
            Paint paint = new Paint();
            paint.setAntiAlias(true);        //设置是否抗锯齿
            paint.setAlpha(255);             //设置画笔透明度,0~255
            paint.setColor(Color.RED);       //设置画笔颜色，
            /*替代方法：同时设置透明度和颜色
            setARGB(int a, int r, int g, int b)*/
            paint.setStyle(Paint.Style.FILL);//设置填充样式：Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
            paint.setStrokeWidth(15);        //设置画笔宽度，若为0则为1像素，且不受Canvas缩放影响
            //设置画布背景颜色
            canvas.drawRGB(255, 255, 255);
            //画点（50，50）
            canvas.drawPoint(50, 50, paint);

            //画点（50，150）、（150，150）、（250，150）、（350，150）、（450，150）
            float[] pts1 = {50, 150, 150, 150, 250, 150, 350, 150, 450, 150};
            canvas.drawPoints(pts1, paint);

            //画点（150，250）、（250，250）、（350，250）（跳过前面2个数值，画后面6个数值）
            float[] pts2 = {50, 250, 150, 250, 250, 250, 350, 250, 450, 250};
            canvas.drawPoints(pts2, 2, 6, paint);
        }

    }

    public class LineView extends View {

        Context m_context;

        public LineView(Context context) {
            super(context);
            m_context = context;
        }


        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //设置画笔基本属性
            Paint paint = new Paint();
            paint.setAntiAlias(true);//抗锯齿功能
            paint.setColor(Color.RED);  //设置画笔颜色
            paint.setStyle(Paint.Style.FILL);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
            paint.setStrokeWidth(10);//设置画笔宽度

            //设置画布背景颜色
            canvas.drawColor(Color.LTGRAY);

            canvas.drawLine(50, 50, 500, 50, paint);
            //画点
            float[] pts1 = {50, 150, 300, 150, 400, 150, 600, 150};
            canvas.drawLines(pts1, paint);

            float[] pts2 = {50, 250, 300, 250, 400, 250, 600, 250, 800, 250, 1000, 250};
            canvas.drawLines(pts2, 2, 8, paint);
        }

    }

    public class RectView extends View {


        public RectView(Context context) {
            super(context);
        }

        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //设置画笔基本属性
            Paint paint = new Paint();
            paint.setAntiAlias(true);//抗锯齿功能
            paint.setColor(Color.RED);  //设置画笔颜色
            paint.setStyle(Paint.Style.FILL);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
            paint.setStrokeWidth(5);//设置画笔宽度

            //设置画布背景颜色
            canvas.drawColor(Color.WHITE);

            //画圆
            canvas.drawRect(50, 50, 250, 250, paint);//直接构造

            RectF rect1 = new RectF(300, 50, 500, 250);
            canvas.drawRect(rect1, paint);//使用RectF构造

            Rect rect2 = new Rect(550, 50, 750, 250);
            canvas.drawRect(rect2, paint);//使用Rect构造

            RectF rect3 = new RectF(800, 50, 1000, 250);
            canvas.drawRoundRect(rect3, 50, 50, paint);//只能用RectF构造
        }

    }

    public class CircleView extends View {
        private static final float SWEEP_INC = 2;
        private static final float START_INC = 15;
        private Paint[] mPaints;
        private boolean[] mUseCenters;
        private float mStart;
        private float mSweep;
        private int mBigIndex;

        public CircleView(Context context) {
            super(context);

            mPaints = new Paint[4];
            mUseCenters = new boolean[4];

            mPaints[0] = new Paint();
            mPaints[0].setAntiAlias(true);
            mPaints[0].setStyle(Paint.Style.FILL);
            mPaints[0].setColor(0x88FF0000);
            mUseCenters[0] = false;

            mPaints[1] = new Paint(mPaints[0]);
            mPaints[1].setColor(0x8800FF00);
            mUseCenters[1] = true;

            mPaints[2] = new Paint(mPaints[0]);
            mPaints[2].setStyle(Paint.Style.STROKE);
            mPaints[2].setStrokeWidth(4);
            mPaints[2].setColor(0x880000FF);
            mUseCenters[2] = false;

            mPaints[3] = new Paint(mPaints[2]);
            mPaints[3].setColor(0x88888888);
            mUseCenters[3] = true;

        }

        //重写OnDraw()函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //设置画笔基本属性
            Paint paint = new Paint();
            paint.setAntiAlias(true);//抗锯齿功能
            paint.setColor(Color.RED);  //设置画笔颜色
            paint.setStyle(Paint.Style.FILL);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
            paint.setStrokeWidth(5);//设置画笔宽度

            //设置画布背景颜色
            canvas.drawColor(Color.LTGRAY);

            //画圆
            canvas.drawCircle(150, 150, 100, paint);

            RectF rect1 = new RectF(300, 50, 600, 200);
            canvas.drawOval(rect1, paint);

            RectF rect2 = new RectF(600, 50, 800, 200);
            canvas.drawArc(rect2, 0, 130, true, paint);

            RectF rect3 = new RectF(800, 50, 1000, 200);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(rect3, 0, 130, false, paint);

            //来自Google的APIDemo
            canvas.drawArc(1100, 30, 1350, 270, mStart, mSweep, mUseCenters[mBigIndex],
                    mPaints[mBigIndex]);

            mSweep += SWEEP_INC;
            if (mSweep > 360) {
                mSweep -= 360;
                mStart += START_INC;
                if (mStart >= 360) {
                    mStart -= 360;
                }
                mBigIndex = (mBigIndex + 1) % mPaints.length;
            }
            invalidate();
        }
    }

    public class PathView extends View {


        public PathView(Context context) {
            super(context);
        }

        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //设置画笔基本属性
            Paint paint = new Paint();
            paint.setAntiAlias(true);//抗锯齿功能
            paint.setColor(Color.RED);  //设置画笔颜色
            paint.setStyle(Paint.Style.STROKE);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
            paint.setStrokeWidth(5);//设置画笔宽度

            //设置画布背景颜色
            canvas.drawColor(Color.WHITE);

            //画多边形
            Path path1 = new Path();
            path1.moveTo(100, 50); //设定起始点
            path1.lineTo(50, 200);//第一条直线的终点，也是第二条直线的起点
            path1.lineTo(300, 200);//画第二条直线
            path1.close();//闭环
            canvas.drawPath(path1, paint);
            //画方
            Path path2 = new Path();
            RectF rect1 = new RectF(350, 50, 550, 200);
            path2.addRect(rect1, Path.Direction.CCW);
            canvas.drawPath(path2, paint);
            Path path3 = new Path();
            rect1 = new RectF(600, 50, 800, 200);
            path3.addRoundRect(rect1, 50, 50, Path.Direction.CW);
            RectF rect2 = new RectF(850, 50, 1050, 200);
            float radii[] = {10, 15, 20, 25, 30, 35, 40, 45};
            path3.addRoundRect(rect2, radii, Path.Direction.CCW);
            canvas.drawPath(path3, paint);
            //画圆
            Path path4 = new Path();
            path4.addCircle(150, 400, 100, Path.Direction.CCW);
            canvas.drawPath(path4, paint);
            path4 = new Path();
            RectF rect = new RectF(300, 350, 500, 450);
            path4.addOval(rect, Path.Direction.CCW);
            canvas.drawPath(path4, paint);
            path4.reset();
            RectF rect3 = new RectF(550, 350, 750, 450);
            path4.addArc(rect3, 100, 180);
            canvas.drawPath(path4, paint);
            RectF rect4 = new RectF(800, 350, 1000, 450);
            path4.arcTo(rect4, 120, -150);
                /*等效：
                path4.arcTo(rect4, 120, -150,false)
                */
            canvas.drawPath(path4, paint);

            Path path5 = new Path();
            path5.moveTo(100, 600);
            path5.quadTo(300, 800, 500, 550);
            canvas.drawPath(path5, paint);
            path5.reset();
            path5.moveTo(600, 550);
            path5.cubicTo(850, 600, 650, 750, 900, 700);
            canvas.drawPath(path5, paint);

        }
    }

    public class PathMiscView extends View {
        public PathMiscView(Context context) {
            super(context);
        }

        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //设置画笔基本属性
            Paint paint = new Paint();
            paint.setAntiAlias(true);//抗锯齿功能
            paint.setColor(Color.RED);  //设置画笔颜色
            paint.setStyle(Paint.Style.STROKE);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
            paint.setStrokeWidth(5);//设置画笔宽度

            //设置画布背景颜色
            canvas.drawColor(Color.LTGRAY);

            //addPath
            Path path = new Path();
            path.addRect(100, 50, 400, 250, Path.Direction.CW);
            Path src = new Path();
            src.addCircle(300, 250, 100, Path.Direction.CW);
            path.addPath(src, 0, 100);
            canvas.drawPath(path, paint);
            //offset
            path.reset();
            path.addCircle(800, 150, 100, Path.Direction.CW);
            Path dst = new Path();
            dst.addCircle(900, 150, 100, Path.Direction.CW);
            path.offset(-100, 100, dst);
            paint.setColor(Color.RED);
            canvas.drawPath(path, paint);
            paint.setColor(Color.BLUE);
            canvas.drawPath(dst, paint);
            //setFillType
            paint.setStyle(Paint.Style.FILL);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
            paint.setColor(Color.RED);
            path.reset();
            path.addCircle(300, 600, 100, Path.Direction.CW);
            path.addCircle(200, 600, 100, Path.Direction.CW);
            path.setFillType(Path.FillType.WINDING);
            canvas.drawPath(path, paint);

            //setFillType
            path.reset();
            path.addCircle(750, 600, 100, Path.Direction.CW);
            path.addCircle(650, 600, 100, Path.Direction.CW);
            path.setFillType(Path.FillType.EVEN_ODD);
            canvas.drawPath(path, paint);
        }
    }


    /*
    public Region()
    public Region(Region region)
    public Region(Rect r)
    public Region(int left, int top, int right, int bottom)

    public void setEmpty()  //清空
    public boolean set(Region region)
    public boolean set(Rect r)
    public boolean set(int left, int top, int right, int bottom)
    public boolean setPath(Path path, Region clip)  //将path和clip的两个区域取交集

    */
    public class RegionView extends View {
        public RegionView(Context context) {
            super(context);
        }

        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //设置画笔基本属性
            Paint paint = new Paint();
            paint.setAntiAlias(true);//抗锯齿功能
            paint.setColor(Color.RED);  //设置画笔颜色
            paint.setStyle(Paint.Style.STROKE);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
            paint.setStrokeWidth(5);//设置画笔宽度

            //设置画布背景颜色
            canvas.drawColor(Color.WHITE);

            Region rgn = new Region(50, 50, 200, 200);

            /*替代
              rgn.set(50,50,200,200); */
            drawRegion(canvas, rgn, paint);
            Path ovalPath = new Path();
            RectF rect = new RectF(350, 50, 650, 200);
            ovalPath.addOval(rect, Path.Direction.CCW);
            //SetPath时,传入一个比椭圆区域小的矩形区域,让其取交集
            rgn.setPath(ovalPath, new Region(350, 50, 550, 200));
            //画出路径
            drawRegion(canvas, rgn, paint);


            ovalPath.reset();
            rect = new RectF(700, 50, 850, 400);
            ovalPath.addOval(rect, Path.Direction.CCW);
            //SetPath时,传入一个比椭圆区域小的矩形区域,让其取交集
            rgn.setPath(ovalPath, new Region(700, 50, 850, 400));
            //画出路径
            drawRegion(canvas, rgn, paint);


            Rect rect1 = new Rect(50, 500, 350, 600);
            Rect rect2 = new Rect(150, 400, 250, 700);
            canvas.drawRect(rect1, paint);
            canvas.drawRect(rect2, paint);
            drawRegionOperation(rect1, rect2, Region.Op.INTERSECT, canvas);

            rect1 = new Rect(400, 500, 700, 600);
            rect2 = new Rect(500, 400, 600, 700);
            canvas.drawRect(rect1, paint);
            canvas.drawRect(rect2, paint);
            drawRegionOperation(rect1, rect2, Region.Op.UNION, canvas);

            rect1 = new Rect(750, 500, 1050, 600);
            rect2 = new Rect(850, 400, 950, 700);
            canvas.drawRect(rect1, paint);
            canvas.drawRect(rect2, paint);
            drawRegionOperation(rect1, rect2, Region.Op.XOR, canvas);

            rect1 = new Rect(50, 850, 350, 950);
            rect2 = new Rect(150, 750, 250, 1050);
            canvas.drawRect(rect1, paint);
            canvas.drawRect(rect2, paint);
            drawRegionOperation(rect1, rect2, Region.Op.DIFFERENCE, canvas);

            rect1 = new Rect(400, 850, 700, 950);
            rect2 = new Rect(500, 750, 600, 1050);
            canvas.drawRect(rect1, paint);
            canvas.drawRect(rect2, paint);
            drawRegionOperation(rect1, rect2, Region.Op.REVERSE_DIFFERENCE, canvas);

            rect1 = new Rect(750, 850, 1050, 950);
            rect2 = new Rect(850, 750, 950, 1050);
            canvas.drawRect(rect1, paint);
            canvas.drawRect(rect2, paint);
            drawRegionOperation(rect1, rect2, Region.Op.REPLACE, canvas);
        }

        private void drawRegion(Canvas canvas, Region rgn, Paint paint) {
            RegionIterator iter = new RegionIterator(rgn);
            Rect r = new Rect();

            while (iter.next(r)) {
                canvas.drawRect(r, paint);
            }
        }

        private void drawRegionOperation(Rect rect1, Rect rect2, Region.Op op, Canvas canvas) {
            Region region1 = new Region(rect1);
            Region region2 = new Region(rect2);
            region1.op(region2, op);
            Paint paint_fill = new Paint();
            paint_fill.setColor(Color.GREEN);
            paint_fill.setStyle(Paint.Style.FILL);
            drawRegion(canvas, region1, paint_fill);
        }
    }
}