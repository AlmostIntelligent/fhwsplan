package de.almostintelligent.fhwsplan.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

import android.content.Context;

import de.almostintelligent.fhwsplan.constants.Constants;
import de.almostintelligent.fhwsplan.data.LectureIDSerializer;

public class SplanConfig
{

	static public void SaveSelectedIDs(Context c, HashSet<Integer> set)
	{
		String ids = LectureIDSerializer.serialize(set);
		try
		{
			FileOutputStream fos = c.openFileOutput(Constants.IDS_FILENAME,
					Context.MODE_PRIVATE);
			fos.write(ids.getBytes());
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static public HashSet<Integer> LoadSelectedIDs(Context c)
	{
		HashSet<Integer> setSelectedLectures = new HashSet<Integer>();

		try
		{
			FileInputStream fis = c.openFileInput(Constants.IDS_FILENAME);
			StringBuilder builder = new StringBuilder();
			int ch;
			try
			{
				while ((ch = fis.read()) != -1)
				{
					builder.append((char) ch);
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setSelectedLectures = LectureIDSerializer.deserialize(builder
					.toString());
		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return setSelectedLectures;
	}
}
