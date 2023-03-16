package edu.ucsd.cse110.cse110_team17_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.R;
import edu.ucsd.cse110.cse110_team17_project.Utilities;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;
import edu.ucsd.cse110.cse110_team17_project.model.UserRepository;
import edu.ucsd.cse110.cse110_team17_project.view.FriendListAdapter;
import edu.ucsd.cse110.cse110_team17_project.viewmodel.AddFriendsViewModel;

public class AddFriendsActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private EditText newFriendText;
    public UserRepository userRepository;
    public FriendListAdapter adapter;

    public String friendListString;
    public List<String> friendList;

    public LiveData<List<UserInfo>> userInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);

        String myUID = preferences.getString("myUID", "UID NOT SET!");
        TextView myUIDView = findViewById(R.id.myUID);
        myUIDView.setText(myUID);

        friendListString = preferences.getString("friendListString", "");
        friendList = Utilities.parseFriendListString(friendListString);

        userRepository = new UserRepository();
        adapter = new FriendListAdapter();
        newFriendText = findViewById(R.id.new_friend_text);

        recyclerView = findViewById(R.id.recyclerView);

        // TODO: is there a way to get one remote UserInfo?
        //  What happens if one of your friends leaves the app and is no longer on the remote server?
        setUpRecyclerView(new ArrayList<>());
        userInfoList = userRepository.getRemoteUserInfo(friendList, false);
        userInfoList.observe(this, this::onUserInfoChanged);
    }

    private void setUpRecyclerView(List<UserInfo> userInfoList){
        adapter.setUserInfoList(userInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void onUserInfoChanged(List<UserInfo> userInfoList) {
        // TODO: what if they enter ANOTHER new friend before we check?
        // TODO: userNotFound is kinda a hacky solution? Do we need to fix other method?
        String userNotFound = "{\"latitude\":0.0,\"longitude\":0.0}";
        for(UserInfo userInfo : userInfoList){
            System.out.println(userInfo.toJSON());
            if(userInfo.toJSON().equals(userNotFound)){
                System.out.println("REMOVE NOW!");
                friendList.remove(friendList.size() - 1);
                Utilities.showError(this, "This friend does not exist.");
                return;
            }
        }
        setUpRecyclerView(userInfoList);
    }

    public void onAddClicked(View view) {
        String newFriendUid = newFriendText.getText().toString();

        if (!Utilities.isValidUID(newFriendUid)) {
            Utilities.showError(this, "Please enter a valid UID.");
        }
        else if (friendList.contains(newFriendUid)) {
            Utilities.showError(this, "You have already added this friend.");
        }
        else {
            friendList.add(newFriendUid);

            System.out.println("ADDED FRIEND!");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            newFriendText.setText("");

        }
    }

    public void onNextClicked(View view) {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("friendListString", Utilities.buildFriendListString(friendList));
        editor.apply();
        finish();
    }
}
