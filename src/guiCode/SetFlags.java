package guiCode;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.mail.Flags;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import useful_ops.CreateSession;

/**this class is the interface for setting a new flag, it also displays already created flags which the user can remove
 * 
 * @author Charlie Street
 *
 */
public class SetFlags extends JFrame
{
	
	private JLabel name;//gui components
	private JLabel keyword;
	private JTextField nameField;
	private JTextField keywordField;
	private JButton addFlag;
	private JScrollPane definedFlags;
	private JPanel currentFlags;
	private JPanel mainPanel;
	
	private CreateSession mySession;
	private boolean anyChanged;//used for filtering
	
	/**constructor sets up gui information for the class
	 * @param mySession the object used to deal with the creation of user flags
	 */
	public SetFlags(CreateSession mySession,DisplayMessages display,String folder)
	{
		super("Filter/Flags");//title for window
		setSize(400,350);
		setLocationRelativeTo(null);//centering on screen
		
		this.mySession = mySession;
		this.anyChanged = false;
		
		this.name = new JLabel("Flag Name: ");//flag name part of interface
		this.nameField = new JTextField(8);
		JPanel allName = new JPanel();
		allName.add(this.name);
		allName.add(this.nameField);
		
		this.keyword = new JLabel("Keyword: ");//keyword part of interface
		this.keywordField = new JTextField(8);
		JPanel allKey = new JPanel();
		allKey.add(this.keyword);
		allKey.add(this.keywordField);
		
		this.addFlag = new JButton("Add");//submit button for flag
		this.addFlag.addActionListener(e -> addFlag());
		
		this.currentFlags = new JPanel(new GridLayout(0,1));
		this.displayAllFlags();
		
		this.definedFlags = new JScrollPane();//scroll pane for flags
		this.definedFlags.setViewportView(this.currentFlags);
		this.definedFlags.revalidate();
		this.definedFlags.repaint();
		
		this.mainPanel = new JPanel();//organising main panel to be shown
		this.mainPanel.setLayout(new BorderLayout());
		
		JPanel header = new JPanel();//panel for fields
		header.add(allName);
		header.add(allKey);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));//for add button
		buttonPanel.add(this.addFlag);
		
		this.mainPanel.add(header,BorderLayout.NORTH);//adding to main panel
		this.mainPanel.add(this.definedFlags,BorderLayout.CENTER);
		this.mainPanel.add(buttonPanel,BorderLayout.SOUTH);
		
		
		this.addWindowListener(new WindowAdapter(){//closing frame in special way
			public void windowClosing(WindowEvent e)
			{
				dispose();
				boolean change = setInnerChanged();//getting changed value
				if(change)//if changed re-adjust messages
				{
					display.displayAll(folder, false, null,change);
				}
			}
		});
		
		add(this.mainPanel);//adding to frame
		setVisible(true);//displaying the jframe
		
	}
	
	/**this method helps with the anonymous class and give it the value of the changed status
	 * 
	 * @return whether the flags have changed
	 */
	private boolean setInnerChanged()
	{
		return this.anyChanged;
	}
	
	/**this method recreates and fills the jPanel with all flags
	 * 
	 */
	private void displayAllFlags()
	{
		this.currentFlags.removeAll();//getting rid of everything in the panel
		this.currentFlags.repaint();
		this.currentFlags.revalidate();
		
		this.currentFlags.setPreferredSize(new Dimension(300,32 * this.mySession.getEntrySet().size()));
		
		for(Map.Entry<Flags,String> entry : this.mySession.getEntrySet())
		{
			this.createFlagPanel(entry.getKey(), entry.getValue());//creating panel for each pair
		}
		
	}
	
	/**this method creates a panel for a pair of flag/keyword and then adds it to the display
	 * 
	 * @param flag the current flag
	 * @param keyword the associated keyword
	 */
	private void createFlagPanel(Flags flag, String keyword)
	{		
		JPanel currentFlag = new JPanel();
		currentFlag.setLayout(null);
		
		String name = "Name: " + this.mySession.getFlagName(flag);//getting flag name
		JLabel nameLabel = new JLabel(name);
		
		String keywordStr = "Keyword: " + keyword;//getting associated keyword
		JLabel keywordLabel = new JLabel(keywordStr);
		
		JButton remove = new JButton("Remove");//button for removing a flag
		remove.addActionListener(e -> this.removeFlag(flag, remove));
		
		nameLabel.setBounds(15,11,120,15);//setting the bounds
		keywordLabel.setBounds(150,11,120,15);
		remove.setBounds(280,7,80,25);
		
		currentFlag.add(nameLabel);//adding to panel
		currentFlag.add(keywordLabel);
		currentFlag.add(remove);
		
		this.currentFlags.add(currentFlag);//adding to bigger panel
		this.currentFlags.repaint();
		this.currentFlags.revalidate();
	}
	
	/**adds a new flag to the set of user defined flags for the email filter
	 * catches runtime errors for incorrect input
	 */
	private void addFlag()
	{
		try
		{
			String name = this.nameField.getText();//values to add
			String keyword = this.keywordField.getText();
			this.mySession.addFlag(name,keyword);
			this.displayAllFlags();//creating associated panel
			this.anyChanged = true;
		}
		catch(RuntimeException e)//if incorrect input entered
		{
			JOptionPane.showMessageDialog(this, "You need to enter valid strings in both fields.","Flag Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**this method will remove a flag and then remove it from the panel showing them all
	 * 
	 * @param flag the flag to be removed
	 * @param source the source of the click
	 */
	private void removeFlag(Flags flag,JButton source)
	{
		this.mySession.remove(flag);
		this.displayAllFlags();//displaying flags
		this.anyChanged = true;//somethings changed!
	}
}
