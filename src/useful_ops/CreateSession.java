package useful_ops;

import java.awt.Component;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.sun.mail.imap.IMAPFolder;

/**the aim of this class is to create a session for use within the entire email client
 * 
 * @author Charlie Street
 *
 */
public class CreateSession
{
	private Session session;
	private Transport transport;
	private Store store;
	private String userName;
	private final String SMTPHOST;
	
	/**takes a user name and password and creates a session
	 * using smtp and imaps in a single session
	 * @param userName the email userName
	 * @param password the account password
	 * @throws MessagingException may be thrown if incorrect credentials. Will be picked up by gui
	 */
	public CreateSession(String userName, String password) throws MessagingException, AuthenticationFailedException
	{
		this.SMTPHOST = "smtp.gmail.com";//using gmail as SMTP host
		this.userName = userName;
		
		Properties properties = System.getProperties();//setting correct properties
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", this.SMTPHOST);
		properties.put("mail.smtp.port", "587");
		properties.setProperty("mail.user", userName);//specifying the email address and password
		properties.setProperty("mail.password", password);
		
		properties.setProperty("mail.store.protocol","imaps");//setting up imaps protocol at same time
		
		this.session = Session.getDefaultInstance(properties);//establishing the session
		
		this.store = this.session.getStore("imaps");
		this.store.connect("imap.googlemail.com",userName,password);//connecting to store
		
		//opening here to check for email and password
		this.transport = this.session.getTransport("smtp");
		this.transport.connect(this.SMTPHOST,userName, password);//only way to check if correct is to use it
		//in this case I may as well keep it open for sending email's across the application
	}
	
	/**returns the session object for use within program
	 * 
	 * @return the current session
	 */
	public Session getSession()
	{
		return this.session;
	}
	
	/**returns the transport object for use by application
	 * 
	 * @return the transport object
	 */
	public Transport getTransport()
	{
		return this.transport;
	}
	
	/**returns the store object being used
	 * 
	 * @return the store for the email account
	 */
	public Store getStore()
	{
		return this.store;
	}
	
	/**returns the user name using the program at a given time
	 * 
	 * @return the user name (or address)
	 */
	public String getUserName()
	{
		return this.userName;
	}
	
	/**this method opens a specified folder from the store for the user
	 * it then gives back all the messages from said store
	 * @param folder the folder name being connected to
	 * @param searchWanted will specify whether a search ought to be carried out
	 * @param searchTerm the term to search for (if needed)
	 * @return all the messages of that folder
	 * @throws MessagingException will be thrown then caught by the gui code
	 */
	public Message[] openFolder(String folder, boolean searchWanted, String searchTerm) throws MessagingException
	{
		
		Folder realFolder = (IMAPFolder)this.getStore().getFolder(folder);//getting folder
		
		if(!realFolder.isOpen())//opening the folder
		{
			realFolder.open(Folder.READ_WRITE);
		}
		
		if(searchWanted)//search (according to documentation this is done on the server)
		{
			SearchEmailTerm searcher = new SearchEmailTerm(searchTerm);
			return realFolder.search(searcher);//returning results of search instead
		}
		else//no search
		{
			return realFolder.getMessages();//returning messages contained in folder
		}
	}
	
	/**closes a single folder in the store
	 * 
	 * @param name the folder name to be closed
	 * @throws MessagingException if something goes wrong (guaranteed to be caught elsewhere)
	 */
	private void closeFolder(String name) throws MessagingException
	{
		if(this.store.getFolder(name).isOpen())//needs to be open first
		{
			this.store.getFolder(name).close(true);
		}
	}
	
	/**this method will close everything for the end of the system
	 * @throws MessagingException will be caught in gui code
	 */
	public void closeItAll() throws MessagingException
	{
		if(store != null)
		{
			closeFolder("inbox");
			closeFolder("[Gmail]/Sent Mail");
			closeFolder("[Gmail]/Drafts");
			this.store.close();
				
		}
			
		if(this.transport != null)//cant close a null object
		{
			this.transport.close();
		}
		
	}
}
