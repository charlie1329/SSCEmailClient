package guiCode;


import java.awt.FlowLayout;
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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import setup.CreateSession;

/**this class represents the interface and function of sending an email (with attachments)
 * 
 * @author Charlie Street
 *
 */
public class ComposeEmail extends JFrame 
{
	private CreateSession mySession;//all the info I need to send the email
	private ArrayList<String> attachments;//representing file paths of all attachments added
	
	//all gui components
	private JPanel messagePanel;
	
	private JLabel to;
	private JLabel cc;
	private JLabel subject;
	private JLabel attachNames;
	
	private JTextField toField;
	private JTextField ccField;
	private JTextField subjectField;
	
	private JTextArea mainContent;
	
	private JButton attach;
	private JButton send;
	
	
	/**constructor sets up attributes as well as the gui information and set up
	 * 
	 * @param mySession the session being used
	 */
	public ComposeEmail(CreateSession mySession)
	{
		super("Compose New Email");//setting title by calling super constructor
		
		setSize(590,380);//demo size, please change later!!!
		setLocationRelativeTo(null);//centering frame on screen
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		this.mySession = mySession;
		this.attachments = new ArrayList<String>();
		
		//setting up gui components
		
		this.messagePanel = new JPanel();//creating main panel
		
		JPanel toPanel = new JPanel();//panel grouping the to part of email together
		this.to = new JLabel("To:       ");//extra spaces to line up
		this.toField = new JTextField(45);
		toPanel.add(this.to);
		toPanel.add(this.toField);
		
		JPanel ccPanel = new JPanel();//grouping cc parts of email together
		this.cc = new JLabel("CC:       ");
		this.ccField = new JTextField(45);
		ccPanel.add(this.cc);
		ccPanel.add(this.ccField);
		
		JPanel subjectPanel = new JPanel();//grouping subject label and field together
		this.subject = new JLabel("Subject:");
		this.subjectField = new JTextField(45);
		subjectPanel.add(this.subject);
		subjectPanel.add(this.subjectField);
		
		this.attachNames = new JLabel("Attachments: ");
		
		this.attach = new JButton("Attach");//button for attachments
		this.attach.addActionListener(e -> attach());//attachment method binded to button
		
		
		this.mainContent = new JTextArea(10,52);//add dimensions in if necessary!	
		
		this.send = new JButton("Send");//send button will try to send email
		this.send.addActionListener(e -> this.send());
		
		//adding to panel now
		this.messagePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));//right align
		this.messagePanel.add(toPanel);
		this.messagePanel.add(ccPanel);
		this.messagePanel.add(subjectPanel);
		this.messagePanel.add(this.attachNames);
		this.messagePanel.add(this.attach);
		this.messagePanel.add(this.mainContent);
		this.messagePanel.add(this.send);
		
		getContentPane().add(this.messagePanel);
		setVisible(true);//displaying frame
	}
	
	/**this method will attach a file selected from a file explorer to the attachments list
	 * 
	 */
	private void attach()
	{
		JFileChooser choose = new JFileChooser((String)null);//creating a new file explorer
		choose.showDialog(this,"Attach");//opening
		if(choose.getSelectedFile() != null)//if x pressed
		{
			File fileToAttach = choose.getSelectedFile();
			this.attachNames.setText(this.attachNames.getText() + " " + fileToAttach.getName());//changing attachments label
			this.attachments.add(fileToAttach.getAbsolutePath());//adds absolute path to the attachments list
		}
		
	}
	
	/**this method will get all the components of the message (except attachments which are already in message by now)
	 * this means the email is well formed enough such that it can be sent
	 * @throws RuntimeExcpetion, will be picked up elsewhere
	 * @throws MessagingException will be caught elsewhere
	 */
	private MimeMessage formMessage() throws RuntimeException, MessagingException
	{
		MimeMessage toSend = new MimeMessage(this.mySession.getSession());//creating new message
		
		try
		{
			toSend.setFrom(new InternetAddress("charliestreet1329@gmail.com"));//setting from 
			
			MimeMultipart multiPart = new MimeMultipart();//will contain all parts of the email
			
			String mainRecipient = this.toField.getText();
			toSend.addRecipient(Message.RecipientType.TO, new InternetAddress(mainRecipient));//adding main recipient
			
			String[] ccRecipient = this.ccField.getText().split(" ");//split by spaces
			if(!ccRecipient[0].equals(""))//ie not empty
			{
				for(String cc: ccRecipient)//looping through al cc recipients
				{
					toSend.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));//adding all cc to email
				}
			}
			
			String subject = this.subjectField.getText();
			if(subject.equals(""))//if empty
			{
				int decision = JOptionPane.showConfirmDialog(this, "Do you want an empty subject? If no, please change.","Subject",JOptionPane.YES_NO_OPTION);
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
			
			String content = this.mainContent.getText();//getting main content text
			
			MimeBodyPart mainBody = new MimeBodyPart();//body part for email content
			
			if(content.equals(""))//if nothing specified
			{
				int decision = JOptionPane.showConfirmDialog(this, "Blank message will be sent? If no, please change.","Body",JOptionPane.YES_NO_OPTION);
				if(decision != JOptionPane.YES_OPTION)//if 
				{
					throw new RuntimeException("Bad content");//stop process if they want to change
				}
			}
			
			mainBody.setText(content);//setting this body part
			
			multiPart.addBodyPart(mainBody);//adding main body to email
			
			//now dealing with any attachments
			for(String toAttach : this.attachments)//looping through file paths
			{
					MimeBodyPart myAttachment = new MimeBodyPart();//new body part
					
					DataSource source = new FileDataSource(toAttach);//process of adding the file
					File current = new File(toAttach);//getting file object to get name
					myAttachment.setDataHandler(new DataHandler(source));
					myAttachment.setFileName(current.getName());
					
					multiPart.addBodyPart(myAttachment);//adding to mime multipart
				
			}
			
			toSend.setContent(multiPart);//adding multipart to the email
			toSend.saveChanges();
			
		}
		catch(AddressException e)//if address error
		{
			JOptionPane.showMessageDialog(this, "Error with recipient (main or cc).", "Recipient",JOptionPane.ERROR_MESSAGE);
		}
		
		return toSend;	//returning the message
	}
	
	/**this method will attempt to send the email
	 * if it fails then the error will be caught and an error message will be popped up
	 */
	private void send()
	{
		try//try sending
		{	
			MimeMessage toSend = this.formMessage();//finally creating the message to send
			this.mySession.getTransport().sendMessage(toSend, toSend.getAllRecipients());//send the message
			JOptionPane.showMessageDialog(this,"Email successfully sent.","Email Sent",JOptionPane.INFORMATION_MESSAGE);//confirmation pop up
			
			setVisible(false);
			dispose();//getting rid of the JFrame after email successfully sent
		}
		catch(MessagingException e)//if unable to send email
		{
			//e.printStackTrace();//for testing
			JOptionPane.showMessageDialog(this, "This email was unable to send. Sorry for the inconvenience.", "Error Sending Email", JOptionPane.ERROR_MESSAGE);
		}
		catch(RuntimeException e)//if dodgy email formation from form message
		{
			//do nothing just exit method allows user to try again
		}
	}
}
