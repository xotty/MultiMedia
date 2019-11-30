/**
 * 演示各种颜色过滤器（ColorFilter）的使用及其效果：
 * 1)ColorMatrixColorFilter:自定义效果
 * 2)LightingColorFilter：光照颜色过滤器
 * 3)PorterDuffColorFilter：颜色混合滤镜
 */
package org.xotty.multimedia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CF_Filter_Fragment extends Fragment {
    private static final int ColorMatrixColorFilterNumber = 29;
    private static final int LightenFilterNumber = 2;
    private static final int PorterDuffFilterNumber = 18;
    private RecyclerView recyclerView;
    private FiltersAdapter filtersAdapter;
    //存放各种滤镜
    private List filters = new ArrayList<>();

    //存放对应的滤镜名称
    private List<String> filterTags = new ArrayList<>();

    public CF_Filter_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cf_filter,
                container, false);
        recyclerView = rootView.findViewById(R.id.mRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        inItFilters();
        filtersAdapter = new FiltersAdapter(getLayoutInflater(), filters, filterTags);
        recyclerView.setAdapter(filtersAdapter);

        return rootView;
    }

    //添加各种滤镜
    private void inItFilters() {
        int filterColor = 0x77E50961;
        filters.add(new ColorMatrix().getArray());
        filterTags.add("原图");
        filters.add(MyColorFilter.colormatrix_heibai);

        filterTags.add("黑白1");
        filters.add(MyColorFilter.colormatrix_heibai_2);
        filterTags.add("黑白2");
        filters.add(MyColorFilter.colormatrix_heibai_3);
        filterTags.add("黑白3");
        filters.add(MyColorFilter.colormatrix_huidu);
        filterTags.add("灰度");
        filters.add(MyColorFilter.colormatrix_hlfs);
        filterTags.add("红绿反色");
        filters.add(MyColorFilter.colormatrix_buse);
        filterTags.add("补色");
        filters.add(MyColorFilter.colormatrix_zuojiu1);
        filterTags.add("做旧1");
        filters.add(MyColorFilter.colormatrix_zuojiu2);
        filterTags.add("做旧2");
        filters.add(MyColorFilter.colormatrix_quse);
        filterTags.add("去色");
        filters.add(MyColorFilter.colormatrix_gbh);
        filterTags.add("高饱和");
        filters.add(MyColorFilter.colormatrix_fugu);
        filterTags.add("复古");
        filters.add(MyColorFilter.colormatrix_gete);
        filterTags.add("哥特");
        filters.add(MyColorFilter.colormatrix_chuan_tong);
        filterTags.add("传统");
        filters.add(MyColorFilter.colormatrix_danya);
        filterTags.add("淡雅");
        filters.add(MyColorFilter.colormatrix_guangyun);
        filterTags.add("光晕");
        filters.add(MyColorFilter.colormatrix_fanse);
        filterTags.add("胶片1");
        filters.add(MyColorFilter.colormatrix_hepian);
        filterTags.add("褐片");
        filters.add(MyColorFilter.colormatrix_huajiu);
        filterTags.add("怀旧");
        filters.add(MyColorFilter.colormatrix_jiao_pian);
        filterTags.add("胶片2");
        filters.add(MyColorFilter.colormatrix_landiao);
        filterTags.add("蓝调");
        filters.add(MyColorFilter.colormatrix_langman);
        filterTags.add("浪漫");
        filters.add(MyColorFilter.colormatrix_ruise);
        filterTags.add("锐色");
        filters.add(MyColorFilter.colormatrix_menghuan);
        filterTags.add("梦幻");
        filters.add(MyColorFilter.colormatrix_qingning);
        filterTags.add("青柠");
        filters.add(MyColorFilter.colormatrix_yese);
        filterTags.add("夜色");
        filters.add(MyColorFilter.colormatrix_jiuhong);
        filterTags.add("酒红");
        filters.add(MyColorFilter.colormatrix_fanhuang);
        filterTags.add("泛黄");
        filters.add(MyColorFilter.colormatrix_huguang);
        filterTags.add("湖光");

        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.CLEAR));
        filterTags.add("");

        //mul：0xRRGGBB，其中00为去色，ff为原样不变
        //add：0xRRGGBB，其中00为原样不变，其它为加深颜色
        filters.add(new LightingColorFilter(0x00ff00, 0x000000));
        filterTags.add("单显绿色");
        filters.add(new LightingColorFilter(0xffffff, 0x0000f0));
        filterTags.add("蓝色加深");

        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.CLEAR));
        filterTags.add("");

        //filterColor作为SRC，imageView作为DST，进行混色
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.ADD));
        filterTags.add("Mode.ADD");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.MULTIPLY));
        filterTags.add("Mode.MULTIPLY");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.DARKEN));
        filterTags.add("Mode.DARKEN");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.LIGHTEN));
        filterTags.add("Mode.LIGHTEN");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.SCREEN));
        filterTags.add("Mode.SCREEN");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.OVERLAY));
        filterTags.add("Mode.OVERLAY");

        //呈现SRC为主的图像
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.SRC));
        filterTags.add("Mode.SRC");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.SRC_IN));
        filterTags.add("Mode.SRC_IN");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.SRC_OUT));
        filterTags.add("Mode.SRC_OUT");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.SRC_OVER));
        filterTags.add("Mode.S_OVER");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.SRC_ATOP));
        filterTags.add("Mode.S_ATOP");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.CLEAR));
        filterTags.add("Mode.CLEAR");
        //呈现DST为主的图像
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.DST));
        filterTags.add("Mode.DST");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.DST_IN));
        filterTags.add("Mode.DST_IN");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.DST_OUT));
        filterTags.add("Mode.DST_OUT");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.DST_OVER));
        filterTags.add("Mode.D_OVER");
        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.DST_ATOP));
        filterTags.add("Mode.D_ATOP");

        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.XOR));
        filterTags.add("Mode.XOR");

        filters.add(new PorterDuffColorFilter(filterColor, PorterDuff.Mode.CLEAR));
        filterTags.add("");

    }


    class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.MyViewHolder> {
        private LayoutInflater mInflater;
        private List filters;
        private List<String> filterTags;

        FiltersAdapter(LayoutInflater mInflater, List filters, List<String> filterTags) {
            this.mInflater = mInflater;
            this.filters = filters;
            this.filterTags = filterTags;
        }

        //定义RecycleView的格式
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder viewHolder;
            viewHolder = new MyViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
            return viewHolder;
        }

        //定义RecycleView的内容个数
        @Override
        public int getItemCount() {
            return filters.size();
        }

        //定义RecycleView的内容
        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.image_placeholder);

            //对ImageView施加滤镜
            if (position < ColorMatrixColorFilterNumber) {
                MyColorFilter.imageViewColorFilter(holder.imageView, (float[]) filters.get(position));
            } else  {
                MyColorFilter.imageViewColorFilter(holder.imageView, (ColorFilter) filters.get(position));
            }
            holder.textView.setText(filterTags.get(position));

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            MyViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.img);
                textView = view.findViewById(R.id.tv);
            }
        }
    }
}
