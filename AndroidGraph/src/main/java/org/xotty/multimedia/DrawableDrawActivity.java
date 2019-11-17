package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
            mDrawable.getPaint().setColor(0xff74AC23);
            mDrawable.setBounds(x, y, x + width, y + height);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mDrawable.draw(canvas);
        }
    }
}