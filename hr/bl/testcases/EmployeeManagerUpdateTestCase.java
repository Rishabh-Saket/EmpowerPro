import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
class EmployeeManagerUpdateTestCase
{
	public static void main(String args[])
	{
		String employeeId="A10000001";
		String name="AAkhand Singh";
		DesignationInterface designation=new Designation();
		designation.setCode(1);
		Date dateOfBirth=new Date();
		char gender='M';
		boolean isIndian=true;
		BigDecimal basicSalary=new BigDecimal("45349.78");
		String panNumber="A1234";
		String aadharCardNumber="U1234";
		try
		{
			EmployeeInterface employee=new Employee();
			employee.setEmployeeId(employeeId);
			employee.setName(name);
			employee.setDesignation(designation);
			employee.setDateOfBirth(dateOfBirth);
			employee.setGender((gender=='M')? GENDER.MALE:GENDER.FEMALE);
			employee.setIsIndian(isIndian);
			employee.setBasicSalary(basicSalary);
			employee.setPANNumber(panNumber);
			employee.setAadharCardNumber(aadharCardNumber);
			EmployeeManagerInterface employeeManager;
			employeeManager=EmployeeManager.getEmployeeManager();
			employeeManager.updateEmployee(employee);
			System.out.println("Employee updated");
		}catch(BLException blException)
		{
			if(blException.hasGenericException())
			{
				System.out.println(blException.getGenericException());
			}
			List<String> properties=blException.getProperties();
			for(String property:properties)
			{
				System.out.println(blException.getException(property));
			}
		}
	}
}

