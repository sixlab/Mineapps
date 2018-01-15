package cn.sixlab.app.mineapps.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import cn.sixlab.app.mineapps.R
import kotlinx.android.synthetic.main.activity_scheme.*

class SchemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheme)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

        }

        val uri = intent.data
        val path = uri.path

        when (path){
            "assignment" -> {
                val year = uri.getQueryParameter("year")
                val month = uri.getQueryParameter("key")
                val day = uri.getQueryParameter("day")

                var intent = Intent(this, DailyAssignmentActivity::class.java)
                intent.putExtra("year",year)
                intent.putExtra("month",month)
                intent.putExtra("day",day)
                startActivity(intent)
            }
        }
    }

}
