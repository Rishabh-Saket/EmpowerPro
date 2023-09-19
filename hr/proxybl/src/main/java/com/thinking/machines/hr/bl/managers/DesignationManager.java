package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import com.thinking.machines.network.client.*;
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
		if(designation==null)
		{
			blException.setGenericException("Designation required");
			throw blException;
		}
		int code=designation.getCode();
		String title=designation.getTitle();
		if(code!=0)
		{
			blException.addException("code","Code should be zero");
		}
		if(title==null)
		{
			blException.addException("Title","Title required");
			title="";
		}
		else
		{
			title=title.trim();
			if(title.length()==0)
			{
				blException.addException("title","Title required");
			}
		}
		if(blException.hasExceptions())
		{
			throw blException;
		}
		Request request=new Request();
		request.setManager("DesignationManager");
		request.setAction("add");
		request.setArguments(designation);

		NetworkClient networkClient=new NetworkClient();
		try 
		{
			Response response=networkClient.send(request);
			if(response.hasException())
			{
				blException=(BLException)response.getException();
				throw blException;
			}
			DesignationInterface d=(DesignationInterface)response.getResult();
			designation.setCode(d.getCode());			
		}catch (NetworkException networkException) 
		{
			blException=new BLException();
			blException.setGenericException(networkException.getMessage());
		}
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
