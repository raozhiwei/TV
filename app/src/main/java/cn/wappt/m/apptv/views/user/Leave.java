package cn.wappt.m.apptv.views.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.wappt.m.apptv.R;
import cn.wappt.m.apptv.utils.StatusBarUtil;

/**
 * 用户反馈
 */
public class Leave extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearInput;
    private EditText eidtInput;
    private EditText editdescribe;
    public final int MAXLINES = 12;
    private ImageView finquitbut;
    private Button button;
    private ImageView img1;
    private ImageView img2;
    private HttpMessageSubmitted httpMessageSubmitted = new HttpMessageSubmitted();
    private Toast toast;
    int count = 0;

    //创建按钮内容
    String buttondata = "遇到问题--";

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
    public Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (HttpMessageSubmitted.mode.equals("1")) {
                toast = Toast.makeText(Leave.this,"提交成功，感谢你的反馈",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                handler2.removeCallbacks(runnable2);      //解除定时器
                finish();
            }else if (HttpMessageSubmitted.mode.equals("2")){
                toast = Toast.makeText(Leave.this,"提交失败，请稍后重试",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                handler2.removeCallbacks(runnable2);      //解除定时器
                finish();
            }else if (HttpMessageSubmitted.mode.equals("3")){
                toast = Toast.makeText(Leave.this,"请求错误",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                handler2.removeCallbacks(runnable2);      //解除定时器
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave);
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

    private void initView() {
        //初始化
        linearInput = findViewById(R.id.leave_linearInput);
        eidtInput = findViewById(R.id.leave_editInput);
        editdescribe = findViewById(R.id.leave_editdescribe);
        finquitbut = findViewById(R.id.leave_quit_remo);
        button = findViewById(R.id.leave_button);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);

        //添加点击事件
        linearInput.setOnClickListener(this);
        finquitbut.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        button.setOnClickListener(this);

        //添加监听事件，限制输入框最大行数
        eidtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                int lines = eidtInput.getLineCount();
                // 限制最大输入行数
                if (lines > MAXLINES) {
                    String str = s.toString();
                    int cursorStart = eidtInput.getSelectionStart();
                    int cursorEnd = eidtInput.getSelectionEnd();
                    if (cursorStart == cursorEnd && cursorStart < str.length() && cursorStart >= 1) {
                        str = str.substring(0, cursorStart-1) + str.substring(cursorStart);
                    } else {
                        str = str.substring(0, s.length()-1);
                    }
                    // setText会触发afterTextChanged的递归
                    eidtInput.setText(str);
                    // setSelection用的索引不能使用str.length()否则会越界
                    eidtInput.setSelection(eidtInput.getText().length());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.leave_linearInput:
                linearInput.requestFocus();
                InputMethodManager imm = (InputMethodManager) linearInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;

            //退出按钮
            case R.id.leave_quit_remo:
                finish();
                break;

            case R.id.img1:
                img1.setBackgroundResource(R.drawable.gouxuan);
                img2.setBackgroundResource(R.drawable.weigouxuan);
                editdescribe.setHint("请描述您遇到的问题");
                eidtInput.setHint("在（）情况下，我的手机出现了（）问题");
                buttondata = "遇到问题--";
                break;

            case R.id.img2:
                img1.setBackgroundResource(R.drawable.weigouxuan);
                img2.setBackgroundResource(R.drawable.gouxuan);
                editdescribe.setHint("请描述您需求的功能");
                eidtInput.setHint("在（）地方，实现（）功能");
                buttondata = "新建议--";
                break;

            case R.id.leave_button:
                //实现接口，进行上传留言
                //判断输入框
                if (editdescribe.getText() != null && editdescribe.getText().length() != 0
                        &&eidtInput.getText() != null && eidtInput.getText().length() != 0) {
                    //调用留言接口，上传留言
                    String buff = buttondata+editdescribe.getText()+"--"+eidtInput.getText();
                    httpMessageSubmitted.get(buff);
                    if (count == 0) {
                        handler2.postDelayed(runnable2,300);     //调用定时器
                        count++;
                    }else{
                        toast = Toast.makeText(Leave.this,"你已经提交过了，请稍等",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM,0,0);
                        toast.show();
                    }
                }else{
                    toast = Toast.makeText(this,"以上信息为必填，不能为空",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;
        }
    }

}
