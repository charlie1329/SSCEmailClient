package useful_ops;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

/**this class represents a search term for the user defined flags
 * 
 * @author Charlie Street
 *
 */
public class FlagAndFilter extends SearchTerm
{
	private CreateSession mySession;
	private boolean anyChanged;//the number of flags removed recently
	
	/**constructor initialises fields
	 * 
	 * @param anyChanged whether flags removed recently
	 * @param mySession the session being used
	 */
	public FlagAndFilter(boolean anyChanged, CreateSession mySession)
	{
		this.mySession = mySession;
		this.anyChanged = anyChanged;
	}
	
	/**this method does a match to see if the flag is already set or the contents contains a string as a keyword
	 * match returns true if the email SHOULD be included in the email set
	 */
	public boolean match(Message message) 
	{
		try
		{
			if(this.mySession.getEntrySet().size() == 0)//if empty (should speed things up a bit)
			{
				return true;
			}
			else if(!anyChanged  && message.isSet(Flags.Flag.USER))//if none changed so we can base upon existing flags
			{
				return !message.getFlags().contains(Flags.Flag.USER);
			}
			else
			{
				Set<Map.Entry<Flags,String>> myFlags = this.mySession.getEntrySet();//getting the entry set
				String body = ReadContents.readMessage(message);//getting body contents
				
				for(Map.Entry<Flags,String> entry : myFlags)//iterating through flags
				{
					if(body.contains(entry.getValue()))//if keyword found
					{
						message.setFlag(Flags.Flag.USER,true);
						return false;//ie don't include
					}
				}
				
				message.setFlag(Flags.Flag.USER,false);//if it reaches here
				return true;
			}
		}
		catch(IOException|MessagingException e)//if something goes wrong
		{
			return true;//default is to include email
		}
	}

}
