import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
public class DesignationManagerGetDesignationsCountTestCase
{
	public static void main(String args[])
	{
		try
		{
			DesignationManagerInterface designationManager;
			designationManager=DesignationManager.getDesignationManager();
			System.out.println("Number of Designations: "+designationManager.getDesignationCount());
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