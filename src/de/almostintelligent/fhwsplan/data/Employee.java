package de.almostintelligent.fhwsplan.data;

import android.util.Log;

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

	/**
	 * @param id
	 */
	public void setID(Integer id)
	{
		iID = id;
	}

	/**
	 * @return the token
	 */
	public String getToken()
	{
		return strToken;
	}
	
	/**
	 * @param tok The new Token
	 */
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
	
	/**
	 * @param name New Prename
	 */
	public void setPrename(String name)
	{
		strPrename = name;
	}

	/**
	 * @return the Surname
	 */
	public String getSurname()
	{
		return strSurname;
	}
	
	/**
	 * @param name New Surame
	 */
	public void setSurname(String name)
	{
		strSurname = name;
	}

	public void print()
	{
		Log.e("employee.id", iID.toString());
		Log.e("employee.token", strToken);
		Log.e("employee.prename", strPrename);
		Log.e("employee.surname", strSurname);
	}
}
