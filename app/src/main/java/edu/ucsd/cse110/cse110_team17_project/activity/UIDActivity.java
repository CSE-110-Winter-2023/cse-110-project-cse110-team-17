package edu.ucsd.cse110.cse110_team17_project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

import edu.ucsd.cse110.cse110_team17_project.R;

public class UIDActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uidactivity);

        String uniqueID = UUID.randomUUID().toString().replace("-","").substring(0, 18);
        TextView uid = findViewById(R.id.yourID);
        uid.setText(uniqueID);

        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("myUID", uniqueID);
        editor.apply();

        Intent enternameIntent = new Intent(this, EnterNameActivity.class);
        startActivity(enternameIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void onNextClicked(View view) {
        //TODO: start "add friends activity"

        finish();
    }
}