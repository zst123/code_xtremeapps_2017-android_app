package codextreme.jimmyneutron.tradbooking

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import codextreme.jimmyneutron.R
import codextreme.jimmyneutron.sidebar.SidebarListAdapter
import android.widget.AdapterView
import codextreme.jimmyneutron.Common
import codextreme.jimmyneutron.HomeActivity
import codextreme.jimmyneutron.baseline.BaselineMapFragment
import codextreme.jimmyneutron.baseline.BaselineMapFragment.*


public class CheckBookingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.fragment_booking, container, false)
        val listView = view?.findViewById(R.id.listView1) as ListView
        val adapter = TradBookingListAdapter(activity)
        (1..10).forEach { i -> adapter.add(TradBookingListAdapter.ItemHolder("Area $i")) }

        listView.adapter = adapter

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                val current_item = listView.adapter.getItem(pos) as TradBookingListAdapter.ItemHolder
                if (Common.DEBUG) {
                    Toast.makeText(activity, current_item.text, Toast.LENGTH_SHORT).show()
                }
                changeActivityFragment(current_item.text)
            }
        }
        return view
    }

    fun changeActivityFragment(title: String) {
        val active = activity as HomeActivity
        val fragmentOfficeView = BaselineMapFragment()
        val args = Bundle()
        //args.putString(BUNDLE_URL, URL_TEST);
        args.putString(BUNDLE_BASE64_IMG, Common.URL_BASELINE)
        args.putString(BUNDLE_TITLE, title)
        args.putString(BUNDLE_COORDINATES, Common.URL_COORDINATES)
        fragmentOfficeView.arguments = args
        active.changeFragment(fragmentOfficeView, true)
        //val sidebarList = activity.findViewById(R.id.sidebar_listview) as ListView


    }
}
