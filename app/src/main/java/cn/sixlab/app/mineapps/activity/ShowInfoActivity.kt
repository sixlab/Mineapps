package cn.sixlab.app.mineapps.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import cn.sixlab.app.mineapps.R

import kotlinx.android.synthetic.main.activity_show_info.*

class ShowInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_info)

        val extra = intent.getStringExtra("showInfo")
        if(null==extra){
            toolbar.setTitle(R.string.apps_show_add)
        }else{
            toolbar.setTitle(R.string.apps_show_modify)
        }

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
