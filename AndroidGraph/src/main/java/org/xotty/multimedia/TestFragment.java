/**
 * 第一个Fragment，其生命周期全过程将会被完整显示出来
 * 1）onAttach()，创建时会执行
 * 2）onCreate()，创建时会执行
 * 3）onCreateView()，创建时会执行
 * 4）onActivityCreated()，创建时会执行
 * 5）onStart()，变得可见时会执行
 * 6）onResume()，变得可见时会执行
 * 7）onPause()，进入后台会执行，销毁时会执行
 * 8) onStop()，进入后台会执行，销毁时会执行
 * 9) onDestroyView()，销毁时会执行
 * 10)onDestroy()，销毁时会执行
 * 11)onDetach()，销毁时会执行
 * <p>
 * <br/>Copyright (C), 2017-2018, Steve Chang
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:FragmentLifecycle
 * <br/>Date:Aug，2017
 * @author xottys@163.com
 * @version 1.0
 */
package org.xotty.multimedia;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFragment extends Fragment {
    private static final String TAG = "FragmentLifecycle";

    //Activity与Fragment绑定
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(TAG, "----onAttach----");
    }

    //Fragment被创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "----onCreate----");
    }

    //Fragment视图被创建,绑定UI视图时,该方法被回调
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle data) {
        Log.d(TAG, "----onCreateView----");

        //这里定义Fragment要显示的内容
        TextView tv = new TextView(getActivity());
        tv.setText("1");
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(90);
        return tv;
    }
    //当宿主 Activity 的 onCreate() 方法返回后，该方法被回调
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "----onActivityCreated----");
    }

    //当Fragment启动时,该方法被回调
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "----onStart----");
    }

    //当Fragment恢复时,该方法被回调
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "----onResume----");
    }

    //当Fragment暂停时,该方法被回调
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "----onPause----");
    }

    //当Fragment停止时,该方法被回调
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "----onStop----");
    }

    //当与Fragment绑定的UI视图被移除时，该方法被回调
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "----onDestroyView----");
    }

    //当Fragment销毁时,该方法被回调
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "----onDestroy----");
    }

    //当Fragment与Activity解除绑定时,该方法被回调
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "----onDetach----");
    }
}
