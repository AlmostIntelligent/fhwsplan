package de.almostintelligent.fhwsplan.data;

import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

import de.almostintelligent.fhwsplan.data.interfaces.IDataLoader;
import de.almostintelligent.fhwsplan.data.loaders.LocalXmlDataLoader;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

public class DataUtils
{

	private static DataUtils	instance	= null;

	public static DataUtils get()
	{
		if (instance == null)
			instance = new DataUtils();
		return instance;
	}

	// Members
	private CreationInfo			createInfo;
	private SparseArray<Day>		days		= new SparseArray<Day>();
	private SparseArray<PlanTime>	times		= new SparseArray<PlanTime>();
	private SparseArray<Room>		rooms		= new SparseArray<Room>();
	private SparseArray<Employee>	employees	= new SparseArray<Employee>();
	private SparseArray<Faculty>	faculties	= new SparseArray<Faculty>();
	private SparseArray<Lecture>	lectures	= new SparseArray<Lecture>();

	public void setCreationInfo(CreationInfo info)
	{
		createInfo = info;
	}

	public CreationInfo getCreationInfo()
	{
		return createInfo;
	}

	private DataUtils()
	{
	}

	public Vector<String> getEmployeeNamesFormated()
	{
		Vector<String> result = new Vector<String>();

		for (int i = 0; i < employees.size(); ++i)
		{
			Integer iKey = employees.keyAt(i);
			Employee e = employees.get(iKey);
			result.add(String.format("%s %s (%s)", e.getPrename(),
					e.getSurname(), e.getToken()));
		}

		Collections.sort(result);

		return result;
	}

	private <T> Vector<T> toVector(SparseArray<T> r)
	{
		Vector<T> result = new Vector<T>();

		for (int i = 0; i < r.size(); ++i)
		{
			Integer iKey = r.keyAt(i);
			T f = r.get(iKey);
			result.add(f);
		}

		return result;
	}

	public Vector<PlanTime> getPlanTimes()
	{
		return toVector(times);
	}

	public Vector<Day> getDays()
	{
		return toVector(days);
	}

	public Vector<Employee> getEmployees()
	{
		return toVector(employees);
	}

	public Vector<Faculty> getFaculties()
	{
		return toVector(faculties);
	}

	public Vector<String> getFacultyNamesFormated()
	{
		Vector<String> result = new Vector<String>();

		for (int i = 0; i < faculties.size(); ++i)
		{
			Integer iKey = faculties.keyAt(i);
			Faculty f = faculties.get(iKey);
			result.add(String.format("%s", f.getLongName()));
		}

		Collections.sort(result);

		return result;
	}

	public Vector<Lecture> getLecturesByDay(Day d, Faculty f, Integer semester)
	{
		Vector<Lecture> result = new Vector<Lecture>();

		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);
			if (l.isOnDay(d))
			{
				if (l.matchesFacultyAndSemester(f, semester))
					result.add(l);
			}
		}

		return result;
	}

	public SparseArray<Lecture> getLectures()
	{
		return lectures;
	}

	public Day getToday()
	{
		Calendar now = Calendar.getInstance();
		int iDay = now.get(Calendar.DAY_OF_WEEK);
		return days.get(iDay - 1);
	}

	/**
	 * @param d
	 */
	public void addDay(Day d)
	{
		days.put(d.getID(), d);
	}

	/**
	 * @param e
	 */
	public void addEmployee(Employee e)
	{
		employees.put(e.getID(), e);
	}

	public void addFaculty(Faculty f)
	{
		faculties.put(f.getID(), f);
	}

	/**
	 * @param r
	 */
	public void addRoom(Room r)
	{
		rooms.put(r.getID(), r);
	}

	/**
	 * @param t
	 */
	public void addTime(PlanTime t)
	{
		times.put(t.getID(), t);
	}

	/**
	 * @param id
	 * @return
	 */
	public Day getDay(Integer id)
	{
		return days.get(id);
	}

	/**
	 * @param id
	 * @return
	 */
	public Employee getEmployee(Integer id)
	{
		return employees.get(id);
	}

	public Faculty getFaculty(Integer id)
	{
		return faculties.get(id);
	}

	/**
	 * @param id
	 * @return
	 */
	public Room getRoom(Integer id)
	{
		return rooms.get(id);
	}

	/**
	 * @param id
	 * @return
	 */
	public PlanTime getTime(Integer id)
	{
		return times.get(id);
	}

	public void addLecture(Lecture l)
	{
		lectures.put(l.getID(), l);
	}

	public void load(Context context)
	{
		IDataLoader loader = new LocalXmlDataLoader();
		if (!loader.loadData(context, this))
		{
			Log.e("Loader.Error", "Could not load Content");
		}
	}

	public int getPlanTimeCount()
	{
		return times.size();
	}

}
