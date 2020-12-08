package cn.wappt.m.apptv.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author: wsq
 * @date: 2020/11/2
 * Description:
 */
public interface RecommendInterdaces {
     //获取分类数据
    @GET("/sjtj.php")
    Call<ResponseBody> sjtjsort(@Query("p") int p, @Query("pd") int pd, @Query("s") int s, @Query("tj") int tj);

    //获取推荐数据
    @GET("/sjtj.php")
    Call<ResponseBody> sjtjtj(@Query("p") int p, @Query("tj") int tj,@Query("s") int s);
    //获取轮播图·
    @GET("/sjtj.php")
    Call<ResponseBody> sjtjlbt( @Query("tj") int tj);

    //获取分类的id数据
    @POST("/api.php/provide/vod/?ac=list")
    Call<ResponseBody> classificationid();



}
