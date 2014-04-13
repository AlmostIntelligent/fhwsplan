package de.almostintelligent.fhwsplan.adapters;

import de.almostintelligent.fhwsplan.fragments.TimeTableDayFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TimeTableDaysFragmentPagerAdapter extends FragmentPagerAdapter
{

	private String[]	dayStrings;

	public TimeTableDaysFragmentPagerAdapter(FragmentManager fm)
	{
		super(fm);

	}

	public void setDayString(String[] days)
	{
		dayStrings = days;
	}

	@Override
	public Fragment getItem(int arg0)
	{
		TimeTableDayFragment fragment = new TimeTableDayFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("day", arg0);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public int getCount()
	{
		// Monday to Friday
		return 5;
	}

	@Override
	public CharSequence getPageTitle(int iPos)
	{
		if (dayStrings == null || iPos >= dayStrings.length)
			return "na";
		return dayStrings[iPos];
	}
}
