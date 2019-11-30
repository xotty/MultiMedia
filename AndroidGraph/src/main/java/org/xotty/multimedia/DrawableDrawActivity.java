package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DrawableDrawActivity extends AppCompatActivity {
    CustomDrawableView mCustomDrawableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
    }


    public class CustomDrawableView extends View {
        private ShapeDrawable mDrawable;

        public CustomDrawableView(Context context) {
            super(context);
            int x = 150;
            int y = 50;
            int width = 800;
            int height = 200;
            mDrawable = new ShapeDrawable(new OvalShape());

            //设置drawable绘制的颜色
            mDrawable.getPaint().setColor(Color.GREEN);

            //设置drawable绘制的位置
            mDrawable.setBounds(x, y, x + width, y + height);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mDrawable.draw(canvas);
        }
    }
}