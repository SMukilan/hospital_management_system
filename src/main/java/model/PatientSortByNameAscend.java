package model;

import java.util.Comparator;

public class PatientSortByNameAscend implements Comparator<Patient>
{

	@Override
	public int compare(Patient o1, Patient o2)
	{
		return o1.getPatientName().compareTo(o2.getPatientName());
	}

}
