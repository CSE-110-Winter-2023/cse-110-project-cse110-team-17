package edu.ucsd.cse110.cse110_team17_project.viewmodel;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

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
    List<UserDisplayView> UserDisplayList = new ArrayList<>();

    Pair<Double, Double> currentLocation = new Pair<>(32.715736, -117.161087);


    public Presenter(Context context, int zoomPosition, List<ImageView> circles){
        this.context = context;
        this.zoomPosition = zoomPosition;
        this.circles = circles;
        zoomUpdate(zoomPosition);
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


    public void ImageViewUpdate(@NonNull List<UserInfo> userInfos){
        checkIfEnoughDisplay(userInfos.size());

        for(int i = 0; i < userInfos.size(); i++){
            UserDisplayList.get(i).updateDisplay(userInfos.get(i), currentLocation);
        }
    }

    private void checkIfEnoughDisplay(int targetSize) {
        int listSize = UserDisplayList.size();
        for(int i = targetSize; i < listSize; i++){
            UserDisplayList.get(i).setInvisible();
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

}
