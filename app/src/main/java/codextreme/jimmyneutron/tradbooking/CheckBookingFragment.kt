package codextreme.jimmyneutron.tradbooking

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import codextreme.jimmyneutron.R
import codextreme.jimmyneutron.sidebar.SidebarListAdapter

public class CheckBookingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.fragment_booking, container, false)
        val listView = view?.findViewById(R.id.listView1) as ListView
        val adapter = TradBookingListAdapter(activity)
        adapter.add(TradBookingListAdapter.ItemHolder("Desk 1"))
        adapter.add(TradBookingListAdapter.ItemHolder("Desk 2"))
        listView.adapter = adapter

        return view
    }
}
