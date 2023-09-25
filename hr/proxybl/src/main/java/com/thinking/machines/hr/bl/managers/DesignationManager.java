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
		request.setManager(Managers.getManagerType(Managers.DESIGNATION));
		request.setAction(Managers.getAction(Managers.Designation.ADD_DESGIGNATION));
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
			blException.setGenericException(networkException.getMessage());
			throw blException;
		}
	}
	public void updateDesignation(DesignationInterface designation) throws BLException
	{
		BLException blException=new BLException();
		if(designation==null)
		{
			blException.setGenericException("Designation required");
			throw blException;
		}
		int code=designation.getCode();
		String title=designation.getTitle();
		if(code<=0)
		{
			blException.addException("code","Invalid Code: "+code);
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
		request.setManager(Managers.getManagerType(Managers.DESIGNATION));
		request.setAction(Managers.getAction(Managers.Designation.UPDATE_DESGIGNATION));
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
		}catch(NetworkException networkException)
		{
			blException.setGenericException(networkException.getMessage());
			throw blException;
		}
	}
	public void removeDesignation(int code) throws BLException
	{
		BLException blException=new BLException();
		if(code<=0)
		{
			blException.addException("code","Invalid Code: "+code);
			throw blException;
		}
		Request request=new Request();
		request.setManager(Managers.getManagerType(Managers.DESIGNATION));
		request.setAction(Managers.getAction(Managers.Designation.REMOVE_DESGIGNATION));
		request.setArguments(code);
		NetworkClient networkClient=new NetworkClient();
		try
		{
			Response response=networkClient.send(request);
			if(response.hasException())
			{
				blException=(BLException)response.getException();
				throw blException;
			}
		}catch(NetworkException networkException)
		{
			blException.setGenericException(networkException.getMessage());
			throw blException;
		}
	}
	public DesignationInterface getDesignationByCode(int code) throws BLException
	{
		BLException blException=new BLException();
		if(code<=0)
		{
			blException.addException("code","Invalid Code: "+code);
			throw blException;
		}
		Request request=new Request();
		request.setManager(Managers.getManagerType(Managers.DESIGNATION));
		request.setAction(Managers.getAction(Managers.Designation.GET_DESGIGNATION_BY_CODE));
		request.setArguments(code);
		NetworkClient networkClient=new NetworkClient();
		try
		{
			Response response=networkClient.send(request);
			if(response.hasException())
			{
				blException=(BLException)response.getException();
				throw blException;
			}
			DesignationInterface designation;
			designation=(DesignationInterface)response.getResult();
			return designation;
		}catch(NetworkException networkException)
		{
			blException.setGenericException(networkException.getMessage());
			throw blException;
		}
	}
	
	
	public DesignationInterface getDesignationByTitle(String title) throws BLException
	{
		BLException blException=new BLException();
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
		request.setManager(Managers.getManagerType(Managers.DESIGNATION));
		request.setAction(Managers.getAction(Managers.Designation.GET_DESGIGNATION_BY_TITLE));
		request.setArguments(title);
		NetworkClient networkClient=new NetworkClient();
		try
		{
			Response response=networkClient.send(request);
			if(response.hasException())
			{
				blException=(BLException)response.getException();
				throw blException;
			}
			DesignationInterface designation;
			designation=(DesignationInterface)response.getResult();
			return designation;
		}catch(NetworkException networkException)
		{
			blException.setGenericException(networkException.getMessage());
			throw blException;
		}
	}
	public int getDesignationCount()
	{
		BLException blException=new BLException();
		Request request=new Request();
		request.setManager(Managers.getManagerType(Managers.DESIGNATION));
		request.setAction(Managers.getAction(Managers.Designation.GET_DESGIGNATION_COUNT));
		NetworkClient networkClient=new NetworkClient();
		try
		{
			Response response=networkClient.send(request);
			Integer count;
			count=(Integer)response.getResult();
			return count.intValue(); // no need for intValue(), unboxing will be done implicitly but for clarity we wrote
		}catch(NetworkException networkException)
		{
			// return 0; --> we can do this also but below one is more suitable
			throw new RuntimeException(networkException.getMessage());
		}
	}
	public boolean designationCodeExists(int code)
	{
		BLException blException=new BLException();
		if(code<=0)
		{
			return false;
		}
		Request request=new Request();
		request.setManager(Managers.getManagerType(Managers.DESIGNATION));
		request.setAction(Managers.getAction(Managers.Designation.DESGIGNATION_CODE_EXISTS));
		request.setArguments(code);
		NetworkClient networkClient=new NetworkClient();
		try
		{
			Response response=networkClient.send(request);
			Boolean exists;
			exists=(Boolean)response.getResult();
			return exists; //unboxing will be done
		}catch(NetworkException networkException)
		{
			throw new RuntimeException(networkException.getMessage());
		}
	}
	public boolean designationTitleExists(String title)
	{
		if(title==null)
		{
			return false;
		}
		title=title.trim();
		if(title.length()==0)
		{
			return false;
		}
		Request request=new Request();
		request.setManager(Managers.getManagerType(Managers.DESIGNATION));
		request.setAction(Managers.getAction(Managers.Designation.DESIGNATION_TITLE_EXISTS));
		request.setArguments(title);
		NetworkClient networkClient=new NetworkClient();
		try
		{
			Response response=networkClient.send(request);
			Boolean exists;
			exists=(Boolean)response.getResult();
			return exists;
		}catch(NetworkException networkException)
		{
			throw new RuntimeException(networkException.getMessage());
		}
	}
	public Set<DesignationInterface> getDesignations()
	{		
		Request request=new Request();
		request.setManager(Managers.getManagerType(Managers.DESIGNATION));
		request.setAction(Managers.getAction(Managers.Designation.GET_DESIGNATIONS));
		NetworkClient networkClient=new NetworkClient();
		try
		{
			Response response=networkClient.send(request);
			Set<DesignationInterface> designations;
			designations=(Set<DesignationInterface>)response.getResult();
			return designations;
		}catch(NetworkException networkException)
		{
			throw new RuntimeException(networkException.getMessage());
		}
	}

}
