package codextreme.jimmyneutron.sidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import codextreme.jimmyneutron.R;

public class SidebarListAdapter extends BaseAdapter {

    List<String> mListEntries = new LinkedList<>();
    // temp. list holding the filtered items

    public SidebarListAdapter(Context context) {
    }

    public SidebarListAdapter setList(List<String> input) {
        mListEntries.clear();
        mListEntries.addAll(input);
        return this;
    }
    public SidebarListAdapter add(String entry) {
        mListEntries.add(entry);
        return this;
    }

    @Override
    public int getCount() {
        return mListEntries.size();
    }

    @Override
    public String getItem(int position) {
        try {
            return mListEntries.get(position);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SidebarListItemHolder holder;
        if (convertView == null) {
            convertView = new SidebarListItemHolder(parent.getContext());
        }
        holder = (SidebarListItemHolder) convertView;
        holder.setIcon(null); //TODO generate icon
        holder.setLabel(getItem(position));
        return convertView;
    }
}