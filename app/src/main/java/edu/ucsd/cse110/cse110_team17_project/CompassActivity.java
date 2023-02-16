package edu.ucsd.cse110.cse110_team17_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CompassActivity extends AppCompatActivity {

    private LocationService locationService;
    private OrientationService orientationService;
    Double lat_local;
    Double lon_local;

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

        String coordinate_1 = coordInfo.getString("coordinate_1");
        String coordinate_2 = coordInfo.getString("coordinate_2");
        String coordinate_3 = coordInfo.getString("coordinate_3");

        String[] coordinate_1_components = coordinate_1.split(",");
        double coordinate_1_lat = Double.parseDouble(coordinate_1_components[0]);
        double coordinate_1_lon = Double.parseDouble(coordinate_1_components[1]);
        System.out.println(coordinate_1_lat + " " + coordinate_1_lon);

        locationService = LocationService.singleton(this);
        orientationService = OrientationService.singleton(this);
        locationService.getLocation().observe(this, loc -> {
            lat_local = loc.first;
            lon_local = loc.second;
            double y = Math.sin(coordinate_1_lon - lon_local) * Math.cos(coordinate_1_lat);
            double x = Math.cos(lat_local)*Math.sin(coordinate_1_lat) -
                    Math.sin(lat_local)*Math.cos(coordinate_1_lat)*Math.cos(coordinate_1_lon - lon_local);
            double res = Math.atan2(y, x) * 180/Math.PI;
            // show angle of coordinate
            System.out.println("Local: " + lat_local + " " + lon_local);
            System.out.println("Coordinate1: " + coordinate_1_lat + " " + coordinate_1_lon);
            System.out.println(res);
        });


        orientationService.getOrientation().observe(this, orientation -> {
            //System.out.println("Orientation: " + Float.toString(orientation)); show north?
            //System.out.println("North: " + -orientation * 360 / (2 * Math.PI));
        });

    }

    public void onBackClicked(View view) {
        finish();
    }
}