package cn.wappt.m.apptv.views.home;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.views.home.ColumnFragment_top.CartoonLeaderboardFragment;
import cn.wappt.m.apptv.views.home.ColumnFragment_top.FilmLeaderboardFragment;
import cn.wappt.m.apptv.views.home.ColumnFragment_top.TeleplayLeaderboardFragment;
import cn.wappt.m.apptv.views.home.ColumnFragment_top.VarietyLeaderboardFragment;


public class ColumnFragment extends Fragment {

    private ColumnViewModel mViewModel;
    public static ViewPager2 mviewPager;      //创建viewPager2视图
    private TabLayout mtabLayout;        //创建选项卡
    private static final int NUM_PAGES = 4;  //有几个选项卡
    private Fragment fragmentt;
    private View view;

    private ConstraintLayout constraintLayout;

    public static int screenWidth;
    public static int screenHeight;

    private  String [] mName={
      "电影","连续剧","综艺","动漫"
    };

    /**
     * 计算屏幕的宽度
     */
    private void basicParamInit() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        ViewTreeObserver vto = constraintLayout.getViewTreeObserver();
        screenWidth=metric.widthPixels;
        screenHeight=metric.heightPixels;
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                constraintLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                constraintLayout.getHeight();
                constraintLayout.getWidth();
                screenHeight=screenHeight-constraintLayout.getHeight();
                screenWidth=constraintLayout.getWidth();
                System.out.println("constraintLayout"+screenHeight);
                System.out.println("constraintLayout"+screenWidth);
            }
        });
    }

    public static ColumnFragment newInstance() {
        return new ColumnFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //view视图的缓存
        if (view == null) {
            view = inflater.inflate(R.layout.column_fragment, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();


        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //实例化视图
        mViewModel = ViewModelProviders.of(this).get(ColumnViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     }

    //创建切换器viewPager2
    public void initView(){
        initdiv();
        mviewPager.setOffscreenPageLimit(1); //预加载
        mviewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return NUM_PAGES;
            }
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        fragmentt = new FilmLeaderboardFragment();
                        break;
                    case 1:
                        fragmentt = new TeleplayLeaderboardFragment();
                        break;
                    case 2:
                        fragmentt = new VarietyLeaderboardFragment();
                        break;
                    case 3:
                        fragmentt = new CartoonLeaderboardFragment();
                        break;
                }
                return fragmentt;
            }
        });
        TabLayoutMediator mediator = new TabLayoutMediator(mtabLayout,mviewPager,
                (tab, position)->{
                    tab.setText(mName[position]);
                });
        mediator.attach();

    }
    //初始化变量
    public void initdiv(){
        //初始化变量
        mviewPager = view.findViewById(R.id.viewPager2_column);
        mtabLayout = view.findViewById(R.id.columnfragment_tablayout_column);
        constraintLayout=view.findViewById(R.id.ConstraintLayout_coumn);
        basicParamInit();
    }


}
