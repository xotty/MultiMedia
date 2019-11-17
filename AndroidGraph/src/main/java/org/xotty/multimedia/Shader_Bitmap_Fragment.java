/**
 * BitmapShader(图形渲染，在超过原始Bitmap的位置以一定方式重复出现)应用：
 * 1)定义： BitmapShader(Bitmap bitmap, TileMode tileX, TileMode tileY)
 * 2)设置： mPaint.setShader(mBitmapShader)
 * 3)渲染： mCanvas.draw(......,mPaint)
 * 4)共有三种重复出现方式：TileMode.REPEAT、TileMode.MIRROR、TileMode.CLAMP
 * 5）始终以Bitmap所在view视图左上角为原点重复绘制Bitmap，不论绘制（draw）的位置和大小。
 */
package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class Shader_Bitmap_Fragment extends Fragment {
    private int rectWidth;
    private int rectHeight;

    public Shader_Bitmap_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(), R.style.AppTheme_NoActionBar);
        LayoutInflater localInflater = inflater
                .cloneInContext(contextThemeWrapper);
        Bitmap mBmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon);

        //构建Fragment的根视图LinearLayout
        LinearLayout root = new LinearLayout(getContext());
        root.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(param);


        //添加原始图
        LinearLayout items0 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame0 = items0.findViewById(R.id.frameLeft);
        BitmapShaderView myBitmapShaderView = new BitmapShaderView(getContext(), 0, mBmp);
        LinearLayout.LayoutParams param0 = new LinearLayout.LayoutParams(mBmp.getWidth(), (int) (mBmp.getHeight() * 1.5));
        leftFrame0.addView(myBitmapShaderView, param0);
        root.addView(items0);

        //添加Repeat和Mirror模式的渲染图
        LinearLayout items1 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame1 = items1.findViewById(R.id.frameLeft);
        FrameLayout rightFrame1 = items1.findViewById(R.id.frameRight);
        rectWidth = mBmp.getWidth() * 3;
        rectHeight = (int) (mBmp.getHeight() * 3.5f);
        myBitmapShaderView = new BitmapShaderView(getContext(), 1, mBmp);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(rectWidth, rectHeight);
        leftFrame1.addView(myBitmapShaderView, param1);
        myBitmapShaderView = new BitmapShaderView(getContext(), 2, mBmp);
        rightFrame1.addView(myBitmapShaderView, param1);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param2.topMargin = 30;
        root.addView(items1, param2);

        //添加Clamot和混合模式的渲染图
        LinearLayout items2 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame2 = items2.findViewById(R.id.frameLeft);
        FrameLayout rightFrame2 = items2.findViewById(R.id.frameRight);
        myBitmapShaderView = new BitmapShaderView(getContext(), 3, mBmp);
        leftFrame2.addView(myBitmapShaderView, param1);
        myBitmapShaderView = new BitmapShaderView(getContext(), 4, mBmp);
        rightFrame2.addView(myBitmapShaderView, param1);
        root.addView(items2, param2);

        //添加Clamot和混合模式的渲染图
        LinearLayout items3 = (LinearLayout) localInflater.inflate(R.layout.fragment_shader_item, container, false);
        FrameLayout leftFrame3 = items3.findViewById(R.id.frameLeft);
        FrameLayout rightFrame3 = items3.findViewById(R.id.frameRight);
        myBitmapShaderView = new BitmapShaderView(getContext(), 3, mBmp);
        LinearLayout.LayoutParams param4 = new LinearLayout.LayoutParams(rectWidth*2, rectHeight);

        leftFrame3.addView(myBitmapShaderView, param4);
        items3.removeView(rightFrame3);
//        myBitmapShaderView = new BitmapShaderView(getContext(), 4, mBmp);
//        rightFrame3.addView(myBitmapShaderView, param1);
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 1000);
        param2.topMargin = 30;
        root.addView(items3,param3);
        return root;
    }

    class BitmapShaderView extends View {
        Bitmap bitmap;
        String modeTag;
        Shader.TileMode xShaderMode, yShaderMode;
        private Paint mPaint, xPaint;

        BitmapShaderView(Context context, int shaderMode, Bitmap bmp) {
            super(context);
            bitmap = bmp;
            init(shaderMode);
        }

        private void init(int mode) {
            mPaint = new Paint();
            xPaint = new Paint();
            xPaint.setColor(Color.RED);
            xPaint.setStrokeWidth(5);
            xPaint.setStyle(Paint.Style.FILL);
            xPaint.setTextAlign(Paint.Align.CENTER);
            xPaint.setTextSize(40);
            switch (mode) {
                case 0:
                    modeTag = "原始图";
                    xShaderMode = Shader.TileMode.REPEAT;
                    yShaderMode = Shader.TileMode.MIRROR;
                    break;
                case 1:
                    modeTag = "TileMode.REPEAT";
                    xShaderMode = Shader.TileMode.REPEAT;
                    yShaderMode = Shader.TileMode.REPEAT;
                    break;
                case 2:
                    modeTag = "TileMode.MIRROR";
                    xShaderMode = Shader.TileMode.MIRROR;
                    yShaderMode = Shader.TileMode.MIRROR;
                    break;
                case 3:
                    modeTag = "TileMode.CLAMP";
                    xShaderMode = Shader.TileMode.CLAMP;
                    yShaderMode = Shader.TileMode.CLAMP;
                    break;
                case 4:
                    modeTag = "x：CLAMP   y: REPEAT";
                    xShaderMode = Shader.TileMode.CLAMP;
                    yShaderMode = Shader.TileMode.REPEAT;
                    break;
            }
            //设置渲染
            mPaint.setShader(new BitmapShader(bitmap, xShaderMode, yShaderMode));
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //实施渲染
            if (modeTag.equals("Origin")) {
                canvas.drawRect(0, 0, getWidth(), getHeight() - 60, mPaint);
                canvas.drawText(modeTag, getWidth() / 2, getHeight() - 20, xPaint);
            } else {
                canvas.drawRect(0, 0, getWidth(), getHeight() - 70, mPaint);
                canvas.drawText(modeTag, getWidth() / 2, getHeight() - 20, xPaint);
            }
        }
    }
}
