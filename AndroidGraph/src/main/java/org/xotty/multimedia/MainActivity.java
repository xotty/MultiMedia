package org.xotty.multimedia;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainActivity extends LauncherActivity {

    //定义要跳转的各个Activity的名称
    String[] names = {"Density","BitmapDraw Demo", "DrawableDraw Demo", "ViewDraw Demo", "SurfaceView Demo",
            "CanvasPlot Demo", "CanvasTextDraw Demo", "CanvasTransform Demo", "Bitmap Demo", "Paint Demo",
    };

    //定义各个Activity对应的实现类
    Class<?>[] clazzs = {DensityActivity.class,BitmapDrawActivity.class, DrawableDrawActivity.class, ViewDrawActivity.class, SurfaceViewActivity.class,
            PlotActivity.class, TextDrawActivity.class, CanvasTransformActivity.class, BitmapActivity.class, PaintActivity.class,};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //定义BaseAdapter子类并重写相关方法
        final BaseAdapter adapter = new BaseAdapter() {

            //返回有多少个item
            @Override
            public int getCount() {

                return names.length;

            }

            //获取item的数据对象
            @Override
            public Object getItem(int position) {
                return names[position];
            }

            //获取item的对应索引值
            @Override
            public long getItemId(int position) {
                return position;
            }

            //获取每个item对应的布局对象
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listitem_main, parent, false);

                TextView text = (TextView) convertView.findViewById(R.id.tv);
                text.setText(names[position]);
                if (position <= 0)
                    text.setBackgroundResource(android.R.color.darker_gray);
                else if (position <= 4)
                    text.setBackgroundResource(android.R.color.holo_orange_light);
                else if (position <= 7)
                    text.setBackgroundResource(android.R.color.holo_green_light);
                else if (position <= 8)
                    text.setBackgroundResource(android.R.color.holo_blue_light);
                else
                    text.setBackgroundResource(android.R.color.holo_purple);

                return convertView;
            }
        };
        setListAdapter(adapter);
    }

    //将clazzs数组直接放入，系统将按顺序对应listview上的每一行，行点击后将跳转相应Intent的Activity
    @Override
    public Intent intentForPosition(int position) {
        return new Intent(MainActivity.this, clazzs[position]);
    }
}
