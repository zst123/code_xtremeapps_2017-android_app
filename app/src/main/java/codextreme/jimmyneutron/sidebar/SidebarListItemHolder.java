package codextreme.jimmyneutron.sidebar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import codextreme.jimmyneutron.R;


public class SidebarListItemHolder extends LinearLayout {

    final TextView mLabel;
    final ImageView mIcon;

    public SidebarListItemHolder(Context context) {
        super(context);
        LayoutInflater inflator = LayoutInflater.from(context);
        inflator.inflate(R.layout.activity_home_drawer_left_listitem, this, true);

        mLabel = (TextView) findViewById(android.R.id.text1);
        mIcon = (ImageView) findViewById(android.R.id.icon);
        mLabel.setFocusable(false);
        mIcon.setFocusable(false);
        setFocusable(false);
    }


    public SidebarListItemHolder setIcon(Drawable drawable) {
        if (drawable != null) {
            mIcon.setImageDrawable(drawable);
        } else {
            mIcon.setImageResource(android.R.drawable.sym_def_app_icon);
        }
        return this;
    }

    public SidebarListItemHolder setIcon(int id) {
        mIcon.setImageResource(id);
        return this;
    }

    public SidebarListItemHolder setLabel(CharSequence str) {
        if (TextUtils.isEmpty(str)) {
            mLabel.setText(this.getContext().getResources().getString(android.R.string.unknownName));
        } else {
            mLabel.setText(str);
        }
        return this;
    }
}