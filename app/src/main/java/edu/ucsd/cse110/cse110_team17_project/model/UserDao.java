package edu.ucsd.cse110.cse110_team17_project.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;


// There are operations more than we really need, some unused operations may be deleted
@Dao
public interface UserDao {

    @Insert
    List<Long> insertAll(List<UserInfo> userInfo);

    @Insert
    long insert(UserInfo userInfo);

    @Upsert
    long upsert(UserInfo userInfo);

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE `private_code` = :privateCode)")
    boolean exists(long privateCode);


    @Query("SELECT * FROM `users` WHERE `private_code` =:privateCode")
    UserInfo get(long privateCode);

    @Query("SELECT * FROM `users`")
    List<UserInfo> getAll();

    @Query("SELECT * FROM `users`")
    LiveData<List<UserInfo>> getAllLive();

    @Update
    int update(UserInfo userInfo);

    @Delete
    public abstract int delete(UserInfo userInfo);
}
