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
package cn.sixlab.app.mineapps.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

object JsonUtil{
    private val mapper = ObjectMapper()
    init {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    fun toJson(obj: Any?): String? {
        try {
            return mapper.writeValueAsString(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "{}"
    }

    fun <T> toBean(content: String, clz: Class<T>): T? {
        try {
            return mapper.readValue(content, clz)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun <T> obj2Bean(obj: Any, clz: Class<T>): T? {
        try {
            val json = mapper.writeValueAsString(obj)
            if (null != json && "" != json) {
                return mapper.readValue(json, clz)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}