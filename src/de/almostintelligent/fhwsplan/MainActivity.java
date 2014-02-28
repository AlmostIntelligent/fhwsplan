package de.almostintelligent.fhwsplan;

import de.almostintelligent.fhwsplan.data.DataUtils;
import de.almostintelligent.fhwsplan.fragments.TimeTableDayFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends android.support.v4.app.FragmentActivity
{
	private String[]		mPlanetTitles;
	private DrawerLayout	mDrawerLayout;
	private ListView		mDrawerList;

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id)
		{
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position)
	{
		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment = new TimeTableDayFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splan);

		DataUtils.get().load(this);

		mPlanetTitles = new String[] {
				getResources().getString(R.string.action_timetable_day),
				getResources().getString(R.string.action_settings) };

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		if (mDrawerLayout == null)
		{
			Log.e("drawer_layout", "Could not create drawer_layout");
		}

		// Set the adapter for the list view
		if (mDrawerList != null)
		{
			mDrawerList.setAdapter(new ArrayAdapter<String>(this,
					R.layout.drawer_list_item, mPlanetTitles));

			// Set the list's click listener
			mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		}
		else
		{
			Log.e("drawer_menu", "Could not create left_drawer");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.splan, menu);
		return true;
	}

}
