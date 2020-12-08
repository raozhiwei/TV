package cn.wappt.m.apptv.views.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.UserInformationBean;
import cn.wappt.m.apptv.dao.UserDao;
import cn.wappt.m.apptv.utils.StatusBarUtil;


/**
 * @author: wsq
 * @date: 2020/9/3
 * Description:个人资料
 */
public class profieActivty extends Activity {



    @BindView(R.id.infoReturn)
    ImageView infoReturn;
    @BindView(R.id.info_username)
    TextView infoUsername;
    @BindView(R.id.info_member)
    TextView infoMember;
    @BindView(R.id.info_integral)
    TextView infoIntegral;
    @BindView(R.id.info_member_points)
    TextView infoMemberPoints;
    @BindView(R.id.info_QQmark)
    TextView infoQQmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profie_main);
        ButterKnife.bind(this);
        infoReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                profieActivty.this.finish();
            }
        });
        updateUI();
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

    private void updateUI() {
        UserDao dao=new UserDao(getApplicationContext());
        UserInformationBean userInformationBean=dao.findByColumnTypes();
        if (userInformationBean!=null){
            infoUsername.setText(userInformationBean.getUsername());
            infoMember.setText(userInformationBean.getMembergroup());
            infoIntegral.setText(userInformationBean.getAccountpoints());
            infoMemberPoints.setText(userInformationBean.getMembershipperiod());
            infoQQmark.setText(userInformationBean.getQqnumber());
        }

    }


}
