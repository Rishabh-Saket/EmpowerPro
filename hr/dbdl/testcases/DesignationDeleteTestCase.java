import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;
public class DesignationDeleteTestCase
{
	public static void main(String args[])
	{
		int code=Integer.parseInt(args[0]);
		try
		{
			DesignationDAOInterface designationDAO;
			designationDAO=new DesignationDAO();
			designationDAO.delete(code);
			System.out.println(code+" Deleted");
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}