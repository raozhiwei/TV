package cn.wappt.m.apptv.views.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.itsnows.upgrade.OnDownloadListener;
import com.itsnows.upgrade.OnUpgradeListener;
import com.itsnows.upgrade.UpgradeClient;
import com.itsnows.upgrade.UpgradeConstant;
import com.itsnows.upgrade.UpgradeException;
import com.itsnows.upgrade.UpgradeManager;
import com.itsnows.upgrade.UpgradeUtil;
import com.itsnows.upgrade.model.bean.Upgrade;
import com.itsnows.upgrade.model.bean.UpgradeOptions;
import com.jeffmony.downloader.database.VideoDownloadDatabaseHelper;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.File;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.dao.ColumnDao;
import cn.wappt.m.apptv.dao.DetailsDao;
import cn.wappt.m.apptv.dao.DownloadHelperDao;
import cn.wappt.m.apptv.dao.UserDao;
import cn.wappt.m.apptv.utils.APKVersionCodeUtils;
import cn.wappt.m.apptv.utils.Constants;
import cn.wappt.m.apptv.utils.DataCleanManager;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import cn.wappt.m.apptv.views.agreement.PrivacyPolicy;
import cn.wappt.m.apptv.views.user.pop.Pop_UpMy;


/**
 * 设置
 */
public class SetUi_Main extends AppCompatActivity implements View.OnClickListener{

    private Switch switch_v;
    private TextView versions;
    private ImageView quit_remo;
    private Context context = this;
    //检测权限是否开启
    NotificationManagerCompat manager;
    //更新
    private UpgradeManager managerupdata;
    private static final String TAG = "mmp";
    private static final String DOC_XML_URL = Constants.DOC_XML_URL;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0x8052;
    BasePopupView popupView;
    private ConstraintLayout constraintLayout2;
    private Button Exit_button;
    private TextView Empty_Cache_size;

