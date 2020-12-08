package cn.wappt.m.apptv.views.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.views.home.HomeFragment_top.CartoonFragment;
import cn.wappt.m.apptv.views.home.HomeFragment_top.ChoicenessFragment;
import cn.wappt.m.apptv.views.home.HomeFragment_top.FilmFragment;
import cn.wappt.m.apptv.views.home.HomeFragment_top.TeleplayFragment;
import cn.wappt.m.apptv.views.home.HomeFragment_top.VarietyFragment;
import cn.wappt.m.apptv.views.search.SearchActivity;
import cn.wappt.m.apptv.views.user.Leave;
import cn.wappt.m.apptv.views.user.view.Round_head;

public class HomeFragment extends Fragment{

    private HomeViewModel mViewMode;
    private ImageView homr_customer_feedback; //跳转用户反馈界面
    private Round_head round_head;  //头像


    public TextView searchtext;

    public static ViewPager2 mviewPager;      //创建viewPager2视图
    private TabLayout mtabLayout;        //创建选项卡
    private static final int NUM_PAGES = 5;
    private String [] mName = {"精选","电影","电视剧","综艺","动漫"};
    private Fragment fragmentt;
    private View view;
    private BottomNavigationView navigation;

    public  static  int screenWidth;
    public  static  int screenHight;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //view视图的缓存
        if (view == null) {
            view = inflater.inflate(R.layout.home_fragment, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        initView();
        //搜索
        searchtext=view.findViewById(R.id.search_text);
        searchtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了跳转到搜索
                Intent intent= new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        homr_customer_feedback=view.findViewById(R.id.homr_customer_feedback);
        homr_customer_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了跳转到用户反馈
                Intent intent =new Intent(getActivity(), Leave.class);
                startActivity(intent);
            }
        });
        navigation=getActivity().findViewById(R.id.bottomNavigationView);
        round_head=view.findViewById(R.id.homr_User_avatar);
        //底部导航视图
        round_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.setSelectedItemId(navigation.getMenu().getItem(2).getItemId());
            }
        });
        basicParamInit();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewMode = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);
    }

    /**
     * 计算屏幕的宽度
     */
    private void basicParamInit() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        screenWidth = metric.widthPixels;
        screenHight = metric.heightPixels;
    }
    //创建切换器viewPager2
    public void initView(){
        initdiv();
        mviewPager.setOffscreenPageLimit(1); //预加载

        //   mviewPager.setOffscreenPageLimit(1); //预加载
        mviewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        fragmentt = new ChoicenessFragment();
                        break;
                    case 1:
                        fragmentt = new FilmFragment();
                        break;
                    case 2:

                        fragmentt = new TeleplayFragment();

                        break;
                    case 3:

                        fragmentt = new VarietyFragment();

                        break;
                    case 4:

                        fragmentt = new CartoonFragment();

                        break;
                }
                return fragmentt;
            }

            @Override
            public int getItemCount() {
                return NUM_PAGES;
            }
        });
        TabLayoutMediator mediator = new TabLayoutMediator(mtabLayout,mviewPager,
                (tab, position)->{
                    tab.setText(mName[position]);
                });
        mediator.attach();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    //初始化变量
    public void initdiv(){
        //初始化变量
        mviewPager = view.findViewById(R.id.viewPager2);
        mtabLayout = view.findViewById(R.id.tabLayout);
    }


}