package cn.wappt.m.apptv.views.details;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import cn.wappt.m.apptv.views.details.fragment.DownloadcompletedFragment;
import cn.wappt.m.apptv.views.details.fragment.DownloadundoneFragment;

public class VideoDownloadListActivity extends AppCompatActivity {
    public static ViewPager2 mviewPager;      //创建viewPager2视图
    @BindView(R.id.download_iv_back)
    ImageView downloadIvBack;
    private TabLayout mtabLayout;        //创建选项卡
    private static final int NUM_PAGES = 2;  //有几个选项卡
    private Fragment fragmentt;
    private View view;
    private static final int MYLIVE_MODE_CHECK = 0; //检查模式
    private static final int MYLIVE_MODE_EDIT = 1; //编辑模式
    private int mEditMode = MYLIVE_MODE_CHECK;  //当前是什么模式
    private boolean isSelectAll = false;//是否全选
    private boolean editorStatus = false; //编辑状态
    @BindView(R.id.download_btn_editor)//编辑
            TextView btnEditor;
    private String[] mName = {
            "未完成", "已完成"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);

        ButterKnife.bind(this);

        initView();
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
    }

    //创建切换器viewPager2
    public void initView() {
        initdiv();
        mviewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return NUM_PAGES;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        //未完成
                        fragmentt = new DownloadundoneFragment( );
                        break;
                    case 1:
                        //已完成
                        fragmentt = new DownloadcompletedFragment();
                        break;
                }
                return fragmentt;
            }
        });
        TabLayoutMediator mediator = new TabLayoutMediator(mtabLayout, mviewPager,
                (tab, position) -> {
                    tab.setText(mName[position]);
                });
        mediator.attach();

        downloadIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上一个页面
                VideoDownloadListActivity.this.finish();
            }
        });

    }



    //初始化变量
    public void initdiv() {
        //初始化变量
        mviewPager =findViewById(R.id.download_viewPager2_column);
        mtabLayout =findViewById(R.id.download_tabLayout_column);
    }




}
