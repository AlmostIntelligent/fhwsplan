package de.almostintelligent.fhwsplan.utils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

public class PlanManager
{
	private Map<String, Set<Integer>>	MapNameToLectures	= new HashMap<String, Set<Integer>>();
	private String						strCurrentPlan;

	private static final String			ELEMENT_ROOT		= "plans";
	private static final String			ELEMENT_PLAN		= "plan";
	private static final String			ELEMENT_LECTURE		= "lecture";
	private static final String			ATTRIBUTE_CURRENT	= "current";
	private static final String			ATTRIBUTE_NAME		= "name";

	public PlanManager()
	{
	}

	public String getCurrentPlanName()
	{
		return strCurrentPlan;
	}

	public Set<Integer> getCurrentPlan()
	{
		return getPlan(getCurrentPlanName());
	}

	public boolean hasPlan(String strName)
	{
		return MapNameToLectures.containsKey(strName);
	}

	public Set<Integer> getPlan(String strName)
	{
		if (!hasPlan(strName))
			return null;

		return MapNameToLectures.get(strName);
	}

	public void addPlan(String strName, Set<Integer> lectures)
	{
		if (hasPlan(strName))
			return;

		MapNameToLectures.put(strName, lectures);
	}

	public void updatePlan(String strName, Set<Integer> lectures)
	{
		MapNameToLectures.remove(strName);
		addPlan(strName, lectures);
	}

	public boolean load(Context c)
	{
		Document d = Utils.openXml(c, "plans_db.xml");
		if (d == null)
		{
			Log.e("PlanManager.load", "Could not load plans_db.xml");
			return false;
		}

		NodeList nodes = d.getElementsByTagName(ELEMENT_PLAN);
		if (nodes != null)
		{
			if (d.getAttributes() != null)
			{
				Node attrCurrent = d.getAttributes().getNamedItem(
						ATTRIBUTE_CURRENT);
				if (attrCurrent != null)
				{
					strCurrentPlan = attrCurrent.getNodeValue();
				}
			}

			for (int i = 0; i < nodes.getLength(); ++i)
			{
				Element e = (Element) nodes.item(i);
				if (e != null)
					loadPlan(e);
			}
		}
		return true;
	}

	private void loadPlan(Element e)
	{
		String strName = new String();
		Node attrName = e.getAttributeNode(ATTRIBUTE_NAME);
		if (attrName != null)
			strName = attrName.getNodeValue();

		Log.e("PlanManager.loadPlan", "Name: " + strName);

		NodeList lectures = e.getElementsByTagName(ELEMENT_LECTURE);
		// Log.e("PlanManager.loadPlan.nodes",
		// String.valueOf(lectures.getLength()));
		Set<Integer> lectureIDs = new HashSet<Integer>();
		for (int i = 0; i < lectures.getLength(); ++i)
		{

			Node lecture = lectures.item(i);
			if (lecture != null)
			{
				try
				{
					String strVal = lecture.getTextContent();
					Integer id = Integer.valueOf(strVal);
					lectureIDs.add(id);
					// Log.e("PlanManager.loadPlan", "  ID: " +
					// String.valueOf(id));
				}
				catch (NumberFormatException ex)
				{
					ex.printStackTrace();
				}
			}
		}

		if (strName.equalsIgnoreCase(""))
		{
			Log.e("PlanManager.loadPlan", "Error! No name for plan");
		}

		MapNameToLectures.put(strName, lectureIDs);
	}

	public boolean save(Context c)
	{
		Log.e("PlanManager.save", "Saving");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try
		{
			DocumentBuilder builder = dbf.newDocumentBuilder();

			Document doc = builder.newDocument();
			Element root = doc.createElement(ELEMENT_ROOT);
			if (strCurrentPlan != null)
				root.setAttribute(ATTRIBUTE_CURRENT, strCurrentPlan);

			for (Map.Entry<String, Set<Integer>> entry : MapNameToLectures
					.entrySet())
			{
				Element plan = doc.createElement(ELEMENT_PLAN);
				plan.setAttribute(ATTRIBUTE_NAME, entry.getKey());
				for (Integer id : entry.getValue())
				{
					Element lecutre = doc.createElement(ELEMENT_LECTURE);
					String strID = String.valueOf(id);
					if (strID != null)
						lecutre.setTextContent(String.valueOf(id));
					plan.appendChild(lecutre);
				}

				root.appendChild(plan);
			}

			doc.appendChild(root);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transf = tf.newTransformer();
			DOMSource src = new DOMSource(doc);
			// StringWriter strWriter = new StringWriter();
			// StreamResult result = new StreamResult(strWriter);
			StreamResult fileResult = new StreamResult(new File(
					c.getFilesDir(), "plans_db.xml"));

			transf.transform(src, fileResult);
			// transf.transform(src, result);
			//
			// String res = strWriter.toString();
			// Log.e("PlanManager.save", "Content: " + res);

			return true;
		}
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TransformerConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
