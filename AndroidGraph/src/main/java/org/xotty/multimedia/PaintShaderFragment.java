package org.xotty.multimedia;


import android.content.Context;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaintShaderFragment extends Fragment {

    BottomNavigationView navigation;
    private View rootView;
    public PaintShaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(), R.style.AppTheme_NoActionBar);
        LayoutInflater localInflater = inflater
                .cloneInContext(contextThemeWrapper);
        rootView = localInflater.inflate(R.layout.fragment_paint_shader,
                container, false);


        rootView.findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shader_Bitmap_Fragment fragment1 = new Shader_Bitmap_Fragment();
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment1).commit();
            }
        });
        rootView.findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shader_LinearGradient_Fragment fragment2 = new Shader_LinearGradient_Fragment();
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment2).commit();

            }
        });
        rootView.findViewById(R.id.bt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shader_RedialGradient_Fragment fragment3 = new Shader_RedialGradient_Fragment();
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment3).commit();
            }
        });

        rootView.findViewById(R.id.bt4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shader_SweepGradient_Fragment fragment1 = new Shader_SweepGradient_Fragment();
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment1).commit();
            }
        });
        rootView.findViewById(R.id.bt5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shader_ComposeGradient_Fragment fragment1 = new Shader_ComposeGradient_Fragment();
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment1).commit();
            }
        });


        Shader_Bitmap_Fragment fragment1 = new Shader_Bitmap_Fragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.container, fragment1).commit();
        return rootView;
    }


}
