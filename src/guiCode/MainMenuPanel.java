package guiCode;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.mail.MessagingException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import useful_ops.CreateSession;

/**this class will be the main interface for the email client
 * 
 * @author Charlie Street
 *
 */
public class MainMenuPanel extends JPanel
{
	private CreateSession mySession;//all the information I need for this part of the program
	private JFrame parent;
	private String currentFolder;
	private JButton currentButton;//to keep track of enabled buttons
	
	//all window components
	private DisplayMessages messages;
	
	private JButton inbox;
	private JButton sent;
	private JButton drafts;
	private JButton compose;
	private JButton refresh;
	private JButton search;
	private JTextField searchBox;
	private JButton createFlag;
	private JButton logOut;
	private JScrollPane scrollMessages;
	
	
	/**the constructor here initialises all information crucial to the running of the program
	 * it also sets up all of the gui information
	 * @param mySession
	 * @param userName
	 * @param password
	 */
	public MainMenuPanel(CreateSession mySession, JFrame parent)
	{
		this.mySession = mySession;
		this.parent = parent;
		this.currentFolder = "inbox";//program will always start with inbox as folder
		this.messages = new DisplayMessages(this.mySession, this.currentFolder);//will create a panel of messages for me
		
		//set up for gui components
		
		//firstly the folder related buttons
		this.inbox = new JButton("Inbox");
		this.inbox.addActionListener(e -> this.displayNewFolder(this.inbox,"inbox"));
		
		this.sent = new JButton("Sent");
		this.sent.addActionListener(e -> this.displayNewFolder(this.sent,"[Gmail]/Sent Mail"));//gmail has an odd folder system
				
		this.drafts = new JButton("Drafts");
		this.drafts.addActionListener(e -> this.displayNewFolder(this.drafts, "[Gmail]/Drafts"));
				
		this.currentButton = this.inbox;//i don't want the current button to be enabled
		this.currentButton.setEnabled(false);
				
		this.refresh = new JButton("Refresh");
		this.refresh.addActionListener(e -> this.refreshMessages());
		
		JPanel foldersPanel = new JPanel();//will display all folder related items
		GridLayout grid = new GridLayout(4,1);//layout for folders
		grid.setVgap(20);
		foldersPanel.setLayout(grid);
		
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);//setting border for panel
		TitledBorder title = BorderFactory.createTitledBorder(etched, "Folders");
		title.setTitlePosition(TitledBorder.TOP);
		foldersPanel.setBorder(title);
		
		foldersPanel.add(this.inbox);//adding all necessary components
		foldersPanel.add(this.drafts);
		foldersPanel.add(this.sent);
		foldersPanel.add(this.refresh);
		
		JPanel folderCover = new JPanel();//new panel to prevent stretching in border layout
		folderCover.add(foldersPanel);
		
		this.compose = new JButton("Compose Email");
		this.compose.addActionListener(e -> this.composeEmail());//will compose a new email
		
		this.search = new JButton();//to carry out search on messages
		ImageIcon searchImg = new ImageIcon("jar and images/search.png");
		this.search.setIcon(searchImg);
		this.search.addActionListener(e -> this.searchMessages());
		
		this.searchBox = new JTextField(20);
		
		JPanel searchPanel = new JPanel();//want these two components kept together as they have to be combined to carry out functionality
		searchPanel.add(this.searchBox);
		searchPanel.add(this.search);
		
		this.createFlag = new JButton();//lets user create custom flag
		ImageIcon flag = new ImageIcon("jar and images/flag.png");//adding image to picture
		this.createFlag.setIcon(flag);
		//insert action listener here once written
		
		this.logOut = new JButton("Log Out");//logs user out of account
		this.logOut.addActionListener(e -> logOut());
		JPanel logPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));//extra panel to prevent stretching
		logPanel.add(this.logOut);
		
		this.scrollMessages = new JScrollPane();//scroll pane for the messages shown
		this.scrollMessages.setViewportView(this.messages);//adding messages to panel
		this.scrollMessages.revalidate();
		this.scrollMessages.repaint();

		JPanel topBar = new JPanel();//panel for top operations
		topBar.add(this.createFlag);
		topBar.add(searchPanel);
		topBar.add(this.compose);
		
		setLayout(new BorderLayout());//setting layout
		
		add(topBar,BorderLayout.NORTH);//adding main components
		add(folderCover,BorderLayout.WEST);
		add(this.scrollMessages,BorderLayout.CENTER);
		add(logPanel,BorderLayout.SOUTH);
		
		
	}
	
	/**this method will change the current folder and then change the displayed messages on screen
	 * it also changes which buttons are enabled
	 * @param newFolder the new folder name (inbox, sent or drafts)
	 */
	private void displayNewFolder(JButton newButton, String newFolder)
	{
		this.currentFolder = newFolder;//changing folder name
		this.messages.displayAll(this.currentFolder,false,null);
		
		this.currentButton.setEnabled(true);//re-enabling old button
		this.currentButton = newButton;
		this.currentButton.setEnabled(false);//disabling the new button
		
	}
	
	/**this method will refresh the messages in the current folder being displayed
	 * 
	 */
	private void refreshMessages()
	{
		this.messages.displayAll(this.currentFolder,false,null);//recall the message displaying method
	}
	
	/** this method will carry out a search for the user and then display the search results
	 * 
	 */
	private void searchMessages()
	{
		String searchTerm = this.searchBox.getText();//get users term
		this.currentButton.setEnabled(true);//set current to be clickable again so that you can go back to standard folder
		this.messages.displayAll(this.currentFolder, true, searchTerm);//re display messages
	}
	
	/**method for use when compose button pressed
	 * 
	 */
	private void composeEmail()
	{
		ComposeEmail compose = new ComposeEmail(this.mySession);//new jframe being opened
	}
	
	/**this method will log the user out of their email account and take them back to the user log in page
	 * 
	 */
	private void logOut()
	{
		try//try to log out
		{
			this.mySession.closeItAll();
		}
		catch(MessagingException e)//if there is trouble logging out
		{
			JOptionPane.showMessageDialog(this.parent, "There has been an error logging out. Sorry for the inconvenience.", "Logging out.", JOptionPane.ERROR_MESSAGE);
		}
		
		this.parent.getContentPane().removeAll();//removing everything from the frame
		this.parent.getContentPane().revalidate();
		this.parent.getContentPane().repaint();
		
		LogInPanel newLogIn = new LogInPanel(this.parent);//adding a new log in panel
		this.parent.getContentPane().add(newLogIn);
	}
	
}
