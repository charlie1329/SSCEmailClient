package guiCode;

import javax.mail.MessagingException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import setup.CreateSession;

/**this class will be the main interface for the email client
 * 
 * @author Charlie Street
 *
 */
public class MainMenuPanel extends JPanel
{
	private CreateSession mySession;//all the information I need for this part of the program
	private String userName;
	private String password;
	private JFrame parent;
	private String currentFolder;
	private JButton currentButton;//to keep track of enabled buttons
	
	//all window components
	private DisplayMessages messages;
	private JLabel folders;
	
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
	public MainMenuPanel(CreateSession mySession, String userName, String password, JFrame parent)
	{
		this.mySession = mySession;
		this.userName = userName;
		this.password = password;
		this.parent = parent;
		this.currentFolder = "inbox";//program will always start with inbox as folder
		this.messages = new DisplayMessages(this.mySession, this.userName, this.password, this.currentFolder);//will create a panel of messages for me
		
		//set up for gui components
		
		//firstly the folder related buttons
		this.inbox = new JButton("Inbox");
		this.inbox.addActionListener(e -> this.displayNewFolder(this.inbox,"inbox"));
		
		this.sent = new JButton("Sent");
		this.sent.addActionListener(e -> this.displayNewFolder(this.sent,"[Gmail]/Sent Mail"));
		
		this.drafts = new JButton("Drafts");
		this.drafts.addActionListener(e -> this.displayNewFolder(this.drafts, "[Gmail]/Drafts"));
		
		this.currentButton = this.inbox;//i don't want the current button to be enabled
		this.currentButton.setEnabled(false);
		
		this.compose = new JButton("Compose Email");
		//add action listener once written
		
		this.refresh = new JButton("Refresh");
		this.refresh.addActionListener(e -> this.refreshMessages());
		
		this.search = new JButton();//to carry out search on messages
		ImageIcon searchImg = new ImageIcon("jar and images/search.png");
		this.search.setIcon(searchImg);
		//insert action listener here
		
		this.searchBox = new JTextField();
		
		JPanel searchPanel = new JPanel();//want these two components kept together as they have to be combined to carry out functionality
		searchPanel.add(this.searchBox);
		searchPanel.add(this.search);
		
		this.createFlag = new JButton("Create Custom Flag");//lets user create custom flag
		//insert action listener here once written
		
		this.logOut = new JButton("Log Out");//logs user out of account
		this.logOut.addActionListener(e -> logOut());
		
		this.scrollMessages = new JScrollPane();//scroll pane for the messages shown
		this.scrollMessages.add(this.messages);//adding messages to panel
		
		
		
	}
	
	/**this method will change the current folder and then change the displayed messages on screen
	 * it also changes which buttons are enabled
	 * @param newFolder the new folder name (inbox, sent or drafts)
	 */
	private void displayNewFolder(JButton newButton, String newFolder)
	{
		this.currentFolder = newFolder;//changing folder name
		this.messages.displayAll(this.userName, this.password, this.currentFolder);
		
		this.currentButton.setEnabled(true);//re-enabling old button
		this.currentButton = newButton;
		this.currentButton.setEnabled(false);//disabling the new button
		
	}
	
	/**this method will refresh the messages in the current folder being displayed
	 * 
	 */
	private void refreshMessages()
	{
		this.messages.displayAll(this.userName, this.password, this.currentFolder);//recall the message displaying method
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
		this.parent.add(newLogIn);
	}
	
}
