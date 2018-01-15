package cn.sixlab.app.mineapps.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.LinearLayout
import cn.sixlab.app.mineapps.R
import cn.sixlab.app.mineapps.util.HttpUtil
import cn.sixlab.app.mineapps.util.ToastMsg
import kotlinx.android.synthetic.main.activity_daily_assignment.*
import kotlinx.android.synthetic.main.content_daily_assignment.*
import kotlinx.android.synthetic.main.snippet_assignment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DailyAssignmentActivity : AppCompatActivity() {

    var data:ArrayList<Any>? = null
    private var year: String? = null
    private var month: String? = null
    private var day: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_assignment)
        setSupportActionBar(toolbar)

        initData()

        fab.setOnClickListener {
            DatePickerDialog(this@DailyAssignmentActivity, { _, selectYear, monthOfYear, dayOfMonth ->
                if(year!!.toInt() != selectYear || month!!.toInt() != monthOfYear || day!!.toInt() != dayOfMonth){
                    year = selectYear.toString()
                    month = (monthOfYear+1).toString()
                    day = dayOfMonth.toString()

                    initView()
                }
            }, year!!.toInt(), month!!.toInt()-1, day!!.toInt()).show()
        }

        initView()
    }

    private fun initData(){
        year = intent.getStringExtra("year")
        month = intent.getStringExtra("month")
        day = intent.getStringExtra("day")

        if(TextUtils.isEmpty(year) || TextUtils.isEmpty(month) || TextUtils.isEmpty(day) ){
            val calendar = Calendar.getInstance()

            year = calendar.get(Calendar.YEAR).toString()
            month = (calendar.get(Calendar.MONTH)+1).toString()
            day = calendar.get(Calendar.DAY_OF_MONTH).toString()
        }
    }

    private fun initView() {
        date_view.text = "$year-$month-$day"

        val route = HttpUtil.buildRoute(this)
        val call: Call<Map<Any, Any>>

        call = route.assignment(year!!, month!!, day!!)

        call.enqueue(object : Callback<Map<Any, Any>> {
            override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                val body = response!!.body()
                data = body!!["data"] as ArrayList<Any>

                renderView()
            }

            override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                ToastMsg.show(this@DailyAssignmentActivity,t)
            }
        })
    }

    private fun renderView() {
        assignment_list.removeAllViews()

        val inflater = LayoutInflater.from(this@DailyAssignmentActivity)
        var count = 0

        data!!
                .map { it as ArrayList<Any> }
                .forEach {
                    it
                            .map { it as Map<Any, Any> }
                            .forEach {
                                count++

                                val view = inflater.inflate(R.layout.snippet_assignment, null) as LinearLayout

                                val nameView = view.name_view
                                val statusView = view.status_view

                                val assignmentHour = it["assignmentHour"] as Int
                                val assignmentName = it["assignmentName"] as String
                                val finishCheck = it["finishCheck"] as Boolean
                                val id = it["id"] as Int

                                nameView.text = "$count. [$assignmentHour]$assignmentName"
                                statusView.isChecked = finishCheck

                                statusView.setOnCheckedChangeListener({ _, isChecked ->
                                    changeStatus(id, isChecked)
                                })

                                assignment_list.addView(view)
                            }
                }
    }

    private fun changeStatus(id: Int, checked: Boolean){
        val route = HttpUtil.buildRoute(this)
        val call: Call<Map<Any, Any>>

        call = route.finish(id, checked)

        call.enqueue(object : Callback<Map<Any, Any>> {
            override fun onResponse(call: Call<Map<Any, Any>>?, response: Response<Map<Any, Any>>?) {
                initView()
            }

            override fun onFailure(call: Call<Map<Any, Any>>?, t: Throwable?) {
                ToastMsg.show(this@DailyAssignmentActivity,t)
            }
        })
    }

}
