/**
 * Copyright (c) 2018 Sixlab. All rights reserved.
 *
 * License information see the LICENSE file in the project's root directory.
 *
 * For more information, please see
 * https://sixlab.cn/
 *
 * @time: 2018/1/2 17:24
 * @author: Patrick <root@sixlab.cn>
 */
package cn.sixlab.app.mineapps.util

import android.content.Context
import android.widget.Toast

object ToastMsg{
    fun show(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
    fun show(context: Context, t: Throwable?) {
        Toast.makeText(context, t!!.message, Toast.LENGTH_LONG).show()
    }
}