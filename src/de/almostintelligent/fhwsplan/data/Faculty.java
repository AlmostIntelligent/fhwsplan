package de.almostintelligent.fhwsplan.data;

import java.util.Vector;

import android.util.Log;

public class Faculty extends DataWithID
{
	private String			strShortName;
	private String			strLongName;
	private Vector<Integer>	semester	= new Vector<Integer>();

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

	/**
	 * @return the semester
	 */
	public Vector<Integer> getSemester()
	{
		return semester;
	}

	public void addSemester(Integer i)
	{
		semester.add(i);
	}

	public void print()
	{
		Log.e("faculty.id", getID().toString());
		Log.e("faculty.short", strShortName);
		Log.e("faculty.long", strLongName);
		for (Integer i : semester)
		{
			Log.e("faculty.sem", i.toString());
		}

	}

}
