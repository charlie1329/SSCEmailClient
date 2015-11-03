package auxilliary;

/**this class acts as a helper for getting the OS information for whichever OS the program is currently running on
 * 
 * @author Charlie Street
 *
 */
public class OSHelper
{
	/**this method will return the operating system in terms of the OS enum
	 * 
	 * @return the operating system the program is being run on
	 */
	public static OS getMyOS()
	{
		String myOS = System.getProperty("os.name");//getting the os name
		
		if(myOS.contains("Windows"))//checking for os names
		{
			return OS.WINDOWS;
		}
		else if(myOS.contains("Linux"))
		{
			return OS.LINUX;
		}
		else if(myOS.contains("Mac"))
		{
			return OS.MAC;
		}
		else
		{
			return OS.DEFAULT;//the error case (will be dealt with elsewhere)
		}
		
	}
}
