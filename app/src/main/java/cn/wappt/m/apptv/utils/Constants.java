package cn.wappt.m.apptv.utils;

/**
 * @author: wsq
 * @date: 2020/9/2
 * Description:
 */
public class Constants {
    //url 服务器路径
    public static final String AppUrl="http://m.cqys.info/";
    //爬虫个人详细中心数据
    public static final String userInfo=AppUrl+"user/ajax_info.html";
    //爬虫个人修改的数据中心数据
    public static final String userModif=AppUrl+"index.php/user/info.html";
    //电影的信息
    public  static  final String userfilm=AppUrl+"vodtype/1.html";
    //电视剧信息
    public static  final String userPlay=AppUrl+"vodtype/2.html";
    //综艺信息
    public static  final String userVariety=AppUrl+"vodtype/3.html";
    //动漫信息
    public  static final String userComic=AppUrl+"vodtype/4.html";
    //登录验证码
    public static String imagcodeurl=AppUrl+"index.php/verify/index.html";

    public   static     int line=0;//线路

    //获取的数据进行解析
    public  static  String  dataAnalysis=AppUrl+"api.php/provide/vod";
    public  static String detailsHttp="voddetail/";
    public  static  String  Feedback=Constants.AppUrl+"index.php/gbook/saveData";
    public static  String DOC_XML_URL = AppUrl+"azsdd.xml"; //更新的接口
    public static String datazxdl = AppUrl+"zxdl.php";
}
