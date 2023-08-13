import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.enums.*;
import java.math.*;
import java.text.*;
import java.util.*;
public class EmployeeEmployeeIdExistsTestCase
{
	public static void main(String args[])
	{
		String employeeId=args[0];
		try
		{
				System.out.println("Employee Id: "+employeeId+" exists : "+new EmployeeDAO().employeeIdExists(employeeId));
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}