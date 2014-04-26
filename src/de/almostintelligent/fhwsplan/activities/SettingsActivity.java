package de.almostintelligent.fhwsplan.activities;

import java.util.Collections;
import java.util.Vector;

import de.almostintelligent.fhwsplan.R;
import de.almostintelligent.fhwsplan.adapters.SettingsLectureListArrayAdapter;
import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.data.Employee;
import de.almostintelligent.fhwsplan.data.Faculty;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.filters.TimeTableFilter;
import de.almostintelligent.fhwsplan.data.sort.LectureSortingNameAndRoom;
import de.almostintelligent.fhwsplan.fragments.SettingsLectureFilterFrament;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends FragmentActivity implements
		SettingsLectureFilterFrament.OnFilterDismissListener
{

	// HashSet<Integer> setSelectedLectures = new HashSet<Integer>();
	// SparseArray<CheckBox> listCheckboxes = new SparseArray<CheckBox>();
	SettingsLectureListArrayAdapter	adapter;

	Employee						filterEmployee;
	Faculty							filterFaculty;
	Integer							filterSemester	= Integer.valueOf(0);

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
				if (adapter != null)
				{
					if (adapter.isSelected(iLectureID))
					{
						adapter.deselectLecture(iLectureID);
						cb.setChecked(false);
					}
					else
					{

						adapter.selectLecture(iLectureID);
						cb.setChecked(true);
					}

				}
			}
		}
	}

	public void onClick(View v)
	{
		if (v.getId() == R.id.btnSettingsSave)
		{
			if (adapter != null)
				adapter.saveSelectedLectures();
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

			{

				LectureSortingNameAndRoom s = new LectureSortingNameAndRoom();
				s.lecture = l;
				// s.date = d;

				listLectures.add(s);

			}
		}

		Collections.sort(listLectures);

		ListView listView = (ListView) findViewById(R.id.settings_listview);
		if (listView != null)
		{
			Lecture[] arrayLectures = new Lecture[listLectures.size()];
			int i = 0;
			for (LectureSortingNameAndRoom l : listLectures)
			{
				arrayLectures[i] = l.lecture;
				++i;
			}

			adapter = new SettingsLectureListArrayAdapter(this,
					R.layout.settings_timetableitem, arrayLectures, adapter);

			adapter.setLectures(lectures);

			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		buildView();

		setupActionBar();
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
				if (adapter != null)
					adapter.clearSelection();
				return true;
			case R.id.settings_select_all_lectures:
				if (adapter != null)
					adapter.selectAllLectures();
				break;
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
		if (iRes == R.id.settings_filter_btnShow)
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
