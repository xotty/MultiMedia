/**
 * Paint作为画图的基本工具，具有丰富的特性和效果：
 * 1）线帽、连接方式、路径
 * 2）颜色过滤（ColorFilter）
 * 3）颜色混合（Xfermode）
 * 4）阴影（Shadow）
 * 5）着色（Shader）
 */
package org.xotty.multimedia;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PaintActivity extends AppCompatActivity {
    boolean isFistVisible=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViewPager();
    }

    private void initViewPager() {
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //选中了tab的逻辑
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 1)
                        navigation.setVisibility(View.VISIBLE);
                    else
                        navigation.setVisibility(View.INVISIBLE);
            }

            //未选中tab的逻辑
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            //再次选中tab的逻辑
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        List<String> titles = new ArrayList<>();
        titles.add("General");
        titles.add("ColorFilter");
        titles.add("Xfermode");
        titles.add("Shadow");
        titles.add("Shader");

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new PaintGeneralFragment());
        fragments.add(new PaintColorMatrixFragment());
        fragments.add(new PaintXffermodeFragment());
        fragments.add(new PaintShadowFragment());
        fragments.add(new PaintShaderFragment());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager_main);
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(0);

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

