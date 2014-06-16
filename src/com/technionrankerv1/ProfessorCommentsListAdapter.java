package com.technionrankerv1;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.lang3.StringEscapeUtils;

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
		String commentorName = thisProfessorComment2.getComment().split("\n")[0] + "\n"; 
		if (a2.isProfessorCommentLiked("" + thisProfessorComment2.getProfessorID()+
				thisProfessorComment2.getStudentID() + thisProfessorComment2.getComment())
				|| !a2.isLoggedIn() || commentorName.equals(a2.getStudentName())) {
			thumbImage.setEnabled(false);
		}
		thumbImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ApplicationWithGlobalVariables a = ((ApplicationWithGlobalVariables) context.getApplicationContext());
				ProfessorComment thisProfessorComment = values[position];
				String commentorName = thisProfessorComment.getComment().split("\n")[0] + "\n"; 
				//If the comment is not liked according to the global variables:
				if (!a.isProfessorCommentLiked("" + thisProfessorComment.getProfessorID() +
						thisProfessorComment.getStudentID() + thisProfessorComment.getComment())
						&& a.isLoggedIn() && !commentorName.equals(a.getStudentName())) {
					a.likeProfessorComment("" + thisProfessorComment.getProfessorID() +
							thisProfessorComment.getStudentID() + thisProfessorComment.getComment());
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
			String[] beenSplit = cc.getComment().split("\n"); 
			cc.setComment(StringEscapeUtils.escapeJava(beenSplit[0]) + "\n" + beenSplit[1]);
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
