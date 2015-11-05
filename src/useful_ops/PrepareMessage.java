package useful_ops;


import java.io.File;
import java.util.ArrayList;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**this class is used for the preparation and sending of messages
 * 
 * @author Charlie Street
 *
 */
public class PrepareMessage 
{
	private CreateSession mySession;
	private JFrame parent;
	
	/**constructor only initialises attributes
	 * 
	 * @param mySession the session being used
	 * @param parent the parent frame (for error messages)
	 */
	public PrepareMessage(CreateSession mySession, JFrame parent)
	{
		this.mySession = mySession;
		this.parent = parent;
	}
	

	/**this method will get all the components of the message
	 * this means the email is well formed enough such that it can be sent
	 * @param toField the main recipient of the message
	 * @param ccFied the courtesy copies of the message
	 * @param subjectField the subject of the message
	 * @param mainContent the main body of the email
	 * @param attachments file paths of all attachments
	 * @throws RuntimeException, will be picked up elsewhere
	 * @throws MessagingException will be caught elsewhere
	 */
	private MimeMessage formMessage(String toField, String ccField, String subjectField, String mainContent, ArrayList<String> attachments) throws RuntimeException, MessagingException
	{
		MimeMessage toSend = new MimeMessage(this.mySession.getSession());//creating new message
		
		try
		{
			toSend = setHeader(toSend, toField, ccField, subjectField);//setting the email header
			toSend.setContent(formContents(mainContent,attachments));//adding multipart to the email
			toSend.saveChanges();
		}
		catch(AddressException e)//if address error
		{
			JOptionPane.showMessageDialog(this.parent, "Error with recipient (main or cc).", "Recipient",JOptionPane.ERROR_MESSAGE);
		}
		
		return toSend;	//returning the message
	}
	
	/**this method will set up the email header 
	 * 
	 * @param current the message being worked on 
	 * @param toField the main recipient 
	 * @param ccField the cc recipients
	 * @param subjectField the subject of the email
	 * @return the new message with header set
	 * @throws MessagingException will be caught elsewhere
	 * @throws RuntimeException will be caught elsewhere
	 */
	private MimeMessage setHeader(MimeMessage current, String toField, String ccField, String subjectField) throws MessagingException, RuntimeException
	{
		MimeMessage toSend = current;//local version of message
		
		toSend.setFrom(new InternetAddress(this.mySession.getUserName()));//setting from 
		
		String mainRecipient = toField;
		toSend.addRecipient(Message.RecipientType.TO, new InternetAddress(mainRecipient));//adding main recipient
		
		String[] ccRecipient = ccField.split(" ");//split by spaces
		if(!ccRecipient[0].equals(""))//ie not empty
		{
			for(String cc: ccRecipient)//looping through al cc recipients
			{
				toSend.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));//adding all cc to email
			}
		}
		
		String subject = subjectField;
		if(subject.equals(""))//if empty
		{
			int decision = JOptionPane.showConfirmDialog(this.parent, "Do you want an empty subject? If no, please change.","Subject",JOptionPane.YES_NO_OPTION);
			if(decision == JOptionPane.YES_OPTION)//if they want an empty message
			{
				subject = "(no subject)";//default option
			}
			else
			{
				throw new RuntimeException("Bad subject");
			}
		}
		toSend.setSubject(subject);//setting the subject
		
		return toSend;//returning back
	}
	
	
	/**this method produces a mime multi part that contains all the content for the email to be sent
	 * 
	 * @param mainContent the main text of the message
	 * @param attachments the attachment paths
	 * @return the mime multi part with all contents
	 * @throws MessagingException will be caught elsewhere
	 */
	private MimeMultipart formContents(String mainContent, ArrayList<String> attachments) throws MessagingException, RuntimeException
	{
		MimeMultipart multiPart = new MimeMultipart();//will contain all parts of the email
		String content = mainContent;//getting main content text
		
		MimeBodyPart mainBody = new MimeBodyPart();//body part for email content
		
		if(content.equals(""))//if nothing specified
		{
			int decision = JOptionPane.showConfirmDialog(this.parent, "Blank message will be sent? If no, please change.","Body",JOptionPane.YES_NO_OPTION);
			if(decision != JOptionPane.YES_OPTION)//if 
			{
				throw new RuntimeException("Bad content");//stop process if they want to change
			}
		}
		
		mainBody.setText(content);//setting this body part
		
		multiPart.addBodyPart(mainBody);//adding main body to email
		
		//now dealing with any attachments
		for(String toAttach : attachments)//looping through file paths
		{
				MimeBodyPart myAttachment = addAttachment(toAttach);//adding the attachment
				multiPart.addBodyPart(myAttachment);//adding to mime multipart
			
		}
		
		return multiPart;//return with everything added
	}
	
	/**this method makes a body part out of a attachment
	 * 
	 * @param toAttach the file path to attach
	 * @return the body part for the attachment
	 * @throws MessagingException will be caught elsewhere
	 */
	private MimeBodyPart addAttachment(String toAttach) throws MessagingException
	{
		MimeBodyPart myAttachment = new MimeBodyPart();//new body part
		
		DataSource source = new FileDataSource(toAttach);//process of adding the file
		File current = new File(toAttach);//getting file object to get name
		myAttachment.setDataHandler(new DataHandler(source));
		myAttachment.setFileName(current.getName());
		
		return myAttachment;
	}
	
	/** this method attempts to send a message
	 * 
	 * @param toField the main recipient
	 * @param ccField the cc recipients
	 * @param subjectField the email subject
	 * @param mainContentthe main email content
	 * @param attachments the attachments of the email
	 * @throws RuntimeException will be caught in gui code
	 * @throws MessagingException will be caught in gui code
	 */
	public void sendMessage(String toField, String ccField, String subjectField, String mainContent, ArrayList<String> attachments) throws RuntimeException, MessagingException 
	{
		MimeMessage toSend = this.formMessage(toField,ccField,subjectField,mainContent,attachments);//finally creating the message to send
		this.mySession.getTransport().sendMessage(toSend, toSend.getAllRecipients());//send the message
	}
}
