package codextreme.jimmyneutron.wordcloud;


import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
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
import codextreme.jimmyneutron.helpers.AssetReader;

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
        browser = (WebView) view.findViewById(R.id.webView1);
        attachWordCloudToBrowser(browser, container.getWidth(),
                container.getHeight(), "");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    public static void attachWordCloudToBrowser(WebView browser, int widthPx, int heightPx, String tableId) {
        WordCloudTextCreator cloud = new WordCloudTextCreator(tableId);
        String wordJsArray = cloud.getCloud();

        Log.d("ZST123", "wordJsArray");
        Log.d("ZST123", wordJsArray);

        Context c = browser.getContext();
        String htmlData = AssetReader.read(c, "template.html");
        htmlData = htmlData.replace("##WORDS##", wordJsArray);
        htmlData = htmlData.replace("##COUNT##", (cloud.getSize() - 1) + "");

        Resources res = c.getResources();
        String width = Common.px_to_dp(res, widthPx) + "";
        String height = Common.px_to_dp(res, heightPx) + "";
        htmlData = htmlData.replace("##WIDTH##", width);
        htmlData = htmlData.replace("##HEIGHT##", height);

        if (Common.DEBUG) {
            Toast.makeText(browser.getContext(), "Width: " + width +"\n Height: " + height, Toast.LENGTH_SHORT).show();
        }

        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new JSInterface(c), "Hello");
        browser.setWebViewClient(new MyBrowser());
        browser.loadDataWithBaseURL("", htmlData, "text/html", "UTF-8", "");

        // https://stackoverflow.com/a/12039477
        browser.setBackgroundColor(Color.TRANSPARENT);
        browser.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        browser.setVisibility(View.INVISIBLE);
    }

    private static class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(),
                    R.anim.zoom_in));
            view.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
        }
    }

    private static class JSInterface {
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