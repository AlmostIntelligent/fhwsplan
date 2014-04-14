package de.almostintelligent.fhwsplan.adapters;

import android.content.Context;
import android.util.SparseArray;
import android.widget.ArrayAdapter;

public class FormatedArrayAdapter<T> extends ArrayAdapter<T>
{
	public SparseArray<T>	items;

	public FormatedArrayAdapter(Context context, int resource)
	{
		super(context, resource);
	}

	String getString()
	{
		return "";
	}

	@Override
	public int getCount()
	{
		if (items == null)
			return 0;
		return items.size();
	}

}
