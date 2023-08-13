import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
public class DesignationManagerGetDesignationByCodeTestCase
{
	public static void main(String args[])
	{
		int code=Integer.parseInt(args[0]);
		try
		{
			DesignationManagerInterface designationManager;
			designationManager=DesignationManager.getDesignationManager();
			DesignationInterface designation=designationManager.getDesignationByCode(code);
			System.out.println("code: "+designation.getCode()+" , Title: "+designation.getTitle());
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