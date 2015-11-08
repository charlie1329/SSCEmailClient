package guiCode;


import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import useful_ops.CreateSession;
import useful_ops.PrepareMessage;

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
		
		this.attach = new JButton();//button for attachments
		ImageIcon attachImg = new ImageIcon("jar and images/attach.png");
		this.attach.setIcon(attachImg);
		this.attach.addActionListener(e -> attach());//attachment method binded to button
		
		
		this.mainContent = new JTextArea(10,52);//add dimensions in if necessary!	
		
		this.send = new JButton();//send button will try to send email
		ImageIcon sendIcon = new ImageIcon("jar and images/send.png");
		this.send.setIcon(sendIcon);
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
	
	
	/**this method will attempt to send the email
	 * if it fails then the error will be caught and an error message will be popped up
	 */
	private void send()
	{
		try//try sending
		{	
			PrepareMessage newMessage = new PrepareMessage(this.mySession,this);
			newMessage.sendMessage(this.toField.getText(), this.ccField.getText(), this.subjectField.getText(),
									this.mainContent.getText(), this.attachments);//try and send message
			
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
