package de.almostintelligent.fhwsplan.data;

abstract public class DataWithID
{
	private Integer	iID = Integer.valueOf(0);

	public Integer getID()
	{
		return iID;
	}

	public void setID(Integer id)
	{
		iID = id;
	}
}
