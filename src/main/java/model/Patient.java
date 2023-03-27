package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import enums.*;

public class Patient
{

	private int admissionNum;
	private String patientName;
	private String managedBy;
	private String treatedBy;
	private int age;
	private String phNumber;
	private Gender gender;
	private String addmittedDate;
	private String dischargedDate;
	private double feePaid;
	private String disease;
	private AdmissionStatus admissionStatus;
	
	public Patient(int admissionNum, String patientName, String managedBy,
			String treatedBy, int age, String phNumber, Gender gender, String addmittedDate,
			String dischargedDate, double feePaid, String disease, AdmissionStatus admissionStatus)
	{
 
		this.admissionNum = admissionNum;
		this.patientName = patientName;
		this.managedBy = managedBy;
		this.treatedBy = treatedBy;
		this.age = age;
		this.phNumber = phNumber;
		this.gender = gender;
		this.addmittedDate = addmittedDate;
		this.dischargedDate = dischargedDate;
		this.feePaid = feePaid;
		this.disease = disease;
		this.admissionStatus = admissionStatus;
		
	}
	
	// Getter methods

	public int getAdmissionNum()
	{
		return admissionNum;
	}

	public String getPatientName()
	{
		return patientName;
	}
	
	public String getManagedBy()
	{
		return managedBy;
	}

	public String getTreatedBy()
	{
		if (treatedBy == null)
		{
			return "Yet to treat";
		}
		return treatedBy;
	}

	public int getAge()
	{
		return age;
	}

	public String getPhNumber()
	{
		return phNumber;
	}
	
	public String getAddmittedDate()
	{
		return addmittedDate;
	}
	
	public String getDischargedDate()
	{
		if (dischargedDate == null)
		{
			return "Yet to discharge";
		}
		return dischargedDate;
	}
	
	public String getFeePaid()
	{
		if (feePaid == 0.0)
		{
			return "Yet to pay";
		}
		return String.valueOf(feePaid);
	}

	public String getGender()
	{
		return gender.toString();
	}

	public String getDisease()
	{
		return disease;
	}
	public AdmissionStatus getAdmissionStatus()
	{
		return admissionStatus;
	}
	
	// Setter methods

	public void setAdmissionNum(int admissionNum)
	{
		this.admissionNum = admissionNum;
	}
	
	public void setPatientName(String patientName)
	{
		this.patientName = patientName;
	}

	public void setManagedBy(String managedBy)
	{
		this.managedBy = managedBy;
	}

	public void setTreatedBy(String treatedBy, Admin currentAdmin)
	{
		this.treatedBy = treatedBy;
	}
	
	public void setAge(int age)
	{
		this.age = age;
	}

	public void setPhNumber(String phNumber)
	{
		this.phNumber = phNumber;
	}

	public void setFeePaid(double feePaid, Admin currentAdmin)
	{
		this.feePaid = feePaid;
	}
	
	public void setGenter(Gender gender)
	{
		this.gender = gender;
	}
	
	public void setDisease(String disease)
	{
		this.disease = disease;
	}

	public void setAdmissionStatus(AdmissionStatus admissionStatus)
	{
		this.admissionStatus = admissionStatus;
	}
	
	public void dischargePatient()
	{
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
		this.dischargedDate = LocalDateTime.now().format(dateTimeFormat);
		this.admissionStatus = AdmissionStatus.DISCHARGED;
	}
	
	@Override
	public String toString()
	{
		return "\nAdmission number: " + admissionNum
				+ "\nPatient name: " + patientName
				+ "\nTreated Doctor id: " + getTreatedBy()
				+ "\nAge: " + age
				+ "\nGender: " + gender.toString()
				+ "\nPhone number:" + phNumber
				+ "\nAdmitted time: " + getAddmittedDate()
				+ "\nDischarged time: " + getDischargedDate()
				+ "\nFee paid: " + getFeePaid()
				+ "\nDisease: " + disease 
				+ "\nAdmission status: " + admissionStatus.toString();
	}
	
}
