package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.io.*;
import java.math.*;
import java.text.*;
import java.sql.*;
public class EmployeeDAO implements EmployeeDAOInterface
{
	private final static String FILE_NAME="employee.data";
	public void add(EmployeeDTOInterface employeeDTO) throws DAOException
	{
		if(employeeDTO==null) throw new DAOException("Employee is null");
		String name=employeeDTO.getName();
		if(name==null) throw new DAOException("Name is null");
		name=name.trim();
		if(name.length()==0) throw new DAOException("Length of name is zero");
		int designationCode=employeeDTO.getDesignationCode();
		if(designationCode<=0) throw new DAOException("Invalid Designation Code"+designationCode);
		
		Connection connection=null;
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		try
		{
			connection=DAOConnection.getConnection();
			preparedStatement=connection.prepareStatement("select code from designation where code=?");
			preparedStatement.setInt(1, designationCode);
			resultSet=preparedStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid Designation Code: "+designationCode);
			}
			resultSet.close();
			preparedStatement.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		
		
		java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
		if(dateOfBirth==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Date of Birth is null");
		}
		char gender=employeeDTO.getGender();
		if(gender==' ')
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Gender not set to M/F");
		} 
		boolean isIndian=employeeDTO.getIsIndian();
		BigDecimal basicSalary=employeeDTO.getBasicSalary();
		if(basicSalary==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Basic Salary is null");
		}
		if(basicSalary.signum()==-1)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Basic Salary is negative");		
		} 
		String panNumber=employeeDTO.getPANNumber();
		if(panNumber==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("PanNumber is null");	
		} 
		panNumber=panNumber.trim();
		if(panNumber.length()==0)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Length of Pan Number is zero");
		} 
		String aadharCardNumber=employeeDTO.getAadharCardNumber();
		if(aadharCardNumber==null) 
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Aadhar Card Number is null");
		}
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Length of Aadhar Card Number is zero");
		} 
		try
		{
			boolean panNumberExists;
			preparedStatement=connection.prepareStatement("select gender from employee where pan_number=?");
			preparedStatement.setString(1, panNumber);
			resultSet=preparedStatement.executeQuery();
			panNumberExists=resultSet.next();
			resultSet.close();
			preparedStatement.close();
			boolean aadharCardNumberExists;
			preparedStatement=connection.prepareStatement("select gender from employee where aadhar_card_number=?");
			preparedStatement.setString(1, aadharCardNumber);
			resultSet=preparedStatement.executeQuery();
			aadharCardNumberExists=resultSet.next();
			preparedStatement.close();
			resultSet.close();
			if(panNumberExists && aadharCardNumberExists)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("PAN number("+panNumber+") exists and Aadhar Card Number ("+aadharCardNumber+") exists");
			}
			if(panNumberExists)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("PAN number ("+panNumber+") exists");
			}
			if(aadharCardNumberExists)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("Aadhar card number ("+aadharCardNumber+") exists");
			}
			preparedStatement=connection.prepareStatement("insert into employee (name,designation_code,date_of_birth,basic_salary,gender,is_indian,pan_number,aadhar_card_number) values(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, designationCode);
			java.sql.Date sqlDateOfBirth=new java.sql.Date(dateOfBirth.getYear(), dateOfBirth.getMonth(), dateOfBirth.getDate());
			preparedStatement.setDate(3, sqlDateOfBirth);
			preparedStatement.setBigDecimal(4, basicSalary);
			preparedStatement.setString(5, String.valueOf(gender));
			preparedStatement.setBoolean(6, isIndian);
			preparedStatement.setString(7, panNumber);
			preparedStatement.setString(8, aadharCardNumber);
			preparedStatement.executeUpdate();
			resultSet=preparedStatement.getGeneratedKeys();
			resultSet.next();
			int generatedEmployeeId=resultSet.getInt(1);
			resultSet.close();
			preparedStatement.close();
			connection.close();
			employeeDTO.setEmployeeId("A"+(10000000+generatedEmployeeId));
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public void update(EmployeeDTOInterface employeeDTO) throws DAOException
	{
		if(employeeDTO==null) throw new DAOException("Employee is null");
		String employeeId=employeeDTO.getEmployeeId();
		if(employeeId==null) throw new DAOException("employee Id is null");
		employeeId=employeeId.trim();
		if(employeeId.length()==0) throw new DAOException("Length of employee Id is zero");
		int actualEmployeeId=0;
		try
		{
			actualEmployeeId=Integer.parseInt(employeeId.substring(1))-10000000;
		}catch(Exception exception)
		{
			throw new DAOException("Invalid employee Id"+employeeId);
		}
		String name=employeeDTO.getName();
		if(name==null) throw new DAOException("Name is null");
		name=name.trim();
		if(name.length()==0) throw new DAOException("Length of name is zero");		
		int designationCode=employeeDTO.getDesignationCode();
		if(designationCode<=0) throw new DAOException("Invalid Designation Code"+designationCode);
		Connection connection=null;
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		try
		{
			connection=DAOConnection.getConnection();
			preparedStatement=connection.prepareStatement("select code from designation where code=?");
			preparedStatement.setInt(1, designationCode);
			resultSet=preparedStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid Designation Code: "+designationCode);
			}
			resultSet.close();
			preparedStatement.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}


		java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
		if(dateOfBirth==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Date of Birth is null");
		}
		char gender=employeeDTO.getGender();
		if(gender==' ')
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Gender not set to M/F");
		} 
		boolean isIndian=employeeDTO.getIsIndian();
		BigDecimal basicSalary=employeeDTO.getBasicSalary();
		if(basicSalary==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Basic Salary is null");
		}
		if(basicSalary.signum()==-1)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Basic Salary is negative");		
		} 
		String panNumber=employeeDTO.getPANNumber();
		if(panNumber==null)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("PanNumber is null");	
		} 
		panNumber=panNumber.trim();
		if(panNumber.length()==0)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Length of Pan Number is zero");
		} 
		String aadharCardNumber=employeeDTO.getAadharCardNumber();
		if(aadharCardNumber==null) 
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Aadhar Card Number is null");
		}
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0)
		{
			try
			{
				connection.close();
			}catch(SQLException sqlException)
			{
				throw new DAOException(sqlException.getMessage());
			}
			throw new DAOException("Length of Aadhar Card Number is zero");
		}
		try
		{
			boolean panNumberExists;
			preparedStatement=connection.prepareStatement("select gender from employee where pan_number=? and employee_id<>?");
			preparedStatement.setString(1, panNumber);
			preparedStatement.setInt(2, actualEmployeeId);
			resultSet=preparedStatement.executeQuery();
			panNumberExists=resultSet.next();
			resultSet.close();
			preparedStatement.close();
			boolean aadharCardNumberExists;
			preparedStatement=connection.prepareStatement("select gender from employee where aadhar_card_number=? and employee_id<>?");
			preparedStatement.setString(1, aadharCardNumber);
			preparedStatement.setInt(2, actualEmployeeId);
			resultSet=preparedStatement.executeQuery();
			aadharCardNumberExists=resultSet.next();
			preparedStatement.close();
			resultSet.close();
			if(panNumberExists && aadharCardNumberExists)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("PAN number("+panNumber+") exists and Aadhar Card Number ("+aadharCardNumber+") exists");
			}
			if(panNumberExists)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("PAN number ("+panNumber+") exists");
			}
			if(aadharCardNumberExists)
			{
				try
				{
					connection.close();
				}catch(SQLException sqlException)
				{
					throw new DAOException(sqlException.getMessage());
				}
				throw new DAOException("Aadhar card number ("+aadharCardNumber+") exists");
			}
			preparedStatement=connection.prepareStatement("update employee set name=?,designation_code=?,date_of_birth=?,basic_salary=?,gender=?,is_indian=?,pan_number=?,aadhar_Card_number=? where employee_id=?");
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, designationCode);
			java.sql.Date sqlDateOfBirth=new java.sql.Date(dateOfBirth.getYear(), dateOfBirth.getMonth(), dateOfBirth.getDate());
			preparedStatement.setDate(3, sqlDateOfBirth);
			preparedStatement.setBigDecimal(4, basicSalary);
			preparedStatement.setString(5, String.valueOf(gender));
			preparedStatement.setBoolean(6, isIndian);
			preparedStatement.setString(7, panNumber);
			preparedStatement.setString(8, aadharCardNumber);
			preparedStatement.setInt(9, actualEmployeeId);
			preparedStatement.executeUpdate();
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public void delete(String employeeId) throws DAOException
	{
		if(employeeId==null) throw new DAOException("employee Id is null");
		employeeId=employeeId.trim();
		if(employeeId.length()==0) throw new DAOException("Length of employee Id is zero");
		int actualEmployeeId=0;
		try
		{
			actualEmployeeId=Integer.parseInt(employeeId.substring(1))-10000000;
		}catch(Exception exception)
		{
			throw new DAOException("Invalid employee Id"+employeeId);
		}
		Connection connection=null;
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		try
		{
			connection=DAOConnection.getConnection();
			preparedStatement=connection.prepareStatement("select gender from employee where employee_id=?");
			preparedStatement.setInt(1, actualEmployeeId);
			resultSet=preparedStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid employee id: "+employeeId);
			}
			resultSet.close();
			preparedStatement.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		try
		{
			preparedStatement=connection.prepareStatement("delete from employee where employee_id=?");
			preparedStatement.setInt(1, actualEmployeeId);
			preparedStatement.executeUpdate();
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
	}
	public Set<EmployeeDTOInterface> getAll() throws DAOException
	{
		Set<EmployeeDTOInterface> employees=new TreeSet<>();
		try
		{
			Connection connection=DAOConnection.getConnection();
			Statement statement;
			statement=connection.createStatement();
			ResultSet resultSet;
			resultSet=statement.executeQuery("select * from employee");
			EmployeeDTOInterface employeeDTO;
			java.sql.Date sqlDateOfBirth;
			java.util.Date dateOfBirth;
			String gender;
			while(resultSet.next())
			{
				employeeDTO=new EmployeeDTO();
				employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
				employeeDTO.setName(resultSet.getString("name").trim());
				employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
				sqlDateOfBirth=resultSet.getDate("date_of_birth");
				dateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
				employeeDTO.setDateOfBirth(dateOfBirth);
				employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
				gender=resultSet.getString("gender");
				if(gender.equals("M"))
				{
					employeeDTO.setGender(GENDER.MALE);
				}
				if(gender.equals("F"))
				{
					employeeDTO.setGender(GENDER.FEMALE);
				}
				employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
				employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
				employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number").trim());
				employees.add(employeeDTO);
			}
			resultSet.close();
			statement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		//System.out.println("End of getAll");
		return employees;		
	}
	public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
	{
		Set<EmployeeDTOInterface> employees=new TreeSet<>();
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement=connection.prepareStatement("select code from designation where code=?");
			preparedStatement.setInt(1, designationCode);
			ResultSet resultSet;
			resultSet=preparedStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid code: "+designationCode);
			}
			resultSet.close();
			preparedStatement.close();
			preparedStatement=connection.prepareStatement("select * from employee where designation_code=?");
			preparedStatement.setInt(1, designationCode);
			resultSet=preparedStatement.executeQuery();
			EmployeeDTOInterface employeeDTO;
			java.sql.Date sqlDateOfBirth;
			java.util.Date dateOfBirth;
			String gender;
			while(resultSet.next())
			{
				employeeDTO=new EmployeeDTO();
				employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
				employeeDTO.setName(resultSet.getString("name").trim());
				employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
				sqlDateOfBirth=resultSet.getDate("date_of_birth");
				dateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
				employeeDTO.setDateOfBirth(dateOfBirth);
				employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
				gender=resultSet.getString("gender");
				if(gender.equals("M"))
				{
					employeeDTO.setGender(GENDER.MALE);
				}
				if(gender.equals("F"))
				{
					employeeDTO.setGender(GENDER.FEMALE);
				}
				employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
				employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
				employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number").trim());
				employees.add(employeeDTO);
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return employees;
	}
	public boolean isDesignationAlloted(int designationCode) throws DAOException
	{
		boolean designationAlloted=false;
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement=connection.prepareStatement("select code from designation where code=?");
			preparedStatement.setInt(1, designationCode);
			ResultSet resultSet;
			resultSet=preparedStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid code: "+designationCode);
			}
			resultSet.close();
			preparedStatement.close();
			preparedStatement=connection.prepareStatement("select gender from employee where designation_code=?");
			preparedStatement.setInt(1, designationCode);
			resultSet=preparedStatement.executeQuery();
			designationAlloted=resultSet.next();
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return designationAlloted;
	}
	public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
	{
		EmployeeDTOInterface employeeDTO=null;
		if(employeeId==null) throw new DAOException("Invalid employee Id : id is null");
		employeeId=employeeId.trim();
		if(employeeId.length()==0) throw new DAOException("Length of employee Id is zero");
		int actualEmployeeId=0;
		try
		{
			actualEmployeeId=Integer.parseInt(employeeId.substring(1))-10000000;
		}catch(Exception exception)
		{
			throw new DAOException("Invalid employee Id"+employeeId);
		}
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			preparedStatement=connection.prepareStatement("select * from employee where employee_id=?");
			preparedStatement.setInt(1, actualEmployeeId);
			resultSet=preparedStatement.executeQuery();
			java.sql.Date sqlDateOfBirth;
			java.util.Date dateOfBirth;
			String gender;
			if(!resultSet.next())
			{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid employee id: "+employeeId);
			}
			employeeDTO=new EmployeeDTO();
			employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
			employeeDTO.setName(resultSet.getString("name").trim());
			employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
			sqlDateOfBirth=resultSet.getDate("date_of_birth");
			dateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
			employeeDTO.setDateOfBirth(dateOfBirth);
			employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
			gender=resultSet.getString("gender");
			if(gender.equals("M"))
			{
				employeeDTO.setGender(GENDER.MALE);
			}
			if(gender.equals("F"))
			{
				employeeDTO.setGender(GENDER.FEMALE);
			}
			employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
			employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
			employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number").trim());
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return employeeDTO;
	}
	public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
	{
		if(panNumber==null) throw new DAOException("Invalid PAN Number"+panNumber);
		panNumber=panNumber.trim();
		if(panNumber.length()==0) throw new DAOException("Invalid PAN Number: length of PAN Number is zero");
		EmployeeDTOInterface employeeDTO=null;
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			preparedStatement=connection.prepareStatement("select * from employee where pan_number=?");
			preparedStatement.setString(1, panNumber);
			resultSet=preparedStatement.executeQuery();
			java.sql.Date sqlDateOfBirth;
			java.util.Date dateOfBirth;
			String gender;
			if(!resultSet.next())
			{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid PAN Number"+panNumber);
			}
			employeeDTO=new EmployeeDTO();
			employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
			employeeDTO.setName(resultSet.getString("name").trim());
			employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
			sqlDateOfBirth=resultSet.getDate("date_of_birth");
			dateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
			employeeDTO.setDateOfBirth(dateOfBirth);
			employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
			gender=resultSet.getString("gender");
			if(gender.equals("M"))
			{
				employeeDTO.setGender(GENDER.MALE);
			}
			if(gender.equals("F"))
			{
				employeeDTO.setGender(GENDER.FEMALE);
			}
			employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
			employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
			employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number").trim());
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return employeeDTO;
	}
	public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
	{
		if(aadharCardNumber==null) throw new DAOException("Invalid Aadhar card number: "+aadharCardNumber);
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0) throw new DAOException("Invalid Aadhar card number: length of  Aadhar card number is zero");
		EmployeeDTOInterface employeeDTO=null;
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			preparedStatement=connection.prepareStatement("select * from employee where aadhar_card_number_number=?");
			preparedStatement.setString(1, aadharCardNumber);
			resultSet=preparedStatement.executeQuery();
			java.sql.Date sqlDateOfBirth;
			java.util.Date dateOfBirth;
			String gender;
			if(!resultSet.next())
			{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid PAN Number"+aadharCardNumber);
			}
			employeeDTO=new EmployeeDTO();
			employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
			employeeDTO.setName(resultSet.getString("name").trim());
			employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
			sqlDateOfBirth=resultSet.getDate("date_of_birth");
			dateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
			employeeDTO.setDateOfBirth(dateOfBirth);
			employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
			gender=resultSet.getString("gender");
			if(gender.equals("M"))
			{
				employeeDTO.setGender(GENDER.MALE);
			}
			if(gender.equals("F"))
			{
				employeeDTO.setGender(GENDER.FEMALE);
			}
			employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
			employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
			employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number").trim());
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return employeeDTO;
	}
	public boolean employeeIdExists(String employeeId) throws DAOException
	{
		if(employeeId==null) return false;
		employeeId=employeeId.trim();
		if(employeeId.length()==0) return false;
		boolean employeeIdExist=false;
		int actualEmployeeId=0;
		try
		{
			actualEmployeeId=Integer.parseInt(employeeId.substring(1))-10000000;
		}catch(Exception exception)
		{
			throw new DAOException("Invalid employee Id"+employeeId);
		}
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			preparedStatement=connection.prepareStatement("select gender from employee where employee_id=?");
			preparedStatement.setInt(1, actualEmployeeId);
			resultSet=preparedStatement.executeQuery();
			employeeIdExist=resultSet.next();
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return employeeIdExist;
		
	}
	public boolean panNumberExists(String panNumber) throws DAOException
	{
		if(panNumber==null) return false; 
		panNumber=panNumber.trim();
		if(panNumber.length()==0) return false;
		boolean panNumberExist=false;
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			preparedStatement=connection.prepareStatement("select gender from employee where pan_number=?");
			preparedStatement.setString(1, panNumber);
			resultSet=preparedStatement.executeQuery();
			panNumberExist=resultSet.next();
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return panNumberExist;
	}
	public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
	{
		if(aadharCardNumber==null) return false; 
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0) return false;
		boolean aadharCardNumberExist=false;
		try
		{
			Connection connection=DAOConnection.getConnection();
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			preparedStatement=connection.prepareStatement("select gender from employee where aadhar_card_number_number=?");
			preparedStatement.setString(1, aadharCardNumber);
			resultSet=preparedStatement.executeQuery();
			aadharCardNumberExist=resultSet.next();
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		return aadharCardNumberExist;
	}
	public int getCount() throws DAOException
	{
		int recordCount=0;
		try
		{
			Connection connection;
			connection=DAOConnection.getConnection();
			Statement statement;
			statement=connection.createStatement();
			ResultSet resultSet;
			resultSet=statement.executeQuery("select count(*) from employee");
			resultSet.next();
			recordCount=resultSet.getInt("count");
			resultSet.close();
			statement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		} 
		return recordCount;
	}
	public int getCountByDesignation(int designationCode) throws DAOException
	{
		int recordCount=0;
		Connection connection=null;
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		try
		{
			connection=DAOConnection.getConnection();
			preparedStatement=connection.prepareStatement("select code from designation where code=?");
			preparedStatement.setInt(1, designationCode);
			resultSet=preparedStatement.executeQuery();
			if(!resultSet.next())
			{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid designation code: "+designationCode); 
			}
			resultSet.close();
			preparedStatement.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		}
		try
		{
			preparedStatement=connection.prepareStatement("select count(*) from employee where designation_code=?");
			preparedStatement.setInt(1, designationCode);
			resultSet=preparedStatement.executeQuery();
			resultSet.next();
			recordCount=resultSet.getInt("count");
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException sqlException)
		{
			throw new DAOException(sqlException.getMessage());
		} 
		return recordCount;
	} 	
}