package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
public class DesignationManager implements DesignationManagerInterface
{
	private static DesignationManager designationManager=null;
	
	private DesignationManager() throws BLException
	{
		
	}
		
	public static DesignationManagerInterface getDesignationManager() throws BLException
	{
		if(designationManager==null) designationManager=new DesignationManager();
		return designationManager;
	}
	
	public void addDesignation(DesignationInterface designation) throws BLException
	{
		BLException blException=new BLException();
		blException.setGenericException("Not yet Implemented");
		throw blException;
	}
	public void updateDesignation(DesignationInterface designation) throws BLException
	{
		BLException blException=new BLException();
		blException.setGenericException("Not yet Implemented");
		throw blException;
	}
	public void removeDesignation(int code) throws BLException
	{
		BLException blException=new BLException();
		blException.setGenericException("Not yet Implemented");
		throw blException;
	}
	public DesignationInterface getDesignationByCode(int code) throws BLException
	{
		BLException blException=new BLException();
		blException.setGenericException("Not yet Implemented");
		throw blException;
	}
	
	DesignationInterface getDSDesignationByCode(int code)
	{
		return null;
	}
	
	public DesignationInterface getDesignationByTitle(String title) throws BLException
	{
		BLException blException=new BLException();
		blException.setGenericException("Not yet Implemented");
		throw blException;
	}
	public int getDesignationCount()
	{
		return 0;
	}
	public boolean designationCodeExists(int code)
	{
		return false;
	}
	public boolean designationTitleExists(String title)
	{
		return false;
	}
	public Set<DesignationInterface> getDesignations()
	{		
		return null;
	}

}
