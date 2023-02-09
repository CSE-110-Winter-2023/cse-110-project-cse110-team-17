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
    }
}