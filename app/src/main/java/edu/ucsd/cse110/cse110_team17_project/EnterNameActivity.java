package edu.ucsd.cse110.cse110_team17_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EnterNameActivity extends AppCompatActivity {

    // load inputs into the page on creation, set the page to portrait only
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadInputs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // handles the submit button clicked action
    public void onSubmitClicked(View view) throws Exception {
        EditText username = findViewById(R.id.username);
        if(checkEmpty(username)){
            Utilities.showError(this, "Please enter Username.");
        }
        else{
            saveInputs();
            Intent uidIntent = new Intent(this, UIDActivity.class);
            startActivity(uidIntent);
            finish();
        }
    }

    // method used to save the entered inputs to shared preferences
    public void saveInputs() {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        EditText username = findViewById(R.id.username);
        editor.putString("username", username.getText().toString());

        editor.apply();
    }

    // method used to load previously entered inputs to the activity
    public void loadInputs() {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String usernameStr = preferences.getString("username", "");

        EditText username = findViewById(R.id.username);
        username.setText(usernameStr);
    }

    // method used to check if a textview is empty
    public boolean checkEmpty(EditText text){
        return text.getText().toString().isEmpty();
    }
}