package edu.ucsd.cse110.cse110_team17_project.view;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import edu.ucsd.cse110.cse110_team17_project.R;
import edu.ucsd.cse110.cse110_team17_project.Utilities;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;
import edu.ucsd.cse110.cse110_team17_project.viewmodel.Presenter;

public class UserDisplayView {
    Presenter presenter;
    TextView textView;
    float zoomSize;

    float angle;

    double distance;

    String label;

    private Pair<Double, Double> currentLocation;
    public UserDisplayView(Presenter pr, TextView tv){
        this.presenter = pr;
        textView = tv;
        zoomSize = pr.register(this);
    }

    public void updateZoom(float zoomNum){
        zoomSize = zoomNum;
        setViewLocation();
    }

    public void updateDisplay(UserInfo userInfo, Pair<Double, Double> currentLocation){
        calculatePosition(userInfo, currentLocation);
        setViewLocation();
    }

    private void calculatePosition(UserInfo userInfo, Pair<Double, Double> currentLocation) {
        if (userInfo == null) return;
        label = userInfo.label;
        angle = (float) Utilities.updateAngle(currentLocation.first.floatValue(), currentLocation.second.floatValue(),
                (float) userInfo.latitude, (float) userInfo.longitude);

        distance = Utilities.distance(currentLocation.first.doubleValue(), currentLocation.second.doubleValue(),
                userInfo.latitude, userInfo.longitude);
    }

    private void setViewLocation(){

        int radius = (int) (Utilities.distanceToViewRadius(distance) * zoomSize);
        //radius = caculateCollisions(curLabelID, layoutParams, angle, radius);
        if (radius < 510) {
            textView.setText(label);
            textView.setTextSize(15.0F);
        }
        else {
            radius = 510;
            textView.setText("·");
            textView.setTextSize(100.0F);
        }

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.constrainedWidth = false;
        layoutParams.matchConstraintMaxWidth = 1000;
        layoutParams.circleConstraint = R.id.status_dot;
        layoutParams.circleRadius = radius;
        layoutParams.circleAngle = angle;
        textView.setLayoutParams(layoutParams);
    }


    public void setInvisible() {
        textView.setVisibility(View.INVISIBLE);
    }
}
