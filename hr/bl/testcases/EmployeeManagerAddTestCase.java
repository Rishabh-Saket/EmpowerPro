import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
class EmployeeManagerAddTestCase
{
	public static void main(String args[])
	{
		String name="Anjali Verma";
		DesignationInterface designation=new Designation();
		designation.setCode(2);
		Date dateOfBirth=new Date();
		char gender='F';
		boolean isIndian=true;
		BigDecimal basicSalary=new BigDecimal("75349.78");
		String panNumber="A21345";
		String aadharCardNumber="U21345";
		try
		{
			EmployeeInterface employee=new Employee();
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
			employeeManager.addEmployee(employee);
			System.out.println("Employee added with Id as: "+employee.getEmployeeId());
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

