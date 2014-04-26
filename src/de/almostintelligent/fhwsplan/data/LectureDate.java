package de.almostintelligent.fhwsplan.data;

import java.util.Vector;

import android.util.Log;

/**
 * @author Freddi
 * 
 */
public class LectureDate
{

	private Day				day;
	private PlanTime		time;
	private Vector<Room>	rooms	= new Vector<Room>();

	/**
	 * @return the day
	 */
	public Day getDay()
	{
		return day;
	}

	public void setDay(Day d)
	{
		day = d;
	}

	/**
	 * @return the time
	 */
	public PlanTime getTime()
	{
		return time;
	}

	public void setTime(PlanTime t)
	{
		time = t;
	}

	/**
	 * @return the rooms
	 */
	public Vector<Room> getRooms()
	{
		return rooms;
	}

	public void addRoom(Room r)
	{
		rooms.add(r);
	}

	public void print()
	{
		Log.d("lecturedate.id", "..");
		day.print();
		time.print();
		for (Room r : rooms)
		{
			r.print();
		}
	}

}
