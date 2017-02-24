package magma.com.anshan.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import magma.com.anshan.R;
import magma.com.anshan.ab.AbOnItemClickListener;
import magma.com.anshan.ab.AbSlidingPlayView;
import magma.com.anshan.adapter.Adapter_GridView;
import magma.com.anshan.adapter.Adapter_GridView_hot;


import static magma.com.anshan.R.id.iv_shao;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment  {

    private ImageView iv_shao;
    //顶部标题栏
    private TextView tv_top_title;
    //分类的九宫格
    private GridView gridView_classify;

    //热门市场的九宫格
    private GridView my_gridView_hot;

    //首页轮播
    private AbSlidingPlayView viewPager;
    // 分类九宫格的资源文件
    private int[] pic_path_classify = { R.mipmap.menu_guide_1, R.mipmap.menu_guide_2, R.mipmap.menu_guide_3, R.mipmap.menu_guide_4, R.mipmap.menu_guide_5, R.mipmap.menu_guide_6, R.mipmap.menu_guide_7, R.mipmap.menu_guide_8 };
    // 热门市场的资源文件
    private int[] pic_path_hot = { R.mipmap.menu_1, R.mipmap.menu_2, R.mipmap.menu_3, R.mipmap.menu_4, R.mipmap.menu_5, R.mipmap.menu_6 };
    private Adapter_GridView adapter_GridView_classify;
    private Adapter_GridView_hot adapter_GridView_hot;

    /**存储首页轮播的界面*/
    private ArrayList<View> allListView;

    /**首页轮播的界面的资源*/
    private int[] resId = { R.mipmap.show_m1, R.mipmap.menu_viewpager_1, R.mipmap.menu_viewpager_2, R.mipmap.menu_viewpager_3, R.mipmap.menu_viewpager_4, R.mipmap.menu_viewpager_5 };



    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        iv_shao=(ImageView) view.findViewById(R.id.iv_shao);
        tv_top_title=(TextView) view.findViewById(R.id.tv_top_title);
        gridView_classify = (GridView) view.findViewById(R.id.my_gridview);
        my_gridView_hot = (GridView) view.findViewById(R.id.my_gridview_hot);
        gridView_classify.setSelector(new ColorDrawable(Color.TRANSPARENT));
        my_gridView_hot.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter_GridView_classify = new Adapter_GridView(getActivity(), pic_path_classify);
        adapter_GridView_hot = new Adapter_GridView_hot(getActivity(), pic_path_hot);
        gridView_classify.setAdapter(adapter_GridView_classify);
        my_gridView_hot.setAdapter(adapter_GridView_hot);
        viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
        //设置播放方式为顺序播放
        viewPager.setPlayType(1);
        //设置播放间隔时间
        viewPager.setSleepTime(3000);

        initViewPager();
    }



    private void initViewPager() {

        if (allListView != null) {
            allListView.clear();
            allListView = null;
        }
        allListView = new ArrayList<View>();

        for (int i = 0; i < resId.length; i++) {
            //导入ViewPager的布局
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.pic_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
            imageView.setImageResource(resId[i]);
            allListView.add(view);
        }


        viewPager.addViews(allListView);
        //开始轮播
        viewPager.startPlay();
/*        viewPager.setOnItemClickListener(new AbOnItemClickListener() {
            @Override
            public void onClick(int position) {
                //跳转到详情界面
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });*/
    }

}
