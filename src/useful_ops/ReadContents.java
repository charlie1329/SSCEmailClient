package useful_ops;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

/**this class is used to provide code which reads in a message
 * and provides a string representation of the email
 * @author Charlie Street
 *
 */
public class ReadContents 
{
	/**this static method will convert a message into a string consisting of all
	 * of the plain text contents of the message
	 * @param message the message to convert
	 * @return the string of the contents
	 * @throws MessagingException will be caught by gui
	 * @throws IOException will be caught by gui
	 */
	public static String readMessage(Message message) throws MessagingException,IOException
	{
		String content = "";//where email content is stored
		
		if(message.getContentType().contains("TEXT/PLAIN"))//if small message
		{
			content = (String)message.getContent();
		}
		else//if multipart message with plain text
		{
			Multipart multi = (Multipart)message.getContent();
			for(int i = 0; i < multi.getCount(); i++)//going through each body part
			{
				BodyPart body = multi.getBodyPart(i);
				if(body.getContentType().contains("TEXT/PLAIN"))//checking if plain text involved
				{
					content += (String)body.getContent();
				}
			}
		}	
		return content;//returning value
	}
	
	/**having to write this out a lot was tedious so I put it in a more convenient method
	 * 
	 * @param message the message being used
	 * @return the main sender of the message
	 * @throws MessagingException will be caught outside in gui code
	 */
	public static String getMainSender(Message message) throws MessagingException
	{
		return message.getFrom()[0].toString();
	}
}
