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




public class AnalyticsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.fragment_analytics, container, false)

        val clockPieView = view?.findViewById(R.id.clock_pie_view) as ClockPieView
        randomSet(clockPieView)

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
}
