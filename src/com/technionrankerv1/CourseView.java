package com.technionrankerv1;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
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
 * This class requires two variables to be passed in: "studentId" and "courseNumber"
 * @author raphaelas
 *
 */
public class CourseView extends Activity {
	Long courseId = Long.valueOf(0);
	boolean alreadySubmitted = false;
	TextView textViewCourseRatingSubmitted;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.course_view);
    	textViewCourseRatingSubmitted = (TextView) findViewById(R.id.textViewCourseRatingSubmitted);
    	//final String courseNumber = "236369";
    	final Long studentId = Long.valueOf(0);
    	//Uncomment this eventually:
		Bundle bundle = getIntent().getExtras();
    	final String courseNumber = bundle.getString("courseNumber");
    	final String courseName = bundle.getString("courseName");
		TextView textViewCourseName = (TextView) findViewById(R.id.textViewCourseName);
		textViewCourseName.setText(courseName);
		TextView textViewCourseNumber = (TextView) findViewById(R.id.textViewCourseNumber);
		textViewCourseNumber.setText(courseNumber);
    	//final Long studentId = savedInstanceState.getLong("studentId");
    	Course c = new Course(null, null, courseNumber, null, null, false);
    	courseId = Long.valueOf(0);//theCourse.getId();
		ClientAsync as = new ClientAsync();
		as.execute(c);
		try {
			as.get(); //This will block until as.execute completes
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Bring this code back once we implement comments:
    	Button commentButton = (Button) findViewById(R.id.comment_button);
    	commentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createComment(courseId, studentId);
			}
		});
		*/
    	//Only one of these "given" variables will be used.  Hopefully givenCourse.
    	//String givenCourseName = savedInstanceState.getString("courseName");
    	//Course givenCourse = (Course) savedInstanceState.get("course");
    	
    	RatingBar rOverall = (RatingBar) findViewById(R.id.ratingBarOverall);
    	RatingBar rEnjoyability = (RatingBar) findViewById(R.id.ratingBarEnjoyability);
    	RatingBar rUsefulness = (RatingBar) findViewById(R.id.ratingBarUsefulness);
    	RatingBar rDifficulty = (RatingBar) findViewById(R.id.ratingBarDifficulty);
    	//Long courseId = getCourse(findViewById(R.id.textViewCourseNumber).toString());
    	//Long courseId = Long.valueOf(0);
    	//TODO: Button for saving changed rating stars.
    	final CourseRating cr = new CourseRating(studentId, Long.valueOf(courseNumber),
    			Math.round(rOverall.getRating()), Math.round(rEnjoyability.getRating()),
    			Math.round(rUsefulness.getRating()), Math.round(rDifficulty.getRating()));
    	Button ratingButton = (Button) findViewById(R.id.rating_button);
    	ratingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveRatings(cr);
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

    	CourseRatingClientAsync as3 = new CourseRatingClientAsync();
    	as3.execute(cr);
		}
		else {
			textViewCourseRatingSubmitted.setTextColor(getResources().getColor(R.color.gray));
			textViewCourseRatingSubmitted.setText("Whoops, you already submitted and cannot submit again.");
		}
    }
    
	public void createComment(Long courseId, Long studentId) {
    	/* Bring this code back once we implement comments:
    	String commentText = et.getText().toString();
    	long currTimeMillis = System.currentTimeMillis();
    	Time currentTime = new Time(currTimeMillis);
    	CourseComment cc = new CourseComment(courseId, studentId, commentText, currentTime, 0);
    	CourseCommentClientAsync as2 = new CourseCommentClientAsync();
    	as2.execute(cc);
    	*/
    }
	
	private class ClientAsync extends AsyncTask<Course, Void, Course> {
		public ClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//tvStatus.setText("wait..");
		}

		@Override
		protected Course doInBackground(Course... params) {
			//Course c = new Course(null, "Geology", "01", (long) 5,
			//		"Spring 2011", true);
			//return new TechnionRankerAPI().insertCourse(c).toString();
			Log.d(getLocalClassName(), String.valueOf(params.length));
			//TODO: check that 50 - 100 succeeded.
	    	Course courseToLookUp = params[0];
	    	Course theCourse = new TechnionRankerAPI().getCourse(courseToLookUp);
			return theCourse;
		}

		@Override
		protected void onPostExecute(Course res) {
			if (res == null)
				Log.d(getLocalClassName(), "Course clientAsync unsuccessful");
			else {
			    //delegate.processFinish(res);
				Log.d(getLocalClassName(), res.getName());
		    	courseId = res.getId();
			}
		}
	}
	
	/*
	private class CourseCommentClientAsync extends AsyncTask<CourseComment, Void, String> {
		public CourseCommentClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//tvStatus.setText("wait..");
		}

		@Override
		protected String doInBackground(CourseComment... params) {
			//Course c = new Course(null, "Geology", "01", (long) 5,
			//		"Spring 2011", true);
			//return new TechnionRankerAPI().insertCourse(c).toString();
			//TODO: check that 50 - 100 succeeded.
	    	CourseComment cc = params[0];
	    	String result = new TechnionRankerAPI().insertCourseComment(cc).toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null)
				Log.d(getLocalClassName(), "CourseComment clientAsync unsuccessful");
			else {
			    //delegate.processFinish(res);
				Log.d(getLocalClassName(), res);
			}
		}
	}
	*/
	
	private class CourseRatingClientAsync extends AsyncTask<CourseRating, Void, String> {
		public CourseRatingClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//tvStatus.setText("wait..");
		}

		@Override
		protected String doInBackground(CourseRating... params) {
			//Course c = new Course(null, "Geology", "01", (long) 5,
			//		"Spring 2011", true);
			//return new TechnionRankerAPI().insertCourse(c).toString();
			Log.d(getLocalClassName(), String.valueOf(params.length));
			//TODO: check that 50 - 100 succeeded.
	    	CourseRating cr = params[0];
	    	String result = new TechnionRankerAPI().insertCourseRating(cr).toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			textViewCourseRatingSubmitted.setMaxLines(1);
			if (res == null) {
				Log.d(getLocalClassName(), "CourseRating ClientAsync unsuccessful");
				
				textViewCourseRatingSubmitted.setTextColor(getResources().getColor(R.color.red));
				textViewCourseRatingSubmitted.setText("Sorry, please try submitting your rating again.");
			}
			
			else {
			    //delegate.processFinish(res);
				Log.d(getLocalClassName(), res);
				textViewCourseRatingSubmitted.setTextColor(getResources().getColor(R.color.white));
				textViewCourseRatingSubmitted.setText("Thank you.  Your rating was received.");
				alreadySubmitted = true;

			}
		}
	}

}
