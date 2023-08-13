import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dao.*;
public class DesignationGetByTitleTestCase
{
	public static void main(String args[])
	{
		String title=args[0];
		try
		{
			DesignationDAOInterface designationDAO;
			designationDAO=new DesignationDAO();
			DesignationDTOInterface designationDTO;
			designationDTO=designationDAO.getByTitle(title);
			System.out.println("Designation: "+designationDTO.getTitle()+","+" Code: "+designationDTO.getCode());
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}