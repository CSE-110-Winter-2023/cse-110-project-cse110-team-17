package edu.ucsd.cse110.cse110_team17_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ucsd.cse110.cse110_team17_project.R;

public class DemoForZoom extends AppCompatActivity {

    MutableLiveData<Float> zoomSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        zoomSubject = new MutableLiveData<>(1F);
        ImageView innerCircle = findViewById(R.id.inner_circle1);
        ImageView outerCircle = findViewById(R.id.circle_rim);
        View Constra = findViewById(R.id.rotateConstra);
        Button zoomInBtn = findViewById(R.id.zoom_in);
        Button zoomOutBtn = findViewById(R.id.zoom_out);
        zoomInBtn.setOnClickListener(this::clickedOnZoomIn);
        zoomOutBtn.setOnClickListener(this::clickedOnZoomOut);
        zoomSubject.observe(this, (num)->{
//            innerCircle.setScaleX(num);
//            innerCircle.setScaleY(num);
//            outerCircle.setScaleX(num);
//            outerCircle.setScaleY(num);
            Constra.setScaleX(num);
            Constra.setScaleY(num);

        });
        // actually X and Y would be 0, but just to make sure
        float centerX = innerCircle.getX();
        float centerY = innerCircle.getY();

        // suppose we will have a label#4
        TextView label4 = new TextView(this);
        label4.setText("label4");
    }



    private void clickedOnZoomOut(View view) {
        zoomSubject.postValue(zoomSubject.getValue() * 1.1F);
    }

    private void clickedOnZoomIn(View view) {
        zoomSubject.postValue(zoomSubject.getValue() / 1.1F);
    }

}