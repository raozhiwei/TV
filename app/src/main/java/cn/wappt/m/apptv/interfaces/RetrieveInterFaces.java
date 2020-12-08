package cn.wappt.m.apptv.interfaces;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @author: wsq
 * @date: 2020/9/7
 * Description:
 */
public interface RetrieveInterFaces {
//    @Query("user_name") String user_name,
//    @Query("user_question") String user_question,
//    @Query("user_answer") String user_answer,
//    @Query("user_pwd") String user_pwd,
//    @Query("user_pwd2") String user_pwd2,
//    @Query("verify") String verify
    /**
     * 找回密码
     * @param
     * @return
     */
     @POST("")
    Call<Map>  findpass(@Url String url);


    @POST("/index.php/user/findpass")
    @Headers({ "Content-Type: application/json"})
    Call<Map> Regist(@Body RequestBody requestBody);



}
