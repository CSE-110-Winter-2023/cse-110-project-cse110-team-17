package edu.ucsd.cse110.cse110_team17_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
    private AddFriendsViewModel addFriendsViewModel;
    private EditText newFriendText;
    public UserRepository userRepository;

    public List<UserInfo> userInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);

        String myUID = preferences.getString("myUID", "UID NOT SET!");
        TextView myUIDView = findViewById(R.id.myUID);
        myUIDView.setText(myUID);
        String friendListString = preferences.getString("friendListString", "");
        FriendListAdapter adapter = new FriendListAdapter();
        userRepository = new UserRepository();

        newFriendText = findViewById(R.id.new_friend_text);
        addFriendsViewModel = new ViewModelProvider(this).get(AddFriendsViewModel.class);

        recyclerView = findViewById(R.id.recyclerView);

        // TODO: is there a way to get one remote UserInfo?
        //  What happens if one of your friends leaves the app and is no longer on the remote server?
        userInfoList = userRepository.getRemoteUserInfo(Utilities.parseFriendListString(friendListString)).getValue();
        if (userInfoList != null) {
            adapter.setUserInfoList(userInfoList);
        }
        else {
            adapter.setUserInfoList(new ArrayList<UserInfo>());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    public void onNextClicked(View view) {
    }

    public void onAddClicked(View view) {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String friendListString = preferences.getString("friendListString", "");

        String friendUid = newFriendText.getText().toString();

        if (!Utilities.isValidUID(friendUid)) {
            Utilities.showError(this, "Please enter a valid UID.");
        }
        else if (friendListString.contains(friendUid)) {
            Utilities.showError(this, "You have already added this friend.");
        }
        else {
            String delimiter = friendListString.isEmpty() ? "" : "-";
            List<String> newFriendUID = List.of(friendUid);
            List<UserInfo> newFriendList = userRepository.getRemoteUserInfo(newFriendUID).getValue();
            UserInfo newFriend = newFriendList != null ? newFriendList.get(0) : null;
            if (newFriend == null) {
                Utilities.showError(this, "This friend does not exist.");
            }
            else {
                userInfoList.add(newFriend); // mock userinfo
                System.out.println("ADDED FRIEND!");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editor.putString("friendListString", friendListString + delimiter + friendUid);
                newFriendText.setText("");
                editor.apply();
            }
        }
    }
}
