package com.technionrankerv1;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    public Long studentId = Long.valueOf(0);
    public Professor professor;
    public boolean alreadySubmitted = false;
    public TextView textViewProfessorRatingSubmitted;
	ArrayList<ProfessorComment> comments =  new ArrayList<ProfessorComment>();
	boolean canSubmit;




	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.prof_view);
    	studentId = ((ApplicationWithGlobalVariables) this.getApplication()).getStudentID();
    	canSubmit = ((ApplicationWithGlobalVariables) this.getApplication()).canSubmitRatings();
    	textViewProfessorRatingSubmitted = (TextView) findViewById(R.id.textViewProfessorRatingSubmitted);
    	displayAllComments(comments);
		Bundle bundle = getIntent().getExtras();
    	String lookupProfessorName = bundle.getString("professorName");
    	TextView professorNameText = (TextView) findViewById(R.id.professorNameText);
    	professorNameText.setText(lookupProfessorName);
		String faculty = bundle.getString("faculty");
		TextView facultyText = (TextView) findViewById(R.id.professorFacultyText);
		facultyText.setText(faculty);
    	Professor lookupProfessor = new Professor(null, lookupProfessorName, null, null, true);
    	ProfessorClientAsync as = new ProfessorClientAsync();
    	as.execute(lookupProfessor);
    	/* Bring this back as soon as our database really works:
    	try {
			as.get(); //This will block until the professor is gotten.
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	 */
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
				createProfessorComment(pr.getProfessorID(), pr.getStudentID());
				alreadySubmitted = true;
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
    	
    	ListView lv = (ListView)findViewById(R.id.professorCommentsList);  // your listview inside scrollview
    	lv.setOnTouchListener(new ListView.OnTouchListener() {
    	        @Override
    	        public boolean onTouch(View v, MotionEvent event) {
    	            int action = event.getAction();
    	            switch (action) {
    	            case MotionEvent.ACTION_DOWN:
    	                // Disallow ScrollView to intercept touch events.
    	                v.getParent().requestDisallowInterceptTouchEvent(true);
    	                break;

    	            case MotionEvent.ACTION_UP:
    	                // Allow ScrollView to intercept touch events.
    	                v.getParent().requestDisallowInterceptTouchEvent(false);
    	                break;
    	            }

    	            // Handle ListView touch events.
    	            v.onTouchEvent(event);
    	            return true;
    	        }
    	    });
    }
	
	protected void saveProfessorRating(ProfessorRating pr) {
    	if (!canSubmit) {
			textViewProfessorRatingSubmitted.setTextColor(getResources().getColor(R.color.gray));
			textViewProfessorRatingSubmitted.setText("Whoops, you've reached the limit for posting ratings this semester.");
    	}
    	else if (!alreadySubmitted) {
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
		if (!alreadySubmitted && canSubmit) {
			EditText et = (EditText) findViewById(R.id.professorComment);
			String commentText = et.getText().toString();
			long currTimeMillis = System.currentTimeMillis();
			Time currentTime = new Time(currTimeMillis);
			ProfessorComment pc = new ProfessorComment(professorId, studentId, commentText, currentTime, 0);
			comments.add(pc);
			displayAllComments(comments);
			ProfessorCommentClientAsync as2 = new ProfessorCommentClientAsync();
			//as2.execute(pc);
		}
	}
	
	public void displayAllComments(ArrayList<ProfessorComment> allComments) {
		ListView professorCommentsList = (ListView) findViewById(R.id.professorCommentsList);
	    ProfessorCommentsListAdapter adapter = new ProfessorCommentsListAdapter(
	    		this, allComments.toArray(new ProfessorComment[(allComments.size())]));
	    TextView emptyComments = (TextView) findViewById(R.id.emptyProfessorComments);
	    professorCommentsList.setEmptyView(emptyComments);
	    professorCommentsList.setAdapter(adapter);
	}
	
	private class ProfessorClientAsync extends AsyncTask<Professor, Void, List<Professor>> {
		public ProfessorClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Professor> doInBackground(Professor... params) {
	    	//Professor professorToLookUp = params[0];
	    	//Professor theProfessor = new TechnionRankerAPI().getProfessor(professorToLookUp);
	    	List<Professor> allProfessors = new TechnionRankerAPI().getAllProfessors();
			return allProfessors;
		}

		@Override
		protected void onPostExecute(List<Professor> res) {
			if (res == null)
				Log.d(getLocalClassName(), "Professor clientAsync unsuccessful");
			else {
				Log.d(getLocalClassName(), res.get(0).getName());
		    	professorId = res.get(0).getId();
		    	professor = res.get(0);
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
				//Log.d(getLocalClassName(), res);
				textViewProfessorRatingSubmitted.setTextColor(getResources().getColor(R.color.white));
				textViewProfessorRatingSubmitted.setText("Thank you.  Your rating was received.");
			}
		}
	}
	
	
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
				Log.d(getLocalClassName(), res);
			}
		}
	}
}
