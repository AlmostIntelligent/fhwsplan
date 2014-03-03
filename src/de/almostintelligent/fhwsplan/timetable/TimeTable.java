package de.almostintelligent.fhwsplan.timetable;

import android.util.Log;
import android.util.SparseArray;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.filters.TimeTableFilter;

public class TimeTable
{

	static public TimeTable createFromFilter(TimeTableFilter f)
	{
		return new TimeTable(f.getLectures());

	}

	private SparseArray<Lecture>	_lectures;

	private TimeTable(SparseArray<Lecture> lectures)
	{
		_lectures = lectures;
	}
	

	public void print()
	{
		Log.e("timetable", "content:");
		for (int i = 0; i < _lectures.size(); ++i)
		{
			Integer iKey = _lectures.keyAt(i);
			Lecture l = _lectures.get(iKey);
			l.print();
		}
		Log.e("timetable.entrycount", String.valueOf(_lectures.size()));
	}

}
