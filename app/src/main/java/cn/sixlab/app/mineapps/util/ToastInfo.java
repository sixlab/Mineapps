/**
 * Copyright (c) 2018 Sixlab. All rights reserved.
 * <p>
 * License information see the LICENSE file in the project's root directory.
 * <p>
 * For more information, please see
 * https://sixlab.cn/
 *
 * @time: 2018/1/2 17:23
 * @author: Patrick <root@sixlab.cn>
 */
package cn.sixlab.app.mineapps.util;

import android.content.Context;
import android.widget.Toast;

public class ToastInfo {
    public static void show(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
