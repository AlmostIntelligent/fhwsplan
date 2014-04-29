package de.almostintelligent.fhwsplan.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlanManager
{
	private Map<String, Set<Integer>>	MapNameToLectures	= new HashMap<String, Set<Integer>>();

	private String						strCurrentPlan;

	public String getCurrentPlanName()
	{
		return strCurrentPlan;
	}

	public Set<Integer> getCurrentPlan()
	{
		return getPlan(getCurrentPlanName());
	}

	public boolean hasPlan(String strName)
	{
		return MapNameToLectures.containsKey(strName);
	}

	public Set<Integer> getPlan(String strName)
	{
		if (!hasPlan(strName))
			return null;

		return MapNameToLectures.get(strName);
	}

	public void addPlan(String strName, Set<Integer> lectures)
	{
		if (hasPlan(strName))
			return;

		MapNameToLectures.put(strName, lectures);
	}

	public void updatePlan(String strName, Set<Integer> lectures)
	{
		MapNameToLectures.remove(strName);
		addPlan(strName, lectures);
	}

	public boolean load()
	{
		return false;
	}

	public boolean save()
	{
		return false;
	}
}
