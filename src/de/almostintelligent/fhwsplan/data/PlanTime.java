package de.almostintelligent.fhwsplan.data;

import android.sax.StartElementListener;
import android.util.Log;

public class PlanTime// implements Comparable<PlanTime>
{

	private Integer	iID				= new Integer(0);
	private String	strDescription	= new String();

	private String	strStartTime	= new String();
	private String	strEndTime		= new String();

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
	public String getTimeString()
	{
		return strDescription;
	}

	public void setTimeString(String desc)
	{
		strDescription = desc;
		splitTimeString();
	}

	public String getStartTime()
	{
		return strStartTime;
	}

	public String getEndTime()
	{
		return strEndTime;
	}

	private void splitTimeString()
	{
		String strDesc = strDescription.replaceAll(" ", "");
		String[] times = strDesc.split("-");

		if (times.length == 2)
		{
			strStartTime = times[0];
			strEndTime = times[1];
		}
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
