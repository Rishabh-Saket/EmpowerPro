import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.enums.*;
import java.math.*;
import java.text.*;
import java.util.*;
public class EmployeeGetByEmployeeIdTestCase
{
	public static void main(String args[])
	{
		String employeeId=args[0];
		try
		{
			EmployeeDAOInterface employeeDAO;
			employeeDAO=new EmployeeDAO();
			EmployeeDTOInterface employeeDTO=new EmployeeDTO();
			employeeDTO=employeeDAO.getByEmployeeId(employeeId);
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
				System.out.println("Employee Id: "+employeeDTO.getEmployeeId());
				System.out.println("Employee Name: "+employeeDTO.getName());
				System.out.println("Employee Designation Code: "+employeeDTO.getDesignationCode());
				System.out.println("Employee Date of birth: "+simpleDateFormat.format(employeeDTO.getDateOfBirth()));
				System.out.println("Employee Gender: "+employeeDTO.getGender());
				System.out.println("Employee Indian?: "+employeeDTO.getIsIndian());
				System.out.println("Employee Basic Salary: "+employeeDTO.getBasicSalary().toPlainString());
				System.out.println("Employee Pan number: "+employeeDTO.getPANNumber());
				System.out.println("Employee Aadharcard Number: "+employeeDTO.getAadharCardNumber());
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}