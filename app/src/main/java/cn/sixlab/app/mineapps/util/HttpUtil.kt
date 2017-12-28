package cn.sixlab.app.mineapps.util

import cn.sixlab.app.mineapps.http.MineService
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory



/**
 * Created by patrick on 2017/12/27.
 */
object HttpUtil {
    var mine = "https://sixlab.cn/"
    var douban = "https://api.douban.com/"

    fun buildRoute(auth:Boolean): MineService {
        val builder = Retrofit.Builder()
        builder.baseUrl(HttpUtil.mine)
        builder.addConverterFactory(JacksonConverterFactory.create())
        if(auth){
            builder.client(apiToken())
        }
        val retrofit = builder.build()

        return retrofit.create(MineService::class.java)
    }

    fun buildData(data:Any): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.toJson(data))
    }

    private fun apiToken(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                    .header("Authorization", "no-cache")
                    .method(original.method(), original.body())
                    .build()

            chain.proceed(request)
        }
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }
}