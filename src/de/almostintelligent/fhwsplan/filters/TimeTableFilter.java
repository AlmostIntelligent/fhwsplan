package de.almostintelligent.fhwsplan.filters;

import java.util.Vector;

import android.util.SparseArray;

import de.almostintelligent.fhwsplan.data.DataWithID;
import de.almostintelligent.fhwsplan.data.Day;
import de.almostintelligent.fhwsplan.data.Employee;
import de.almostintelligent.fhwsplan.data.Faculty;
import de.almostintelligent.fhwsplan.data.Lecture;

public class TimeTableFilter
{

	private SparseArray<Lecture>	_includeLectures	= new SparseArray<Lecture>();
	private SparseArray<Lecture>	_excludeLectures	= new SparseArray<Lecture>();

	private SparseArray<Faculty>	_includeFaculty		= new SparseArray<Faculty>();
	private SparseArray<Faculty>	_excludeFaculty		= new SparseArray<Faculty>();

	private SparseArray<Employee>	_includeLecturer	= new SparseArray<Employee>();
	private SparseArray<Employee>	_excludeLecturer	= new SparseArray<Employee>();

	private SparseArray<Day>		_includeDay			= new SparseArray<Day>();
	private SparseArray<Day>		_excludeDay			= new SparseArray<Day>();

	/**
	 * @param o
	 *            Object to insert in Add
	 * @param remove
	 *            Object will be removed from this Vector (if vector contains
	 *            it)
	 * @param add
	 *            Vector o will be added to
	 */
	private <T> void checkInsert(DataWithID o, SparseArray<T> remove,
			SparseArray<T> add)
	{
		if (contains(o, remove))
			remove.remove(o.getID());

		// if (o instanceof T)
		add.put(o.getID(), (T) o);
	}

	private <T> boolean contains(DataWithID o, SparseArray<T> array)
	{
		return array.get(o.getID()) != null;
	}

	public <T> boolean passesFilter(DataWithID o, SparseArray<T> incl,
			SparseArray<T> excl)
	{
		boolean bInclude = contains(o, _includeLectures);
		boolean bExclude = contains(o, _excludeLectures);
		return (!bInclude && !bExclude) || (bInclude && !bExclude);
	}

	public void includeLecture(Lecture l)
	{
		checkInsert(l, _excludeLectures, _includeLectures);
		// if (excludeLectures.contains(l))
		// excludeLectures.remove(l);
		// includeLectures.add(l);
	}

	public void excludeLecture(Lecture l)
	{
		checkInsert(l, _includeLectures, _excludeLectures);
		// if (includeLectures.contains(l))
		// includeLectures.remove(l);
		// excludeLectures.add(l);
	}

	public void includeFaculty(Faculty f)
	{
		checkInsert(f, _excludeFaculty, _includeFaculty);
		// if (excludeFaculty.contains(f))
		// excludeFaculty.remove(f);
		// includeFaculty.add(f);
	}

	public void excludeFaculty(Faculty f)
	{
		checkInsert(f, _includeFaculty, _excludeFaculty);
		// if (includeFaculty.contains(f))
		// includeFaculty.remove(f);
		// excludeFaculty.add(f);
	}

	public void includeEmployee(Employee e)
	{
		checkInsert(e, _excludeLecturer, _includeLecturer);
	}

	public void excludeEmployee(Employee e)
	{
		checkInsert(e, _includeLecturer, _excludeLecturer);
	}

	public void includeDay(Day d)
	{
		checkInsert(d, _excludeDay, _includeDay);
	}

	private void excludeDay(Day d)
	{
		checkInsert(d, _includeDay, _excludeDay);
	}

	public SparseArray<Lecture> apply(SparseArray<Lecture> lectures)
	{
		SparseArray<Lecture> result = new SparseArray<Lecture>();

		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);

			if (passesFilter(l, _includeLectures, _excludeLectures)
					|| passesFilter(l, _includeFaculty, _excludeFaculty)
					|| passesFilter(l, _includeLecturer, _excludeLecturer))
			{
				result.put(l.getID(), l);
			}
		}

		return result;
	}

}
