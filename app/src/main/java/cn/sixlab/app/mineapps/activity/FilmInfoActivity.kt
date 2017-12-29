package cn.sixlab.app.mineapps.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import cn.sixlab.app.mineapps.R

import kotlinx.android.synthetic.main.activity_film_info.*

class FilmInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_info)

        val extra = intent.getStringExtra("filmInfo")
        if(null==extra){
            toolbar.setTitle(R.string.apps_film_add)
        }else{
            toolbar.setTitle(R.string.apps_film_modify)
        }

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    fun submitFilm(v: View?){

    }
}
