/**阴影显示方式：
 * 1)阴影层：setShadowLayer(float radius, float dx, float dy, int color)
 *         TextView类控件可以直接在XML中设置相关属性
 *         也可以用 <layer-list>标签组织Shape构建阴影效果（本例无此演示）
 * 2)模糊效果：BlurMaskFilter(float radius,BlurMaskFilter.Blur style)
 * 3)浮雕效果：EmbossMaskFilter (float[] direction,float ambient,float specular,float blurRadius)
 */
package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class PaintShadowFragment extends Fragment {

    NestedScrollView nestedScrollView;
    LinearLayout root;
    int sx,sy,dx,dy,wcenter,radius,mRadius;
    Paint mPaint, xPaint;

    public PaintShadowFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        wcenter =dm.widthPixels/2;

        nestedScrollView = (NestedScrollView) inflater.inflate(R.layout.fragment_createbmp, container, false);
        root = (LinearLayout) nestedScrollView.findViewById(R.id.rt);

        //setShadowLayer的效果
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1050);
        final ShadowLayerView myView1 = new ShadowLayerView(getContext());
        root.addView(myView1, params);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.shadow_button, container, false);
        root.addView(ll);

        //显示Shadow
        Button button_show = ll.findViewById(R.id.show);
        button_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView1.showShadow();
            }
        });
        //清除Shadow
        Button button_clear = ll.findViewById(R.id.clear);
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView1.clearShadow();
            }
        });

        //BlurMaskFilter效果
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 650);
        BlurMaskFilterView myView2 = new BlurMaskFilterView(getContext());
        root.addView(myView2, params);

        //EmBossMaskFilter效果
        EmbossMaskFilterView myView3 = new EmbossMaskFilterView(getContext());
        root.addView(myView3, params);

        return nestedScrollView;
    }

    private  class ShadowLayerView extends View {
        Bitmap bitmap;
        private int xRadius = 2, mDx = 10, mDy = 10;
        private boolean mSetShadow = true;
        Rect mRect;

        public ShadowLayerView(Context context) {
            super(context);
            init(this);
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scene);
            mRect=new Rect(100, 520, 100 + bitmap.getWidth(), 520 + bitmap.getHeight());
        }

        public void clearShadow() {
            mSetShadow = false;
            postInvalidate();
        }

        public void showShadow() {
            mSetShadow = true;
            postInvalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawText("1.setShadowLayer", sx, sy, xPaint);
            if (mSetShadow) {
                mPaint.setShadowLayer(xRadius, mDx, mDy, Color.GRAY);
            } else {
                mPaint.clearShadowLayer();
            }
            //文字阴影
            canvas.drawText("花开花落，云卷云舒", wcenter, sy+dy, mPaint);
            //图形阴影
            canvas.drawCircle(wcenter, sx*3+2*dy, radius*2, mPaint);
            //图片阴影
            canvas.drawBitmap(bitmap, null, mRect, mPaint);
        }
    }

    //Blur.INNER(内发光)、Blur.SOLID(外发光)、Blur.NORMAL(内外发光)、Blur.OUTER(仅发光部分可见)
    private class BlurMaskFilterView extends View {
        BlurMaskFilter innerFilter,solidFilter,normalFilter,outerFilter;

        public BlurMaskFilterView(Context context) {
            super(context);
            init(this);
            mPaint.clearShadowLayer();
            innerFilter=new BlurMaskFilter(mRadius, BlurMaskFilter.Blur.INNER);
            solidFilter=new BlurMaskFilter(mRadius, BlurMaskFilter.Blur.SOLID);
            normalFilter= new BlurMaskFilter(mRadius, BlurMaskFilter.Blur.NORMAL);
            outerFilter=new BlurMaskFilter(mRadius, BlurMaskFilter.Blur.OUTER);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mPaint.clearShadowLayer();
            canvas.drawText("2.BlurMaskFilter", sx, sy, xPaint);

            //内发光遮罩过滤器
            mPaint.setMaskFilter(innerFilter);
            canvas.drawCircle(dx, 400, radius*2, mPaint);
            canvas.drawText("Inner", sx+60, 400+radius*4, xPaint);

            //外发光遮罩过滤器
            mPaint.setMaskFilter(solidFilter);
            canvas.drawCircle(dx*3-sx*2, 400, radius*2, mPaint);
            canvas.drawText("Solid", sx+330, 400+radius*4, xPaint);

            //外发光遮罩过滤器用于文字
            canvas.drawText("Hello World", 500, 200, mPaint);

            //内外发光遮罩过滤器
            mPaint.setMaskFilter(normalFilter);
            canvas.drawCircle(dx*4+70, 400, radius*2, mPaint);
            canvas.drawText("Normal", 600, 400+radius*4, xPaint);

            //边缘发光遮罩过滤器
            mPaint.setMaskFilter(outerFilter);
            canvas.drawCircle(dx*6+sx, 400, radius*2, mPaint);
            canvas.drawText("Outer", 860, 400+radius*4, xPaint);
        }
    }

    private  class EmbossMaskFilterView extends View {

        float[] direction = new float[]{1, 1, 1};    //指定光源的方向
        float light = 0.3f;                          //光照亮度（0～1）
        float specular = 1f;                         //反光系数
        float blur = 5;                              //模糊半径
        EmbossMaskFilter embossMaskFilter;

        public EmbossMaskFilterView(Context context) {
            super(context, null, 0);
            init(this);
            embossMaskFilter=new EmbossMaskFilter(direction, light, specular, blur);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mPaint.clearShadowLayer();

            canvas.drawText("3.EmbossMaskFilter", sx, sy, xPaint);
            //设置浮雕滤镜
            mPaint.setMaskFilter(embossMaskFilter);
            //画文字
            mPaint.setTextSize(150);
            mPaint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
            canvas.drawText("Hello Android", wcenter, sy+dy, mPaint);
            //画图形1
            canvas.drawCircle(300, 450, 150, mPaint);
            //画图形2
            mPaint.setColor(Color.BLUE);
            canvas.drawCircle(800, 450, 150, mPaint);
            mPaint.setTextSize(80);
            mPaint.setColor(Color.RED);

        }
    }

    private void init(View view) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(80);
        xPaint.setColor(Color.BLACK);
        xPaint.setTextSize(50);
        mPaint.setTextAlign(Paint.Align.CENTER);
        sx=20;
        sy=70;
        dx=150;
        dy=150;
        radius=50;
        mRadius=50;
    }

}
