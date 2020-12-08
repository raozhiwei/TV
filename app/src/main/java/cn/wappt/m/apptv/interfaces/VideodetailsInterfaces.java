package cn.wappt.m.apptv.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author: wsq
 * @date: 2020/12/7
 * Description:
 */
public interface VideodetailsInterfaces {

    /**获取详情
     * @return
     */
    @POST("/api.php/provide/vod/?ac=detail")
    Call<ResponseBody> detail(@Query("ids") int idsp);

    /**喜欢的视频
     * @return
     */
    @POST("/cnxh.php")
    Call<ResponseBody> cnxh(
             @Query("t") String t
            , @Query("s") String s);





}