    UserDao dao;;
    //创建定时器获取基本数据
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            handler2.sendMessage(msg);
            handler2.postDelayed(this,200); //设置多久刷新一次
        }
    };

    //检测如果有值则显示清空按钮
    @SuppressLint("HandlerLeak")
    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获取当前去系统是否开启此权限
            manager =  NotificationManagerCompat.from(context);
            boolean isOpened = manager.areNotificationsEnabled();
            if (isOpened) {
                switch_v.setChecked(true);
            }else{
                switch_v.setChecked(false);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tity_main);
        handler2.postDelayed(runnable2,300);     //调用定时器
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
        dao=new UserDao(getApplication());
    }

    /**
     * 初始化数据
     */
    private void initView() {
        switch_v = findViewById(R.id.switch_v);
        versions = findViewById(R.id.versions);
        quit_remo = findViewById(R.id.quit_remo);
        Exit_button=findViewById(R.id.Exit_button);
        constraintLayout2=findViewById(R.id.constraintLayout2);
        Empty_Cache_size=findViewById(R.id.Empty_Cache_size);
        try {
            Empty_Cache_size.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        managerupdata = new UpgradeManager(this);
        //添加点击事件
        quit_remo.setOnClickListener(this);
        Exit_button.setOnClickListener(this);
        constraintLayout2.setOnClickListener(this);
        int verint = APKVersionCodeUtils.getVersionCode(this);  //获取本地当前版本
        String version = APKVersionCodeUtils.getVerName(this);    //获取用户能看到的版本
        versions.setText("当前版本:v"+version);
        message();
    }

    //清空缓存
    public void delete() throws Exception {

        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(this)
                .create();
        builder.show();
        if (builder.getWindow() == null) return;
        builder.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
        TextView msg =  builder.findViewById(R.id. browserecords_tv_msg);
        Button cancle =  builder.findViewById(R.id.browserecords_btn_cancle);
        Button sure =  builder.findViewById(R.id.browserecords_btn_sure);

        if (msg == null || cancle == null || sure == null) return;
            msg.setText("删除后不可恢复，是否删除"+DataCleanManager.getTotalCacheSize(getApplicationContext())+"?");
        //选中关闭弹窗
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        //确定删除
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.clearAllCache(getApplicationContext());
                ColumnDao columnDao=new ColumnDao(getApplication());
                DetailsDao detailsDao=new DetailsDao(getApplication());
                DownloadHelperDao downloadHelperDao=new DownloadHelperDao(getApplication());
                VideoDownloadDatabaseHelper downloadDatabaseHelper=new VideoDownloadDatabaseHelper(getApplication());
                columnDao.delect();
                detailsDao.delectData();
                downloadDatabaseHelper.deleteAllDownloadInfos();
                downloadHelperDao.delectData();

                try {
                    Empty_Cache_size.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //删除弹框
                builder.dismiss();

            }
        });


    }

    //服务协议隐私政策内容
    public void service(View view){
      /*  new XPopup.Builder(this)
                .isDestroyOnDismiss(true)    //是否在消失的时候销毁资源
                .asCustom(new Pop_Up(this))
                .show();*/

      Intent intent=new Intent(this, PrivacyPolicy.class);
      startActivity(intent);
    }

    //关于我们
    public void AboutUS(View view){
        new XPopup.Builder(this)
                .isDestroyOnDismiss(true)    //是否在消失的时候销毁资源
                .asCustom(new Pop_UpMy(this))
                .show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
          //删除弹框
        if (builder!=null){
            builder.dismiss();
        }

    }

    //消息通知
    public void message(){

        switch_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localIntent = new Intent();
                //直接跳转到应用通知设置的代码：
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0及以上
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0以上到8.0以下
                    localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    localIntent.putExtra("app_package", getPackageName());
                    localIntent.putExtra("app_uid", getApplicationInfo().uid);
                } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {//4.4
                    localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    localIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    localIntent.setData(Uri.parse("package:" + getPackageName()));
                } else {
                    //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);
                        localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                        localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                    }
                }
                startActivity(localIntent);
            }
        });

    }

    //意见反馈
    public void oponion(View view){
        //跳转
        Intent intent =new Intent(SetUi_Main.this,Leave.class);
        startActivity(intent);

    }
     androidx.appcompat.app.AlertDialog builder ;
    //注销登录
    public void quit(){
        builder = new androidx.appcompat.app.AlertDialog.Builder(SetUi_Main.this)
                .create();

        if (dao.findByColumnTypes()!=null){
            builder.show();
            if (builder.getWindow() == null) return;
            builder.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
            TextView msg =  builder.findViewById(R.id. browserecords_tv_msg);
            Button cancle =  builder.findViewById(R.id.browserecords_btn_cancle);
            Button sure =  builder.findViewById(R.id.browserecords_btn_sure);
            if (msg == null || cancle == null || sure == null) return;
            msg.setText("是否退出");
            //选中关闭弹窗
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除数据
                    SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
                        sp.edit().clear().commit();
                        dao.delect();
                        finish();
                }
            });

        }else {
                Toast.makeText(this, "没有登录,请重新登录", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View v) {
        //退出
        switch (v.getId()){
            case R.id.Exit_button:
                quit();
                break;
            case R.id.quit_remo:
                finish();
                break;
            case R.id.constraintLayout2:
                try {
                    delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    /**
     * 检测更新
     */
    public void update(View view){
        popupView =new XPopup.Builder(this)
                .asLoading("正在检测更新,请稍后")
                .show();

        managerupdata.checkForUpdates(new UpgradeOptions.Builder()
                        .setIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                        .setTitle("弘禾影视")
                        .setDescription("下载更新")
                        .setUrl(DOC_XML_URL)
                        .setStorage(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/com.hhys.apk"))
                        .setMultithreadEnabled(true)
                        .setMultithreadPools(10)
                        .setMd5(null)
                        .setAutocleanEnabled(true)
                        .setAutomountEnabled(true)
                        .build(), new OnUpgradeListener() {
                    @Override
                    public void onUpdateAvailable(UpgradeClient client) {

                    }

                    @Override
                    public void onUpdateAvailable(Upgrade.Stable stable, UpgradeClient client) {
                        showUpgradeDialog(stable, client);
                    }

                    @Override
                    public void onUpdateAvailable(Upgrade.Beta bate, UpgradeClient client) {

                    }

                    @Override
                    public void onNoUpdateAvailable(String message) {
                        Toast toast = Toast.makeText(SetUi_Main.this, "已经是最新版了", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        popupView.dismiss();
                    }
                }
        );

    }

    /**
     *  弹出提示框选择更新
     * @param stable
     * @param client
     */
    private void showUpgradeDialog(Upgrade.Stable stable, final UpgradeClient client) {
        popupView.dismiss();
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
                if (mayRequestExternalStorage(SetUi_Main.this, true)) {
                    client.start();
                }
                View toastRoot = SetUi_Main.this.getLayoutInflater().inflate(R.layout.zdybiankuan, null);
                Toast toast = new Toast(SetUi_Main.this);
                toast.setView(toastRoot);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();

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
