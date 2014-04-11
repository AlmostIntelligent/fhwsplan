package de.almostintelligent.fhwsplan;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import de.almostintelligent.fhwsplan.config.SplanConfig;
import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.data.Day;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.LectureDate;
import de.almostintelligent.fhwsplan.data.Room;
import de.almostintelligent.fhwsplan.data.sort.LectureSortingDate;
import de.almostintelligent.fhwsplan.filters.TimeTableFilter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{

	private TextView[]			txtDays;
	private HashSet<Integer>	setSelectedLectures;
	private Integer				iSelectedDay	= -1;

	public void onClickDayOfWeek(View v)
	{
		for (TextView txt : txtDays)
		{
			txt.setSelected(false);
		}

		TextView txt = (TextView) v;
		if (txt != null)
		{
			txt.setSelected(true);
		}

		if (v.getId() == R.id.txtMonday)
		{
			buildTimeTable(0);
		}
		else if (v.getId() == R.id.txtTuesday)
		{
			buildTimeTable(1);
		}
		else if (v.getId() == R.id.txtWednesday)
		{
			buildTimeTable(2);
		}
		else if (v.getId() == R.id.txtThursday)
		{
			buildTimeTable(3);
		}
		else if (v.getId() == R.id.txtFriday)
		{
			buildTimeTable(4);
		}
		else
			buildTimeTable(4);
	}

	private void buildTimeTable(int iDay)
	{
		iSelectedDay = iDay;
		TimeTableFilter filter = new TimeTableFilter(DataUtils.get()
				.getLectures());
		// +1 because getDay searches by ID. Monday = 1 and not 0
		Day day = DataUtils.get().getDay(iDay + 1);
		filter.whereDay(day);
		//if (setSelectedLectures.size() != 0)
		{
			filter.whereIDs(setSelectedLectures);
		}

		SparseArray<Lecture> lectures = filter.getLectures();

		LinearLayout timeTableContainer = (LinearLayout) findViewById(R.id.TimeTableContainer);
		if (timeTableContainer == null)
			return;

		timeTableContainer.removeAllViews();

		Vector<LectureSortingDate> listSort = new Vector<LectureSortingDate>();

		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);
			Vector<LectureDate> dates = l.getDates();

			for (LectureDate d : dates)
			{
				if (d.getDay().getID() == day.getID())
				{
					LectureSortingDate s = new LectureSortingDate();
					s.lecture = l;
					s.date = d;

					listSort.add(s);
				}
			}
		}

		Collections.sort(listSort);

		for (LectureSortingDate s : listSort)
		{
			LinearLayout newItem = (LinearLayout) getLayoutInflater().inflate(
					R.layout.timetableitem, null);
			setTextViewTextByID(R.id.item_lecture_name, newItem,
					s.lecture.getLectureName());
			setTextViewTextByID(R.id.item_lecture_appendix, newItem,
					s.lecture.getLectureAppendix());

			String strRooms = new String();
			for (Room r : s.date.getRooms())
			{
				strRooms += r.getShortName() + " ";
			}

			setTextViewTextByID(R.id.item_lecture_room, newItem, strRooms);
			setTextViewTextByID(R.id.item_lecture_time, newItem, s.date
					.getTime().getDescription());

			timeTableContainer.addView(newItem);
		}

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

	private static final String	SELECTED_DAY_RESTORE	= "SELECTED_DAY_RESTORE";

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putInt(SELECTED_DAY_RESTORE, iSelectedDay);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splan);

		if (savedInstanceState != null)
		{
			iSelectedDay = savedInstanceState.getInt(SELECTED_DAY_RESTORE);
		}
		else
		{
			Calendar c = Calendar.getInstance();
			iSelectedDay = c.get(Calendar.DAY_OF_WEEK) - 2;
		}

		DataUtils.get().load(this);

		setSelectedLectures = SplanConfig.LoadSelectedIDs(this);

		txtDays = new TextView[5];

		// -2 because Monday should be the first Day in the Week and in the
		// array.
		txtDays[Calendar.MONDAY - 2] = (TextView) findViewById(R.id.txtMonday);
		txtDays[Calendar.TUESDAY - 2] = (TextView) findViewById(R.id.txtTuesday);
		txtDays[Calendar.WEDNESDAY - 2] = (TextView) findViewById(R.id.txtWednesday);
		txtDays[Calendar.THURSDAY - 2] = (TextView) findViewById(R.id.txtThursday);
		txtDays[Calendar.FRIDAY - 2] = (TextView) findViewById(R.id.txtFriday);

		highlightCurrentDay();

		buildTimeTable(iSelectedDay);

		setupActionBar();

	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar()
	{

		getActionBar().setDisplayHomeAsUpEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		// case android.R.id.home:
		// // This ID represents the Home or Up button. In the case of this
		// // activity, the Up button is shown. Use NavUtils to allow users
		// // to navigate up one level in the application structure. For
		// // more details, see the Navigation pattern on Android Design:
		// //
		// //
		// http://developer.android.com/design/patterns/navigation.html#up-vs-back
		// //
		// NavUtils.navigateUpFromSameTask(this);
		// return true;
			case R.id.action_edit_timetable:
			{
				Intent i = new Intent(this, SettingsActivity.class);
				startActivity(i);
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	private void highlightCurrentDay()
	{
		for (TextView txt : txtDays)
		{
			if (txt != null)
				txt.setSelected(false);
		}

		if (txtDays[iSelectedDay] != null)
		{
			txtDays[iSelectedDay].setSelected(true);
		}
	}

}
