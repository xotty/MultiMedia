package org.xotty.multimedia;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaintColorFilterFragment extends Fragment {

    public PaintColorFilterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(), R.style.AppTheme_NoActionBar);
        LayoutInflater localInflater = inflater
                .cloneInContext(contextThemeWrapper);
        View  rootView = localInflater.inflate(R.layout.fragment_paint_colorfilter,
                container, false);

        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        Log.i("TAG", "BottomNavigationView: "+navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.inflateMenu(R.menu.menu_paint_colorfilter);

        navigation.setSelectedItemId(0);
        navigation.setItemIconSize(1);
        CF_Matrix_Fragment fragment1 = new CF_Matrix_Fragment();
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

                    CF_Matrix_Fragment fragment1 = new CF_Matrix_Fragment();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1).commit();
                    return true;
                case R.id.bottom_navigation_rgba:
                    CF_RGBA_Fragment fragment2 = new CF_RGBA_Fragment();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment2).commit();
                    return true;
                case R.id.bottom_navigation_hsl:
                    CF_HSL_Fragment fragment3 = new CF_HSL_Fragment();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment3).commit();
                    return true;
                case R.id.bottom_navigation_filter:
                    CF_Filter_Fragment fragment4 = new CF_Filter_Fragment();
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment4).commit();
                    return true;
            }
            return false;
        }
    };

}
