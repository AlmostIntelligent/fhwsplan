package de.almostintelligent.fhwsplan.data;

import android.util.Log;

public class Day
{

	// Members
	private Integer	iID;
	private String	strShortName;
	private String	strLongName;

	// Getter
	public Integer getID()
	{
		return iID;
	}

	public void setID(Integer id)
	{
		iID = id;
	}

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
		Log.e("day.id", iID.toString());
		Log.e("day.short", strShortName);
		Log.e("day.long", strLongName);
	}

}
