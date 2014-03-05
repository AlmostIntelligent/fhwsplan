package de.almostintelligent.fhwsplan.drawermenu;

import android.content.Context;

public class DrawerMenuItem
{
	private Context	_context;
	private int		iLocalisation;
	private Integer	iID;

	public DrawerMenuItem(Integer id, int localisation, Context context)
	{
		_context = context;
		iID = id;
		iLocalisation = localisation;
	}

	public String getCaption()
	{
		return _context.getResources().getString(iLocalisation);
	}

	public Integer getID()
	{
		return iID;
	}
}
