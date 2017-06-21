package codextreme.jimmyneutron;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MorphingButton button = (MorphingButton) findViewById(R.id.button);
        button.setOnLongClickListener(view -> {
            Toast.makeText(MainActivity.this, "You clicked me", Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(this.findViewById(android.R.id.background), "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
            snackbar.show();
            Picasso.with(this).load("http://i.imgur.com/DvpvklR.png")
                    .resize(200, 200)
                    .into((ImageView) findViewById(R.id.imageView2));
            Picasso.with(this).setIndicatorsEnabled(true);
            return true;
        });


        button.setOnClickListener(view -> {
            morphToSuccess(button);
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, HomeActivity.class));
            }, 1000);
        });
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

}
