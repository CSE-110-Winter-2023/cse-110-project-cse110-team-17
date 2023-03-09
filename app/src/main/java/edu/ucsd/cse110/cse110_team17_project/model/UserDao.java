package edu.ucsd.cse110.cse110_team17_project.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;


// There are operations more than we really need, some unused operations may be deleted
@Dao
public interface UserDao {

    @Insert
    List<Long> insertAll(List<edu.ucsd.cse110.cse110_team17_project.model.UserInfo> userInfo);

    @Insert
    long insert(edu.ucsd.cse110.cse110_team17_project.model.UserInfo userInfo);

    @Upsert
    long upsert(edu.ucsd.cse110.cse110_team17_project.model.UserInfo userInfo);

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE `private_code` = :privateCode)")
    boolean exists(long privateCode);


    @Query("SELECT * FROM `users` WHERE `private_code` =:privateCode")
    edu.ucsd.cse110.cse110_team17_project.model.UserInfo get(long privateCode);

    @Query("SELECT * FROM `users`")
    List<edu.ucsd.cse110.cse110_team17_project.model.UserInfo> getAll();

    @Query("SELECT * FROM `users`")
    LiveData<List<edu.ucsd.cse110.cse110_team17_project.model.UserInfo>> getAllLive();

    @Update
    int update(edu.ucsd.cse110.cse110_team17_project.model.UserInfo userInfo);

    @Delete
    public abstract int delete(UserInfo userInfo);
}
