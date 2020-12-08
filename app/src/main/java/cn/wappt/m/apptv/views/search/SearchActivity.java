package cn.wappt.m.apptv.views.search;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.helper.DatabaseHelper;
import cn.wappt.m.apptv.utils.StatusBarUtil;

/**
 * @author: wsq
 * @date: 2020/10/26
 * Description:
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private TextView textquit;
    private EditText searchdata;
    private ImageView clear;
    private int num = 1;
    private TextView empey;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_for_activit);
        init();
        put();
        handler.postDelayed(runnable,200);     //调用定时器
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

    //创建定时器获取基本数据
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            handler.sendMessage(msg);
            handler.postDelayed(this,200); //设置多久刷新一次
        }
    };



    //检测如果有值则显示清空按钮
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断是否可以显示
            if (searchdata.getText().length()!=0) {
                if (num == 1) {
                    clear.setVisibility(View.VISIBLE);
                    num--;
                }
            }
            if (searchdata.getText().length()==0) {
                if (num == 0) {
                    clear.setVisibility(View.INVISIBLE);
                    num++;
                }
            }
        }
    };


    /**
     * 初始化容器
     */
    private void init() {
        textquit = findViewById(R.id.textquit);
        searchdata = findViewById(R.id.searchdata);
        clear = findViewById(R.id.clear);
        empey = findViewById(R.id.empey);
        empey.setOnClickListener(this);
        clear.setOnClickListener(this);
        textquit.setOnClickListener(this);
        searchdata.setOnClickListener(this);


        //监听搜索栏回车事件
        searchdata.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            /**
             * @param v 被监听的对象
             * @param actionId  动作标识符,如果值等于EditorInfo.IME_NULL，则回车键被按下。
             * @param event    如果由输入键触发，这是事件；否则，这是空的(比如非输入键触发是空的)。
             * @return 返回你的动作
             */
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (searchdata.getText()!= null && searchdata.getText().length()!=0) {
                    String names = searchdata.getText().toString();
                    //开启子线程进行处理
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int count = 0;
                            //创建游标对象
                            Cursor cursor = db.query("userSearchHistory", new String[]{"name"}, null, null, null, null, null);
                            //创建存放数据的ContentValues对象
                            ContentValues values = new ContentValues();
                            values.put("name",names);
                            while(cursor.moveToNext()){
                                String name = cursor.getString(cursor.getColumnIndex("name"));
                                if (name.equals(names)) {
                                    //删除已经存在的数据
                                    String [] args = {names};
                                    db.delete("userSearchHistory","name=?",args);
                                    //数据库执行插入
                                    db.insert("userSearchHistory", null, values);
                                    count = 1;
                                    break;
                                }
                            }
                            if (count == 0) {
                                //数据库执行插入
                                db.insert("userSearchHistory", null, values);
                            }
                            Message message = haha.obtainMessage();
                            message.what = 1;
                            haha.sendMessage(message);
                        }
                    }).start();
                    //跳转页面
                  Intent intent=new Intent(SearchActivity.this,SearchForDetails.class);//传递数据
                    intent.putExtra("searchString",names);    //一定要加toString 不然传过去的是地址
                    startActivity(intent);
                    searchdata.setText("");
                }
                return event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }
    /**
     * 通过子线程更新UI
     */
    @SuppressLint("HandlerLeak")
    Handler haha = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                put();
            }
        }
    };

    /**
     * 加载历史记录
     */
    public void put(){
        //实例化创建数据库创建
        dbHelper= new DatabaseHelper(SearchActivity.this,"test_db",null,1);
        db = dbHelper.getWritableDatabase();
        FlowLayout flKeyword = findViewById(R.id.fl_keyword);
        // 关键字集合
        List<String> list = new ArrayList<>();
        //创建游标对象 查询
        Cursor cursor = db.query("userSearchHistory", new String[]{"name"}, null, null, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(name);
        }
        // 关闭游标，释放资源
        cursor.close();
        List<String> mode = new ArrayList<>();
        for (int i = list.size()-1; i >=0 ; i--) {
            mode.add(list.get(i));
        }

       // 设置文字大小
        flKeyword.setTextSize(10);
        // 设置文字颜色
        flKeyword.setTextColor(Color.BLACK);
        // 设置文字背景
        flKeyword.setBackgroundResource(R.drawable.bg_frame);

        // 设置文字水平margin
        flKeyword.setHorizontalSpacing(12);
        // 设置文字垂直margin
        flKeyword.setVerticalSpacing(12);

        // 设置文字水平padding
        flKeyword.setTextPaddingH(14);
        // 设置文字垂直padding
        flKeyword.setTextPaddingV(5);

        // 设置UI与点击事件监听
        // 最后调用setViews方法
        flKeyword.setViews(mode, new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                //跳转页面
                Intent intent=new Intent(SearchActivity.this,SearchForDetails.class);//传递数据
                intent.putExtra("searchString",content);    //一定要加toString 不然传过去的是地址
                startActivity(intent);
                put();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击取消则关闭页面
            case R.id.textquit:
                //更改成跳转回首页
                finish();
                break;
           /* //点击搜索栏，出现搜索推荐(以后再整)
            case R.id.searchdata:

                break;*/
            case R.id.clear:
                searchdata.setText("");
                break;
            case R.id.empey:
                db.delete("userSearchHistory",null,null);
                put();
                break;
        }
    }


}
