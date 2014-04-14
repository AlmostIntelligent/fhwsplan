package de.almostintelligent.fhwsplan.adapters;

import java.util.Collections;
import java.util.List;

import de.almostintelligent.fhwsplan.data.Employee;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SettingsFilterEmployeeArrayAdapter extends ArrayAdapter<Employee>
{
	int	iResource;

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Employee e = getItem(position);

		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(iResource,
					null);
		}

		TextView text = (TextView) convertView.findViewById(android.R.id.text1);
		if (text != null)
		{
			text.setText(e.getNameFormated());

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
		Employee e = getItem(position);

		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(iResource,
					null);
		}

		TextView text = (TextView) convertView.findViewById(android.R.id.text1);
		if (text != null)
		{
			text.setText(e.getNameFormated());
		}

		return convertView;
	}

	public SettingsFilterEmployeeArrayAdapter(Context context, int resource,
			List<Employee> objects)
	{
		super(context, resource);
		Collections.sort(objects);

		Employee e = new Employee();
		e.setID(-1);
		e.setPrename("Alle");

		objects.add(0, e);

		addAll(objects);
		iResource = resource;
	}

}
