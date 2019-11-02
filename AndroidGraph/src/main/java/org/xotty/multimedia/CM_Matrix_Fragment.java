/**
 * 颜色矩阵的基本功能演示
 * 1）初始化和构建
 * ColorMatrix()：               单位初始矩阵
 * ColorMatrix(float[] src)      20个元素
 * ColorMatrix(ColorMatrix src)  4行5列
 * 2）应用
 * ColorMatrixColorFilter colorFilter=new ColorMatrixColorFilter(colorMatrix)
 *                                   =new ColorMatrixColorFilter(float[] array)
 * paint.setColorFilter/imageiview.setColorFilter
 */
package org.xotty.multimedia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

public class CM_Matrix_Fragment extends Fragment {
    private ImageView mImageView;
    private GridLayout mGridLayout;
    private Bitmap bitmap;
    private int mEtWidth, mEtHeight;
    private EditText[] mEditTexts = new EditText[20];
    private float[] mColorMatrix = new float[20];

    public CM_Matrix_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cm_matrix,
                container, false);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scene);
        mImageView = (ImageView) rootView.findViewById(R.id.imageview);
        mGridLayout = (GridLayout) rootView.findViewById(R.id.grid);
        mImageView.setImageBitmap(bitmap);
        mGridLayout.post(new Runnable() {
            @Override
            public void run() {
                // 获取宽高信息
                mEtWidth = mGridLayout.getWidth() / 5;
                mEtHeight = mGridLayout.getHeight() / 4;
                addEditText();
                initEditTexts();
            }
        });

        Button button1 = rootView.findViewById(R.id.bt_change);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMatrix();
                setImageMatrix();
            }
        });

        Button button2 = rootView.findViewById(R.id.bt_reset);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initEditTexts();
                getMatrix();
                setImageMatrix();
            }
        });
        return rootView;
    }

    //获取界面上的矩阵值
    private void getMatrix() {
        for (int i = 0; i < 20; i++) {
            mColorMatrix[i] = Float.valueOf(mEditTexts[i].getText().toString());
        }
    }

    //将矩阵值设置到图像
    private void setImageMatrix() {
        //用界面上的颜色值构建颜色矩阵
        ColorMatrix colorMatrix = new ColorMatrix(mColorMatrix);

        Bitmap bmp = Bitmap.createBitmap(
                bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        //将paint添加颜色矩阵过滤器
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        //用paint重新绘制图片
        canvas.drawBitmap(bitmap, 0, 0, paint);

        //将图片放到ImageView中
        mImageView.setImageBitmap(bmp);
        
        /*等效替代
        mImageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));*/
    }

    // 添加EditText
    private void addEditText() {
        for (int i = 0; i < 20; i++) {
            EditText editText = new EditText(getActivity());
            editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3), new InputFilterMinMax(0, 255)});
            editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            mEditTexts[i] = editText;
            mGridLayout.addView(editText, mEtWidth, mEtHeight);
        }
    }

    //给颜色矩阵赋初值
    private void initEditTexts() {
        for (int i = 0; i < 20; i++) {
            if (i % 6 == 0) {
                mEditTexts[i].setText(String.valueOf(1));
            } else {
                mEditTexts[i].setText(String.valueOf(0));
            }
        }
    }

    //自定义EditText Filter，限制其输入的数字在min～max之间
    private class InputFilterMinMax implements InputFilter {
        private int min, max;

        private InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        private InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }


        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;//表示不过滤输入的内容
            } catch (NumberFormatException nfe) {
            }
            return "";  //表示过滤输入的内容
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
