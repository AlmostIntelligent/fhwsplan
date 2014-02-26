package de.almostintelligent.fhwsplan.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

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

	private DataUtils()
	{
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

	/**
	 * @param d
	 */
	public void addDay(Day d)
	{
		days.put(d.getID(), d);
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
	public PlanTime getTime(Integer id)
	{
		return times.get(id);
	}

	/**
	 * @param r
	 */
	public void addRoom(Room r)
	{
		rooms.put(r.getID(), r);
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
	 * @param e
	 */
	public void addEmployee(Employee e)
	{
		employees.put(e.getID(), e);
	}

	/**
	 * @param id
	 * @return
	 */
	public Employee getEmployee(Integer id)
	{
		return employees.get(id);
	}

	public void addFaculty(Faculty f)
	{
		faculties.put(f.getID(), f);
	}

	public Faculty getFaculty(Integer id)
	{
		return faculties.get(id);
	}

	private String		currentValue;

	// Variables for Loading Stuff
	private boolean		bLoadingDays;
	private Day			dayLoading;

	private boolean		bLoadingPlanTimes;
	private PlanTime	planTimeLoading;

	private boolean		bLoadingRooms;
	private Room		roomLoading;

	private boolean		bLoadingEmployees;
	private Employee	employeeLoading;

	private boolean		bLoadingLecutres;
	private Lecture		lectureLoading;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		currentValue = new String(ch, start, length);

	}

	@Override
	public void endDocument() throws SAXException
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
		else if (localName.equalsIgnoreCase("mitarbeiter"))
		{
			bLoadingEmployees = true;
		}
		// Fachrichtung
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
		// Fachrichtung
		else if (bLoadingLecutres
				&& localName.equalsIgnoreCase("veranstaltung"))
		{
			lectureLoading = new Lecture();
		}

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

	}

	private void endRoomElement(String localName)
	{
		// try
		// {
		// Log.e("sleep", "1");
		// Thread.sleep(10);
		// }
		// catch (InterruptedException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
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
			roomLoading.print();
			roomLoading = null;
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
			planTimeLoading.setDescription(currentValue);
		}
		else if (localName.equalsIgnoreCase("zeit"))
		{
			times.put(planTimeLoading.getID(), planTimeLoading);
			// planTimeLoading.print();
			planTimeLoading = null;
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
			// dayLoading.print();
			dayLoading = null;
		}
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException
	{
		// TODO Auto-generated method stub

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
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

}
