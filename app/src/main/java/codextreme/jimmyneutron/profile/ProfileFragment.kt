package codextreme.jimmyneutron.tradbooking

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import codextreme.jimmyneutron.Common
import codextreme.jimmyneutron.R
import im.dacer.androidcharts.BarView


public class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.fragment_profile, container, false)
        val userprofile_name = view?.findViewById(R.id.userprofile_name) as TextView
        userprofile_name.text = Common.mUsername

        val barView = view?.findViewById(R.id.bar_view) as BarView
        barView.setBottomTextList(arrayListOf("S", "M", "T", "W", "T", "F", "S"))
        val dataList = arrayListOf(10, 50, 70, 30, 60, 20, 80)
        barView.setDataList(dataList, 100)

        return view
    }
}
