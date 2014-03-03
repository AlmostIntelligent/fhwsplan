package de.almostintelligent.fhwsplan.filters;

import java.util.HashSet;

import android.util.Log;
import android.util.SparseArray;

import de.almostintelligent.fhwsplan.data.Day;
import de.almostintelligent.fhwsplan.data.Faculty;
import de.almostintelligent.fhwsplan.data.Lecture;

public class TimeTableFilter
{

	private SparseArray<Lecture>	_lectures	= new SparseArray<Lecture>();

	private SparseArray<Lecture> getLecturesCopy()
	{
		SparseArray<Lecture> lectures = new SparseArray<Lecture>();

		for (int i = 0; i < _lectures.size(); ++i)
		{
			Integer iKey = _lectures.keyAt(i);
			Lecture l = _lectures.get(iKey);
			lectures.put(l.getID(), l);
		}

		return lectures;
	}

	public SparseArray<Lecture> getLectures()
	{
		return _lectures;
	}

	public TimeTableFilter(SparseArray<Lecture> lectures)
	{
		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);
			_lectures.put(l.getID(), l);
		}
	}

	public TimeTableFilter whereID(Integer id)
	{
		SparseArray<Lecture> lectures = getLecturesCopy();
		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);
			if (l != null)
			{
				if (l.getID() != id)
				{
					_lectures.remove(iKey);
				}
			}
		}
		return this;
	}

	public void printSize(String msg)
	{
		Log.e("TimeTableFilter.printsize", Integer.valueOf(_lectures.size())
				.toString() + " Items: " + msg);
	}

	public TimeTableFilter whereIDs(HashSet<Integer> ids)
	{
		SparseArray<Lecture> lectures = getLecturesCopy();
		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);
			if (l != null)
			{
				if (!ids.contains(l.getID()))
				{
					_lectures.remove(iKey);
				}
			}
		}
		return this;
	}

	public TimeTableFilter whereFaculty(Faculty f)
	{
		SparseArray<Lecture> lectures = getLecturesCopy();
		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);
			if (l != null)
			{
				if (!l.isForFaculty(f))
				{
					_lectures.removeAt(i);
				}

			}

		}

		return this;
	}
	
	public TimeTableFilter whereFacultyAndSemester(Faculty f, Integer s)
	{
		SparseArray<Lecture> lectures = getLecturesCopy();
		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);
			if (l != null)
			{
				if (!l.matchesFacultyAndSemester(f, s))
				{
					_lectures.removeAt(i);
				}

			}

		}

		return this;
	}

	public TimeTableFilter whereSemester(Integer semester)
	{
		SparseArray<Lecture> lectures = getLecturesCopy();
		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);
			if (l != null)
			{
				if (!l.hasSemester(semester))
				{
					_lectures.remove(iKey);
				}
			}
		}
		// printSize("whereSemester after");
		return this;
	}

	public TimeTableFilter whereDay(Day d)
	{
		SparseArray<Lecture> lectures = getLecturesCopy();
		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);
//			if (l != null)
			{
				if (!l.isOnDay(d))
				{
					_lectures.remove(iKey);
				}
			}
		}
		return this;
	}

}
