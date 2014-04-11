package de.almostintelligent.fhwsplan.data;

import java.util.Vector;

import android.util.Log;
import android.util.SparseArray;

public class Lecture extends DataWithID
{

	private Integer							iParent						= 0;
	private String							strDescription;
	private String							strDescAppendix;
	private Integer							iDuration;
	private Boolean							bOptional					= false;
	private Boolean							bArranged					= true;

	private Vector<LectureDate>				dates						= new Vector<LectureDate>();
	private Vector<Employee>				lecturers					= new Vector<Employee>();
	private SparseArray<Vector<Integer>>	facultySemesterInLecture	= new SparseArray<Vector<Integer>>();

	public boolean isOnDay(Day d)
	{
		for (LectureDate ld : dates)
		{
			if (ld.getDay().getID() == d.getID())
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * @return the iParent
	 */
	public Integer getParent()
	{
		return iParent;
	}

	public void setParent(Integer parent)
	{
		iParent = parent;
	}

	public boolean isForFaculty(Faculty f)
	{
		// if (facultySemesterInLecture.get(f.getID()) != null)
		// {
		// Log.e("divider",
		// "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		// Log.e("lecture.isForFaculty", getDescription() + " ID: "
		// + getID().toString() + " is for faculty " + f.getLongName()
		// + " ID: " + f.getID().toString());
		// Log.e("divider",
		// "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		// }
		// else
		// {
		// Log.e("lecture.isForFaculty", getDescription() +
		// " IS NOT for faculty " + f.getLongName());
		// }
		return facultySemesterInLecture.get(f.getID()) != null;
	}

	public boolean hasSemester(Integer s)
	{
		int key = 0;
		for (int i = 0; i < facultySemesterInLecture.size(); i++)
		{
			key = facultySemesterInLecture.keyAt(i);
			Vector<Integer> semesters = facultySemesterInLecture.get(key);
			for (Integer semeseter : semesters)
			{
				if (s.equals(semeseter))
				{
					Log.e("lecture.hasSemester", s.toString() + " == "
							+ semeseter.toString());
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @return the strDescription
	 */
	public String getLectureName()
	{
		return strDescription;
	}

	/**
	 * @return the strDescAppendix
	 */
	public String getLectureAppendix()
	{
		return strDescAppendix;
	}

	/**
	 * @return the iDuration
	 */
	public Integer getDuration()
	{
		return iDuration;
	}

	/**
	 * @return the bOptional
	 */
	public Boolean getOptional()
	{
		return bOptional;
	}

	/**
	 * @return the bArranged
	 */
	public Boolean getArranged()
	{
		return bArranged;
	}

	/**
	 * @return the dates
	 */
	public Vector<LectureDate> getDates()
	{
		return dates;
	}

	public void addDate(LectureDate l)
	{
		dates.add(l);
	}

	public void setIsOptional(Boolean b)
	{
		bOptional = b;
	}

	public void setIsArranged(Boolean b)
	{
		bArranged = b;
	}

	public Vector<Employee> getLecturers()
	{
		return lecturers;
	}

	public void addLecturer(Employee e)
	{
		if (e == null)
			return;
		lecturers.add(e);
	}

	public void addFacultySemester(Faculty f, Integer s)
	{
		// if (f.getID() == 13)
		// Log.e("lecture.addFacultySemester",
		// getID().toString() + ": " + getDescription() + " "
		// + getDescAppendix() + " " + f.getLongName()
		// + " Semester: " + s.toString());
		if (facultySemesterInLecture.get(f.getID()) == null)
		{
			Vector<Integer> semester = new Vector<Integer>();
			semester.add(s);
			facultySemesterInLecture.put(f.getID(), semester);
		}
		else
		{
			facultySemesterInLecture.get(f.getID()).add(s);
		}
	}

	public boolean matchesFacultyAndSemester(Faculty f, Integer s)
	{
		if (facultySemesterInLecture.get(f.getID()) != null)
		{
			if (facultySemesterInLecture.get(f.getID()).contains(s))
				return true;
		}
		return false;
	}

	public void print()
	{
		Log.e("divider",
				"--------------------------------------------------------------------------");
		Log.e("lecture", getID().toString() + ": " + strDescription
				+ strDescAppendix);

		// for (Employee e : lecturers)
		// {
		// Log.e("lecture.lecturer", e.getPrename() + " " + e.getSurname());
		// }
		int key = 0;
		for (int i = 0; i < facultySemesterInLecture.size(); i++)
		{
			key = facultySemesterInLecture.keyAt(i);

			Faculty f = DataUtils.get().getFaculty(key);

			String strMsg = f.getID().toString() + " " + f.getLongName();

			for (Integer sem : facultySemesterInLecture.get(key))
			{
				strMsg = strMsg + " " + sem.toString();
			}

			Log.e("lecture.faculty", strMsg);
		}

	}

	public void setDuration(Integer duration)
	{
		iDuration = duration;
	}

	public void setDescription(String desc)
	{
		strDescription = desc;
	}

	public void setDescAppendix(String appendix)
	{
		strDescAppendix = appendix;
	}
}
