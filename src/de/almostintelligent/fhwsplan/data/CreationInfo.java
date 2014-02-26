package de.almostintelligent.fhwsplan.data;

public class CreationInfo
{

	private String	strVersion;
	private String	strAuthor;
	private String	strTermType;

	public String getAuthor()
	{
		return strAuthor;
	}

	public void setAuthor(String a)
	{
		strAuthor = a;
	}

	public String getTermType()
	{
		return strTermType;
	}

	public void setTermType(String t)
	{
		strTermType = t;
	}

	public String getVersion()
	{
		return strVersion;
	}

	public void setVersion(String v)
	{
		strVersion = v;
	}
}
