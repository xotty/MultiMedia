/**
 * RedialGradient（环形渲染）应用：
 * 1)定义： RadialGradient(float centerX, float centerY, float radius, int centerColor, int edgeColor, TileMode tileMode)
 * RadialGradient(float centerX, float centerY, float radius, int[] colors, float[] stops, TileMode tileMode)
 * 2)设置： mPaint.setShader(mBitmapShader)
 * 3)渲染： mCanvas.draw(......,mPaint)
 * 4)共有三种重复出现方式：TileMode.REPEAT、TileMode.MIRROR、TileMode.CLAMP
 * 5）始终以（centerX, centerY）为中心，以radius为半径向外扩散，不论绘制（draw）的位置和大小。
 */
package org.xotty.multimedia;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

public class Shader_RedialGradient_Fragment extends Fragment {

    private int rectHeight = 400;
    private int rectWidth ;
    public void setRectWidth(int rectWidth) {
        this.rectWidth = rectWidth;
    }

    public Shader_RedialGradient_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(), R.style.AppTheme_NoActionBar);
        LayoutInflater localInflater = inflater
                .cloneInContext(contextThemeWrapper);

        //构建Fragment的根视图LinearLayout
        LinearLayout root = new LinearLayout(getContext());
        root.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(param);


        //添加双色和多色的无填充渲染图
        LinearLayout items0 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame0 = items0.findViewById(R.id.frameLeft);
        FrameLayout rightFrame0 = items0.findViewById(R.id.frameRight);
        RedialShaderView myRedialShaderView = new RedialShaderView(getContext(), 0);
        LinearLayout.LayoutParams param0 = new LinearLayout.LayoutParams(rectWidth, rectHeight);
        leftFrame0.addView(myRedialShaderView, param0);
        myRedialShaderView = new RedialShaderView(getContext(), 1);
        rightFrame0.addView(myRedialShaderView, param0);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param1.topMargin = 30;
        root.addView(items0,param1);

        //添加Repeat和Mirror模式的填充渲染图
        LinearLayout items1 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame1 = items1.findViewById(R.id.frameLeft);
        FrameLayout rightFrame1 = items1.findViewById(R.id.frameRight);
        myRedialShaderView = new RedialShaderView(getContext(), 2);
