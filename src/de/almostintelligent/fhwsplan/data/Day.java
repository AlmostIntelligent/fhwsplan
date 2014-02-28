package de.almostintelligent.fhwsplan.data;

import android.util.Log;

public class Day extends DataWithID
{

	// Members
	private String	strShortName;
	private String	strLongName;

	public String getShortName()
	{
		return strShortName;
	}

	public void setShortName(String name)
	{
		strShortName = name;
	}

	public String getLongName()
	{
		return strLongName;
	}

	public void setLongName(String name)
	{
		strLongName = name;
	}

	public void print()
	{
		Log.e("day.id", getID().toString());
		Log.e("day.short", strShortName);
		Log.e("day.long", strLongName);
	}

}
