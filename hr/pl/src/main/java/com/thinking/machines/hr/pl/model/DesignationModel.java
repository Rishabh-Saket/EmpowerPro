package com.thinking.machines.hr.pl.model;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.io.image.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.borders.*;
public class DesignationModel extends AbstractTableModel
{
	private java.util.List<DesignationInterface> designations;
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
	
	public void exportToPDF(File file) throws BLException
	{
		try
		{
			if(file.exists()) file.delete();
			PdfWriter pdfWriter=new PdfWriter(file);
			PdfDocument pdfDocument=new PdfDocument(pdfWriter);
			Document document=new Document(pdfDocument);
			
			// adding comapny's logo
			Image logo=new Image(ImageDataFactory.create(this.getClass().getResource("/icons/logo_icon.png")));
			Paragraph logoPara=new Paragraph();
			logoPara.add(logo);
			
			// company name
			Paragraph companyNamePara=new Paragraph();
			companyNamePara.add("Saket's Corporation");
			
			//font for company 
			PdfFont companyNameFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			companyNamePara.setFont(companyNameFont);
			companyNamePara.setFontSize(18);
			
			//title for report
			Paragraph reportTitlePara=new Paragraph("List of designations");
			PdfFont reportTitleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			reportTitlePara.setFont(reportTitleFont);
			reportTitlePara.setFontSize(15);
			
			//font for title of table
			PdfFont columnTitleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			PdfFont pageNumberFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			PdfFont dataFont=PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			//Declaring titles of column
			Paragraph columnTitle1=new Paragraph("S.No");
			columnTitle1.setFont(columnTitleFont);
			columnTitle1.setFontSize(14);
			
			Paragraph columnTitle2=new Paragraph("Designations");
			columnTitle2.setFont(columnTitleFont);
			columnTitle2.setFontSize(14);
			
			//Now creating every dynamic factors that will be initialized during logic
			
			Paragraph pageNumberParagraph;
			Paragraph dataParagraph;
			
			float topTableColumnWidths[]= {1,5}; // parameters to set logo and company name
			float dataTableColumnWidths[]= {1,5}; // parameters for actual data
			
			
			//now actual logic 
			int sno,x,pageSize;
			sno=0;
			x=0;
			pageSize=5;			
			boolean newPage;
			newPage=true;
			Table topTable; //table to set logo and company name
			Table dataTable=null; //table to set data
			Table pageNumberTable; //table for page number 
			Cell cell;
			int pageNumber=0;
			int numberOfPages=this.designations.size()/pageSize;
			if(this.designations.size()%pageSize!=0) numberOfPages++;
			DesignationInterface designation;
			while(x<this.designations.size())
			{
				if(newPage)
				{
					// create header
					pageNumber++;
					topTable=new Table(UnitValue.createPercentArray(topTableColumnWidths));
					cell=new Cell();
					cell.setBorder(Border.NO_BORDER);
					cell.add(logoPara);
					topTable.addCell(cell);
					
					cell=new Cell();
					cell.setBorder(Border.NO_BORDER);
					cell.add(companyNamePara);
					cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
					topTable.addCell(cell);
					
					document.add(topTable);
					
					pageNumberParagraph=new Paragraph("Page: "+pageNumber+"/"+numberOfPages);
					pageNumberParagraph.setFont(pageNumberFont);
					pageNumberParagraph.setFontSize(10);
					
					pageNumberTable=new Table(1);
					pageNumberTable.setWidth(UnitValue.createPercentValue(100));
					cell=new Cell();
					cell.add(pageNumberParagraph);
					cell.setBorder(Border.NO_BORDER);
					cell.setTextAlignment(TextAlignment.RIGHT);
					pageNumberTable.addCell(cell);
					document.add(pageNumberTable);
					
					//creating data table
					dataTable=new Table(UnitValue.createPercentArray(dataTableColumnWidths));
					dataTable.setWidth(UnitValue.createPercentValue(100));
					
					cell=new Cell(1,2);
					cell.add(reportTitlePara);
					cell.setTextAlignment(TextAlignment.CENTER);
					dataTable.addHeaderCell(cell);
					dataTable.addHeaderCell(columnTitle1);
					dataTable.addHeaderCell(columnTitle2);									
					newPage=false;					
				}
				designation=this.designations.get(x);
				sno++;
				x++;
				
				cell=new Cell();
				dataParagraph=new Paragraph(String.valueOf(sno));
				dataParagraph.setFont(dataFont);
				dataParagraph.setFontSize(14);
				cell.add(dataParagraph);
				cell.setTextAlignment(TextAlignment.RIGHT);
				dataTable.addCell(cell);
				
				cell=new Cell();
				dataParagraph=new Paragraph(designation.getTitle());
				dataParagraph.setFont(dataFont);
				dataParagraph.setFontSize(14);
				cell.add(dataParagraph);
				dataTable.addCell(cell);
				
				if(sno%pageSize==0 || x==this.designations.size())
				{
					//create footer
					document.add(dataTable);
					document.add(new Paragraph("Software by: Saket"));
					if(x<this.designations.size())
					{
						// add new page
						document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
						newPage=true;
					}
				}
			}	
			document.close();
		}catch(Exception exception)
		{
			BLException blException=new BLException();
			blException.setGenericException(exception.getMessage());
			throw blException;
		}
	}
}