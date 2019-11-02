/**
 * Bitmap图片像素类型Bitmap.Config包括：ALPHA_8、RGB_565、ARGB_4444、ARGB_8888四种
 * Bitmap图片创建分两大类：Bitmap.createBitmap()和BitmapFactory.decodeXXX()
 * Bitmap压缩输出格式有三种：Bitmap.CompressFormat.JPEG、Bitmap.CompressFormat.PNG、Bitmap.CompressFormat.WEBP
 */
package org.xotty.multimedia;


import android.app.ActivityManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class PaintActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViewPager();
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
        titles.add("General");
        titles.add("Color Matrix");
        titles.add("Xffermode");
        titles.add("Shadow");
        titles.add("Shader");

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new PaintGeneralFragment());
        fragments.add(new PaintColorMatrixFragment());
        fragments.add(new PaintXffermodeFragment());
        fragments.add(new PaintShadowFragment());
        fragments.add(new PaintShaderFragment());

        //定义ViewPager及其适配器
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager_main);
//        mViewPager.setOffscreenPageLimit(2);
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);

        //定义ViewPager滑动事件
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


}

