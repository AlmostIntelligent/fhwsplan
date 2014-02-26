package de.almostintelligent.fhwsplan.data;

import java.util.Vector;

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

	/**
	 * @return the time
	 */
	public PlanTime getTime()
	{
		return time;
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

	public LectureDate(Day d, PlanTime p)
	{
		day = d;
		time = p;
	}
}
