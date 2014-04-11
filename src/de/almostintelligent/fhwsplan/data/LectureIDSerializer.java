package de.almostintelligent.fhwsplan.data;

import java.util.HashSet;

public class LectureIDSerializer
{
	static public String serialize(HashSet<Integer> set)
	{
		String result = new String();

		if (set.size() == 0)
			return result;

		for (Integer id : set)
		{
			result += id.toString() + ",";
		}

		result = result.substring(0, result.length() - 1);
		return result;
	}

	static public HashSet<Integer> deserialize(String str)
	{
		HashSet<Integer> result = new HashSet<Integer>();

		if (str.indexOf(",") == -1 && str.length() == 0)
			return result;

		String[] ids = str.split(",");
		for (String id : ids)
		{
			Integer iID = Integer.valueOf(id);
			result.add(iID);
		}

		return result;
	}
}
