/**
 * SweepGradient（扫描渲染）应用：
 * 1)定义： SweepGradient(float centerX, float centerY, float radius, int centerColor, int edgeColor, TileMode tileMode)
 * SweepGradient(float centerX, float centerY, float radius, int[] colors, float[] stops, TileMode tileMode)
 * 2)设置： mPaint.setShader(mSweepGradient)
 * 3)渲染： mCanvas.draw(......,mPaint)
 * 4）始终以（centerX, centerY）为中心，0～360度旋转扫描扩散渐变，不论绘制（draw）的位置和大小。
 */
package org.xotty.multimedia;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class Shader_SweepGradient_Fragment extends Fragment {

    private int viewHeight = 570;
    private int viewWidth = 500;
    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public Shader_SweepGradient_Fragment() {
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
        param.topMargin = 50;
        root.setLayoutParams(param);

        //添加双色渲染图
        LinearLayout items0 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame0 = items0.findViewById(R.id.frameLeft);
        FrameLayout rightFrame0 = items0.findViewById(R.id.frameRight);
        SweepShaderView mySweepShaderView = new SweepShaderView(getContext(), 0);
        LinearLayout.LayoutParams param0 = new LinearLayout.LayoutParams(viewWidth, viewHeight);
        leftFrame0.addView(mySweepShaderView, param0);
        mySweepShaderView = new SweepShaderView(getContext(), 1);
        rightFrame0.addView(mySweepShaderView, param0);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param1.topMargin = 50;
        root.addView(items0,param1);

        //添加多色渲染图
        LinearLayout items1 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame1 = items1.findViewById(R.id.frameLeft);
        FrameLayout rightFrame1 = items1.findViewById(R.id.frameRight);
        mySweepShaderView = new SweepShaderView(getContext(), 2);
        leftFrame1.addView(mySweepShaderView, param0);
        mySweepShaderView = new SweepShaderView(getContext(), 3);
        rightFrame1.addView(mySweepShaderView, param0);
      root.addView(items1, param1);

        return root;
    }

    public class SweepShaderView extends View {
        String modeTag;
        int tileMode, rectWidth, rectHeight;
        private Paint mPaint, xPaint;

        SweepShaderView(Context context, int shaderMode) {
            super(context);
            init(shaderMode);
            rectWidth = viewWidth;
            rectHeight = viewHeight - 70;
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

        @SuppressLint("DrawAllocation")
        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            SweepGradient mGradient = null;
            //填充颜色数组
            int[] colors = {0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00, 0xff00ffff};
            //与颜色数组对应的位置数组（0～1）
            float[] pos = {0f, 0.2f, 0.4f, 0.6f, 1.0f};
            //颜色沿着（x0,y0）～ (x1,y1)的直线渐变，多余部分会按照TileMode方式填充
            switch (tileMode) {
                case 0:
                case 1:
                    modeTag = "双色";
                    mGradient = new SweepGradient(rectWidth / 2, rectHeight / 2, 0xffff0000, 0xff00ff00);
                    break;
                case 2:
                case 3:
                    modeTag = "多色";
                    mGradient = new SweepGradient(rectWidth / 2, rectHeight / 2, colors, pos);
                    break;
            }

            //设置渲染
            mPaint.setShader(mGradient);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //实施渲染
            if (tileMode % 2 == 0)
                canvas.drawRect(0, 0, rectWidth, rectHeight, mPaint);
            else
                canvas.drawCircle(rectWidth / 2, rectHeight / 2, Math.min(rectHeight,rectWidth) / 2, mPaint);

            canvas.drawText(modeTag, getWidth() / 2, getHeight() - 30, xPaint);
        }
    }
}
