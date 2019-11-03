/**
 * Paint有3种初始化方式：
 * 1）new Paint()
 * 2）new Paint(int flags)
 * 3）new Paint(Paint paint)
 * 通过Paint的属性设置可以实现
 * 1）线帽样式：paint.setStrokeCap
 * 2）直线连接方式：paint.setStrokeJoin
 * 3）路径特效：paint.setPathEffect
 */
package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PaintGeneralFragment extends Fragment {

    NestedScrollView nestedScrollView;
    LinearLayout root;

    public PaintGeneralFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nestedScrollView = (NestedScrollView) inflater.inflate(R.layout.fragment_createbmp, container, false);
        root = (LinearLayout) nestedScrollView.findViewById(R.id.rt);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1000);
        MyView myView1 = new MyView(getContext(), 1);
        root.addView(myView1, params);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1000);
        myView1 = new MyView(getContext(), 2);
        root.addView(myView1, params);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 4300);
        myView1 = new MyView(getContext(), 3);
        root.addView(myView1, params);

        return nestedScrollView;
    }

    public class MyView extends View {

        Context m_context;
        Paint paint;
        int operationNumber;

        public MyView(Context context, int number) {
            super(context);
            m_context = context;
            operationNumber = number;
            //设置画笔基本属性
            paint = new Paint();
            paint.setAntiAlias(true);//抗锯齿功能
            paint.setDither(true);//抗锯齿功能
            /*替换方法
            1）Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG)
            2）paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);*/
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            switch (operationNumber) {
                case 1:
                    //StrokeCap示例
                    drawStrokeCap(canvas);
                    canvas.translate(500, 0);
                    //StokeJoin示例
                    drawStrokeJoin(canvas);
                    break;
                case 2:
                    canvas.translate(50, 0);
                    //CornerPathEffect示例
                    drawCornerPathEffect(canvas);
                    canvas.translate(0, 600);
                    drawCornerPathEffectCurve(canvas);
                    break;
                case 3:
                    canvas.translate(50, 50);
                    //DashPathEffect示例
                    drawDashPathEffectCurve(canvas);
                    canvas.translate(0, 250);
                    //DiscretePathEffect示例
                    drawDiscretePathEffectDemo(canvas);
                    canvas.translate(-50, 250);
                    //PathDashPathEffect示例
                    drawPathDashPathEffect(canvas);
                    canvas.translate(0, 550);
                    drawPathDashPathEffectRect(canvas);
                    canvas.translate(0, 250);
                    //SumPathEffect与ComposePathEffect示例
                    drawSum_ComposePathEffect(canvas);
                    break;
            }
        }

        //线帽
        private void drawStrokeCap(Canvas canvas) {
            paint.reset();
            paint.setTextSize(60);
            canvas.drawText("setStrokeCap", 30, 50, paint);
            paint = getPaint();
            paint.setStrokeWidth(80);

            //无线帽(缺省)
            paint.setStrokeCap(Paint.Cap.BUTT);
            canvas.drawLine(100, 200, 400, 200, paint);
            //方形线帽
            paint.setStrokeCap(Paint.Cap.SQUARE);
            canvas.drawLine(100, 400, 400, 400, paint);
            //圆形线帽
            paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawLine(100, 600, 400, 600, paint);

            //垂直画出x=100这条线
            paint.reset();
            paint.setStrokeWidth(2);
            paint.setColor(Color.RED);
            canvas.drawLine(100, 100, 100, 750, paint);
        }

        //直线连接方式
        private void drawStrokeJoin(Canvas canvas) {
            paint.reset();
            paint.setTextSize(60);
            canvas.drawText("setStrokeJoin", 30, 50, paint);
            paint = getPaint();
            paint.setStrokeWidth(40);

            //夹角小于此值，连接将改为平角。此值缺省值为4，代表28.96度
            paint.setStrokeMiter(10);

            Path path = new Path();
            path.moveTo(100, 100);
            path.lineTo(450, 100);
            path.lineTo(100, 300);
            //尖角（缺省）
            paint.setStrokeJoin(Paint.Join.MITER);
            canvas.drawPath(path, paint);

            path.moveTo(100, 400);
            path.lineTo(450, 400);
            path.lineTo(100, 600);
            //平角（夹角小于StrokeMiter时的缺省）
            paint.setStrokeJoin(Paint.Join.BEVEL);
            canvas.drawPath(path, paint);

            path.moveTo(100, 700);
            path.lineTo(450, 700);
            path.lineTo(100, 900);
            //圆角
            paint.setStrokeJoin(Paint.Join.ROUND);
            canvas.drawPath(path, paint);
        }

        //直线连接时使用圆角
        private void drawCornerPathEffect(Canvas canvas) {
            paint.reset();
            paint.setTextSize(60);
            canvas.drawText("CornerPathEffect", 0, 50, paint);
            paint = getPaint();
            Path path = new Path();
            path.moveTo(300, 100);
            path.lineTo(900, 200);
            path.lineTo(300, 500);

            //缺省为尖角连接
            paint.setColor(Color.GREEN);
            canvas.drawPath(path, paint);
            //设为半径为100的圆角连接
            paint.setPathEffect(new CornerPathEffect(100));
            paint.setColor(Color.RED);
            canvas.drawPath(path, paint);
            //设为半径为200的圆角连接
            paint.setPathEffect(new CornerPathEffect(200));
            paint.setColor(Color.BLUE);
            canvas.drawPath(path, paint);
        }

        //圆角连接曲线示例
        private void drawCornerPathEffectCurve(Canvas canvas) {
            paint = getPaint();
            Path path = getPath();
            //缺省尖角连接
            canvas.drawPath(path, paint);
            //设为圆角连接
            paint.setPathEffect(new CornerPathEffect(200));
            canvas.translate(0, 200);
            paint.setColor(Color.RED);
            canvas.drawPath(path, paint);
        }

        //虚线效果示例
        private void drawDashPathEffectCurve(Canvas canvas) {
            paint.reset();
            paint.setTextSize(60);
            canvas.drawText("DashPathEffect", 0, 50, paint);
            paint = getPaint();
            Path path = getPath();

            canvas.translate(0, 100);
            paint.setPathEffect(new DashPathEffect(new float[]{15, 20, 15, 15}, 0));
            paint.setColor(Color.RED);
            canvas.drawPath(path, paint);
        }

        //"生锈铁丝"效果示例
        private void drawDiscretePathEffectDemo(Canvas canvas) {
            paint.reset();
            paint.setTextSize(60);
            canvas.drawText("DiscretePathEffect", 0, 50, paint);
            paint = getPaint();
            Path path = getPath();
            canvas.translate(0, 100);
            canvas.drawPath(path, paint);
            paint.setColor(Color.RED);
            /**
             * 把原有的路线,在指定的间距处插入一个突刺
             * 第一个这些突出的“杂点”的间距,值越小间距越短,越密集
             * 第二个是突出距离
             */
            canvas.translate(0, 200);
            paint.setPathEffect(new DiscretePathEffect(2, 5));
            canvas.drawPath(path, paint);

            canvas.translate(0, 200);
            paint.setPathEffect(new DiscretePathEffect(6, 5));
            canvas.drawPath(path, paint);


            canvas.translate(0, 200);
            paint.setPathEffect(new DiscretePathEffect(6, 15));
            canvas.drawPath(path, paint);
        }

        //自定义虚线效果示例
        private void drawPathDashPathEffect(Canvas canvas) {
            paint.reset();
            paint.setTextSize(60);
            canvas.drawText("PathDashPathEffect", 30, 50, paint);
            paint = getPaint();
            Path path = new Path();
            path.moveTo(300, 100);
            path.lineTo(900, 200);
            path.lineTo(300, 500);
            canvas.drawPath(path, paint);
            canvas.translate(-100, 20);

            //以另一个路径为基础单位,延着路径盖章.相当于PS的印章工具
            paint.setColor(Color.RED);
            paint.setPathEffect(new PathDashPathEffect(getStampPath(), 35, 0, PathDashPathEffect.Style.MORPH));
            canvas.drawPath(path, paint);

            canvas.translate(100, 0);
        }

        //自定义曲线转角处不同效果示例
        private void drawPathDashPathEffectRect(Canvas canvas) {
            Paint paint = getPaint();
            Path path = getRectPath();
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("Origin", 200 + 100, 125 + 50, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);

            canvas.translate(500, 0);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("TRANSLATE", 200 + 100, 125 + 50, paint);
            paint.setStyle(Paint.Style.STROKE);
            //平移转角连接
            paint.setPathEffect(new PathDashPathEffect(getStampPath(), 35, 0, PathDashPathEffect.Style.TRANSLATE));
            canvas.drawPath(path, paint);

            canvas.translate(-500, 400);
            //旋转转角连接
            paint.setPathEffect(null);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("ROTATE", 200 + 100, 125 + 50, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setPathEffect(new PathDashPathEffect(getStampPath(), 35, 0, PathDashPathEffect.Style.ROTATE));
            canvas.drawPath(path, paint);

            canvas.translate(500, 0);
            paint.setPathEffect(null);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("MORPH", 200 + 100, 125 + 50, paint);
            paint.setStyle(Paint.Style.STROKE);
            //拉伸或压缩转角连接
            paint.setPathEffect(new PathDashPathEffect(getStampPath(), 35, 0, PathDashPathEffect.Style.MORPH));
            canvas.drawPath(path, paint);

            canvas.translate(-500, 150);
        }

        //多重特效组合示例
        private void drawSum_ComposePathEffect(Canvas canvas) {
            //画原始路径
            paint.reset();
            paint.setTextSize(60);
            canvas.drawText("SumPathEffect与ComposePathEffect", 30, 50, paint);
            paint = getPaint();
            Path path = getSquarePath();
            canvas.translate(0, 100);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("Origin", 300, 250, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);
            paint.setColor(Color.RED);
            //仅应用圆角特效的路径
            canvas.translate(0, 500);
            CornerPathEffect cornerPathEffect = new CornerPathEffect(100);
            paint.setPathEffect(cornerPathEffect);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("CornerPathEffect", 300, 250, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);
            //仅应用虚线特效的路径
            canvas.translate(500, 0);
            DashPathEffect dashPathEffect = new DashPathEffect(new float[]{2, 5, 10, 10}, 0);
            paint.setPathEffect(dashPathEffect);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("DashPathEffect", 300, 250, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);

            //利用SumPathEffect,分别将圆角与虚线特效应用于原始路径,然后将生成的两条特效路径合并
            canvas.translate(-500, 500);
            paint.setStyle(Paint.Style.STROKE);
            SumPathEffect sumPathEffect = new SumPathEffect(cornerPathEffect, dashPathEffect);
            paint.setPathEffect(sumPathEffect);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("SumPathEffect", 300, 250, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);

            //利用ComposePathEffect先应用圆角特效,再应用虚线特效
            canvas.translate(500, 0);
            ComposePathEffect composePathEffect = new ComposePathEffect(dashPathEffect, cornerPathEffect);
            paint.setPathEffect(composePathEffect);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("ComposePathEffect", 300, 250, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);
        }


        private Paint getPaint() {
            paint.reset();
            paint.setTextSize(40);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            return paint;
        }

        //生成随机曲线
        private Path getPath() {
            Path path = new Path();
            // 定义路径的起点
            path.moveTo(0, 0);
            // 定义路径的各个点
            for (int i = 0; i <= 40; i++) {
                path.lineTo(i * 30, (float) (Math.random() * 150));
            }
            return path;
        }

        //生成长方形
        private Path getRectPath() {
            Path path = new Path();
            path.addRoundRect(100, 50, 500, 300, 10, 10, Path.Direction.CW);
            return path;
        }

        //生成正方形
        private Path getSquarePath() {
            Path path = new Path();
            path.addRect(100, 50, 500, 450, Path.Direction.CCW);
            return path;
        }

        //画一个小三角形，作为生成虚线的基础单元
        private Path getStampPath() {
            Path path = new Path();
            path.moveTo(0, 20);
            path.lineTo(10, 0);
            path.lineTo(20, 20);
            path.close();
            return path;
        }
    }
}
