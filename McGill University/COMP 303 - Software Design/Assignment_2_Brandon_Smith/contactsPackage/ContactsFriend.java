package contactsPackage;

/* Creator: Brandon Smith
 * Name: ContactsFriend
 * Type: Class
 * Purpose: Define the elements particular to an Friend contact type.
 * Notes: Extends Contacts.
 * 		  All API are protected so that only elements inside of the package can use them.
 */
public class ContactsFriend extends Contacts {
	private String address;
	private String birthdate;

	/* Creator: Brandon Smith
	 * Name: ContactsFriend
	 * Type: Constructor
	 * Purpose: Initialize any one given ContactsFriend with a name, phone number, address, and birthdate.
	 * Arguments: String name, String phone, String address, String birthdate.
	 * Returns: N/A
	 */
	protected ContactsFriend(String name, String phone, String address, String birthdate)
	{
		super(name, phone);
		this.address = address;
		this.birthdate = birthdate;
	}

	/* Creator: Brandon Smith
	 * Name: getInfo
	 * Type: getterMethod (Package API)
	 * Purpose: Get all of the info of the ContactsFriend.
	 * Arguments: N/A
	 * Returns: String[] {name, phone, address, birthdate}.
	 */
	protected String[] getInfo()
	{
		String[] temp = super.getInfo();
		return new String[] {temp[0], temp[1], this.address, this.birthdate};
	}
}
