package edu.ucsd.cse110.cse110_team17_project;

import android.util.Log;

import androidx.annotation.WorkerThread;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LocationAPI {

    private volatile static LocationAPI instance = null;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client;

    public LocationAPI() {
        this.client = new OkHttpClient();
    }

    public static LocationAPI provide() {
        if (instance == null) {
            instance = new LocationAPI();
        }
        return instance;
    }

    @WorkerThread
    public String getUser(String id) {
        id = id.replace(" ", "%20");

        var body ="";

        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location" + id)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            body = response.body().string();
            Log.i("GETUSER", body);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return body;
    }

    @WorkerThread
    public String postInfo(UserInfo ui) {
        String id = ui.privateCode;
        id = id.replace(" ", "%20");
        String jsonUI = ui.toJSON();
        String responseStr = "";
        RequestBody body = RequestBody.create(jsonUI, JSON);
        Request request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location" + id)
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            responseStr = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }


}
