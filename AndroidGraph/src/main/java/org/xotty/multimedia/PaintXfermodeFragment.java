/**PorterDuffXfermode的功能及其应用：
 * 1）通常先要关闭硬件加速
 * 2）xx=canvas.saveLayer
 * 3）canvas.draw DST
 * 4）paint.setXfermode
 * 5）canvas.draw SRC
 * 6) paint.setXfermode(null);
      canvas.restoreToCount(xx);
 * 可用于许多图形和动画特效的实现
 */
package org.xotty.multimedia;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

public class PaintXfermodeFragment extends Fragment {

    NestedScrollView nestedScrollView;
    LinearLayout root;

    public PaintXfermodeFragment() {
    }

    // DST，圆
    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, 2 * w / 3, 2 * h / 3), p);
        return bm;
    }

    //SRC，矩形
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF66AAFF);
        c.drawRect(0, 0, 2 * w / 3, 2 * h / 3, p);
        return bm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nestedScrollView = (NestedScrollView) inflater.inflate(R.layout.fragment_createbmp, container, false);
        root = (LinearLayout) nestedScrollView.findViewById(R.id.rt);
        //圆（DST）与矩形（SRC）相交后各种Mode的效果
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1700);
        PorterDuffView myView1 = new PorterDuffView(getContext());
        root.addView(myView1, params);

        //DST_IN模式的应用示例
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 300);
        CircleWave_DSTIN myView2 = new CircleWave_DSTIN(getContext());
        root.addView(myView2, params);
        return nestedScrollView;
    }

    //PorterDuffMode全部模式效果展示
    private static class PorterDuffView extends View {
        private static final int W = 64 * 4 - 30;
        private static final int H = 64 * 4 - 30;
        private static final int ROW_MAX = 4;
        //全部18种PorterDuff.Mode
        private static final Xfermode[] sModes = {
                new PorterDuffXfermode(PorterDuff.Mode.ADD),
                new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
                new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
                new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
                new PorterDuffXfermode(PorterDuff.Mode.OVERLAY),
                new PorterDuffXfermode(PorterDuff.Mode.SCREEN),

                new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
                new PorterDuffXfermode(PorterDuff.Mode.XOR),

                new PorterDuffXfermode(PorterDuff.Mode.SRC),
                new PorterDuffXfermode(PorterDuff.Mode.DST),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
                new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
                new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
                new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
                new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
        };
        private static final String[] sLabels = {
                "Add", "Lighten", "Darken", "Multiply", "Overlay",
                "Screen", "Clear", "Xor", "Src", "Dst", "SrcIn", "DstIn",
                "SrcOut", "DstOut", "SrcOver", "DstOver", "SrcATop", "DstATop"
        };
        private Bitmap mSrcB;
        private Bitmap mDstB;

        public PorterDuffView(Context context) {
            super(context);

            mSrcB = makeSrc(W, H);
            mDstB = makeDst(W, H);
            //关闭硬件加速
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            Paint labelP = new Paint(Paint.ANTI_ALIAS_FLAG);
            labelP.setTextAlign(Paint.Align.CENTER);
            labelP.setTextSize(40);

            Paint paint = new Paint();
            paint.setFilterBitmap(false);

            canvas.translate(25, 45);

            int x = 0;
            int y = 0;

            for (int i = 0; i < sModes.length; i++) {
                //画框
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.LTGRAY);
                canvas.drawRect(x - 15f, y - 15f,
                        x + W + 15f, y + H + 15f, paint);

                // 再框中画图
                int sc = canvas.saveLayer(x, y, x + W, y + H, null,
                        Canvas.ALL_SAVE_FLAG);

                canvas.translate(x, y);
                canvas.drawBitmap(mDstB, 0, 0, paint);
                paint.setXfermode(sModes[i]);
                canvas.drawBitmap(mSrcB, W / 3, H / 3, paint);

                paint.setXfermode(null);
                canvas.restoreToCount(sc);

                //画Mode名称
                canvas.drawText(sLabels[i],
                        x + W / 2, y + H + labelP.getTextSize() + 10, labelP);
                x += W + 10 * 4;

                //换行
                if ((i % ROW_MAX) == ROW_MAX - 1) {
                    x = 0;
                    y += H + 30 * 3;
                }
            }
        }
    }

    //PorterDuffMode.DST_IN应用示例
    public class CircleWave_DSTIN extends View {
        private Paint mPaint;
        private Path mPath;
        private int mItemWaveLength = 1000;
        private int dx;

        private Bitmap BmpSRC, BmpDST;

        public CircleWave_DSTIN(Context context) {
            super(context);
            //关闭硬件加速
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            mPath = new Path();
            mPaint = new Paint();
            mPaint.setColor(Color.GREEN);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

            //文字（其余部分为空白）
            BmpSRC = BitmapFactory.decodeResource(getResources(), R.drawable.wavetext, null);
            //颜色填充波纹线下及其下部的图片
            BmpDST = Bitmap.createBitmap(BmpSRC.getWidth(), BmpSRC.getHeight(), Bitmap.Config.ARGB_8888);

            startAnim();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //画文字标题
            Paint xPaint = new Paint();
            xPaint.setStrokeWidth(5);
            xPaint.setTextSize(50);
            xPaint.setStyle(Paint.Style.FILL);
            xPaint.setColor(Color.BLACK);
            xPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("PorterDuffMode.DST_IN应用示例", 20, 60, xPaint);
            //生成波纹图
            generageWavePath();
            //先清空bitmap上的图像,然后再画上波纹图
            Canvas c = new Canvas(BmpDST);
            c.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
            c.drawPath(mPath, mPaint);
            //先画上一遍文字
            canvas.drawBitmap(BmpSRC, 0, 60, mPaint);

            //用波纹图覆盖文字
            int layerId = canvas.saveLayer(0, 60, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            canvas.drawBitmap(BmpDST, 0, 60, mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(BmpSRC, 0, 60, mPaint);

            mPaint.setXfermode(null);
            canvas.restoreToCount(layerId);
        }

        //生成波纹图，是一个上部为波浪线，其它部分与文字框一半大小（高一半，长相等）的闭合区域
        private void generageWavePath() {
            mPath.reset();
            int originY = BmpSRC.getHeight() / 2;
            int halfWaveLen = mItemWaveLength / 2;
            mPath.moveTo(-mItemWaveLength + dx, originY);
            for (int i = -mItemWaveLength; i <= getWidth() + mItemWaveLength; i += mItemWaveLength) {
                mPath.rQuadTo(halfWaveLen / 2, -50, halfWaveLen, 0);
                mPath.rQuadTo(halfWaveLen / 2, 50, halfWaveLen, 0);
            }
            mPath.lineTo(BmpSRC.getWidth(), BmpSRC.getHeight());
            mPath.lineTo(0, BmpSRC.getHeight());
            mPath.close();
        }

        //通过不断改变波纹图的起始横坐标来实现动画效果
        public void startAnim() {
            ValueAnimator animator = ValueAnimator.ofInt(0, mItemWaveLength);
            animator.setDuration(2000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    dx = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animator.start();
        }
    }

}
