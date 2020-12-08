package cn.wappt.m.apptv.views.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.lxj.xpopup.XPopup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.base.UserInformationBean;
import cn.wappt.m.apptv.dao.UserDao;
import cn.wappt.m.apptv.interfaces.UserInterfaces;
import cn.wappt.m.apptv.utils.Constants;
import cn.wappt.m.apptv.utils.RetrofitManager;
import cn.wappt.m.apptv.views.details.VideoDownloadListActivity;
import cn.wappt.m.apptv.views.user.LoginActivty;
import cn.wappt.m.apptv.views.user.ModifyUserActicty;
import cn.wappt.m.apptv.views.user.SetUi_Main;
import cn.wappt.m.apptv.views.user.profieActivty;
import cn.wappt.m.apptv.views.user.view.Pop_Sorry;
import cn.wappt.m.apptv.views.user.view.Round_head;
import cn.wappt.m.apptv.views.watch.MyWatchActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends Fragment implements View.OnClickListener {

    View view;
    //上面的列表
    ConstraintLayout myInfo;//我的信息
    ConstraintLayout myFavorites;//我的收藏
    ConstraintLayout historyRecord;//浏览记录
    ConstraintLayout PersonalCenter;//个人中心

    //下面的列表
    ConstraintLayout share_tap; //分享
    ConstraintLayout Cache_tap;   //缓存
    ConstraintLayout setui_tap;   //设置
    ConstraintLayout theme_tap;   //风格
    Round_head headTop;  //图片
    ConstraintLayout userModificationLayout; //修改
    TextView log_text;  //用户名
    TextView Login_discovery;

    private String user_answer;
    private String user_question;
    private String user_phone;
    SharedPreferences sp ;
    private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
            //保存登录的信息
    String[] key;
    String[] value;

    UserDao dao;
    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //view视图的缓存
        if (view == null) {
            view = inflater.inflate(R.layout.personalcenter_main, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();

        if (parent != null) {
            parent.removeView(view);
        }

        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dao=new UserDao(getContext());
        System.out.println(dao.findByColumnTypes());
        System.out.println(log_text.getText());

        if (dao.findByColumnTypes()!=null&& user_answer==null){
            System.out.println("修改用户");
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        getSelectUser();  //进入修改的数据
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            readAccount();
        }
       if (log_text.getText().toString().equals("登录/注册")  &&dao.findByColumnTypes()==null){
           System.out.println("第一次开始判断是否加载成功");
            new Thread(new Runnable() {
                @Override
                public void run() {
                        getNews();  //个人信息
                }
            }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getSelectUser();  //进入修改的数据
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    if (log_text.getText()=="登录/注册" && (dao.findByColumnTypes()!=null)){
           System.out.println("第2次进行更新数据");
            cwjHandler.post(mUpdateResults); //告诉UI线程可以更新结果了
    }else {
        System.out.println("第3次进行更新数据");
        cwjHandler.post(mUpdateResults); //告诉UI线程可以更新结果了
    }
        if (dao.findByColumnTypes()!=null){
            try {
                String cookis=SharedPreferencesCookis();
                if (dao.findByColumnTypes().getCookie()!=null && cookis!=null &&(!dao.findByColumnTypes().getCookie().equals(cookis))){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                                getNews();  //个人信息

                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getSelectUser();  //进入修改的数据
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    final Handler cwjHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
                if (dao.findByColumnTypes()!=null) {
                    log_text.setText(dao.findByColumnTypes().getUsername());
                    Login_discovery.setText("会员:  "+dao.findByColumnTypes().getMembergroup());
                    headTop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dao.findByColumnTypes()!=null) {
                                Intent intent = new Intent(getActivity(), profieActivty.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getContext(), "没有登录,请重新登录", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    log_text.setText("登录/注册");
                    Login_discovery.setText("登录发现更多精彩");
                }
            }
    };

    public void initView() {
        myInfo=view.findViewById(R.id.my_info);
        myFavorites=view.findViewById(R.id.my_Favorites);
        historyRecord=view.findViewById(R.id.history_record);
        PersonalCenter=view.findViewById(R.id.Personal_center);
        Login_discovery=view.findViewById(R.id.Login_discovery);
        share_tap=view.findViewById(R.id.share_tap);
        Cache_tap=view.findViewById(R.id.Cache_tap);
        setui_tap=view.findViewById(R.id.setui_tap);
        theme_tap=view.findViewById(R.id.theme_tap);
        userModificationLayout=view.findViewById(R.id.user_modification_layout_tap);
        headTop=view.findViewById(R.id.headTop);
        log_text=view.findViewById(R.id.log_text);


    }

    //显示没有写完的页面
    public void zanwu(View view) {
        new XPopup.Builder(getContext())
                .isDestroyOnDismiss(true)    //是否在消失的时候销毁资源
                .asCustom(new Pop_Sorry(getContext()))
                .show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取用户名
        sp= getActivity().getSharedPreferences("info", MODE_PRIVATE);
        //获得保存在SharedPredPreferences中的用户名和密码

        //添加点击事件
        share_tap.setOnClickListener(this);
        //我的消息
        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zanwu(view);
            }
        });
        //我的收藏
        myFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zanwu(view);
            }
        });
        //主题风格
        theme_tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zanwu(view);
            }
        });
        //登录
        log_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (log_text.getText().equals("登录/注册")&&Login_discovery.getText().equals("登录发现更多精彩")) {
                    //跳转
                    Intent intent1 =new Intent(getContext(),LoginActivty.class);
                    startActivity(intent1);
                }
            }
        });

        //设置页面
        setui_tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), SetUi_Main.class);
                intent.putExtra("value",value);
                startActivity(intent);
            }
        });

        //会员个人中心  --暂时没有搞
        PersonalCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zanwu(view);
            }
        });

        //修改
        userModificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dao.findByColumnTypes()!=null && user_answer!=null) {
                    //跳转修改页面
                    Intent intent = new Intent(getActivity(), ModifyUserActicty.class);
                    intent.putExtra("user_answer",user_answer);
                    intent.putExtra("user_question",user_question);
                    intent.putExtra("user_phone",user_phone);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "没有登录,请重新登录", Toast.LENGTH_SHORT).show();
                }
            }

        });
        //缓存
        Cache_tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VideoDownloadListActivity.class);
                startActivity(intent);
            }
        });
        //浏览记录
        historyRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyWatchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            //点击头像进行切换头像
