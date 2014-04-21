package de.almostintelligent.fhwsplan.utils;

import android.view.View;
import android.widget.TextView;

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

	public static void setTextViewTextByID(int resID, View parent,
			String caption)
	{
		if (parent == null)
			return;
		TextView txt = (TextView) parent.findViewById(resID);
		if (txt != null)
		{
			txt.setText(caption);
		}
	}
}
