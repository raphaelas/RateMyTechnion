package com.technionrankerv1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

class SearchableAdapter extends BaseAdapter {
	private static LayoutInflater inflater = null;
	private int count;
	
	private ArrayList<String> filteredData = null;
	// private ItemFilter mFilter = new ItemFilter();
	String contstaint;
	ArrayList<String> values;

	// public ImageLoader imageLoader;

	public SearchableAdapter(String constraint, Activity activity) {
		ItemFilter fil = new ItemFilter();
		values = fil.mich(constraint);
		// values = new ArrayList<String>();
		// values.add("found1");
		// values.add("found2");
		count = values.size();
		this.contstaint = constraint;
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_item, null);

		TextView name = (TextView) vi.findViewById(R.id.lblListItem);
		name.setText(values.get(position));
		// imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL),
		// thumb_image);
		return vi;
	}

	private class ItemFilter extends Filter {
		public ItemFilter() {

		}

		public FilterResults performFiltering(CharSequence constraint) {
			Log.d("MyApp", "Im fucking Filtering");

			String filterString = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();

			AutoCorrect ac = new AutoCorrect();
			String[] corArray = null;
			String[] proArray = null;
			/*
			try {
				Log.d("MyApp", "die bitch die");

				corArray = ac.parseCourses();
				Log.d("MyApp", "aieeeeee");

				proArray = ac.parseProfessors();
				Log.d("MyApp", "Im fucking NOT bla blu Filtering");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/
		//	final String[] list = ac.concat(corArray, proArray);
			String[] list= new String[3];
			list[0]="adam hi";
			list[1]="adam bye";
			list[2]="blablieblue";
			int count = list.length;
			final ArrayList<String> nlist = new ArrayList<String>(count);

			for (int i = 0; i < list.length; i++) {
				if (list[i].toLowerCase().contains(constraint)) {
					nlist.add(list[i]);
				}
			}
			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		public ArrayList<String> mich(String constraint) {
			return (ArrayList<String>) performFiltering(constraint).values;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredData = (ArrayList<String>) results.values;
			notifyDataSetChanged();
		}

	}
}