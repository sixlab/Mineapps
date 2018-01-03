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
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import cn.sixlab.app.mineapps.R
import cn.sixlab.app.mineapps.util.HttpUtil
import cn.sixlab.app.mineapps.util.MineCallback
import cn.sixlab.app.mineapps.util.ToastMsg
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.content_show.*
import kotlinx.android.synthetic.main.snippet_show_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ShowActivity : AppCompatActivity() {

    var status: String = "20"
    var data: java.util.ArrayList<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var intent = Intent(this, ShowInfoActivity::class.java)
            intent.putExtra("showInfo","showInfo")
            startActivity(intent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val list = ArrayList<String>()
        list.add("收看")
        list.add("已停")
        list.add("全部")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list)
        search_dropdown.adapter = adapter
        search_dropdown.setSelection(0)

        search_dropdown.onItemSelectedListener = object : OnItemSelectedListener {
            // parent： 为控件Spinner
            // view：显示文字的TextView
            // position：下拉选项的位置从0开始
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //获取Spinner控件的适配器
                //                val adapter = parent.adapter as ArrayAdapter<String>
                //                adapter.getItem(position)
                when(position){
                    0 -> status = "20"
                    1 -> status = "30"
                    2 -> status = ""
                }

                searchShow(null)
            }

            //没有选中时的处理
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        search_view.isIconified = true
        search_view.isFocusableInTouchMode = true
        search_view.isFocusable = true

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchShow(newText)
                return true
            }
        })
    }

    private fun searchShow(keyword: String?) {
        val route = HttpUtil.buildRoute(this)
        val call = route.searchShows(keyword ?: "", status)

        call.enqueue(object : Callback<Map<Any, Any>> {
            override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                val body = response!!.body()
                data = body!!["data"] as java.util.ArrayList<Any>

                renderView()
            }

            override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                ToastMsg.show(this@ShowActivity,t)
            }
        })
    }

    private fun renderView(){
        show_result_list.removeAllViews()

        val inflater = LayoutInflater.from(this@ShowActivity)
        var count = 0
        data!!
                .map { it as Map<Any, Any> }
                .forEach {
                    count++
                    val view = inflater.inflate(R.layout.snippet_show_list, null) as LinearLayout

                    val showId = it["id"] as Int
                    val showName = it["showName"]
                    view.name_view.text = "${count.toString()}. ${showName.toString()}"

                    val viewStatus = it["viewStatus"].toString()
                    when(viewStatus){
                        "20" -> view.change_status.text = "停看"
                        "30" -> view.change_status.text = "恢复"
                    }

                    val showSeason = it["showSeason"] as Int

                    view.add_s.text = String.format("S %02d",showSeason)

                    val showEpisode = it["showEpisode"] as Int

                    view.add_e.text = String.format("E %02d",showEpisode)

                    //                    var item = it
                    //                    view.setOnClickListener {
                    //                        var intent = Intent(this, ShowInfoActivity::class.java)
                    //                        intent.putExtra("showInfo", JsonUtil.toJson(item));
                    //                        startActivity(intent)
                    //                    }

                    view.add_s.setOnClickListener {
                        promptDialog("请输入进度：季",showSeason+1, object : MineCallback<String> {
                            override fun run(param: String) {
                                if(!TextUtils.isEmpty(param)){
                                    updateSeason(showId, param)
                                }
                            }
                        })
                    }

                    view.add_e.setOnClickListener {
                        promptDialog("请输入进度：集",showEpisode+1, object : MineCallback<String> {
                            override fun run(param: String) {
                                if(!TextUtils.isEmpty(param)){
                                    updateEpisode(showId, param)
                                }
                            }
                        })
                    }

                    view.change_status.setOnClickListener {
                        updateStatus(showId, viewStatus)
                    }

                    show_result_list.addView(view)
                }
    }

    private fun updateSeason(showId: Int, param: String) {
        if(!TextUtils.isEmpty(param)){
            val route = HttpUtil.buildRoute(this)
            val call = route.updateSeason(showId, param.toInt())

            call.enqueue(object : Callback<Map<Any, Any>> {
                override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                    //                    val body = response!!.body()
                    ToastMsg.show(this@ShowActivity,"操作成功")
                }

                override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                    ToastMsg.show(this@ShowActivity,t)
                }
            })
        }
    }

    private fun updateEpisode(showId: Int, param: String) {
        if(!TextUtils.isEmpty(param)){
            val route = HttpUtil.buildRoute(this)
            val call = route.updateEpisode(showId, param.toInt())

            call.enqueue(object : Callback<Map<Any, Any>> {
                override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                    //                    val body = response!!.body()
                    ToastMsg.show(this@ShowActivity,"操作成功")
                }

                override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                    ToastMsg.show(this@ShowActivity,t)
                }
            })
        }
    }

    private fun updateStatus(showId: Int, param: String) {
        if(!TextUtils.isEmpty(param)){
            val route = HttpUtil.buildRoute(this)
            val call = route.updateStatus(showId, param)

            call.enqueue(object : Callback<Map<Any, Any>> {
                override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                    //                    val body = response!!.body()
                    ToastMsg.show(this@ShowActivity,"操作成功")
                }

                override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                    ToastMsg.show(this@ShowActivity,t)
                }
            })
        }
    }

    private fun promptDialog(title: String, showNumber: Int, callback: MineCallback<String>) {
        //        val li = LayoutInflater.from(this@ShowActivity)
        //        val view = li.inflate(R.layout.snippet_dialog_input, null)

        val view = EditText(this)
        view.setText(showNumber.toString())
        view.setSelectAllOnFocus(true)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setView(view)
        builder.setPositiveButton("确定"){ dialog, which ->
            callback.run(view.text.toString())
        }
        builder.create().show()
        view.requestFocus()
    }
}
