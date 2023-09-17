package com.thinking.machines.hr.pl.model;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import javax.swing.table.*;
import java.util.*;
public class DesignationModel extends AbstractTableModel
{
	private List<DesignationInterface> designations;
	private DesignationManagerInterface designationManager;
	private String[] columnTitle;
	public DesignationModel()
	{
		populateDataStructures();
	}
	private void populateDataStructures()
	{
		this.columnTitle=new String[2];
		this.columnTitle[0]="S.No.";
		this.columnTitle[1]="Designation";
		try
		{
			this.designationManager=DesignationManager.getDesignationManager();
		}catch(BLException blException)
		{
			// ???? what to do
		}
		this.designations=new LinkedList<>();
		Set<DesignationInterface> blDesignations=this.designationManager.getDesignations();
		for(DesignationInterface designation: blDesignations)
		{
			this.designations.add(designation);
		}
		Collections.sort(designations,new Comparator<DesignationInterface>(){
			public int compare(DesignationInterface left,DesignationInterface right)
			{
				return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
			}
		});
	}
	
	public int getRowCount()
	{
		return this.designations.size();
	}
	
	public int getColumnCount()
	{
		return this.columnTitle.length;
	}
	
	public String getColumnName(int columnIndex)
	{
		return this.columnTitle[columnIndex];
	}
	
	public Object getValueAt(int rowIndex,int columnIndex)
	{
		if(columnIndex==0) return rowIndex+1;
		return this.designations.get(rowIndex).getTitle();
	}
	
	public Class getColumnClass(int columnIndex)
	{
		if(columnIndex==0) return Integer.class; // special treatement as good as writing Class.forName("java.lang.Integer");
		return String.class;
	}
	
	public boolean isCellEditable(int columnIndex,int rowIndex)
	{
		return false;
	}
	
	//Application Specific Method=----->>
	
	public void add(DesignationInterface designation) throws BLException
	{
		designationManager.addDesignation(designation);
		this.designations.add(designation);
		Collections.sort(this.designations,new Comparator<DesignationInterface>(){
			public int compare(DesignationInterface left,DesignationInterface right)
			{
				return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
			}
		});
		fireTableDataChanged();
	}
	
	public int indexOfDesignation(DesignationInterface designation) throws BLException
	{
		int index=0;
		Iterator<DesignationInterface>iterator=this.designations.iterator();
		DesignationInterface d=null;
		while(iterator.hasNext())
		{
			d=iterator.next();
			if(d.equals(designation))
			{
				return index;
			}
			index++;
		}
		BLException blException=new BLException();
		blException.setGenericException("Invalid designation: "+designation.getTitle());
		throw blException;
	}
	
	public int indexOfTitle(String title,boolean partialLeftSearch) throws BLException
	{
		Iterator<DesignationInterface> iterator=this.designations.iterator();
		int index=0;
		DesignationInterface d=null;
		while(iterator.hasNext())
		{
			d=iterator.next();
			if(partialLeftSearch)
			{
				if(d.getTitle().toUpperCase().startsWith(title.toUpperCase())) {
					return index;
				}
			}
			else
			{
				if(d.getTitle().equalsIgnoreCase(title))
				{
					return index;
				}
			}
			index++;
		}

		BLException blException=new BLException();
		blException.setGenericException("Invalid title: "+title);
		throw blException;
	}
	
	public void update(DesignationInterface designation) throws BLException
	{
		this.designationManager.updateDesignation(designation);
		this.designations.remove(indexOfDesignation(designation));
		this.designations.add(designation);
		Collections.sort(this.designations,new Comparator<DesignationInterface>(){
			public int compare(DesignationInterface left,DesignationInterface right)
			{
				return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
			}
		});
		fireTableDataChanged();
	}
	
	public void remove(int code) throws BLException
	{
		this.designationManager.removeDesignation(code);
		Iterator<DesignationInterface> iterator=this.designations.iterator();
		int index=0;
		while(iterator.hasNext())
		{
			if(iterator.next().getCode()==code) break;
			index++;
		}
		if(index==this.designations.size())
		{
			BLException blException=new BLException();
			blException.setGenericException("Invalid designation code: "+code);
			throw blException;
		}
		this.designations.remove(index);
		fireTableDataChanged();
	}
	
	public DesignationInterface getDesignationAt(int index) throws BLException
	{
		if(index<0 || index>=this.designations.size())
		{
			BLException blException=new BLException();
			blException.setGenericException("Invalid index: "+index);
			throw blException;
		}
		return this.designations.get(index);
	}
}