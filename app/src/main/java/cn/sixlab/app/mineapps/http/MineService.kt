/**
 * Copyright (c) 2017 Sixlab. All rights reserved.
 *
 * License information see the LICENSE file in the project's root directory.
 *
 * For more information, please see
 * https://sixlab.cn/
 *
 * @time: 2017
 * @author: Patrick <root@sixlab.cn>
 */
package cn.sixlab.app.mineapps.http

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface MineService {

    //    @GET("api/movie/search/{keyword}")
    //    SixlabMovies searchMovie(@Path("keyword") String keyword);
    //    SixlabMovies searchMovie(@Query("keyword") String keyword);
    //    Call<ResponseBody> get();

    //    @POST("api/movie/search")
    //    Call<ResponseBody> queryMovie(@Query("keyword")String keyword);

    //登录
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("login")
    fun login(@Body body: RequestBody): Call<Map<Any, Any>>

    //添加电影
    @POST("movie/film")
    fun addFilm(@Body body: RequestBody): Call<Map<Any, Any>>





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