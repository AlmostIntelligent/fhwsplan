package de.almostintelligent.fhwsplan.data;

import java.util.Vector;

public class Lecture
{

	private Integer				iID;
	private Integer				iParent		= 0;
	private String				strDescription;
	private String				strDescAppendix;
	private Integer				iDuration;
	private Boolean				bOptional	= false;
	private Boolean				bArranged	= true;

	private Vector<LectureDate>	dates		= new Vector<LectureDate>();
	private Vector<Employee>	lecturers	= new Vector<Employee>();

	/**
	 * @return the iID
	 */
	public Integer getID()
	{
		return iID;
	}

	/**
	 * @return the iParent
	 */
	public Integer getParent()
	{
		return iParent;
	}

	/**
	 * @return the strDescription
	 */
	public String getDescription()
	{
		return strDescription;
	}

	/**
	 * @return the strDescAppendix
	 */
	public String getDescAppendix()
	{
		return strDescAppendix;
	}

	/**
	 * @return the iDuration
	 */
	public Integer getDuration()
	{
		return iDuration;
	}

	/**
	 * @return the bOptional
	 */
	public Boolean getOptional()
	{
		return bOptional;
	}

	/**
	 * @return the bArranged
	 */
	public Boolean getArranged()
	{
		return bArranged;
	}

	/**
	 * @return the dates
	 */
	public Vector<LectureDate> getDates()
	{
		return dates;
	}

	public void AddDate(LectureDate l)
	{
		dates.add(l);
	}

	public void setIsOptional(Boolean b)
	{
		bOptional = b;
	}

	public void setIsArranged(Boolean b)
	{
		bArranged = b;
	}

	public Vector<Employee> getLecturers()
	{
		return lecturers;
	}

	public void addLecturer(Employee e)
	{
		lecturers.add(e);
	}
}
