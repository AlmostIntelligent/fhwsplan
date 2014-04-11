package de.almostintelligent.fhwsplan;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.data.Day;
import de.almostintelligent.fhwsplan.data.Faculty;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.LectureDate;
import de.almostintelligent.fhwsplan.data.sort.LectureSorting;
import de.almostintelligent.fhwsplan.filters.TimeTableFilter;
import de.almostintelligent.fhwsplan.timetable.TimeTable;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{

	private TextView[]	txtDays;

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
		TimeTableFilter filter = new TimeTableFilter(DataUtils.get()
				.getLectures());
		// +1 because getDay searches by ID. Monday = 1 and not 0
		Day day = DataUtils.get().getDay(iDay + 1);
		filter.whereDay(day);
		// HashSet<Integer> ids = new HashSet<Integer>();
		// ids.add(79);
		// ids.add(156);
		// ids.add(18);
		// ids.add(141);
		// ids.add(142);
		// ids.add(143);
		// ids.add(145);
		// ids.add(75);
		// ids.add(86);
		// ids.add(87);
		// filter.whereIDs(ids);

		SparseArray<Lecture> lectures = filter.getLectures();

		LinearLayout timeTableContainer = (LinearLayout) findViewById(R.id.TimeTableContainer);
		if (timeTableContainer == null)
			return;

		timeTableContainer.removeAllViews();

		Vector<LectureSorting> listSort = new Vector<LectureSorting>();

		for (int i = 0; i < lectures.size(); ++i)
		{
			Integer iKey = lectures.keyAt(i);
			Lecture l = lectures.get(iKey);
			Vector<LectureDate> dates = l.getDates();

			for (LectureDate d : dates)
			{
				if (d.getDay().getID() == day.getID())
				{
					LectureSorting s = new LectureSorting();
					s.lecture = l;
					s.date = d;

					listSort.add(s);
				}
			}
		}

		Collections.sort(listSort);

		for (LectureSorting s : listSort)
		{
			LinearLayout newItem = (LinearLayout) getLayoutInflater().inflate(
					R.layout.timetableitem, null);
			setTextViewTextByID(R.id.item_lecture_name, newItem,
					s.lecture.getLectureName());
			setTextViewTextByID(R.id.item_lecture_appendix, newItem,
					s.lecture.getLectureAppendix());
			setTextViewTextByID(R.id.item_lecture_room, newItem, s.date
					.getRooms().firstElement().getShortName());
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splan);

		Calendar c = Calendar.getInstance();
		int iDay = c.get(Calendar.DAY_OF_WEEK) - 2;

		txtDays = new TextView[5];

		// -2 because Monday should be the first Day in the Week and in the
		// array.
		txtDays[Calendar.MONDAY - 2] = (TextView) findViewById(R.id.txtMonday);
		txtDays[Calendar.TUESDAY - 2] = (TextView) findViewById(R.id.txtTuesday);
		txtDays[Calendar.WEDNESDAY - 2] = (TextView) findViewById(R.id.txtWednesday);
		txtDays[Calendar.THURSDAY - 2] = (TextView) findViewById(R.id.txtThursday);
		txtDays[Calendar.FRIDAY - 2] = (TextView) findViewById(R.id.txtFriday);

		highlightCurrentDay();

		DataUtils.get().load(this);

		// TimeTableFilter filter = new TimeTableFilter(DataUtils.get()
		// .getLectures());
		//
		// Faculty f = DataUtils.get().getFaculty(13);
		// filter.whereFacultyAndSemester(f, 1);
		// Day d = DataUtils.get().getDay(1);
		// filter.whereDay(d);

		buildTimeTable(iDay);

		// TimeTable table = TimeTable.createFromFilter(filter);
		// table.print();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.splan, menu);
		return true;
	}

	private void highlightCurrentDay()
	{
		Calendar c = Calendar.getInstance();
		int iDay = c.get(Calendar.DAY_OF_WEEK) - 2;

		for (TextView txt : txtDays)
		{
			if (txt != null)
				txt.setSelected(false);
		}

		if (txtDays[iDay] != null)
		{
			txtDays[iDay].setSelected(true);
		}
	}

}
