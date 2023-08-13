package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
import java.math.*;
import java.text.*;
public class EmployeeManager implements EmployeeManagerInterface
{
	private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
	private Map<String,EmployeeInterface> panNumberWiseEmployeesMap;
	private Map<String,EmployeeInterface> aadharCardNumberWiseEmployeesMap;	
	private Set<EmployeeInterface> employeesSet;
	private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeesMap;
	private static EmployeeManager employeeManager=null;
	
	private EmployeeManager() throws BLException
	{
		populateDataStructures();
	}
	
	private void populateDataStructures() throws BLException
	{
		this.employeeIdWiseEmployeesMap=new HashMap<>();
		this.panNumberWiseEmployeesMap=new HashMap<>();
		this.aadharCardNumberWiseEmployeesMap=new HashMap<>();
		this.designationCodeWiseEmployeesMap=new HashMap<>();
		this.employeesSet=new TreeSet<>(); 
		try
		{
			Set<EmployeeDTOInterface> dlEmployees;
			dlEmployees=new EmployeeDAO().getAll();
			EmployeeInterface employee;
			DesignationManagerInterface designationManager;
			designationManager=DesignationManager.getDesignationManager();
			DesignationInterface designation;
			Set<EmployeeInterface> ets;
			for(EmployeeDTOInterface dlEmployee:dlEmployees)
			{
				employee=new Employee();
				employee.setEmployeeId(dlEmployee.getEmployeeId());
				employee.setName(dlEmployee.getName());
				designation=designationManager.getDesignationByCode(dlEmployee.getDesignationCode());
				employee.setDesignation(designation);
				employee.setDateOfBirth(dlEmployee.getDateOfBirth());
				if(dlEmployee.getGender()=='M')
				{				
				employee.setGender(GENDER.MALE);
				}
				else
				{
				employee.setGender(GENDER.FEMALE);
				}
				employee.setIsIndian(dlEmployee.getIsIndian());
				employee.setBasicSalary(dlEmployee.getBasicSalary());
				employee.setPANNumber(dlEmployee.getPANNumber());
				employee.setAadharCardNumber(dlEmployee.getAadharCardNumber());
				this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),employee);
				this.panNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(),employee);
				this.aadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(),employee);
				this.employeesSet.add(employee);
				ets=this.designationCodeWiseEmployeesMap.get(designation.getCode());
				if(ets==null)
				{
					ets=new TreeSet<>();
					ets.add(employee);
					this.designationCodeWiseEmployeesMap.put(designation.getCode(),ets);
				}
				else
				{
					ets.add(employee);
				}
			}
		}catch(DAOException daoException)
		{
			BLException blException=new BLException();
			blException.setGenericException(daoException.getMessage());
			throw blException;
		}
	}
	
	public static EmployeeManagerInterface getEmployeeManager() throws BLException
	{
		if(employeeManager==null) employeeManager=new EmployeeManager();
		return employeeManager;
	}
	
	public void addEmployee(EmployeeInterface employee) throws BLException
	{
		BLException blException;
		blException=new BLException();
		String employeeId=employee.getEmployeeId();
		String name=employee.getName();
		DesignationInterface designation=employee.getDesignation();
		int designationCode=0;
		Date dateOfBirth=employee.getDateOfBirth();
		char gender=employee.getGender();
		boolean isIndian=employee.getIsIndian();
		BigDecimal basicSalary=employee.getBasicSalary();
		String panNumber=employee.getPANNumber();
		String aadharCardNumber=employee.getAadharCardNumber();
		if(employeeId!=null)
		{
			employeeId=employeeId.trim();
			if(employeeId.length()>0)
			{
				blException.addException("employeeId","employee Id should be nil/empty");
			}
		}
		if(name==null)
		{
			blException.addException("name","Name required");
		}
		else
		{
			name=name.trim();
			if(name.length()==0) blException.addException("name","Name required");
		}
		DesignationManagerInterface designationManager;
		designationManager=DesignationManager.getDesignationManager();
		if(designation==null)
		{
			blException.addException("designation","Designation required");
		}
		else
		{
			designationCode=designation.getCode();
			if(designationManager.designationCodeExists(designationCode)==false) 	blException.addException("designation","Invalid designation");
		}
		if(dateOfBirth==null) 	blException.addException("dateOfBirth","date of birth required");
		if(gender==' ') 	blException.addException("gender","gender required");
		if(basicSalary==null)
		{
			blException.addException("basicSalary","basic salary required");
		}
		else
		{
			if(basicSalary.signum()==-1) blException.addException("basicSalary","basic salary can't be negative");
		}
		if(panNumber==null)
		{
			blException.addException("panNumber","PAN Number required");
		}
		else
		{
			panNumber=panNumber.trim();
			if(panNumber.length()==0) blException.addException("panNumber","PAN Number required");
		}
		if(aadharCardNumber==null)
		{
			blException.addException("aadharCardNumber","aadhar Card Number required");
		}
		else
		{
			aadharCardNumber=aadharCardNumber.trim();
			if(aadharCardNumber.length()==0) blException.addException("aadharCardNumber","aadhar Card Number required");
		}
		if(panNumber!=null && panNumber.trim().length()>0)
		{
			if(this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase())) blException.addException("panNumber","PAN Number "+panNumber+" exists");
		}
		if(aadharCardNumber!=null && aadharCardNumber.trim().length()>0)
		{
			if(this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase())) blException.addException("aadharCardNumber","aadhar Card Number "+aadharCardNumber+" exists");
		}
		if(blException.hasExceptions()) throw blException;
		try
		{
			EmployeeDTOInterface dlEmployee=new EmployeeDTO();
			dlEmployee.setName(name);
			dlEmployee.setDesignationCode(designationCode);
			dlEmployee.setDateOfBirth(dateOfBirth);
			dlEmployee.setGender((gender=='M')? GENDER.MALE:GENDER.FEMALE);
			dlEmployee.setIsIndian(isIndian);
			dlEmployee.setBasicSalary(basicSalary);
			dlEmployee.setPANNumber(panNumber);
			dlEmployee.setAadharCardNumber(aadharCardNumber);
			new EmployeeDAO().add(dlEmployee);
			employee.setEmployeeId(dlEmployee.getEmployeeId());
			
			//update DS
			EmployeeInterface dsEmployee=new Employee();
			dsEmployee.setEmployeeId(employee.getEmployeeId());
			dsEmployee.setName(name);
			dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
			dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
			dsEmployee.setGender((gender=='M')? GENDER.MALE:GENDER.FEMALE);
			dsEmployee.setIsIndian(isIndian);
			dsEmployee.setBasicSalary(basicSalary);
			dsEmployee.setPANNumber(panNumber);
			dsEmployee.setAadharCardNumber(aadharCardNumber);
			this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
			this.panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
			this.aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
			this.employeesSet.add(dsEmployee);
			Set<EmployeeInterface> ets;
			ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
			if(ets==null)
			{
				ets=new TreeSet<>();
				ets.add(employee);
				this.designationCodeWiseEmployeesMap.put(designation.getCode(),ets);
			}
			else
			{
				ets.add(employee);
			}
		}catch(DAOException daoException)
		{

			blException.setGenericException(daoException.getMessage());
			throw blException;
		}
	}
	public void updateEmployee(EmployeeInterface employee) throws BLException
	{
		BLException blException;
		blException=new BLException();
		String employeeId=employee.getEmployeeId();
		String name=employee.getName();
		DesignationInterface designation=employee.getDesignation();
		int designationCode=0;
		Date dateOfBirth=employee.getDateOfBirth();
		char gender=employee.getGender();
		boolean isIndian=employee.getIsIndian();
		BigDecimal basicSalary=employee.getBasicSalary();
		String panNumber=employee.getPANNumber();
		String aadharCardNumber=employee.getAadharCardNumber();
		if(employeeId==null)
		{
			blException.addException("employeeId","employee Id required");
		}
		else
		{			
			employeeId=employeeId.trim();
			if(employeeId.length()==0)
			{
				blException.addException("employeeId","employee Id required");
			}
			else
			{
				if(!this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase()))
				{
					blException.addException("employeeId","Invalid employee Id "+employeeId);
					throw blException;
				}
			}
		}
		if(name==null)
		{
			blException.addException("name","Name required");
		}
		else
		{
			name=name.trim();
			if(name.length()==0) blException.addException("name","Name required");
		}
		DesignationManagerInterface designationManager;
		designationManager=DesignationManager.getDesignationManager();
		if(designation==null)
		{
			blException.addException("designation","Designation required");
		}
		else
		{
			designationCode=designation.getCode();
			if(designationManager.designationCodeExists(designationCode)==false) 	blException.addException("designation","Invalid designation");
		}
		if(dateOfBirth==null) 	blException.addException("dateOfBirth","date of birth required");
		if(gender==' ') 	blException.addException("gender","gender required");
		if(basicSalary==null)
		{
			blException.addException("basicSalary","basic salary required");
		}
		else
		{
			if(basicSalary.signum()==-1) blException.addException("basicSalary","basic salary can't be negative");
		}
		if(panNumber==null)
		{
			blException.addException("panNumber","PAN Number required");
		}
		else
		{
			panNumber=panNumber.trim();
			if(panNumber.length()==0) blException.addException("panNumber","PAN Number required");
		}
		if(aadharCardNumber==null)
		{
			blException.addException("aadharCardNumber","aadhar Card Number required");
		}
		else
		{
			aadharCardNumber=aadharCardNumber.trim();
			if(aadharCardNumber.length()==0) blException.addException("aadharCardNumber","aadhar Card Number required");
		}
		if(panNumber!=null && panNumber.trim().length()>0)
		{
			EmployeeInterface e=this.panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
			if(e!=null && e.getEmployeeId().equalsIgnoreCase(employeeId)==false) blException.addException("panNumber","PAN Number "+panNumber+" exists");
		}
		if(aadharCardNumber!=null && aadharCardNumber.trim().length()>0)
		{
			EmployeeInterface e=this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
			if(e!=null && e.getEmployeeId().equalsIgnoreCase(employeeId)==false) blException.addException("aadharCardNumber","aadhar Card Number "+aadharCardNumber+" exists");
		}
		if(blException.hasExceptions()) throw blException;
		try
		{
			EmployeeInterface dsEmployee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
			String oldPANNumber=dsEmployee.getPANNumber();
			String oldAadharCardNumber=dsEmployee.getAadharCardNumber();
			int oldDesignationCode=dsEmployee.getDesignation().getCode();
			EmployeeDTOInterface dlEmployee=new EmployeeDTO();
			dlEmployee.setEmployeeId(dsEmployee.getEmployeeId());
			dlEmployee.setName(name);
			dlEmployee.setDesignationCode(designationCode);
			dlEmployee.setDateOfBirth(dateOfBirth);
			dlEmployee.setGender((gender=='M')? GENDER.MALE:GENDER.FEMALE);
			dlEmployee.setIsIndian(isIndian);
			dlEmployee.setBasicSalary(basicSalary);
			dlEmployee.setPANNumber(panNumber);
			dlEmployee.setAadharCardNumber(aadharCardNumber);
			new EmployeeDAO().update(dlEmployee);			
			//update DS
			dsEmployee.setName(name);
			dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
			dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
			dsEmployee.setGender((gender=='M')? GENDER.MALE:GENDER.FEMALE);
			dsEmployee.setIsIndian(isIndian);
			dsEmployee.setBasicSalary(basicSalary);
			dsEmployee.setPANNumber(panNumber);
			dsEmployee.setAadharCardNumber(aadharCardNumber);
			//remove old data
			this.employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
			this.aadharCardNumberWiseEmployeesMap.remove(aadharCardNumber.toUpperCase());
			this.panNumberWiseEmployeesMap.remove(panNumber.toUpperCase());
			this.employeesSet.remove(dsEmployee);
			//update new data
			this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
			this.panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
			this.aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
			this.employeesSet.add(dsEmployee);
			if(oldDesignationCode!=dsEmployee.getDesignation().getCode())
			{
				Set<EmployeeInterface> ets;
				ets=this.designationCodeWiseEmployeesMap.get(oldDesignationCode);
				ets.remove(dsEmployee);
				ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
				if(ets==null)
				{
					ets=new TreeSet<>();
					ets.add(employee);
					this.designationCodeWiseEmployeesMap.put(designation.getCode(),ets);
				}
				else
				{
					ets.add(employee);
				}
			}
		}catch(DAOException daoException)
		{

			blException.setGenericException(daoException.getMessage());
			throw blException;
		}
	}
	public void removeEmployee(String employeeId) throws BLException
	{
		if(employeeId==null)
		{
			BLException blException;
			blException=new BLException();
			blException.addException("employeeId","employee Id required");
			throw blException;
		}
		else
		{			
			employeeId=employeeId.trim();
			if(employeeId.length()==0)
			{
				BLException blException;
				blException=new BLException();
				blException.addException("employeeId","employee Id required");
				throw blException;
			}
			else
			{
				if(!this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase()))
				{
					BLException blException;
					blException=new BLException();
					blException.addException("employeeId","Invalid employee Id "+employeeId);
					throw blException;
				}
			}
		}
		try
		{
			EmployeeInterface dsEmployee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
			new EmployeeDAO().delete(dsEmployee.getEmployeeId());
			//remove old data
			this.employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
			this.aadharCardNumberWiseEmployeesMap.remove(dsEmployee.getAadharCardNumber().toUpperCase());
			this.panNumberWiseEmployeesMap.remove(dsEmployee.getPANNumber().toUpperCase());
			this.employeesSet.remove(dsEmployee);
			Set<EmployeeInterface> ets;
			ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
			ets.remove(dsEmployee); 
		}catch(DAOException daoException)
		{
			BLException blException;
			blException=new BLException();
			blException.setGenericException(daoException.getMessage());
			throw blException;
		}
	}
	public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException
	{
		BLException blException;
		blException=new BLException();
		EmployeeInterface dsEmployee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
		if(dsEmployee==null)
		{
			blException.addException("employeeId","Invalid Employee Id: "+employeeId);
			throw blException;
		}
		EmployeeInterface employee=new Employee();
		employee.setEmployeeId(dsEmployee.getEmployeeId());
		employee.setName(dsEmployee.getName());
		DesignationInterface designation=new Designation();
		designation.setCode(dsEmployee.getDesignation().getCode());
		designation.setTitle(dsEmployee.getDesignation().getTitle());
		employee.setDesignation(designation);
		employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
		employee.setGender((dsEmployee.getGender()=='M')? GENDER.MALE:GENDER.FEMALE);
		employee.setIsIndian(dsEmployee.getIsIndian());
		employee.setBasicSalary(dsEmployee.getBasicSalary());
		employee.setPANNumber(dsEmployee.getPANNumber());
		employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
		return employee;
	}
	public EmployeeInterface getEmployeeByPANNumber(String panNumber) throws BLException
	{
		BLException blException;
		blException=new BLException();
		EmployeeInterface dsEmployee=this.panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
		if(dsEmployee==null)
		{
			blException.addException("panNumber","Invalid PAN Number: "+panNumber);
			throw blException;
		}
		EmployeeInterface employee=new Employee();
		employee.setEmployeeId(dsEmployee.getEmployeeId());
		employee.setName(dsEmployee.getName());
		DesignationInterface designation=new Designation();
		designation.setCode(dsEmployee.getDesignation().getCode());
		designation.setTitle(dsEmployee.getDesignation().getTitle());
		employee.setDesignation(designation);
		employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
		employee.setGender((dsEmployee.getGender()=='M')? GENDER.MALE:GENDER.FEMALE);
		employee.setIsIndian(dsEmployee.getIsIndian());
		employee.setBasicSalary(dsEmployee.getBasicSalary());
		employee.setPANNumber(dsEmployee.getPANNumber());
		employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
		return employee;
	}
	public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException
	{
		BLException blException;
		blException=new BLException();
		EmployeeInterface dsEmployee=this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
		if(dsEmployee==null)
		{
			blException.addException("aadharCardNumber","Invalid aadhar Card Number: "+aadharCardNumber);
			throw blException;
		}
		EmployeeInterface employee=new Employee();
		employee.setEmployeeId(dsEmployee.getEmployeeId());
		employee.setName(dsEmployee.getName());
		DesignationInterface designation=new Designation();
		designation.setCode(dsEmployee.getDesignation().getCode());
		designation.setTitle(dsEmployee.getDesignation().getTitle());
		employee.setDesignation(designation);
		employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
		employee.setGender((dsEmployee.getGender()=='M')? GENDER.MALE:GENDER.FEMALE);
		employee.setIsIndian(dsEmployee.getIsIndian());
		employee.setBasicSalary(dsEmployee.getBasicSalary());
		employee.setPANNumber(dsEmployee.getPANNumber());
		employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
		return employee;
	}
	public int getEmployeeCount()
	{
		return this.employeesSet.size();
	}
	public boolean employeeIdExists(String employeeId)
	{
		return this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase());
	}
	public boolean employeePANNumberExists(String panNumber) 
	{
		return this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase());
	}
	public boolean employeeAadharCardNumberExists(String aadharCardNumber) 
	{
		return this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase());
	}
	public Set<EmployeeInterface> getEmployees()
	{
		Set<EmployeeInterface> employees=new TreeSet<>();
		EmployeeInterface employee;
		DesignationInterface designation;
		for(EmployeeInterface dsEmployee:employeesSet)
		{
			employee=new Employee();
			employee.setEmployeeId(dsEmployee.getEmployeeId());
			employee.setName(dsEmployee.getName());
			designation=new Designation();
			designation.setCode(dsEmployee.getDesignation().getCode());
			designation.setTitle(dsEmployee.getDesignation().getTitle());
			employee.setDesignation(designation);
			employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
			employee.setGender((dsEmployee.getGender()=='M')? GENDER.MALE:GENDER.FEMALE);
			employee.setIsIndian(dsEmployee.getIsIndian());
			employee.setBasicSalary(dsEmployee.getBasicSalary());
			employee.setPANNumber(dsEmployee.getPANNumber());
			employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
			employees.add(employee);
		}
		return employees;
	}
	public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException
	{
		DesignationManagerInterface designationManager;
		designationManager=DesignationManager.getDesignationManager();
		if(!designationManager.designationCodeExists(designationCode))
		{
			BLException blException;
			blException=new BLException();
			blException.setGenericException("Invalid designation code: "+designationCode);
			throw blException;
		}		
		Set<EmployeeInterface> employees=new TreeSet<>();
		Set<EmployeeInterface> ets=this.designationCodeWiseEmployeesMap.get(designationCode);
		if(ets==null)
		{
			return employees;
		}
		EmployeeInterface employee;
		DesignationInterface designation;
		for(EmployeeInterface dsEmployee:ets)
		{
			employee=new Employee();
			employee.setEmployeeId(dsEmployee.getEmployeeId());
			employee.setName(dsEmployee.getName());
			designation=new Designation();
			designation.setCode(dsEmployee.getDesignation().getCode());
			designation.setTitle(dsEmployee.getDesignation().getTitle());
			employee.setDesignation(designation);
			employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
			employee.setGender((dsEmployee.getGender()=='M')? GENDER.MALE:GENDER.FEMALE);
			employee.setIsIndian(dsEmployee.getIsIndian());
			employee.setBasicSalary(dsEmployee.getBasicSalary());
			employee.setPANNumber(dsEmployee.getPANNumber());
			employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
			employees.add(employee);
		}
		return employees;
	
	}
	public int getEmployeeCountByDesignationCode(int designationCode) throws BLException
	{
		Set<EmployeeInterface> ets;
		ets=this.designationCodeWiseEmployeesMap.get(designationCode);
		if(ets==null) return 0;
		return ets.size();
	}
	public boolean designationAlloted(int designationCode) throws BLException
	{
		return this.designationCodeWiseEmployeesMap.containsKey(designationCode);
	}
}