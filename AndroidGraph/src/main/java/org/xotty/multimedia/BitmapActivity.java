/**
 * Bitmap图片像素类型Bitmap.Config包括：ALPHA_8、RGB_565、ARGB_4444、ARGB_8888四种
 * Bitmap图片创建分两大类：Bitmap.createBitmap()和BitmapFactory.decodeXXX()
 * Bitmap压缩输出格式有三种：Bitmap.CompressFormat.JPEG、Bitmap.CompressFormat.PNG、Bitmap.CompressFormat.WEBP
 */
package org.xotty.multimedia;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BitmapActivity extends AppCompatActivity {

    static private View fragment1View;

    public void setFragment1View(View fragment1View) {
        this.fragment1View = fragment1View;
        Log.i("TAG", "setFragment1View: ");
    }

    public View getFragment1View() {
        Log.i("TAG", "getFragment1View: ");
        return fragment1View;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
//        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        int memClass = activityManager.getMemoryClass();
//        int lmemClass = activityManager.getLargeMemoryClass();
//        boolean islow = activityManager.isLowRamDevice();
//
//        Resources resources = this.getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
//        float density1 = dm.density;
//        int width3 = dm.widthPixels;
//        int height3 = dm.heightPixels;
//
//        WindowManager wm1 = this.getWindowManager();
//        int width1 = wm1.getDefaultDisplay().getWidth();
//        int height1 = wm1.getDefaultDisplay().getHeight();
        initView();
        initViewPager();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViewPager() {
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            //选中了tab的逻辑
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//            }
//
//            //未选中tab的逻辑
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            //再次选中tab的逻辑
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        List<String> titles = new ArrayList<>();
        titles.add("Create BMP");
        titles.add("Operate BMP");
        titles.add("Cache BMP");

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CreateBmpFragment());
        fragments.add(new OperateBmpFragment());
        fragments.add(new CacheBmpFragment());

        //定义ViewPager及其适配器
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager_main);
        mViewPager.setOffscreenPageLimit(2);
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);

//        //定义ViewPager滑动事件
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.i("TAG", "onPageSelected: "+position);
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.i("TAG", "onPageScrollStateChanged: "+state);
//
//            }
//        });

        mTabLayout.setupWithViewPager(mViewPager);
    }



}

