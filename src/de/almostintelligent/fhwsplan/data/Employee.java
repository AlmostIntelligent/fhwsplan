package de.almostintelligent.fhwsplan.data;

public class Employee
{

	private Integer	iID;
	private String	strToken;
	private String	strPrename;
	private String	strSurname;

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
	 * @return the strShort
	 */
	public String getToken()
	{
		return strToken;
	}
	
	public void setToken(String tok)
	{
		strToken = tok;
	}

	/**
	 * @return the strPrename
	 */
	public String getPrename()
	{
		return strPrename;
	}
	
	public void setPrename(String name)
	{
		strPrename = name;
	}

	/**
	 * @return the strSurname
	 */
	public String getSurname()
	{
		return strSurname;
	}
	
	public void setSurname(String name)
	{
		strSurname = name;
	}
}
