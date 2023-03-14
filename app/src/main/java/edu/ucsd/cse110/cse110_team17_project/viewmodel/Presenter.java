package edu.ucsd.cse110.cse110_team17_project.viewmodel;

import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;

public class Presenter implements ModelObserver, ViewObserver{
    int ZoomPositoin;
    List<UserInfo> UserInfors;

    void presenter(int ZoomPosition){
        this.ZoomPositoin = ZoomPositoin;
    }

    @Override
    public void vUpdate() {

    }
    @Override
    public void mUpdate() {

    }
}
