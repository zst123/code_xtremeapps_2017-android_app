package codextreme.jimmyneutron.baseline;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;

import codextreme.jimmyneutron.Common;
import codextreme.jimmyneutron.R;

import static codextreme.jimmyneutron.Common.urlEncode;


public class BaselineBookingDialog {

    public static View createDialogView(final Activity a, final LayoutInflater inflater,
                                        final BaselineMapView.DeskHolder desk,
                                        final String username, final AlertDialog dialog) {

        View dialogView = inflater.inflate(R.layout.dialog_baseline_booking, null);
        final TextView titleView = (TextView) dialogView.findViewById(R.id.titleBooking);
        titleView.setText("Booking: " + desk.name);

        final Button startTimeButton = (Button) dialogView.findViewById(R.id.startTimeButton);
        final Button endTimeButton = (Button) dialogView.findViewById(R.id.endTimeButton);

        final Button startDateButton = (Button) dialogView.findViewById(R.id.startDateButton);
        final Button endDateButton = (Button) dialogView.findViewById(R.id.endDateButton);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int dd1 = calendar.get(Calendar.DATE);
        int mm1 = calendar.get(Calendar.MONTH);
        int yy1 = calendar.get(Calendar.YEAR);

        calendar.add(Calendar.HOUR, 1);
        int nextHour = calendar.get(Calendar.HOUR_OF_DAY);
        int dd2 = calendar.get(Calendar.DATE);
        int mm2 = calendar.get(Calendar.MONTH);
        int yy2 = calendar.get(Calendar.YEAR);

        startTimeButton.setText(currentHour+":00");
        endTimeButton.setText(nextHour+":00");
        startDateButton.setText( String.format("%d-%d-%d", dd1, mm1, yy1) );
        endDateButton.setText( String.format("%d-%d-%d", dd2, mm2, yy2) );



        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(a, "Start Timing", new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        startTimeButton.setText(hourOfDay + ":00");
                    }
                });
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(a, "End Timing", new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        endTimeButton.setText(hourOfDay + ":00");
                    }
                });
            }
        });

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(a, "Start Date", new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int yy, int mm, int dd) {
                                startDateButton.setText(String.format("%d-%d-%d", dd, mm, yy));
                            }
                        }
                );
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(a, "End Date", new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int yy, int mm, int dd) {
                                endDateButton.setText(String.format("%d-%d-%d", dd, mm, yy));
                            }
                        }
                );
            }
        });

        dialogView.findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(a, "Hi", Toast.LENGTH_SHORT).show();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            /*
                            username:
                            facilityId:
                            facilityType:
                            dateTimeBook:
                            startTime:
                            endTime:
                            date:
                            */
                            Connection c = Jsoup.connect(Common.URL_BOOKING)
                                    .data("username", username)
                                    .data("floorPlanId", "1")
                                    .data("tableId", desk.seatId.split(",")[0])
                                    .data("facilityId", desk.seatId.split(",")[1])
                                    .data("facilityType", desk.seatType)
                                    .data("startTime", startTimeButton.getText().toString())
                                    .data("endTime", endTimeButton.getText().toString())
                                    .data("dateTimeBook", startDateButton.getText().toString())
                                    .data("BookDateTime", startDateButton.getText().toString())
                                    .data("date", endDateButton.getText().toString())

                                    .ignoreHttpErrors(true);
                            final Document doc = c.post();

                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String result = doc.text();
                                    boolean success = result.contains("Success") || result.contains("true");
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(a);
                                    dialog.setTitle("Booking")
                                            .setMessage(success ? "The booking is successful!" : result)
                                            .setPositiveButton(android.R.string.yes, null)
                                            .show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        return dialogView;
    }

    public static void showDatePicker(Activity a, String title, DatePickerDialog.OnDateSetListener listener) {
        Calendar now = Calendar.getInstance();

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DATE, 1);

        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DATE, -1);

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                listener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(false);
        dpd.setMaxDate(maxDate);
        dpd.setMinDate(minDate);
        dpd.show(a.getFragmentManager(), "Datepickerdialog");
    }
    public static void showTimePicker(Activity a, String title, TimePickerDialog.OnTimeSetListener listener) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                listener,
                now.get(Calendar.HOUR_OF_DAY),
                0,
                true
        );
        tpd.setThemeDark(false);
        tpd.dismissOnPause(true);
        tpd.enableSeconds(false);
        tpd.enableMinutes(false);
        tpd.setVersion(true ? TimePickerDialog.Version.VERSION_2 : TimePickerDialog.Version.VERSION_1);
        tpd.setTitle(title);
        // tpd.setTimeInterval(1, 15, 1);
        tpd.show(a.getFragmentManager(), "Timepickerdialog");
    }

}