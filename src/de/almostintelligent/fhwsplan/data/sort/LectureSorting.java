package de.almostintelligent.fhwsplan.data.sort;

import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.LectureDate;

public class LectureSorting implements Comparable<LectureSorting>
{

	public Lecture		lecture;
	public LectureDate	date;

	@Override
	public int compareTo(LectureSorting another)
	{
		if (date.getTime().getID() < another.date.getTime().getID())
			return -1;
		if (date.getTime().getID() == another.date.getTime().getID())
			return 0;
		else
			return 1;
	}

}
