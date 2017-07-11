package codextreme.jimmyneutron.wordcloud;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import codextreme.jimmyneutron.Common;
import codextreme.jimmyneutron.R;

/*
https://github.com/prateek27/android-word-cloud
https://stackoverflow.com/questions/5781616/jquery-attronclick
https://stackoverflow.com/questions/20917235/webviews-html-button-click-detection-in-activityjava-code
https://stackoverflow.com/questions/4065312/detect-click-on-html-button-through-javascript-in-android-webview
 */
public class WordCloudFragment extends Fragment {

    WebView browser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wordcloud, container, false);


        String[] strArr = (
                "Programming application development Project management Help desk technical support" +
                        " Security compliance governance Web development Database administration Business" +
                        " intelligence analytics Accounting Banking/Brokerage Cross-Functional Desktop Graphics" +
                        " Desktop Publishing Electronic Mail Help Desk Insurance Integrated PC Apps Legal Manufacturing" +
                        " Online Services Payroll/Personnel PC Communications Presentation Report Writing Scheduling/Planning" +
                        " Screen Grabbers Security Spreadsheets Miscellaneous Apps"
        ).split(" ");
        String wordJsArray = "[";
        for (int i = 0; i < strArr.length; i++) {
            String eachStr = strArr[i];
            int priority = 2;
            if (i == 0) {
                priority = 20;
            } else if (i == 1) {
                priority = 15;
            } else if (i <= 5) {
                priority = 10;
            } else if (i <= 10) {
                priority = 8;
            } else if (i <= 12) {
                priority = 7;
            } else if (i <= 15) {
                priority = 6;
            } else if (i <= 20) {
                priority = 5;
            } else if (i <= 30) {
                priority = 3;
            }
            wordJsArray += "[`" + eachStr + "`, " + priority + "]";
            if (i < strArr.length-1) {
                wordJsArray += ", ";
            }
        }

        wordJsArray += "]";
        wordJsArray = wordJsArray.replace ("`", "\"");

        Log.d("ZST123", "wordJsArray");
        Log.d("ZST123", wordJsArray);

        String htmlData = AssetReader.read(getActivity(), "template.html");
        htmlData = htmlData.replace("##WORDS##", wordJsArray);
        htmlData = htmlData.replace("##COUNT##", (strArr.length - 1) + "");

        String width = Common.px_to_dp(getResources(), container.getWidth()) + "";
        String height = Common.px_to_dp(getResources(), container.getHeight()) + "";
        htmlData = htmlData.replace("##WIDTH##", width);
        htmlData = htmlData.replace("##HEIGHT##", height);

        if (Common.DEBUG) {
            Toast.makeText(getActivity(), "Width: " + width +"\n Height: " + height, Toast.LENGTH_SHORT).show();
        }

        browser = (WebView) view.findViewById(R.id.webView1);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new JSInterface(getActivity()), "Hello");
        browser.setWebViewClient(new MyBrowser());
        browser.loadDataWithBaseURL("", htmlData, "text/html", "UTF-8", "");

        // https://stackoverflow.com/a/12039477
        browser.setBackgroundColor(Color.TRANSPARENT);
        browser.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        browser.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.startAnimation(AnimationUtils.loadAnimation(getContext(),
                    R.anim.zoom_in));
            view.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
        }
    }

    private class JSInterface {
        Context context;
        JSInterface(Context x) {
            context = x;
        }
        @JavascriptInterface
        public void javascriptClick(String text) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

}