package cn.wappt.m.apptv.interfaces;

import java.util.Map;

import cn.wappt.m.apptv.base.LoginReceive;
import cn.wappt.m.apptv.base.RegistUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author: wsq
 * @date: 2020/9/2
 * Description:
 */
public interface





RegissterInterfaces {
    @POST("/user/reg")
    Call<LoginReceive> Regist(@Body Map map);
}
