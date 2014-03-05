package de.almostintelligent.fhwsplan.drawermenu;

import de.almostintelligent.fhwsplan.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DrawerMenuAdapter extends ArrayAdapter<DrawerMenuItem>
{
	private Context	_context;

	public DrawerMenuAdapter(Context context, int resource,
			DrawerMenuItem[] objects)
	{
		super(context, resource, objects);

		_context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View result = inflater
				.inflate(R.layout.drawer_list_item, parent, false);
		if (result != null)
		{
			if (result instanceof TextView)
			{
				TextView text = (TextView) result;
				text.setText(getItem(position).getCaption());
			}
		}

		return result;
	}

}
