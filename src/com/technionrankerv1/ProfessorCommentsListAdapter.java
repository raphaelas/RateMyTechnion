package com.technionrankerv1;

import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.serverapi.TechnionRankerAPI;

public class ProfessorCommentsListAdapter extends ArrayAdapter<ProfessorComment> {
	private final Context context;
	public ProfessorComment[] values;

	public ProfessorCommentsListAdapter(Context context, ProfessorComment[] values) {
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
		ProfessorComment thisProfessorComment2 = values[position];
		if (a2.isProfessorCommentLiked(a2.getStudentID())
				|| !a2.isLoggedIn() || thisProfessorComment2.
				getStudentID().equals(a2.getStudentID())) {
			thumbImage.setEnabled(false);
		}
		thumbImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ApplicationWithGlobalVariables a = ((ApplicationWithGlobalVariables) context.getApplicationContext());
				ProfessorComment thisProfessorComment = values[position];
				//If the comment is not liked according to the global variables:
				if (!a.isProfessorCommentLiked(a.getStudentID())
						&& a.isLoggedIn() && !thisProfessorComment.
						getStudentID().equals(a.getStudentID())) {
					a.likeProfessorComment(a.getStudentID());
					int oldCount = Integer.parseInt(likesTextView.getText().toString());
					thisProfessorComment.incrementLikes();
					ProfessorCommentClientAsync as = new ProfessorCommentClientAsync();
					as.execute(thisProfessorComment);
					likesTextView.setText("" + (oldCount + 1));
					notifyDataSetChanged(); //This line is necessary for sorting.
				}
			}
		});
		String commentBeingDisplayed = values[position].getComment();
		commentTextView.setText(commentBeingDisplayed);
		likesTextView.setText(values[position].getLikes() + "");

		return rowView;
	}

	@Override
	public void notifyDataSetChanged() {
		Arrays.sort(values, new Comparator<ProfessorComment>() {
			@Override
			public int compare(ProfessorComment o1, ProfessorComment o2) {
				int toReturn = o2.getLikes() - o1.getLikes();
				return toReturn;
			}
		});
		super.notifyDataSetChanged();
	}
	
	private class ProfessorCommentClientAsync extends AsyncTask<ProfessorComment, Void, String> {
		public ProfessorCommentClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(ProfessorComment... params) {
	    	ProfessorComment cc = params[0];
	    	String result = new TechnionRankerAPI().insertProfessorComment(cc).toString();
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null)
				Log.d("CommentsListAdapter", "ProfessorComment clientAsync unsuccessful");
			else {
				Log.d("CommentsListAdapter", "ProfessorComment updating: " + res);
			}
		}
	}
}
