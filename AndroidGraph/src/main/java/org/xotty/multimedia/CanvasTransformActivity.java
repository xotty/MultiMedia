/**Canvas变换的两个基本点：
1）所有的变换操作(旋转、平移、缩放、错切)默认都是以坐标原点为基准点的。
2）之前操作的坐标系状态会保留，并且影响到后续状态。
*/

package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CanvasTransformActivity extends AppCompatActivity {
    int titleHeight = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvastransform);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TranslateView tv = new TranslateView(this);
        LinearLayout root = (LinearLayout) findViewById(R.id.rt);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                800);

        root.addView(tv, params);

        params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                380);
        ClipView cv = new ClipView(this);
        root.addView(cv, params);
        params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                550);
        MatrixView mv = new MatrixView(this);
        root.addView(mv, params);

        params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                550);
        LayerView lv = new LayerView(this);
        root.addView(lv, params);
        params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                700);
        SampleView sv = new SampleView(this);
        root.addView(sv, params);

    }

    private Paint generatePaint(int color, Paint.Style style, int width) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(style);
        paint.setStrokeWidth(width);
        return paint;
    }

    public class TranslateView extends View {


        public TranslateView(Context context) {
            super(context);
        }

        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //设置画布背景颜色
            canvas.drawColor(Color.WHITE);

            Paint paint = generatePaint(Color.RED, Paint.Style.FILL, 5);
            paint.setTextSize(50);
            canvas.drawText("1.Translate", 10, titleHeight, paint);
            canvas.save();
            //设置画笔基本属性
            Paint paint_green = generatePaint(Color.GREEN, Paint.Style.STROKE, 5);
            Paint paint_red = generatePaint(Color.RED, Paint.Style.STROKE, 5);
            canvas.restore();
            canvas.save();

            //平移Translate
            RectF rect1 = new RectF(50, 50 + titleHeight, 400, 200 + titleHeight);
            canvas.drawRect(rect1, paint_red);
            canvas.translate(100, 100);//平移到（100，100）
            canvas.drawRect(rect1, paint_green);

            //错切Skew
            Rect rect2 = new Rect(550, 50 + titleHeight, 900, 200 + titleHeight);
            canvas.drawRect(rect2, paint_red);
            canvas.skew(0.533f, 0);  //X轴倾斜60度，Y轴不变
            canvas.drawRect(rect2, paint_green);//画出旋转后的矩形

            //旋转Rotate
            canvas.restore();
            canvas.save();
            Rect rect3 = new Rect(50, 500 + titleHeight, 400, 650 + titleHeight);
            canvas.drawRect(rect3, paint_red);
            canvas.rotate(-15);//逆时针旋转画布15度，（0,0） 为旋转中心
            // canvas.rotate(-15, 50, 600); // （px，py） 为旋转中心
            canvas.drawRect(rect3, paint_green);

            //缩放Scale
            canvas.restore();
            canvas.save();
            Rect rect4 = new Rect(650, 500 + titleHeight, 1000, 650 + titleHeight);
            canvas.drawRect(rect4, paint_red);
            canvas.scale(1.1f, 0.9f);//x放大到1.1倍，y缩小到0.9倍
            canvas.drawRect(rect4, paint_green);

        }

    }

    /*API28以上Region.Op仅支持INTERSECT和DIFFERENCE
    boolean  clipRect(Rect rect)
    boolean  clipRect(RectF rect)
    boolean  clipRect(int left, int top, int right, int bottom)
    boolean  clipRect(float left, float top, float right, float bottom)

    boolean  clipOutRect(Rect rect)
    boolean  clipOutRect(RectF rect)
    boolean  clipOutRect(int left, int top, int right, int bottom)
    boolean  clipOutRect(float left, float top, float right, float bottom)

    boolean  clipRect(Rect rect, Region.Op op)
    boolean  clipRect(RectF rect, Region.Op op)
    boolean  clipRect(float left, float top, float right, float bottom, Region.Op op)

    boolean  clipPath(Path path)
    boolean  clipOutPath(Path path)
    boolean  clipPath(Path path, Region.Op op)
    */
    public class ClipView extends View {


        public ClipView(Context context) {
            super(context);
        }

        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //设置画布背景颜色和标题
            canvas.drawColor(Color.LTGRAY);
            canvas.save();
            Paint paint = generatePaint(Color.RED, Paint.Style.FILL, 5);
            paint.setTextSize(50);
            canvas.drawText("2.Clip", 10, titleHeight, paint);

            //原图
            canvas.restore();
            canvas.save();
            canvas.translate(10, titleHeight+20);
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.timg);
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.restore();

            //单矩形裁剪，相当于DIFFERENCE
            canvas.save();
            canvas.translate(350, titleHeight+20);
            canvas.clipOutRect(0, 50, 200, 150 );
            canvas.drawBitmap(bitmap, 0, 0, null);

            //单圆形裁剪
            canvas.restore();
            canvas.save();
            canvas.translate(630, titleHeight+20);
            Path path = new Path();
            path.addCircle( bitmap.getWidth() / 2, (bitmap.getWidth() / 2) , bitmap.getWidth() / 2, Path.Direction.CW);
            canvas.clipPath(path);
            canvas.drawBitmap(bitmap, 0, 0, null);

            //两个圆相交部分INTERSECT
            canvas.restore();
            canvas.save();
            canvas.translate(980, titleHeight+20);
            Path path1 = new Path();
            Path path2 = new Path();
            path1.addCircle(0, bitmap.getHeight() / 2 , bitmap.getWidth() / 2, Path.Direction.CW);
            path2.addCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2 , bitmap.getWidth() / 2, Path.Direction.CW);
            canvas.clipPath(path1);
            canvas.clipPath(path2, Region.Op.INTERSECT);
            /*可替换
            canvas.clipPath(path2);*/
            canvas.drawBitmap(bitmap, 0, 0, null);

            //两个矩形相减部分DIFFERENCE
            canvas.restore();
            canvas.save();
            canvas.translate(1200, titleHeight+20);
            canvas.clipRect(0, 0, bitmap.getWidth()*2 / 3, bitmap.getHeight()*2/3);
            canvas.clipRect(bitmap.getWidth() / 3, bitmap.getHeight()/3, bitmap.getWidth(), bitmap.getHeight(), Region.Op.DIFFERENCE);
            /*可替换
            canvas.clipOutRect(bitmap.getWidth() / 3, bitmap.getHeight()/3, bitmap.getWidth(), bitmap.getHeight());*/
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    /*若M是原始状态矩阵，A是变换矩阵，M'是变换后的结果矩阵，则：
        pre  :  M‘ = M*A，右乘
        post :  M’ = A*M，左乘

        通常的变换执行程序：
        Matrix matrix = new Matrix();
        各种变换操作：平移、旋转，缩放，错切......
        matrix.postTranslate(pivotX,pivotY);
        matrix.preTranslate(-pivotX, -pivotY)
    初始化
     1.Matrix ()
     2.void reset ()

     3.Matrix (Matrix src)
     4.void set (Matrix src)

     5.void setValues (float[] values)    //float数组为9个浮点数
     主要运算：
     1.setTranslate(float dx, float dy)/preTranslate/postTranslate
     2.setScale(float sx, float sy)/preScale/postScale
       setScale(float sx, float sy, float px, float py)/preScale/postScale
     3.setRotate(float degrees)/preRotate/postRotate
       setRotate(float degrees, float px, float py)//preRotate/postRotate
       setSinCos(float sinValue, float cosValue)
       setSinCos(float sinValue, float cosValue, float px, float py)

     4.setSkew(float kx, float ky)/preSkew/postSKew
       setSkew(float kx, float ky, float px, float py)/preSkew/postSKew

     5.setConcat(Matrix a, Matrix b) //a * b
     6.preConcat(Matrix other)       //M * other
     7.postConcat(Matrix other)      //other * M

     计算变换后的坐标
     1.mapPoints (float[] pts)
     2.mapPoints (float[] dst, float[] src)
     3.mapPoints (float[] dst, int dstIndex,float[] src, int srcIndex, int pointCount)
     */
    public class MatrixView extends View {

        private static final String TAG = "TAG";

        public MatrixView(Context context) {
            super(context);
        }

        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //设置画布背景颜色
            canvas.drawColor(Color.WHITE);
            Paint paint = generatePaint(Color.RED, Paint.Style.FILL, 5);
            paint.setTextSize(50);
            canvas.drawText("3.Matrix", 10, titleHeight, paint);
            canvas.save();

            canvas.restore();
            Paint paint_red = generatePaint(Color.RED, Paint.Style.STROKE, 5);


            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.scene);
            Matrix matrix = new Matrix();
            matrix.setTranslate(50, 200);
            matrix.preRotate(15, bitmap.getWidth() / 2, bitmap.getHeight() / 2 + titleHeight);
            matrix.preScale(0.5f, 0.5f);
            canvas.drawBitmap(bitmap, matrix, null);

            matrix.reset();
            matrix.setTranslate(600, 250);
            matrix.preRotate(-35);
            canvas.concat(matrix);  //canvas.setMatrix(matrix);
            RectF rect1 = new RectF(0, 0 + titleHeight, 400, 200 + titleHeight);
            canvas.drawRect(rect1, paint_red);


            Matrix a = new Matrix();
            Matrix b = new Matrix();
            Matrix c = new Matrix();
            a.setValues(new float[]{1, 0, 100, 0, 1, 80, 0, 0, 1});
            b.setValues(new float[]{0.5f, 0, 0, 0, 0.4f, 0, 0, 0, 1});
            c.setValues(new float[]{1, 0, -100, 0, 1, -80, 0, 0, 1});
            matrix.setConcat(c, b);
            Log.i(TAG, "M1=" + matrix.toString());
            matrix.postConcat(a);
            Log.i(TAG, "M2=" + matrix.toShortString());
        }
    }

    public class LayerView extends View {

        private static final String TAG = "TAG";

        public LayerView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.RED);
            canvas.drawColor(Color.LTGRAY);     //canvas是图层0，灰色
            mPaint.setTextSize(50);
            canvas.drawText("4.Layer", 10, titleHeight, mPaint);
            canvas.save();
            canvas.drawCircle(150, 20 + 150 + titleHeight, 100, mPaint);
            canvas.saveLayer(0, 20 + titleHeight, 600, 300 + 20 + titleHeight, mPaint);//图层1
            /*可选形式
            canvas.saveLayerAlpha(0, 0, 600, 300, 0x50, Canvas.ALL_SAVE_FLAG);//图层1*/

            canvas.drawColor(Color.BLUE);//图层1，蓝色
            canvas.drawCircle(300, 20 + 150 + titleHeight, 100, mPaint);
            canvas.restore();  //去掉图层1，否则图层2是基于图层1的

            canvas.saveLayerAlpha(300, 20 + titleHeight, 900, 400 + 20 + titleHeight, 0x88);//图层2
            canvas.drawColor(Color.YELLOW);//图层2，黄色
            canvas.drawCircle(600, 150 + 20 + titleHeight, 100, mPaint);

        }
    }

    public class SampleView extends View {
        private static final int HOUR_LINE_HEIGHT = 20;
        private static final int MINUTE_LINE_HEIGHT = 10;
        private static final String TAG = "TAG";
        private Paint mCirclePaint, mLinePaint;
        private DrawFilter mDrawFilter;
        //圆心（表盘中心）
        private int mCenterX, mCenterY, mCenterRadius;
        // 圆环线宽度
        private int mCircleLineWidth;
        // 直线刻度线宽度
        private int mHourLineWidth, mMinuteLineWidth;
        // 时针长度
        private int mHourLineHeight;
        // 分针长度
        private int mMinuteLineHeight;
        // 刻度线的左、上位置
        private int mLineLeft, mLineTop;
        // 刻度线的下边位置
        private int mLineBottom;
        // 用于控制刻度线位置
        private int mFixLineHeight;

        public SampleView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.RED);
            canvas.drawColor(Color.WHITE);     //canvas是图层0，灰色
            mPaint.setTextSize(50);
            canvas.drawText("5.Sample", 10, titleHeight, mPaint);

            //画正方形
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
            int l = 10;
            int t = 10;
            int r = 410;
            int b = 410;
            int space = 30;
            Rect squareRect = new Rect(l, t, r, b);
            int squareCount = (r - l) / space;
            float px = l + (r - l) / 2;
            float py = t + (b - t) / 2;
            canvas.translate(50, 50 + titleHeight);
            for (int i = 0; i < squareCount; i++) {
                // 保存画布
                canvas.save();
                float fraction = (float) i / squareCount;
                // 将画布以正方形中心进行缩放
                canvas.scale(fraction, fraction, px, py);
                canvas.drawRect(squareRect, paint);
                // 画布回滚
                canvas.restore();
            }


            //画表盘
            mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                    | Paint.FILTER_BITMAP_FLAG);

            mCircleLineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                    getResources().getDisplayMetrics());
            mHourLineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                    getResources().getDisplayMetrics());
            mMinuteLineWidth = mHourLineWidth / 2;

            mFixLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                    getResources().getDisplayMetrics());

            mHourLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    HOUR_LINE_HEIGHT,
                    getResources().getDisplayMetrics());
            mMinuteLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    MINUTE_LINE_HEIGHT,
                    getResources().getDisplayMetrics());

            mCenterX = 750;
            mCenterY = 200 + titleHeight;
            mCenterRadius = Math.min(mCenterX, mCenterY) - mCircleLineWidth / 2;

            mLineLeft = mCenterX - mMinuteLineWidth / 2;
            mLineTop = mCenterY - mCenterRadius;
            initPaint();
            // 绘制表盘
            drawCircle(canvas);
            // 绘制刻度
            drawLines(canvas);
        }

        private void initPaint() {
            mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCirclePaint.setColor(Color.RED);
            mCirclePaint.setStyle(Paint.Style.STROKE);
            mCirclePaint.setStrokeWidth(mCircleLineWidth);

            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mLinePaint.setStrokeWidth(mHourLineWidth);
        }

        //绘制刻度
        private void drawLines(Canvas canvas) {
            for (int degree = 0; degree <= 360; degree++) {
                if (degree % 30 == 0) {
                    //时针
                    mLineBottom = mLineTop + mHourLineHeight;
                    mLinePaint.setStrokeWidth(mHourLineWidth);
                    mLinePaint.setColor(Color.RED);
                } else {
                    mLineBottom = mLineTop + mMinuteLineHeight;
                    mLinePaint.setStrokeWidth(mMinuteLineWidth);
                    mLinePaint.setColor(Color.BLACK);
                }

                if (degree % 6 == 0) {
                    canvas.save();
                    canvas.rotate(degree, mCenterX, mCenterY);
                    canvas.drawLine(mLineLeft, mLineTop, mLineLeft, mLineBottom, mLinePaint);
                    canvas.restore();
                }
            }
        }

        private void drawCircle(Canvas canvas) {
            canvas.drawCircle(mCenterX, mCenterY, mCenterRadius, mCirclePaint);
        }
    }
}
