package cn.wappt.m.apptv.views.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.wappt.m.apptv.Activitv_Home;
import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.LoginReceive;
import cn.wappt.m.apptv.interfaces.LoginInterfaces;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivty extends Activity  {
    private String userName,psw,spPsw;//获取的用户名，密码，验证码

    private EditText
            et_user_name,
            et_user_psw;//编辑框


    private ImageView login_return;
    Button login;
    private TextView forget_password_log, Sign_up_now_log ,log_Privacy_Policy,log_Service_Agreement;
    private CheckBox check_yes_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_main);
        login_return=findViewById(R.id.login_return);
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

    //服务协议
    public void service1(View view){
        Pop_Up pop_up = new Pop_Up(this);
        pop_up.jump = true;
        new XPopup.Builder(this)
                .isDestroyOnDismiss(true)    //是否在消失的时候销毁资源
                .asCustom(pop_up)
                .show();
    }

    //隐私政策
    public void service2(View view){
        Pop_Up pop_up = new Pop_Up(this);
        pop_up.jump = false;
        new XPopup.Builder(this)
                .isDestroyOnDismiss(true)    //是否在消失的时候销毁资源
                .asCustom(pop_up)
                .show();
    }

    //获取界面控件
    private void init() {
        login_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                LoginActivty.this.finish();
            }
        });
        //获取的用户名，密码，验证码
        et_user_name=  findViewById(R.id.user_name_log);
        et_user_psw=  findViewById(R.id.user_password_log);
        login=findViewById(R.id.log_button);
        forget_password_log=findViewById(R.id.forget_password_log);
        Sign_up_now_log=findViewById(R.id.Sign_up_now_log);
        log_Privacy_Policy= findViewById(R.id.log_Privacy_Policy);
        log_Service_Agreement=findViewById(R.id.log_Service_Agreement);
        check_yes_no=findViewById(R.id.check_yes_no);
        log_Privacy_Policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service1(v);
            }
        });

        log_Service_Agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service2(v);
            }
        });

        //立即注册控件的点击事件
        Sign_up_now_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了跳转到注册界面，并实现注册功能
                Intent intent=new Intent(LoginActivty.this,RegisterActivty.class);
                startActivityForResult(intent, 1);
            }
        });
        //找回密码控件的点击事件
        forget_password_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivty.this,RetrieveActivty.class));
            }
        });
        readAccount();


        //登录按钮的点击事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始登录，获取用户名和密码 getText().toString().trim();
                userName = et_user_name.getText().toString().trim();
                psw = et_user_psw.getText().toString().trim();
                // TextUtils.isEmpty
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivty.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivty.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    // md5Psw.equals(); 判断，输入的密码加密后，是否与保存在SharedPreferences中一致
                }else  if (! (psw.length()> 5)){
                    Toast.makeText(LoginActivty.this, "密码长度小于6位数，请重新输入", Toast.LENGTH_SHORT).show();
                }else if (!check_yes_no.isChecked()){
                    Toast.makeText(LoginActivty.this, "请勾选隐私政策", Toast.LENGTH_SHORT).show();
                }
                else  {
                    Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
                    LoginInterfaces request = retrofit.create(LoginInterfaces.class);
                    //对 发送请求数据进行封装
                    Call<LoginReceive> call = request.login(userName,psw);
                    //发送网络请求(异步)
                    call.enqueue(new Callback<LoginReceive>() {
                        //请求成功时回调
                        @Override
                        public void onResponse(Call<LoginReceive> call, Response<LoginReceive> response) {
                            LoginReceive receive = response.body();
                            System.out.println(receive);
                            if (receive.getCode() == 1) {
                                //一致登录成功
                                Toast.makeText(LoginActivty.this, "登录成功", Toast.LENGTH_SHORT).show();
                                //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                                //创建sharedPreference对象，info表示文件名，MODE_PRIVATE表示访问权限为私有的
                                addShared(userName,psw);
                                List<String> list =response.headers().values("Set-Cookie");
                                String  cookis="PHPSESSID=rv9kg99cllvsdgfo0kpr7u9937;";
                                cookis+= getSessionCookie(list);
                                //保 存
                                SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
                                SharedPreferences.Editor ed = sp.edit();
                                System.out.println(cookis);
                                ed.putString("cookie", cookis);
                                ed.commit();
                                //登录成功后关闭此页面进入主页
                                Intent data = new Intent();
                                //datad.putExtra( ); name , value ;
                                data.putExtra("isLogin", true);
                                //RESULT_OK为Activity系统常量，状态码为-1
                                // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                                setResult(RESULT_OK, data);
                                //销毁登录界面
                                LoginActivty.this.finish();
                                //跳转到主界面，登录成功的状态传递到 MainActivity 中
                                Intent intent = new Intent(LoginActivty.this, Activitv_Home.class);
                                intent.putExtra("id",1);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivty.this, receive.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<LoginReceive> call, Throwable t) {
                            Toast.makeText(LoginActivty.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        });
    }



    // 校验账号不能为空且必须是中国大陆手机号（宽松模式匹配）
    private boolean isTelphoneValid(String account) {
        if (account == null) {
            return false;
        }
        // 首位为1, 第二位为3-9, 剩下九位为 0-9, 共11位数字
        String pattern = "^[1]([3-9])[0-9]{9}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(account);
        return m.matches();
    }

    //读取保存在本地的用户名和密码
    public void readAccount() {
        //创建SharedPreferences对象
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        //获得保存在SharedPredPreferences中的用户名和密码
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        //在用户名和密码的输入框中显示用户名和密码
        et_user_name.setText(username);
        et_user_psw.setText(password);
    }

    //保存数据的方法
    public void addShared(String userName,String password){
        //创建sharedPreference对象，info表示文件名，MODE_PRIVATE表示访问权限为私有的
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        //获得sp的编辑器
        SharedPreferences.Editor ed = sp.edit();
        //以键值对的显示将用户名和密码保存到sp中
        ed.putString("username", userName);
        ed.putString("password", password);
        //提交用户名和密码
        ed.commit();

    }


    /*
     * 用户的cookie截取,
     * */
    public static String getSessionCookie( List<String> list) {
        String cookis="";
        if (list !=null) {
            for (int i = 0; i <list.size() ; i++) {
                if (i==list.size()-1){
                    cookis += list.get(i).substring(0, list.get(i).indexOf(";") );
                }else {
                    cookis += list.get(i).substring(0, list.get(i).indexOf(";") + 1);
                }
            }
            return cookis;
        }
        return "";
    }


}