package cn.sixlab.app.mineapps.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import cn.sixlab.app.mineapps.R
import cn.sixlab.app.mineapps.util.HttpUtil
import cn.sixlab.app.mineapps.util.ToastMsg
import kotlinx.android.synthetic.main.activity_show_info.*
import kotlinx.android.synthetic.main.content_show_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ShowInfoActivity : AppCompatActivity() {

    var searchText = ""
    var dbList: ArrayList<Map<Any, Any>>? = null

    var oldInfo:Map<Any, Any>? = null
    var alertDialog: AlertDialog? = null
    var dbKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_info)

        val extra = intent.getStringExtra("showInfo")
        if(null==extra){
            toolbar.setTitle(R.string.apps_show_add)
        }else{
            toolbar.setTitle(R.string.apps_show_add)
            // 待实现
            oldInfo = HashMap()
        }

        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            searchDb()
        }

        initInput(null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun searchDb(){
        val showName = text_show_name.text

        if(showName.toString() == searchText){
            showChoice()
        }else{
            val route = HttpUtil.buildDbRoute()
            val call = route.queryMovie(showName)

            call.enqueue(object : Callback<Map<Any, Any>> {
                override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                    val body = response!!.body()
                    dbList = body!!["subjects"] as ArrayList<Map<Any, Any>>
                    searchText = showName.toString()
                    showChoice()
                }

                override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                    ToastMsg.show(this@ShowInfoActivity,t)
                }
            })
        }
    }

    private fun showChoice(){
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

            alertDialog = AlertDialog.Builder(this@ShowInfoActivity).setTitle("选择电视剧")
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
                    text_show_name.setText(body!!["title"] as String)

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
                ToastMsg.show(this@ShowInfoActivity,t)
            }
        })
    }

    fun initInput(v: View?){
        var showName = ""
        var showSeason = 1
        var showEpisode = 1
        var showRemark = ""

        var date = Date()
        val text = SimpleDateFormat("yyyy-MM-dd").format(date)

        text_show_name.setText(showName)
        text_show_season.setText(showSeason.toString())
        text_show_episode.setText(showEpisode.toString())
        text_remark.setText(showRemark)
        text_begin_date.setText(text)
    }

    fun submitShow(v: View?){
        val map:HashMap<String, Any> = HashMap()

        val showName = text_show_name.text
        if(TextUtils.isEmpty(showName)){
            text_show_name.error = getString(R.string.error_field_required)
            text_show_name.requestFocus()
            return
        }
        map.put("showName", showName.toString())

        val showSeason = text_show_season.text
        if(!TextUtils.isEmpty(showSeason)){
            map.put("showSeason", showSeason.toString().toInt())
        }else{
            map.put("showSeason", 1)
        }

        val showEpisode = text_show_episode.text
        if(!TextUtils.isEmpty(showEpisode)){
            map.put("showEpisode", showEpisode.toString().toInt())
        }else{
            map.put("showEpisode", 1)
        }

        val beginDate = text_begin_date.text
        if(TextUtils.isEmpty(beginDate)){
            text_begin_date.error = getString(R.string.error_field_required)
            text_begin_date.requestFocus()
            return
        }else{
            val sdf = SimpleDateFormat("yyyy-MM-dd")

            map.put("beginDate", sdf.parse(beginDate.toString()))
        }

        val remark = text_remark.text
        if(!TextUtils.isEmpty(remark)){
            map.put("remark", remark.toString())
        }

        if(null!=dbKey){
            map.put("doubanKey", dbKey!!)
        }

        val body = HttpUtil.buildData(map)

        val route = HttpUtil.buildRoute(this)
        var call: Call<Map<Any, Any>>

        call = route.addShow(body)

        call.enqueue(object : Callback<Map<Any, Any>> {
            override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                submitSuccess(response)
            }

            override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                ToastMsg.show(this@ShowInfoActivity,t)
            }
        })
    }

    private fun submitSuccess(response: Response<Map<Any, Any>>?) {
        ToastMsg.show(this@ShowInfoActivity,"操作成功")

        if(null==oldInfo){
            val intent = Intent(this, ShowActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}
