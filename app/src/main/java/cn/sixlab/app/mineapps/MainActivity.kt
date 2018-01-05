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

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import cn.sixlab.app.mineapps.activity.FilmActivity
import cn.sixlab.app.mineapps.activity.FilmInfoActivity
import cn.sixlab.app.mineapps.activity.ShowActivity
import cn.sixlab.app.mineapps.activity.ShowInfoActivity
import cn.sixlab.app.mineapps.ft.AppsFragment
import cn.sixlab.app.mineapps.ft.HomeFragment
import cn.sixlab.app.mineapps.ft.MineFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),FragmentListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        initView(item)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //BottomNavigationViewHelper.formatDate(navigation)
        initView(navigation.menu.getItem(1))
        navigation.selectedItemId = navigation.menu.getItem(1).itemId
    }

    var context: Context? = null
    var preFt: Fragment? = null
    var preItemId: Int? = null
    var themeId: Int = 0

    private fun initView(item:MenuItem){
        val itemId = item.itemId
        if (preItemId != null && itemId == preItemId) {
            return
        }
        val clz: Class<*>
        when (itemId) {
            R.id.navigation_home -> clz = HomeFragment::class.java
            R.id.navigation_mine -> clz = MineFragment::class.java
//            R.id.navigation_mine -> clz = WatchedFragment::class.java
            else -> clz = AppsFragment::class.java
        }

        val ft = fragmentManager.beginTransaction()
        if (preFt != null) {
            ft.detach(preFt)
        }

        val bundle = Bundle()
        bundle.putInt("args", themeId)

        val name = clz.name
        val fragment = Fragment.instantiate(context, name, bundle)
        ft.add(R.id.ftContainer, fragment, name)
        preFt = fragment
        preItemId = itemId
        ft.commit()
    }

//    private fun callFragment(position:Int){
//        fragmentManager.beginTransaction()
//                .replace(R.id.ftContainer, ItemFragment.newInstance(position))
//                .commit();
//    }
}
