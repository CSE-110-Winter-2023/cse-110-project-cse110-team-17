package edu.ucsd.cse110.cse110_team17_project;

import androidx.appcompat.app.AppCompatActivity;

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
            }
        }
    }

    public boolean checkEmpty(EditText text){
        return text.getText().toString().isEmpty();
    }
}