/*            case R.id.headTop:
                break;*/

            //分享
            case R.id.share_tap:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "www.baidu.com");    //内容
                //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
                shareIntent = Intent.createChooser(shareIntent, "分享应用");     //标题
                startActivity(shareIntent);
                break;
        }
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            //是获取注册界面回传过来的用户名
            String userName = data.getStringExtra("userName");
            if (!TextUtils.isEmpty(userName)) {
                //设置用户名到 et_user_name 控件
                log_text.setText(userName);
                System.out.println("\n\n" + userName + "\n\n\n\n");
            }
        }
    }
    public String SharedPreferencesCookis() throws IOException {
        SharedPreferences sp = getActivity().getSharedPreferences("info", MODE_PRIVATE);
        //获得保存在SharedPredPreferences中的用户名和密码
        String cookie = sp.getString("cookie", "");
        return cookie;
    }

    //获取修改的信息
    private void getSelectUser() throws IOException {
        String cookie = SharedPreferencesCookis();
        Document document = Jsoup.connect(Constants.userModif).header("Cookie", cookie).timeout(60000).get();
        ;//这里可用get也可以post方式，具体区别请自行了解
        Elements titleLinks = document.select("div.cur").select("p");    //解析来获取每条新闻的标题与链接地
        value = new String[titleLinks.size()];
        if (titleLinks.size() != 0) {
            user_phone = titleLinks.select("input[name=user_phone]").attr("value");
            user_question = titleLinks.select("input[name=user_question]").attr("value");
            user_answer = titleLinks.select("input[name=user_answer]").attr("value");
            addShared(user_phone,user_question,user_answer);
        } else {
            Looper.prepare();
          //  Toast.makeText(getContext(), "没有登录,请重新登录", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }

    }


    //获取个人信息
    private void getNews(){
        SharedPreferences sp = getActivity().getSharedPreferences("info", MODE_PRIVATE);
        //获得保存在SharedPredPreferences中的用户名和密码
        String cookie = sp.getString("cookie", "");
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        UserInterfaces request = retrofit.create(UserInterfaces.class);
        //对 发送请求数据进行封装
        Call<ResponseBody> call = request.selecyuser(username, password);
        //发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //把原始数据转为字符串
                String jsonStr = null;
                try {
                    jsonStr = response.body().string();
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Date date = dateFormat.parse(jsonObject.getString("regtime"));
                    Date date2 = null;
                    if (jsonObject.getString("lastlogin") != null && jsonObject.getString("lastlogin") != "") {
                        date2 = dateFormat.parse(jsonObject.getString("lastlogin"));
                    }
                    UserInformationBean informationBean = new UserInformationBean(jsonObject.get("name").toString(),
                            jsonObject.get("group").toString(),
                            jsonObject.get("points").toString(),
                            jsonObject.get("condition").toString(),
                            jsonObject.get("qq").toString(),
                            jsonObject.get("email").toString(),
                            date,
                            date2,
                            cookie
                    );
                    System.out.println(informationBean);
                    dao.add(informationBean);
                    cwjHandler.post(mUpdateResults); //告诉UI线程可以更新结果了
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "对不起,服务器连接出错", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //获取问题
    public void readAccount() {
         user_phone = sp.getString("user_phone_fragment", "");
         user_question = sp.getString("user_question_fragment", "");
         user_answer = sp.getString("user_answer_fragment", "");
    }

    //保存数据的方法
    public void addShared(String user_phone_fragment,String user_question_fragment,String  user_answer_fragment){
        //获得sp的编辑器
        SharedPreferences.Editor ed = sp.edit();
        //以键值对的显示将用户名和密码保存到sp中
        ed.putString("user_phone_fragment", user_phone_fragment);
        ed.putString("user_question_fragment", user_question_fragment);
        ed.putString("user_answer_fragment", user_answer_fragment);
        //提交用户名和密码
        ed.commit();

    }

}


