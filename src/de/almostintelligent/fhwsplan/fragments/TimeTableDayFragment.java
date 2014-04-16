package de.almostintelligent.fhwsplan.fragments;

import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import de.almostintelligent.fhwsplan.R;
import de.almostintelligent.fhwsplan.config.SplanConfig;
import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.data.Day;
import de.almostintelligent.fhwsplan.data.Lecture;
import de.almostintelligent.fhwsplan.data.LectureDate;
import de.almostintelligent.fhwsplan.data.PlanTime;
import de.almostintelligent.fhwsplan.data.Room;
import de.almostintelligent.fhwsplan.data.filters.TimeTableFilter;
import de.almostintelligent.fhwsplan.data.sort.LectureSortingDate;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeTableDayFragment extends android.support.v4.app.Fragment
{

	private LayoutInflater		_inflater;
	private HashSet<Integer>	setSelectedLectures;
	private int					iDay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstaceState)
	{
		setSelectedLectures = SplanConfig.LoadSelectedIDs(getActivity());
		_inflater = inflater;

		iDay = getArguments().getInt("day");

		View view = inflater.inflate(R.layout.fragment_timetable_day,
				container, false);

		LinearLayout times = (LinearLayout) view
				.findViewById(R.id.timetable_times_container);
		if (times != null)
		{
			Vector<PlanTime> plantimes = DataUtils.get().getPlanTimes();
			for (PlanTime t : plantimes)
			{
				LinearLayout timeLayout = (LinearLayout) inflater.inflate(
						R.layout.timetable_time, null, false);
				if (timeLayout != null)
				{
					TextView time = (TextView) timeLayout
							.findViewById(R.id.timetable_time_text);

					if (time != null)
					{
						time.setText(t.getStartTime() + "\n" + t.getEndTime());
					}

					times.addView(timeLayout);
				}
			}

		}

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		buildTimeTable(iDay);
	}

	public void buildTimeTable(int iDay)
	{
		// iSelectedDay = iDay;
		TimeTableFilter filter = new TimeTableFilter(DataUtils.get()
				.getLectures());
		// +1 because getDay searches by ID. Monday = 1 and not 0
		Day day = DataUtils.get().getDay(iDay + 1);
		filter.whereDay(day);
		// if (setSelectedLectures.size() != 0)
		{
			filter.whereIDs(setSelectedLectures);
		}

		SparseArray<Lecture> lectures = filter.getLectures();

		LinearLayout timeTableContainer = (LinearLayout) getView()
				.findViewById(R.id.timetable_container);
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
			LinearLayout newItem = (LinearLayout) _inflater.inflate(
					R.layout.timetable_item, null);
			LinearLayout layoutContainer = (LinearLayout) newItem
					.findViewById(R.id.timetable_item_container);
			if (layoutContainer != null)
			{
				try
				{
					layoutContainer.getLayoutParams().height = (int) getResources()
							.getDimension(R.dimen.timetable_item_time_unit)
							* s.lecture.getDuration();
				}
				catch (Exception e)
				{
					// TODO: handle exception
				}

			}
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
			String strTime;
			if (s.lecture.getDuration() > 1)
			{
				strTime = s.date.getTime().getStartTime() + " - ";
				strTime += DataUtils
						.get()
						.getTime(
								s.date.getTime().getID()
										+ s.lecture.getDuration() - 1)
						.getEndTime();
			}
			else
			{
				strTime = s.date.getTime().getTimeString();
			}
			setTextViewTextByID(R.id.item_lecture_time, newItem, strTime);

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
}
