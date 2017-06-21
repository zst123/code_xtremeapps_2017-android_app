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

import codextreme.jimmyneutron.Common;

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

                Toast.makeText(context,
                        "inside2 Touch coordinates : x = " +
                                touchX + " & y = " + touchY
                        , Toast.LENGTH_SHORT).show();

                for (DeskHolder desk : mDesks) {
                    if (Common.is_point_within_path(desk.path, touchX, touchY)) {
                        if (mDeskClickListener != null) {
                            mDeskClickListener.onDeskClick(desk);
                        }
                        invalidate();
                    }
                    Toast.makeText(context,
                            "Touched: "+desk.name
                            , Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mDesks.clear();

        int c_w = getWidth();//canvas.getWidth();
        int c_h = getHeight();//canvas.getHeight();
        float ratio_x = c_w * 1.0f / getDrawable().getIntrinsicWidth();
        float ratio_y = c_h * 1.0f / getDrawable().getIntrinsicHeight();

        mDesks.add(new DeskHolder(
                createPathPolygonScaled(
                        488, 173,
                        490, 208,
                        560, 206,
                        560, 171,
                        ratio_x, ratio_y),
                Color.RED,
                "TV"
        ));

        mDesks.add(new DeskHolder(
                createPathPolygonScaled(
                        296, 384,
                        296, 294,
                        392, 273,
                        393, 336,
                        ratio_x, ratio_y),
                Color.BLUE,
                "Desk 2"
        ));
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


    public Path createPathPolygonScaled(
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

    public Path createPathPolygon(
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

    public void setDeskClickListener(DeskClickListener d) {
        mDeskClickListener = d;
    }

    public interface DeskClickListener {
        public void onDeskClick(DeskHolder desk);
    }

    public class DeskHolder {
        Path path;
        int color;
        String name;

        public DeskHolder(Path p, int c, String n) {
            path = p;
            color = c;
            name = n;
        }
    }
}