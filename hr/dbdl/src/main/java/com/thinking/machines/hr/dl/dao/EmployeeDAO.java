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
		String employeeId;
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
			throw new DAOException("Invalid employee Id");
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
			throw new DAOException("Invalid employee Id");
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
			File file=new File(FILE_NAME);
			if(!file.exists()) return employees;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return employees;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			EmployeeDTOInterface employeeDTO;
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				employeeDTO=new EmployeeDTO();
				employeeDTO.setEmployeeId(randomAccessFile.readLine().trim());
				employeeDTO.setName(randomAccessFile.readLine());
				employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
				try
				{
					employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
				}catch(ParseException parseException)
				{
					
				}
				employeeDTO.setGender((randomAccessFile.readLine().charAt(0)=='M')? GENDER.MALE:GENDER.FEMALE);
				employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
				employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
				employeeDTO.setPANNumber(randomAccessFile.readLine());
				employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
				employees.add(employeeDTO);
			}
			randomAccessFile.close();
			return employees;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
	{
		if(new DesignationDAO().codeExists(designationCode)==false) throw new DAOException("Invalid code: "+designationCode);
		Set<EmployeeDTOInterface> employees=new TreeSet<>();
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists()) return employees;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return employees;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			String fEmployeeId;
			String fName;
			int fDesignationCode;
			EmployeeDTOInterface employeeDTO;
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				fEmployeeId=randomAccessFile.readLine().trim();
				fName=randomAccessFile.readLine();
				fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
				if(fDesignationCode!=designationCode)
				{
					for(int i=1;i<=6;i++) randomAccessFile.readLine();
					continue;
				}
				employeeDTO=new EmployeeDTO();
				employeeDTO.setEmployeeId(fEmployeeId);
				employeeDTO.setName(fName);
				employeeDTO.setDesignationCode(designationCode);
				try
				{
					employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
				}catch(ParseException parseException)
				{
					
				}
				employeeDTO.setGender((randomAccessFile.readLine().charAt(0)=='M')? GENDER.MALE:GENDER.FEMALE);
				employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
				employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
				employeeDTO.setPANNumber(randomAccessFile.readLine());
				employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
				employees.add(employeeDTO);
			}
			randomAccessFile.close();
			return employees;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public boolean isDesignationAlloted(int designationCode) throws DAOException
	{
		if(new DesignationDAO().codeExists(designationCode)==false)
		{
			throw new DAOException("Invalid designation code: "+designationCode);
		}
		if(designationCode<=0) return false; 
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists()) return false;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return false;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			int fDesignationCode;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				randomAccessFile.readLine();
				randomAccessFile.readLine();
				fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
				if(fDesignationCode==designationCode)
				{
					randomAccessFile.close();
					return true;
				}
				for(int i=4;i<=9;i++) randomAccessFile.readLine();
			}
			randomAccessFile.close();
			return false;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		} 
	}
	public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
	{
		if(employeeId==null) throw new DAOException("Invalid employee Id : id is null");
		employeeId=employeeId.trim();
		if(employeeId.length()==0) throw new DAOException("Length of employee Id is zero");
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists()) throw new DAOException("Invalid Employee Id"+employeeId); 
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				throw new DAOException("Invalid Employee Id"+employeeId);
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			EmployeeDTOInterface employeeDTO;
			String fEmployeeId;
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				fEmployeeId=randomAccessFile.readLine();
				if(fEmployeeId.equalsIgnoreCase(employeeId))
				{
					employeeDTO=new EmployeeDTO();
					employeeDTO.setEmployeeId(employeeId);
					employeeDTO.setName(randomAccessFile.readLine());
					employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
					try
					{
						employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
					}catch(ParseException parseException)
					{
						// do nothing
					}
					employeeDTO.setGender((randomAccessFile.readLine().charAt(0)=='M')? GENDER.MALE:GENDER.FEMALE);
					employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
					employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
					employeeDTO.setPANNumber(randomAccessFile.readLine());
					employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
					randomAccessFile.close();
					return employeeDTO;
				}
				for(int i=1;i<=8;i++) randomAccessFile.readLine();
			}
			randomAccessFile.close();
			throw new DAOException("Invalid Employee Id"+employeeId);
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage()); 
		}
	}
	public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
	{
		if(panNumber==null) throw new DAOException("Invalid PAN Number"+panNumber);
		panNumber=panNumber.trim();
		if(panNumber.length()==0) throw new DAOException("Invalid PAN Number: length of PAN Number is zero");
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists()) throw new DAOException("Invalid PAN Number"+panNumber);
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0) 
			{
				randomAccessFile.close();
				throw new DAOException("Invalid PAN Number"+panNumber);
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			EmployeeDTOInterface employeeDTO;
			String fEmployeeId;
			String fName;
			int fDesignationCode;
			java.util.Date fDateOfBirth=null;
			char fGender;
			boolean fIsIndian;
			BigDecimal fBasicSalary;
			String fPANNumber;
			String fAadharCardNumber;
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				fEmployeeId=randomAccessFile.readLine();
				fName=randomAccessFile.readLine();
				fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
				try
				{
					fDateOfBirth=simpleDateFormat.parse(randomAccessFile.readLine());
				}catch(ParseException parseException)
				{
					// DO NOTHING
				}
				fGender=randomAccessFile.readLine().charAt(0);
				fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
				fBasicSalary=new BigDecimal(randomAccessFile.readLine());
				fPANNumber=randomAccessFile.readLine();
				fAadharCardNumber=randomAccessFile.readLine();
				if(fPANNumber.equalsIgnoreCase(panNumber))
				{
					randomAccessFile.close();
					employeeDTO=new EmployeeDTO();
					employeeDTO.setEmployeeId(fEmployeeId);
					employeeDTO.setName(fName);
					employeeDTO.setDesignationCode(fDesignationCode);
					employeeDTO.setDateOfBirth(fDateOfBirth);
					employeeDTO.setGender((fGender=='M')? GENDER.MALE:GENDER.FEMALE);
					employeeDTO.setIsIndian(fIsIndian);
					employeeDTO.setBasicSalary(fBasicSalary);
					employeeDTO.setPANNumber(fPANNumber);
					employeeDTO.setAadharCardNumber(fAadharCardNumber);
					return employeeDTO;
				}
			}
			randomAccessFile.close();
			throw new DAOException("Invalid PAN Number"+panNumber);
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
	{
		if(aadharCardNumber==null) throw new DAOException("Invalid Aadhar card number: "+aadharCardNumber);
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0) throw new DAOException("Invalid Aadhar card number: length of  Aadhar card number is zero");
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists()) throw new DAOException("Invalid Aadhar card number: "+aadharCardNumber);
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0) 
			{
				randomAccessFile.close();
				throw new DAOException("Invalid Aadhar card number: "+aadharCardNumber);
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			EmployeeDTOInterface employeeDTO;
			String fEmployeeId;
			String fName;
			int fDesignationCode;
			java.util.Date fDateOfBirth=null;
			char fGender;
			boolean fIsIndian;
			BigDecimal fBasicSalary;
			String fPANNumber;
			String fAadharCardNumber;
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				fEmployeeId=randomAccessFile.readLine();
				fName=randomAccessFile.readLine();
				fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
				try
				{
					fDateOfBirth=simpleDateFormat.parse(randomAccessFile.readLine());
				}catch(ParseException parseException)
				{
					// DO NOTHING
				}
				fGender=randomAccessFile.readLine().charAt(0);
				fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
				fBasicSalary=new BigDecimal(randomAccessFile.readLine());
				fPANNumber=randomAccessFile.readLine();
				fAadharCardNumber=randomAccessFile.readLine();
				if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
				{
					randomAccessFile.close();
					employeeDTO=new EmployeeDTO();
					employeeDTO.setEmployeeId(fEmployeeId);
					employeeDTO.setName(fName);
					employeeDTO.setDesignationCode(fDesignationCode);
					employeeDTO.setDateOfBirth(fDateOfBirth);
					employeeDTO.setGender((fGender=='M')? GENDER.MALE:GENDER.FEMALE);
					employeeDTO.setIsIndian(fIsIndian);
					employeeDTO.setBasicSalary(fBasicSalary);
					employeeDTO.setPANNumber(fPANNumber);
					employeeDTO.setAadharCardNumber(fAadharCardNumber);
					return employeeDTO;
				}
			}
			randomAccessFile.close();
			throw new DAOException("Invalid Aadhar card number: "+aadharCardNumber);
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public boolean employeeIdExists(String employeeId) throws DAOException
	{
		if(employeeId==null) return false;
		employeeId=employeeId.trim();
		if(employeeId.length()==0) return false;
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists()) return false; 
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return false;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			String fEmployeeId;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				fEmployeeId=randomAccessFile.readLine();
				if(fEmployeeId.equalsIgnoreCase(employeeId))
				{
					randomAccessFile.close();
					return true;
				}
				for(int i=1;i<=8;i++) randomAccessFile.readLine();
			}
			randomAccessFile.close();
			return false;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage()); 
		} 
	}
	public boolean panNumberExists(String panNumber) throws DAOException
	{
		if(panNumber==null) return false; 
		panNumber=panNumber.trim();
		if(panNumber.length()==0) return false;
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists()) return false;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return false;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			String fPanNumber;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				for(int i=1;i<=7;i++) randomAccessFile.readLine();
				fPanNumber=randomAccessFile.readLine();
				if(fPanNumber.equalsIgnoreCase(panNumber))
				{
					randomAccessFile.close();
					return true;
				}
				randomAccessFile.readLine();
			}
			randomAccessFile.close();
			return false;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
	{
		if(aadharCardNumber==null) return false; 
		aadharCardNumber=aadharCardNumber.trim();
		if(aadharCardNumber.length()==0) return false;
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists()) return false;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return false;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			String fAadharCardNumber;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				for(int i=1;i<=8;i++) randomAccessFile.readLine();
				fAadharCardNumber=randomAccessFile.readLine();
				if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
				{
					randomAccessFile.close();
					return true;
				}
			}
			randomAccessFile.close();
			return false;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	}
	public int getCount() throws DAOException
	{
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists()) return 0;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return 0;
			}
			randomAccessFile.readLine();
			int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
			randomAccessFile.close();
			return recordCount;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		} 
	}
	public int getCountByDesignation(int designationCode) throws DAOException
	{
		if(new DesignationDAO().codeExists(designationCode)==false) throw new DAOException("Invalid designation code: "+designationCode); 
		try
		{
			File file=new File(FILE_NAME);
			if(!file.exists()) return 0;
			RandomAccessFile randomAccessFile;
			randomAccessFile=new RandomAccessFile(file, "rw");
			if(randomAccessFile.length()==0)
			{
				randomAccessFile.close();
				return 0;
			}
			randomAccessFile.readLine();
			randomAccessFile.readLine();
			int fDesignationCode;
			int count=0;
			while(randomAccessFile.getFilePointer()<randomAccessFile.length())
			{
				randomAccessFile.readLine();
				randomAccessFile.readLine();
				fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
				if(fDesignationCode==designationCode) count++;
				for(int i=4;i<=9;i++) randomAccessFile.readLine();
			}
			randomAccessFile.close();
			return count;
		}catch(IOException ioException)
		{
			throw new DAOException(ioException.getMessage());
		}
	} 	
}