package de.almostintelligent.fhwsplan.data;

import android.util.Log;

public class Room
{

	private Integer	iID;
	private String	strShortName;
	private String	strLongName;

	/**
	 * @return the iID
	 */
	public Integer getID()
	{
		return iID;
	}

	public void setID(Integer id)
	{
		iID = id;
	}

	/**
	 * @return the strShortName
	 */
	public String getShortName()
	{
		return strShortName;
	}

	public void setShortName(String name)
	{
		strShortName = name;
	}

	/**
	 * @return the strLongName
	 */
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
		Log.e("room.id", iID.toString());
		Log.e("room.short", strShortName);
		Log.e("room.long", strLongName);
	}
}
