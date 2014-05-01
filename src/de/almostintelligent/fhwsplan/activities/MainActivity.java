package de.almostintelligent.fhwsplan.activities;

import java.util.Calendar;

import com.example.android.common.view.SlidingTabLayout;

import de.almostintelligent.fhwsplan.R;
import de.almostintelligent.fhwsplan.adapters.TimeTableDaysFragmentPagerAdapter;
import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.utils.PlanManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity
{
	private Integer						iSelectedDay			= -1;
	private ViewPager					vpDayPager;
	TimeTableDaysFragmentPagerAdapter	fpaDays;

	private static final String			SELECTED_DAY_RESTORE	= "SELECTED_DAY_RESTORE";

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

		setupActionBar();

		setContentView(R.layout.activity_splan);

		PlanManager pm = new PlanManager();
		if (pm.load(this))
		{
			if (!pm.save(this))
			{
				Log.e("MainActivity", "Save failed");
			}
		}

		if (savedInstanceState != null)
		{
			iSelectedDay = savedInstanceState.getInt(SELECTED_DAY_RESTORE);
		}
		else
		{
			Calendar c = Calendar.getInstance();
			// iSelectedDay = c.get(Calendar.DAY_OF_WEEK) - 2;
			switch (c.get(Calendar.DAY_OF_WEEK))
			{
				case Calendar.SUNDAY:
					iSelectedDay = 0;
					break;
				case Calendar.SATURDAY:
					iSelectedDay = 0;
					break;
				default:
					iSelectedDay = c.get(Calendar.DAY_OF_WEEK) - 2;
			}
		}

		vpDayPager = (ViewPager) findViewById(R.id.timetable_days_pager);
		if (vpDayPager != null)
		{
			fpaDays = new TimeTableDaysFragmentPagerAdapter(
					getSupportFragmentManager());

			String[] dayStrings = new String[5];
			dayStrings[0] = getResources()
					.getString(R.string.week_day_mon_long);
			dayStrings[1] = getResources()
					.getString(R.string.week_day_tue_long);
			dayStrings[2] = getResources()
					.getString(R.string.week_day_wed_long);
			dayStrings[3] = getResources()
					.getString(R.string.week_day_thu_long);
			dayStrings[4] = getResources()
					.getString(R.string.week_day_fri_long);

			fpaDays.setDayString(dayStrings);

			vpDayPager.setAdapter(fpaDays);

			SlidingTabLayout stl = (SlidingTabLayout) findViewById(R.id.timetable_pager_title_strip);
			if (stl != null)
			{
				stl.setViewPager(vpDayPager);
			}

			vpDayPager.setCurrentItem(iSelectedDay);
		}

		DataUtils.get().load(this);

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
}
