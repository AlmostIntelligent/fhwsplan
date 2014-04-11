package de.almostintelligent.fhwsplan.data.sort;

import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.LectureDate;

public class LectureSortingNameAndRoom implements Comparable<LectureSortingNameAndRoom>
{

	public Lecture lecture;
	public LectureDate date;
	
	@Override
	public int compareTo(LectureSortingNameAndRoom another)
	{
		return lecture.getLectureName().compareTo(another.lecture.getLectureName());
	}

}
