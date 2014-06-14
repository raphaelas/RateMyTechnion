package com.technionrankerv1;

import java.util.Arrays;
import java.util.Comparator;

import com.serverapi.TechnionRankerAPI;

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
		thumbImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CourseComment thisCourseComment = values[position];
				ApplicationWithGlobalVariables a = ((ApplicationWithGlobalVariables) context.getApplicationContext());
				//If the comment is not liked according to the global variables:
				if (!a.isCourseCommentLiked(thisCourseComment.getId())
						&& a.isLoggedIn() && !thisCourseComment.
						getStudentID().equals(a.getStudentID())) {
					((ApplicationWithGlobalVariables) context.getApplicationContext()).likeCourseComment(thisCourseComment.getId());
					int oldCount = Integer.parseInt(likesTextView.getText().toString());
					thisCourseComment.incrementLikes();
					CourseCommentClientAsync as = new CourseCommentClientAsync();
					as.execute(thisCourseComment);
					likesTextView.setText("" + (oldCount + 1));
					notifyDataSetChanged(); //This line is necessary for sorting.
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
				Log.d("CommentsListAdapter", "CourseComment clientAsync unsuccessful");
			else {
				Log.d("CommentsListAdapter", "CourseComment updating: " + res);
			}
		}
	}
}