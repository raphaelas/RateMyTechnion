package com.technionrankerv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentsListAdapter extends ArrayAdapter<String> {
	  private final Context context;
	  private final String[] values;

	  public CommentsListAdapter(Context context, String[] values) {
	    super(context, R.layout.comments_list_item, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.comments_list_item, parent, false);
	    TextView commentTextView = (TextView) rowView.findViewById(R.id.singleCommentText);
	    final TextView likesTextView = (TextView) rowView.findViewById(R.id.likesCountText);
	    ImageView thumbImage = (ImageView) rowView.findViewById(R.id.thumbImage);
	    thumbImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int oldCount = Integer.parseInt(likesTextView.getText().toString());
				likesTextView.setText("" + (oldCount + 1));
			}
	    	
	    });
	    commentTextView.setText(values[position]);
	    likesTextView.setText("4");

	    return rowView;
	  }
}
