package codextreme.jimmyneutron.tradbooking

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import codextreme.jimmyneutron.Common
import codextreme.jimmyneutron.R
import im.dacer.androidcharts.BarView
import im.dacer.androidcharts.ClockPieHelper
import im.dacer.androidcharts.ClockPieView
import im.dacer.androidcharts.LineView






public class AnalyticsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.fragment_analytics, container, false)

        val clockPieView = view?.findViewById(R.id.clock_pie_view) as ClockPieView
        randomSet(clockPieView)

        val lineView = view?.findViewById(R.id.line_view) as LineView
        initLineView(lineView)

        return view
    }


    private fun randomSet(clockPieView: ClockPieView) {
        val clockPieHelperArrayList = ArrayList<ClockPieHelper>()
        for (i in 0..19) {
            val startHour = (24 * Math.random()).toInt()
            val startMin = (60 * Math.random()).toInt()
            val duration = (50 * Math.random()).toInt()
            clockPieHelperArrayList.add(ClockPieHelper(startHour, startMin, 0, startHour, startMin + duration, 0))
        }
        clockPieView.setDate(clockPieHelperArrayList)
    }

    private fun initLineView(lineView: LineView) {
        val test = ArrayList<String>()
        for (i in 0..8) {
            test.add((i + 1).toString())
        }
        lineView.setBottomTextList(test)
        lineView.setColorArray(intArrayOf(Color.parseColor("#F44336"), Color.parseColor("#9C27B0"), Color.parseColor("#2196F3"), Color.parseColor("#009688")))
        lineView.setDrawDotLine(true)
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE)

        val dataList = arrayListOf<Int>()
        var random = (Math.random() * 9 + 1).toFloat()
        for (i in 0..8) {
            dataList.add((Math.random() * random).toInt())
        }

        val dataList2 = arrayListOf<Int>()
        random = (Math.random() * 9 + 1).toInt().toFloat()
        for (i in 0..8) {
            dataList2.add((Math.random() * random).toInt())
        }

        val dataList3 = arrayListOf<Int>()
        random = (Math.random() * 9 + 1).toInt().toFloat()
        for (i in 0..8) {
            dataList3.add((Math.random() * random).toInt())
        }

        val dataLists = arrayListOf(dataList, dataList2, dataList3)
        lineView.setDataList(dataLists)

    }
}
