package cn.wappt.m.apptv.interfaces;



import java.util.Map;

import cn.wappt.m.apptv.base.LoginReceive;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author: wsq
 * @date: 2020/9/2
 * Description:
 */
public interface LoginInterfaces {

    //登录
    @FormUrlEncoded
    @POST("/index.php/user/login")
    Call<LoginReceive> login(@Field("user_name") String user_name, @Field("user_pwd") String user_pwd);



}
