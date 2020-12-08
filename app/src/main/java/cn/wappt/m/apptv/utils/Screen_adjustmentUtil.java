package cn.wappt.m.apptv.utils;

public class Screen_adjustmentUtil {
    public  static  int CLASS_SCREEN=110;
    private static Screen_adjustmentUtil screen_adjustmentUtil;

    private Screen_adjustmentUtil(){
    }

    //调用生成的对象
    public static Screen_adjustmentUtil getInstance(){//方法无需同步，各线程同时访问
        if (screen_adjustmentUtil==null){
            synchronized (ReptilesRun.class){
                if (screen_adjustmentUtil==null){
                    screen_adjustmentUtil=new Screen_adjustmentUtil();
                }
            }
        }
        return screen_adjustmentUtil;
    }


}
