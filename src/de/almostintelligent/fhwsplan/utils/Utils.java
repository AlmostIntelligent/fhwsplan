package de.almostintelligent.fhwsplan.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.data.Employee;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.LectureDate;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Utils
{
	public static Set<Integer> getLecturesFromURL(String strUrl)
	{
		Set<Integer> result = null;

		strUrl = strUrl.substring(strUrl.indexOf('?') + 1);
		String[] parts = strUrl.split("&");

		result = new HashSet<Integer>();
		boolean bTypFound = false;

		for (String part : parts)
		{
			String[] elements = part.split("=");
			if (elements.length == 2)
			{
				if (elements[0].equals("typ")
						&& elements[1].equals("benutzer_vz_ausgabe"))
					bTypFound = true;

				if (elements[0].equals("id"))
				{
					try
					{
						Integer id = Integer.valueOf(elements[1]);
						result.add(id);
					}
					catch (NumberFormatException e)
					{
						Log.w("Utils.getLecturesFromURL",
								"Could not convert ID");
					}
				}

			}
		}

		if (!bTypFound)
		{
			Log.w("Utils.getLecturesFromURL",
					"Splan URL is not of type 'benutzer_vz_ausgabe'");
		}

		return result;
	}

	public static Document openXml(Context c, String filename)
	{
		Document document = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Log.e("Utils.openXml", "Loading: " + filename);
		try
		{
			DocumentBuilder builder = dbf.newDocumentBuilder();
			// d = builder.parse(new File(c.getFilesDir(), filename));
			// document = builder.parse(c.getAssets().open(filename));
			document = builder.parse(new File(c.getFilesDir(), filename));

			if (document == null)
			{
				Log.e("Utils.openXml", "Loading of file " + filename
						+ " failed.");
			}
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return document;
	}

	public static int maxInArray(int[] array)
	{
		int max = Integer.MIN_VALUE;
		for (int i : array)
		{
			if (i > max)
				max = i;
		}

		return max;
	}

	public static void setTextViewTextByID(int resID, View parent,
			String caption)
	{
		if (parent == null)
			return;
		TextView txt = (TextView) parent.findViewById(resID);
		if (txt != null)
		{
			txt.setText(caption);
		}
	}

	public static String generateEmployeeListString(Lecture l)
	{
		StringBuilder strLecturers = new StringBuilder();
		int i = 0, iLecturerCount = l.getLecturers().size();
		for (Employee e : l.getLecturers())
		{
			strLecturers.append(e.getToken());
			if (i != iLecturerCount - 1)
				strLecturers.append(", ");
			++i;
		}
		return strLecturers.toString();
	}

	public static String generateTimeString(Lecture l)
	{
		StringBuilder strTimes = new StringBuilder();
		if (l.getDates().size() != 0)
		{
			int i = 0, iDateCount = l.getDates().size();
			for (LectureDate d : l.getDates())
			{
				String strTime;
				if (l.getDuration() > 1)
				{
					strTime = d.getTime().getStartTime() + " - ";
					strTime += DataUtils.get()
							.getTime(d.getTime().getID() + l.getDuration() - 1)
							.getEndTime();
				}
				else
				{
					strTime = d.getTime().getTimeString();
				}
				strTimes.append(String.format("%s (%s)", d.getDay()
						.getShortName(), strTime));
				if (i != iDateCount - 1)
					strTimes.append(", ");
				++i;
			}
		}
		else
			strTimes.append("No Time");
		return strTimes.toString();
	}
}
