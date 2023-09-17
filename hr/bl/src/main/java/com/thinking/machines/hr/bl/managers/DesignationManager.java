package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
public class DesignationManager implements DesignationManagerInterface
{
	private Map<Integer,DesignationInterface> codeWiseDesignationMap;
	private Map<String,DesignationInterface> titleWiseDesignationMap;
	private Set<DesignationInterface> designationsSet;
	private static DesignationManager designationManager=null;
	
	private DesignationManager() throws BLException
	{
		populateDataStructures();
	}
	
	private void populateDataStructures() throws BLException
	{
		this.codeWiseDesignationMap=new HashMap<>();
		this.titleWiseDesignationMap=new HashMap<>();
		this.designationsSet=new TreeSet<>();
		try
		{
			Set<DesignationDTOInterface> dlDesignations;
			dlDesignations=new DesignationDAO().getAll();
			DesignationInterface designation;
			for(DesignationDTOInterface dlDesignation:dlDesignations)
			{
				designation=new Designation();
				designation.setCode(dlDesignation.getCode());
				designation.setTitle(dlDesignation.getTitle());
				this.codeWiseDesignationMap.put(designation.getCode(),designation);
				this.titleWiseDesignationMap.put(designation.getTitle().toUpperCase(),designation);
				this.designationsSet.add(designation);
			}
		}catch(DAOException daoException)
		{
			BLException blException=new BLException();
			blException.setGenericException(daoException.getMessage());
			throw blException;
		}
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
		if(title.length()>0)
		{
			if(this.titleWiseDesignationMap.containsKey(title.toUpperCase()))
			{
				blException.addException("title","Designation: "+title+" already exists");
			}
		}
		if(blException.hasExceptions())
		{
			throw blException;
		}
		try
		{
			DesignationDTOInterface designationDTO;
			designationDTO=new DesignationDTO();
			designationDTO.setTitle(title);
			DesignationDAOInterface designationDAO;
			designationDAO=new DesignationDAO();
			designationDAO.add(designationDTO);
			code=designationDTO.getCode();
			designation.setCode(code);
			DesignationInterface dsDesignation;
			dsDesignation=new Designation();
			dsDesignation.setCode(code);
			dsDesignation.setTitle(title);
			this.codeWiseDesignationMap.put(code,dsDesignation);
			this.titleWiseDesignationMap.put(title.toUpperCase(),dsDesignation);
			this.designationsSet.add(dsDesignation);
		}catch(DAOException daoException)
		{
			blException.setGenericException(daoException.getMessage());
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
		else
		{
			if(this.codeWiseDesignationMap.containsKey(code)==false)
			{
				blException.addException("code","Invalid Code: "+code);
				throw blException;
			}
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
		if(title.length()>0)
		{
			DesignationInterface d;
			d=this.titleWiseDesignationMap.get(title.toUpperCase());
			if(d!=null && d.getCode()!=code)
			{
				blException.addException("title","Designation: "+title+" already exists");
			}
		}
		if(blException.hasExceptions())
		{
			throw blException;
		}
		try
		{
			DesignationDTOInterface designationDTO;
			designationDTO=new DesignationDTO();
			designationDTO.setCode(code);
			designationDTO.setTitle(title);
			new DesignationDAO().update(designationDTO);
			DesignationInterface dsDesignation=this.codeWiseDesignationMap.get(code);
			// remove old data from DS
			this.codeWiseDesignationMap.remove(code);
			this.titleWiseDesignationMap.remove(dsDesignation.getTitle().toUpperCase());
			this.designationsSet.remove(dsDesignation);
			// update the DS object
			dsDesignation.setTitle(title);
			// update the DS
			this.codeWiseDesignationMap.put(code,dsDesignation);
			this.titleWiseDesignationMap.put(title.toUpperCase(),dsDesignation);
			this.designationsSet.add(dsDesignation);
		}catch(DAOException daoException)
		{
			blException.setGenericException(daoException.getMessage());
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
		else
		{
			if(this.codeWiseDesignationMap.containsKey(code)==false)
			{
				blException.addException("code","Invalid Code: "+code);
				throw blException;
			}
		}
		try
		{
			new DesignationDAO().delete(code);
			DesignationInterface dsDesignation=this.codeWiseDesignationMap.get(code);
			// remove old data from DS
			this.codeWiseDesignationMap.remove(code);
			this.titleWiseDesignationMap.remove(dsDesignation.getTitle().toUpperCase());
			this.designationsSet.remove(dsDesignation);
		}catch(DAOException daoException)
		{
			blException.setGenericException(daoException.getMessage());
		}
	}
	public DesignationInterface getDesignationByCode(int code) throws BLException
	{
		DesignationInterface designation;
		designation=this.codeWiseDesignationMap.get(code);
		if(designation==null)
		{
			BLException blException=new BLException();
			blException.addException("code","Invalid code: "+code);
			throw blException;
		}
		DesignationInterface d=new Designation();
		d.setCode(designation.getCode());
		d.setTitle(designation.getTitle());
		return d;
	}
	
	DesignationInterface getDSDesignationByCode(int code)
	{
		DesignationInterface designation;
		designation=this.codeWiseDesignationMap.get(code);
		return designation;
	}
	
	public DesignationInterface getDesignationByTitle(String title) throws BLException
	{
		DesignationInterface designation;
		designation=this.titleWiseDesignationMap.get(title.toUpperCase());
		if(designation==null)
		{
			BLException blException=new BLException();
			blException.addException("title","Invalid designation: "+title);
			throw blException;
		}
		DesignationInterface d=new Designation();
		d.setCode(designation.getCode());
		d.setTitle(designation.getTitle());
		return d;
	}
	public int getDesignationCount()
	{
		return this.designationsSet.size();
	}
	public boolean designationCodeExists(int code)
	{
		return this.codeWiseDesignationMap.containsKey(code);
	}
	public boolean designationTitleExists(String title)
	{
		return this.titleWiseDesignationMap.containsKey(title.toUpperCase());
	}
	public Set<DesignationInterface> getDesignations()
	{
		Set<DesignationInterface> designations;
		designations=new TreeSet<>();
		this.designationsSet.forEach((designation)->{
			DesignationInterface d=new Designation();
			d.setCode(designation.getCode());
			d.setTitle(designation.getTitle());
			designations.add(d);
		});
		return designations;
	}

}
