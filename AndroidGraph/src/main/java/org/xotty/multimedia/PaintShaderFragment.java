/**
 * Shader作为Paint的基本特性，实现各种图形渲染效果：
 * 1）BitmapShader
 * 2）LinearGradient
 * 3）RedialGradient
 * 4）SweepGradient
 * 5）ComposeGradient
 */

package org.xotty.multimedia;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

public class PaintShaderFragment extends Fragment {

    public PaintShaderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(), R.style.AppTheme_NoActionBar);
        LayoutInflater localInflater = inflater
                .cloneInContext(contextThemeWrapper);
        View rootView = localInflater.inflate(R.layout.fragment_paint_shader,
                container, false);
        //根据屏幕宽度计算合适的单控件宽度
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        final int rectWidth = dm.widthPixels / 2 - 50;

        //BitmapShader演示
        rootView.findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shader_Bitmap_Fragment fragment1 = new Shader_Bitmap_Fragment();
                fragment1.setRectWidth(rectWidth);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment1).commit();
            }
        });
        //LinearGradient演示
        rootView.findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shader_LinearGradient_Fragment fragment2 = new Shader_LinearGradient_Fragment();
                fragment2.setRectWidth(rectWidth);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment2).commit();

            }
        });
        //RedialGradient演示
        rootView.findViewById(R.id.bt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shader_RedialGradient_Fragment fragment3 = new Shader_RedialGradient_Fragment();
                fragment3.setRectWidth(rectWidth);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment3).commit();
            }
        });
        //SweepGradient演示
        rootView.findViewById(R.id.bt4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shader_SweepGradient_Fragment fragment4 = new Shader_SweepGradient_Fragment();
                fragment4.setViewWidth(rectWidth);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment4).commit();
            }
        });
        //ComposeGradien演示
        rootView.findViewById(R.id.bt5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shader_ComposeGradient_Fragment fragment5 = new Shader_ComposeGradient_Fragment();
                fragment5.setViewWidth(rectWidth);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment5).commit();
            }
        });

        //立即加载第一个BitmapShader
        Shader_Bitmap_Fragment fragment1 = new Shader_Bitmap_Fragment();
        fragment1.setRectWidth(rectWidth);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.container, fragment1).commit();

        return rootView;
    }
}
