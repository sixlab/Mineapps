/**
 * Copyright (c) 2018 Sixlab. All rights reserved.
 *
 * License information see the LICENSE file in the project's root directory.
 *
 * For more information, please see
 * https://sixlab.cn/
 *
 * @time: 2018/1/3 16:34
 * @author: Patrick <root@sixlab.cn>
 */
package cn.sixlab.app.mineapps.util

interface MineCallback<in T> {
    fun run(param:T)
}