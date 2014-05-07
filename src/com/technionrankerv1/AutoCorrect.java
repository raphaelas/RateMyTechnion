package com.technionrankerv1;

import com.serverapi.TechnionRankerAPI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class AutoCorrect extends Activity implements OnClickListener {
	Button bSend;
	TextView tvStatus;
	
	protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.welcome_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ITEMS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteView);
        textView.setAdapter(adapter);
        
	    tvStatus = (TextView) findViewById(R.id.tvStatus);
	    bSend = (Button) findViewById(R.id.bSend);
	    bSend.setOnClickListener(this);
    }
	
	public void showSearchResults(View view) {
		Intent intent = new Intent(this, SearchResultsList.class);
		startActivity(intent);
		/*
		 * From Android Developer's website: EditText editText = (EditText)
		 * findViewById(R.id.edit_message); String message =
		 * editText.getText().toString(); intent.putExtra(EXTRA_MESSAGE,
		 * message);
		 */
	}

    private static final String[] ITEMS = new String[] {
        "Belgium", "France", "Italy", "Germany", "Spain"
    };
    
	@Override
	public void onClick(View v) {
		tvStatus.setText("wait..");
		ClientAsync as = new ClientAsync();
		as.execute("");
	}
	
	private class ClientAsync extends AsyncTask<String, Void, String> {

	    public ClientAsync() {
	    }

	    @Override
	    protected void onPreExecute() {
	      // TODO Auto-generated method stub
	      super.onPreExecute();
	      tvStatus.setText("wait..");
	    }

	    @Override
	    protected String doInBackground(String... params) {
	      Course c = new Course("01", "Geology", (long) 5, "Spring 2011", true);
	      return new TechnionRankerAPI().insertCourse(c).toString();
	    }

	    @Override
	    protected void onPostExecute(String res) {
	      if (res == null)
	        tvStatus.setText("null");
	      else
	        tvStatus.setText(res);
	    }
	  }
}
