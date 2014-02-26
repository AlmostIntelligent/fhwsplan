package de.almostintelligent.fhwsplan.data;

import java.util.Vector;

public class Faculty
{
	private Integer			iID;
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

	/**
	 * @return the strLongName
	 */
	public String getLongName()
	{
		return strLongName;
	}

	/**
	 * @return the semester
	 */
	public Vector<Integer> getSemester()
	{
		return semester;
	}

	public Integer getID()
	{
		return iID;
	}

	public Faculty(Integer id, String shortName, String longName)
	{
		iID = id;
		strShortName = shortName;
		strLongName = longName;
	}
}
