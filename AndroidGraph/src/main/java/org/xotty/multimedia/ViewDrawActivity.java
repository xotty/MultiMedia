package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;


public class ViewDrawActivity extends AppCompatActivity {
    private float[] mPts;
    private static final float SIZE = 300;
    private static final int SEGS = 32;
    private static final int X = 0;
    private static final int Y = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));

    }

    public class MyView extends View {
        public MyView(Context context) {
            super(context);
            mPts= GraphUtil.buildPoints();
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            GraphUtil.drawGraph(canvas,mPts);
        }
    }
}
