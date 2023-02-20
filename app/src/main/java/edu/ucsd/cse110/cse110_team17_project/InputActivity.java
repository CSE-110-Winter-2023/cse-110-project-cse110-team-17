package edu.ucsd.cse110.cse110_team17_project;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {

    // load inputs into the page on creation, set the page to portrait only
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadInputs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // check inputs of label and coordinates when submit is clicked
    // if no problem exist, save the inputs and close this activity
    // else warn the user to re-input the label and coordinates
    public void onSubmitClicked(View view) throws Exception {
        boolean[] coordinate_entered = new boolean[3];
        boolean[] name_entered = new boolean[3];
        boolean showError = false;

        EditText name_label1 = findViewById(R.id.label_name1);
        EditText name_label2 = findViewById(R.id.label_name2);
        EditText name_label3 = findViewById(R.id.label_name3);
        EditText value_coordinate1 = findViewById(R.id.coordinate1);
        EditText value_coordinate2 = findViewById(R.id.coordinate2);
        EditText value_coordinate3 = findViewById(R.id.coordinate3);

        coordinate_entered[0] = checkEmpty(value_coordinate1);
        coordinate_entered[1] = checkEmpty(value_coordinate2);
        coordinate_entered[2] = checkEmpty(value_coordinate3);
        name_entered[0] = checkEmpty(name_label1);
        name_entered[1] = checkEmpty(name_label2);
        name_entered[2] = checkEmpty(name_label3);

        // Check for the validity of the inputs given
        // For the inputs to be valid, at least one pair of label and coordinate should be filled and each label should have a coordinate.
        if(name_entered[0] && coordinate_entered[0] && name_entered[1] &&
                coordinate_entered[1] && name_entered[2] && coordinate_entered[2]){
            name_label1.setError("Missing Label!");
            value_coordinate1.setError("Missing Coordinate!");
            Utilities.showError(this, "Please enter at least one label and coordinate.");
        }
        else {
            if(name_entered[0] != coordinate_entered[0]){
                showError = true;
                if(name_entered[0]) name_label1.setError("Missing Label!");
                if(coordinate_entered[0]) value_coordinate1.setError("Missing Coordinate!");
            }
            if(name_entered[1] != coordinate_entered[1]){
                showError = true;
                if(name_entered[1]) name_label2.setError("Missing Label!");
                if(coordinate_entered[1]) value_coordinate2.setError("Missing Coordinate!");
            }
            if(name_entered[2] != coordinate_entered[2]){
                showError = true;
                if(name_entered[2]) name_label3.setError("Missing Label!");
                if(coordinate_entered[2]) value_coordinate3.setError("Missing Coordinate!");
            }
            if(!checkEmpty(value_coordinate1) &&
                    Utilities.validCoordinate(value_coordinate1.getText().toString()) == null){
                showError = true;
            }
            if(!checkEmpty(value_coordinate2) &&
                    Utilities.validCoordinate(value_coordinate2.getText().toString()) == null){
                showError = true;
            }
            if(!checkEmpty(value_coordinate3) &&
                    Utilities.validCoordinate(value_coordinate3.getText().toString()) == null){
                showError = true;
            }
            if(showError){
                Utilities.showError(this, "Please enter valid coordinates/labels.");
            }
            else{
                saveInputs();
                finish();
            }
        }
    }

    // method used to load previously entered inputs to the activity
    public void loadInputs() {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String label1 = preferences.getString("label1", "");
        String label2 = preferences.getString("label2", "");
        String label3 = preferences.getString("label3", "");

        String coordinate1 = preferences.getString("coordinate1", "");
        String coordinate2 = preferences.getString("coordinate2", "");
        String coordinate3 = preferences.getString("coordinate3", "");

        EditText name_label1 = findViewById(R.id.label_name1);
        EditText name_label2 = findViewById(R.id.label_name2);
        EditText name_label3 = findViewById(R.id.label_name3);

        EditText value_coordinate1 = findViewById(R.id.coordinate1);
        EditText value_coordinate2 = findViewById(R.id.coordinate2);
        EditText value_coordinate3 = findViewById(R.id.coordinate3);

        name_label1.setText(label1);
        name_label2.setText(label2);
        name_label3.setText(label3);

        value_coordinate1.setText(coordinate1);
        value_coordinate2.setText(coordinate2);
        value_coordinate3.setText(coordinate3);
    }

    // method used to save the entered inputs to shared preferences
    public void saveInputs() {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        EditText name_label1 = findViewById(R.id.label_name1);
        EditText name_label2 = findViewById(R.id.label_name2);
        EditText name_label3 = findViewById(R.id.label_name3);

        EditText value_coordinate1 = findViewById(R.id.coordinate1);
        EditText value_coordinate2 = findViewById(R.id.coordinate2);
        EditText value_coordinate3 = findViewById(R.id.coordinate3);

        editor.putString("label1", name_label1.getText().toString());
        editor.putString("label2", name_label2.getText().toString());
        editor.putString("label3", name_label3.getText().toString());
        editor.putString("coordinate1", value_coordinate1.getText().toString());
        editor.putString("coordinate2", value_coordinate2.getText().toString());
        editor.putString("coordinate3", value_coordinate3.getText().toString());

        editor.apply();
    }

    // method used to check if a textview is empty
    public boolean checkEmpty(EditText text){
        return text.getText().toString().isEmpty();
    }
}
