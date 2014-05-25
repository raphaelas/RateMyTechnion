package com.technionrankerv1;

import java.sql.Time;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.course_view);
    	textViewCourseRatingSubmitted = (TextView) findViewById(R.id.textViewCourseRatingSubmitted);
		textViewCourseRatingSubmitted.setMaxLines(1);
    	final Long studentId = Long.valueOf(0);
    			Bundle bundle = getIntent().getExtras();
    	final String courseNumber = bundle.getString("courseNumber");
    	final String courseName = bundle.getString("courseName");
		TextView textViewCourseName = (TextView) findViewById(R.id.textViewCourseName);
		textViewCourseName.setText(courseName);
		TextView textViewCourseNumber = (TextView) findViewById(R.id.textViewCourseNumber);
		textViewCourseNumber.setText(courseNumber);
		//We will need studentId passed in - not currently the case.
    	//final Long studentId = savedInstanceState.getLong("studentId");
    	Course c = new Course(null, null, courseNumber, null, null, false);
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
    }
    
    protected void saveRatings(CourseRating cr) {
		if (!alreadySubmitted) {
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
		EditText et = (EditText) findViewById(R.id.comment);
    	String commentText = et.getText().toString();
    	long currTimeMillis = System.currentTimeMillis();
    	Time currentTime = new Time(currTimeMillis);
    	CourseComment cc = new CourseComment(courseId, studentId, commentText, currentTime, 0);
    	CourseCommentClientAsync as2 = new CourseCommentClientAsync();
    	as2.execute(cc);
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
				alreadySubmitted = true;

			}
		}
	}

}
