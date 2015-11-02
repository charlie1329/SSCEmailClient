package setup;

import java.awt.Component;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
		
		Properties properties = System.getProperties();//setting correct properties
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", this.SMTPHOST);
		properties.put("mail.smtp.port", "587");
		properties.setProperty("mail.user", userName);//specifying the email address and password
		properties.setProperty("mail.password", password);
		
		properties.setProperty("mail.store.protocol","imaps");//setting up imaps protocol at same time
		
		this.session = Session.getDefaultInstance(properties);//establishing the session
		
		this.store = null;//if I store it here I can close it easily
		
		//creating a temporary transport object to check email and password
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
			closeFolder("sent");
			closeFolder("drafts");
			this.store.close();
				
		}
			
		if(this.transport != null)//cant close a null object
		{
			this.transport.close();
		}
		
	}
}
