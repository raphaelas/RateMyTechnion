package com.technionrankerv1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.serverapi.TechnionRankerAPI;

public class AutoCorrect extends Activity implements OnClickListener {
	Button bSend;
	TextView tvStatus;
	
	protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.welcome_view);
        String[] professors = null;
        try {
        	professors = concat(parse(), parseProfessors());
        }
        catch (Exception e) {
			e.printStackTrace();
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, professors);
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

	public String[] parseProfessors() throws Exception {
		ArrayList<String> profList= new ArrayList<String>();
		String inputLine = "";
		String[] temp;
		String[] profListArray = null;
		String[] professorFiles = getAssets().list("ProfessorListings");
		for (int i = 0; i < professorFiles.length; i++) {
		    BufferedReader infile = new BufferedReader(new InputStreamReader(getAssets().open("ProfessorListings/" + professorFiles[i])));
			while (infile.ready()) {// while more info exists
			inputLine = infile.readLine();
			if(inputLine.startsWith("<td><a href=")){
				inputLine = inputLine.substring(1,inputLine.length()-9);
				temp = inputLine.split(">");
				profList.add(temp[2]);
			}
			else if (inputLine.startsWith("            <td class=")) {
				try {
					String email = inputLine.substring(53, inputLine.length()-48);
				}
				catch (StringIndexOutOfBoundsException e) {
					Log.d(getLocalClassName(), "Email unavailable");
				}
			}
		}
		profListArray = profList.toArray(new String[profList.size()]);
		infile.close();	
		}
	return profListArray;
	}
	
	public String[] parse() throws Exception { 
		 //create Hashmap, where the numbers are the keys and the Titles are the values
		HashMap<String,String> map = new HashMap<String,String>();
		String inputLine = "";
		String[] temp;
		String[] allCourses = new String[0];
		String[] courseFiles = getAssets().list("CourseListings");
		for (int i = 0; i < courseFiles.length; i++) {
			int lineNumber = 0;
		    BufferedReader infile = new BufferedReader(new InputStreamReader(getAssets().open("CourseListings/" + courseFiles[i])));
			while (infile.ready()) {// while more info exists
				if(lineNumber>=13 && lineNumber%3==1){ // only take numbers 13 and up for every 3
					temp = inputLine.split(" - ");
					for (int t = 0; t<temp.length; t++){
						String number = temp[0].trim();//number;
						String name = temp[1].replaceAll("</A>","").trim();//name
						map.put(number,name); // trim and place only the number and name in
					} //for temp
				} //if
				inputLine = infile.readLine(); // read the next line of the text
				lineNumber++;
			} //while infile
			infile.close();	
			Object[] temp1 = map.values().toArray();
			String[] oneFacultyCourses = Arrays.copyOf(temp1, temp1.length, String[].class);
			allCourses = concat(allCourses, oneFacultyCourses);
		} //for courseFiles
		return allCourses;
	} //parse()
	
	String[] concat(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] c= new String[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
    
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
