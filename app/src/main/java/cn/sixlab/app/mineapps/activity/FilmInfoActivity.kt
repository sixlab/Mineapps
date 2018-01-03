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

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import cn.sixlab.app.mineapps.R
import cn.sixlab.app.mineapps.util.HttpUtil
import cn.sixlab.app.mineapps.util.JsonUtil
import cn.sixlab.app.mineapps.util.ToastMsg
import kotlinx.android.synthetic.main.activity_film_info.*
import kotlinx.android.synthetic.main.content_film_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FilmInfoActivity : AppCompatActivity() {

    var searchText = ""
    var dbList:ArrayList<Map<Any, Any>>? = null

    var oldInfo:Map<Any, Any>? = null
    var alertDialog: AlertDialog? = null
    var dbKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_info)

        val extra = intent.getStringExtra("filmInfo")
        if(null==extra){
            toolbar.setTitle(R.string.apps_film_add)
        }else{
            toolbar.setTitle(R.string.apps_film_modify)

           oldInfo = JsonUtil.toBean(extra, Map::class.java) as Map<Any, Any>
        }

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            searchDb()
        }

        initInput(null)
    }

    fun searchDb(){
        val filmName = text_film_name.text

        if(filmName.toString() == searchText){
            showChoice()
        }else{
            val route = HttpUtil.buildDbRoute()
            val call = route.queryMovie(filmName)

            call.enqueue(object : Callback<Map<Any, Any>> {
                override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                    val body = response!!.body()
                    dbList = body!!["subjects"] as ArrayList<Map<Any, Any>>
                    searchText = filmName.toString()
                    showChoice()
                }

                override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                    ToastMsg.show(this@FilmInfoActivity,t)
                }
            })
        }
    }

    fun showChoice(){
        if(null != dbList){
            var arr = arrayOfNulls<String>(dbList!!.size)

            dbList!!.forEachIndexed { index, item ->
                var pubdates = item["pubdates"]
                var year = item["year"]
                var title = item["title"] as String

                if(year!=null){
                    title = "[$year] $title"
                }

                if(pubdates!=null){
                    title = "$title $pubdates"
                }
                arr[index] = title
            }

            alertDialog = AlertDialog.Builder(this@FilmInfoActivity).setTitle("选择电影")
                    .setSingleChoiceItems(arr, 0) { _, which ->
                        val item = dbList!![which]
                        if (null != item && null != item["id"]) {
                            choose(item["id"] as String)
                        }
                    }
                    .setPositiveButton(R.string.close) { _, _ ->

                    }
                    .setNegativeButton(R.string.cancle) { _, _ ->

                    }
                    .show()
        }
    }

    private fun choose(subjectId: String) {
        val route = HttpUtil.buildDbRoute()
        val call = route.selectMovie(subjectId)

        call.enqueue(object : Callback<Map<Any, Any>> {
            override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                val body = response!!.body()
                if(null!=body){
                    text_film_name.setText(body!!["title"] as String)
                    text_produce_year.setText(body!!["year"] as String)

                    val rating = body!!["rating"] as Map<Any, Any>
                    text_douban_score.setText(rating["average"].toString())

                    val directors = body!!["directors"] as List<Map<Any, Any>>
                    val directorArr = arrayOfNulls<String>(directors.size)
                    directors.forEachIndexed { index, item ->
                        directorArr[index] = item["name"] as String
                    }

                    text_director.setText(directorArr.joinToString("、"))

                    var remark = text_remark.text.toString()

                    if(null!=body["original_title"]){
                        remark += ( "<原名>：" + body["original_title"] as String )
                    }
                    if(null!=body["aka"]){
                        remark += ( "<别名>:" + (body["aka"] as List<String>).joinToString("、") )
                    }
                    if(null!=body["countries"]){
                        remark += ( "<国家>:" + (body["countries"] as List<String>).joinToString("、") )
                    }

                    text_remark.setText(remark)

                    dbKey = subjectId
                    if(alertDialog!=null){
                        alertDialog!!.hide()
                    }
                }
            }

            override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                ToastMsg.show(this@FilmInfoActivity,t)
            }
        })
    }

    fun initInput(v: View?){
        var filmName = ""
        var produceYear = ""
        var director = ""
        var cinema = ""
        var doubanScore = ""
        var remark = ""

        var date = Date()
        if(oldInfo != null){
            if(null!= oldInfo!!["movieName"]){
                filmName = oldInfo!!["movieName"] as String
            }

            if(null!= oldInfo!!["produceYear"]){
                produceYear = oldInfo!!["produceYear"] as String
            }

            if(null!= oldInfo!!["director"]){
                director = oldInfo!!["director"] as String
            }

            if(null!= oldInfo!!["remark"]){
                remark = oldInfo!!["remark"] as String
            }

            if(null!= oldInfo!!["cinema"]){
                cinema = oldInfo!!["cinema"] as String
            }

            if(null!= oldInfo!!["doubanScore"]){
                doubanScore = (oldInfo!!["doubanScore"] as Double).toString()
            }

            if(null!= oldInfo!!["viewDate"]){
                date = Date(oldInfo!!["viewDate"] as Long)
            }
        }

        val text = SimpleDateFormat("yyyy-MM-dd").format(date)
        if(TextUtils.isEmpty(produceYear)){
            produceYear = text.split("-")[0]
        }

        text_film_name.setText(filmName)

        text_director.setText(director)

        text_cinema.setText(cinema)
        text_douban_score.setText(doubanScore)

        text_remark.setText(remark)

        text_produce_year.setText(produceYear)
        text_view_date.setText(text)

    }

    fun submitFilm(v: View?){
        val map:HashMap<String, Any> = HashMap()

        val filmName = text_film_name.text
        if(TextUtils.isEmpty(filmName)){
            text_film_name.error = getString(R.string.error_field_required)
            text_film_name.requestFocus()
            return
        }
        map.put("movieName", filmName.toString())

        val produceYear = text_produce_year.text
        if(!TextUtils.isEmpty(produceYear)){
            map.put("produceYear", produceYear.toString())
        }

        val director = text_director.text
        if(!TextUtils.isEmpty(director)){
            map.put("director", director.toString())
        }

        val viewDate = text_view_date.text
        if(TextUtils.isEmpty(viewDate)){
            text_view_date.error = getString(R.string.error_field_required)
            text_view_date.requestFocus()
            return
        }else{
            val sdf = SimpleDateFormat("yyyy-MM-dd")

            map.put("viewDate", sdf.parse(viewDate.toString()))
        }

        val cinema = text_cinema.text
        if(!TextUtils.isEmpty(cinema)){
            map.put("cinema", cinema.toString())
        }

        val doubanScore = text_douban_score.text
        if(!TextUtils.isEmpty(doubanScore)){
            map.put("doubanScore", doubanScore.toString().toDouble())
            //            map.put("doubanKey", mUsername)
        }

        val remark = text_remark.text
        if(!TextUtils.isEmpty(remark)){
            map.put("remark", remark.toString())
        }

        if(null!=oldInfo){
            map.put("id", oldInfo!!["id"] as Int)
        }

        if(null!=dbKey){
            map.put("doubanKey", dbKey!!)
        }

        val body = HttpUtil.buildData(map)

        val route = HttpUtil.buildRoute(this)
        var call: Call<Map<Any, Any>>

        if(null!=oldInfo){
            call = route.modifyFilm(body, oldInfo!!["id"] as Int)
        }else{
            call = route.addFilm(body)
        }

        call.enqueue(object : Callback<Map<Any, Any>> {
            override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                submitSuccess(response)
            }

            override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                ToastMsg.show(this@FilmInfoActivity,t)
            }
        })

    }

    private fun submitSuccess(response: Response<Map<Any, Any>>?) {
        ToastMsg.show(this@FilmInfoActivity,"操作成功")

        if(null==oldInfo){
            val intent = Intent(this, FilmActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}
