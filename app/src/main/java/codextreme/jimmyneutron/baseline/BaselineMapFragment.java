package codextreme.jimmyneutron.baseline;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mingle.sweetpick.BlurEffect;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.squareup.picasso.Picasso;

import codextreme.jimmyneutron.Common;
import codextreme.jimmyneutron.R;

import static codextreme.jimmyneutron.Common.percent_of_in_range;

// http://www.vogella.com/tutorials/AndroidFragments/article.html
// https://developer.android.com/training/basics/fragments/fragment-ui.html
public class BaselineMapFragment extends Fragment {
    public static final String BUNDLE_URL = "url";
    private BaselineMapView imageView;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baseline, container, false);
        imageView = (BaselineMapView) view.findViewById(R.id.view_baseline_map);

        linearLayout = (LinearLayout) view.findViewById(android.R.id.home);

        SeekBar seekbar = (SeekBar) view.findViewById(android.R.id.secondaryProgress);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float imageRatio = imageView.getDrawable().getIntrinsicHeight() * 1.0f / imageView.getDrawable().getIntrinsicWidth();
                float minHeight = linearLayout.getWidth() * imageRatio;
                int maxHeight = linearLayout.getHeight();

                float progress = seekBar.getProgress() * 0.01f;
                imageView.getLayoutParams().height = (int) percent_of_in_range(minHeight, maxHeight, progress);
                imageView.requestLayout();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        imageView.setDeskClickListener(new BaselineMapView.DeskClickListener() {
            @Override
            public void onDeskClick(BaselineMapView.DeskHolder desk) {
                showDialog(getActivity(), desk.name);
            }
        });

        return view;
    }


    public void showDialog(Context thiz, String text) {
        // SweetSheet 控件,根据 rl 确认位置
        SweetSheet mSweetSheet3 = new SweetSheet((FrameLayout) getActivity().getWindow().getDecorView());
        //mSweetSheet3.setBackgroundEffect(new BlurEffect(8));

        View view = LayoutInflater.from(thiz).inflate(R.layout.dialog_baseline_popup, null, false);

        //定义一个 CustomDelegate 的 Delegate ,并且设置它的出现动画.
        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangLayoutAnimation);
        customDelegate.setCustomView(view);
        mSweetSheet3.setDelegate(customDelegate);

        ((TextView)view.findViewById(android.R.id.text1)).setText(text);
        mSweetSheet3.show();

        /*
        //因为使用了 CustomDelegate 所以mSweetSheet3中的 setMenuList和setOnMenuItemClickListener就没有效果了
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSweetSheet3.dismiss();
            }
        });*/
        //监听返回

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String link = bundle.getString(BUNDLE_URL);
            if (!TextUtils.isEmpty(link)) {

                if (Common.DEBUG)
                    Toast.makeText(this.getActivity(), link, Toast.LENGTH_SHORT).show();

                Picasso.with(getActivity())
                        .load(link)
                        .placeholder(R.drawable.ic_done)
                        .into(imageView);
            }
        }
    }

}