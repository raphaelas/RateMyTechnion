package com.technionrankerv1;

import java.util.Arrays;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
	    TextView likesTextView = (TextView) rowView.findViewById(R.id.likesCountText);
	    Log.d(Arrays.toString(values), position + "");
	    commentTextView.setText(values[position]);
	    likesTextView.setText("4");

	    return rowView;
	  }
	  
	  /*protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);
	    Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	  }*/
}
