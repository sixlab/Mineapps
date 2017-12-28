package cn.sixlab.app.mineapps

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import cn.sixlab.app.mineapps.activity.*
import cn.sixlab.app.mineapps.ft.AppsFragment
import cn.sixlab.app.mineapps.ft.HomeFragment
import cn.sixlab.app.mineapps.ft.MineFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


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
        Log.e("sixlab", " onCreate main" )
        setContentView(R.layout.activity_main)
        context = this

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //BottomNavigationViewHelper.formatDate(navigation)
        initView(navigation.menu.getItem(1))

        //判断是否登录，如果未登录，先去登录，然后再回来初始化
        val preferences = getSharedPreferences("cn.sixlab", Context.MODE_PRIVATE);
        val authentication = preferences.getString("Authentication", null)
        val exp = preferences.getLong("AuthenticationExp", 0)
        val now = Date().time - 30*60*1000;
        if (authentication == null || exp < now) {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
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

    fun itemClick(v: View?) {
        var intent:Intent? = null
        when (v?.id) {
            R.id.apps_film_add -> intent = Intent(this, FilmInfoActivity::class.java)
            R.id.apps_show_add -> intent = Intent(this, ShowInfoActivity::class.java)
            R.id.apps_film -> intent = Intent(this, FilmActivity::class.java)
            R.id.apps_show -> intent = Intent(this, ShowActivity::class.java)
        }

        if(null!=intent){
            startActivity(intent)
        }
    }

//    private fun callFragment(position:Int){
//        fragmentManager.beginTransaction()
//                .replace(R.id.ftContainer, ItemFragment.newInstance(position))
//                .commit();
//    }
}
