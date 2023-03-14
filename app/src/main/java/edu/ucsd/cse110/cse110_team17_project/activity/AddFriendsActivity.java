package edu.ucsd.cse110.cse110_team17_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.ucsd.cse110.cse110_team17_project.R;
import edu.ucsd.cse110.cse110_team17_project.viewmodel.AddFriendsViewModel;

public class AddFriendsActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private AddFriendsViewModel addFriendsViewModel;
    private EditText newFriendText;

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
    }


    public void onNextClicked(View view) {
    }

    public void onAddClicked(View view) {
        String friendUid = newFriendText.getText().toString();
        newFriendText.setText("");
        addFriendsViewModel.createFriend(friendUid);
        System.out.println("ADDED FRIEND!");
    }
}