import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
public class EmployeeUpdateTestCase
{
	public static void main(String args[])
	{
		String employeeId=args[0];
		String name=args[1];
		int designationCode=Integer.parseInt(args[2]);
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		Date dateOfBirth=null;
		try
		{
			dateOfBirth=sdf.parse(args[3]);
		}catch(ParseException pe)
		{
			System.out.println(pe.getMessage());
			return;
		}
		char gender=args[4].charAt(0);
		boolean isIndian=Boolean.parseBoolean(args[5]);
		BigDecimal basicSalary=new BigDecimal(args[6]);
		String panNumber=args[7];
		String aadharCardNumber=args[8];
		try
		{
			EmployeeDTOInterface employeeDTO;
			employeeDTO=new EmployeeDTO();
			employeeDTO.setEmployeeId(employeeId);
			employeeDTO.setName(name);
			employeeDTO.setDesignationCode(designationCode);
			employeeDTO.setDateOfBirth(dateOfBirth);
			if(gender=='M') employeeDTO.setGender(GENDER.MALE);
			if(gender=='F') employeeDTO.setGender(GENDER.FEMALE);
			employeeDTO.setIsIndian(isIndian);
			employeeDTO.setBasicSalary(basicSalary);
			employeeDTO.setPANNumber(panNumber);
			employeeDTO.setAadharCardNumber(aadharCardNumber);
			EmployeeDAOInterface employeeDAO;
			employeeDAO=new EmployeeDAO();
			employeeDAO.update(employeeDTO);
			System.out.println("Employee Updated");
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}