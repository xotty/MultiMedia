/**
 * LinearGradient（线性渲染）应用：
 * 1)定义：LinearGradient(float x0, float y0, float x1, float y1,int color0, int color1,  TileMode tile)
 *        LinearGradient(float x0, float y0, float x1, float y1,int colors[], float positions[],  TileMode tile)
 * 2)设置： mPaint.setShader(mLinearGradient)
 * 3)渲染： mCanvas.draw(......,mPaint)
 * 4)共有三种重复出现方式：TileMode.REPEAT、TileMode.MIRROR、TileMode.CLAMP
 * 5）始终以控件所在view视图左上角为原点重复绘制，不论绘制（draw）的位置和大小。
 */
package org.xotty.multimedia;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class Shader_LinearGradient_Fragment extends Fragment {
    private int rectHeight = 400;
    private int rectWidth ;
    public void setRectWidth(int rectWidth) {
        this.rectWidth = rectWidth;
    }


    public Shader_LinearGradient_Fragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        LinearShaderView myLinearShaderView = new LinearShaderView(getContext(), 0);
        LinearLayout.LayoutParams param0 = new LinearLayout.LayoutParams(rectWidth, rectHeight);
        leftFrame0.addView(myLinearShaderView, param0);
        myLinearShaderView = new LinearShaderView(getContext(), 1);
        rightFrame0.addView(myLinearShaderView, param0);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param1.topMargin = 30;

        root.addView(items0,param1);

        //添加Repeat和Mirror模式的填充渲染图
        LinearLayout items1 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame1 = items1.findViewById(R.id.frameLeft);
        FrameLayout rightFrame1 = items1.findViewById(R.id.frameRight);
        myLinearShaderView = new LinearShaderView(getContext(), 2);
        leftFrame1.addView(myLinearShaderView, param0);
        myLinearShaderView = new LinearShaderView(getContext(), 3);
        rightFrame1.addView(myLinearShaderView, param0);
        root.addView(items1, param1);

        //添加Clamp模式的填充渲染图
        LinearLayout items2 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame2 = items2.findViewById(R.id.frameLeft);
        FrameLayout rightFrame2 = items2.findViewById(R.id.frameRight);
        myLinearShaderView = new LinearShaderView(getContext(), 4);
        leftFrame2.addView(myLinearShaderView, param0);
        myLinearShaderView = new LinearShaderView(getContext(), 5);
        rightFrame2.addView(myLinearShaderView, param0);
        root.addView(items2, param1);

        LinearLayout items3 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame3 = items3.findViewById(R.id.frameLeft);
        FrameLayout rightFrame3 = items3.findViewById(R.id.frameRight);
        myLinearShaderView = new LinearShaderView(getContext(), 6);
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(rectWidth * 2 + 30, rectHeight/2);
        leftFrame3.addView(myLinearShaderView, param3);
        items3.removeView(rightFrame3);
        LinearLayout.LayoutParams param4 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 130);
        param4.topMargin = 50;
        root.addView(items3, param4);

        LinearLayout items4 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame4 = items4.findViewById(R.id.frameLeft);
        FrameLayout rightFrame4 = items4.findViewById(R.id.frameRight);
        ShimmerTextView shimmerTextView = new ShimmerTextView(getContext());
        shimmerTextView.setText("行到水穷处，坐看云起时");
        shimmerTextView.setTextSize(20);
        shimmerTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams param5 = new LinearLayout.LayoutParams(rectWidth * 2 + 30, rectHeight/2);
        leftFrame4.addView(shimmerTextView, param5);
        items4.removeView(rightFrame4);
        LinearLayout.LayoutParams param6 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 400);
        param6.topMargin = 50;
        root.addView(items4, param6);
        return root;
    }

    public class LinearShaderView extends View {
        String modeTag;
        int tileMode;
        private Paint mPaint, xPaint;

        LinearShaderView(Context context, int shaderMode) {
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
            LinearGradient mGradient = null;
            //填充颜色数组
            int[] colors = {0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00, 0xff00ffff};
            //与颜色数组对应的位置数组（0～1）
            float[] pos = {0f, 0.2f, 0.4f, 0.6f, 1.0f};
            //颜色沿着（x0,y0）～ (x1,y1)的直线渐变，多余部分会按照TileMode方式填充
            switch (tileMode) {
                case 0:
                    modeTag = "双色";
                    mGradient = new LinearGradient(0, getHeight() / 2, getWidth(), getHeight() / 2, 0xffff0000, 0xff00ff00, Shader.TileMode.CLAMP);
                    break;
                case 1:
                    modeTag = "多色";
                    mGradient = new LinearGradient(0, 0, getWidth(), 0, colors, pos, Shader.TileMode.CLAMP);
                    break;
                case 2:
                    modeTag = "TileMode.REPEAT";
                    mGradient = new LinearGradient(0, getHeight() / 2, getWidth() / 2, getHeight() / 2, colors, pos, Shader.TileMode.REPEAT);
                    break;
                case 3:
                    modeTag = "TileMode.MIRROR";
                    mGradient = new LinearGradient(0, getHeight() / 2, getWidth() / 2, getHeight() / 2, colors, pos, Shader.TileMode.MIRROR);
                    break;
                case 4:
                    //水平渐变
                    modeTag = "TileMode.CLAMP";
                    mGradient = new LinearGradient(0, getHeight() / 2, getWidth() / 2, getHeight() / 2, colors, pos, Shader.TileMode.CLAMP);
                    break;
                case 5:
                    //对角线渐变
                    modeTag = "TileMode.CLAMP";
                    mGradient = new LinearGradient(0, 0, getWidth() / 2, getHeight() / 2, colors, pos, Shader.TileMode.CLAMP);
                    break;

                case 6:
                    //应用1：闪光文字
                    modeTag = "Demo";
                    mGradient = new LinearGradient(0, getHeight() / 2, getWidth() / 2, getHeight() / 2, colors, pos, Shader.TileMode.MIRROR);

                    break;
            }

            //设置渲染
            mPaint.setShader(mGradient);

        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            super.onDraw(canvas);
            mPaint.setTextSize(100);
            mPaint.setTextAlign(Paint.Align.CENTER);
            //实施渲染
            if (modeTag.equals("Demo"))
                canvas.drawText("花开花落，云卷云舒", getWidth() / 2, getHeight()/2 , mPaint);
            else {
                canvas.drawRect(0, 0, getWidth(), getHeight() - 70, mPaint);
                canvas.drawText(modeTag, getWidth() / 2, getHeight() - 20, xPaint);
            }
        }
    }


    //应用2：动态闪光文字
    public class ShimmerTextView extends AppCompatTextView {
        private Paint mPaint;
        private int mDx;
        private LinearGradient mLinearGradient;
        public ShimmerTextView(Context context) {
            super(context);
            init();
        }

        private void init(){
            mPaint =getPaint();
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);

            ValueAnimator animator = ValueAnimator.ofInt(0,2*getMeasuredWidth());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mDx = (Integer) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setDuration(2000);
            animator.start();

            mLinearGradient = new LinearGradient(- getMeasuredWidth(),0,0,0,new int[]{
                    getCurrentTextColor(),0xffff0000,0xff00ff00,getCurrentTextColor()
            },
                    new float[]{
                            0,
                            0.3f,
                            0.6f,
                            1
                    },
                    Shader.TileMode.CLAMP
            );
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //动态变换Shader的位置
            Matrix matrix = new Matrix();
            matrix.setTranslate(mDx,0);
            mLinearGradient.setLocalMatrix(matrix);
            mPaint.setShader(mLinearGradient);
            super.onDraw(canvas);
        }
    }


}
