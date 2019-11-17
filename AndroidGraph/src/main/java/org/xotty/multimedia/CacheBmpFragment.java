/**Bitmap有两种缓存机制：内存（LruCache）和磁盘（DiskLruCache）
 * LruCache是系统自带的API，常用的Bitmap可以放在其中，以保证系统的流畅度
 * 1）初始化
 *     mMemoryCache = new LruCache<String, Bitmap>(maxCacheSize)
 * 2）操作
 *     mMemoryCache.put(key,value);
 *     mMemoryCache.get(key);
 *     mMemoryCache.remove(key);
 * DiskLruCache是第三方的API，网络获取的Bitmap可以放在其中，以使系统减少对网络的访问或离线时系统的正常显示
 * 1）引入
 *    import com.jakewharton.disklrucache.DiskLruCache
 *   （implementation 'com.jakewharton:disklrucache:2.0.2'）
 * 2）初始化
 *    DiskLruCache open(File directory, int appVersion, int valueCount, long maxSize)
 * 3）操作
 *    DiskLruCache.Editor editor = mDiskCache.edit(key);
 *    OutputStream out = editor.newOutputStream(0);
 *    out.write();
 *    editor.commit()
 *
 *    DiskLruCache.Snapshot snapshot = mDiskCache.get(key)
 *    InputStream is = snapshot.getInputStream(0);
 *
 *    mDiskCache.remove(key);
 *    mDiskCache.delete();
 *    mDiskCache.close();
 *
 */
package org.xotty.multimedia;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.collection.LruCache;
import androidx.fragment.app.Fragment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CacheBmpFragment extends Fragment {

    private static final String IMG_URL = "http://seopic.699pic.com/photo/50130/6536.jpg_wh1200.jpg";
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 100; // 100MB
    private static final int IO_BUFFER_SIZE = 1024 * 8;
    private LruCache<String, Bitmap> mMemoryCache;
    private ImageView mImageView;
    private ImageView dImageView;
    private Handler mHandler;
    private DiskLruCache mDiskCache;

    public CacheBmpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_canchebmp, container, false);
        mImageView = view.findViewById(R.id.iv1);
        dImageView = view.findViewById(R.id.iv2);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 缓存大小通常为APP可用内存的八分之一
        final int cacheSize = maxMemory / 8;
        //初始化内存缓存
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        //从LruCache中获取图片
        loadBitmapFromMemoryCache(R.raw.sample, mImageView);

        //异步获获取到网络图片并添加到磁盘缓存后立即显示出来
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    String key = hashKeyForDisk(IMG_URL);
                    Bitmap bitmap = getBitmapFromDiskCache(key);
                    if (bitmap != null) {
                        dImageView.setImageBitmap(bitmap);
                    }
                }
            }
        };
        //初始化磁盘缓存
        initDiskLruCache();
        //从DiskLruCache中获取图片
        loadBitmapFromDiskCache(IMG_URL, dImageView);

        Button bt_load = view.findViewById(R.id.bt_load);
        //从缓存中载入图片，如果缓存中没有则加入到缓存中
        bt_load.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           loadBitmapFromMemoryCache(R.raw.sample, mImageView);
                                           loadBitmapFromDiskCache(IMG_URL, dImageView);
                                       }
                                   }
        );

   
        Button bt_remove = view.findViewById(R.id.bt_remove);
        //删除缓存
        bt_remove.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             String key = hashKeyForDisk(IMG_URL);
                                             try {
                                                 mMemoryCache.remove(String.valueOf(R.raw.sample));
                                                 mDiskCache.remove(key);
                                             } catch (IOException e) {
                                                 e.printStackTrace();
                                             }
                                             mImageView.setImageResource(R.drawable.image_placeholder);
                                             dImageView.setImageResource(R.drawable.image_placeholder);
                                         }
                                     }
        );
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            //删除磁盘缓存
            mDiskCache.delete();

            //关闭磁盘缓存
            mDiskCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从内存缓存冲获取图片并显示
    private void loadBitmapFromMemoryCache(int resId, ImageView imageView) {
        //key设为图片资源的ID号
        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap = getBitmapFromMemoryCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.image_placeholder);

            //异步加载Bitmap，同时将Bitmap加入内存缓存
            BitmapWorkTask task = new BitmapWorkTask(mImageView);
            task.execute(resId);
        }
    }

    private Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }


    //异步将图片取样后放入内存缓存，然后显示出来
    class BitmapWorkTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        BitmapWorkTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // 取样图片然后将其放入缓存
        @Override
        protected Bitmap doInBackground(Integer... params) {
            final Bitmap bitmap = decodeSampledBitmapFromResource(
                    getResources(), params[0], 200, 300);
            addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }

        //获取图片后将其赋值给ImageView显示出来
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


    //取样解码原始图片
    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                   int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    /*图片加载需要考虑下列因素：
    1.估计加载整个图像所占用的内存
    2.你可以接收的内存使用量和你程序可以使用的内存
    3.你放该图片的ImageView的尺寸或者其他UI组件的尺寸
    4.屏幕的大小*/
    //长和宽每次减半，直到其中任一个小于指定长、宽即可
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    //从磁盘缓存冲获取图片并显示
    private void loadBitmapFromDiskCache(final String imgUrl, ImageView imageView) {

        final String key = hashKeyForDisk(imgUrl);
        final Bitmap bitmap = getBitmapFromDiskCache(key);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.image_placeholder);

            //从网络获取图片并放入磁盘缓存
            new Thread(new Runnable() {
                @Override
                public void run() {
                   if (writeUrlBitmapToDiskCache(imgUrl))
                       mHandler.sendEmptyMessage(0);
                   else
                       mHandler.sendEmptyMessage(1);
                }
            }).start();
        }
    }

    //SD卡可用时：/storage/emulated/0/Android/data/package_name/cache
    //SD卡不可用时：/data/data/package_name/cache
    //APP卸载后上述目录会被清空，而Environment.getExternalStorageDirectory().getAbsolutePath() + "/mDiskCache"
    //获取的路径/storage/emulated/0/mDiskCache则会永久保留
    private File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()
                ? context.getExternalCacheDir().getPath()
                : context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    //将URL通过MD5运算转为唯一的key
    private  String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private  String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    //从磁盘缓存获取图片
    private Bitmap getBitmapFromDiskCache(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
            if (snapshot != null) {
                InputStream in = snapshot.getInputStream(0);
                Bitmap bmp = BitmapFactory.decodeStream(in);
                in.close();
                return bmp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean writeUrlBitmapToDiskCache(String imgUrl){
        boolean result;
        String key = hashKeyForDisk(imgUrl);
        try {
            DiskLruCache.Editor editor = mDiskCache.edit(key);
            OutputStream out = editor.newOutputStream(0);
            if (downloadUrlToStream(imgUrl, out)) {
                //放入磁盘缓存
                editor.commit();
                result=true;
            } else {
                editor.abort();
                result=false;
            }
            mDiskCache.flush();
            out.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //建立Cache路径并初始化DiskLruCache
    private void initDiskLruCache() {
        if (mDiskCache == null || mDiskCache.isClosed()) {
            try {
                File cacheDir = getDiskCacheDir(getContext(), "CacheDir");
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                //初始化DiskLruCache
                mDiskCache = DiskLruCache.open(cacheDir, 1, 1, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //从网络下载图片
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
            }
        }
        return false;
    }
}
