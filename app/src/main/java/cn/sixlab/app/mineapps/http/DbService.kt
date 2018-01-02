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

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DbService {

    @GET("v2/movie/search")
    fun queryMovie(@Query("q") q: CharSequence): Call<Map<*, *>>

    @GET("v2/movie/subject/{subject}")
    fun selectMovie(@Path("subject") subject: String): Call<Map<*, *>>
}