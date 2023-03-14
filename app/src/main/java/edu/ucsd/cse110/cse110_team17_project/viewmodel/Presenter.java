package edu.ucsd.cse110.cse110_team17_project.viewmodel;

import java.util.List;

public class Presenter implements ModelObserver, ZoomObserver {
    int zoomPosition;
    List<ZoomView> zoomViewList;

    void presenter(int ZoomPosition){
        this.zoomPosition = zoomPosition;
    }

    @Override
    public void zoomUpdate(int zoomPosition) {
        this.zoomPosition = zoomPosition;
    }
    @Override
    public void mUpdate() {

    }
}
