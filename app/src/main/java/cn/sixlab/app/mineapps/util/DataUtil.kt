/**
 * Copyright (c) 2018 Sixlab. All rights reserved.
 *
 * License information see the LICENSE file in the project's root directory.
 *
 * For more information, please see
 * https://sixlab.cn/
 *
 * @time: 2018/1/20 14:19
 * @author: Patrick <root@sixlab.cn>
 */
package cn.sixlab.app.mineapps.util

import java.util.*

object DataUtil {

    fun getItem(data:ArrayList<Any>?, id:Int): Map<Any, Any>? {
        data!!
                .map { it as Map<Any, Any> }
                .forEach {
                    val itemId = it["id"] as Int
                    if(id == itemId){
                        return it;
                    }
                }
        return null;
    }

    fun updateItem(data:ArrayList<Any>?, id: Int,newData: Map<Any, Any>) {
        data!!
                .map { it as MutableMap<Any, Any> }
                .forEach {
                    val itemId = it["id"] as Int
                    if(id == itemId){
                        val oldIt = it
                        newData.map {
                            val key = it.key
                            val value = it.value
                            oldIt[key] = value
                        }
                    }
                }
    }
}