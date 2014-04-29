package de.almostintelligent.fhwsplan.data.loaders;

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

import de.almostintelligent.fhwsplan.data.CreationInfo;
import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.data.Day;
import de.almostintelligent.fhwsplan.data.Employee;
import de.almostintelligent.fhwsplan.data.Faculty;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.LectureDate;
import de.almostintelligent.fhwsplan.data.PlanTime;
import de.almostintelligent.fhwsplan.data.Room;
import de.almostintelligent.fhwsplan.data.interfaces.IDataLoader;

public class LocalXmlDataLoader implements IDataLoader, ContentHandler
{

	private String			currentValue;
	private CreationInfo	createInfo;
	private DataUtils		utils;

	// Variables for Loading Stuff
	private boolean			bLoadingDays;
	private Day				dayLoading;

	private boolean			bLoadingPlanTimes;
	private PlanTime		planTimeLoading;

	private boolean			bLoadingRooms;
	private Room			roomLoading;

	private boolean			bLoadingEmployees;
	private Employee		employeeLoading;

	private boolean			bLoadingFaculties;
	private Faculty			facultyLoading;

	private boolean			bLoadingLecutres;
	private Lecture			lectureLoading;

	@Override
	public boolean loadData(Context context, DataUtils utils)
	{
		if (utils == null)
			return false;

		this.utils = utils;
		try
		{
			System.setProperty("org.xml.sax.driver",
					"org.xmlpull.v1.sax2.Driver");
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();

			StringBuilder buf = new StringBuilder();
			InputStream content = context.getAssets().open("plan_raw.xml");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					content));
			String str;

			while ((str = in.readLine()) != null)
			{
				buf.append(str);
			}

			in.close();

			String strContent = buf.toString();
			if (strContent.length() > 3)
			{
				byte[] BOM = strContent.substring(0, 3).getBytes();

				int iStart = 0;
				if (BOM[0] == 0xFF && BOM[1] == 0xFE) // UTF16 Little Endian
				{
					iStart = 2;
				}
				else if (BOM[0] == 0xFE && BOM[1] == 0xFF) // UTF16 Big Endian
				{
					iStart = 2;
				}
				else if (BOM[0] == 0xEF && BOM[1] == 0xBB && BOM[2] == 0xBF) // UTF8
				{
					iStart = 3;
				}

				strContent = strContent.substring(iStart);
			}

			// Ugly :S Maybe somehow shift to the BufferedReader
			strContent = strContent.replaceAll("&uuml;", "Ÿ")
					.replaceAll("&ouml;", "š").replaceAll("&auml;", "Š")
					.replaceAll("&Uuml;", "†").replaceAll("&Ouml;", "…")
					.replaceAll("&Auml;", "€").replaceAll("&szlig;", "§");

			StringReader fileReader = new StringReader(strContent);
			InputSource src = new InputSource(fileReader);

			xmlReader.setContentHandler(this);
			xmlReader.parse(src);

			return true;
		}
		catch (SAXException e)
		{
			Log.e("DataUtils", "SAXException");
			Log.e("DataUtils.SAXException", e.getMessage());
			Log.e("DataUtils.SAXException", e.getLocalizedMessage());

			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			Log.e("DataUtils", "FileNotFoundException");
			Log.e("DataUtils.FileNotFoundException", e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Log.e("DataUtils", "IOException");
			Log.e("DataUtils.IOException", e.getMessage());
			e.printStackTrace();
		}

		return false;
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
			utils.addDay(dayLoading);
			dayLoading = null;
		}
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException
	{
		// TODO Auto-generated method stub

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
			lectureDateLoading.setDay(utils.getDay(Integer
					.valueOf(currentValue)));
		}
		else if (localName.equalsIgnoreCase("zeit"))
		{
			lectureDateLoading.setTime(utils.getTime(Integer
					.valueOf(currentValue)));
		}
		else if (localName.equalsIgnoreCase("raum"))
		{
			lectureDateLoading.addRoom(utils.getRoom(Integer
					.valueOf(currentValue)));
		}
		else if (localName.equalsIgnoreCase("termin"))
		{
			lectureLoading.addDate(lectureDateLoading);
			lectureDateLoading = null;
		}
	}

	boolean	bLoadingLectureLecturer_dozent;
	boolean	bLoadingLectureLecturer_hoerer;

	private void endLectureLecturerElement(String localName)
	{
		if (bLoadingLectureLecturer_dozent)
		{
			if (localName.equalsIgnoreCase("id"))
			{
				lectureLoading.addLecturer(utils.getEmployee(Integer
						.valueOf(currentValue)));
			}
			else
			{
				if (localName.equalsIgnoreCase("dozent"))
					bLoadingLectureLecturer_dozent = false;
			}
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
					utils.getFaculty(iLectureFacultyLoading),
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
			utils.addLecture(lectureLoading);
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
			utils.addFaculty(facultyLoading);
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
		else if (localName.equalsIgnoreCase("teilnahme"))
		{
			employeeLoading.setAttendance(Boolean.valueOf(currentValue));
		}
		else if (localName.equalsIgnoreCase("person"))
		{
			utils.addEmployee(employeeLoading);
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
			utils.addTime(planTimeLoading);
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
			utils.addRoom(roomLoading);
			roomLoading = null;
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
			if (bLoadingLectureLecturer && localName.equalsIgnoreCase("dozent"))
			{
				bLoadingLectureLecturer_dozent = true;
			}
			else
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

	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

}
