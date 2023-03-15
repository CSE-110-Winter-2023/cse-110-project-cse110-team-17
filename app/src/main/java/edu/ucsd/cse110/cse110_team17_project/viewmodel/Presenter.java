package edu.ucsd.cse110.cse110_team17_project.viewmodel;

import android.view.View;
import android.widget.ImageView;

import java.util.List;

public class Presenter implements ZoomObserver {
    final float[] zoomSizes = new float[]{1F, 1.5F, 3F};
    int zoomPosition;
    float zoomSize;
    List<ImageView> circles;
    List<ZoomView> zoomViewList;

    void presenter(int ZoomPosition, List<ImageView> circles){
        this.zoomPosition = zoomPosition;
        this.circles = circles;
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
}
