package model;

import java.util.Comparator;

public class PatientSortByAdmissionNoDescend implements Comparator<Patient>
{

	@Override
	public int compare(Patient o1, Patient o2)
	{
		return o2.getAdmissionNum() - o1.getAdmissionNum();
	}

}
