package com.ratemytechnion;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ratemytechnion.R;

public class UserProfessorsAdapter extends ArrayAdapter<String> {
	private final Context context;
	public String[] values;

	public UserProfessorsAdapter(Context context, String[] values) {
		super(context, R.layout.comments_list_item, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.user_professor_list_item, parent, false);
		TextView englishTextView = (TextView) rowView.findViewById(R.id.lblListItemUser);
		TextView hebrewTextView = (TextView) rowView.findViewById(R.id.hebrewListItemUser);
		String thisHebrewName = values[position];
		String thisEnglishName = ((FragmentMainActivity) getContext()).englishNameMap.get(values[position]);
		englishTextView.setText(thisEnglishName);
		hebrewTextView.setText(thisHebrewName);
		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	            String value = values[position];
				Intent i = new Intent(getContext(), ProfessorView.class);
				i.putExtra("professorName", value);
				i.putExtra("faculty", ((FragmentMainActivity) getContext()).facultyMap.get(value));
				i.putExtra("courseValues", ((FragmentMainActivity) getContext()).getCourseValues());
				i.putExtra("professorValues", ((FragmentMainActivity) getContext()).getProfessorValues());
				i.putExtra("facultyMap", ((FragmentMainActivity) getContext()).facultyMap);
				i.putExtra("englishNameMap", ((FragmentMainActivity) getContext()).englishNameMap);
				getContext().startActivity(i);
			}
		});
		return rowView;
	}
}