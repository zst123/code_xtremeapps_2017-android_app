package codextreme.jimmyneutron.baseline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import codextreme.jimmyneutron.Common;

@SuppressLint("AppCompatCustomView")
public class BaselineMapView extends ImageView implements View.OnTouchListener {

    DeskClickListener mDeskClickListener;
    GestureDetector gestureDetector;

    LinkedList<DeskHolder> mDesks = new LinkedList<>();

    Paint mPaintFill = new Paint();
    Paint mPaintStroke = new Paint();

    public BaselineMapView(Context context) {
        super(context);
        init(context);
    }

    public BaselineMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaselineMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(final Context context) {
        setOnTouchListener(this);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                int touchX = (int) event.getX();
                int touchY = (int) event.getY();

                for (DeskHolder desk : mDesks) {
                    if (Common.is_point_within_path(desk.path, touchX, touchY)) {
                        if (mDeskClickListener != null) {
                            mDeskClickListener.onDeskClick(desk);
                        }
                        invalidate();
                        if (Common.DEBUG) {
                            Toast.makeText(context,
                                "Touched: "+desk.name
                                , Toast.LENGTH_SHORT).show();
                        }
                    }

                }


                /*
                if (Common.is_point_within_path(mDesks.get(1).path, touchX, touchY)) {
                    if (mDesks.get(1).color == Color.BLUE) {
                        mDesks.get(1).color = Color.GREEN;
                    } else {
                        mDesks.get(1).color = Color.BLUE;
                    }
                    invalidate();
                } else {
                    Toast.makeText(context,
                            "Touch coordinates : x = " +
                                    touchX + " & y = " + touchY
                            , Toast.LENGTH_SHORT).show();
                }
                */
                return super.onSingleTapConfirmed(event);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                return super.onSingleTapUp(event);
            }
        });
    }

    public BaselineMapView addDesk(DeskHolder desk) {
        mDesks.add(desk);
        return this;
    }

    public void addDesksHardCoded() {
        //TODO init desk from online
        mDesks.clear();

        mDesks.add(new DeskHolder(
                488, 173,
                490, 208,
                560, 206,
                560, 171,
                Color.RED, "TV",
                "432342", "Utility"
        ));

        mDesks.add(new DeskHolder(
                282, 296,
                280, 396,
                117, 455,
                118, 331,
                Color.RED, "Table 1",
                "43546", "Table"
        ));

        mDesks.add(new DeskHolder(
                296, 384,
                296, 294,
                392, 273,
                393, 336,
                Color.GREEN, "Table 2",
                "43545", "Table"
        ));

        mDesks.add(new DeskHolder(
                394, 273,
                393, 333,
                446, 311,
                446, 261,
                Color.RED, "Table 3",
                "43546", "Table"
        ));

        mDesks.add(new DeskHolder(
                655, 316,
                523, 288,
                561, 251,
                634, 263,
                Color.RED, "Table 4",
                "43546", "Table"
        ));

        mDesks.add(new DeskHolder(
                674, 337,
                715, 436,
                404, 380,
                497, 318,
                Color.RED, "Table 5",
                "43546", "Table"
        ));

        requestLayout();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int c_w = getWidth();//canvas.getWidth();
        int c_h = getHeight();//canvas.getHeight();
        float ratio_x = c_w * 1.0f / getDrawable().getIntrinsicWidth();
        float ratio_y = c_h * 1.0f / getDrawable().getIntrinsicHeight();

        for (DeskHolder desk : mDesks) {
            desk.setScale(ratio_x, ratio_y);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // https://stackoverflow.com/questions/26866136/how-to-draw-rounded-corner-polygons-on-android-canvas
        for (DeskHolder desk : mDesks) {
            drawPathOutlineOnCanvas(canvas, desk.path, desk.color);
        }

        /*
        int c_w = getWidth();//canvas.getWidth();
        int c_h = getHeight();//canvas.getHeight();

        float ratio_x = c_w * 1.0f / getDrawable().getIntrinsicWidth();
        float ratio_y = c_h * 1.0f / getDrawable().getIntrinsicHeight();


        if (Common.DEBUG && false) {
            Toast.makeText(this.getContext(),
                    "\nratio_x=" + ratio_x +
                            "\nratio_x=" + ratio_x +
                            "\nratio_y=" + ratio_y +
                            "\nc_w=" + c_w + "OR" + getWidth() +
                            "\nc_h=" + c_h + "OR" + getHeight() +
                            "\ngetIntrinsicWidth=" + getDrawable().getIntrinsicWidth() +
                            "\ngetIntrinsicHeight=" + getDrawable().getIntrinsicHeight() +
                            "\nYou clicked me"
                    , Toast.LENGTH_SHORT).show();
        }
        */
    }

    /**
     * Helper method to draw a path with a 50% fill of a certain color
     *
     * @param canvas
     * @param path
     * @param color
     */
    public void drawPathOutlineOnCanvas(Canvas canvas, Path path, int color) {
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintStroke.setStrokeWidth(Common.dp_to_px(getResources(), 2));
        mPaintStroke.setStyle(Paint.Style.STROKE);

        mPaintStroke.setColor(color);
        mPaintFill.setColor(Common.color_alpha(color, 128));

        canvas.drawPath(path, mPaintFill);
        canvas.drawPath(path, mPaintStroke);
    }


    public static Path createPathPolygonScaled(
            int x1, int y1,
            int x2, int y2,
            int x3, int y3,
            int x4, int y4,
            float scaleX, float scaleY) {
        return createPathPolygon(
                scaleX * x1, scaleY * y1,
                scaleX * x2, scaleY * y2,
                scaleX * x3, scaleY * y3,
                scaleX * x4, scaleY * y4
        );
    }

    public static Path createPathPolygon(
            float x1, float y1,
            float x2, float y2,
            float x3, float y3,
            float x4, float y4) {
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        path.lineTo(x1, y1);
        path.close();
        return path;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

        }
        return true;
    }

    public List<DeskHolder> getDesks() {
        return mDesks;
    }

    public void randomiseColors() {
        Random randomGenerator = new Random();
        for (DeskHolder desk : mDesks) {
            int randomInt = randomGenerator.nextInt(3); // number 0 - 2 inclusive
            switch (randomInt) {
                case 0:
                    desk.color = Color.GREEN;
                    break;
                case 1:
                    desk.color = Color.RED;
                    break;
                case 2:
                    desk.color = Color.YELLOW;
                    break;
            }
        }
        invalidate();
    }

    public void setDeskClickListener(DeskClickListener d) {
        mDeskClickListener = d;
    }

    public interface DeskClickListener {
        public void onDeskClick(DeskHolder desk);
    }

    public static class DeskHolder {
        Path path = null;
        int color;
        String name;
        int x1;
        int y1;
        int x2;
        int y2;
        int x3;
        int y3;
        int x4;
        int y4;
        String seatId;
        String seatType;

        public DeskHolder(
                int x1, int y1,
                int x2, int y2,
                int x3, int y3,
                int x4, int y4,
                int c, String n,
                String seatId, String seatType) {
            this.x1 = x1;
            this.x2 = x2;
            this.x3 = x3;
            this.x4 = x4;
            this.y1 = y1;
            this.y2 = y2;
            this.y3 = y3;
            this.y4 = y4;
            this.seatId = seatId;
            this.seatType = seatType;
            color = c;
            name = n;
        }

        public Path setScale(float sX, float sY) {
            path = createPathPolygonScaled(
                    x1, y1,
                    x2, y2,
                    x3, y3,
                    x4, y4,
                    sX, sY);
            return path;
        }
    }
}