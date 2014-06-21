package com.technionrankerv1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringEscapeUtils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.serverapi.TechnionRankerAPI;

/**
 * This class requires two variables to be passed in: "courseName" and
 * "studentId"
 * 
 * @author raphaelas
 * 
 */
public class CourseView extends SearchResults {
	private Long courseId = Long.valueOf(0);
	private Long studentId = Long.valueOf(0);
	private boolean alreadySubmitted = false;
	private TextView textViewCourseRatingSubmitted;
	private List<CourseComment> comments = new ArrayList<CourseComment>();
	private boolean canSubmit;
	private boolean loggedIn;
	private boolean shouldPreventSubmit = false;
	private float averageOverall = 0;
	private float averageEnjoyability = 0;
	private float averageUsefulness = 0;
	private float averageDifficulty = 0;
	private int totalRatings = 0;
	private RatingBar rOverall;
	private RatingBar rUsefulness;
	private RatingBar rEnjoyability;
	private RatingBar rDifficulty;
	private String emptyCommentsString;
	private ApplicationWithGlobalVariables a;
	private CourseComment courseCommentToUpdate;

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_view);
		a = ((ApplicationWithGlobalVariables) this.getApplication());
		studentId = a.getStudentID();
		canSubmit = a.canSubmitRatings();
		loggedIn = a.isLoggedIn();
		textViewCourseRatingSubmitted = (TextView) findViewById(R.id.textViewCourseRatingSubmitted);
		Bundle bundle = getIntent().getExtras();
		String lookupCourseNumber = bundle.getString("courseNumber");
		String lookupCourseName = bundle.getString("courseName");
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
				&& (!isTablet(getApplicationContext()))) {
			TextView t = (TextView) findViewById(R.id.course_overall);
			t.setTextSize(18);
			TextView t1 = (TextView) findViewById(R.id.course_difficulty);
			t1.setTextSize(18);
			TextView t2 = (TextView) findViewById(R.id.course_enjoyability);
			t2.setTextSize(18);
			TextView t3 = (TextView) findViewById(R.id.course_usefulness);
			t3.setTextSize(18);
			TextView t4 = (TextView) findViewById(R.id.totalRatingsCaption);
			t4.setTextSize(18);
		}
		GetProfessorClientAsync gpca = new GetProfessorClientAsync();
		Course cLookup = new Course(null, null, lookupCourseNumber, null, null,
				null, true);
		ClientAsyncGetCourseByCourseNumber cagpbpn = new ClientAsyncGetCourseByCourseNumber();
		try {
			Professor p = gpca.execute(lookupCourseNumber).get(10, TimeUnit.SECONDS);
			final String tempPHebrewName = StringEscapeUtils.unescapeJava(p.getHebrewName());
			TextView headProfessor = (TextView) findViewById(R.id.headProfessorText);
			headProfessor.setPaintFlags(headProfessor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			headProfessor.setText(tempPHebrewName);
			headProfessor.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(CourseView.this, ProfessorView.class);
					String thisFaculty = a.facultyMap.get(tempPHebrewName);
					i.putExtra("professorName", tempPHebrewName);
					if (!thisFaculty.equals("<null>")) {
						Log.d(getLocalClassName(), thisFaculty);
						i.putExtra("faculty", a.facultyMap.get(tempPHebrewName));
					}
					i.putExtra("courseValues", actionBarCourseValues);
					i.putExtra("professorValues", actionBarProfessorValues);
					i.putExtra("facultyMap", scheduleFacultyMap);
					i.putExtra("englishNameMap", scheduleEnglishNameMap);
					startActivity(i);
				}
				
			});
			courseId = cagpbpn.execute(cLookup).get(10, TimeUnit.SECONDS).get(0).getId();
			getAllCourseRatingsDatabase();
			getAllCourseCommentsDatabase();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		TextView courseNameText = (TextView) findViewById(R.id.textViewCourseName);
		courseNameText.setText(lookupCourseNumber + " - " + lookupCourseName);
		String faculty = bundle.getString("faculty");
		TextView facultyText = (TextView) findViewById(R.id.courseFacultyText);
		facultyText.setText(faculty);
		rOverall = (RatingBar) findViewById(R.id.ratingBarOverall);
		rEnjoyability = (RatingBar) findViewById(R.id.ratingBarEnjoyability);
		rUsefulness = (RatingBar) findViewById(R.id.ratingBarUsefulness);
		rDifficulty = (RatingBar) findViewById(R.id.ratingBarDifficulty);

		final CourseRating cr = new CourseRating(studentId, courseId,
				rOverall.getRating(), rEnjoyability.getRating(),
				rUsefulness.getRating(), rDifficulty.getRating());
		Button ratingButton = (Button) findViewById(R.id.rating_button);
		ratingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				saveCourseRating(cr);
				createProfessorComment(cr.getCourseId(), cr.getStudentID());
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
		rOverall.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				cr.setOverallRating(rating);
			}
		});

		rUsefulness
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						cr.setUsefulness(rating);
					}
				});

		rEnjoyability
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						cr.setEnjoyability(rating);
					}
				});

		rDifficulty
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						cr.setDifficulty(Math.round(rating));
					}
				});

		ListView lv = (ListView) findViewById(R.id.courseCommentsList); // your
																		// listview
																		// inside
																		// scrollview
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

	protected void saveCourseRating(CourseRating cr) {
		if (!canSubmit && !alreadySubmitted) {
			textViewCourseRatingSubmitted.setTextColor(getResources().getColor(
					R.color.gray));
			textViewCourseRatingSubmitted
					.setText("Whoops, you've reached the limit for posting ratings this semester.");
		} else if (alreadySubmitted || shouldPreventSubmit) {
			textViewCourseRatingSubmitted.setTextColor(getResources().getColor(
					R.color.gray));
			textViewCourseRatingSubmitted
					.setText("Please wait while we update your response.");
			InsertCourseRatingClientAsync as3 = new InsertCourseRatingClientAsync();
			as3.execute(cr);
		} else {
			textViewCourseRatingSubmitted.setTextColor(getResources().getColor(
					R.color.gray));
			textViewCourseRatingSubmitted
					.setText("Please wait while we record your response.");
			InsertCourseRatingClientAsync as3 = new InsertCourseRatingClientAsync();
			as3.execute(cr);
		}

	}

	public void getAllCourseCommentsDatabase() {
		GetCourseCommentsClientAsync gccca = new GetCourseCommentsClientAsync();
		ProfessorComment ccLookup = new ProfessorComment(courseId, null, null,
				null, 0);
		gccca.execute(ccLookup);
	}

	public void getAllCourseRatingsDatabase() {
		CourseRating crLookup = new CourseRating(studentId, courseId, 0, 0, 0,
				0);
		GetCourseRatingsClientAsync gcrca = new GetCourseRatingsClientAsync();
		gcrca.execute(crLookup);
	}

	private void hideSoftKeyboard() {
		EditText et = (EditText) findViewById(R.id.comment);
		if (getCurrentFocus() != null && getCurrentFocus() instanceof EditText) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
		}
	}

	protected void createProfessorComment(Long courseId, Long studentId) {
		if ((canSubmit || alreadySubmitted)) {
			EditText et = (EditText) findViewById(R.id.comment);
			String studentName = ((ApplicationWithGlobalVariables) this
					.getApplication()).getStudentName();
			String tempCommentText = et.getText().toString();
			if (tempCommentText.length() > 0) {
				String immediateCommentText = "" + studentName + tempCommentText;
				String dbCommentText = StringEscapeUtils
						.escapeJava(immediateCommentText);
				ProfessorComment databasePC;
				if (shouldPreventSubmit) {
					databasePC = new ProfessorComment(
							courseCommentToUpdate.getCourseID(), 
							courseCommentToUpdate.getStudentID(),
							dbCommentText, null,
							courseCommentToUpdate.getLikes());
					databasePC.setStudentsWhoLikedThisComment(
							courseCommentToUpdate.getStudentsWhoLikedThisComment());
				}
				else {
					databasePC = new ProfessorComment(courseId,
							studentId, dbCommentText, null, 0);
				}

				InsertCourseCommentClientAsync as2 = new InsertCourseCommentClientAsync();
				as2.execute(databasePC);
			}
		}
	}

	public void displayAllComments(List<CourseComment> allComments) {
		ListView courseCommentsList = (ListView) findViewById(R.id.courseCommentsList);
		CourseCommentsListAdapter adapter = new CourseCommentsListAdapter(this,
				allComments.toArray(new CourseComment[(allComments.size())]));
		TextView emptyComments = (TextView) findViewById(R.id.emptyCourseComments);
		emptyComments.setText(emptyCommentsString);
		adapter.sort(new Comparator<CourseComment>() {

			@Override
			public int compare(CourseComment o1, CourseComment o2) {
				int toReturn = o2.getLikes() - o1.getLikes();
				return toReturn;
			}

		});
		courseCommentsList.setEmptyView(emptyComments);
		courseCommentsList.setAdapter(adapter);
		LayoutParams l = courseCommentsList.getLayoutParams();
		if (l.height < 450) {
			l.height = l.height + (allComments.size() * 150);
			courseCommentsList.setLayoutParams(l);
		}

	}

	private class ClientAsyncGetCourseByCourseNumber extends
			AsyncTask<Course, Void, List<Course>> {
		public ClientAsyncGetCourseByCourseNumber() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Course> doInBackground(Course... params) {
			Course courseToLookUp = params[0];
			List<Course> theCourses = new TechnionRankerAPI()
					.getCourseByCourseNumber(courseToLookUp);
			return theCourses;
		}

		@Override
		protected void onPostExecute(List<Course> res) {
			if (res == null)
				Log.d(getLocalClassName(), "GetCourse clientAsync unsuccessful");
			else if (res.isEmpty()) {
				Log.d(getLocalClassName(),
						"GetCourse clientAsync returned empty.");
			} else {
				Log.d(getLocalClassName(), "GetCourse clientAsync successful");
				courseId = res.get(0).getId();
			}
		}
	}

	private class InsertCourseRatingClientAsync extends
			AsyncTask<CourseRating, Void, String> {
		public InsertCourseRatingClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			Log.d(getLocalClassName(),
					"Starting InsertCourseRatingClientAsync...");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(CourseRating... params) {
			CourseRating cr = params[0];
			String result = new TechnionRankerAPI().insertCourseRating(cr)
					.toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null || res.equals("FAILED")) {
				// "CourseRating ClientAsync unsuccessful");
				textViewCourseRatingSubmitted.setTextColor(getResources()
						.getColor(R.color.red));
				textViewCourseRatingSubmitted
						.setText("Sorry, please try submitting your rating again.");
			} else if (!alreadySubmitted && !shouldPreventSubmit) {
				textViewCourseRatingSubmitted.setTextColor(getResources()
						.getColor(R.color.white));
				textViewCourseRatingSubmitted
						.setText("Thank you.  Your rating was received.");
				alreadySubmitted = true;
				Toast.makeText(getApplicationContext(), "Refreshing page...", Toast.LENGTH_SHORT).show();
				getAllCourseRatingsDatabase();
				a.incrementRatingsSubmitted();
				Course c = new Course(null, null, StringEscapeUtils.escapeJava(a.getStudentName()), Long.valueOf(a.getRatingsSubmitted()), null, null, true);
				InsertCourseClientAsync icca = new InsertCourseClientAsync();
				icca.execute(c);
				a.studentsToRatingsSubmitted.put(a.getStudentName(), a.getRatingsSubmitted());

			}
			else {
				textViewCourseRatingSubmitted.setTextColor(getResources().getColor(R.color.white));
				textViewCourseRatingSubmitted.setText("Thank you.  Your rating was updated.");
				Toast.makeText(getApplicationContext(), "Refreshing page...", Toast.LENGTH_SHORT).show();
				getAllCourseRatingsDatabase();
			}
		}
	}

	private class InsertCourseCommentClientAsync extends
			AsyncTask<ProfessorComment, Void, String> {
		public InsertCourseCommentClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			Log.d(getLocalClassName(),
					"Starting InsertProfessorCommentClientAsync...");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(ProfessorComment... params) {
			ProfessorComment cc = params[0];
			String result = new TechnionRankerAPI().insertProfessorComment(cc)
					.toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null || res.equals("FAILED")) 
				Log.d(getLocalClassName(),
						"CourseComment clientAsync unsuccessful");
			else {
				Log.d(getLocalClassName(), "Insert CourseComment: " + res);
				getAllCourseCommentsDatabase();
			}
		}
	}

	private class GetCourseRatingsClientAsync extends
			AsyncTask<CourseRating, Void, List<CourseRating>> {
		public GetCourseRatingsClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			Log.d(getLocalClassName(),
					"Starting ProfessorCommentClientAsync...");
			super.onPreExecute();
		}

		@Override
		protected List<CourseRating> doInBackground(CourseRating... params) {
			CourseRating cr = params[0];
			List<CourseRating> result = new TechnionRankerAPI()
					.getCourseRatingByCourse(cr);
			return result;
		}

		@Override
		protected void onPostExecute(List<CourseRating> res) {
			if (res == null) {
				Log.d(getLocalClassName(), "Get CourseRatings failed.");
			} else if (res.isEmpty()) {
				Log.d(getLocalClassName(), "Get CourseRatings returned empty.");
			} else {
				Log.d(getLocalClassName(), "Get CourseRatings succeeded.");
				List<CourseRating> currRatings = res;
				totalRatings = currRatings.size();
				averageOverall = 0;
				averageEnjoyability = 0;
				averageUsefulness = 0;
				averageDifficulty = 0;
				for (CourseRating cRating : currRatings) {
					Long tempStudentId = cRating.getStudentID();
					double tempOverallRating = cRating.getOverallRating();
					double tempEnjoyability = cRating.getEnjoyability();
					double tempUsefulness = cRating.getUsefulness();
					double tempDifficulty = cRating.getDifficulty();
					if (tempStudentId.equals(studentId)) {
						alreadySubmitted = true;
					} else if (tempStudentId < 1000) {
						totalRatings += tempStudentId;
						tempOverallRating *= totalRatings;
						tempEnjoyability *= totalRatings;
						tempUsefulness *= totalRatings;
						tempDifficulty *= totalRatings;
					} else {
						totalRatings++;
					}
					averageOverall += tempOverallRating;
					averageEnjoyability += tempEnjoyability;
					averageUsefulness += tempUsefulness;
					averageDifficulty += tempDifficulty;

				}
				averageOverall /= totalRatings;
				averageEnjoyability /= totalRatings;
				averageUsefulness /= totalRatings;
				averageDifficulty /= totalRatings;
				rOverall.setRating(averageOverall);
				rUsefulness.setRating(averageUsefulness);
				rEnjoyability.setRating(averageEnjoyability);
				rDifficulty.setRating(averageDifficulty);
				TextView totalRatingsTextView = (TextView) findViewById(R.id.totalRatingsCountCourse);
				totalRatingsTextView.setText(totalRatings + "");
			}
		}
	}

	/**
	 * Note: getCourseComments doesn't work - it returns useless
	 * ProfessorComments So we are using ProfessorComment database calls
	 * instead.
	 * 
	 * @author raphaelas
	 * 
	 */
	private class GetCourseCommentsClientAsync extends
			AsyncTask<ProfessorComment, Void, List<ProfessorComment>> {
		public GetCourseCommentsClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			Log.d(getLocalClassName(),
					"Starting GetCourseCommentClientAsync...");
			super.onPreExecute();
		}

		@Override
		protected List<ProfessorComment> doInBackground(
				ProfessorComment... params) {
			ProfessorComment cc = params[0];
			List<ProfessorComment> result = new TechnionRankerAPI()
					.getProfessorCommentByProfessor(cc);
			return result;
		}

		@Override
		protected void onPostExecute(List<ProfessorComment> res) {
			if (res == null) {
				Log.d(getLocalClassName(), "Get CourseComments failed.");
				emptyCommentsString = "Whoops, there was an error retrieving the comments.";
			} else if (res.isEmpty()) {
				Log.d(getLocalClassName(), "Get CourseComments returned empty.");
				emptyCommentsString = "There are no comments to show yet.";
				displayAllComments(comments);
			} else {
				ArrayList<CourseComment> resToUse = new ArrayList<CourseComment>();
				Log.d(getLocalClassName(), "Get CourseComments succeeded.");
				for (int i = 0; i < res.size(); i++) {
					ProfessorComment tempPC = res.get(i);
					CourseComment tempCC = new CourseComment(
							tempPC.getProfessorID(), tempPC.getStudentID(),
							tempPC.getComment(), null, tempPC.getLikes());
					tempCC.setStudentsWhoLikedThisComment(tempPC.getStudentsWhoLikedThisComment());
					String nameToSet = StringEscapeUtils.unescapeJava(tempCC
							.getComment());
					String realName = StringEscapeUtils.escapeJava(nameToSet
							.split("\n")[0]);
					if ((StringEscapeUtils
							.escapeJava(a.getStudentName().trim()))
							.equals(realName)) {
						shouldPreventSubmit = true;
						courseCommentToUpdate = tempCC;
					}
					tempCC.setComment(nameToSet);
					resToUse.add(tempCC);
				}
				comments = resToUse;
				displayAllComments(comments);
			}
		}
	}
	
	private class InsertCourseClientAsync extends AsyncTask<Course, Void, String> { 
		public InsertCourseClientAsync() {
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute(); 
		}

		@Override 
		protected String doInBackground(Course... params) {
			String result = null; Course courseToAdd = params[0];
			Log.d(getLocalClassName(), courseToAdd.toString());
			List<Course> listToAdd = new ArrayList<Course>();
			listToAdd.add(courseToAdd); result = new TechnionRankerAPI().insertCourse(listToAdd).toString();
			return result;
		}

		@Override 
		protected void onPostExecute(String res) { 
			if (res == null || res.equals("FAILED")) {
				Log.d(getLocalClassName(), "Insert course failed.");
			} 
			else {
				Log.d(getLocalClassName(), "Insert course: " + res);
			}
		}
	}
	
	private class GetProfessorClientAsync extends
	AsyncTask<String, Void, Professor> {
		public GetProfessorClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Professor doInBackground(String... params) {
			String courseNumber = params[0];
			Course lookup = new Course(null, null, courseNumber, null, null,
					null, true);
			Professor result = new TechnionRankerAPI()
			.getProfessorForCourse(lookup);
			return result;
		}

		@Override
		protected void onPostExecute(Professor res) {
			if (res == null) {
				Log.d(getLocalClassName(),
						"Get of professor for course failed.");
//				a.decrementRatingsThreshold();
			} else {
			}
		}
	}
}
