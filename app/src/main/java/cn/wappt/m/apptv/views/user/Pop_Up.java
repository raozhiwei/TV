package cn.wappt.m.apptv.views.user;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;

import cn.wappt.m.apptv.R;

/**
 * @author 纵游四方悠自得
 * @create 2020/10/28--14:20
 * @effect
 */
public class Pop_Up extends CenterPopupView {

    Resources re;
    String [] linList;
    LinearLayout lin_jump;
    public boolean jump = true;     //true=服务协议，false=隐私政策

    public Pop_Up(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.scrollviewsorrytextfwzc;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initview();
        coume();
    }

    private void coume() {
        if (jump) {
            for (int i = 0; i <linList.length ; i++) {
                TextView text = null;
                if (i%2 == 1) {
                    text = new TextView(getContext());
                    LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    text.setLayoutParams(layoutheight);
                    text.setText(linList[i]);
                }else{
                    text = new TextView(getContext());
                    LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    text.setLayoutParams(layoutheight);
                    text.setTextColor(Color.BLACK);
                    text.setText(linList[i]);
                }
                lin_jump.addView(text);
            }
        }else{
            for (int i = 0; i <linList.length ; i++) {
                TextView text = null;
                if (i%2 == 0) {
                    text = new TextView(getContext());
                    LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    text.setLayoutParams(layoutheight);
                    text.setText(linList[i]);
                }else{
                    text = new TextView(getContext());
                    LinearLayout.LayoutParams layoutheight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    text.setLayoutParams(layoutheight);
                    text.setTextColor(Color.BLACK);
                    text.setText(linList[i]);
                }
                lin_jump.addView(text);
            }
        }
    }

    private void initview() {
        re = getResources();
        lin_jump = findViewById(R.id.lin_jump);
        if (jump) {
            linList = re.getStringArray(R.array.fwxy);
        }else{
            linList = re.getStringArray(R.array.yszc);
        }
    }

    // 设置最大宽度，看需要而定
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }
    // 设置最大高度，看需要而定
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }
    // 设置自定义动画器，看需要而定
    @Override
    protected PopupAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }
    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    protected int getPopupHeight() {
        return 0;
    }
}
