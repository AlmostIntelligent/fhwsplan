package de.almostintelligent.fhwsplan.timetable;

import android.util.Log;
import android.util.SparseArray;
import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.data.Day;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.filters.TimeTableFilter;

public class TimeTable
{

	static public TimeTable createFromFilter(TimeTableFilter f)
	{
		return new TimeTable(f.apply(DataUtils.get().getLectures()));

	}

	private SparseArray<Lecture>	_lectures;

	private TimeTable(SparseArray<Lecture> lectures)
	{
		_lectures = lectures;
	}
	
	public SparseArray<Lecture> getLecturesByDay(Day d)
	{
		TimeTableFilter f = new TimeTableFilter();
//		f.includeDay(d);
		
		return f.apply(_lectures);
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
		
	}

}
