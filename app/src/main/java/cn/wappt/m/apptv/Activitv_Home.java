package cn.wappt.m.apptv;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.itsnows.upgrade.OnDownloadListener;
import com.itsnows.upgrade.OnUpgradeListener;
import com.itsnows.upgrade.UpgradeClient;
import com.itsnows.upgrade.UpgradeConstant;
import com.itsnows.upgrade.UpgradeException;
import com.itsnows.upgrade.UpgradeManager;
import com.itsnows.upgrade.UpgradeUtil;
import com.itsnows.upgrade.model.bean.Upgrade;
import com.itsnows.upgrade.model.bean.UpgradeOptions;

import java.io.File;

import cn.wappt.m.apptv.utils.Constants;
import cn.wappt.m.apptv.utils.PermisionUtils;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import cn.wappt.m.apptv.views.home.ColumnFragment;
import cn.wappt.m.apptv.views.home.HomeFragment;
import cn.wappt.m.apptv.views.home.MyFragment;

public class Activitv_Home extends AppCompatActivity {

    private UpgradeManager manager;
    private static final String TAG = "mmp";
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0x8052;
    public static ViewPager2 mviewPager;      //创建viewPager2视图
    private TabLayout mtabLayout;        //创建选项卡
    private Fragment fragmentt;
    private static final int NUM_PAGES = 3;  //有几个选项卡
    public BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitv__home);
        //底部导航视图
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        mviewPager=findViewById(R.id.fragment_viewPager2);
        initView();

       //更新操作
        if (manager==null){
            manager = new UpgradeManager(this);
            customerCheckUpdates();
        }
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
        PermisionUtils.verifyStoragePermissions(Activitv_Home.this); //进行判断是否有权限
    }




    //创建切换器viewPager2
    public void initView(){

        //启用或禁用用户滑动
        mviewPager.setUserInputEnabled(false);
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
                        fragmentt = new HomeFragment();
                        break;
                    case 1:
                        fragmentt = new ColumnFragment();

                        break;
                    case 2:
                        fragmentt = new MyFragment();
                        break;
                }
                return fragmentt;
            }
        });


        //导航栏点击事件
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeFragment_Main:
                        mviewPager.setCurrentItem(0);
                        return true;
                    case R.id.columnFragment_Main:
                        mviewPager.setCurrentItem(1);
                        return true;
                    case R.id.MyFragment_Main:
                        mviewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
    }



    @Override
    public void onResume() {
                    int id = getIntent().getIntExtra("id", 0);
                    if (id == 1) {
                        Intent i = new Intent();
                        i.setClass(Activitv_Home.this, MyFragment.class);
                    }
                    super.onResume();
                }
    /**
     * 检测更新
     */
    public void customerCheckUpdates(){
        manager.checkForUpdates(new UpgradeOptions.Builder()
                        .setIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                        .setTitle("弘禾影视")
                        .setDescription("下载更新")
                        .setUrl(Constants.DOC_XML_URL)
                        .setStorage(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/com.hhys.apk"))
                        .setMultithreadEnabled(true)
                        .setMultithreadPools(1)
                        .setMd5(null)
                        .setAutocleanEnabled(true)
                        .setAutomountEnabled(true)
                        .build(), new OnUpgradeListener() {

                    @Override
                    public void onUpdateAvailable(UpgradeClient client) {

                    }

                    @Override  //更新可以使用
                    public void onUpdateAvailable(Upgrade.Stable stable, UpgradeClient client) {
                        showUpgradeDialog(stable, client);
                    }

                    @Override
                    public void onUpdateAvailable(Upgrade.Beta bate, UpgradeClient client) {

                    }

                    @Override
                    public void onNoUpdateAvailable(String message) {
                        Toast.makeText(Activitv_Home.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }


    /**
     *  弹出提示框选择更新
     * @param stable
     * @param client
     */
    private void showUpgradeDialog(Upgrade.Stable stable,final UpgradeClient client) {

        StringBuffer logs = new StringBuffer();
        for (int i = 0; i < stable.getLogs().size(); i++) {
            logs.append(stable.getLogs().get(i));
            logs.append(i < stable.getLogs().size() - 1 ? "\n" : "");
        }

        View view = View.inflate(this, R.layout.dialog, null);
        TextView tvMessage = view.findViewById(R.id.tv_dialog_custom_message);
        final Button btnUpgrade = view.findViewById(R.id.btn_dialog_custom_upgrade);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                client.remove();
            }
        });

        tvMessage.setText(logs.toString());
        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 开始下载
                if (mayRequestExternalStorage(Activitv_Home.this, true)) {
                    client.start();
                    Toast.makeText(Activitv_Home.this, "开始更新", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        client.setOnDownloadListener(new OnDownloadListener() {

            @Override
            public void onStart() {
                super.onStart();
                Log.d(TAG, "onStart");
            }

            @Override
            public void onProgress(long max, long progress) {
                Log.d(TAG, "onProgress：" + UpgradeUtil.formatByte(progress) + "/" + UpgradeUtil.formatByte(max));
            }

            @Override
            public void onPause() {
                super.onPause();
                Log.d(TAG, "onPause");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(UpgradeException e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                btnUpgrade.setTag(UpgradeConstant.MSG_KEY_DOWNLOAD_COMPLETE_REQ);
                Log.d(TAG, "onComplete");
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                client.remove();
            }
        });
        dialog.show();
    }
    /**
     * 判断申请外部存储所需权限
     *
     * @param context
     * @param isActivate
     * @return
     */
    public boolean mayRequestExternalStorage(Context context, boolean isActivate) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (isActivate) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }

        return false;
    }


}
