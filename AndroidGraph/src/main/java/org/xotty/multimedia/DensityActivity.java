/**
 * 一、不同图片资源的获取和解码
 * 1) BitmapFactory.decodeResource(R.drawable.xxx)获取Bitmap
 * 2) getResources().getDrawable(R.drawable.xxx,null)获取Drawable对象
 *    二者缺省时都是要进行缩放的，缩放比例的计算方法见二。当BitmapFactory.Options()的inScaled=false时Bitmap不缩放。
 *    使用decodeFile(...)的时候，不设置对应inDensity,inTargetDensity,inScreenDensity，系统是不会对Bitmap进行缩放操作
 * 二、不同图片的缩放和显示
 * 1）缩放比例的计算
 * （1）与图片所在drawable文件夹有关:px-->dp(dp=px/drawable-xdpi文件夹代表的基准比例，drawable和drawable-nodpi的基准比例为1）
 * （2）与targetDensity有关：dp-->px(px=dp*targetDensity/设备Density*与设备Density相当的基准比例，drawable-nodpi时该比例为1)
 * 2）显示
 * (1)imageView.setImageDrawable(mDrawable)/imageView.setImageBitmap(bitmap)
 *    imageView.setImageResource(R.drawable.xxx)/imageView.setImageURI(mUri)
 * (2)canvas.drawBitmap(mBitmap)生成自定义View
 * (3)view.setBackground(mDrawabe),view.setBackgroundResource(R.drawable.xxx)
 * 三、Density
 * 1）设备的density：随设备固定
 * 2）Bitmap的density：缺省为设备density，可更改mBitmap.setDensity()
 * 3）Drawable的targetDensity:缺省为设备density，可更改mDrawable.setTargetDensity()
 * BitmapFactoy中的options也有以上三个参数：options.inScreenDensity、options.inDensity、options.inTargetDensity
 * 后两个参数的缺省值均为设备Density或drawable文件夹对应的Density。若二者不一致且inScale=true时则会缩放图片，缩放比例为scale=inTargetDensity/inDensity
 */

