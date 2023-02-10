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
        loadInputs();
    }

    @Override
    protected void onDestroy() {
        saveInputs();
        super.onDestroy();
    }

    public void onSubmitbtnClicked(View view) throws Exception {
        EditText name_label1 = findViewById(R.id.label_name1);
        EditText name_label2 = findViewById(R.id.label_name2);
        EditText name_label3 = findViewById(R.id.label_name3);
        if(name_label1.getText().toString().isEmpty() && name_label2.getText().toString().isEmpty() && name_label3.getText().toString().isEmpty()){
            name_label1.setError("Missing Names!");
            name_label2.setError("Missing Names!");
            name_label3.setError("Missing Names!");
        }

        EditText coordinate_1 = findViewById(R.id.coordinate1);
        EditText coordinate_2 = findViewById(R.id.coordinate2);
        EditText coordinate_3 = findViewById(R.id.coordinate3);
        if(coordinate_1.getText().toString().isEmpty() && coordinate_2.getText().toString().isEmpty() && coordinate_3.getText().toString().isEmpty()){
            coordinate_1.setError("Missing Coordinates!");
            coordinate_2.setError("Missing Coordinates!");
            coordinate_3.setError("Missing Coordinates!");
        }
        finish();
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
}