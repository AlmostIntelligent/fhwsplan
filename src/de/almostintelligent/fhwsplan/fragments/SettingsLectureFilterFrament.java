package de.almostintelligent.fhwsplan.fragments;

import de.almostintelligent.fhwsplan.R;
import de.almostintelligent.fhwsplan.adapters.SettingsFilterEmployeeArrayAdapter;
import de.almostintelligent.fhwsplan.adapters.SettingsFilterFacultyArrayAdapter;
import de.almostintelligent.fhwsplan.data.DataUtils;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SettingsLectureFilterFrament extends DialogFragment implements
		View.OnClickListener
{

	public Spinner	facultySpinner;
	public Spinner	employeeSpinner;
	public Spinner	semesterSpinner;

	public interface OnFilterDismissListener
	{
		public void onFilterDismiss(int iRes, SettingsLectureFilterFrament frag);
	}

	private OnFilterDismissListener	filterDismissListener;

	public void setOnFilterDismissListener(OnFilterDismissListener l)
	{
		filterDismissListener = l;
	}

	@Override
	public void onClick(View v)
	{
		if (filterDismissListener != null)
		{
			filterDismissListener.onFilterDismiss(v.getId(), this);
		}
		this.dismiss();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		getDialog().setTitle(R.string.settings_filter);

		View view = inflater.inflate(R.layout.fragment_settings_filter_dialog,
				container);

		semesterSpinner = (Spinner) view
				.findViewById(R.id.settings_filter_fragment_spinnerSemester);
		if (semesterSpinner != null)
		{
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(getActivity(),
							R.array.settings_semester_list,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			semesterSpinner.setAdapter(adapter);
		}

		employeeSpinner = (Spinner) view
				.findViewById(R.id.settings_filter_fragment_spinnerEmployee);
		if (employeeSpinner != null)
		{
			SettingsFilterEmployeeArrayAdapter adapter = new SettingsFilterEmployeeArrayAdapter(
					getActivity(),
					R.layout.simple_spinner_dropdown_item_multiline, DataUtils
							.get().getEmployees());

			adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_multiline);
			employeeSpinner.setAdapter(adapter);
		}

		facultySpinner = (Spinner) view
				.findViewById(R.id.settings_filter_fragment_spinnerFaculty);
		if (facultySpinner != null)
		{
			SettingsFilterFacultyArrayAdapter adapter = new SettingsFilterFacultyArrayAdapter(
					getActivity(),
					R.layout.simple_spinner_dropdown_item_multiline, DataUtils
							.get().getFaculties());

			adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_multiline);
			facultySpinner.setAdapter(adapter);
		}

		Button btnSave = (Button) view
				.findViewById(R.id.settings_filter_btnSave);
		if (btnSave != null)
		{
			btnSave.setOnClickListener(this);
		}

		Button btnCancel = (Button) view
				.findViewById(R.id.settings_filter_btnCancel);
		if (btnCancel != null)
		{
			btnCancel.setOnClickListener(this);
		}

		return view;
	}
}
