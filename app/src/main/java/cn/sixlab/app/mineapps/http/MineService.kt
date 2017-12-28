package cn.sixlab.app.mineapps.http

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by patrick on 2017/12/27.
 */
interface MineService {

    //    @GET("api/movie/search/{keyword}")
    //    SixlabMovies searchMovie(@Path("keyword") String keyword);
    //    SixlabMovies searchMovie(@Query("keyword") String keyword);
    //    Call<ResponseBody> get();

    //    @POST("api/movie/search")
    //    Call<ResponseBody> queryMovie(@Query("keyword")String keyword);

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("login")
    fun login(@Body body: RequestBody): Call<Map<Any, Any>>

    @POST("api/movie/search")
    fun queryMovie(@Query("keyword") keyword: String): Call<Map<Any, Any>>

    @POST("api/movie/add")
    fun addMovie(@Query("movieName") movieName: String, @Query("produceYear") produceYear: String, @Query("director") director: String, @Query("remark") remark: String, @Query("viewDate") viewDate: String, @Query("doubanScore") doubanScore: Double, @Query("doubanKey") doubanKey: String): Call<Map<Any, Any>>

    @POST("api/show/search")
    fun queryShow(@Query("keyword") keyword: String): Call<Map<Any, Any>>

    @POST("api/show/watching")
    fun queryWatching(@Query("keyword") keyword: String): Call<Map<Any, Any>>

    @POST("api/show/watched")
    fun queryWatched(@Query("keyword") keyword: String): Call<Map<Any, Any>>

    @POST("api/show/season/add")
    fun addSeason(@Query("id") id: Int): Call<Map<Any, Any>>

    @POST("api/show/episode/add")
    fun addEpisode(@Query("id") id: Int): Call<Map<Any, Any>>

    @POST("api/show/end")
    fun endShow(@Query("id") id: Int): Call<Map<Any, Any>>

    @POST("api/show/finish")
    fun finishShow(@Query("id") id: Int): Call<Map<Any, Any>>

    @POST("api/show/view/status")
    fun changeViewStatus(@Query("id") id: Int, @Query("status") status: String): Call<Map<Any, Any>>

    @POST("api/show/add")
    fun addShow(@Query("showName") showName: String, @Query("showSeason") showSeason: Int, @Query("showEpisode") showEpisode: Int, @Query("beginDate") beginDate: String, @Query("remark") remark: String, @Query("tv") tv: String, @Query("doubanKey") doubanKey: String): Call<Map<Any, Any>>

}