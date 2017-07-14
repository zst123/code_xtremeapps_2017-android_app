package codextreme.jimmyneutron.baseline;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import codextreme.jimmyneutron.Common;
import codextreme.jimmyneutron.R;

import static codextreme.jimmyneutron.Common.percent_of_in_range;
import static codextreme.jimmyneutron.wordcloud.WordCloudFragment.attachWordCloudToBrowser;

// http://www.vogella.com/tutorials/AndroidFragments/article.html
// https://developer.android.com/training/basics/fragments/fragment-ui.html
public class BaselineMapFragment extends Fragment {
    public static final String BUNDLE_BASE64_IMG = "url_base64";
    public static final String BUNDLE_URL = "url";
    public static final String BUNDLE_USER = "user";
    public static final String BUNDLE_TITLE = "title";
    public static final String BUNDLE_COORDINATES = "coord";
    public static final String BUNDLE_RANDOM_COLOUR = "random_colour";

    private BaselineMapView imageView;
    private LinearLayout linearLayout;
    private SeekBar seekbar;
    private boolean doRandomColours;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baseline, container, false);
        imageView = (BaselineMapView) view.findViewById(R.id.view_baseline_map);

        linearLayout = (LinearLayout) view.findViewById(android.R.id.home);

        seekbar = (SeekBar) view.findViewById(android.R.id.secondaryProgress);
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
                showDialog(getActivity(), desk);
            }
        });

        return view;
    }


    public BaselineMapView getBaselineMapView() {
        return imageView;
    }

    public void showDialog(Context thiz, final BaselineMapView.DeskHolder desk) {
        // SweetSheet 控件,根据 rl 确认位置
        FrameLayout frame = (FrameLayout) getActivity().getWindow().getDecorView();
        SweetSheet mSweetSheet3 = new SweetSheet(frame);
        //mSweetSheet3.setBackgroundEffect(new BlurEffect(8));
        View view = LayoutInflater.from(thiz).inflate(R.layout.dialog_baseline_popup, null, false);
        //定义一个 CustomDelegate 的 Delegate ,并且设置它的出现动画.
        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.AlphaAnimation);
        customDelegate.setCustomView(view);

        int contentHeight = (int) (frame.getHeight() * 0.75);
        customDelegate.setContentHeight(contentHeight);
        mSweetSheet3.setDelegate(customDelegate);
        ((TextView)view.findViewById(android.R.id.text1)).setText(desk.name);
        mSweetSheet3.show();

        view.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context darkContext = new ContextThemeWrapper(v.getContext(), R.style.Theme_AppCompat_Light_Dialog);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(darkContext);
                LayoutInflater inflater = LayoutInflater.from(darkContext);

                AlertDialog alertDialog = dialogBuilder.create();
                View dialogView = BaselineBookingDialog.createDialogView(
                        BaselineMapFragment.this.getActivity(),
                        inflater, desk, Common.mUsername, alertDialog);
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
        WebView browser = (WebView) view.findViewById(R.id.webView2);
        attachWordCloudToBrowser(browser, (int) (frame.getWidth() * 0.9),
                (int) (contentHeight * 0.75), "0");
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

            final String link_for_b64 = bundle.getString(BUNDLE_BASE64_IMG);
            if (!TextUtils.isEmpty(link_for_b64)) {
                imageView.setImageResource(R.drawable.ic_done); // Placeholder
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Document doc = Jsoup.connect(link_for_b64).get();
                            final String result = doc.text();
                            Activity thiz = getActivity();
                            if (thiz != null) {
                                thiz.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                                        byte[] decodedString = Base64.decode(result, Base64.DEFAULT);
                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                        imageView.setImageBitmap(decodedByte);
                                    }
                                });
                                Log.d("ZST123", "link_for_b64 - result:");
                                Log.d("ZST123", result);
                            } else {
                                if (Common.DEBUG) {
                                    Toast.makeText(getActivity(), "thiz is null", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (IOException e) {
                            if (Common.DEBUG) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            e.printStackTrace();
                        }
                    }
                }.start();
            }

            final String coordUrl = bundle.getString(BUNDLE_COORDINATES);
            if (!TextUtils.isEmpty(coordUrl)) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            final String result = Jsoup.connect(coordUrl).get().text();
                            Log.d("ZST123", "coordUrl - result:");
                            Log.d("ZST123", result);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //1,0:(260, 301), (292, 334);1,1:(88, 297), (139, 343);
                                    // Regex of (\d*?),(\d*?):\((\d*?), (\d*?)\), \((\d*?), (\d*?)\);
                                    // Escaped version: (\\d*?),(\\d*?):\\((\\d*?), (\\d*?)\\), \\((\\d*?), (\\d*?)\\);
                                    Pattern pattern = Pattern.compile("(\\d*?),(\\d*?):\\((\\d*?), (\\d*?)\\), \\((\\d*?), (\\d*?)\\);");
                                    Matcher matcher = pattern.matcher(result);
                                    while (matcher.find()) {
                                        Log.d("ZST123", "coordUrl - matcher.find():" + matcher.group(0));
                                        String table = matcher.group(1);
                                        String seat = matcher.group(2);
                                        int x1 = Integer.parseInt(matcher.group(3));
                                        int y1 = Integer.parseInt(matcher.group(4));
                                        int x2 = Integer.parseInt(matcher.group(5));
                                        int y2 = Integer.parseInt(matcher.group(6));
                                        imageView.addDesk(new BaselineMapView.DeskHolder(
                                                x1, y1,
                                                x2, y1,
                                                x2, y2,
                                                x1, y2,
                                                Color.RED,
                                                "Table " + table + " (Seat " + seat + ")",
                                                table + "," + seat,
                                                "Table " + table
                                        ));
                                    }
                                    imageView.requestLayout();
                                }
                            });
                        } catch (IOException e) {
                            if (Common.DEBUG) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            e.printStackTrace();
                        }
                    }
                }.start();
            }

            String title = bundle.getString(BUNDLE_TITLE);
            if (!TextUtils.isEmpty(title)) {
                TextView titleBaseline = (TextView) getView().findViewById(R.id.titleBaseline);
                titleBaseline.setVisibility(View.VISIBLE);
                titleBaseline.setText(title);
            }

            doRandomColours = bundle.containsKey(BUNDLE_RANDOM_COLOUR);
            if (doRandomColours) {
                final Handler handler = new Handler();
                handler.postDelayed(new Thread() {
                    @Override
                    public void run() {
                        imageView.randomiseColors();
                        handler.postDelayed(this, 5000);
                    }
                }, 5000);

            }

            //mUsername = bundle.getString(BUNDLE_USER);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // solves issue of layout not reloading on first load
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                seekbar.setProgress(50);
            }
        }, 250);

        if (!doRandomColours) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Document doc = Jsoup.connect(Common.URL_SEAT_STATUS)
                                            .data("floorPlanId", "1")
                                            .post();
                                    // 0;0;Green:1;0;Green:2;0;Green:3;0;Green:4;0;RED:5;0;RED:6;0;Green:7;0;Green:8;0;Green:9;0;RED:
                                    for (String eachSeat : doc.text().split(":")) {
                                        if (!TextUtils.isEmpty(eachSeat.trim())) {
                                            // seat;table;color
                                            Log.d("ZST123", "Doing for seat: " + eachSeat);
                                            String[] items = eachSeat.trim().split(";");
                                            for (BaselineMapView.DeskHolder desk : imageView.getDesks()) {
                                                Log.d("ZST123", ">> " + desk.seatId + " and " + items[2]);
                                                if (desk.seatId.equals(items[1] + "," + items[0])) {
                                                    Log.d("ZST123", ">> Got in at " + desk.seatId + " and " + items[2]);
                                                    if (items[2].equalsIgnoreCase("green")) {
                                                        desk.color = Color.GREEN;
                                                    } else if (items[2].equalsIgnoreCase("red")) {
                                                        desk.color = Color.RED;
                                                    } else if (items[2].equalsIgnoreCase("yellow")) {
                                                        desk.color = Color.YELLOW;
                                                    } else {
                                                        desk.color = Color.GRAY;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (isVisible()) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView.invalidate();
                                            }
                                        });
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        t.start();
                        t.join();
                        if (isVisible()) {
                            handler.postDelayed(this, Common.TIME_UPDATE_SEAT_STATUS);
                        } else {
                            Log.d("ZST123", "BaselineMapFragment: onResume - not visible, killing");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }, Common.TIME_UPDATE_SEAT_STATUS);
        }

    }
}
