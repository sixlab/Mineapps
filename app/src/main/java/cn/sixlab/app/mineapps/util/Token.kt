package cn.sixlab.app.mineapps.util

import android.content.SharedPreferences
import java.util.*

/**
 * Copyright (c) 2018 Sixlab. All rights reserved.
 *
 * License information see the LICENSE file in the project's root directory.
 *
 * For more information, please see
 * https://sixlab.cn/
 *
 * @time: 2018/1/20 15:41
 * @author: Patrick <root@sixlab.cn>
 */
object Token {
    fun readToken(preferences: SharedPreferences): String {
        val authentication = preferences.getString("Authentication", "")
        val exp = preferences.getLong("AuthenticationExp", 0)
        val now = Date().time - 30*60*1000;

        if(authentication ==null || exp < now){
            return ""
        }
        return authentication;
    }
    fun token(preferences: SharedPreferences): String {
        val authentication = preferences.getString("Authentication", "")
        return authentication;
    }

    fun login(preferences: SharedPreferences, data: Map<Any, Any>, username: String, password: String){
        val token:String = data["token"] as String
        val exp:Long = data["expiration"] as Long

        val editor = preferences.edit()
        editor.putString("Authentication",token)
        editor.putString("username", username)
        editor.putString("password", password)
        editor.putLong("AuthenticationExp",exp)
        editor.commit()
    }

    fun refresh(preferences: SharedPreferences, data: Map<Any, Any>){
        val token:String = data["token"] as String
        val exp:Long = data["expiration"] as Long

        val editor = preferences.edit()
        editor.putString("Authentication",token)
        editor.putLong("AuthenticationExp",exp)
        editor.commit()
    }

    fun remove(preferences: SharedPreferences){
        val editor = preferences.edit()
        editor.remove("Authentication")
        editor.remove("AuthenticationExp")
        editor.commit()
    }
}