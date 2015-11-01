package guiCode;

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
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);//dont want resizable window
		
		LogInPanel logIn = new LogInPanel(mainFrame);//opening log in page
		mainFrame.add(logIn);//adding to jFrame
		
		mainFrame.setLocationRelativeTo(null);//centering on screen
		mainFrame.setVisible(true);
		
	}
}
