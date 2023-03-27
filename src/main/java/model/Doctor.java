package model;

import enums.DoctorStatus;

public class Doctor
{

	private String docId;
	private String doctorName;
	private String managedBy;
	private String phNumber;
	private String specialist;
	private String timing;
	private String qualification;
	private DoctorStatus doctorStatus;
	
	// Constructor
	
	public Doctor (String docId, String doctorName, String managedBy, String phNumber, String specialist, String timing, String qualification, DoctorStatus doctorStatus)
	{
		
		this.docId = docId;
		this.doctorName = doctorName;
		this.managedBy = managedBy;
		this.phNumber = phNumber;
		this.specialist = specialist;
		this.timing = timing;
		this.qualification = qualification;
		this.doctorStatus = doctorStatus;
		
	}
	
	// Getter methods

	public String getDocId()
	{
		return docId;
	}
	
	public String getDoctorName()
	{
		return doctorName;
	}
	
	public String getManagedBy()
	{
		return managedBy;
	}

	public String getPhNumber()
	{
		return phNumber;
	}

	public String getSpecialist()
	{
		return specialist;
	}

	public String getQualification()
	{
		return qualification;
	}

	public String getTiming()
	{
		return timing;
	}
	
	public DoctorStatus getDoctorStatus()
	{
		return doctorStatus;
	}
	
	// Setter methods
	
	public void setDoctorName(String doctorName)
	{
		this.doctorName = doctorName;
	}

	public void setManagedBy(String managedBy)
	{
		this.managedBy = managedBy;
	}

	public void setPhNumber(String phNumber)
	{
		this.phNumber = phNumber;
	}
	
	public void setSpecialist(String specialist)
	{
		this.specialist = specialist;
	}
	
	public void setTiming(String timing)
	{
		this.timing = timing;
	}
	
	public void setQualification(String qualification)
	{
		this.qualification = qualification;
	}
	
	public void setDoctorStatus(DoctorStatus doctorStatus)
	{
		this.doctorStatus = doctorStatus;
	}
	
	@Override
	public String toString()
	{
		return "\nDoctor Id: " + docId
				+ "\nDoctor name: " + doctorName
				+ "\nPhone number: " + phNumber
				+ "\nSpecialist: " + getSpecialist()
				+ "\nTiming: " + timing
				+ "\nQualification: " + qualification;
	}
	
}