package org.xotty.multimedia;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DensityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater li = (LayoutInflater) getSystemService(
                LAYOUT_INFLATER_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.setTitle("Scale="+dm.density+ "   DensityDPI="+dm.densityDpi);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        //Resource-->Bitmap-->BitmapDrawable ,scale=true
        LinearLayout layout = new LinearLayout(this);
        addBitmapDrawable(layout, R.drawable.logo120, true);
        addBitmapDrawable(layout, R.drawable.logo160, true);
        addBitmapDrawable(layout, R.drawable.logo240, true);
        addLabelToRoot(root, "1.Manual Scaled bitmap in drawable");
        addChildToRoot(root, layout);
//        layout = new LinearLayout(this);
//        addBitmapDrawable(layout, R.drawable.logo320, true);
//        addBitmapDrawable(layout, R.drawable.logo480, true);
//        addBitmapDrawable(layout, R.drawable.logo640, true);
//        addLabelToRoot(root, " ");
//        addChildToRoot(root, layout);

        //Resource-->Bitmap-->BitmapDrawable ,scale=false
        layout = new LinearLayout(this);
        addBitmapDrawable(layout, R.drawable.logo120, false);
        addBitmapDrawable(layout, R.drawable.logo160, false);
        addBitmapDrawable(layout, R.drawable.logo240, false);

        addLabelToRoot(root, "2. Auto Scaled bitmap in drawable");
        addChildToRoot(root, layout);
//        layout = new LinearLayout(this);
//        addBitmapDrawable(layout, R.drawable.logo320, false);
//        addBitmapDrawable(layout, R.drawable.logo480, false);
//        addBitmapDrawable(layout, R.drawable.logo640, false);
//        addLabelToRoot(root, " ");
//        addChildToRoot(root, layout);

        //Resource-->Drawable(drawable-xxxdpi)
        layout = new LinearLayout(this);
        addResourceDrawable(layout, R.drawable.logo120);
        addResourceDrawable(layout, R.drawable.logo160);
        addResourceDrawable(layout, R.drawable.logo240);
        addLabelToRoot(root, "3.X-dpi Resource drawable(Auto Scaled)");
        addChildToRoot(root, layout);

//        layout = new LinearLayout(this);
//        addResourceDrawable(layout, R.drawable.logo320);
//        addResourceDrawable(layout, R.drawable.logo480);
//        addResourceDrawable(layout, R.drawable.logo640);
//        addLabelToRoot(root, " ");
//        addChildToRoot(root, layout);

        //Resource-->Drawable(drawable-nodpi)
        layout = new LinearLayout(this);
        addResourceDrawable(layout, R.drawable.n120);
        addResourceDrawable(layout, R.drawable.n160);
        addResourceDrawable(layout, R.drawable.n240);
        addResourceDrawable(layout, R.drawable.n320);
        addLabelToRoot(root, "4.No-dpi resource drawable(No Scaled)");
        addChildToRoot(root, layout);
        layout = new LinearLayout(this);
        addResourceDrawable(layout, R.drawable.n480);
        addResourceDrawable(layout, R.drawable.n640);
        addLabelToRoot(root, "");
        addChildToRoot(root, layout);

        //Resource-->Bitmap-->View ,scale=true
        layout = new LinearLayout(this);
        addScaledBitmap(layout, R.drawable.logo120, true);
        addScaledBitmap(layout, R.drawable.logo160, true);
        addScaledBitmap(layout, R.drawable.logo240, true);
        addLabelToRoot(root, "5.1 Prescaled bitmap in Scaled View ");
        addChildToRoot(root, layout);
        //Resource-->Bitmap-->View ,scale=false
        layout = new LinearLayout(this);
        addScaledBitmap(layout, R.drawable.logo120, false);
        addScaledBitmap(layout, R.drawable.logo160, false);
        addScaledBitmap(layout, R.drawable.logo240, false);
        addLabelToRoot(root, "5.2 Prescaled bitmap in No Scaled View");
        addChildToRoot(root, layout);


        //Resource-->Bitmap-->View ,scale=true
        layout = new LinearLayout(this);
        addNoScaledBitmap(layout, R.drawable.logo320, true);
        addNoScaledBitmap(layout, R.drawable.logo480, true);
        addNoScaledBitmap(layout, R.drawable.logo640, true);
        addLabelToRoot(root, "5.3 NoScaled bitmap in Scaled View ");
        addChildToRoot(root, layout);
        //Resource-->Bitmap-->View ,scale=false
        layout = new LinearLayout(this);
        addNoScaledBitmap(layout, R.drawable.logo320, false);
        addNoScaledBitmap(layout, R.drawable.logo480, false);
        addNoScaledBitmap(layout, R.drawable.logo640, false);
        addLabelToRoot(root, "5.4 NoScaled bitmap in No Scaled View");
        addChildToRoot(root, layout);

        //Imageview的主流显示方式（会自动缩放）
        layout = (LinearLayout) li.inflate(R.layout.density_image_views, null);
        ImageView imv1 = layout.findViewById(R.id.imv1);
        ImageView imv2 = layout.findViewById(R.id.imv2);
        ImageView imv3 = layout.findViewById(R.id.imv3);
        Drawable d1 = getResources().getDrawable(R.drawable.logo120, null);
        Drawable d2 = getResources().getDrawable(R.drawable.logo160, null);
        Drawable d3 = getResources().getDrawable(R.drawable.logo240, null);
        imv1.setImageDrawable(d1);
        imv2.setImageDrawable(d2);
        imv3.setImageDrawable(d3);
        addLabelToRoot(root, "6.1 Inflated layout1（xdpi）");
        addChildToRoot(root, layout);
        //Imageview的三种不同显示方式（no-dpi下的图不会缩放）
        layout = (LinearLayout) li.inflate(R.layout.density_image_views, null);
        imv1 = layout.findViewById(R.id.imv1);
        imv2 = layout.findViewById(R.id.imv2);
        imv3 = layout.findViewById(R.id.imv3);
        Log.i("TAG", "onCreate: " + "---" + d2.getIntrinsicWidth());
        imv1.setImageResource(R.drawable.n320);
        Bitmap bmp = loadAndPrintDpi(R.drawable.n480, false);
        imv2.setImageBitmap(bmp);
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + getResources().getResourcePackageName(R.drawable.n640) + "/"
                + getResources().getResourceTypeName(R.drawable.n640) + "/"
                + getResources().getResourceEntryName(R.drawable.n640));
        imv3.setImageURI(uri);
        addLabelToRoot(root, "6.2 Inflated layout2（nodpi）");
        addChildToRoot(root, layout);

        layout = (LinearLayout) li.inflate(R.layout.density_styled_image_views, null);
        addLabelToRoot(root, "6.3 Inflated styled layout");
        addChildToRoot(root, layout);

        //.9图自动缩放
        layout = new LinearLayout(this);
        addNinePatchResourceDrawable(layout, R.drawable.npatch120dpi);
        addNinePatchResourceDrawable(layout, R.drawable.npatch160dpi);
        addNinePatchResourceDrawable(layout, R.drawable.npatch240dpi);
        addLabelToRoot(root, "7.Prescaled 9-patch resource drawable");
        addChildToRoot(root, layout);

        //同一图片（s160dpi）在不同drawable文件夹下的表现
        layout = (LinearLayout) li.inflate(R.layout.density_image_views, null);
        imv1 = layout.findViewById(R.id.imv1);
        imv2 = layout.findViewById(R.id.imv2);
        imv3 = layout.findViewById(R.id.imv3);
        d1 = getResources().getDrawable(R.drawable.s160ldpi, null);
        d2 = getResources().getDrawable(R.drawable.s160mdpi, null);
        d3 = getResources().getDrawable(R.drawable.s160hdpi, null);
        imv1.setScaleType(ImageView.ScaleType.CENTER);
        imv1.setImageDrawable(d1);
        imv2.setImageDrawable(d2);
        imv3.setImageDrawable(d3);
        addLabelToRoot(root, "8.1 ldpi~mdpi~hdpi ");
        addChildToRoot(root, layout);

        layout = (LinearLayout) li.inflate(R.layout.density_image_views, null);
        imv1 = layout.findViewById(R.id.imv1);
        imv2 = layout.findViewById(R.id.imv2);
        imv3 = layout.findViewById(R.id.imv3);
        d1 = getResources().getDrawable(R.drawable.s160xhdpi, null);
        d2 = getResources().getDrawable(R.drawable.s160xxhdpi, null);
        d3 = getResources().getDrawable(R.drawable.s160xxxhdpi, null);
        imv1.setImageDrawable(d1);
        imv2.setImageDrawable(d2);
        imv3.setImageDrawable(d3);
        addLabelToRoot(root, "8.2 xhdpi~xxhdpi~xxxhdpi ");
        addChildToRoot(root, layout);

        layout = (LinearLayout) li.inflate(R.layout.density_image_views, null);
        imv1 = layout.findViewById(R.id.imv1);
        imv2 = layout.findViewById(R.id.imv2);
        imv3 = layout.findViewById(R.id.imv3);
        d1 = getResources().getDrawable(R.drawable.s160drawable, null);
        d2 = getResources().getDrawable(R.drawable.s160nodpi, null);
        d3 = getResources().getDrawable(R.drawable.s160anydpi, null);
        imv1.setImageDrawable(d1);
        imv2.setImageDrawable(d2);
        imv3.setImageDrawable(d3);
        addLabelToRoot(root, "8.3 drawable~nodpi~anydpi ");
        addChildToRoot(root, layout);
        setContentView(scrollWrap(root));
    }

    private View scrollWrap(View view) {
        ScrollView scroller = new ScrollView(this);
        scroller.addView(view, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.MATCH_PARENT));
        return scroller;
    }

    private void addLabelToRoot(LinearLayout root, String text) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextSize(15);
        label.setTextColor(Color.RED);
        root.addView(label, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    private void addChildToRoot(LinearLayout root, LinearLayout layout) {
        root.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    //resource-->bitmap(No Scale)-->drawable
    //drawable的尺寸（getIntrinsicWidth()）会按照TargetDensity进行缩放，TargetDensity缺省为设备的Density
    private void addBitmapDrawable(LinearLayout layout, int resource, boolean scale) {
        Bitmap bitmap;
        //获取原始大小图片
        bitmap = loadAndPrintDpi(resource, true);

        View view = new View(this);
        final BitmapDrawable d = new BitmapDrawable(getResources(), bitmap);

        //缺省为设备Density，若与Bitmap的Density不同则会缩放，缩放比例为BitmapDrawable的density / Bitmap的density
        if (scale) d.setTargetDensity(300);

        view.setBackground(d);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(d.getIntrinsicWidth(),
                d.getIntrinsicHeight());
        lp.leftMargin = 40;
        view.setLayoutParams(lp);
        layout.addView(view);
    }


    //resource-->drawable(是否Scale取决于resource所在的drawable文件夹)
    private void addResourceDrawable(LinearLayout layout, int resource) {
        View view = new View(this);
        final Drawable d = getResources().getDrawable(resource, null);
        view.setBackground(d);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(d.getIntrinsicWidth(),
                d.getIntrinsicHeight());
        lp.leftMargin = 40;
        view.setLayoutParams(lp);
        layout.addView(view);

    }


    //resource-->bitmap-->view（Bitmap在解码时Scale）
    private void addScaledBitmap(LinearLayout layout, int resource, boolean scale) {
        Bitmap bitmap;
        bitmap = loadAndPrintDpi(resource, true);
        ScaledBitmapView view = new ScaledBitmapView(this, bitmap, scale);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 40;
        view.setLayoutParams(lp);
        layout.addView(view);
    }

    //resource-->bitmap-->view（Bitmap在解码时不Scale）
    private void addNoScaledBitmap(LinearLayout layout, int resource, boolean scale) {
        Bitmap bitmap;
        bitmap = loadAndPrintDpi(resource, false);

        //缺省为设备Density或drawable文件夹对应的Density，若与之不同则会缩放，缩放比例为：设备的density / Bitmap的density
        if (scale) bitmap.setDensity(400);

        ScaledBitmapView view = new ScaledBitmapView(this, bitmap, scale);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 40;
        view.setLayoutParams(lp);
        layout.addView(view);
    }

    //.9图片resource-->drawable
    private void addNinePatchResourceDrawable(LinearLayout layout, int resource) {
        View view = new View(this);

        final Drawable d = getResources().getDrawable(resource, null);
        view.setBackground(d);

        Log.i("foo", "9-patch #" + Integer.toHexString(resource)
                + " w=" + d.getIntrinsicWidth() + " h=" + d.getIntrinsicHeight());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(d.getIntrinsicWidth(),
                d.getIntrinsicHeight());
        lp.leftMargin = 40;
        view.setLayoutParams(lp);
        layout.addView(view);
    }

    //resourece-->bitmap，
    private Bitmap loadAndPrintDpi(int id, boolean scale) {
        Bitmap bitmap;
        if (scale) {
            bitmap = BitmapFactory.decodeResource(getResources(), id);
        } else {
            BitmapFactory.Options opts = new BitmapFactory.Options();

            opts.inScaled = false;

            bitmap = BitmapFactory.decodeResource(getResources(), id, opts);
            Log.i("TAG", "inScreenDensity: "+opts.inScreenDensity);
            Log.i("TAG", "inDensity: "+opts.inDensity);
            Log.i("TAG", "inTargetDensity: "+opts.inTargetDensity);
        }
        return bitmap;
    }

    //bitmap-->view
    private class ScaledBitmapView extends View {
        private Bitmap mBitmap;
        private boolean isScaled;

        public ScaledBitmapView(Context context, Bitmap bitmap, boolean scale) {
            super(context);
            mBitmap = bitmap;
            isScaled = scale;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            final DisplayMetrics metrics = getResources().getDisplayMetrics();
            if (isScaled)
                setMeasuredDimension(
                        mBitmap.getScaledWidth(metrics),
                        mBitmap.getScaledHeight(metrics));
            else
                setMeasuredDimension(
                        mBitmap.getWidth(),
                        mBitmap.getHeight());
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.LTGRAY);
            canvas.drawBitmap(mBitmap, 0.0f, 0.0f, null);
        }
    }
}
