package useful_ops;

import java.io.IOException;
import java.util.Enumeration;


import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import javax.mail.search.SearchTerm;

/**this class will provide the functionality for searching through the subject and contents of email
 * it does this by extending the SearchTerm abstract class
 * @author Charlie Street
 *
 */
public class SearchEmailTerm extends SearchTerm
{
	private String toMatch;
	private FlagAndFilter flag;//needed also 
	
	/**this constructor initialises the matching term for this search term
	 * 
	 * @param toMatch the search term as a string
	 * @param anyChanged if any flags have been changed recently
	 * @param mySesion containing the necessary hash map
	 */
	public SearchEmailTerm(String toMatch, boolean anyChanged, CreateSession mySession)
	{
		super();//calling super constructor
		this.toMatch = toMatch;
		this.flag = new FlagAndFilter(anyChanged, mySession);
	}
	
	/**this method searches through the email and then returns true
	 * if the searchTerm content specified as a field is included in the subject or the body of the email
	 * @param message the message to match against
	 */
	public boolean match(Message message) 
	{
		return this.flag.match(message) && (this.checkHeaderFromSubject(message) || this.checkBody(message) || this.checkHeaderDetails(message));//carrying out checks
		//flag needs to return true if we are to display the message
	}
	
	/**this method will check the contents of the message for the string matcher
	 * returns default value of false in case of error
	 * @param message the message being checked
	 * @return a boolean saying if the string was found
	 */
	private boolean checkBody(Message message)
	{
		try//try to get all of the content from message
		{
			String content = ReadContents.readMessage(message);//read in the message
			return content.contains(this.getMatcher());//check if contained in contents
		}
		catch(IOException|MessagingException e)//return default in case of error
		{
			return false;
		}
	}
	
	/**this method will check the header of an email (from and subject)to look for the search term
	 * 
	 * @return a boolean specifying whether the matching term is found in the header of the email
	 * @param message the message being checked
	 */
	private boolean checkHeaderFromSubject(Message message) 
	{
		try//try to check message
		{
			boolean from = message.getFrom()[0].toString().contains(this.getMatcher());//getting both possible checks
			boolean sub;
			
			if(message.getSubject() == null)//got to be careful of null pointers
			{
				sub = false;//trivially not in if null
			}
			else
			{
				sub = message.getSubject().contains(this.getMatcher());
			}
			
			return from || sub;
		}
		catch(MessagingException e)//if error occurs during checks it will return a default of false
		{
			return false;
		}
		
	}
	
	/**this method will go through all the headers of the email to check for the matcher in them
	 * 
	 * @param message the message being checked
	 * @return whether the matcher is contained in the headers
	 */
	private boolean checkHeaderDetails(Message message)
	{
		try
		{
			Enumeration headers = message.getAllHeaders();//getting all headers
			
			while(headers.hasMoreElements())//looping through each header
			{
				Header head = (Header) headers.nextElement();
				if(head.getValue().contains(this.getMatcher()))//checking for matcher
				{
					return true;
				}
			}
		}
		catch(MessagingException e)//in this scenario I wanted to return a default value
		{
			return false;
		}
		
		return false;//if not found in header
	}
	
	/**returns the string representing the matching pattern
	 * 
	 * @return the matching string
	 */
	private String getMatcher()
	{
		return this.toMatch;
	}
}
