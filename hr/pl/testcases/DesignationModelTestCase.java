import java.awt.*;
import javax.swing.*;
import com.thinking.machines.hr.pl.model.*;
public class DesignationModelTestCase extends JFrame
{
	private DesignationModel designationModel;
	private JTable table;
	private Container container;
	private JScrollPane jsp;
	public DesignationModelTestCase()
	{
		designationModel=new DesignationModel();
		this.table=new JTable(designationModel);
		jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		container=getContentPane();
		container.setLayout(new FlowLayout());
		container.add(jsp);
		setLocation(400,500);
		setSize(400,500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String args[])
	{
		DesignationModelTestCase dmtc=new DesignationModelTestCase();
	}
}
