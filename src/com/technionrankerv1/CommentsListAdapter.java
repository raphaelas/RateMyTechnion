package com.technionrankerv1;

import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CommentsListAdapter extends ArrayAdapter<CourseComment> {
	//TODO: ensure that page reload does not allow for re-liking comment.
	private final Context context;
	public CourseComment[] values;
	private boolean[] enabledListeners;

	public CommentsListAdapter(Context context, CourseComment[] values) {
		super(context, R.layout.comments_list_item, values);
		this.context = context;
		this.values = values;
		this.enabledListeners = new boolean[values.length];
		Arrays.fill(enabledListeners, Boolean.TRUE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.comments_list_item, parent, false);
		TextView commentTextView = (TextView) rowView.findViewById(R.id.singleCommentText);
		final TextView likesTextView = (TextView) rowView.findViewById(R.id.likesCountText);
		final ImageButton thumbImage = (ImageButton) rowView.findViewById(R.id.thumbImage);
		thumbImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (enabledListeners[position] == true) {
					enabledListeners[position] = false;
					int oldCount = Integer.parseInt(likesTextView.getText().toString());
					values[position].incrementLikes();
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
				int currI = 0;
				int currJ = 0;
				int toReturn = o2.getLikes() - o1.getLikes();
				if (toReturn < 0) {
					for (int i = 0; i < values.length; i++) {
						if (values[i].equals(o1)) {
							currI = i;
						}
					}
					for (int j = 0; j < values.length; j++) {
						if (values[j].equals(o2)) {
							currJ = j;
						}
					}
					boolean temp = enabledListeners[currI];
					enabledListeners[currI] = enabledListeners[currJ];
					enabledListeners[currJ] = temp;
				}
				return toReturn;
			}
		});
		super.notifyDataSetChanged();
	}
}