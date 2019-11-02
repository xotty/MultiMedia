/*在复杂的文本显示时会用到drawText：
  1）基本：位置、大小、样式
  2）复杂：自定义字体、围绕路径、竖向排列
  3）文本五线谱：精准定位
 */
package org.xotty.multimedia;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextDrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout root = (LinearLayout) findViewById(R.id.rt);
        TextView tv = new TextView(this);
        tv.setText("点");
        TextDrawView1 tdv1 = new TextDrawView1(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                1700);

        root.addView(tdv1, params);
        params = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                1100);
        TextDrawView2 tdv2 = new TextDrawView2(this);
        root.addView(tdv2, params);

        TextDrawView3 tdv3 = new TextDrawView3(this);
        root.addView(tdv3, params);
    }

    public class TextDrawView1 extends View {

        Context m_context;

        public TextDrawView1(Context context) {
            super(context);
            m_context = context;
        }

        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            String text = "春有百花秋有月";
            //文字绘图样式
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);//抗锯齿功能
            mPaint.setColor(Color.RED);  //设置画笔颜色
            mPaint.setStrokeWidth(5);//设置画笔宽度
            mPaint.setTextSize(70);
            //设置绘图样式为填充
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, 100, 100, mPaint);
            //设置绘图样式为描边
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawText(text, 100, 200, mPaint);
            //设置绘图样式为填充且描边
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawText(text, 100, 300, mPaint);
            
            //文字对齐
            mPaint.setColor(Color.BLUE);  
            mPaint.setStyle(Paint.Style.FILL);
            //设置对齐方式  左对齐
            mPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(text, 500, 500, mPaint);//点（500,500）在文本的左边
            //设置对齐方式  中间对齐
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, 500, 600, mPaint);//点（500,600）在文本的中间
            //设置对齐方式  右对齐
            mPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(text, 500, 700, mPaint);//点（500,700）在文本的右边
            
            //文字特殊效果
            mPaint.setColor(Color.RED);  //设置画笔颜色
            mPaint.setFakeBoldText(true);//是否粗体文字
            mPaint.setUnderlineText(true);//设置下划线
            mPaint.setStrikeThruText(true);//设置删除线效果
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, 500, 900, mPaint);
            
            //文字倾斜
            mPaint.reset();
            mPaint.setAntiAlias(true);//抗锯齿功能
            mPaint.setColor(Color.BLUE);  //设置画笔颜色
            mPaint.setStrokeWidth(5);//设置画笔宽度
            mPaint.setTextSize(70);
            //文字右倾斜
            mPaint.setTextSkewX(0.25f);
            canvas.drawText(text, 100, 1100, mPaint);
            //文字左倾斜
            mPaint.setTextSkewX(-0.5f);
            canvas.drawText(text, 100, 1200, mPaint);
            
            //文字水平拉伸
            mPaint.setTextSkewX(0f);
            mPaint.setColor(Color.RED);  //设置画笔颜色
            //水平方向拉伸2倍
            mPaint.setTextScaleX(2);
            canvas.drawText(text, 100, 1400, mPaint);
            //水平方向拉伸3倍
            mPaint.setTextScaleX(3);
            canvas.drawText(text, 100, 1500, mPaint);

        }

    }

    public class TextDrawView2 extends View {

        Context m_context;

        public TextDrawView2(Context context) {
            super(context);
            m_context = context;
        }

        //重写OnDraw（）函数，在每次重绘时自主实现绘图
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //设置画笔基本属性
            String text = "夏有凉风冬有雪";
            //设置画笔基本属性
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);//抗锯齿功能
            mPaint.setColor(Color.RED);  //设置画笔颜色
            mPaint.setStrokeWidth(5);//设置画笔宽度
            mPaint.setTextSize(70);
            mPaint.setStyle(Paint.Style.FILL);
            /*绘制文字的方法列举如下：
              1）drawText(String text, float x, float y, Paint paint)
              2）drawText(char[] text, int index, int count, float x, float y, Paint paint)
              3）drawText(CharSequence text, int start, int end, float x, float y, Paint paint)
              4）drawTextRun(char[] text, int index, int count, int contextIndex, int contextCount, float x, float y, boolean isRtl, Paint paint)
              5）drawTextRun(CharSequence text, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, Paint paint)
            */
            canvas.drawText(text.toCharArray(), 0, 4, 600, 100, mPaint);

            canvas.drawText(text, 0, 4, 600, 200, mPaint);

            //最小sdk23
            canvas.drawTextRun(text.toCharArray(), 0, 4, 0, 4, 600, 300, true, mPaint);

            canvas.drawTextRun(text.toCharArray(), 0, 4, 0, 4, 600, 400, false, mPaint);

            mPaint.setColor(Color.BLUE);  //设置画笔颜色
            float[] pos = {100, 100, 200, 200, 300, 300, 400, 400, 500, 500, 600, 600, 700, 700};
            canvas.drawPosText(text, pos, mPaint);

            Path mPath = new Path();
            mPath.addArc(300, 700, 700, 1100, 200, 150);
            mPaint.setColor(Color.RED);  //设置画笔颜色
            canvas.drawTextOnPath(text, mPath, 0, 20, mPaint);

            /*使用自定义字体
              1) createFromAsset(AssetManager mgr, String path) //Asset中获取
              2) createFromFile(File path) //文件路径获取
              3) createFromFile(String path) //外部路径获取
              定义字体样式
              1）create(String familyName, int style) //字体名
              2）create(Typeface family, int style)   //类型
              3）defaultFromStyle(int style)          //默认类型
              style是枚举类型，枚举值如下：
                 Typeface.NORMAL //正常体
                 Typeface.BOLD //粗体
                 Typeface.ITALIC //斜体
                 Typeface.BOLD_ITALIC //粗斜体
             */
            mPaint.setColor(Color.BLUE);  //设置画笔颜色
            Typeface typeface = Typeface.createFromAsset(getAssets(), "jian_luobo.ttf");
            mPaint.setTypeface(Typeface.create(typeface, Typeface.ITALIC));
            canvas.drawText(text, 270, 1000, mPaint);
        }

    }


    public class TextDrawView3 extends View {
        public TextDrawView3(Context context) {
            super(context);
        }

        //canvas.drawText()中参数y是基线y的位置；
        //mPaint.setTextAlign(Paint.Align.LEFT)时点（x,y）在文字矩形的左边
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            String text="abCDEfg行到水穷处";
            Paint mPaint = new Paint();

            /*绘制字符控制线和文本矩形
            ascentY = baselineY + fontMetric.ascent；
            descentY = baselineY + fontMetric.descent；
            topY = baselineY + fontMetric.top；
            bottomY = baselineY + fontMetric.bottom；
            获取fontMetric的方法：
            Paint.FontMetrics fm = paint.getFontMetrics();
            Paint.FontMetricsInt fmInt = paint.getFontMetricsInt();
            */
            int baseLineX = 100;
            int baseLineY = 100;
            mPaint.setTextSize(100);
            mPaint.setTextAlign(Paint.Align.LEFT);
            mPaint.setStrokeWidth(2);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();

            float top = fontMetrics.top + baseLineY;
            float ascent = fontMetrics.ascent + baseLineY;
            float descent = fontMetrics.descent + baseLineY;
            float bottom = fontMetrics.bottom + baseLineY;

            //绘制基线
            mPaint.setColor(Color.RED);
            canvas.drawLine(0, baseLineY, getWidth(), baseLineY, mPaint);

            //绘制top直线
            mPaint.setColor(Color.BLUE);
            canvas.drawLine(0, top, getWidth(), top, mPaint);

            //绘制ascent直线
            mPaint.setColor(Color.MAGENTA);
            canvas.drawLine(0, ascent, getWidth(), ascent, mPaint);

            //绘制descent直线
            mPaint.setColor(Color.MAGENTA);
            canvas.drawLine(0, descent, getWidth(), descent, mPaint);

            //绘制bottom直线
            mPaint.setColor(Color.BLUE);
            canvas.drawLine(0, bottom, getWidth(), bottom, mPaint);

            mPaint.setColor(Color.RED);
            canvas.drawText(text, 100, baseLineY, mPaint);


            /*画text所占的区域矩形
            文字宽度：width =  mPaint.measureText(text);
            文字高度：height = fm.bottom - fm.top
            * */
            Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
            int topR = baseLineY + fm.top;
            int bottomR = baseLineY + fm.bottom;
            int width = (int)mPaint.measureText(text);
            Rect rect = new Rect(baseLineX,topR,baseLineX+width,bottomR);
            mPaint.setColor(Color.GREEN);
            canvas.drawRect(rect,mPaint);

            //画最小矩形
            Rect minRect = new Rect();

            //以（0，0）为基线得到的最小矩形
            mPaint.getTextBounds(text,0,text.length(),minRect);
            //基线调整
            minRect.left = baseLineX + minRect.left;
            minRect.right = baseLineX + minRect.right;
            minRect.top = baseLineY + minRect.top;
            minRect.bottom = baseLineY + minRect.bottom;

            mPaint.setColor(Color.RED);
            canvas.drawRect(minRect,mPaint);
            //写文字
            mPaint.setColor(Color.BLACK);
            canvas.drawText(text, baseLineX, baseLineY, mPaint);


            int centerY = 300;
            //画center线
            mPaint.setColor(Color.BLUE);
            mPaint.setStrokeWidth(5);
            canvas.drawLine(0, centerY, getWidth(), centerY, mPaint);

            //计算出baseLine位置
            baseLineY = centerY + (fm.bottom - fm.top)/2 - fm.bottom;

            //画基线
            mPaint.setColor(Color.RED);
            canvas.drawLine(0, baseLineY, getWidth(), baseLineY, mPaint);

            //写文字
            mPaint.setColor(Color.BLACK);
            canvas.drawText(text, baseLineX, baseLineY, mPaint);
        }
    }
}