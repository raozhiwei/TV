package cn.wappt.m.apptv.views.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import cn.wappt.m.apptv.R;

/**
 * @author: wsq
 * @date: 2020/11/13
 * Description:
 */
public class Transparentutil {
    private static Transparentutil transparentutil;

    private Transparentutil(){
    }

    //调用生成的对象
    public static Transparentutil getInstance(){//方法无需同步，各线程同时访问
        if (transparentutil==null){
            synchronized (Transparentutil.class){
                if (transparentutil==null){
                    transparentutil=new Transparentutil();
                }
            }
        }
        return transparentutil;
    }



    //添加透明占位图
    public  void addTransParentStatusView(Activity activity, ViewGroup contentView){
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        //  statusBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.status));
        statusBarView.setBackgroundColor(activity.getResources().getColor(R.color.color_00000000));
        statusBarView.setLayoutParams(lp);
        contentView.addView(statusBarView, 0);

    }

        private  int getStatusBarHeight(Activity activity) {
            int result = 0;
            //获取状态栏高度的资源id
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = activity.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

}
