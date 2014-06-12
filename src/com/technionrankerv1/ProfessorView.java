package com.technionrankerv1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringEscapeUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
	List<ProfessorComment> comments =  new ArrayList<ProfessorComment>();
	boolean canSubmit;
	boolean loggedIn;
	float averageOverall = 0;
	float averageClarity = 0;
	float averagePreparedness = 0;
	float averageInteractivity = 0;
	public RatingBar rOverall;
	public RatingBar rPreparedness;
	public RatingBar rClarity;
	public RatingBar rInteractivity;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.prof_view);
    	studentId = ((ApplicationWithGlobalVariables) this.getApplication()).getStudentID();
    	canSubmit = ((ApplicationWithGlobalVariables) this.getApplication()).canSubmitRatings();
    	loggedIn = ((ApplicationWithGlobalVariables) this.getApplication()).isLoggedIn();
    	textViewProfessorRatingSubmitted = (TextView) findViewById(R.id.textViewProfessorRatingSubmitted);
    	displayAllComments(comments);
		Bundle bundle = getIntent().getExtras();
    	String lookupProfessorName = bundle.getString("professorName");
    	Professor cLookup = new Professor(null, null, null, StringEscapeUtils.escapeJava(lookupProfessorName), true);
    	ClientAsyncGetProfessorByProfessorName cagpbpn = new ClientAsyncGetProfessorByProfessorName();
    	Long pID;
		try {
			pID = cagpbpn.execute(cLookup).get().get(0).getId();
	    	ProfessorRating crLookup = new ProfessorRating(null, null, pID, 0, 0, 0, 0);
	    	GetProfessorRatingsClientAsync gcrca = new GetProfessorRatingsClientAsync();
	    	gcrca.execute(crLookup);
    		GetProfessorCommentsClientAsync gccca = new GetProfessorCommentsClientAsync();
    		ProfessorComment ccLookup = new ProfessorComment(pID, null, null, null, 0);
    		gccca.execute(ccLookup);

		} catch (InterruptedException e) {
			//TODO: better exception handlers.
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	TextView professorNameText = (TextView) findViewById(R.id.professorNameText);
    	professorNameText.setText(lookupProfessorName);
		String faculty = bundle.getString("faculty");
		TextView facultyText = (TextView) findViewById(R.id.professorFacultyText);
		facultyText.setText(faculty);
    	rOverall = (RatingBar) findViewById(R.id.professorRatingBarOverall);
    	rClarity = (RatingBar) findViewById(R.id.professorRatingBarClarity);
    	rPreparedness = (RatingBar) findViewById(R.id.professorRatingBarPreparedness);
    	rInteractivity = (RatingBar) findViewById(R.id.professorRatingBarInteractivity);
    	
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
    	if (!loggedIn) {
        	rOverall.setIsIndicator(true);
        	rClarity.setIsIndicator(true);
        	rPreparedness.setIsIndicator(true);
        	rInteractivity.setIsIndicator(true);
        	ratingButton.setVisibility(View.GONE);
			EditText et = (EditText) findViewById(R.id.professorComment);
			et.setHint("Please sign in to submit a rating.");
			et.setHintTextColor(getResources().getColor(R.color.gray));
			et.setGravity(Gravity.CENTER);
			et.setFocusable(false);
    	}
    	rOverall.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				pr.setOverallRating(rating);
			}
    	});
    	
    	rPreparedness.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				pr.setPreparedness(rating);
			}
    	});
    	
    	rClarity.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				pr.setClarity(rating);
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
			InsertProfessorRatingClientAsync as3 = new InsertProfessorRatingClientAsync();
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
	    	String studentName = ((ApplicationWithGlobalVariables) this.getApplication()).getStudentName();
			String commentText = et.getText().toString();
	    	commentText = studentName + ": " + commentText;
			ProfessorComment pc = new ProfessorComment(professorId, studentId, commentText, null, 0);
			comments.add(pc);
			displayAllComments(comments);
			InsertProfessorCommentClientAsync as2 = new InsertProfessorCommentClientAsync();
			as2.execute(pc);
		}
	}
	
	public void displayAllComments(List<ProfessorComment> allComments) {
		ListView professorCommentsList = (ListView) findViewById(R.id.professorCommentsList);
	    ProfessorCommentsListAdapter adapter = new ProfessorCommentsListAdapter(
	    		this, allComments.toArray(new ProfessorComment[(allComments.size())]));
	    TextView emptyComments = (TextView) findViewById(R.id.emptyProfessorComments);
	    professorCommentsList.setEmptyView(emptyComments);
	    professorCommentsList.setAdapter(adapter);
	}
	
	private class ClientAsyncGetProfessorByProfessorName extends AsyncTask<Professor, Void, List<Professor>> {
		public ClientAsyncGetProfessorByProfessorName() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Professor> doInBackground(Professor... params) {
	    	Professor professorToLookUp = params[0];
	    	List<Professor> theProfessors = new TechnionRankerAPI().
	    			getProfessorByProfessorHebrewName(professorToLookUp);
			return theProfessors;
		}

		@Override
		protected void onPostExecute(List<Professor> res) {
			if (res == null)
				Log.d(getLocalClassName(), "GetProfessor clientAsync unsuccessful");
			else if (res.isEmpty()) {
				Log.d(getLocalClassName(), "GetProfessor clientAsync returned empty.");
			}
			else {
				Log.d(getLocalClassName(), "GetProfessor clientAsync successful");
		    	professorId = res.get(0).getId();
		    	professor = res.get(0);
			}
		}
	}
	
	
	private class InsertProfessorRatingClientAsync extends AsyncTask<ProfessorRating, Void, String> {
		public InsertProfessorRatingClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			Log.d(getLocalClassName(), "Starting InsertProfessorRatingClientAsync...");
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
	
	
	private class InsertProfessorCommentClientAsync extends AsyncTask<ProfessorComment, Void, String> {
		public InsertProfessorCommentClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			Log.d(getLocalClassName(), "Starting InsertProfessorCommentClientAsync...");
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
	
	private class GetProfessorRatingsClientAsync extends AsyncTask<ProfessorRating, Void, List<ProfessorRating>> {
		public GetProfessorRatingsClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			Log.d(getLocalClassName(), "Starting ProfessorCommentClientAsync...");
			super.onPreExecute();
		}

		@Override
		protected List<ProfessorRating> doInBackground(ProfessorRating... params) {
	    	ProfessorRating cr = params[0];
	    	List<ProfessorRating> result = new TechnionRankerAPI().getProfessorRatingByProfessor(cr);
			return result;
		}

		@Override
		protected void onPostExecute(List<ProfessorRating> res) {
			if (res == null) {
				Log.d(getLocalClassName(), "Get ProfessorRatings failed.");
			}
			else if (res.isEmpty()) {
				Log.d(getLocalClassName(), "Get ProfessorRatings returned empty.");
			}
			else {
				Log.d(getLocalClassName(), "Get ProfessorRatings succeeded.");
		    	List<ProfessorRating> currRatings = res;
		    	int totalRatings = currRatings.size();
		    	averageOverall = 0;
		    	averageClarity = 0;
		    	averagePreparedness = 0;
		    	averageInteractivity = 0;
		    	for (ProfessorRating pRating: currRatings) {
		    		Long tempStudentId = pRating.getStudentID();
		    		double tempOverallRating = pRating.getOverallRating();
		    		double tempClarity = pRating.getClarity();
		    		double tempPreparedness = pRating.getPreparedness();
		    		double tempInteractivity = pRating.getInteractivity();
		    		if (tempStudentId.equals(studentId)) {
		    			alreadySubmitted = true;
		    		}
		    		else if (tempStudentId < 1000) {
		    			totalRatings += tempStudentId;
		    			tempOverallRating *= totalRatings;
		    			tempClarity *= totalRatings;
		    			tempPreparedness *= totalRatings;
		    			tempInteractivity *= totalRatings;
		    		}
					averageOverall += tempOverallRating;
					averageClarity += tempClarity;
					averagePreparedness += tempPreparedness;
					averageInteractivity += tempInteractivity;

		    	}
	    		averageOverall /= totalRatings;
	    		averageClarity /= totalRatings;
	    		averagePreparedness /= totalRatings;
	    		averageInteractivity /= totalRatings;
	    		Log.d(getLocalClassName(), "" + averageOverall);
	        	rOverall.setRating(averageOverall);
	        	rPreparedness.setRating(averagePreparedness);
	        	rClarity.setRating(averageClarity);
	        	rInteractivity.setRating(averageInteractivity);
			}
		}
	}
	
	private class GetProfessorCommentsClientAsync extends AsyncTask<ProfessorComment, Void, List<ProfessorComment>> {
		public GetProfessorCommentsClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			Log.d(getLocalClassName(), "Starting GetProfessorCommentClientAsync...");
			super.onPreExecute();
		}

		@Override
		protected List<ProfessorComment> doInBackground(ProfessorComment... params) {
	    	ProfessorComment cc = params[0];
	    	List<ProfessorComment> result = new TechnionRankerAPI().getProfessorCommentByProfessor(cc);
			return result;
		}

		@Override
		protected void onPostExecute(List<ProfessorComment> res) {
			if (res == null) {
				Log.d(getLocalClassName(), "Get ProfessorComments failed.");
			}
			else if (res.isEmpty()) {
				Log.d(getLocalClassName(), "Get ProfessorComments returned empty.");
			}
			else {
				Log.d(getLocalClassName(), "Get ProfessorComments succeeded.");
				comments = res;
		    	displayAllComments(comments);
			}
		}
	}
}
