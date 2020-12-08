package cn.wappt.m.apptv.views.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.UserUpdateReceive;
import cn.wappt.m.apptv.dao.UserDao;
import cn.wappt.m.apptv.interfaces.UserInterfaces;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author: wsq
 * @date: 2020/9/8
 * Description:
 */
public class ModifyUserActicty extends Activity {



    @BindView(R.id.quit_amend)
    ImageView quitAmend;

    @BindView(R.id.edit_username_amend)
    EditText editUsernameAmend;
    @BindView(R.id.edit_user_amendwt)
    EditText editUserAmendwt;
    @BindView(R.id.edit_user_amend)
    EditText editUserAmend;
    @BindView(R.id.edit_password_u1)
    EditText editPasswordU1;
    @BindView(R.id.edit_password_u2)
    EditText editPasswordU2;
    @BindView(R.id.edit_password_u3)
    EditText editPasswordU3;
    @BindView(R.id.edit_password_u4)
    EditText editPasswordU4;
    @BindView(R.id.submit_u2)
    Button submitU2;


    private String qq_phone;
    private String user_email;
    private String user_answer;
    private String user_question;
    private String user_phone;
    private String cookies;
    UserDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_main);
        ButterKnife.bind(this);
        //获取传入的参数
        Intent intent=getIntent();
        dao=new UserDao(this);
        if (intent!=null) {
            user_answer = intent.getStringExtra("user_answer");
            user_question = intent.getStringExtra("user_question");
            cookies=dao.findByColumnTypes().getCookie();
            if (user_answer!=null) {
                editUserAmend.setText(user_answer);
            }
            if (user_answer!=null){
                editUserAmendwt.setText(user_answer);
            }
            if (dao.findByColumnTypes().getQqnumber()!=null) {
                editPasswordU4.setText(dao.findByColumnTypes().getQqnumber());
            }
    if (dao.findByColumnTypes().getUsername()!=null){
        editUsernameAmend.setText(dao.findByColumnTypes().getUsername());
    }
        }//返回
        quitAmend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    //提交修改的数据
        submitU2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取输入的数据
                        String user_pwd = editPasswordU1.getText().toString().trim();
                        String user_pwd1 = editPasswordU2.getText().toString().trim();
                        String user_pwd2 = editPasswordU3.getText().toString().trim();

                        String user_qq = editPasswordU4.getText().toString().trim();
                        String user_email ="";//暂时为空
                        String user_phone = "";//暂时为空
                        String user_question = editUserAmendwt.getText().toString().trim();
                        String user_answer = editUserAmend.getText().toString().trim();
                        //修改数据
                        Modifuser(user_pwd, user_pwd1, user_pwd2, user_qq, user_email, user_phone, user_question, user_answer);
                    }

                }).start();

            }
        });


    }

    //获取个人信息
    private void Modifuser(String user_pwd, String user_pwd1, String user_pwd2, String user_qq, String user_email, String user_phone, String user_question, String user_answer){
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        UserInterfaces request = retrofit.create(UserInterfaces.class);
        //对 发送请求数据进行封装
        Call<UserUpdateReceive> call = request.updateuser(dao.findByColumnTypes().getUsername(),user_pwd, user_pwd1,user_pwd2,user_qq,user_email,user_phone,user_question,user_answer);
        //发送网络请求(异步)
        call.enqueue(new Callback<UserUpdateReceive>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<UserUpdateReceive> call, Response<UserUpdateReceive> response) {
                //把原始数据转为字符串
                String jsonStr = null;
                UserUpdateReceive  jsonObject = response.body();
                if ( jsonObject.getCode()==1&& jsonObject.getMsg().equals("保存成功")){
                    addShared(user_pwd2);
                    Toast.makeText(ModifyUserActicty.this, "请重新登录", Toast.LENGTH_SHORT).show();
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中
                    Intent intent = new Intent(ModifyUserActicty.this, LoginActivty.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ModifyUserActicty.this, jsonObject.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserUpdateReceive> call, Throwable t) {
                Toast.makeText(ModifyUserActicty.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //保存数据的方法
    public void addShared(String password){
        //创建sharedPreference对象，info表示文件名，MODE_PRIVATE表示访问权限为私有的
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        //获得sp的编辑器
        SharedPreferences.Editor ed = sp.edit();
        //以键值对的显示将用户名和密码保存到sp中

        ed.putString("password", password);
        //提交用户名和密码
        ed.commit();

    }

}
