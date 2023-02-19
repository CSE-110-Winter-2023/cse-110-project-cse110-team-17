package edu.ucsd.cse110.cse110_team17_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String label1 = preferences.getString("label1", "");
        String label2 = preferences.getString("label2", "");
        String label3 = preferences.getString("label3", "");
        String coordinate1 = preferences.getString("coordinate1", "");
        String coordinate2 = preferences.getString("coordinate2", "");
        String coordinate3 = preferences.getString("coordinate3", "");

        if(label1.isEmpty() && label2.isEmpty() && label3.isEmpty()){
            Intent inputIntent = new Intent(this, InputActivity.class);
            startActivity(inputIntent);
        }

        TextView name_label1 = (TextView) findViewById(R.id.label_1);
        TextView name_label2 = (TextView) findViewById(R.id.label_2);
        TextView name_label3 = (TextView) findViewById(R.id.label_3);
        name_label1.setText(label1);
        name_label2.setText(label2);
        name_label3.setText(label3);
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        String label1 = preferences.getString("label1", "a");
        String label2 = preferences.getString("label2", "b");
        String label3 = preferences.getString("label3", "c");
        String coordinate1 = preferences.getString("coordinate1", "");
        String coordinate2 = preferences.getString("coordinate2", "");
        String coordinate3 = preferences.getString("coordinate3", "");
        TextView name_label1 = (TextView) findViewById(R.id.label_1);
        TextView name_label2 = (TextView) findViewById(R.id.label_2);
        TextView name_label3 = (TextView) findViewById(R.id.label_3);
        name_label1.setText(label1);
        name_label2.setText(label2);
        name_label3.setText(label3);
    }

    public void onBackClicked(View view) {
        Intent inputIntent = new Intent(this, InputActivity.class);
        startActivity(inputIntent);
    }

    public void onClearClicked(View view) {
        SharedPreferences preferences = getSharedPreferences("MAIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        finish();
    }
}