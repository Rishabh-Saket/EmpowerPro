import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
public class DesignationUpdateTestCase
{
	public static void main(String args[])
	{
		int code=Integer.parseInt(args[0]);
		String title=args[1];
		try
		{
			DesignationDTOInterface designationDTO;
			designationDTO=new DesignationDTO();
			designationDTO.setCode(code);
			designationDTO.setTitle(title);
			DesignationDAOInterface designationDAO;
			designationDAO=new DesignationDAO();
			designationDAO.update(designationDTO);
			System.out.println("Designation Update");
		}catch(DAOException daoException)
		{
			System.out.println(daoException.getMessage());
		}
	}
}