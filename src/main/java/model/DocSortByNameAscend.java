package model;

import java.util.Comparator;

public class DocSortByNameAscend implements Comparator<Doctor>
{

	@Override
	public int compare(Doctor o1, Doctor o2)
	{
		return o1.getDoctorName().compareTo(o2.getDoctorName());
	}

}