import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
public class DesignationManagerGetDesignationsTestCase
{
	public static void main(String args[])
	{
		try
		{
			DesignationManagerInterface designationManager;
			designationManager=DesignationManager.getDesignationManager();
			Set<DesignationInterface> designations=designationManager.getDesignations();
			designations.forEach((designation)->{
			System.out.println("code: "+designation.getCode()+" , Title: "+designation.getTitle());
			});
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