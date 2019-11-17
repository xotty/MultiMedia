/**调整颜色矩阵对角线R/G/B/A的值来过滤图片
 * setScale(float rScale, float gScale, float bScale,float aScale)
 */
package org.xotty.multimedia;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class CF_RGBA_Fragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private ImageView imageView;
    private SeekBar seekBarR;
    private SeekBar seekBarG;
    private SeekBar seekBarB;
    private SeekBar seekBarA;
    private ColorMatrix colorMatrix;
    private View colorView;
    private TextView colorText;

    public CF_RGBA_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cf_rgba,
                container, false);
        colorMatrix = new ColorMatrix();

        imageView = rootView.findViewById(R.id.imageView);
        seekBarR = rootView.findViewById(R.id.bar_R);
        seekBarG = rootView.findViewById(R.id.bar_G);
        seekBarB = rootView.findViewById(R.id.bar_B);
        seekBarA = rootView.findViewById(R.id.bar_A);
        colorView = rootView.findViewById(R.id.color_view);
        colorText = rootView.findViewById(R.id.color_text);

        seekBarR.setOnSeekBarChangeListener(this);
        seekBarG.setOnSeekBarChangeListener(this);
        seekBarB.setOnSeekBarChangeListener(this);
        seekBarA.setOnSeekBarChangeListener(this);

        return rootView;
    }

    //以128为标准计算缩放比例
    private float caculateScale(int progress) {
        float scale = progress / 128f;
        return scale;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //根据进度条的值对颜色进行缩放，设中间值128时缩放比例为1
        colorMatrix.setScale(caculateScale(seekBarR.getProgress()), caculateScale(seekBarG.getProgress()),
                caculateScale(seekBarB.getProgress()), caculateScale(seekBarA.getProgress()));

        //获取和显示颜色值（16进制）
        colorText.setText("颜色值：#" + Integer.toHexString(seekBarA.getProgress())
                + Integer.toHexString(seekBarR.getProgress())
                + Integer.toHexString(seekBarG.getProgress())
                + Integer.toHexString(seekBarB.getProgress()));
        
        //绘制颜色图块
        colorView.setBackgroundColor(Color.argb(seekBarA.getProgress(),
                seekBarR.getProgress(),
                seekBarG.getProgress(),
                seekBarB.getProgress()));
        
        //将缩放后的颜色矩阵应用于图片
        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
