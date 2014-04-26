package de.almostintelligent.fhwsplan.data;

import android.util.Log;

public class Employee extends DataWithID implements Comparable<Employee>
{

	private String	strToken	= new String();
	private String	strPrename	= new String();
	private String	strSurname	= new String();
	private Boolean	bAttendance	= Boolean.valueOf(false);

	public String getNameFormated()
	{
		if (strToken.equals("") && strSurname.equals(""))
		{
			return getPrename();
		}
		return String.format("%s, %s (%s)", getSurname(), getPrename(),
				getToken());
	}

	private String getSortName()
	{
		return String.format("%s %s (%s)", getSurname(), getPrename(),
				getToken());
	}

	public void setAttendance(Boolean b)
	{
		bAttendance = b;
	}

	public Boolean getAttendance()
	{
		return bAttendance;
	}

	/**
	 * @return the token
	 */
	public String getToken()
	{
		return strToken;
	}

	/**
	 * @param tok
	 *            The new Token
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
	 * @param name
	 *            New Prename
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
	 * @param name
	 *            New Surame
	 */
	public void setSurname(String name)
	{
		strSurname = name;
	}

	public void print()
	{
		Log.d("employee.id", getID().toString());
		Log.d("employee.token", strToken);
		Log.d("employee.prename", strPrename);
		Log.d("employee.surname", strSurname);
	}

	@Override
	public int compareTo(Employee another)
	{
		return getSortName().compareTo(another.getSortName());
	}
}
