package com.technionrankerv1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class AutoCorrect extends Activity{
	protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.welcome_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ITEMS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteView);
        textView.setAdapter(adapter);
    }

    private static final String[] ITEMS = new String[] {
        "Belgium", "France", "Italy", "Germany", "Spain"
    };

}
