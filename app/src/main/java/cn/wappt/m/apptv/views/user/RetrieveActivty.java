package cn.wappt.m.apptv.views.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.interfaces.RetrieveInterFaces;
import cn.wappt.m.apptv.utils.Constants;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.utils.StatusBarUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author: wsq
 * @date: 2020/9/7
 * Description: 找回密码
 */
public class RetrieveActivty extends Activity {


    @BindView(R.id.retrieve_return)
    ImageView retrieve_return;//返回
    @BindView(R.id.user_name_zhmm)
    EditText userNameZhmm;//用户名
    @BindView(R.id.Security_Question)
    EditText SecurityQuestion;//问题
    @BindView(R.id.Secret_security)
    EditText Secretsecurity;//问题答案
    @BindView(R.id.user_password_zhmm)
    EditText userPasswordZhmm;//密码
    @BindView(R.id.zhmm_code)
    EditText zhmm_code;//输入验证码
    @BindView(R.id.zhmm_verification_code)
    ImageView zhmm_verification_code;  //验证码图片
    @BindView(R.id.zhmm_button)
    Button zhmmButton;//确定



    private String name, questio, answer, password, passwordss;
    // private Bitmap bitmap;//验证码对象

    private int phoneCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieve_main);
        ButterKnife.bind(this);
        Instance();
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
        retrieve_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                RetrieveActivty.this.finish();
            }
        });
        //刷新验证码图片
        zhmm_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Instance();
            }
        });

        //登录按钮的点击事件
        zhmmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取数据
                name = userNameZhmm.getText().toString().trim();
                answer = Secretsecurity.getText().toString().trim();
                questio = SecurityQuestion.getText().toString().trim();
                password = userPasswordZhmm.getText().toString().trim();
                passwordss=password;
                phoneCodes = Integer.parseInt(zhmm_code.getText().toString());
                // TextUtils.isEmpty
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(RetrieveActivty.this, "请输入用户名", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(answer) && TextUtils.isEmpty(questio)) {
                    Toast.makeText(RetrieveActivty.this, "请输入问题和答案", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RetrieveActivty.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(passwordss)) {
                    Toast.makeText(RetrieveActivty.this, "密码不一致", Toast.LENGTH_SHORT).show();
                } else if (!(password.length() > 5)) {
                    Toast.makeText(RetrieveActivty.this, "密码长度小于6位数，请重新输入", Toast.LENGTH_SHORT).show();
//if (code.toLowerCase().equals(phoneCodes.toLowerCase()))
                } else if (phoneCodes==0) {
                    Toast.makeText(RetrieveActivty.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                } else {
                    //进行服务器交互
                    Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
                    RetrieveInterFaces request = retrofit.create(RetrieveInterFaces.class);
                    Map map = new HashMap();
                    map.put("user_name", name);
                    map.put("user_question", questio);
                    map.put("user_answer", answer);
                    map.put("user_pwd", password);
                    map.put("user_pwd2", password);
                    map.put("verify", phoneCodes);
                    JSONObject jsons = new JSONObject(map);
                    String json = jsons.toString();
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                    //对 发送请求数据进行封装
                    Call<Map> call = request.Regist(body);
                    System.out.println(body.toString());
                    System.out.println(json);

                    call.enqueue(new Callback<Map>() {
                        @Override
                        public void onResponse(Call<Map> call, Response<Map> response) {
                            Map receive = response.body();
                            System.out.println(receive.get("code") + "\t" + receive.get("mag"));
                            if (response.code() == 200) {
                                //一致登录成功
                                Toast.makeText(RetrieveActivty.this, "找回密码成功", Toast.LENGTH_SHORT).show();
                                //修改成功跳转登录
                                Intent data = new Intent();
                                //RESULT_OK为Activity系统常量，状态码为-1
                                // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                                addShared(name, password);
                                setResult(RESULT_OK, data);
                                //                                    //销毁登录界面
                                RetrieveActivty.this.finish();
                                //跳转到主界面，登录成功的状态传递到 MainActivity 中
                                startActivity(new Intent(RetrieveActivty.this, LoginActivty.class));
                            } else {
                                Toast.makeText(RetrieveActivty.this, (CharSequence) receive.get("mag"), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Map> call, Throwable t) {
                            Toast.makeText(RetrieveActivty.this, "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    private void Instance() {
        String updateTime = String.valueOf(System.currentTimeMillis());// 在需要重新获取更新的图片时调用
        Glide.with(RetrieveActivty.this)
                .load(Constants.imagcodeurl)
                .skipMemoryCache(true)   //验证码不缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(zhmm_verification_code);
    }


    //保存数据的方法
    public void addShared(String userName, String password) {
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


}
