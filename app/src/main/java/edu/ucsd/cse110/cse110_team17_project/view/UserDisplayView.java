package edu.ucsd.cse110.cse110_team17_project.view;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import edu.ucsd.cse110.cse110_team17_project.R;
import edu.ucsd.cse110.cse110_team17_project.Utilities;
import edu.ucsd.cse110.cse110_team17_project.activity.MainActivity;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;
import edu.ucsd.cse110.cse110_team17_project.viewmodel.Presenter;

public class UserDisplayView {
    Presenter presenter;
    public TextView textView;
    float zoomSize;
    public float angle;
    ConstraintLayout.LayoutParams layoutParams;
    double distance;
    public int radius;
    String label;

    private float rotation = 0F;

    public UserDisplayView(Presenter pr, TextView tv){
        this.presenter = pr;
        textView = tv;
        zoomSize = pr.register(this);
        layoutParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
    }

    public UserDisplayView(Presenter pr){
        this.presenter = pr;
        zoomSize = pr.register(this);
        layoutParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
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

        radius = (int) (Utilities.distanceToViewRadius(distance) * zoomSize);
        calculateCollisions();
        if (radius < 510) {
            textView.setText(label);
            textView.setTextSize(15.0F);
        }
        else {
            radius = 510;
            textView.setText("·");
            textView.setTextSize(100.0F);
        }
        setByLayout();
    }

    private void setByLayout() {
        layoutParams.constrainedWidth = false;
        layoutParams.matchConstraintMaxWidth = 1000;
        layoutParams.circleConstraint = R.id.status_dot;
        layoutParams.circleRadius = radius;
        layoutParams.circleAngle = angle + rotation;
        textView.setLayoutParams(layoutParams);
    }

    private void calculateCollisions() {
        for (UserDisplayView position : presenter.UserDisplayList) {
            if (position == this){
                break;
            }
            TextView collisionLabel = position.textView;
            ConstraintLayout.LayoutParams layoutParamsCollsion = (ConstraintLayout.LayoutParams)
                    collisionLabel.getLayoutParams();
            if (Math.abs(position.angle - angle) < 10) {
                if (Math.abs(position.radius - radius) < 50) {
                    layoutParamsCollsion.constrainedWidth = true;
                    layoutParamsCollsion.matchConstraintMaxWidth = 80;
                    collisionLabel.setLayoutParams(layoutParamsCollsion);
                    radius += 80;
                    layoutParams.constrainedWidth = true;
                    layoutParams.matchConstraintMaxWidth = 80;
                }
            }
        }
    }


    public void setInvisible() {
        textView.setVisibility(View.INVISIBLE);
    }

    public void updateRotation(float rotation) {
        this.rotation = rotation;
        setByLayout();
    }
}
