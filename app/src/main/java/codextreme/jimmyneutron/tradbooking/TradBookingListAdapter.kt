package codextreme.jimmyneutron.tradbooking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import codextreme.jimmyneutron.R

public class TradBookingListAdapter(context: Context) : BaseAdapter() {
    val mInflator: LayoutInflater = LayoutInflater.from(context)

    var sList: MutableList<ItemHolder> = mutableListOf()

    override fun getCount(): Int {
        return sList.size
    }

    override fun getItem(position: Int): Any {
        return sList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun add(item: ItemHolder): TradBookingListAdapter {
        sList.add(item)
        return this
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val vh: ViewHolder
        if (convertView == null) {
            view = this.mInflator.inflate(android.R.layout.simple_list_item_1, parent, false)
            vh = ViewHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        vh.label.text = sList[position].text
        return view
    }

    public class ItemHolder(text1: String) {
        var text: String = text1
    }

    private class ViewHolder(row: View?) {
        public val label: TextView = row?.findViewById(android.R.id.text1) as TextView

    }

}