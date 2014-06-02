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
	  private final Context context;
	  private final CourseComment[] values;

	  public CommentsListAdapter(Context context, CourseComment[] values) {
	    super(context, R.layout.comments_list_item, values);
	    this.context = context;
		//Arrays.sort(values, new CommentsComparator());
	    this.values = values;
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
				if (thumbImage.isEnabled()) {
					thumbImage.setEnabled(false);
					int oldCount = Integer.parseInt(likesTextView.getText().toString());
					values[position].incrementLikes();
					likesTextView.setText("" + (oldCount + 1));
					notifyDataSetChanged();
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
	  
}
