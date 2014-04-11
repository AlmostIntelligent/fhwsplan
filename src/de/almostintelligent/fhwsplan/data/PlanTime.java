package de.almostintelligent.fhwsplan.data;

import android.util.Log;

public class PlanTime// implements Comparable<PlanTime>
{

	private Integer	iID;
	private String	strDescription;

	// private Room room;

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

	// @Override
	// public int compareTo(PlanTime another)
	// {
	// if (getID() < another.getID())
	// return -1;
	// if (getID() == another.getID())
	// return 0;
	// else
	// return 1;
	// }

}
