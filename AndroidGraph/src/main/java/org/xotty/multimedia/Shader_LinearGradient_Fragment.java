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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Shader_LinearGradient_Fragment extends Fragment {
    private int rectWidth = 500;
    private int rectHeight = 400;

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
        root.addView(items0);

        //添加Repeat和Mirror模式的填充渲染图
        LinearLayout items1 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame1 = items1.findViewById(R.id.frameLeft);
        FrameLayout rightFrame1 = items1.findViewById(R.id.frameRight);
        myLinearShaderView = new LinearShaderView(getContext(), 2);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(rectWidth, rectHeight);
        leftFrame1.addView(myLinearShaderView, param1);
        myLinearShaderView = new LinearShaderView(getContext(), 3);
        rightFrame1.addView(myLinearShaderView, param1);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param2.topMargin = 30;
        root.addView(items1, param2);

        //添加Clamp模式的填充渲染图
        LinearLayout items2 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame2 = items2.findViewById(R.id.frameLeft);
        FrameLayout rightFrame2 = items2.findViewById(R.id.frameRight);
        myLinearShaderView = new LinearShaderView(getContext(), 4);
        leftFrame2.addView(myLinearShaderView, param1);
        myLinearShaderView = new LinearShaderView(getContext(), 5);
        rightFrame2.addView(myLinearShaderView, param1);
        root.addView(items2, param2);
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
            }

            //设置渲染
            mPaint.setShader(mGradient);

        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            super.onDraw(canvas);
            //实施渲染
            canvas.drawRect(0, 0, getWidth(), getHeight() - 70, mPaint);
            canvas.drawText(modeTag, getWidth() / 2, getHeight() - 20, xPaint);
        }
    }
}
