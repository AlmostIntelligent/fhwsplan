package de.almostintelligent.fhwsplan.data;

import android.util.Log;

public class PlanTime
{

	private Integer	iID;
	private String	strDescription;

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
	 * @return the strDescription
	 */
	public String getDescription()
	{
		return strDescription;
	}

	public void setDescription(String desc)
	{
		strDescription = desc;
	}

	public void print()
	{
		Log.e("plantime.id", iID.toString());
		Log.e("plantime.desc", strDescription);

	}

}
