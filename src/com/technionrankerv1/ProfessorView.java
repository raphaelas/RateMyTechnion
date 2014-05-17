package com.technionrankerv1;

import java.sql.Time;

import com.serverapi.TechnionRankerAPI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

/**
 * This class requires two variables to be passed in: "professorName" and "studentId"
 * @author raphaelas
 *
 */
public class ProfessorView extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.prof_view);
    	String lookupProfessorName = savedInstanceState.getString("professorName");
    	Professor lookupProfessor = new Professor(null, lookupProfessorName, true);
    	final Long professorId = lookupProfessor.getId();
    	final Long studentId = savedInstanceState.getLong("studentId");
    	Button commentButton = (Button) findViewById(R.id.professorCommentButton);
    	commentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createProfessorComment(professorId, studentId);
			}
		});
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

	protected void saveProfessorRating(ProfessorRating pr) {
		new TechnionRankerAPI().insertProfessorRating(pr);
	}

	protected void createProfessorComment(Long professorId, Long studentId) {
    	EditText et = (EditText) findViewById(R.id.professorComment);
    	String commentText = et.getText().toString();
    	long currTimeMillis = System.currentTimeMillis();
    	Time currentTime = new Time(currTimeMillis);
    	ProfessorComment pc = new ProfessorComment(professorId, studentId, commentText, currentTime, 0);
    	new TechnionRankerAPI().insertProfessorComment(pc);
	}
}
