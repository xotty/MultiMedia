package org.xotty.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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


        public MyBitmapView(Context context) {
            super(context);
            mPts = GraphUtil.buildPoints();
            myBitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            Canvas mCanvas = new Canvas(myBitmap);
            /*等效替代
            Canvas  canvas = new Canvas();
            canvas.setBitmap(myBitmap);*/

            //生成Bitmap
            GraphUtil.drawGraph(mCanvas, mPts);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //绘制Bitmap
            canvas.drawBitmap(myBitmap, 0, 0, null);
        }
    }
}
