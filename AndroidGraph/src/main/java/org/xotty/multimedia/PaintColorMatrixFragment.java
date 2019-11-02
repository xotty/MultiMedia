package org.xotty.multimedia;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaintColorMatrixFragment extends Fragment {

    BottomNavigationView navigation;
    View rootView;
    public PaintColorMatrixFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(), R.style.AppTheme_NoActionBar);
        LayoutInflater localInflater = inflater
                .cloneInContext(contextThemeWrapper);
      rootView = localInflater.inflate(R.layout.fragment_paint_colormatrix,
                container, false);

     navigation = (BottomNavigationView) rootView.findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // If BottomNavigationView has more than 3 items, using reflection to disable shift mode
//        disableShiftMode(navigation);
        navigation.setSelectedItemId(0);
        navigation.setItemIconSize(1);
        CM_Matrix_Fragment fragment1 = new CM_Matrix_Fragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.container, fragment1).commit();
        return rootView;
    }

    //BottomNavigationView中Item点击事件
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_matrix:

                    CM_Matrix_Fragment fragment1 = new CM_Matrix_Fragment();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1).commit();
                    return true;
                case R.id.bottom_navigation_rgba:
                    CM_RGBA_Fragment fragment2 = new CM_RGBA_Fragment();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment2).commit();
                    return true;
                case R.id.bottom_navigation_hsl:
                    CM_HSL_Fragment fragment3 = new CM_HSL_Fragment();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment3).commit();
                    return true;
                case R.id.bottom_navigation_filter:
                    CM_Filter_Fragment fragment4 = new CM_Filter_Fragment();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment4).commit();
                    return true;
            }
            return false;
        }
    };

}
