package contactsPackage;

/* Creator: Brandon Smith
 * Name: Contacts
 * Type: abstract Class
 * Purpose: Define the elements essential to all contact types.
 * Notes: Self defined tree structure.
 * 		  All API are protected so that only elements inside of the package can use them.
 */
public abstract class Contacts {
	private String name;
	private String phone;
	private Contacts right = null;
	private Contacts middle = null;
	private Contacts left = null;
	
	/* Creator: Brandon Smith
	 * Name: Contacts
	 * Type: Constructor
	 * Purpose: Initialize any one given Contacts with a name and phone number.
	 * Arguments: String name, String phone.
	 * Returns: N/A
	 */
	protected Contacts(String name, String phone)
	{
		this.name = name;
		this.phone = phone;
	}
	
	/* Creator: Brandon Smith
	 * Name: getName
	 * Type: getterMethod (Package API)
	 * Purpose: Get the name of the Contacts.
	 * Arguments: N/A
	 * Returns: String name: this instances name.
	 */
	protected String getName()
	{
		return this.name;
	}
	
	/* Creator: Brandon Smith
	 * Name: getInfo
	 * Type: getterMethod (Package API)
	 * Purpose: Get all of the info of the Contacts.
	 * Arguments: N/A
	 * Returns: String[] {name, phone}: this instances name and phone number.
	 */
	protected String[] getInfo()
	{
		return new String[] {this.name, this.phone};
	}
	
	/* Creator: Brandon Smith
	 * Name: setContactRight
	 * Type: setterMethod (Package API)
	 * Purpose: Set the right (tree structure) Contacts to the given contact.
	 * Arguments: Contacts contact.
	 * Returns: N/A
	 */
	protected void setContactRight(Contacts contact)
	{
		this.right = contact;
	}
	
	/* Creator: Brandon Smith
	 * Name: getContactRight
	 * Type: getterMethod (Package API)
	 * Purpose: Get the right (tree structure) Contacts of the current instance of Contacts.
	 * Arguments: N/A
	 * Returns: Contacts: right (tree structure) Contacts.
	 */
	protected Contacts getContactRight()
	{
		return this.right;
	}
	
	/* Creator: Brandon Smith
	 * Name: setContactMiddle
	 * Type: setterMethod (Package API)
	 * Purpose: Set the middle (tree structure) Contacts to the given contact.
	 * Arguments: Contacts contact.
	 * Returns: N/A
	 */
	protected void setContactMiddle(Contacts contact)
	{
		this.middle = contact;
	}
	
	/* Creator: Brandon Smith
	 * Name: getContactMiddle
	 * Type: getterMethod (Package API)
	 * Purpose: Get the middle (tree structure) Contacts of the current instance of Contacts.
	 * Arguments: N/A
	 * Returns: Contacts: middle (tree structure) Contacts.
	 */
	protected Contacts getContactMiddle()
	{
		return this.middle;
	}
	
	/* Creator: Brandon Smith
	 * Name: setContactMiddle
	 * Type: setterMethod (Package API)
	 * Purpose: Set the middle (tree structure) Contacts to the given contact.
	 * Arguments: Contacts contact.
	 * Returns: N/A
	 */
	protected void setContactLeft(Contacts contact)
	{
		this.left = contact;
	}
	
	/* Creator: Brandon Smith
	 * Name: getContactLeft
	 * Type: getterMethod (Package API)
	 * Purpose: Get the left (tree structure) Contacts of the current instance of Contacts.
	 * Arguments: N/A
	 * Returns: Contacts: left (tree structure) Contacts.
	 */
	protected Contacts getContactLeft()
	{
		return this.left;
	}
}
