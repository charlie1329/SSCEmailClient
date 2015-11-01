package guiCode;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import setup.CreateSession;

import java.awt.Font;

/**this panel represents the initial log on screen of the email client
 * 
 * @author Charlie Street
 *
 */
public class LogInPanel extends JPanel
{
	private JTextField userField;//all components
	private JPasswordField passwordField;
	private JLabel loginPrompt;
	private JLabel emailLabel;
	private JLabel passLabel;
	private JLabel title;
	private JFrame parent;
	private JButton logButton;
	
	/**this constructor sets up all of the gui for the log in panel part of the application
	 * 
	 * @param parent the parent jframe
	 */
	public LogInPanel(JFrame parent) 
	{
		this.parent = parent;
		
		setLayout(null);//absolute positioning
		
		this.userField = new JTextField();//creating email address field
		userField.setFont(new Font("Tahoma", Font.PLAIN, 22));
		this.userField.setBounds(220, 220, 300, 35);
		add(this.userField);
		
		this.passwordField = new JPasswordField();//email password field
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 22));
		this.passwordField.setBounds(220, 290, 300, 35);
		add(this.passwordField);
		
		this.title = new JLabel("GMail Client");//gmail title label
		this.title.setFont(new Font("Tahoma", Font.PLAIN, 43));
		this.title.setHorizontalAlignment(SwingConstants.CENTER);
		this.title.setBounds(140, 20, 300, 50);
		add(this.title);
		
		this.loginPrompt = new JLabel("Please log in:");//prompt label
		this.loginPrompt.setFont(new Font("Tahoma", Font.PLAIN, 29));
		this.loginPrompt.setHorizontalAlignment(SwingConstants.LEADING);
		this.loginPrompt.setBounds(40, 160, 300, 50);
		this.add(this.loginPrompt);
		
		this.emailLabel = new JLabel("Email:");//email label
		this.emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		this.emailLabel.setBounds(40, 220, 200, 35);
		add(this.emailLabel);
		
		this.passLabel = new JLabel("Password:");//password label
		this.passLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		this.passLabel.setBounds(40, 290, 200, 35);
		add(this.passLabel);
		
		this.logButton = new JButton("Login");//login button
		this.logButton.setFont(new Font("Tahoma", Font.PLAIN, 28));
		this.logButton.setBounds(370,370,150,50);
		this.logButton.addActionListener(e -> login());//setting action listener
		add(this.logButton);
		
	}
	
	
	/**method will try to log user in to email account
	 * 
	 */
	public void login()
	{
		try
		{
			String address = this.userField.getText();//getting login credentials
			String password = new String(this.passwordField.getPassword());//real password
			
			CreateSession mySession = new CreateSession(address,password);//creating a session object
			destroy(mySession.getSession(),address,password);//next part of program
		}
		catch(AuthenticationFailedException e)//if bad details entered
		{
			JOptionPane.showMessageDialog(this.parent, "Login credentials incorrect. Please try again.","Incorrect credentials" ,JOptionPane.ERROR_MESSAGE);
		}
		catch(MessagingException e)//for other errors connecting
		{
			JOptionPane.showMessageDialog(this.parent, "Error logging in. Please try again later.","Email error" ,JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**this method will destroy the panel and then open up an instance of the main menu
	 * @param mySession the current session object
	 * @param userName the account user name
	 * @param password the account password
	 */
	public void destroy(Session mySession, String userName, String password)
	{
		this.parent.getContentPane().remove(this);//removing and repainting
		this.parent.getContentPane().revalidate();
		this.parent.getContentPane().repaint();
		//add main part here!!!
		//test 
		DisplayMessages display = new DisplayMessages(mySession,userName, password, "inbox");
		this.parent.add(display);
	}
}