//        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(rectWidth, rectHeight);
        leftFrame1.addView(myRedialShaderView, param0);
        myRedialShaderView = new RedialShaderView(getContext(), 3);
        rightFrame1.addView(myRedialShaderView, param0);

        root.addView(items1, param1);

        //添加Clamp模式的填充渲染图
        LinearLayout items2 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame2 = items2.findViewById(R.id.frameLeft);
        FrameLayout rightFrame2 = items2.findViewById(R.id.frameRight);
        myRedialShaderView = new RedialShaderView(getContext(), 4);
        leftFrame2.addView(myRedialShaderView, param0);
        myRedialShaderView = new RedialShaderView(getContext(), 5);
        rightFrame2.addView(myRedialShaderView, param0);
        root.addView(items2, param1);

        LinearLayout items3 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame3 = items3.findViewById(R.id.frameLeft);
        FrameLayout rightFrame3 = items3.findViewById(R.id.frameRight);
        RippleButtonView  rippleButtonView = new RippleButtonView(getContext());
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(rectWidth * 2 + 30, rectHeight/2+50);
        leftFrame3.addView(rippleButtonView, param3);
        items3.removeView(rightFrame3);
        LinearLayout.LayoutParams param4 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 630);
        param4.topMargin = 50;
        root.addView(items3, param4);
        return root;
    }

    public class RedialShaderView extends View {
        String modeTag;
        int tileMode;
        private int mRadius = 100;
        private Paint mPaint, xPaint;

        RedialShaderView(Context context, int shaderMode) {
            super(context);
            init(shaderMode);
        }

        private void init(int mode) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);

            mPaint = new Paint();
            xPaint = new Paint();
            xPaint.setColor(Color.RED);
            xPaint.setStrokeWidth(5);
            xPaint.setStyle(Paint.Style.FILL);
            xPaint.setTextAlign(Paint.Align.CENTER);
            xPaint.setTextSize(40);
            tileMode = mode;


        }


        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            RadialGradient mGradient = null;
            //填充颜色数组
            int[] colors = {0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00, 0xff00ffff};
            //与颜色数组对应的位置数组（0～1）
            float[] pos = {0f, 0.2f, 0.4f, 0.6f, 1.0f};
            //颜色沿着（x0,y0）～ (x1,y1)的直线渐变，多余部分会按照TileMode方式填充
            switch (tileMode) {
                case 0:
                    modeTag = "TileMode.CLAMP";
                    mGradient = new RadialGradient(getWidth() / 2, getHeight() / 2, mRadius * 2, 0xffff0000, 0xff00ff00, Shader.TileMode.CLAMP);
                    break;
                case 1:
                    modeTag = "TileMode.CLAMP";
                    mGradient = new RadialGradient(getWidth() / 2, getHeight() / 2, mRadius * 2, colors, pos, Shader.TileMode.CLAMP);
                    break;
                case 2:
                    modeTag = "TileMode.REPEAT";
                    mGradient = new RadialGradient(getWidth() / 2, getHeight() / 2, mRadius / 2, 0xffff0000, 0xff00ff00, Shader.TileMode.REPEAT);
                    break;
                case 3:
                    modeTag = "TileMode.REPEAT";
                    mGradient = new RadialGradient(getWidth() / 2, getHeight() / 2, mRadius / 2, colors, pos, Shader.TileMode.REPEAT);
                    break;
                case 4:
                    modeTag = "TileMode.MIRROR";
                    mGradient = new RadialGradient(getWidth() / 2, getHeight() / 2, mRadius / 2, 0xffff0000, 0xff00ff00, Shader.TileMode.MIRROR);
                    break;
                case 5:
                    //水平渐变
                    modeTag = "TileMode.MIRROR";
                    mGradient = new RadialGradient(getWidth() / 2, getHeight() / 2, mRadius / 2, colors, pos, Shader.TileMode.MIRROR);
                    break;
            }

            //设置渲染
            mPaint.setShader(mGradient);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //实施渲染
            canvas.drawRect(0, 0, getWidth(), getHeight() - 70, mPaint);
            canvas.drawText(modeTag, getWidth() / 2, getHeight() - 20, xPaint);
        }
    }

    public class RippleButtonView extends AppCompatButton {
        private int mX, mY;
        private ObjectAnimator mAnimator;
        private int DEFAULT_RADIUS = 50;
        private int mCurRadius = 0;
        private RadialGradient mRadialGradient;
        private Paint mPaint;

        public RippleButtonView(Context context) {
            super(context);
            init();
        }


        private void init() {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            mPaint = new Paint();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if (mX != event.getX() || mY != mY) {
                mX = (int) event.getX();
                mY = (int) event.getY();
                setRadius(DEFAULT_RADIUS);
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.cancel();
                }

                if (mAnimator == null) {
                    mAnimator = ObjectAnimator.ofInt(this, "radius", DEFAULT_RADIUS, Math.max(getWidth(),getHeight()));
                }

                mAnimator.setInterpolator(new AccelerateInterpolator());
                mAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setRadius(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                mAnimator.start();
            }

            return super.onTouchEvent(event);
        }


        public void setRadius(final int radius) {
            mCurRadius = radius;
            if (mCurRadius > 0) {
                mRadialGradient = new RadialGradient(mX, mY, mCurRadius, 0x00FFFFFF, 0xFF58FAAC, Shader.TileMode.CLAMP);
                mPaint.setShader(mRadialGradient);
            }
            postInvalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(mX, mY, mCurRadius, mPaint);
        }
    }


}
