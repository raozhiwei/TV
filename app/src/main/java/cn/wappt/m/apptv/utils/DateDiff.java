package cn.wappt.m.apptv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author: wsq
 * @date: 2020/10/28
 * Description:
 */
public class DateDiff {
    private static DateDiff dateDiff;

    private DateDiff(){
    }

    //调用生成的对象
    public static DateDiff getInstance(){//方法无需同步，各线程同时访问
        if (dateDiff==null){
            synchronized (ReptilesRun.class){
                if (dateDiff==null){
                    dateDiff=new DateDiff();
                }
            }
        }
        return dateDiff;
    }
    public long dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            System.out.println("时间相差：" + day + "天" + hour + "小时" + min
                    + "分钟" + sec + "秒。");
            if (day>=1) {
                return day;
            }else {
                if (day==0) {
                    return 0;
                }else {
                    return 0;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

}
