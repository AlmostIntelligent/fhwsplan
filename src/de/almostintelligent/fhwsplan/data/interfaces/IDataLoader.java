package de.almostintelligent.fhwsplan.data.interfaces;

import de.almostintelligent.fhwsplan.data.DataUtils;
import android.content.Context;

public interface IDataLoader
{
	public boolean loadData(Context context, DataUtils utils);
}
