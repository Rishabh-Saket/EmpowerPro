import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dao.*;
public class DesignationGetByCodeTestCase
{
	public static void main(String args[])
	{
		int code=Integer.parseInt(args[0]);
		try
		{
			DesignationDAOInterface designationDAO;
			designationDAO=new DesignationDAO();
			DesignationDTOInterface designationDTO;
			designationDTO=designationDAO.getByCode(code);
			System.out.println("Designation: "+designationDTO.getTitle()+","+" Code: "+designationDTO.getCode());
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}