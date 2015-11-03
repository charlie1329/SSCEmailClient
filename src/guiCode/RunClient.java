package guiCode;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**this class will actually run the email client
 * 
 * @author Charlie Street
 *
 */
public class RunClient
{
	
	/**main method creates a frame with a login panel and then sets to visible
	 * so program can start
	 * @param args command line arguments (none)
	 */
	public static void main(String[] args)
	{

		JFrame mainFrame = new JFrame("Gmail Client");
		mainFrame.setSize(580, 650);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.setResizable(false);//dont want resizable window
		
		LogInPanel logIn = new LogInPanel(mainFrame);//opening log in page
		mainFrame.add(logIn);//adding to jFrame
		
		mainFrame.addWindowListener(new WindowAdapter(){//closing all parts of connection on exit
			public void windowClosing(WindowEvent e)
			{
					try
					{
						logIn.closeAll();
					}
					catch(RuntimeException f)//in case not yet properly set up i.e not logged in
					{
						System.exit(0);//just exit the system as wished
					}
				
				System.exit(0);//closing system down
			}
		});
		
		mainFrame.setLocationRelativeTo(null);//centering on screen
		mainFrame.setVisible(true);
		
	}
}
