package codextreme.jimmyneutron;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.github.florent37.materialtextfield.MaterialTextField;
import com.squareup.picasso.Picasso;

import codextreme.jimmyneutron.baseline.BaselineMapFragment;

import static codextreme.jimmyneutron.baseline.BaselineMapFragment.BUNDLE_URL;
import static codextreme.jimmyneutron.baseline.BaselineMapFragment.BUNDLE_USER;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MorphingButton button = (MorphingButton) findViewById(R.id.button);
        final TextView userTV = (TextView) findViewById(android.R.id.text1);
        final TextView passTV = (TextView) findViewById(android.R.id.text2);
        final ProgressBar prog = (ProgressBar) findViewById(R.id.progressBar);

        // Disable focus initially, fixes racing issue
        userTV.setFocusable(false);
        passTV.setFocusable(false);

        // Expand material textboxes and prevent it from closing
        final MaterialTextField materialtextfield1 = (MaterialTextField) findViewById(R.id.materialtextfield1);
        final MaterialTextField materialtextfield2 = (MaterialTextField) findViewById(R.id.materialtextfield2);
        materialtextfield1.setOnClickListener(null);
        materialtextfield2.setOnClickListener(null);
        materialtextfield1.expand();
        materialtextfield2.expand();

        // Enable focus after animation, fixes racing issue
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (materialtextfield1.isExpanded() && materialtextfield2.isExpanded()) {
                    // https://stackoverflow.com/questions/7407851/edittext-setfocusablefalse-cant-be-set-to-true
                    userTV.setFocusable(true);
                    userTV.setFocusableInTouchMode(true);
                    passTV.setFocusable(true);
                    passTV.setFocusableInTouchMode(true);
                } else {
                    new Handler().postDelayed(this, 250);
                }
            }
        }, 250);

        // Login stuff
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = userTV.getText().toString();
                final String pass = passTV.getText().toString();
                prog.setVisibility(View.VISIBLE);
                button.setEnabled(false);
                new Thread() {
                    public void run() {
                        if (tryLogin(user, pass)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    morphToSuccess(button);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                            startActivity(i);
                                            Common.mUsername = user;
                                            //Bundle args = new Bundle();
                                            //args.putString(BUNDLE_USER, user);
                                            //startActivity(i, args);
                                            prog.setVisibility(View.INVISIBLE);
                                            button.setEnabled(true);
                                        }
                                    }, 500);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    morphToFailure(button);
                                    prog.setVisibility(View.INVISIBLE);
                                    button.setEnabled(true);
                                }
                            });
                        }
                    }
                }.start();
            }
        });
    }

    private boolean tryLogin(String user, String pass) {
        if (user.equals("test")) {
            return true;
        } else if (!TextUtils.isEmpty(user) && pass.startsWith("pass")){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            return true;
        } else {
            // TODO online login
        }

        return false;
    }
    private void morphToSuccess(final MorphingButton btnMorph) {
        final Resources resources = getResources();
        int color_normal = resources.getColor(R.color.mb_green);
        int color_pressed = resources.getColor(R.color.mb_green_dark);
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius((int) Common.dp_to_px(getResources(), 56))
                .width(btnMorph.getWidth())
                .height(btnMorph.getHeight())
                .color(color_normal)
                .colorPressed(color_pressed)
                .text(getResources().getString(android.R.string.ok));
        btnMorph.morph(circle);
    }


    private void morphToFailure(final MorphingButton btnMorph) {
        final Resources resources = getResources();
        int color_normal = resources.getColor(R.color.mb_red);
        int color_pressed = resources.getColor(R.color.mb_red_dark);
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius((int) Common.dp_to_px(getResources(), 56))
                .width(btnMorph.getWidth())
                .height(btnMorph.getHeight())
                .color(color_normal)
                .colorPressed(color_pressed)
                .text("Wrong credentials");
        btnMorph.morph(circle);
    }

}
