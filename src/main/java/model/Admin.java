package model;

import java.util.List;

import enums.AdminStatus;

public class Admin
{
	private String adminUserId;
	private String userPassword;
	private String adminUserName;
	private String phNumber;
	private String adminHospitalName;
	private AdminStatus admissionStatus;
	private List<Doctor> doctorsToManage;
	private List<Patient> patientsToManage;

	public Admin(String adminUserId, String adminUserName, String phNumber, String userPassword, String adminHospitalName, AdminStatus admissionStatus, List<Doctor> doctorsToManage, List<Patient> patientsToManage)
	{
		this.adminUserId = adminUserId;
		this.userPassword = userPassword;
		this.adminUserName = adminUserName;
		this.phNumber = phNumber;
		this.adminHospitalName = adminHospitalName;
		this.admissionStatus = admissionStatus;
		this.doctorsToManage = doctorsToManage;
		this.patientsToManage = patientsToManage;
	}

	// Getter methods

	public String getUserPassword()
	{
		return userPassword;
	}

	public String getAdminUserId()
	{
		return adminUserId;
	}

	public String getAdminUserName()
	{
		return adminUserName;
	}
	
	public String getPhNumber()
	{
		return phNumber;
	}

	public String getAdminHospitalName()
	{
		return adminHospitalName;
	}
	
	public AdminStatus getAdmissionStatus()
	{
		return admissionStatus;
	}

	public List<Doctor> getDoctorsToManage()
	{
		return doctorsToManage;
	}

	public List<Patient> getPatientsToManage()
	{
		return patientsToManage;
	}
	
	// Setter methods

	public void setAdminUserName(String adminUserName)
	{
		this.adminUserName = adminUserName;
	}

	public void setPhNumber(String phNumber)
	{
		this.phNumber = phNumber;
	}

	public void setUserPassword(String userPassword)
	{
		this.userPassword = userPassword;
	}

	public void setAdminHospitalName(String adminHospitalName)
	{
		this.adminHospitalName = adminHospitalName;
	}

	public void setAdmissionStatus(AdminStatus admissionStatus)
	{
		this.admissionStatus = admissionStatus;
	}

	public void setDoctorsToManage(List<Doctor> doctorsToManage)
	{
		this.doctorsToManage = doctorsToManage;
	}

	public void setPatientsToManage(List<Patient> patientsToManage)
	{
		this.patientsToManage = patientsToManage;
	}
	
	@Override
	public String toString()
	{
		return "\nAdmin's user Id: " + adminUserId
				+ "\nAdmin's password: " + "*".repeat(userPassword.length())
				+ "\nAdmin's user name: " + adminUserName
				+ "\nAdmin's phone number: " + phNumber
				+ "\nAdmin's hospital name: " + adminHospitalName;
	}
}
