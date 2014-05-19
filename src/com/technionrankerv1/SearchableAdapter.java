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
	String[] list = null;

	// public ImageLoader imageLoader;

	public SearchableAdapter(String constraint, Activity activity,
			String[] professorsAndCourses) {

		// values = new ArrayList<String>();
		// values.add("found1");
		// values.add("found2");
		list = professorsAndCourses;
		/*
		 * AutoCorrect ac = new AutoCorrect(); String[] corArray = null;
		 * String[] proArray = null;
		 * 
		 * try { Log.d("MyApp", "Trying to parse.");
		 * 
		 * corArray = ac.parseCourses(); Log.d("MyApp",
		 * "Did i parse anything?");
		 * 
		 * proArray = ac.parseProfessors(); Log.d("MyApp",
		 * "No way I parsed everything.");
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 * 
		 * this.list = ac.concat(corArray, proArray);
		 */
		ItemFilter fil = new ItemFilter(list);
		values = fil.mich(constraint);
		
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

		if (getCount() != 0) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.list_item, null);

			TextView name = (TextView) vi.findViewById(R.id.lblListItem);
			name.setText(values.get(position));
			// imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL),
			// thumb_image);

			return vi;

		} else {
		}

		return convertView;
	}

	private class ItemFilter extends Filter {
		String[] list1 = null;

		public ItemFilter(String[] list) {
			list1 = list;
		}

		public FilterResults performFiltering(CharSequence constraint) {

			String filterString = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();

			int count = list1.length;

			final ArrayList<String> nlist = new ArrayList<String>(count);
			

			for (int i = 0; i < list1.length; i++) {
				if (list1[i].toLowerCase().contains(filterString)) {
					nlist.add(list1[i]);
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
