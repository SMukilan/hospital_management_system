package model;

import java.util.Comparator;

public class PatientSortByAdmissionNoAscend implements Comparator<Patient>
{

	@Override
	public int compare(Patient o1, Patient o2)
	{
		return o1.getAdmissionNum() - o2.getAdmissionNum();
	}

}
