import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
class EmployeeManagerRemoveTestCase
{
	public static void main(String args[])
	{
		String employeeId="A10000001";
		try
		{
			EmployeeManagerInterface employeeManager;
			employeeManager=EmployeeManager.getEmployeeManager();
			employeeManager.getEmployeeByEmployeeId(employeeId);
			System.out.println("Employee deleted");
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

