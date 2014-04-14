package de.almostintelligent.fhwsplan.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

public class DataUtils implements ContentHandler
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

	private String					currentValue;

	// Variables for Loading Stuff
	private boolean					bLoadingDays;
	private Day						dayLoading;

	private boolean					bLoadingPlanTimes;
	private PlanTime				planTimeLoading;

	private boolean					bLoadingRooms;
	private Room					roomLoading;

	private boolean					bLoadingEmployees;
	private Employee				employeeLoading;

	private boolean					bLoadingFaculties;
	private Faculty					facultyLoading;

	private boolean					bLoadingLecutres;
	private Lecture					lectureLoading;

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

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		currentValue = new String(ch, start, length).trim();

	}

	private void endCreateInfoElement(String localName)
	{
		if (localName.equalsIgnoreCase("autor"))
		{
			createInfo.setAuthor(currentValue);
		}
		else if (localName.equalsIgnoreCase("version"))
		{
			createInfo.setVersion(currentValue);
		}
		else if (localName.equalsIgnoreCase("semestertyp"))
		{
			createInfo.setTermType(currentValue);
		}
	}

	private void endDayElement(String localName)
	{
		if (localName.equalsIgnoreCase("kurz"))
		{
			dayLoading.setShortName(currentValue);
		}
		else if (localName.equalsIgnoreCase("lang"))
		{
			dayLoading.setLongName(currentValue);
		}
		else if (localName.equalsIgnoreCase("id"))
		{
			dayLoading.setID(Integer.valueOf(currentValue));
		}
		else if (localName.equalsIgnoreCase("tag"))
		{
			days.put(dayLoading.getID(), dayLoading);
			dayLoading = null;
		}
	}

	@Override
	public void endDocument() throws SAXException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{

		if (createInfo != null)
		{
			endCreateInfoElement(localName);
		}

		if (bLoadingDays)
		{
			if (dayLoading != null)
			{
				endDayElement(localName);
			}
			if (localName.equalsIgnoreCase("tage"))
			{
				bLoadingDays = false;
			}
		}
		else if (bLoadingPlanTimes)
		{
			if (planTimeLoading != null)
			{
				endPlanTimeElement(localName);
			}
			if (localName.equalsIgnoreCase("zeiten"))
			{
				bLoadingPlanTimes = false;
			}
		}
		else if (bLoadingRooms)
		{
			if (roomLoading != null)
			{
				endRoomElement(localName);
			}
			if (localName.equalsIgnoreCase("raeume"))
			{
				bLoadingRooms = false;
			}
		}
		else if (bLoadingEmployees)
		{
			if (employeeLoading != null)
			{
				endEmployeeElement(localName);
			}
			if (localName.equalsIgnoreCase("mitarbeiter"))
			{
				bLoadingEmployees = false;
			}
		}
		else if (bLoadingFaculties)
		{
			if (facultyLoading != null)
			{
				endFacultyElement(localName);
			}
			if (localName.equalsIgnoreCase("fachrichtungen"))
			{
				bLoadingFaculties = false;
			}
		}
		else if (bLoadingLecutres)
		{
			if (lectureLoading != null)
			{
				endLectureElement(localName);
			}
			if (localName.equalsIgnoreCase("veranstaltungen"))
			{
				bLoadingLecutres = false;
			}
		}

	}

	private boolean		bLoadingLectureDate;
	private LectureDate	lectureDateLoading;
	private boolean		bLoadingLectureLecturer;
	private boolean		bLoadingLectureFaculties;

	private void endLectureDateElement(String localName)
	{
		if (localName.equalsIgnoreCase("tag"))
		{
			lectureDateLoading.setDay(getDay(Integer.valueOf(currentValue)));
		}
		else if (localName.equalsIgnoreCase("zeit"))
		{
			lectureDateLoading.setTime(getTime(Integer.valueOf(currentValue)));
		}
		else if (localName.equalsIgnoreCase("raum"))
		{
			lectureDateLoading.addRoom(getRoom(Integer.valueOf(currentValue)));
		}
		else if (localName.equalsIgnoreCase("termin"))
		{
			lectureLoading.addDate(lectureDateLoading);
			lectureDateLoading = null;
		}
	}

	private void endLectureLecturerElement(String localName)
	{
		if (localName.equalsIgnoreCase("id"))
		{
			lectureLoading.addLecturer(getEmployee(Integer
					.valueOf(currentValue)));
		}
	}

	private Integer	iLectureFacultyLoading;

	private void endLectureFacultiesElement(String localName)
	{
		if (localName.equalsIgnoreCase("id"))
		{
			iLectureFacultyLoading = Integer.valueOf(currentValue);
		}
		else if (localName.equalsIgnoreCase("sem"))
		{
			lectureLoading.addFacultySemester(
					getFaculty(iLectureFacultyLoading),
					Integer.valueOf(currentValue));
		}
	}

	private void endLectureElement(String localName)
	{
		if (bLoadingLectureDate)
		{
			if (lectureDateLoading != null)
			{
				endLectureDateElement(localName);
			}
			if (localName.equalsIgnoreCase("termine"))
			{
				bLoadingLectureDate = false;
			}
		}
		else if (bLoadingLectureLecturer)
		{
			if (localName.equalsIgnoreCase("mitarbeiter"))
			{
				bLoadingLectureLecturer = false;
			}
			else
				endLectureLecturerElement(localName);

		}
		else if (bLoadingLectureFaculties)
		{
			if (localName.equalsIgnoreCase("fachrichtungen"))
			{
				bLoadingLectureFaculties = false;
			}
			else
				endLectureFacultiesElement(localName);
		}
		else if (localName.equalsIgnoreCase("id"))
		{
			lectureLoading.setID(Integer.valueOf(currentValue));
		}
		else if (localName.equalsIgnoreCase("parent"))
		{
			try
			{
				lectureLoading.setParent(Integer.valueOf(currentValue));
			}
			catch (java.lang.NumberFormatException e)
			{
				lectureLoading.setParent(0);
			}
		}
		else if (localName.equalsIgnoreCase("stunden"))
		{
			lectureLoading.setDuration(Integer.valueOf(currentValue));
		}
		else if (localName.equalsIgnoreCase("bezeichnung"))
		{
			lectureLoading.setDescription(currentValue);
		}
		else if (localName.equalsIgnoreCase("bez_zusatz"))
		{
			lectureLoading.setDescAppendix(currentValue);
		}
		else if (localName.equalsIgnoreCase("veranstaltung"))
		{
			lectures.put(lectureLoading.getID(), lectureLoading);
			lectureLoading = null;
		}
	}

	private void endFacultyElement(String localName)
	{
		if (localName.equalsIgnoreCase("kurz"))
		{
			facultyLoading.setShortName(currentValue);
		}
		else if (localName.equalsIgnoreCase("lang"))
		{
			facultyLoading.setLongName(currentValue);
		}
		else if (localName.equalsIgnoreCase("sem"))
		{
			facultyLoading.addSemester(Integer.valueOf(currentValue));
		}
		else if (localName.equalsIgnoreCase("id"))
		{
			facultyLoading.setID(Integer.valueOf(currentValue));
		}
		else if (localName.equalsIgnoreCase("fachrichtung"))
		{
			faculties.put(facultyLoading.getID(), facultyLoading);
			facultyLoading = null;
		}
	}

	private void endEmployeeElement(String localName)
	{

		if (localName.equalsIgnoreCase("kuerzel"))
		{
			employeeLoading.setToken(currentValue);
		}
		else if (localName.equalsIgnoreCase("vorname"))
		{
			employeeLoading.setPrename(currentValue);
		}
		else if (localName.equalsIgnoreCase("nachname"))
		{
			employeeLoading.setSurname(currentValue);
		}
		else if (localName.equalsIgnoreCase("id"))
		{
			employeeLoading.setID(Integer.valueOf(currentValue));
		}
		else if (localName.equalsIgnoreCase("person"))
		{
			employees.put(employeeLoading.getID(), employeeLoading);
			employeeLoading = null;
		}
	}

	private void endPlanTimeElement(String localName)
	{
		if (localName.equalsIgnoreCase("id"))
		{
			planTimeLoading.setID(Integer.valueOf(currentValue));
		}
		else if (localName.equalsIgnoreCase("bezeichnung"))
		{
			planTimeLoading.setTimeString(currentValue);
		}
		else if (localName.equalsIgnoreCase("zeit"))
		{
			times.put(planTimeLoading.getID(), planTimeLoading);
			planTimeLoading = null;
		}
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException
	{
		// TODO Auto-generated method stub

	}

	private void endRoomElement(String localName)
	{
		if (localName.equalsIgnoreCase("kurz"))
		{
			roomLoading.setShortName(currentValue);
		}
		else if (localName.equalsIgnoreCase("lang"))
		{
			roomLoading.setLongName(currentValue);
		}
		else if (localName.equalsIgnoreCase("id"))
		{
			roomLoading.setID(Integer.valueOf(currentValue));
		}
		else if (localName.equalsIgnoreCase("raum"))
		{
			rooms.put(roomLoading.getID(), roomLoading);
			roomLoading = null;
		}
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

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

	public void load(Context context)
	{
		try
		{
			System.setProperty("org.xml.sax.driver",
					"org.xmlpull.v1.sax2.Driver");
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();

			StringBuilder buf = new StringBuilder();
			InputStream json = context.getAssets().open("plan.xml");
			BufferedReader in = new BufferedReader(new InputStreamReader(json));
			String str;

			while ((str = in.readLine()) != null)
			{
				buf.append(str);
			}

			in.close();
			String strContent = buf.toString();
			StringReader fileReader = new StringReader(strContent);
			InputSource src = new InputSource(fileReader);

			xmlReader.setContentHandler(this);
			xmlReader.parse(src);

		}
		catch (SAXException e)
		{
			Log.e("DataUtils", "SAXException");
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			Log.e("DataUtils", "FileNotFoundException");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Log.e("DataUtils", "IOException");
			e.printStackTrace();
		}

	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(Locator locator)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String name) throws SAXException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument() throws SAXException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException
	{

		if (localName.equalsIgnoreCase("erstellung"))
		{
			createInfo = new CreationInfo();
		}
		else if (localName.equalsIgnoreCase("tage"))
		{
			bLoadingDays = true;
		}
		else if (localName.equalsIgnoreCase("zeiten"))
		{
			bLoadingPlanTimes = true;
		}
		else if (localName.equalsIgnoreCase("raeume") && !bLoadingLecutres)
		{
			bLoadingRooms = true;
		}
		else if (localName.equalsIgnoreCase("mitarbeiter") && !bLoadingLecutres)
		{
			bLoadingEmployees = true;
		}
		else if (localName.equalsIgnoreCase("fachrichtungen")
				&& !bLoadingLecutres)
		{
			bLoadingFaculties = true;
		}
		else if (localName.equalsIgnoreCase("veranstaltungen"))
		{
			bLoadingLecutres = true;
		}

		if (bLoadingDays && localName.equalsIgnoreCase("tag"))
		{
			dayLoading = new Day();
		}
		else if (bLoadingPlanTimes && localName.equalsIgnoreCase("zeit"))
		{
			planTimeLoading = new PlanTime();
		}
		else if (bLoadingRooms && localName.equalsIgnoreCase("raum"))
		{
			roomLoading = new Room();
		}
		else if (bLoadingEmployees && localName.equalsIgnoreCase("person"))
		{
			employeeLoading = new Employee();
		}
		else if (bLoadingFaculties
				&& localName.equalsIgnoreCase("fachrichtung"))
		{
			facultyLoading = new Faculty();
		}
		else if (bLoadingLecutres)
		{
			if (localName.equalsIgnoreCase("veranstaltung"))
			{
				lectureLoading = new Lecture();
			}
			else if (localName.equalsIgnoreCase("termine"))
			{
				bLoadingLectureDate = true;
			}
			else if (bLoadingLectureDate
					&& localName.equalsIgnoreCase("termin"))
			{
				lectureDateLoading = new LectureDate();
			}
			else if (localName.equalsIgnoreCase("mitarbeiter"))
			{
				bLoadingLectureLecturer = true;
			}
			else if (localName.equalsIgnoreCase("fachrichtungen"))
			{
				bLoadingLectureFaculties = true;
			}
		}

	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

}
