/**
 * 演示位图的几种常见操作：
 * 1）将视图转换为Bitmap：当前屏幕完全可见、部分可见、完全不可见的视图
 * 2）压缩和保存
 * 3）变换：平移、旋转、缩放、斜切
 */
package org.xotty.multimedia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class OperateBmpFragment extends Fragment {
    private NestedScrollView nestedScrollView;
    private LinearLayout fragmentLayout;
    private File filePath;
    private String title = "";
    private TextView tv;
    private ImageView iv;
    private int srcSize, dstSize, srcFileSize, dstFileSize;
    private Bitmap srcBmp, dstBitmap;
    private ContentLoadingProgressBar bar;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 表明消息是由该程序发送的
            if (msg.what == 0xA0) {
                bar.hide();
            } else if (msg.what == 0xA1) {
                bar.hide();
                iv.setVisibility(View.VISIBLE);
                dstSize = getBitmapSize(dstBitmap);

                dstFileSize = (int) writeBitmapToFile(filePath + "/" + title + ".jpg", dstBitmap);
                DecimalFormat df = new DecimalFormat("#,###");

                tv.setText(title + "\n原图文件:" + df.format(srcFileSize) + " 压缩后文件：" + df.format(dstFileSize) + "\n原图内存：" + df.format(srcSize) + " 压缩后内存：" + df.format(dstSize));
                iv.setImageBitmap(dstBitmap);
            }
        }
    };
    private boolean isBT1Pressed;

    public OperateBmpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getContext().getCacheDir();
        //读取原始图片
        new Thread() {
            @Override
            public void run() {
                try {
                    InputStream in = BitmapActivity.class.getResourceAsStream("/res/raw/sample.png");
                    if (in != null) {
                        srcFileSize = in.available();
                        byte[] bmpArray = toByteArray(in);
                        srcBmp = BitmapFactory.decodeByteArray(bmpArray, 0, srcFileSize);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0xA0);
            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operatebmp, container, false);
        Button bt1 = view.findViewById(R.id.bt1);
        Button bt2 = view.findViewById(R.id.bt2);
        Button bt3 = view.findViewById(R.id.bt3);
        bar = view.findViewById(R.id.pbar);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnClick(view);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnClick(view);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnClick(view);
            }
        });
        tv = view.findViewById(R.id.tv);
        iv = view.findViewById(R.id.iv);
        fragmentLayout = view.findViewById(R.id.ll);
        nestedScrollView = view.findViewById(R.id.sr);
        return view;
    }

    public void onBtnClick(View btn) {
        PopupMenu popup = new PopupMenu(getActivity(), btn);
        switch (btn.getId()) {
            case R.id.bt1:
                //在视图中放第一张张图片
                tv.setText("View转Bitmap");
                iv.setVisibility(View.VISIBLE);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setImageResource(R.drawable.scene1);
                //动态添加第二张图片
                if (!isBT1Pressed) {
                    isBT1Pressed = true;
                    final ImageView imageView = new ImageView(getActivity());

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(600,
                            900);
                    params.gravity = Gravity.CENTER;
                    params.topMargin = 30;
                    imageView.setLayoutParams(params);

                    imageView.setImageResource(R.drawable.scene2);
                    fragmentLayout.addView(imageView, params);
                    View fragmentView = fragmentLayout.getRootView();
                    Log.i("TAG", "size0: " + fragmentView.getWidth() + "----" + fragmentView.getHeight() + "----" + imageView.getHeight());
                    //当前界面绘制完成后开始截图
                    fragmentView.post(new Runnable() {
                        @Override
                        public void run() {
                            //当前界面全屏截图
                            Bitmap bmp = view2Bitmap1(getActivity().getWindow().getDecorView());
                            writeBitmapToFile(filePath + "/" + "view1.jpg", bmp);

                            //计算ScrollView的高度以便把超出屏幕的内容也能纳入进来
                            int sumHeight = 0;
                            for (int i = 0; i < fragmentLayout.getChildCount(); i++) {
                                sumHeight += fragmentLayout.getChildAt(i).getHeight();
                            }

                            //将部分可见的视图全部内容截图
                            bmp = view2Bitmap2(nestedScrollView, fragmentLayout.getWidth(), sumHeight);
                            writeBitmapToFile(filePath + "/" + "view2.jpg", bmp);
                        }
                    });

                    //将上一个 Fragment(CreatBmpFragment)中的视图（当前完全不可见）传入后截图
                    View view = ((BitmapActivity) getActivity()).getFragment1View();
                    Bitmap bmp = view2Bitmap3(view, view.getWidth(), view.getHeight());
                    writeBitmapToFile(filePath + "/" + "view3.jpg", bmp);
                }
                break;
            case R.id.bt2:
                if (isBT1Pressed) {
                    isBT1Pressed = false;
                    fragmentLayout.removeViewAt(fragmentLayout.getChildCount() - 1);
                }
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                // 将R.menu.popup_menu菜单资源加载到popup菜单中
                getActivity().getMenuInflater().inflate(R.menu.popup_menu2, popup.getMenu());
                // 为popup菜单的菜单项单击事件绑定事件监听器
                popup.setOnMenuItemClickListener(
                        new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                iv.setVisibility(View.GONE);
                                Log.i("TAG", "onClickMenu: ");
                                bar.show();
                                switch (item.getItemId()) {
                                    case R.id.compress:
                                        title = "Compress";
                                        srcSize = getBitmapSize(srcBmp);
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                dstBitmap = compressQuality(srcBmp, 20);
                                                mHandler.sendEmptyMessage(0xA1);
                                            }
                                        }.start();
                                        break;
                                    case R.id.sampling:
                                        bar.show();
                                        title = "Sampling";
                                        new Thread() {
                                            @Override
                                            public void run() {

                                                dstBitmap = compressSampling(srcBmp);
                                                mHandler.sendEmptyMessage(0xA1);
                                            }
                                        }.start();
                                        break;
                                    case R.id.matrix:
                                        title = "Matrix";
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                dstBitmap = compressMatrix(srcBmp, 0.5f, 0.8f);
                                                mHandler.sendEmptyMessage(0xA1);
                                            }
                                        }.start();
                                        break;
                                    case R.id.rgb565:
                                        title = "RGB_565";
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                dstBitmap = compressRGB565(srcBmp);
                                                mHandler.sendEmptyMessage(0xA1);
                                            }
                                        }.start();
                                        break;
                                    case R.id.scalebmp:
                                        title = "Scale BMP";
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                dstBitmap = compressScaleBitmap(srcBmp, 500, 300);
                                                mHandler.sendEmptyMessage(0xA1);
                                            }
                                        }.start();
                                        break;
                                }
                                tv.setText(title + "   Start......");
                                tv.setGravity(Gravity.CENTER);

                                return true;
                            }
                        });
                popup.show();

                break;
            case R.id.bt3:
                if (isBT1Pressed) {
                    isBT1Pressed = false;
                    fragmentLayout.removeViewAt(fragmentLayout.getChildCount() - 1);
                }

                //Bitmap可以通过Matrix进行各种变换
                iv.setVisibility(View.VISIBLE);
                Matrix matrix = new Matrix();
                //旋转
                matrix.postRotate(-45);
                //缩放
                matrix.postScale(0.2f, 0.2f);
                //斜切
                matrix.postSkew(0.5f, 1f);
                Bitmap resultBmp = Bitmap.createBitmap(srcBmp, 0, 0, srcBmp.getWidth(), srcBmp.getHeight(), matrix, true);

                tv.setText("平移、缩放、旋转、斜切");
                iv.setScaleType(ImageView.ScaleType.MATRIX);
                iv.setImageBitmap(resultBmp);

                matrix.reset();
                //平移
                matrix.preTranslate(100, 100);
                iv.setImageMatrix(matrix);
                break;
        }
    }

    //将位图存入文件
    private long writeBitmapToFile(String filePath, Bitmap b) {
        long length = 0;
        try {
            File desFile = new File(filePath);
            FileOutputStream fos = new FileOutputStream(desFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                /*可替代方法
                b.compress(Bitmap.CompressFormat.PNG,100, bos);
                b.compress(Bitmap.CompressFormat.WEBP,100, bos);*/

            bos.flush();
            bos.close();
            length = desFile.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return length;
    }

    private Bitmap compressQuality(Bitmap bitmap, int quality) {

        return codec(bitmap, Bitmap.CompressFormat.JPEG, quality, null);
    }


    private Bitmap compressSampling(Bitmap bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        return codec(bitmap, Bitmap.CompressFormat.PNG, 0, options);
    }

    private Bitmap compressMatrix(Bitmap bitmap, float xs, float ys) {
        Matrix matrix = new Matrix();
        if (xs > 1) xs = 1.0f;
        if (ys > 1) ys = 1.0f;
        matrix.setScale(xs, ys);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap compressRGB565(Bitmap bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return codec(bitmap, Bitmap.CompressFormat.PNG, 0, options);
    }

    private Bitmap compressScaleBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
                         int quality, BitmapFactory.Options opts) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);

        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length, opts);
    }

    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            Log.i("TAG", "getBitmapSize: " + bitmap.getAllocationByteCount());
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    private byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n ;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    //将已经显示在屏幕上的View转成图片
    private Bitmap view2Bitmap1(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    //将没有在屏幕上显示完全的view转成图片
    private Bitmap view2Bitmap2(View view, int width, int height) {
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bmp;
    }

    //将完全没有显示在屏幕上的view转成图片
    private Bitmap view2Bitmap3(View view, int width, int height) {
        //测量view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        view.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        return view2Bitmap2(view, view.getWidth(), view.getHeight());
    }
}
