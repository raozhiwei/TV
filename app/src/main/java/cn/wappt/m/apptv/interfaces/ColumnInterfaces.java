package cn.wappt.m.apptv.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author: wsq
 * @date: 2020/10/27
 * Description:排行榜接口
 */
public interface ColumnInterfaces {

    /**
     *   获取排行榜
     * @param n	数量
     * @param p	页码
     * @param t	分类
     * @return
     */
    @GET("/bdxs.php")
    Call<ResponseBody> bdxssort(@Query("n") int n, @Query("p") int p, @Query("t") int t);

}
