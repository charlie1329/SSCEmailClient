package setup;

import java.util.Properties;

import javax.mail.Session;

/**the aim of this class is to create a session for use within the entire email client
 * 
 * @author Charlie Street
 *
 */
public class CreateSession
{
	private Session session;
	private final String SMTPHOST;
	
	/**takes a user name and password and creates a session
	 * using smtp and imaps in a single session
	 * @param userName the email userName
	 * @param password the account password
	 */
	public CreateSession(String userName, String password)
	{
		this.SMTPHOST = "smtp.gmail.com";//using gmail as SMTP host
		
		Properties properties = System.getProperties();//setting correct properties
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", this.SMTPHOST);
		properties.put("mail.smtp.port", "587");
		properties.setProperty("mail.user", userName);
		properties.setProperty("mail.password", password);
		
		properties.setProperty("mail.store.protocol","imaps");//setting up imaps protocol at same time
		
		this.session = Session.getDefaultInstance(properties);//establishing the session
	}
	
	/**returns the session object for use within program
	 * 
	 * @return the current session
	 */
	public Session getSession()
	{
		return this.session;
	}
}
