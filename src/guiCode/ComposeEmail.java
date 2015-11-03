package guiCode;

import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
	private String userName;
	private String password;
	private ArrayList<String> attachments;//representing file paths of all attachments added
	
	//all gui components
	private JPanel messagePanel;
	
	private JLabel to;
	private JLabel cc;
	private JLabel subject;
	
	private JTextField toField;
	private JTextField ccField;
	private JTextField subjectField;
	
	private JTextArea mainContent;
	
	private JButton attach;
	private JButton send;
	
	
	/**constructor sets up attributes as well as the gui information and set up
	 * 
	 * @param mySession the session being used
	 * @param userName the account user name being used
	 * @param password the account password being used
	 */
	public ComposeEmail(CreateSession mySession, String userName, String password)
	{
		super("Compose New Email");//setting title by calling super constructor
		
		setSize(450,450);//demo size, please change later!!!
		setLocationRelativeTo(null);//centering frame on screen
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.mySession = mySession;
		this.userName = userName;
		this.password = password;
		this.attachments = new ArrayList<String>();
		
		//setting up gui components
		
		this.messagePanel = new JPanel();//creating main panel
		
		JPanel toPanel = new JPanel();//panel grouping the to part of email together
		this.to = new JLabel("To:      ");//extra spaces to line up
		this.toField = new JTextField();
		toPanel.add(this.to);
		toPanel.add(this.toField);
		
		JPanel ccPanel = new JPanel();//grouping cc parts of email together
		this.cc = new JLabel("CC:      ");
		this.ccField = new JTextField();
		ccPanel.add(this.cc);
		ccPanel.add(this.ccField);
		
		JPanel subjectPanel = new JPanel();//grouping subject label and field together
		this.subject = new JLabel("Subject: ");
		this.subjectField = new JTextField();
		subjectPanel.add(this.subject);
		subjectPanel.add(this.subjectField);
		
		this.mainContent = new JTextArea();//add dimensions in if neccesary!
		
		this.attach = new JButton();//button for attachments
		ImageIcon paperClip = new ImageIcon("jar and images/attach.png");//paperclip image
		this.attach.setIcon(paperClip);
		//attach action listener here
		
		this.send = new JButton("Send");//send button will try to send email
		this.send.addActionListener(e -> this.send());
		
		
		setVisible(true);//displaying frame
	}
	
	/**this method will get all the components of the message (except attachments which are already in message by now)
	 * this means the email is well formed enough such that it can be sent
	 */
	private MimeMessage formMessage()
	{
		return null;//get to, get cc, get subject, get content attachments should already be added
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
			JOptionPane.showMessageDialog(this, "This email was unable to send. Sorry for the inconvenience.", "Error Sending Email", JOptionPane.ERROR_MESSAGE);
		}
	}
}
