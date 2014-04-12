package de.almostintelligent.fhwsplan;

import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import de.almostintelligent.fhwsplan.config.SplanConfig;
import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.data.Employee;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.LectureDate;
import de.almostintelligent.fhwsplan.data.sort.LectureSortingNameAndRoom;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends Activity
{

	HashSet<Integer>	setSelectedLectures	= new HashSet<Integer>();
	Vector<CheckBox>	vecCheckboxes		= new Vector<CheckBox>();

	public void onSelectLecture(View v)
	{
		CheckBox cb = (CheckBox) v;
		if (cb != null)
		{
			Integer iLectureID = (Integer) cb
					.getTag(R.id.settings_cb_lecture_id_tag);
			if (iLectureID != null)
			{
				if (setSelectedLectures.contains(iLectureID))
				{
					setSelectedLectures.remove(iLectureID);
					cb.setChecked(false);
				}
				else
				{
					setSelectedLectures.add(iLectureID);
					cb.setChecked(true);
				}
			}
		}
	}

	public void onClick(View v)
	{
		if (v.getId() == R.id.btnSettingsSave)
		{
			SplanConfig.SaveSelectedIDs(this, setSelectedLectures);
		}

		NavUtils.navigateUpFromSameTask(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		setSelectedLectures = SplanConfig.LoadSelectedIDs(this);

		Vector<LectureSortingNameAndRoom> listLectures = new Vector<LectureSortingNameAndRoom>();

		SparseArray<Lecture> lectures = DataUtils.get().getLectures();
		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);

			// Vector<LectureDate> dates = l.getDates();

			// for (LectureDate d : dates)
			{

				LectureSortingNameAndRoom s = new LectureSortingNameAndRoom();
				s.lecture = l;
				// s.date = d;

				listLectures.add(s);

			}
		}

		Collections.sort(listLectures);

		LinearLayout container = (LinearLayout) findViewById(R.id.SettingsLecturesContainer);

		for (LectureSortingNameAndRoom l : listLectures)
		{

			LinearLayout newItem = (LinearLayout) getLayoutInflater().inflate(
					R.layout.settings_timetableitem, null);

			setTextViewTextByID(R.id.item_settings_lecture_name, newItem,
					l.lecture.getLectureName());
			setTextViewTextByID(R.id.item_settings_lecture_appendix, newItem,
					l.lecture.getLectureAppendix());

			CheckBox cbSelected = (CheckBox) newItem
					.findViewById(R.id.cbSelectLecture);
			if (cbSelected != null)
			{
				cbSelected.setTag(R.id.settings_cb_lecture_id_tag,
						l.lecture.getID());

				if (setSelectedLectures.contains(l.lecture.getID()))
				{

					cbSelected.setChecked(true);
				}

				vecCheckboxes.add(cbSelected);

			}

			{
				StringBuilder strLecturers = new StringBuilder();
				int i = 0, iLecturerCount = l.lecture.getLecturers().size();
				for (Employee e : l.lecture.getLecturers())
				{
					strLecturers.append(e.getToken());
					if (i != iLecturerCount - 1)
						strLecturers.append(", ");
					++i;
				}

				setTextViewTextByID(R.id.item_settings_lecture_lecturer,
						newItem, strLecturers.toString());

			}

			{
				StringBuilder strTimes = new StringBuilder();
				if (l.lecture.getDates().size() != 0)
				{
					int i = 0, iDateCount = l.lecture.getDates().size();
					for (LectureDate d : l.lecture.getDates())
					{
						String strTime;
						if (l.lecture.getDuration() > 1)
						{
							strTime = d.getTime().getStartTime() + " - ";
							strTime += DataUtils
									.get()
									.getTime(
											d.getTime().getID()
													+ l.lecture.getDuration()
													- 1).getEndTime();
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

				setTextViewTextByID(R.id.item_settings_lecture_times, newItem,
						strTimes.toString());
			}

			container.addView(newItem);

		}

		setupActionBar();
	}

	private void setTextViewTextByID(int resID, View parent, String caption)
	{
		if (parent == null)
			return;
		TextView txt = (TextView) parent.findViewById(resID);
		if (txt != null)
		{
			txt.setText(caption);
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar()
	{

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.action_deselect_all_lectures:

				for (CheckBox c : vecCheckboxes)
				{
					c.setChecked(false);
				}

				setSelectedLectures.clear();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
