package com.technionrankerv1;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class SearchableAdapter extends BaseAdapter implements Filterable {
	
	private ArrayList<String> filteredData = null;
	private LayoutInflater mInflater;
	private ItemFilter mFilter = new ItemFilter();
	
	public SearchableAdapter(Context context) {
    	mInflater = LayoutInflater.from(context);
    }

	public int getCount() {
		return filteredData.size();
	}

	public Object getItem(int position) {
		return filteredData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

   public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
    /*    ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.list_view);

            // Bind the data efficiently with the holder.

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // If weren't re-ordering this you could rely on what you set last time
        holder.text.setText(filteredData.get(position));

        return convertView;*/
	   return null;
    }
	
    static class ViewHolder {
        TextView text;
    }

	public Filter getFilter() {
		return mFilter;
	}

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			String filterString = constraint.toString().toLowerCase();
			
			FilterResults results = new FilterResults();
			
			AutoCorrect ac=new AutoCorrect();
			String[] corArray = null;
			String[] proArray = null;
			try {
				corArray = ac.parseCourses();
				proArray = ac.parseProfessors();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			final String[] list = ac.concat(corArray, proArray);
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

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			filteredData = (ArrayList<String>) results.values;
			notifyDataSetChanged();
		}

	}
}
