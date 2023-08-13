import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dao.*;
public class DesignationTitleExistsTestCase
{
	public static void main(String args[])
	{
		String title=args[0];
		try
		{
			DesignationDAOInterface designationDAO;
			designationDAO=new DesignationDAO();
			System.out.println("Title: "+title+" exists: "+designationDAO.titleExists(title));
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}