package guiCode;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	 * @param session the session object being used
	 * @param userName the current user name and password
	 * @param password the current password
	 * @param folder the folder being used
	 */
	public DisplayMessages(Session session, String userName, String password, String folder)
	{
		this.session = session;
		this.displayAll(userName, password, folder);//calling as I don't want constructor dealing with exceptions
	}
	
	/**this method opens the folder of emails, then displays it in a standard form of parts for each email
	 * 
	 * @param userName the account address
	 * @param password the account password
	 * @param folder a string representing the folder to open
	 */
	private void displayAll(String userName, String password, String folder)
	{
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
			
			for(Message message: messages)//looping round each message
			{
				JPanel currentEmail = new JPanel();//panel per email
				
				Flags messageFlags = message.getFlags();//getting the flags of the message
				if(messageFlags.contains(Flag.SEEN))//if read make it gray, else white
				{
					currentEmail.setBackground(Color.LIGHT_GRAY);
				}
				else
				{
					currentEmail.setBackground(Color.WHITE);
				}
				
				if(messageFlags.contains(Flag.RECENT))//if a recent message found
				{
					JLabel recent = new JLabel();//new jlabel for image
					ImageIcon recimg = new ImageIcon("jar and images/recent.png");//getting the image
					recent.setIcon(recimg);
					
					currentEmail.add(recent);//adding pic to email panel
				}
				
				
				JLabel from = new JLabel(message.getFrom()[0].toString().split(" ")[0]);//info to display
				JLabel subject = new JLabel((message.getSubject().length() < 30 ? message.getSubject() : message.getSubject().substring(0,27)+"..."));//cutting subject
				
				currentEmail.add(from);//adding to currentPanel
				currentEmail.add(subject);
				currentEmail.setOpaque(true);//allows me to change color
				
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
			currentEmail.setBackground(Color.LIGHT_GRAY);//changing color to seen color
			
		}
		catch(MessagingException e)//if there is an error
		{
			JOptionPane.showMessageDialog(this, "There has been an error retreiving emails. Please try again later","Email error" ,JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
