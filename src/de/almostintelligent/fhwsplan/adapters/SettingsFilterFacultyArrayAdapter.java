package de.almostintelligent.fhwsplan.adapters;

import java.util.Collections;
import java.util.List;

import de.almostintelligent.fhwsplan.R;
import de.almostintelligent.fhwsplan.data.Faculty;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SettingsFilterFacultyArrayAdapter extends ArrayAdapter<Faculty>
{
	int	iResource;

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Faculty f = getItem(position);

		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(iResource,
					null);
		}

		TextView text = (TextView) convertView.findViewById(android.R.id.text1);
		if (text != null)
		{
			text.setText(f.getLongName());

		}

		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getDropDownView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		Faculty f = getItem(position);

		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(iResource,
					null);
		}

		TextView text = (TextView) convertView.findViewById(android.R.id.text1);
		if (text != null)
		{
			text.setText(f.getLongName());
		}

		return convertView;
	}

	public SettingsFilterFacultyArrayAdapter(Context context, int resource,
			List<Faculty> objects)
	{
		super(context, resource);
		Collections.sort(objects);

		Faculty e = new Faculty();
		e.setID(-1);
		e.setLongName(context.getResources().getString(R.string.all_value));

		objects.add(0, e);

		addAll(objects);
		iResource = resource;
	}

}
