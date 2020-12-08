package cn.wappt.m.apptv.views.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.LoginReceive;
import cn.wappt.m.apptv.interfaces.RegissterInterfaces;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//注册
public class RegisterActivty extends Activity {

    @BindView(R.id.registered_return)
    ImageView registeredReturn;
    @BindView(R.id.registered_user_name)
    EditText registered_user_name;
    @BindView(R.id.registered_user_passwork_zc)
    EditText registeredUserPassworkZc;
    @BindView(R.id.registered_user_passworks_zc)
    EditText registeredUserPassworksZc;
    @BindView(R.id.registered_button)
    Button registeredButton;

    private String userName, psw, pwss;//获取的用户名，密码,确定密码

    private Bitmap bitmap;//验证码对象
    private String code; //图片验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        ButterKnife.bind(this);

        init();
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

    //获取界面控件
    private void init() {
        registeredReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                RegisterActivty.this.finish();
            }
        });

        //注册按钮的点击事件
        registeredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始登录，获取用户名和密码 getText().toString().trim();
                userName = registered_user_name.getText().toString().trim();
                psw = registeredUserPassworkZc.getText().toString().trim();
                pwss = registeredUserPassworksZc.getText().toString().trim();
                // TextUtils.isEmpty
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegisterActivty.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(RegisterActivty.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (!(userName.length() > 5)) {
                    Toast.makeText(RegisterActivty.this, "用户名长度小于6位数，请重新输入", Toast.LENGTH_SHORT).show();
                } else if (!(psw.length() > 5)) {
                    Toast.makeText(RegisterActivty.this, "密码长度小于6位数，请重新输入", Toast.LENGTH_SHORT).show();
                } else if ((psw != null && !TextUtils.isEmpty(psw) && !psw.equals(pwss))) {
                    Toast.makeText(RegisterActivty.this, "输入的密码不一致", Toast.LENGTH_SHORT).show();
                } else  {
                    Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
                    RegissterInterfaces request = retrofit.create(RegissterInterfaces.class);
                    //对 发送请求数据进行封装
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("user_name", userName);
                    map.put("user_pwd", psw);
                    map.put("user_pwd2", pwss);
                    Call<LoginReceive> call = request.Regist(map);
                    //步骤6:发送网络请求(异步)
                    call.enqueue(new Callback<LoginReceive>() {
                        @Override
                        public void onResponse(Call<LoginReceive> call, Response<LoginReceive> response) {
                            LoginReceive receive = response.body();
                            System.out.println(receive);
                            if (receive.getCode() == 1) {
                                //一致登录成功
                                Toast.makeText(RegisterActivty.this, "注册成功", Toast.LENGTH_SHORT).show();
                                //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                                //登录成功后关闭此页面进入主页
                                Intent data = new Intent();
                                //datad.putExtra( ); name , value ;
                                data.putExtra("isLogin", true);
                                //RESULT_OK为Activity系统常量，状态码为-1
                                // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用1setResult传递data值
                                setResult(RESULT_OK, data);
                                //销毁注册界面
                                RegisterActivty.this.finish();
                                //跳转到登录界面，登录成功的状态传递到 MainActivity 中
                                startActivity(new Intent(RegisterActivty.this, LoginActivty.class));
                            } else {
                                Toast.makeText(RegisterActivty.this, "添加用户失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<LoginReceive> call, Throwable t) {
                            Toast.makeText(RegisterActivty.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


}
