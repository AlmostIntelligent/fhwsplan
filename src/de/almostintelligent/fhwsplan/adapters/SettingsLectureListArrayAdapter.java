package de.almostintelligent.fhwsplan.adapters;

import java.util.HashSet;

import de.almostintelligent.fhwsplan.R;
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

	public SettingsLectureListArrayAdapter(Context context, int resource,
			Lecture[] objects)
	{
		super(context, resource, objects);
		iResource = resource;
		// TODO Auto-generated constructor stub
	}

	float[]					hsvArray			= new float[] { 0.0f, 1.0f,
			1.0f								};
	HashSet<Integer>		setSelectedLectures	= new HashSet<Integer>();
	SparseArray<Lecture>	listItems;
	int						iResource;

	public void setLectures(SparseArray<Lecture> items)
	{
		listItems = items;
	}

	public void setSelectedLectures(HashSet<Integer> set)
	{
		setSelectedLectures = set;
	}

	public void selectLecture(Integer id)
	{
		setSelectedLectures.add(id);
	}

	public void deselectLecture(Integer id)
	{
		setSelectedLectures.remove(id);
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
			cbSelected.setTag(R.id.settings_cb_lecture_id_tag, l.getID());

			if (setSelectedLectures.contains(l.getID()))
			{
				cbSelected.setChecked(true);
				// cbSelected.callOnClick();
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
