package de.almostintelligent.fhwsplan.utils;

import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.data.Employee;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.LectureDate;
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

	public static String generateEmployeeListString(Lecture l)
	{
		StringBuilder strLecturers = new StringBuilder();
		int i = 0, iLecturerCount = l.getLecturers().size();
		for (Employee e : l.getLecturers())
		{
			strLecturers.append(e.getToken());
			if (i != iLecturerCount - 1)
				strLecturers.append(", ");
			++i;
		}
		return strLecturers.toString();
	}

	public static String generateTimeString(Lecture l)
	{
		StringBuilder strTimes = new StringBuilder();
		if (l.getDates().size() != 0)
		{
			int i = 0, iDateCount = l.getDates().size();
			for (LectureDate d : l.getDates())
			{
				String strTime;
				if (l.getDuration() > 1)
				{
					strTime = d.getTime().getStartTime() + " - ";
					strTime += DataUtils.get()
							.getTime(d.getTime().getID() + l.getDuration() - 1)
							.getEndTime();
				}
				else
				{
					strTime = d.getTime().getTimeString();
				}
				strTimes.append(String.format("%s (%s)", d.getDay()
						.getShortName(), strTime));
				if (i != iDateCount - 1)
					strTimes.append(", ");
				++i;
			}
		}
		else
			strTimes.append("No Time");
		return strTimes.toString();
	}
}
