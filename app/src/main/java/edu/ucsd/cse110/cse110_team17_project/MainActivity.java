package edu.ucsd.cse110.cse110_team17_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // loadInputs(); should we load inputs instead of just checking if there are coordinates
        // saved and jump to the second activity based on that?
    }

    @Override
    protected void onDestroy() {
        saveInputs();
        super.onDestroy();
    }

    public void onSubmitClicked(View view) throws Exception {
        boolean[] coordinate_entered = new boolean[3];
        boolean[] name_entered = new boolean[3];
        boolean showError = false;

        EditText name_label1 = findViewById(R.id.label_name1);
        EditText name_label2 = findViewById(R.id.label_name2);
        EditText name_label3 = findViewById(R.id.label_name3);
        EditText coordinate_1 = findViewById(R.id.coordinate1);
        EditText coordinate_2 = findViewById(R.id.coordinate2);
        EditText coordinate_3 = findViewById(R.id.coordinate3);

        coordinate_entered[0] = checkEmpty(coordinate_1);
        coordinate_entered[1] = checkEmpty(coordinate_2);
        coordinate_entered[2] = checkEmpty(coordinate_3);
        name_entered[0] = checkEmpty(name_label1);
        name_entered[1] = checkEmpty(name_label2);
        name_entered[2] = checkEmpty(name_label3);

        if(name_entered[0] && coordinate_entered[0] && name_entered[1] &&
                coordinate_entered[1] && name_entered[2] && coordinate_entered[2]){
            name_label1.setError("Missing Label!");
            coordinate_1.setError("Missing Coordinate!");
            Utilities.showError(this, "Please Enter at least one set of coordinates.");
        }
        else {
            // check if label 1 entered
            if(!name_entered[0]){
                if(coordinate_entered[0]){
                    showError = true;
                }
            }
            // check if label 2 entered
            if(!name_entered[1]){
                if(coordinate_entered[1]){
                    showError = true;
                }
            }
            // check if label 3 entered
            if(!name_entered[2]){
                if(coordinate_entered[2]){
                    showError = true;
                }
            }
            if(showError){
                Utilities.showError(this, "Please Enter the Coordinates for Named Labels.");
            }
            else{
                Utilities.showSuccess(this, "Click \"Ok\" to proceed.");
                finish();
            }
        }
    }

    public void loadInputs() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String label1 = preferences.getString("label1", "");
        String label2 = preferences.getString("label2", "");
        String label3 = preferences.getString("label3", "");

        String coordinate1 = preferences.getString("coordinate1", "");
        String coordinate2 = preferences.getString("coordinate2", "");
        String coordinate3 = preferences.getString("coordinate3", "");

        EditText labelField1 = findViewById(R.id.label_name1);
        EditText labelField2 = findViewById(R.id.label_name2);
        EditText labelField3 = findViewById(R.id.label_name3);

        EditText coordinateField1 = findViewById(R.id.coordinate1);
        EditText coordinateField2 = findViewById(R.id.coordinate2);
        EditText coordinateField3 = findViewById(R.id.coordinate3);

        labelField1.setText(label1);
        labelField2.setText(label2);
        labelField3.setText(label3);

        coordinateField1.setText(coordinate1);
        coordinateField2.setText(coordinate2);
        coordinateField3.setText(coordinate3);
    }

    public void saveInputs() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        EditText labelField1 = findViewById(R.id.label_name1);
        EditText labelField2 = findViewById(R.id.label_name2);
        EditText labelField3 = findViewById(R.id.label_name3);

        EditText coordinateField1 = findViewById(R.id.coordinate1);
        EditText coordinateField2 = findViewById(R.id.coordinate2);
        EditText coordinateField3 = findViewById(R.id.coordinate3);

        editor.putString("label1", labelField1.getText().toString());
        editor.putString("label2", labelField2.getText().toString());
        editor.putString("label3", labelField3.getText().toString());
        editor.putString("coordinate1", coordinateField1.getText().toString());
        editor.putString("coordinate2", coordinateField2.getText().toString());
        editor.putString("coordinate3", coordinateField3.getText().toString());

        editor.apply();
    }

    public boolean checkEmpty(EditText text){
        return text.getText().toString().isEmpty();
    }
}