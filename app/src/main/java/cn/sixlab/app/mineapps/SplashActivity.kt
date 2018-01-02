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
package cn.sixlab.app.mineapps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //判断是否登录，如果未登录，先去登录，然后再回来初始化
        val preferences = getSharedPreferences("cn.sixlab", Context.MODE_PRIVATE);
        val authentication = preferences.getString("Authentication", null)
        val exp = preferences.getLong("AuthenticationExp", 0)
        val now = Date().time - 30*60*1000;

        val intent: Intent
        if (authentication == null || exp < now) {
            intent = Intent(this, LoginActivity::class.java)
        }else{
            intent = Intent(this, MainActivity::class.java)

        }
        startActivity(intent)
        finish()
    }
}
