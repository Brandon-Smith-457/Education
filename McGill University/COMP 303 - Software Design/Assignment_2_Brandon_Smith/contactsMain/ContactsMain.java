package contactsMain;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import contactsPackage.ContactsApp; //I've separated the main from the ContactsApp object to draw a distinction between the protected Contacts classes and the Public API.

/* Creator: Brandon Smith
 * Name: ContactsMain
 * Type: Class
 * Purpose: Use the main function to create a user interface and instantiate a ContactsApp.
 */
public class ContactsMain {
	public static void main (String[] args)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ContactsApp phoneBook = new ContactsApp();
		
		String inputString = "";		//Temporary variable to store user inputs
		String[] inputStringArr;		//Temporary variable to store user inputs
		String[] outputStringArr;		//Temporary variable to store ContactsApp outputs
		String[][] allContactsTabular;	//Temporary variable to store ContactsApp tabular outputs
		int[] formattingInts;			//Temporary variable to store ContactsApp outputs

		//Menu loop
		while (true)
		{
			System.out.println("1. New Contact\n2. Find Contact\n3. List All\n4. Quit");
			try
			{
				inputString = br.readLine();
				
				//Code for adding a new contact
				if (inputString.equals("1"))
				{
					while (true)
					{
						System.out.println("1. Acquaintance\n2. Business\n3. Friend");
						inputString = br.readLine();
						
						//Adding an Acquaintance
						if (inputString.equals("1"))
						{
							inputStringArr = acquaintanceInfo(br);
							phoneBook.addAcquaintance(inputStringArr);
							break;
						}
						
						//Adding a Business
						else if (inputString.equals("2"))
						{
							inputStringArr = businessInfo(br);
							phoneBook.addBusiness(inputStringArr);
							break;
						}
						
						//Adding a Friend
						else if (inputString.equals("3"))
						{
							inputStringArr = friendInfo(br);
							phoneBook.addFriend(inputStringArr);
							break;
						}
					}
				}
				
				//Code for finding an existing contact
				else if (inputString.equals("2"))
				{
					System.out.println("Please input the name of the contact you wish to find.");
					inputString = br.readLine();
					outputStringArr = phoneBook.findContact(inputString);
					if (outputStringArr != null)
					{
						for (int i = 0; i < outputStringArr.length; i++)
						{
							System.out.println(outputStringArr[i]);
						}
					}
					else
					{
						System.out.println("Not found");
					}
				}
				
				//Code for listing all existing contacts
				else if (inputString.equals("3"))
				{
					inputStringArr = new String[] {"TYPE", "NAME", "PHONE", "ADDRESS", "BIRTHDATE", "BUSINESSNAME"};
					formattingInts = phoneBook.getLargestStringLengths();
					printFormattedRow(inputStringArr, formattingInts);
					allContactsTabular = phoneBook.listAllContacts();
					for (int i = 0; i < allContactsTabular.length; i++)
					{
						printFormattedRow(allContactsTabular[i], formattingInts);
					}
				}
				
				//Code for terminating the program
				else if (inputString.equals("4"))
				{
					br.close();
					return;
				}
			}
			
			catch (IOException e)
			{
				System.out.println("IOException");
			}
			catch (NullPointerException e)
			{
				System.out.println("NullPointerException");
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				System.out.println("ArrayIndexOutOfBoundsException");
			}
			catch (Exception e)
			{
				System.out.println("Unhandled Exception");
			}
		}
	}

	/* Creator: Brandon Smith
	 * Name: acquaintanceInfo
	 * Type: Method
	 * Purpose: Gather the data necessary to instantiate a ContactsAcquaintance.
	 * Arguments: BufferedReader br.
	 * Returns: String[] {name, phone}: user input for both.
	 * Notes: Throws IOException.
	 */
	public static String[] acquaintanceInfo(BufferedReader br) throws IOException
	{
		String name;
		String phone;

		System.out.println("Please input the name of your acquaintance:");
		name = br.readLine();
		System.out.println("Please input the phone number of " + name + ":");
		phone = br.readLine();
		return new String[] {name, phone};
	}

	/* Creator: Brandon Smith
	 * Name: businessInfo
	 * Type: Method
	 * Purpose: Gather the data necessary to instantiate a ContactsBusiness.
	 * Arguments: BufferedReader br.
	 * Returns: String[] {name, phone, address, businessName}: user input for all.
	 * Notes: Throws IOException.
	 */
	public static String[] businessInfo(BufferedReader br) throws IOException
	{
		String name;
		String phone;
		String address;
		String businessName;

		System.out.println("Please input the name of your business relation:");
		name = br.readLine();
		System.out.println("Please input the phone number of " + name + ":");
		phone = br.readLine();
		System.out.println("Please input the address of " + name + "'s business:");
		address = br.readLine();
		System.out.println("Please input the name of " + name + "'s business:");
		businessName = br.readLine();
		return new String[] {name, phone, address, businessName};
	}

	/* Creator: Brandon Smith
	 * Name: FriendInfo
	 * Type: Method
	 * Purpose: Gather the data necessary to instantiate a ContactsFriend.
	 * Arguments: BufferedReader br.
	 * Returns: String[] {name, phone, address, birthdate}: user input for all.
	 * Notes: Throws IOException.
	 */
	public static String[] friendInfo(BufferedReader br) throws IOException
	{
		String name;
		String phone;
		String address;
		String birthdate;

		System.out.println("Please input the name of your friend:");
		name = br.readLine();
		System.out.println("Please input the phone number of " + name + ":");
		phone = br.readLine();
		System.out.println("Please input the address of " + name + ":");
		address = br.readLine();
		System.out.println("Please input the birthdate of " + name + ":");
		birthdate = br.readLine();
		return new String[] {name, phone, address, birthdate};
	}

	/* Creator: Brandon Smith
	 * Name: printFormattedRow
	 * Type: Method
	 * Purpose: Print information is a String[] depending on an array of int[] for formatting.
	 * Arguments: String[] inputStringArr, int[] formattingInts.
	 * Returns: N/A
	 * Notes: formattingInts' length must be >= inputStringArr's length.
	 */
	public static void printFormattedRow(String[] inputStringArr, int[] formattingInts)
	{
		for (int i = 0; i < inputStringArr.length; i++)
		{
			System.out.print(inputStringArr[i]);
			for (int j = 0; j < formattingInts[i] - inputStringArr[i].length() + 1; j++)
			{
				System.out.print(" ");
			}
		}
		System.out.print("\n");
	}
}
