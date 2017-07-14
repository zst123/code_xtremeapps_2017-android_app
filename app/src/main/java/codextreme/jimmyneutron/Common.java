package codextreme.jimmyneutron;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.TypedValue;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Common {

    public static final boolean DEBUG = true;


    public static final String URL_BASELINE = "http://codextremejimmyneutron.azurewebsites.net/webservice.asmx/GetLastFloorPlanPic";;
    public static final String URL_COORDINATES = "http://codextremejimmyneutron.azurewebsites.net/webservice.asmx/GetLastFloorPlanPicCoordinates";
    public static final String URL_BOOKING = "http://codextremejimmyneutron.azurewebsites.net/webservice.asmx/CreateNewBooking";
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    //"Code Xtreme Jimmy Neutron App";

    public static String mUsername;

    public static String urlEncode(String in) {
        try {
            return URLEncoder.encode(in, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static float dp_to_px(Resources r, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static float px_to_dp(Resources r, float px) {
        return px / dp_to_px(r, 1);
    }

    /**
     * Change the alpha of the given color.
     */
    public static int color_alpha(int col, int alpha) {
        return (col & 0x00FFFFFF) | (alpha << 24);
    }

    public static float percent_of_in_range(float low, float high, float percent) {
        return ((high - low) * percent) + low;
    }


    /**
     * https://stackoverflow.com/questions/2597590/how-can-i-tell-if-a-closed-path-contains-a-given-point
     */
    public static boolean is_point_within_path(Path path, int x, int y) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);

        Region region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));

        return region.contains(x, y);
    }

}
