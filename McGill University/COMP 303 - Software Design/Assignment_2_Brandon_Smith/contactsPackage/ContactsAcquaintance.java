package contactsPackage;

/* Creator: Brandon Smith
 * Name: ContactsAcquaintance
 * Type: Class
 * Purpose: Define the elements particular to an Acquaintance contact type.
 * Notes: Extends Contacts.
 * 		  All API are protected so that only elements inside of the package can use them.
 */
public class ContactsAcquaintance extends Contacts {	
	/* Creator: Brandon Smith
	 * Name: ContactsAcquaintance
	 * Type: Constructor
	 * Purpose: Initialize any one given ContactsAcquaintance with a name and phone number.
	 * Arguments: String name, String phone.
	 * Returns: N/A
	 */
	protected ContactsAcquaintance(String name, String phone)
	{
		super(name, phone);
	}
}
