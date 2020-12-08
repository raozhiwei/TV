package cn.wappt.m.apptv.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author: wsq
 * @date: 2020/10/20
 * Description:
 */
public interface SortInterfaces {
    //获取分类数据
    @GET("zxdl.php")
    Call<ResponseBody> zxdlsort(@Query("s") int s, @Query("p") int p, @Query("y") String y, @Query("pd") int pd, @Query("t") int t, @Query("g") String g);

    //获取分类数据
    @GET("zxlx.php")
    Call<ResponseBody> zxlxsort(@Query("s") int s, @Query("p") int p, @Query("y") String y, @Query("pd") int pd, @Query("t") int t, @Query("g") String g);
}
