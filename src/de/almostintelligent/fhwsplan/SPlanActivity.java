package de.almostintelligent.fhwsplan;

import de.almostintelligent.fhwsplan.data.DataUtils;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class SPlanActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splan);

		final TextView text = (TextView) findViewById(R.id.hello_world);
		text.setText("loading...");
		final Context c = this;

		Thread t = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				DataUtils.get().load(c);
				text.setText(getString(R.string.hello_world));
			}
		});
		t.run();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splan, menu);
		return true;
	}

}
