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
import javax.swing.filechooser.*;
import java.io.*;
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
	private ImageIcon logoIcon;
	private ImageIcon addIcon;
	private ImageIcon deleteIcon;
	private ImageIcon editIcon;
	private ImageIcon cancelIcon;
	private ImageIcon clearIcon;
	private ImageIcon saveIcon;
	private ImageIcon pdfIcon;
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
		logoIcon=new ImageIcon(this.getClass().getResource("/icons/logo_icon.png"));
		addIcon=new ImageIcon(this.getClass().getResource("/icons/add_icon.png"));
		deleteIcon=new ImageIcon(this.getClass().getResource("/icons/delete_icon.png"));
		cancelIcon=new ImageIcon(this.getClass().getResource("/icons/cancel_icon.png"));
		editIcon=new ImageIcon(this.getClass().getResource("/icons/edit_icon.png"));
		clearIcon=new ImageIcon(this.getClass().getResource("/icons/clear_icon.png"));
		pdfIcon=new ImageIcon(this.getClass().getResource("/icons/pdf_icon.png"));
		saveIcon=new ImageIcon(this.getClass().getResource("/icons/save_icon.png"));
		designationModel=new DesignationModel();
		titleLabel=new JLabel("Designations");
		searchLabel=new JLabel("Search");
		searchErrorLabel=new JLabel("");
		searchTextField=new JTextField();
		clearSearchButton=new JButton(clearIcon);
		designationTable=new JTable(designationModel);
		designationPanel=new DesignationPanel();
		scrollPane=new JScrollPane(designationTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		container=getContentPane();
	}
	
	private void setAppearance()
	{
		setIconImage(logoIcon.getImage());
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
			searchTextField.setEnabled(false);
			clearSearchButton.setEnabled(false);
			designationTable.setEnabled(false);
		}
		else
		{
			searchTextField.setEnabled(true);
			clearSearchButton.setEnabled(true);
			designationTable.setEnabled(true);
		}
	}
	
	private void setAddMode()
	{
		this.mode=MODE.ADD;
		searchTextField.setEnabled(false);
		clearSearchButton.setEnabled(false);
		designationTable.setEnabled(false);
	}
	
	private void setEditMode()
	{
		this.mode=MODE.EDIT;
		searchTextField.setEnabled(false);
		clearSearchButton.setEnabled(false);
		designationTable.setEnabled(false);
	}
	
	private void setDeleteMode()
	{
		this.mode=MODE.DELETE;
		searchTextField.setEnabled(false);
		clearSearchButton.setEnabled(false);
		designationTable.setEnabled(false);
	}
	
	private void setExportToPDFMode()
	{
		this.mode=MODE.EXPORT_TO_PDF;
		searchTextField.setEnabled(false);
		clearSearchButton.setEnabled(false);
		designationTable.setEnabled(false);
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
			clearTitleTextFieldButton=new JButton(clearIcon);
			buttonsPanel=new JPanel();
			addButton=new JButton(addIcon);
			editButton=new JButton(editIcon);
			cancelButton=new JButton(cancelIcon);
			deleteButton=new JButton(deleteIcon);
			exportToPDFButton=new JButton(pdfIcon);
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
			String title=titleTextField.getText().trim();
			if(title.length()==0)
			{
				JOptionPane.showMessageDialog(this,"Designation required");
				this.titleTextField.requestFocus();
				return false;
			}
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
			}
			this.titleTextField.requestFocus();
			return false;
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
			d.setCode(this.designation.getCode());
			d.setTitle(title);
			try
			{
				designationModel.update(d);
				int rowIndex=0;
				try
				{
					rowIndex=designationModel.indexOfDesignation(d);
				}catch(BLException blException)
				{
					
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
				int selectedOption=JOptionPane.showConfirmDialog(this,"Delete "+title+" ?","Confirmation Dialog",JOptionPane.YES_NO_OPTION);
				if(selectedOption==JOptionPane.NO_OPTION) return;
				designationModel.remove(this.designation.getCode());
				JOptionPane.showMessageDialog(this,title+" deleted");
				this.clearDesignation();
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
						if(addDesignation()) setViewMode();	
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
				public void actionPerformed(ActionEvent ev)
				{				
						setViewMode();
				}
			});
			
			this.deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev)
				{
					setDeleteMode();
				}
			});
			
			this.exportToPDFButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev)
				{
					JFileChooser fileChooser=new JFileChooser();
					fileChooser.setCurrentDirectory(new File("."));
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
						public boolean accept(File file)
						{
							if(file.isDirectory()) return true;
							if(file.getName().endsWith(".java")) return true;
							return false;
						}
						
						public String getDescription()
						{
							return "Java Files";
						}
					});
					int selectedOption=fileChooser.showSaveDialog(DesignationUI.this);
					if(selectedOption==JFileChooser.APPROVE_OPTION)
					{
						try
						{
							File selectedFile=fileChooser.getSelectedFile();
							String pdfFile=selectedFile.getAbsolutePath();
							if(pdfFile.endsWith(".")) pdfFile+="pdf";
							else if(pdfFile.endsWith(".pdf")==false) pdfFile+=".pdf";
							File file=new File(pdfFile);
							File parent=new File(file.getParent());
							if(parent.exists()==false || parent.isDirectory()==false)
							{
								JOptionPane.showMessageDialog(DesignationUI.this,"Incorrect Path: "+pdfFile);
								return;
							}
							designationModel.exportToPDF(file);
							JOptionPane.showMessageDialog(DesignationUI.this,"Data exported to: "+pdfFile);
						}catch(BLException blException)
						{
							if(blException.hasGenericException())
							{
								JOptionPane.showMessageDialog(DesignationUI.this,blException.getGenericException());
							}
						}
						catch(Exception e)
						{
							System.out.println(e.getMessage());
						}
					}
				}
			});
			this.clearTitleTextFieldButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ev)
				{
					titleTextField.setText("");
				}
			});
		}
		
		void setViewMode()
		{
			DesignationUI.this.setViewMode();
			this.addButton.setIcon(addIcon);
			this.editButton.setIcon(editIcon);
			this.titleTextField.setVisible(false);
			this.titleLabel.setVisible(true);
			this.clearTitleTextFieldButton.setVisible(false);
			this.addButton.setEnabled(true);
			this.cancelButton.setEnabled(false);
			if(designationModel.getRowCount()>0)
			{
				this.editButton.setEnabled(true);
				this.deleteButton.setEnabled(true);
				this.exportToPDFButton.setEnabled(true);
			}
			else
			{
				this.editButton.setEnabled(false);
				this.deleteButton.setEnabled(false);
				this.exportToPDFButton.setEnabled(false);
			}
		}
		
		void setAddMode()
		{
			DesignationUI.this.setAddMode();
			this.titleTextField.setText("");
			this.titleLabel.setVisible(true);
			this.titleTextField.setVisible(true);
			this.titleTextField.requestFocus();
			this.clearTitleTextFieldButton.setVisible(true);
			this.addButton.setIcon(saveIcon);
			cancelButton.setEnabled(true);
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
			exportToPDFButton.setEnabled(false);
		}
		
		void setEditMode()
		{
			if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
			{
				JOptionPane.showMessageDialog(this,"Select designation to edit");
				return;
			}
			DesignationUI.this.setEditMode();
			this.titleTextField.setText(this.designation.getTitle());
			this.titleLabel.setVisible(false);
			this.titleTextField.setVisible(true);
			this.clearTitleTextFieldButton.setVisible(true);
			addButton.setEnabled(false);
			cancelButton.setEnabled(true);
			deleteButton.setEnabled(false);
			exportToPDFButton.setEnabled(false);
			editButton.setIcon(saveIcon);
		}
		
		void setDeleteMode()
		{
			if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
			{
				JOptionPane.showMessageDialog(this,"Select designation to delete");
				return;
			}
			DesignationUI.this.setDeleteMode();
			addButton.setEnabled(false);
			editButton.setEnabled(false);
			cancelButton.setEnabled(false);
			deleteButton.setEnabled(false);
			exportToPDFButton.setEnabled(false);
			deleteDesignation();
			setViewMode();
		}
		
		void setExportToPDFMode()
		{
			DesignationUI.this.setExportToPDFMode();
			addButton.setEnabled(false);
			editButton.setEnabled(false);
			cancelButton.setEnabled(false);
			deleteButton.setEnabled(false);
			exportToPDFButton.setEnabled(false);
		}
			
	}//inner class ends
	
}