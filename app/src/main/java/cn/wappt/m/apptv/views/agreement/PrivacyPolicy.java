package cn.wappt.m.apptv.views.agreement;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cn.wappt.m.apptv.R;

/**
 * 服务协议与隐私政策
 */
public class PrivacyPolicy extends AppCompatActivity {

    Resources re;
    String [] fw;
    String [] ys;
    LinearLayout lin_ssr;
    TextView text_title;
    TextView text_data;
    TextView text_privacy;
    TextView text_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        init();
        coume();
    }

    private void coume() {
        lin_ssr.removeAllViews();
        text_title.setText(R.string.fwxytitle);
        text_data.setText(R.string.fwxydate);
        for (int i = 0; i <fw.length ; i++) {
            TextView text = null;
            if (i%2 == 1) {
                text = new TextView(this);
                LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text.setLayoutParams(layoutheight);
                text.setText(fw[i]);
            }else{
                text = new TextView(this);
                LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text.setLayoutParams(layoutheight);
                text.setTextColor(Color.BLACK);
                text.setText(fw[i]);
            }
            lin_ssr.addView(text);
        }
    }

    public void on1(View view){
        text_privacy.setBackgroundResource(R.drawable.bk_underline);
        text_server.setBackgroundResource(R.drawable.bk_underline2);
        lin_ssr.removeAllViews();
        text_title.setText(R.string.fwxytitle);
        text_data.setText(R.string.fwxydate);
        for (int i = 0; i <fw.length ; i++) {
            TextView text = null;
            if (i%2 == 1) {
                text = new TextView(this);
                LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text.setLayoutParams(layoutheight);
                text.setText(fw[i]);
            }else{
                text = new TextView(this);
                LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text.setLayoutParams(layoutheight);
                text.setTextColor(Color.BLACK);
                text.setText(fw[i]);
            }
            lin_ssr.addView(text);
        }
    }

    public void on2(View view){
        text_privacy.setBackgroundResource(R.drawable.bk_underline2);
        text_server.setBackgroundResource(R.drawable.bk_underline);
        lin_ssr.removeAllViews();
        text_title.setText(R.string.yszctitle);
        text_data.setText(R.string.yszcdate);
        for (int i = 0; i <ys.length ; i++) {
            TextView text = null;
            if (i%2 == 0) {
                text = new TextView(this);
                LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text.setLayoutParams(layoutheight);
                text.setText(ys[i]);
            }else{
                text = new TextView(this);
                LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                text.setLayoutParams(layoutheight);
                text.setTextColor(Color.BLACK);
                text.setText(ys[i]);
            }
            lin_ssr.addView(text);
        }
    }



    private void init() {
        re = getResources();
        lin_ssr = findViewById(R.id.lin_ssr);
        text_title = findViewById(R.id.text_title);
        text_data = findViewById(R.id.text_data);
        text_privacy = findViewById(R.id.text_privacy);
        text_server = findViewById(R.id.text_server);
        fw = re.getStringArray(R.array.fwxy);
        ys = re.getStringArray(R.array.yszc);

    }


}
