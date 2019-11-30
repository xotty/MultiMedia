/**
 * 构建生成Bitmap的方法：
 * 1、createBitmap             常用5种，见M1
 * 2、BitmapFactory.decodeXXX  5种(openRawResource) 见M3和M4
 * 3、Drawable转换              2种, 见M2和M5
 * 4、Canvas mCanvas = new Canvas(myBitmap);
 *    mCanvas.drawXXX          见BitmapDrawActivity
 */
package org.xotty.multimedia;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class CreateBmpFragment extends Fragment {

    NestedScrollView nestedScrollView;
    BitmapCreateView cv;
    LinearLayout root;

    public CreateBmpFragment() {
        //使得网络请求可以同步使用,Android3.0之后不能在主线程中进行HTTP请求
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cv = new BitmapCreateView(getContext());

        //在工作目录中准备位图文件，以便后续程序读取使用
        try {
            File file = new File(getContext().getCacheDir() + "/scene.jpg");
            InputStream in = getResources().openRawResource(R.raw.scene);
            OutputStream out = new FileOutputStream(file);
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(), R.style.AppTheme_NoActionBar);
        LayoutInflater localInflater = inflater
                .cloneInContext(contextThemeWrapper);
        nestedScrollView = (NestedScrollView) localInflater.inflate(R.layout.fragment_createbmp, container, false);
        root = (LinearLayout) nestedScrollView.findViewById(R.id.rt);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1600);
        root.addView(cv, params);
        return nestedScrollView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View fragmentView = root;
        ((BitmapActivity) getActivity()).setFragment1View(fragmentView);
    }

    class BitmapCreateView extends View {

        private static final int w = 300;
        private static final int h = 100;
        private static final int xStart = 50;
        private static final int yStart = 50;
        private Bitmap defaultBitmap = null;

        public BitmapCreateView(Context context) {
            super(context);
            defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_placeholder);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStrokeWidth(5);  //设置画笔宽度
            paint.setAntiAlias(true); //
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextSize(60);

            /*1)createBitmap(int width, int height, Config config)　
              2)createBitmap(Bitmap src)　
              3)createBitmap(Bitmap src, int x, int y, int width, int height)　
              4)createBitmap(int[] colors,int width, int height, Config config)
              5)createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter)
            */
            //Drawable --> Bitmap
            canvas.drawText("1", xStart + w / 2, yStart - 10, paint);
            Bitmap bitmap1 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas mCanvas = new Canvas(bitmap1);
            Drawable drawable = getContext().getDrawable(R.drawable.scene);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(mCanvas);
            canvas.drawBitmap(bitmap1, xStart, yStart, null);
            bitmapRecycle(bitmap1);

            canvas.drawText("2", xStart + w + 50 + w / 2, yStart - 10, paint);
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            Bitmap bitmap2 = Bitmap.createBitmap(metrics, w, h, Bitmap.Config.RGB_565);
            canvas.drawBitmap(bitmap2, xStart + w + 50, yStart, null);
            bitmapRecycle(bitmap2);

            canvas.drawText("3", xStart + 2 * w + 100 + w / 2, yStart - 10, paint);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            int[] pixels = new int[width * height];
            bmp.getPixels(pixels, 0, width, 0, 0, width, height);
            Bitmap bitmap3 = Bitmap.createBitmap(pixels, width, h, Bitmap.Config.ARGB_4444);
            canvas.drawBitmap(bitmap3, xStart + 2 * w + 100, yStart, null);
            bitmapRecycle(bitmap3);

            //decodeResource、decodeFile、decodeStream、decodeByteArray、decodeFileDescriptor
            //decodeResource在解析时会根据当前设备屏幕像素密度densityDpi对Bitmap进行缩放适配操作
            //From Resource
            canvas.drawText("4", xStart + w / 2, yStart + 200 - 10, paint);
            Bitmap bitmap4 = readBitmapFromResource1(getResources(), R.drawable.scene, w, h);
            canvas.drawBitmap(bitmap4, xStart, yStart + 200, null);
            bitmapRecycle(bitmap4);

            canvas.drawText("5", xStart + w + 250 + w / 2, yStart + 200 - 10, paint);
            Bitmap bitmap5 = readBitmapFromResource2(getResources(), R.raw.scene, w, h + 60);
            canvas.drawBitmap(bitmap5, xStart + w + 250, yStart + 200, null);
            bitmapRecycle(bitmap5);

            canvas.drawText("6", xStart + w / 2, yStart + 200 * 2 + 150 - 10, paint);
            Bitmap bitmap6 = readBitmapFromResource3(R.drawable.scene, w, h);
            canvas.drawBitmap(bitmap6, xStart, yStart + 200 * 2 + 100 + 150, null);
            bitmapRecycle(bitmap6);

            canvas.drawText("7", xStart + w + 150 + w / 2, yStart + 200 * 2 + 150 - 10, paint);
            Bitmap bitmap7 = readBitmapFromResource4();
            canvas.drawBitmap(bitmap7, xStart + w + 150, yStart + 200 * 2 + 150, null);
            bitmapRecycle(bitmap7);

            //From File
            canvas.drawText("8", xStart + w / 2, yStart + 200 * 4 + 50 - 10, paint);
            String path = getContext().getCacheDir() + "/scene.jpg";
            Log.i("TAG", "onDraw: +path");
            Bitmap bitmap8 = readBitmapFromFile1(path, w, h);
            canvas.drawBitmap(bitmap8, xStart, yStart + 200 * 4 + 50, null);
            bitmapRecycle(bitmap8);

            canvas.drawText("9", xStart + w + 150 + w / 2, yStart + 200 * 4 + 50 - 10, paint);
            Bitmap bitmap9 = readBitmapFromFile2(path);
            canvas.drawBitmap(bitmap9, xStart + w + 150, yStart + 200 * 4 + 50, null);
            bitmapRecycle(bitmap9);

            canvas.drawText("10", xStart + w / 2, yStart + 200 * 5 + 100 - 10, paint);
            Bitmap bitmap10 = readBitmapFromFileDescriptor(path, w, h);
            canvas.drawBitmap(bitmap10, xStart, yStart + 200 * 5 + 100, null);
            bitmapRecycle(bitmap10);

            canvas.drawText("11", xStart + w + 150 + w / 2, yStart + 200 * 5 + 100 - 10, paint);
            Bitmap bitmap11 = readBitmapFromAssetsFile(getContext(), "scene.jpg");
            canvas.drawBitmap(bitmap11, xStart + w + 150, yStart + 200 * 5 + 100, null);
            bitmapRecycle(bitmap11);

            //From Stream
            canvas.drawText("12", xStart + w / 2, yStart + 200 * 6 + 150 - 10, paint);
            BufferedInputStream inputStream = null;
            Bitmap bitmap12 = null;
            try {
                //创建URL对象，获取网址
                URL url = new URL("https://www.baidu.com/img/bd_logo1.png");
                //打开连接
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //接收输入流
                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                bitmap12 = readBitmapFromInputStream(inputStream, w, h);
                canvas.drawBitmap(bitmap12, xStart, yStart + 200 * 6 + 150, null);
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmapRecycle(bitmap12);

            //From ByteArray
            canvas.drawText("13", xStart + w + 150 + w / 2, yStart + 200 * 6 + 150 - 10, paint);
            byte[] data = null;
            AssetManager am = getResources().getAssets();
            try {
                InputStream is = am.open("scene.txt");    //scene.jpg的Base64文件
                int length = is.available();
                data = new byte[length];

                //InputStream --> byte[]
                is.read(data);
                // byte[] --> InputStream
                //InputStream is = new ByteArrayInputStream(data)

                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] input = Base64.decode(data, Base64.DEFAULT);
            Bitmap bitmap13 = readBitmapFromByteArray(input, w, h);
            Log.i("TAG", "readBitmapFromByteArray: ");
            canvas.drawBitmap(bitmap13, xStart + w + 150, yStart + 200 * 6 + 150 - 10, null);
            bitmapRecycle(bitmap13);

            //inputStream --> outputStream
            //  byte[] buffer = new byte[1024];
            //  while ((len = inputStream.read(buffer)) != -1) {
            //      outputStrean.write(buffer, 0, len);}

            //outputStream --> inputStream
            //inputStream=new ByteArrayInputStream(outputStream.toByteArray());
        }

        //4
        private Bitmap readBitmapFromResource1(Resources resources, int resourcesId, int width, int height) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(resources, resourcesId, options);
            options = setOptions(options, width, height);

            //Resoureces --> Bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourcesId, options);
            if (bitmap == null) bitmap = defaultBitmap;
            return bitmap;
        }

        //5
        private Bitmap readBitmapFromResource2(Resources resources, int resourcesId, int width, int height) {
            Bitmap bitmap = null;
            try {
                InputStream ins = resources.openRawResource(resourcesId);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(resources, resourcesId, options);
                options = setOptions(options, width, height);
                //InputStream --> Bitmap
                bitmap = BitmapFactory.decodeStream(ins, null, options);
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap == null) bitmap = defaultBitmap;
            return bitmap;
        }

        //6
        private Bitmap readBitmapFromResource3(int resourcesId, int width, int height) {
            //Drawable --> Bitmap
            BitmapDrawable bitmapDrawable = (BitmapDrawable) getContext().getDrawable(resourcesId);
            int h = bitmapDrawable.getIntrinsicHeight();
            int w = bitmapDrawable.getIntrinsicWidth();
            Bitmap newBmp = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), width, height, true);
            if (newBmp == null) newBmp = defaultBitmap;
            return newBmp;

            //Bitmap --> Drawable
            //bitmapDrawable = new BitmapDrawable(getResources(),bitmap);
        }

        //7
        private Bitmap readBitmapFromResource4() {
            //Drawable --> Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(BitmapActivity.class.getResourceAsStream("/res/raw/scene.jpg"));
            /*等价替代：
            Bitmap bitmap=BitmapFactory.decodeStream(BitmapActivity.class.getClassLoader().getResourceAsStream("res/raw/scene.jpg"));
            资源也可以来自assets/scene.jpg或res/drawable-v24/scene.jpg*/
            if (bitmap == null) bitmap = defaultBitmap;
            return bitmap;
        }

        //8
        private Bitmap readBitmapFromFile1(String filePath, int width, int height) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            options = setOptions(options, width, height);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            if (bitmap == null) bitmap = defaultBitmap;
            return bitmap;
        }

        //9
        private Bitmap readBitmapFromFile2(String filePath) {
            Bitmap bitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            try {
                InputStream fis = new FileInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(fis, null, options);
                Log.i("TAG", "readBitmapFromFile2: ");
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("TAG", "readBitmapFromFile2:  Exception");
            }
            if (bitmap == null) {
                bitmap = defaultBitmap;
                Log.i("TAG", "readBitmapFromResource2: null" + defaultBitmap);
            }

            return bitmap;
        }

        //10
        private Bitmap readBitmapFromFileDescriptor(String filePath, int width, int height) {
            Bitmap bitmap = null;
            try {
                FileInputStream fis = new FileInputStream(filePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
                options = setOptions(options, width, height);
                FileDescriptor fd = fis.getFD();
                bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bitmap == null) bitmap = defaultBitmap;
            return bitmap;
        }

        //11
        private Bitmap readBitmapFromAssetsFile(Context context, String filePath) {
            Bitmap bitmap = null;
            AssetManager am = context.getResources().getAssets();
            try {
                InputStream is = am.open(filePath);
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap == null) bitmap = defaultBitmap;
            return bitmap;
        }

        //12
        private Bitmap readBitmapFromInputStream(BufferedInputStream ins, int width, int height) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            ins.mark(0);
            BitmapFactory.decodeStream(ins, null, options);
            try {
                ins.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
            options = setOptions(options, width, height);
            Bitmap bitmap = BitmapFactory.decodeStream(ins, null, options);
            if (bitmap == null) bitmap = defaultBitmap;
            return bitmap;
        }

        //13
        private Bitmap readBitmapFromByteArray(byte[] data, int width, int height) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            options = setOptions(options, width, height);

            //byte[] --> Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            if (bitmap == null) bitmap = defaultBitmap;
            return bitmap;

            // Bitmap --> byte[]
            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // byte[] data = baos.toByteArray();    //OutputStream --> byte[]

            //baos.write(data)                      //byte[] --> OutputStream
        }

        private BitmapFactory.Options setOptions(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;
            if (srcHeight > reqHeight || srcWidth > reqWidth) {
                // 计算出实际宽高和目标宽高的比率
                final int heightRatio = Math.round(srcHeight / (float) reqHeight);
                final int widthRatio = Math.round(srcWidth / (float) reqWidth);
                inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
            }
            BitmapFactory.Options mOptions = new BitmapFactory.Options();
            mOptions.inJustDecodeBounds = false;
            mOptions.inSampleSize = inSampleSize;
            return mOptions;
        }

        private void bitmapRecycle(Bitmap bitmap) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

}
