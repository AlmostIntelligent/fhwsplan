package de.almostintelligent.fhwsplan;

import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import de.almostintelligent.fhwsplan.config.SplanConfig;
import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.data.Employee;
import de.almostintelligent.fhwsplan.data.Faculty;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.LectureDate;
import de.almostintelligent.fhwsplan.data.sort.LectureSortingNameAndRoom;
import de.almostintelligent.fhwsplan.filters.TimeTableFilter;
import de.almostintelligent.fhwsplan.fragments.SettingsLectureFilterFrament;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends FragmentActivity implements
		SettingsLectureFilterFrament.OnFilterDismissListener
{

	HashSet<Integer>	setSelectedLectures	= new HashSet<Integer>();
	Vector<CheckBox>	vecCheckboxes		= new Vector<CheckBox>();

	Employee			filterEmployee;
	Faculty				filterFaculty;
	Integer				filterSemester		= Integer.valueOf(0);

	public void onFilterButtonClick(View view)
	{
		NavUtils.navigateUpFromSameTask(this);
	}

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

	private void buildView()
	{
		Vector<LectureSortingNameAndRoom> listLectures = new Vector<LectureSortingNameAndRoom>();

		SparseArray<Lecture> lectures = DataUtils.get().getLectures();

		if (filterEmployee != null || filterFaculty != null
				|| filterSemester != 0)
		{
			TimeTableFilter filter = new TimeTableFilter(lectures);
			if (filterEmployee != null)
				filter.whereEmployee(filterEmployee);

			if (filterFaculty != null && filterSemester > 0)
			{
				filter.whereFacultyAndSemester(filterFaculty, filterSemester);
			}
			else if (filterFaculty != null && filterSemester <= 0)
			{
				filter.whereFaculty(filterFaculty);
			}
			else if (filterFaculty == null && filterSemester > 0)
			{
				filter.whereSemester(filterSemester);
			}

			lectures = filter.getLectures();
		}

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
		container.removeAllViews();

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
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		setSelectedLectures = SplanConfig.LoadSelectedIDs(this);

		buildView();

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
			case R.id.settings_action_deselect_all_lectures:

				for (CheckBox c : vecCheckboxes)
				{
					c.setChecked(false);
				}

				setSelectedLectures.clear();
				return true;
			case R.id.settings_action_show_filter:
			{
				FragmentManager fm = getSupportFragmentManager();
				SettingsLectureFilterFrament fragment = new SettingsLectureFilterFrament();
				fragment.show(fm, "fragment_edit_filter");
				fragment.setOnFilterDismissListener(this);
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onFilterDismiss(int iRes, SettingsLectureFilterFrament fragment)
	{
		if (iRes == R.id.settings_filter_btnSave)
		{
			filterEmployee = (Employee) fragment.employeeSpinner
					.getSelectedItem();
			filterFaculty = (Faculty) fragment.facultySpinner.getSelectedItem();

			String strSemester = (String) fragment.semesterSpinner
					.getSelectedItem();
			try
			{
				filterSemester = Integer.valueOf(strSemester);
			}
			catch (Exception e)
			{
				filterSemester = 0;
			}

			if (filterEmployee != null && filterEmployee.getID() == -1)
				filterEmployee = null;
			if (filterFaculty != null && filterFaculty.getID() == -1)
				filterFaculty = null;

			buildView();
		}

	}
}
