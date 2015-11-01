package guiCode;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.BorderLayout;



public class ReadEmail extends JFrame
{
	private Message message;
	private JLabel fromLabel;
	private JTextArea messageBody;
	private JPanel messagePanel;
	private JLabel subjectLabel;
	private JScrollPane scrollPane;
	
	/**this constructor sets up all of the gui in order to display the message
	 *  
	 */
	public ReadEmail(Message message) 
	{
		super("Read Message");
		
		try
		{
			this.message = message;
			
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//sorting out frame
			setSize(434,262);//setting size
			
			this.messagePanel = new JPanel();
			
			this.fromLabel = new JLabel("From: " + this.message.getFrom()[0].toString());//getting sender
			this.fromLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			
			String content = "";//getting content from email
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
				
				if(content.equals(""))//if nothing to show
				{
					content = "No plain text to display.";
				}
			}
			
			getContentPane().add(this.messagePanel, BorderLayout.NORTH);//setting certain layouts
			
			this.subjectLabel = new JLabel("Subject: " + this.message.getSubject());//subject label set up
			this.subjectLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			
			this.scrollPane = new JScrollPane();//creating the scroll pane
			
			GroupLayout gl_messagePanel = new GroupLayout(messagePanel);
			gl_messagePanel.setHorizontalGroup(//using group layout and setting the horizontal layout
				gl_messagePanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_messagePanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_messagePanel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_messagePanel.createSequentialGroup()
								.addGroup(gl_messagePanel.createParallelGroup(Alignment.LEADING)
									.addComponent(fromLabel, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
									.addComponent(subjectLabel, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE))
								.addGap(175))
							.addGroup(gl_messagePanel.createSequentialGroup()
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 395, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(13, Short.MAX_VALUE))))
			);
			gl_messagePanel.setVerticalGroup(//setting the vertical layout
				gl_messagePanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_messagePanel.createSequentialGroup()
						.addGap(9)
						.addComponent(fromLabel)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(subjectLabel)
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE))
			);
			
			this.messageBody = new JTextArea(content);//setting text area
			scrollPane.setViewportView(messageBody);//adding text area to scroll pane
			this.messageBody.setEditable(false);
			messagePanel.setLayout(gl_messagePanel);//setting message layout to group layout
			
			setLocationRelativeTo(null);//displaying frame
			setVisible(true);
		}
		catch(MessagingException e)//if error reading message
		{
			JOptionPane.showMessageDialog(this, "Error getting message. Please try again.", "Email error",JOptionPane.ERROR_MESSAGE);
		}
		catch(IOException e)//if error elsewhere
		{
			JOptionPane.showMessageDialog(this, "Error getting message. Please try again.", "Email error",JOptionPane.ERROR_MESSAGE);
		}
	}
}
