package com.technionrankerv1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.serverapi.TechnionRankerAPI;

/**
 * This class requires two variables to be passed in: "professorName" and "studentId"
 * @author raphaelas
 *
 */
public class ProfessorView extends SearchResults {
    public Long professorId = Long.valueOf(0);
    public Professor professor;
    public boolean alreadySubmitted = false;
    public TextView textViewProfessorRatingSubmitted;


	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.prof_view);
    	textViewProfessorRatingSubmitted = (TextView) findViewById(R.id.textViewProfessorRatingSubmitted);
		textViewProfessorRatingSubmitted.setMaxLines(1);
		Bundle bundle = getIntent().getExtras();
    	String lookupProfessorName = bundle.getString("professorName");
    	TextView professorNameText = (TextView) findViewById(R.id.professorNameText);
    	professorNameText.setText(lookupProfessorName);
    	Professor lookupProfessor = new Professor(null, lookupProfessorName, true);
    	/* Bring this back as soon as our database really works:
    	ProfessorClientAsync as = new ProfessorClientAsync();
    	as.execute(lookupProfessor);
    	try {
			as.get(); //This will block until the professor is gotten.
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	 */
    	final Long studentId = Long.valueOf(0); //savedInstanceState.getLong("studentId");
    	/* Bring back when we implement comments:
    	Button commentButton = (Button) findViewById(R.id.professorCommentButton);
    	commentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createProfessorComment(professorId, studentId);
			}
		}); */
    	RatingBar rOverall = (RatingBar) findViewById(R.id.professorRatingBarOverall);
    	RatingBar rClarity = (RatingBar) findViewById(R.id.professorRatingBarClarity);
    	RatingBar rPreparedness = (RatingBar) findViewById(R.id.professorRatingBarPreparedness);
    	RatingBar rInteractivity = (RatingBar) findViewById(R.id.professorRatingBarInteractivity);
    	
    	final ProfessorRating pr = new ProfessorRating(null, studentId, professorId,
    			Math.round(rOverall.getRating()), Math.round(rClarity.getRating()),
    			Math.round(rPreparedness.getRating()), Math.round(rInteractivity.getRating()));
    	Button ratingButton = (Button) findViewById(R.id.professorRatingButton);
    	ratingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveProfessorRating(pr);
			}
		});
    	rOverall.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				pr.setOverallRating(Math.round(rating));
			}
    	});
    	
    	rPreparedness.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				pr.setPreparedness(Math.round(rating));
			}
    	});
    	
    	rOverall.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				pr.setOverallRating(Math.round(rating));
			}
    	});
    	
    	rInteractivity.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				pr.setInteractivity(Math.round(rating));
			}
    	});
    }

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		// Get the SearchView and set the searchable configuration
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		// SearchView searchView = (SearchView)
		// menu.findItem(R.id.action_search)
		// .getActionView();
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the
		// widget;
		// expand it by default
		// searchView.setSubmitButtonEnabled(true);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items

		switch (item.getItemId()) {
		case R.id.action_logout:
			// openLoginPage(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	*/
	
	protected void saveProfessorRating(ProfessorRating pr) {
		if (!alreadySubmitted) {
			textViewProfessorRatingSubmitted.setTextColor(getResources().getColor(R.color.gray));
			textViewProfessorRatingSubmitted.setText("Please wait while we record your response.");
			ProfessorRatingClientAsync as3 = new ProfessorRatingClientAsync();
			as3.execute(pr);
		}
		else {
			textViewProfessorRatingSubmitted.setTextColor(getResources().getColor(R.color.gray));
			textViewProfessorRatingSubmitted.setText("Whoops, you already submitted and cannot submit again.");
		}
	}

	protected void createProfessorComment(Long professorId, Long studentId) {
		/*
    	EditText et = (EditText) findViewById(R.id.professorComment);
    	String commentText = et.getText().toString();
    	long currTimeMillis = System.currentTimeMillis();
    	Time currentTime = new Time(currTimeMillis);
    	ProfessorComment pc = new ProfessorComment(professorId, studentId, commentText, currentTime, 0);
    	new TechnionRankerAPI().insertProfessorComment(pc);
    	*/
	}
	
	private class ProfessorClientAsync extends AsyncTask<Professor, Void, Professor> {
		public ProfessorClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//tvStatus.setText("wait..");
		}

		@Override
		protected Professor doInBackground(Professor... params) {
			//Course c = new Course(null, "Geology", "01", (long) 5,
			//		"Spring 2011", true);
			//return new TechnionRankerAPI().insertCourse(c).toString();
			//TODO: check that 50 - 100 succeeded.
	    	Professor professorToLookUp = params[0];
	    	Professor theProfessor = new TechnionRankerAPI().getProfessor(professorToLookUp);
			return theProfessor;
		}

		@Override
		protected void onPostExecute(Professor res) {
			if (res == null)
				Log.d(getLocalClassName(), "Professor clientAsync unsuccessful");
			else {
			    //delegate.processFinish(res);
				Log.d(getLocalClassName(), res.getName());
		    	professorId = res.getId();
		    	professor = res;
			}
		}
	}
	
	
	private class ProfessorRatingClientAsync extends AsyncTask<ProfessorRating, Void, String> {
		public ProfessorRatingClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(ProfessorRating... params) {
	    	ProfessorRating pr = params[0];
	    	String result = new TechnionRankerAPI().insertProfessorRating(pr).toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null) {
				//Log.d(getLocalClassName(), "ProfessorRating ClientAsync unsuccessful");
				textViewProfessorRatingSubmitted.setTextColor(getResources().getColor(R.color.red));
				textViewProfessorRatingSubmitted.setText("Sorry, please try submitting your rating again.");
			}
			else {
			    //delegate.processFinish(res);
				//Log.d(getLocalClassName(), res);
				textViewProfessorRatingSubmitted.setTextColor(getResources().getColor(R.color.white));
				textViewProfessorRatingSubmitted.setText("Thank you.  Your rating was received.");
				alreadySubmitted = true;
			}
		}
	}
	
	/*
	private class ProfessorCommentClientAsync extends AsyncTask<ProfessorComment, Void, String> {
		public ProfessorCommentClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(ProfessorComment... params) {
	    	ProfessorComment pc = params[0];
	    	String result = new TechnionRankerAPI().insertProfessorComment(pc).toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null)
				Log.d(getLocalClassName(), "ProfessorComment clientAsync unsuccessful");
			else {
			    //delegate.processFinish(res);
				Log.d(getLocalClassName(), res);
			}
		}
	}
	*/
}
