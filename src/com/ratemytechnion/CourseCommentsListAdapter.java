package com.ratemytechnion;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import org.apache.commons.lang3.StringEscapeUtils;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.ratemytechnion.R;
import com.serverapi.TechnionRankerAPI;

public class CourseCommentsListAdapter extends ArrayAdapter<CourseComment> {
	private final Context context;
	public CourseComment[] values;

	public CourseCommentsListAdapter(Context context, CourseComment[] values) {
		super(context, R.layout.comments_list_item, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.comments_list_item, parent, false);
		TextView commentTextView = (TextView) rowView.findViewById(R.id.singleCommentText);
		final TextView likesTextView = (TextView) rowView.findViewById(R.id.likesCountText);
		ImageButton thumbImage = (ImageButton) rowView.findViewById(R.id.thumbImage);
		ApplicationWithGlobalVariables a2 = ((ApplicationWithGlobalVariables) context.getApplicationContext());
		CourseComment thisCourseComment2 = values[position];
		String commentorName = StringEscapeUtils.unescapeJava(thisCourseComment2.getComment().split("\n")[0] + "\n"); 
		if (thisCourseComment2.getStudentsWhoLikedThisComment().contains(StringEscapeUtils.escapeJava(a2.getStudentName()))
				|| !a2.isLoggedIn() || commentorName.equals(a2.getStudentName())) {
			thumbImage.setEnabled(false);
		}
		else {
			Log.d("CourseCommentsListAdapter", a2.getCourseCommentsLiked().toString());
		}
		thumbImage.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				CourseComment thisCourseComment = values[position];
				ApplicationWithGlobalVariables a = ((ApplicationWithGlobalVariables) context.getApplicationContext());
				String commentorName = StringEscapeUtils.unescapeJava(thisCourseComment.getComment().split("\n")[0] + "\n"); 
				//If the comment is not liked according to the global variables:
				if (!thisCourseComment.getStudentsWhoLikedThisComment().contains(StringEscapeUtils.escapeJava(a.getStudentName()))
						&& a.isLoggedIn() && !commentorName.equals(a.getStudentName())) {
					Log.d(StringEscapeUtils.escapeJava(a.getStudentName()), thisCourseComment.getStudentsWhoLikedThisComment().toString());
					int oldCount = Integer.parseInt(likesTextView.getText().toString());
					thisCourseComment.incrementLikes();
					thisCourseComment.addStudentsWhoLikedThisComment(StringEscapeUtils.escapeJava(a.getStudentName()));
					CourseCommentClientAsync as = new CourseCommentClientAsync();
					ProfessorComment professorCopy = new ProfessorComment(
							thisCourseComment.getCourseID(), 
							thisCourseComment.getStudentID(),
							thisCourseComment.getComment(),
							null, thisCourseComment.getLikes());
					professorCopy.setStudentsWhoLikedThisComment((HashSet<String>) thisCourseComment.getStudentsWhoLikedThisComment().clone());
					Log.d("ToInsert HS:", professorCopy.getStudentsWhoLikedThisComment().toString());
					as.execute(professorCopy);
					likesTextView.setText("" + (oldCount + 1));
					notifyDataSetChanged(); //This line is necessary for sorting.
				}
				else {
					Log.d("CourseCommentsListAdapter", thisCourseComment.getStudentsWhoLikedThisComment().toString());
					Log.d("CourseCommentsListAdapter text:", thisCourseComment.getComment());
					Log.d("CourseCommentsListAdapter + isPreviouslyLiked:", thisCourseComment.getStudentsWhoLikedThisComment().contains(StringEscapeUtils.escapeJava(a.getStudentName()))+"");
					Log.d("CourseCommentsListAdapter loggedIn:", a.isLoggedIn() + "");
					Log.d("CourseCommentsListAdapter isOwnComment:", thisCourseComment.
							getStudentID().equals(a.getStudentID()) + "");

					
				}
			}
		});
		commentTextView.setText(values[position].getComment());
		likesTextView.setText(values[position].getLikes() + "");

		return rowView;
	}

	@Override
	public void notifyDataSetChanged() {
		Arrays.sort(values, new Comparator<CourseComment>() {
			@Override
			public int compare(CourseComment o1, CourseComment o2) {
				int toReturn = o2.getLikes() - o1.getLikes();
				return toReturn;
			}
		});
		super.notifyDataSetChanged();
	}
	
	private class CourseCommentClientAsync extends AsyncTask<ProfessorComment, Void, String> {
		public CourseCommentClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(ProfessorComment... params) {
	    	ProfessorComment cc = params[0];
			String[] beenSplit = cc.getComment().split("\n"); 
			cc.setComment(StringEscapeUtils.escapeJava(beenSplit[0] + "\n" + beenSplit[1]));
	    	String result = new TechnionRankerAPI().insertProfessorComment(cc).toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null)
				Log.d("CommentsListAdapter", "CourseComment clientAsync unsuccessful");
			else {
				Log.d("CommentsListAdapter", "CourseComment updating: " + res);
			}
		}
	}
}