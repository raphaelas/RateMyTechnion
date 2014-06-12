package com.technionrankerv1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
 * This class requires one variable to be passed in: "courseNumber"
 * @author raphaelas
 *
 */
public class CourseView extends SearchResults {
	Long courseId = Long.valueOf(0);
	Long studentId = Long.valueOf(0);
	boolean alreadySubmitted = false;
	TextView textViewCourseRatingSubmitted;
	List<CourseComment> comments =  new ArrayList<CourseComment>();
	boolean canSubmit;
	boolean loggedIn;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.course_view);
    	studentId = ((ApplicationWithGlobalVariables) this.getApplication()).getStudentID();
    	canSubmit = ((ApplicationWithGlobalVariables) this.getApplication()).canSubmitRatings();
    	loggedIn = ((ApplicationWithGlobalVariables) this.getApplication()).isLoggedIn();
    	textViewCourseRatingSubmitted = (TextView) findViewById(R.id.textViewCourseRatingSubmitted);
    	Bundle bundle = getIntent().getExtras();
    	final String courseNumber = bundle.getString("courseNumber");
    	final String courseName = bundle.getString("courseName");
    	Course cLookup = new Course(null, null, courseNumber, null, null, null, true);
    	ClientAsyncGetCourseByCourseNumber cagcbcn = new ClientAsyncGetCourseByCourseNumber();
    	Long cID;
    	float averageOverall = 0;
    	float averageEnjoyability = 0;
    	float averageUsefulness = 0;
    	float averageDifficulty = 0;
		try {
			cID = cagcbcn.execute(cLookup).get().get(0).getId();
	    	CourseRating crLookup = new CourseRating(null, cID, 0, 0, 0, 0);
	    	GetCourseRatingsClientAsync gcrca = new GetCourseRatingsClientAsync();
	    	List<CourseRating> currRatings = gcrca.execute(crLookup).get();
	    	int totalRatings = currRatings.size();
	    	averageOverall = 0;
	    	averageEnjoyability = 0;
	    	averageUsefulness = 0;
	    	averageDifficulty = 0;
	    	for (CourseRating cRating: currRatings) {
	    		Long tempStudentId = cRating.getStudentID();
	    		averageOverall += cRating.getOverallRating();
	    		averageEnjoyability += cRating.getEnjoyability();
	    		averageUsefulness += cRating.getUsefulness();
	    		averageDifficulty += cRating.getDifficulty();
	    		if (tempStudentId.equals(studentId)) {
	    			alreadySubmitted = true;
	    		}
	    	}
    		averageOverall /= totalRatings;
    		averageEnjoyability /= totalRatings;
    		averageUsefulness /= totalRatings;
    		averageDifficulty /= totalRatings;
    		GetCourseCommentsClientAsync gccca = new GetCourseCommentsClientAsync();
    		CourseComment ccLookup = new CourseComment(cID, null, null, null, 0);
    		comments = gccca.execute(ccLookup).get();
	    	displayAllComments(comments);
		} catch (InterruptedException e) {
			//TODO: better exception handlers.
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		String faculty = bundle.getString("faculty");
		TextView facultyText = (TextView) findViewById(R.id.courseFacultyText);
		facultyText.setText(faculty);
		TextView textViewCourseName = (TextView) findViewById(R.id.textViewCourseName);
		textViewCourseName.setText(courseNumber + " - " + courseName);

    	Course c = new Course(null, null, courseNumber, null, null, null, false);
		ClientAsyncGetCourseByCourseNumber as = new ClientAsyncGetCourseByCourseNumber();
		as.execute(c);
		/* Bring this back once our database really works:
		try {
			as.get(); //This will block until as.execute completes
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    	
    	RatingBar rOverall = (RatingBar) findViewById(R.id.ratingBarOverall);
    	RatingBar rEnjoyability = (RatingBar) findViewById(R.id.ratingBarEnjoyability);
    	RatingBar rUsefulness = (RatingBar) findViewById(R.id.ratingBarUsefulness);
    	RatingBar rDifficulty = (RatingBar) findViewById(R.id.ratingBarDifficulty);
    	final CourseRating cr = new CourseRating(studentId, Long.valueOf(courseNumber),
    			Math.round(rOverall.getRating()), Math.round(rEnjoyability.getRating()),
    			Math.round(rUsefulness.getRating()), Math.round(rDifficulty.getRating()));
    	Button ratingButton = (Button) findViewById(R.id.rating_button);
    	ratingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveRatings(cr);
				createComment(cr.getCourseId(), cr.getStudentID());
				alreadySubmitted = true;
			}
		});
    	if (!loggedIn) {
        	rOverall.setIsIndicator(true);
        	rEnjoyability.setIsIndicator(true);
        	rUsefulness.setIsIndicator(true);
        	rDifficulty.setIsIndicator(true);
        	ratingButton.setVisibility(View.GONE);
			EditText et = (EditText) findViewById(R.id.comment);
			et.setHint("Please sign in to submit a rating.");
			et.setHintTextColor(getResources().getColor(R.color.gray));
			et.setGravity(Gravity.CENTER);
			et.setFocusable(false);
    	}
    	rOverall.setRating(averageOverall);
    	rEnjoyability.setRating(averageEnjoyability);
    	rUsefulness.setRating(averageUsefulness);
    	rDifficulty.setRating(averageDifficulty);
    	rOverall.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				cr.setOverallRating(Math.round(rating));
			}
    	});
    	rEnjoyability.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {		
				cr.setEnjoyability(Math.round(rating));
			}
    	});
    	rUsefulness.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {		
				cr.setUsefulness(Math.round(rating));
			}
    	});
    	rDifficulty.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {		
				cr.setDifficulty(Math.round(rating));
			}
    	});
    	

    	/**
    	 * This was copied from StackOverflow to allow for the comments
    	 * ListView to be inside a ScrollView (without this code, only
    	 * the first comment would be displayed)
    	 */
    	ListView lv = (ListView)findViewById(R.id.courseCommentsList);  // your listview inside scrollview
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
    
    protected void saveRatings(CourseRating cr) {
    	if (!canSubmit) {
			textViewCourseRatingSubmitted.setTextColor(getResources().getColor(R.color.gray));
			textViewCourseRatingSubmitted.setText("Whoops, you've reached the limit for posting ratings this semester.");
    	}
    	else if (!alreadySubmitted) {
			textViewCourseRatingSubmitted.setTextColor(getResources().getColor(R.color.gray));
			textViewCourseRatingSubmitted.setText("Please wait while we record your response.");
			CourseRatingClientAsync as3 = new CourseRatingClientAsync();
			as3.execute(cr);
		}
		else {
			textViewCourseRatingSubmitted.setTextColor(getResources().getColor(R.color.gray));
			textViewCourseRatingSubmitted.setText("Whoops, you already submitted and cannot submit again.");
		}
    }
    
	public void createComment(Long courseId, Long studentId) {
		if (!alreadySubmitted && canSubmit) {
			((ApplicationWithGlobalVariables) this.getApplication()).incrementRatingsSubmitted();
	    	canSubmit = ((ApplicationWithGlobalVariables) this.getApplication()).canSubmitRatings();
	    	String studentName = ((ApplicationWithGlobalVariables) this.getApplication()).getStudentName();
			EditText et = (EditText) findViewById(R.id.comment);
	    	String commentText = et.getText().toString();
	    	commentText = studentName + ": " + commentText;
	    	if (commentText != null && commentText.length() > 0) {
		    	CourseComment cc = new CourseComment(courseId, studentId, commentText, null, 0);
		    	comments.add(cc);
		    	displayAllComments(comments);
		    	CourseCommentClientAsync as2 = new CourseCommentClientAsync();
		    	as2.execute(cc);
	    	}
		}
    }
	 
	public void displayAllComments(List<CourseComment> comments2) {
		ListView courseCommentsList = (ListView) findViewById(R.id.courseCommentsList);
	    CourseCommentsListAdapter adapter = new CourseCommentsListAdapter(
	    		this, comments2.toArray(new CourseComment[(comments2.size())]));
	    TextView emptyComments = (TextView) findViewById(R.id.emptyCourseComments);
	    courseCommentsList.setEmptyView(emptyComments);
	    courseCommentsList.setAdapter(adapter);
	}
	
	private class ClientAsyncGetCourseByCourseNumber extends AsyncTask<Course, Void, List<Course>> {
		public ClientAsyncGetCourseByCourseNumber() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Course> doInBackground(Course... params) {
	    	Course courseToLookUp = params[0];
	    	List<Course> theCourse = new TechnionRankerAPI().getCourseByCourseNumber(courseToLookUp);
			return theCourse;
		}

		@Override
		protected void onPostExecute(List<Course> res) {
			if (res == null)
				Log.d(getLocalClassName(), "Course clientAsync unsuccessful");
			else {
				Log.d(getLocalClassName(), res.get(0).getName());
		    	courseId = res.get(0).getId();
			}
		}
	}
	
	
	private class CourseCommentClientAsync extends AsyncTask<CourseComment, Void, String> {
		public CourseCommentClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(CourseComment... params) {
	    	CourseComment cc = params[0];
	    	String result = new TechnionRankerAPI().insertCourseComment(cc).toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null)
				Log.d(getLocalClassName(), "CourseComment clientAsync unsuccessful");
			else {
				Log.d(getLocalClassName(), "CourseComment saving: " + res);
			}
		}
	}
	
	
	private class CourseRatingClientAsync extends AsyncTask<CourseRating, Void, String> {
		public CourseRatingClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(CourseRating... params) {
	    	CourseRating cr = params[0];
	    	String result = new TechnionRankerAPI().insertCourseRating(cr).toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null) {
				textViewCourseRatingSubmitted.setTextColor(getResources().getColor(R.color.red));
				textViewCourseRatingSubmitted.setText("Sorry, please try submitting your rating again.");
			}
			
			else {
				textViewCourseRatingSubmitted.setTextColor(getResources().getColor(R.color.white));
				textViewCourseRatingSubmitted.setText("Thank you.  Your rating was received.");
			}
		}
	}
	
	private class GetCourseRatingsClientAsync extends AsyncTask<CourseRating, Void, List<CourseRating>> {
		public GetCourseRatingsClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<CourseRating> doInBackground(CourseRating... params) {
	    	CourseRating cr = params[0];
	    	List<CourseRating> result = new TechnionRankerAPI().getCourseRatingByCourse(cr);
			return result;
		}

		@Override
		protected void onPostExecute(List<CourseRating> res) {
			if (res == null) {
				Log.d(getLocalClassName(), "Get CourseRatings failed.");
			}
			
			else {
				Log.d(getLocalClassName(), "Get CourseRatings succeeded.");
			}
		}
	}
	
	private class GetCourseCommentsClientAsync extends AsyncTask<CourseComment, Void, List<CourseComment>> {
		public GetCourseCommentsClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<CourseComment> doInBackground(CourseComment... params) {
	    	CourseComment cc = params[0];
	    	List<CourseComment> result = new TechnionRankerAPI().getCourseCommentByCourse(cc);
			return result;
		}

		@Override
		protected void onPostExecute(List<CourseComment> res) {
			if (res == null) {
				Log.d(getLocalClassName(), "Get CourseComments failed.");

			}
			else {
				Log.d(getLocalClassName(), "Get CourseComments succeeded.");

			}
		}
	}
}
