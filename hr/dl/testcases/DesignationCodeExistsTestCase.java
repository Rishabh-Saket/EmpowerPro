import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dao.*;
public class DesignationCodeExistsTestCase
{
	public static void main(String args[])
	{
		int code=Integer.parseInt(args[0]);
		try
		{
			DesignationDAOInterface designationDAO;
			designationDAO=new DesignationDAO();
			System.out.println("Code: "+code+" exists: "+designationDAO.codeExists(code));
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}