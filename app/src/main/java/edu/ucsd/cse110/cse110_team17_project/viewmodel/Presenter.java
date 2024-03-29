package edu.ucsd.cse110.cse110_team17_project.viewmodel;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;
import edu.ucsd.cse110.cse110_team17_project.view.UserDisplayView;

public class Presenter implements ZoomObserver,locationObserver {
    Context context;
    final float[] zoomSizes = new float[]{1F, 1.5F, 3F};
    int zoomPosition;
    float zoomSize;
    List<ImageView> circles;
    public List<UserDisplayView> UserDisplayList;
    Pair<Double, Double> currentLocation = new Pair<>(32.715736, -117.161087);
    private List<UserInfo> userInfos;


    public Presenter(int zoomPosition, List<ImageView> circles){
        this.zoomPosition = zoomPosition;
        this.circles = circles;
        UserDisplayList = new ArrayList<>();
        zoomUpdate(zoomPosition);
    }

    public Presenter(){
        UserDisplayList = new ArrayList<>();
    }

    @Override
    public void zoomUpdate(int zoomPosition) {
        this.zoomPosition = zoomPosition;
        zoomSize = zoomSizes[zoomPosition];
        for(int i = 0; i < circles.size(); i++){
            ImageView circle = circles.get(i);
            scaleCircle(circle);
            visibleSet(circle, i);
        }
        for(var each:UserDisplayList){
            each.updateZoom(zoomSize);
        }
    }

    @Override
    public void updateLocation(Pair<Double, Double> currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void infosUpdate(@NonNull List<UserInfo> userInfos){
        this.userInfos = userInfos;
        viewUpdate();
    }

    public void viewUpdate(){
        for(int i = 0; i < userInfos.size(); i++){
            UserDisplayList.get(i).updateDisplay(userInfos.get(i), currentLocation);
        }
    }



    private void scaleCircle(ImageView circle){
        circle.setScaleX(zoomSize);
        circle.setScaleY(zoomSize);
    }

    private void visibleSet(ImageView circle, int i) {
        if(i < zoomPosition){
            circle.setVisibility(View.INVISIBLE);
        }else{
            circle.setVisibility(View.VISIBLE);
        }
    }

    public float register(UserDisplayView userDisplayView) {
        UserDisplayList.add(userDisplayView);
        return zoomSize;
    }

    public void updateRotation(float rotation) {
        for(var each: UserDisplayList){
            each.updateRotation(rotation);
        }
    }
}
