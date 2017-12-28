package cn.sixlab.app.mineapps.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by patrick on 2017/12/27.
 */
interface DbService {

    @GET("v2/movie/search")
    fun queryMovie(@Query("q") q: CharSequence): Call<Map<*, *>>

    @GET("v2/movie/subject/{subject}")
    fun selectMovie(@Path("subject") subject: String): Call<Map<*, *>>
}