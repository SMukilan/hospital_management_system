package model;

import java.util.Comparator;

public class PatientSortByNameDescend implements Comparator<Patient>
{

	@Override
	public int compare(Patient o1, Patient o2)
	{
		return -1 * (o1.getPatientName().compareTo(o2.getPatientName()));
	}

}
