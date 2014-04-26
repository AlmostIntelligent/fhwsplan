package de.almostintelligent.fhwsplan.adapters;

import java.util.HashSet;

import de.almostintelligent.fhwsplan.R;
import de.almostintelligent.fhwsplan.config.SplanConfig;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.utils.Utils;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class SettingsLectureListArrayAdapter extends ArrayAdapter<Lecture>
{

	float[]					hsvArray			= new float[] { 0.0f, 1.0f,
			1.0f								};
	HashSet<Integer>		setSelectedLectures	= new HashSet<Integer>();
	SparseArray<CheckBox>	listCheckboxes		= new SparseArray<CheckBox>();
	SparseArray<Lecture>	listItems;
	int						iResource;

	public SettingsLectureListArrayAdapter(Context context, int resource,
			Lecture[] objects, SettingsLectureListArrayAdapter old)
	{
		super(context, resource, objects);
		iResource = resource;
		if (old == null)
		{
			setSelectedLectures = SplanConfig.LoadSelectedIDs(context);
		}
		else
			setSelectedLectures = old.setSelectedLectures;
		// TODO Auto-generated constructor stub
	}

	public void saveSelectedLectures()
	{
		SplanConfig.SaveSelectedIDs(getContext(), setSelectedLectures);
	}

	public void setLectures(SparseArray<Lecture> items)
	{
		listItems = items;
	}

	public void selectAllLectures()
	{
		// setSelectedLectures.clear();
		for (int i = 0; i < listItems.size(); ++i)
		{
			// Integer index = listItems.indexOfKey(i);
			Lecture l = listItems.valueAt(i);
			setSelectedLectures.add(l.getID());

		}
		for (int i = 0; i < listCheckboxes.size(); ++i)
		{
			CheckBox cb = listCheckboxes.valueAt(i);
			if (cb != null)
				cb.setChecked(true);
		}
	}

	public void setSelectedLectures(HashSet<Integer> set)
	{
		setSelectedLectures = set;
	}

	public boolean isSelected(Integer id)
	{
		return setSelectedLectures.contains(id);
	}

	public void selectLecture(Integer id)
	{
		setSelectedLectures.add(id);
	}

	public void deselectLecture(Integer id)
	{
		setSelectedLectures.remove(id);
	}

	public void clearSelection()
	{
		for (int i = 0; i < listCheckboxes.size(); ++i)
		{
			Integer iKey = listCheckboxes.keyAt(i);
			listCheckboxes.get(iKey).setChecked(false);
		}

		setSelectedLectures.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Lecture l = getItem(position);

		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(iResource,
					null);
		}

		Utils.setTextViewTextByID(R.id.item_settings_lecture_name, convertView,
				l.getLectureName());
		Utils.setTextViewTextByID(R.id.item_settings_lecture_appendix,
				convertView, l.getLectureAppendix());

		View vColor = convertView.findViewById(R.id.timetable_item_color);
		if (vColor != null)
		{

			String hash = l.getLectureName() + l.getLectureAppendix();
			int iColorHash = Math.abs(hash.hashCode() % 360);

			hsvArray[0] = (iColorHash * 1.0f);
			int iColor = Color.HSVToColor(hsvArray);

			vColor.setBackgroundColor(iColor);
		}

		CheckBox cbSelected = (CheckBox) convertView
				.findViewById(R.id.cbSelectLecture);
		if (cbSelected != null)
		{
			Integer iPrevLectureID = (Integer) cbSelected
					.getTag(R.id.settings_cb_lecture_id_tag);
			if (iPrevLectureID != null)
				listCheckboxes.remove(iPrevLectureID);
			cbSelected.setTag(R.id.settings_cb_lecture_id_tag, l.getID());
			listCheckboxes.put(l.getID(), cbSelected);

			if (setSelectedLectures.contains(l.getID()))
			{
				cbSelected.setChecked(true);
			}
			else
				cbSelected.setChecked(false);

		}

		{
			String strLecturers = Utils.generateEmployeeListString(l);

			Utils.setTextViewTextByID(R.id.item_settings_lecture_lecturer,
					convertView, strLecturers);

		}

		{
			String strTimes = Utils.generateTimeString(l);

			Utils.setTextViewTextByID(R.id.item_settings_lecture_times,
					convertView, strTimes);
		}

		return convertView;
	}

}
