package model;

import java.util.Comparator;

public class DocSortByNameDescend implements Comparator<Doctor>
{
	
	@Override
	public int compare(Doctor o1, Doctor o2)
	{
		return -1 * (o1.getDoctorName().compareTo(o2.getDoctorName()));
	}
	
}