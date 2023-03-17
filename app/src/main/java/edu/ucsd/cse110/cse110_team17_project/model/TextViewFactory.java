package edu.ucsd.cse110.cse110_team17_project.model;

import android.widget.LinearLayout;
import android.widget.TextView;

import edu.ucsd.cse110.cse110_team17_project.R;


public class TextViewFactory {
    TextView sample;
    int layout;

    public TextViewFactory(TextView tv, int layout){
        this.layout = layout;
        this.sample = tv;
    }
    public TextView makeTextView(){
        TextView textView = new TextView(sample.getContext());
        textView.setTextSize(sample.getTextSize());
        textView.setTextColor(sample.getTextColors());

        return textView;
    }
}
