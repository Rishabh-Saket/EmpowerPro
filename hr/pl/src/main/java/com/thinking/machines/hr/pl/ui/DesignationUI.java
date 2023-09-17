package com.thinking.machines.hr.pl.ui;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
public class DesignationUI extends JFrame implements DocumentListener,ListSelectionListener
{
	private JLabel titleLabel;
	private JLabel searchLabel;
	private JLabel searchErrorLabel;
	private JTextField searchTextField;
	private JButton clearSearchButton;
	private JTable designationTable;
	private JScrollPane scrollPane;
	private DesignationModel designationModel;
	private Container container;
	private DesignationPanel designationPanel;
	private enum MODE{VIEW,ADD,EDIT,DELETE,EXPORT_TO_PDF};
	private MODE mode;
	public DesignationUI()
	{
		initComponents();
		setAppearance();
		addListeners();
		setViewMode();
		designationPanel.setViewMode();
	}
	
	private void initComponents()
	{
		designationModel=new DesignationModel();
		titleLabel=new JLabel("Designations");
		searchLabel=new JLabel("Search");
		searchErrorLabel=new JLabel("");
		searchTextField=new JTextField();
		clearSearchButton=new JButton("X");
		designationTable=new JTable(designationModel);
		designationPanel=new DesignationPanel();
		scrollPane=new JScrollPane(designationTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		container=getContentPane();
	}
	
	private void setAppearance()
	{
		Font titleFont=new Font("Verdana",Font.BOLD,18);
		Font captionFont=new Font("Verdana",Font.BOLD,16);
		Font dataFont=new Font("Verdana",Font.PLAIN,16);
		Font searchErrorFont=new Font("Verdana",Font.BOLD,12);
		titleLabel.setFont(titleFont);
		searchLabel.setFont(captionFont);
		searchErrorLabel.setFont(searchErrorFont);
		searchErrorLabel.setForeground(Color.red);
		searchTextField.setFont(dataFont);
		designationTable.setFont(dataFont);
		designationTable.setRowHeight(30);
		designationTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		designationTable.getColumnModel().getColumn(1).setPreferredWidth(400);
		JTableHeader header=designationTable.getTableHeader();
		header.setFont(captionFont);
		header.setReorderingAllowed(false);
		header.setResizingAllowed(false);
		designationTable.setRowSelectionAllowed(true);
		designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		container.setLayout(null);
		int leftMargin=0;
		int topMargin=0;
		titleLabel.setBounds(leftMargin+10,topMargin+10,200,40);
		searchErrorLabel.setBounds(leftMargin+10+435,topMargin+10+20,100,30);
		searchLabel.setBounds(leftMargin+10,topMargin+10+40+10,100,30);
		searchTextField.setBounds(leftMargin+10+80,topMargin+10+40+10,420,30);
		clearSearchButton.setBounds(leftMargin+10+100+400+10,topMargin+10+40+10,30,30);
		scrollPane.setBounds(leftMargin+10,topMargin+10+40+10+40,540,200);
		designationPanel.setBounds(leftMargin+10,topMargin+10+40+10+40+200+10,540,200);
		container.add(titleLabel);
		container.add(searchLabel);
		container.add(searchErrorLabel);
		container.add(searchTextField);
		container.add(clearSearchButton);
		container.add(scrollPane);
		container.add(designationPanel);
		int w=575;
		int h=600;
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		setSize(w,h);
		setLocation((d.width/2)-(w/2),(d.height/2)-(h/2));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void addListeners()
	{
		searchTextField.getDocument().addDocumentListener(this);
		clearSearchButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				searchTextField.setText("");
				searchTextField.requestFocus();
			}
		});
		designationTable.getSelectionModel().addListSelectionListener(this);
	}
	
	private void searchDesignation()
	{
		searchErrorLabel.setText("");
		String title=searchTextField.getText().trim();
		if(title.length()==0) return;
		int rowIndex=0;
		try
		{
			rowIndex=designationModel.indexOfTitle(title,true);
			
		}catch(BLException blException)
		{
			searchErrorLabel.setText("Not found");
			return;
		}
		designationTable.setRowSelectionInterval(rowIndex,rowIndex);
		Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
		designationTable.scrollRectToVisible(rectangle);
	}
	
	public void insertUpdate(DocumentEvent ev)
	{
		searchDesignation();
	}
	
	public void changedUpdate(DocumentEvent ev)
	{
		searchDesignation();
	}
	
	public void removeUpdate(DocumentEvent ev)
	{
		searchDesignation();
	}
	
	public void valueChanged(ListSelectionEvent ev)
	{
		int selectedRowIndex=designationTable.getSelectedRow();
		try
		{
			DesignationInterface designation=designationModel.getDesignationAt(selectedRowIndex);
			designationPanel.setDesignation(designation);
		}catch(BLException blException)
		{
			designationPanel.clearDesignation();
		}
	}
	
	private void setViewMode()
	{
		this.mode=MODE.VIEW;
		if(designationModel.getRowCount()==0)
		{
			this.searchTextField.setEnabled(false);
			this.clearSearchButton.setEnabled(false);
			this.designationTable.setEnabled(false);
		}
		else
		{
			this.searchTextField.setEnabled(true);
			this.clearSearchButton.setEnabled(true);
			this.designationTable.setEnabled(true);
		}
	}
	
	private void setAddMode()
	{
		this.mode=MODE.ADD;
		this.searchTextField.setEnabled(false);
		this.clearSearchButton.setEnabled(false);
		this.designationTable.setEnabled(false);
	}
	
	private void setEditMode()
	{
		this.mode=MODE.EDIT;
		this.searchTextField.setEnabled(false);
		this.clearSearchButton.setEnabled(false);
		this.designationTable.setEnabled(false);
	}
	
	private void setDeleteMode()
	{
		this.mode=MODE.DELETE;
		this.searchTextField.setEnabled(false);
		this.clearSearchButton.setEnabled(false);
		this.designationTable.setEnabled(false);
	}
	
	private void setExportToPDFMode()
	{
		this.mode=MODE.EXPORT_TO_PDF;
		this.searchTextField.setEnabled(false);
		this.clearSearchButton.setEnabled(false);
		this.designationTable.setEnabled(false);
	}
	
	//inner class
	class DesignationPanel extends JPanel
	{
		private JLabel titleCaptionLabel;
		private JLabel titleLabel;
		private JTextField titleTextField;
		private JButton clearTitleTextFieldButton;
		private JButton addButton;
		private JButton editButton;
		private JButton cancelButton;
		private JButton deleteButton;
		private JButton exportToPDFButton;
		private JPanel buttonsPanel;
		private DesignationInterface designation;
		DesignationPanel()
		{
			setBorder(BorderFactory.createLineBorder(new Color(175,175,175)));
			initComponents();
			addAppearance();
			addListeners();
		}
		
		
		private void initComponents()
		{
			this.designation=null;
			titleCaptionLabel=new JLabel("Designation");
			titleLabel=new JLabel("");
			titleTextField=new JTextField();
			clearTitleTextFieldButton=new JButton("X");
			buttonsPanel=new JPanel();
			addButton=new JButton("A");
			editButton=new JButton("E");
			cancelButton=new JButton("C");
			deleteButton=new JButton("D");
			exportToPDFButton=new JButton("E");
		}
		
		public void setDesignation(DesignationInterface designation)
		{
			this.designation=designation;
			titleLabel.setText(designation.getTitle());
		}
		
		public void clearDesignation()
		{
			this.designation=null;
			titleLabel.setText("");
		}
		
		private void addAppearance()
		{
			Font captionFont=new Font("Verdana",Font.BOLD,16);
			Font dataFont=new Font("Verdana",Font.PLAIN,16);
			titleCaptionLabel.setFont(captionFont);
			titleLabel.setFont(dataFont);
			titleTextField.setFont(dataFont);
			setLayout(null);
			int leftMargin=0;
			int topMargin=0;
			titleCaptionLabel.setBounds(leftMargin+10,topMargin+20,110,30);
			titleLabel.setBounds(leftMargin+110+15,topMargin+20,400,30);
			titleTextField.setBounds(leftMargin+110+5+10,topMargin+20,350,30);
			clearTitleTextFieldButton.setBounds(leftMargin+110+5+10+10+5+350,topMargin+20,30,30);
			buttonsPanel.setBounds(50,topMargin+20+30+30,465,75);
			buttonsPanel.setBorder(BorderFactory.createLineBorder(new Color(165,165,165)));
			addButton.setBounds(70,12,50,50);
			editButton.setBounds(70+50+20,12,50,50);
			cancelButton.setBounds(70+50+20+50+20,12,50,50);
			deleteButton.setBounds(70+50+20+50+20+50+20,12,50,50);
			exportToPDFButton.setBounds(70+50+20+50+20+50+20+50+20,12,50,50);
			buttonsPanel.setLayout(null);
			buttonsPanel.add(addButton);
			buttonsPanel.add(editButton);
			buttonsPanel.add(cancelButton);
			buttonsPanel.add(deleteButton);
			buttonsPanel.add(exportToPDFButton);
			add(titleCaptionLabel);
			add(titleTextField);
			add(titleLabel);
			add(clearTitleTextFieldButton);
			add(buttonsPanel);
		}
		
		private boolean addDesignation()
		{
			String title=this.titleTextField.getText().trim();
			if(title.length()==0)
			{
				JOptionPane.showMessageDialog(this,"Designation required");
				this.titleTextField.requestFocus();
				return false;
			}
			else
			{
				DesignationInterface d=new Designation();
				d.setTitle(title);
				try
				{
					designationModel.add(d);
					int rowIndex=0;
					try
					{						
						rowIndex=designationModel.indexOfDesignation(d);						
					}catch(BLException blException)
					{
						//do nothing
					}
					designationTable.setRowSelectionInterval(rowIndex,rowIndex);
					Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
					designationTable.scrollRectToVisible(rectangle);
					return true;
				}catch(BLException blException)
				{
					if(blException.hasGenericException())
					{
						JOptionPane.showMessageDialog(this,blException.getGenericException());
					}
					else
					{
						if(blException.hasException("title"))
						{
							JOptionPane.showMessageDialog(this,blException.getException("title"));
						}
					}
					this.titleTextField.requestFocus();
					return false;
				}
			}
		}
		
		private boolean updateDesignation()
		{
			String title=this.titleTextField.getText().trim();
			if(title.length()==0)
			{
				JOptionPane.showMessageDialog(this,"Designation required");
				this.titleTextField.requestFocus();
				return false;
			}
			DesignationInterface d=new Designation();
			d.setTitle(title);
			d.setCode(this.designation.getCode());
			try
			{	
				designationModel.update(d);
				int rowIndex=0;
				try
				{
					rowIndex=designationModel.indexOfDesignation(d);
				}catch(BLException blException)
				{
					// do nothing
				}	
				designationTable.setRowSelectionInterval(rowIndex,rowIndex);
				Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
				designationTable.scrollRectToVisible(rectangle);
				searchTextField.setText("");
				return true;
			}catch(BLException blException)
			{
				if(blException.hasGenericException())
				{
					JOptionPane.showMessageDialog(this,blException.getGenericException());
				}
				else
				{
					if(blException.hasException("title"))
					{
						JOptionPane.showMessageDialog(this,blException.getException("title"));
					}
				}
			}
			this.titleTextField.requestFocus();
			return false;
		}
		
		private void deleteDesignation()
		{
			try
			{
				String title=this.designation.getTitle();
				int selectedOption=JOptionPane.showConfirmDialog(this,"Delete: "+title+" ?","Comfirmation Dialog",JOptionPane.YES_NO_OPTION);
				if(selectedOption==JOptionPane.NO_OPTION) return;
				designationModel.remove(this.designation.getCode());
				JOptionPane.showMessageDialog(this,"Designation: "+title+" deleted");
			}catch(BLException blException)
			{
				if(blException.hasGenericException())
				{
					JOptionPane.showMessageDialog(this,blException.getGenericException());
				}
				else
				{
					if(blException.hasException("title"))
					{
						JOptionPane.showMessageDialog(this,blException.getException("title"));
					}
				}
			}
		}
		
		private void addListeners()
		{
			this.addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev)
				{
					if(mode==MODE.VIEW)
					{
						setAddMode();
					}
					else
					{
						if(addDesignation())
						{
						setViewMode();
						}
					}
				}
			});
			
			this.editButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev)
				{
					if(mode==MODE.VIEW)
					{
						setEditMode();
					}
					else
					{
						if(updateDesignation()) setViewMode();
					}
				}
			});
			
			this.cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					setViewMode();
				}
			});
			
			this.deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev)
				{
					setDeleteMode();
				}
			});
		}
		
		void setViewMode()
		{
			DesignationUI.this.setViewMode();
			this.titleLabel.setVisible(true);
			this.titleTextField.setVisible(false);
			this.clearTitleTextFieldButton.setVisible(false);
			this.addButton.setEnabled(true);
			this.cancelButton.setEnabled(false);
			this.addButton.setText("A");
			this.editButton.setText("E");
			if(designationModel.getRowCount()==0)
			{
				this.editButton.setEnabled(false);
				this.deleteButton.setEnabled(false);
				this.exportToPDFButton.setEnabled(false);
			}
			else
			{
				this.editButton.setEnabled(true);
				this.deleteButton.setEnabled(true);
				this.exportToPDFButton.setEnabled(true);
			}
		}
		
		void setAddMode()
		{
			DesignationUI.this.setAddMode();
			this.titleTextField.setText("");
			this.titleLabel.setVisible(false);
			this.titleTextField.setVisible(true);
			this.clearTitleTextFieldButton.setVisible(true);
			this.addButton.setText("S");
			this.cancelButton.setEnabled(true);
			this.editButton.setEnabled(false);
			this.deleteButton.setEnabled(false);
			this.exportToPDFButton.setEnabled(false);
		}
		
		void setEditMode()
		{
			if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
			{
				JOptionPane.showMessageDialog(this,"Please select designation to edit");
				return;
			}
			DesignationUI.this.setEditMode();
			this.titleLabel.setVisible(false);
			this.titleTextField.setText(this.designation.getTitle());
			this.titleTextField.setVisible(true);
			this.clearTitleTextFieldButton.setVisible(true);
			this.addButton.setEnabled(false);
			this.deleteButton.setEnabled(false);
			this.cancelButton.setEnabled(true);
			this.exportToPDFButton.setEnabled(false);
			this.editButton.setText("U");
		}
		
		void setDeleteMode()
		{
			if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
			{
				JOptionPane.showMessageDialog(this,"Please select designation to delete");
				return;
			}
			DesignationUI.this.setDeleteMode();
			this.addButton.setEnabled(false);
			this.editButton.setEnabled(false);
			this.cancelButton.setEnabled(false);
			this.exportToPDFButton.setEnabled(false);
			this.deleteButton.setEnabled(false);
			deleteDesignation();
			setViewMode();
		}
		
		void setExportToPDFMode()
		{
			DesignationUI.this.setExportToPDFMode();
			this.addButton.setEnabled(false);
			this.editButton.setEnabled(false);
			this.cancelButton.setEnabled(false);
			this.deleteButton.setEnabled(false);
			this.exportToPDFButton.setEnabled(false);
		}
	}//inner class ends
	
}