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
package cn.sixlab.app.mineapps.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SearchView
import cn.sixlab.app.mineapps.R
import cn.sixlab.app.mineapps.util.HttpUtil
import cn.sixlab.app.mineapps.util.JsonUtil
import cn.sixlab.app.mineapps.util.ToastMsg
import kotlinx.android.synthetic.main.activity_film.*
import kotlinx.android.synthetic.main.content_film.*
import kotlinx.android.synthetic.main.snippet_film_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FilmActivity : AppCompatActivity() {

    var data:ArrayList<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var intent = Intent(this, FilmInfoActivity::class.java)
            startActivity(intent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        search_view.isIconified = true
        search_view.isFocusableInTouchMode = true
        search_view.isFocusable = true

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(TextUtils.isEmpty(newText)){
                    searchRecentlyFilm()
                }else if ("null" == newText){
                    searchDb()
                }else{
                    searchFilm(newText)
                }
                return true
            }
        })

        searchRecentlyFilm()
    }

    private fun searchDb(){
        val route = HttpUtil.buildRoute(this)
        val call = route.searchDb()

        call.enqueue(object : Callback<Map<Any, Any>> {
            override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                val body = response!!.body()
                data = body!!["data"] as ArrayList<Any>

                renderView()
            }

            override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                ToastMsg.show(this@FilmActivity,t)
            }
        })
    }

    private fun searchFilm(newText: String) {
        val route = HttpUtil.buildRoute(this)
        val call = route.searchFilms(newText)

        call.enqueue(object : Callback<Map<Any, Any>> {
            override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                val body = response!!.body()
                data = body!!["data"] as ArrayList<Any>

                renderView()
            }

            override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                ToastMsg.show(this@FilmActivity,t)
            }
        })
    }

    private fun searchRecentlyFilm() {
        val route = HttpUtil.buildRoute(this)
        val call = route.recentFilms(10)

        call.enqueue(object : Callback<Map<Any, Any>> {
            override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                val body = response!!.body()
                data = body!!["data"] as ArrayList<Any>

                renderView()
            }

            override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                ToastMsg.show(this@FilmActivity,t)
            }
        })
    }

    fun renderView(){
        film_result_list.removeAllViews()

        val inflater = LayoutInflater.from(this@FilmActivity)
        var count = 0
        data!!
                .map { it as Map<Any, Any> }
                .forEach {
                    val view = inflater.inflate(R.layout.snippet_film_list, null) as LinearLayout

                    val nameView = view.name_view
                    val introView = view.intro_view

                    count++
                    val movieName = it["movieName"]
                    val produceYear = it["produceYear"]

//                    nameView.text = count.toString()+" ["+it["produceYear"].toString()+"]"+it["movieName"].toString()
                    nameView.text = "${count.toString()}. ${movieName.toString()} [${produceYear.toString()}]"

                    var intro = "";
                    if(null!=it["viewDate"]){
                        val date = Date(it["viewDate"] as Long)
                        intro = "  日期："+SimpleDateFormat("yyyy-MM-dd").format(date)
                    }

                    if(null!=it["doubanScore"]){
                        intro += ("  评分"+it["doubanScore"])
                    }

                    introView.text = intro

                    var item = it
                    view.setOnClickListener {
                        var intent = Intent(this, FilmInfoActivity::class.java)
                        intent.putExtra("filmInfo",JsonUtil.toJson(item));
                        startActivity(intent)
                    }

                    film_result_list.addView(view)
                }
    }
}
