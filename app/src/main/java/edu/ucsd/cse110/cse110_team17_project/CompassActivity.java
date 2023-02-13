package edu.ucsd.cse110.cse110_team17_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CompassActivity extends AppCompatActivity {

    private Future<Void> future;
    private ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        Bundle coordInfo = getIntent().getExtras();
        TextView name_label1 = (TextView) findViewById(R.id.label_1);
        TextView name_label2 = (TextView) findViewById(R.id.label_2);
        TextView name_label3 = (TextView) findViewById(R.id.label_3);

        name_label1.setText(coordInfo.getString("label_1"));
        name_label2.setText(coordInfo.getString("label_2"));
        name_label3.setText(coordInfo.getString("label_3"));


    }

    public void onBackClicked(View view) {
        finish();
    }

    public void onRotateTestClicked(View view) {
        ImageView compass = (ImageView) findViewById(R.id.compass_face);


        this.future = backgroundExecutor.submit(() -> {
            float angle1 = 0;
            do {

                final float finalAngle = angle1;
                runOnUiThread(() -> {
                    compass.setRotation(finalAngle);
                });
                angle1 += 1;

                Thread.sleep(20);
            } while (angle1 < 360);

            return null;
        });


    }

    public void onAngleConfirmClicked(View view) {
        TextView AngleInput = (TextView) findViewById(R.id.input_angle);
        ImageView compass = (ImageView) findViewById(R.id.compass_face);
        Integer angle = Integer.parseInt(AngleInput.getText().toString());
        compass.setRotation(angle.floatValue());
    }
}