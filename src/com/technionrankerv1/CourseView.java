package com.technionrankerv1;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.serverapi.TechnionRankerAPI;

/**
 * This class requires two variables to be passed in: "studentId" and "courseNumber"
 * @author raphaelas
 *
 */
public class CourseView extends SearchResults {
	Long courseId = Long.valueOf(0);
	boolean alreadySubmitted = false;
	TextView textViewCourseRatingSubmitted;
	ArrayList<CourseComment> comments =  new ArrayList<CourseComment>();
	boolean canSubmit;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.course_view);
    	canSubmit = ((ApplicationWithGlobalVariables) this.getApplication()).canSubmitRatings();
    	displayAllComments(comments);
    	textViewCourseRatingSubmitted = (TextView) findViewById(R.id.textViewCourseRatingSubmitted);
    	final Long studentId = Long.valueOf(0);
    	Bundle bundle = getIntent().getExtras();
    	final String courseNumber = bundle.getString("courseNumber");
    	final String courseName = bundle.getString("courseName");
		String faculty = bundle.getString("faculty");
		TextView facultyText = (TextView) findViewById(R.id.courseFacultyText);
		facultyText.setText(faculty);
		TextView textViewCourseName = (TextView) findViewById(R.id.textViewCourseName);
		textViewCourseName.setText(courseNumber + " - " + courseName);
		//We will need studentId passed in - not currently the case.
    	//final Long studentId = savedInstanceState.getLong("studentId");
    	Course c = new Course(null, null, courseNumber, null, null, null, false);
		ClientAsync as = new ClientAsync();
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
			EditText et = (EditText) findViewById(R.id.comment);
	    	String commentText = et.getText().toString();
	    	if (commentText != null && commentText.length() > 0) {
		    	long currTimeMillis = System.currentTimeMillis();
		    	Time currentTime = new Time(currTimeMillis);
		    	CourseComment cc = new CourseComment(courseId, studentId, commentText, currentTime, 0);
		    	comments.add(cc);
		    	displayAllComments(comments);
		    	CourseCommentClientAsync as2 = new CourseCommentClientAsync();
		    	//as2.execute(cc);
	    	}
		}
    }
	 
	public void displayAllComments(ArrayList<CourseComment> allComments) {
		ListView courseCommentsList = (ListView) findViewById(R.id.courseCommentsList);
	    CommentsListAdapter adapter = new CommentsListAdapter(this, allComments.toArray(new CourseComment[(allComments.size())]));
	    courseCommentsList.setAdapter(adapter);
	}
	
	private class ClientAsync extends AsyncTask<Course, Void, Course> {
		public ClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Course doInBackground(Course... params) {
	    	Course courseToLookUp = params[0];
	    	Course theCourse = new TechnionRankerAPI().getCourse(courseToLookUp);
			return theCourse;
		}

		@Override
		protected void onPostExecute(Course res) {
			if (res == null)
				Log.d(getLocalClassName(), "Course clientAsync unsuccessful");
			else {
				Log.d(getLocalClassName(), res.getName());
		    	courseId = res.getId();
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
	    	String result = "Did nothing"; //new TechnionRankerAPI().insertCourseRating(cr).toString();
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

}
