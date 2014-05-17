package com.technionrankerv1;

import java.sql.Time;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.serverapi.TechnionRankerAPI;

public class CourseView extends Activity {
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.course_view);
    	final String courseNumber = savedInstanceState.getString("courseNumber");
    	final Long studentId = savedInstanceState.getLong("studentId");
    	Course courseToLookUp = new Course(null, null, courseNumber, null, null, false);
    	Course theCourse = new TechnionRankerAPI().getCourseByCourseNumber(courseToLookUp);
    	final Long courseId = theCourse.getId();
    	Button commentButton = (Button) findViewById(R.id.comment_button);
    	commentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createComment(courseId, studentId);
			}
		});
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
    	new TechnionRankerAPI().insertCourseRating(cr);
	}
    
	public void createComment(Long courseId, Long studentId) {
    	EditText et = (EditText) findViewById(R.id.comment);
    	String commentText = et.getText().toString();
    	long currTimeMillis = System.currentTimeMillis();
    	Time currentTime = new Time(currTimeMillis);
    	CourseComment cc = new CourseComment(courseId, studentId, commentText, currentTime, 0);
    	new TechnionRankerAPI().insertCourseComment(cc);

    }
}
