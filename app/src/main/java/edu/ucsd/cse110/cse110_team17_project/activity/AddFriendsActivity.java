package edu.ucsd.cse110.cse110_team17_project.activity;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.R;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;
import edu.ucsd.cse110.cse110_team17_project.view.FriendListAdapter;
import edu.ucsd.cse110.cse110_team17_project.viewmodel.AddFriendsViewModel;

public class AddFriendsActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private AddFriendsViewModel addFriendsViewModel;
    private EditText newFriendText;

    public List<UserInfo> userInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String myUID = preferences.getString("myUID", "UID NOT SET!");
        TextView myUIDView = findViewById(R.id.myUID);
        myUIDView.setText(myUID);

        newFriendText = findViewById(R.id.new_friend_text);
        addFriendsViewModel = new ViewModelProvider(this).get(AddFriendsViewModel.class);

        recyclerView = findViewById(R.id.recyclerView);
        userInfoList = new ArrayList<UserInfo>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FriendListAdapter adapter = new FriendListAdapter();
        adapter.setUserInfoList(userInfoList);
        recyclerView.setAdapter(adapter);
    }


    public void onNextClicked(View view) {
    }

    public void onAddClicked(View view) {
        String friendUid = newFriendText.getText().toString();
        newFriendText.setText("");
        addFriendsViewModel.createFriend(friendUid);

        userInfoList.add(new UserInfo("",friendUid,""));
        System.out.println("ADDED FRIEND!");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
