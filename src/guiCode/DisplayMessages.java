package guiCode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import setup.CreateSession;

import com.sun.mail.imap.IMAPFolder;

/**this class represents the displaying of the emails on the scroll pane
 * 
 * @author Charlie Street
 *
 */
public class DisplayMessages extends JPanel
{
	private Session session;
	private Store store;
	private IMAPFolder folder;
	
	/**constructor will create panel of messages, using specified folder and imaps protocol
	 * 
	 * @param mySession the CreateSession object being used
	 * @param userName the current user name and password
	 * @param password the current password
	 * @param folder the folder being used
	 */
	public DisplayMessages(CreateSession mySession, String userName, String password, String folder)
	{
		this.session = mySession.getSession();
		this.store = mySession.getStore();
		this.displayAll(userName, password, folder);//calling as I don't want constructor dealing with exceptions
		
		GridLayout newGrid = new GridLayout(0,1);//setting to grid
		setLayout(newGrid);
	}
	
	/**this method opens the folder of emails, then displays it in a standard form of parts for each email
	 * 
	 * @param userName the account address
	 * @param password the account password
	 * @param folder a string representing the folder to open
	 */
	public void displayAll(String userName, String password, String folder)
	{
		removeAll();//remove everything already in panel
		repaint();
		revalidate();
		
		try
		{
			this.store = this.session.getStore("imaps");
			this.store.connect("imap.googlemail.com",userName,password);//connecting to store
			
			this.folder = (IMAPFolder)this.store.getFolder(folder);//getting folder
			
			if(!this.folder.isOpen())//opening the folder
			{
				this.folder.open(Folder.READ_WRITE);
			}
			
			Message[] messages = this.folder.getMessages();//getting all messages
			
			setPreferredSize(new Dimension(this.getWidth(),messages.length * 30));//setting size of panel for scroll bar
			
			for(Message message: messages)//looping round each message
			{
				JPanel currentEmail = new JPanel();//panel per email
				currentEmail.setLayout(null);//allows sime absolute positioning
				currentEmail.setOpaque(true);//allows me to change colour
				
				Flags messageFlags = message.getFlags();//getting the flags of the message
				if(messageFlags.contains(Flag.SEEN))//if read make it gray, else white
				{
					currentEmail.setBackground(new Color(227,227,227));
				}
				else
				{
					currentEmail.setBackground(Color.WHITE);
				}
				
				JLabel recent = new JLabel();//new jlabel for image if necessary(needs to be here for grid layout)
				if(messageFlags.contains(Flag.RECENT))//if a recent message found
				{
					ImageIcon recimg = new ImageIcon("jar and images/recent.png");//getting the image
					recent.setIcon(recimg);
				}
				
				String sender = message.getFrom()[0].toString().split(" ")[0];//sender of email
				if(sender.length() > 20)//cutting down if necessary
				{
					sender = sender.substring(0,17)+"...";
				}
				
				JLabel from = new JLabel(sender);//info to display
				
				String sub = message.getSubject();//getting subject
				if(sub == null)//no subject
				{
					sub = "(no subject)";
				}
				
				JLabel subject = new JLabel((sub.length() < 40 ? sub : sub.substring(0,37)+"..."));//cutting subject
				
				from.setBounds(10,7,130,15);//setting bounds of labels
				subject.setBounds(170,7,260,15);
				recent.setBounds(420,7,15,15);
				
				currentEmail.add(from);//adding parts to current email
				currentEmail.add(subject);
				currentEmail.add(recent);//adding pic to email panel
				
				
				currentEmail.setBorder(BorderFactory.createDashedBorder(Color.BLACK));//setting border on panel
				
				currentEmail.addMouseListener(new MouseAdapter() {//adding mouse events to the labels
				      public void mouseClicked(MouseEvent e) {
				    	  readMessage(message,currentEmail);//message displayed on click
				      }
				});
				
				add(currentEmail);//adding to panel
			}
			
		}
		catch(MessagingException e)//if error retrieving emails
		{
			JOptionPane.showMessageDialog(this, "There has been an error retreiving emails. Please try again later","Email error" ,JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**this method will read an email and set it to seen 
	 * 
	 * @param message the message being dealt with
	 */
	private void readMessage(Message message, JPanel currentEmail)
	{
		try//try to open new email
		{
			ReadEmail read = new ReadEmail(message);//opens a new window
			message.setFlag(Flag.SEEN, true);//setting to seen
			currentEmail.setBackground(new Color(227,227,227));//changing colour to seen colour
			
		}
		catch(MessagingException e)//if there is an error
		{
			JOptionPane.showMessageDialog(this, "There has been an error retreiving emails. Please try again later","Email error" ,JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
