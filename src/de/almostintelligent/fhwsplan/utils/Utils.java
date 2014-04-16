package de.almostintelligent.fhwsplan.utils;

public class Utils
{
	public static int maxInArray(int[] array)
	{
		int max = Integer.MIN_VALUE;
		for (int i : array)
		{
			if (i > max)
				max = i;
		}

		return max;
	}
}
