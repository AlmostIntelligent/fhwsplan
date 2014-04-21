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
import de.almostintelligent.fhwsplan.utils.Utils;
import android.graphics.Color;
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
						time.setText(t.getTimeString());
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

		LinearLayout timeTableContainer = (LinearLayout) getView()
				.findViewById(R.id.timetable_container);
		if (timeTableContainer == null)
			return;

		timeTableContainer.removeAllViews();

		Vector<LectureSortingDate> listSort = createLectureSortingList(iDay);

		int[][] iOccupancy = calculateOccupancy(listSort);
		int iMaxOccupancy = calculateMaxOccupancy(listSort);

		float[] hsvArray = new float[] { 0.0f, 1.0f, 1.0f };

		LinearLayout[] colLayouts = new LinearLayout[iMaxOccupancy];
		for (int i = 0; i < iMaxOccupancy; ++i)
		{
			LinearLayout l = new LinearLayout(getView().getContext());

			l.setOrientation(LinearLayout.VERTICAL);

			colLayouts[i] = l;
		}

		for (LectureSortingDate s : listSort)
		{
			int iUnitHeight = (int) getResources().getDimension(
					R.dimen.timetable_item_time_unit);
			int iSpace = (int) getResources().getDimension(
					R.dimen.timetable_item_time_space);

			LinearLayout newItem = (LinearLayout) _inflater.inflate(
					R.layout.timetable_item, null);

			LinearLayout layoutContainer = (LinearLayout) newItem
					.findViewById(R.id.timetable_item_container);

			int iRow = s.date.getTime().getID() - 1;
			int iCol = 0;
			while (iOccupancy[iRow][iCol] != s.lecture.getID()
					&& iCol < iMaxOccupancy)
			{
				++iCol;
			}
			if (iCol >= iMaxOccupancy)
				iCol = iMaxOccupancy - 1;

			if (layoutContainer != null)
			{

				try
				{
					layoutContainer.getLayoutParams().height = (int) iUnitHeight
							* s.lecture.getDuration()
							+ iSpace
							* (s.lecture.getDuration() - 1);
				}
				catch (Exception e)
				{
					// TODO: handle exception
				}

				int iPlanTimeCount = DataUtils.get().getPlanTimeCount();
				int[] iOccuCol = new int[iPlanTimeCount];
				for (int i = 0; i < iPlanTimeCount; ++i)
				{
					iOccuCol[i] = iOccupancy[i][iCol];
				}

				int iMarginTop = calculateSpaceToPrevItem(iOccuCol, s,
						iUnitHeight, iSpace);

				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layoutContainer
						.getLayoutParams();
				if (params != null)
				{
					params.setMargins(params.leftMargin, params.topMargin
							+ iMarginTop, params.rightMargin, params.bottomMargin);
				}
			}

			Utils.setTextViewTextByID(R.id.item_lecture_name, newItem,
					s.lecture.getLectureName());
			Utils.setTextViewTextByID(R.id.item_lecture_appendix, newItem,
					s.lecture.getLectureAppendix());

			View vColor = newItem.findViewById(R.id.timetable_item_color);
			if (vColor != null)
			{

				String hash = s.lecture.getLectureName()
						+ s.lecture.getLectureAppendix();
				int iColorHash = Math.abs(hash.hashCode() % 360);

				hsvArray[0] = (iColorHash * 1.0f);
				int iColor = Color.HSVToColor(hsvArray);

				vColor.setBackgroundColor(iColor);
			}

			String strRooms = new String();
			for (Room r : s.date.getRooms())
			{
				strRooms += r.getShortName() + " ";
			}

			Utils.setTextViewTextByID(R.id.item_lecture_room, newItem, strRooms);

			Utils.setTextViewTextByID(R.id.item_lecture_time, newItem,
					constructTimeString(s));

			colLayouts[iCol].addView(newItem);

			// timeTableContainer.addView(newItem);
		}

		for (LinearLayout l : colLayouts)
		{
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (p != null)
			{
				p.weight = 1.0f;
				l.setLayoutParams(p);
			}

			timeTableContainer.addView(l);
		}

	}

	private Vector<LectureSortingDate> createLectureSortingList(int iDay)
	{
		// iSelectedDay = iDay;
		TimeTableFilter filter = new TimeTableFilter(DataUtils.get()
				.getLectures());
		SparseArray<Lecture> lectures = filter.getLectures();
		// +1 because getDay searches by ID. Monday = 1 and not 0
		Day day = DataUtils.get().getDay(iDay + 1);
		filter.whereDay(day);
		// if (setSelectedLectures.size() != 0)
		{
			filter.whereIDs(setSelectedLectures);
		}

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
		return listSort;
	}

	private String constructTimeString(LectureSortingDate s)
	{
		String strTime;
		if (s.lecture.getDuration() > 1)
		{
			strTime = s.date.getTime().getStartTime() + " - ";
			strTime += DataUtils
					.get()
					.getTime(
							s.date.getTime().getID() + s.lecture.getDuration()
									- 1).getEndTime();
		}
		else
		{
			strTime = s.date.getTime().getTimeString();
		}
		return strTime;
	}

	private int calculateMaxOccupancy(Vector<LectureSortingDate> listSort)
	{
		int iPlanTimeCount = DataUtils.get().getPlanTimeCount();
		int[] iOccupancy = new int[iPlanTimeCount];

		for (LectureSortingDate s : listSort)
		{
			int iStart = s.date.getTime().getID();
			int iEnd = iStart + s.lecture.getDuration();
			for (int i = iStart; i < iEnd; ++i)
			{
				iOccupancy[i - 1] += 1;
			}
		}

		return Utils.maxInArray(iOccupancy);
	}

	private int calculateSpaceToPrevItem(int[] iOccupancy,
			LectureSortingDate s, int iUnitHeight, int iSpace)
	{
		int iMarginTop = 0;
		int iTime = s.date.getTime().getID() - 1;
		while (iTime > 0)
		{
			if (iOccupancy[iTime - 1] == 0)
			{
				iMarginTop += iUnitHeight + iSpace;
				--iTime;
			}
			else
				break;

		}
		return iMarginTop;
	}

	private int[][] calculateOccupancy(Vector<LectureSortingDate> listSort)
	{
		int iPlanTimeCount = DataUtils.get().getPlanTimeCount();

		int iMax = calculateMaxOccupancy(listSort);
		int[][] iLectureGrid = new int[iPlanTimeCount][iMax];
		for (LectureSortingDate s : listSort)
		{
			int iRow = s.date.getTime().getID() - 1;
			int iCol = 0;
			while (iLectureGrid[iRow][iCol] != 0)
			{
				iCol++;
			}
			int iStart = s.date.getTime().getID() - 1;
			int iEnd = iStart + (s.lecture.getDuration());
			for (int i = iStart; i < iEnd; ++i)
			{
				iLectureGrid[i][iCol] = s.lecture.getID();
			}
		}

		// StringBuilder b = new StringBuilder();
		// b.append("start\n");
		// for (int[] cols : iLectureGrid)
		// {
		// for (int id : cols)
		// {
		// b.append(String.valueOf(id) + " ");
		// }
		// b.append("\n");
		// }
		// b.append("ende\n");
		// Log.e("timetable", b.toString());
		return iLectureGrid;
	}
}
