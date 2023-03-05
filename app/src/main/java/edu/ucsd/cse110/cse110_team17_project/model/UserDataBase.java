package edu.ucsd.cse110.cse110_team17_project.model;


import android.content.Context;
import android.service.autofill.UserData;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {UserInfo.class}, version = 1)
public abstract class UserDataBase extends RoomDatabase {
    private static UserDataBase singleton = null;
    public abstract UserDao userDao();

    public synchronized static UserDataBase getSingleton(Context context){
        if(singleton == null){
            singleton = UserDataBase.makeDatabase(context);
        }
        return singleton;
    }

    private static UserDataBase makeDatabase(Context context) {
        return Room.databaseBuilder(context, UserDataBase.class, "SocialCompass_app.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(UserDataBase testDatabase){
        if(singleton != null){
            singleton.close();
        }
        singleton = testDatabase;
    }
}
