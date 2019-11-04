package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BitmapDrawActivity extends AppCompatActivity {
    private static final int WIDTH = 960;
    private static final int HEIGHT = 1280;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyBitmapView(this));
    }

    private static class MyBitmapView extends View {
        private Bitmap myBitmap;
        private float[] mPts;

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(myBitmap, 0, 0, null);
        }

        public MyBitmapView(Context context) {
            super(context);
            mPts = GraphUtil.buildPoints();
            myBitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(myBitmap);
            /*等效替代
            Canvas  canvas = new Canvas();
            canvas.setBitmap(myBitmap);*/

            //此时生成Bitmap的内容
            GraphUtil.drawGraph(canvas, mPts);
        }
    }
}
