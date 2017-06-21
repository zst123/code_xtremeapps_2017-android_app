package codextreme.jimmyneutron;

import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.TypedValue;

public class Common {

    public static final boolean DEBUG = true;

    public static float dp_to_px(Resources r, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
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