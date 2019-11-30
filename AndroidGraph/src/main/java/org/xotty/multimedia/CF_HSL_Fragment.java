/**对色相（Hue）、饱和度（Saturation）、亮度（Lightness）进行综合设置
 * 色相（Hue）：setRotate
 * 饱和度（Saturation）：setSaturation
 * 亮度（Lightness）：setScale
 * 对比度（Contrast）设置ColorMatrix
 * 效果叠加：postConcat
 */
package org.xotty.multimedia;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

public class CF_HSL_Fragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private ImageView imageView;
    private SeekBar seekBarHue;
    private SeekBar seekBarSaturation;
    private SeekBar seekBarLightness;
    private SeekBar seekBarContrast;
    private ColorMatrix colorMatrix = new ColorMatrix();
    private ColorMatrix mHueMatrix = new ColorMatrix();
    private ColorMatrix mSaturationMatrix = new ColorMatrix();
    private ColorMatrix mLightnessMatrix = new ColorMatrix();
    private ColorMatrix mContrastMatrix = new ColorMatrix();

    public CF_HSL_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cf_hsl,
                container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        seekBarHue = (SeekBar) rootView.findViewById(R.id.bar_hue);
        seekBarSaturation = (SeekBar) rootView.findViewById(R.id.bar_saturation);
        seekBarLightness = (SeekBar) rootView.findViewById(R.id.bar_lightness);
        seekBarContrast = (SeekBar) rootView.findViewById(R.id.bar_contrast);

        seekBarHue.setOnSeekBarChangeListener(this);
        seekBarSaturation.setOnSeekBarChangeListener(this);
        seekBarLightness.setOnSeekBarChangeListener(this);
        seekBarContrast.setOnSeekBarChangeListener(this);
        return rootView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        //根据进度条取值设置色相（-180～180）、饱和度（0～2）、亮度（0～2）的值，其中128是标准起点值
        float mHueValue = (seekBarHue.getProgress() - 128f) * 1.0f / 128f * 180;
        float mSaturationValue = seekBarSaturation.getProgress() / 128f;
        float mLightnessValue = seekBarLightness.getProgress() / 128f;
        float mContrastValue = seekBarContrast.getProgress() / 128f-1.0f;

        //设置色相
        mHueMatrix.reset();
        mHueMatrix.setRotate(0, mHueValue);
        mHueMatrix.setRotate(1, mHueValue);
        mHueMatrix.setRotate(2, mHueValue);

        //设置饱和度
        mSaturationMatrix.reset();
        mSaturationMatrix.setSaturation(mSaturationValue);

        //设置亮度
        mLightnessMatrix.reset();
        mLightnessMatrix.setScale(mLightnessValue, mLightnessValue, mLightnessValue, 1);


        //设置对比度
        mContrastMatrix.reset();
        setContrast(mContrastMatrix,mContrastValue);

        //效果叠加
        colorMatrix.reset();
        colorMatrix.postConcat(mHueMatrix);
        colorMatrix.postConcat(mSaturationMatrix);
        colorMatrix.postConcat(mLightnessMatrix);
        colorMatrix.postConcat(mContrastMatrix);

        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //设置对比度
    private  void setContrast(ColorMatrix cm, float contrast) {
        float scale = contrast + 1.f;
        float translate = (-.5f * scale + .5f) * 255.f;
        cm.set(new float[] {
                scale, 0, 0, 0, translate,
                0, scale, 0, 0, translate,
                0, 0, scale, 0, translate,
                0, 0, 0, 1, 0 });
    }
}
