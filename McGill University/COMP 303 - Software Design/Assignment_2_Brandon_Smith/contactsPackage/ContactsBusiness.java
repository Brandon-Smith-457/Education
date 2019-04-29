package contactsPackage;

/* Creator: Brandon Smith
 * Name: ContactsBusiness
 * Type: Class
 * Purpose: Define the elements particular to an Business contact type.
 * Notes: Extends Contacts.
 * 		  All API are protected so that only elements inside of the package can use them.
 */
public class ContactsBusiness extends Contacts {
	private String address;
	private String businessName;

	/* Creator: Brandon Smith
	 * Name: ContactsBusiness
	 * Type: Constructor
	 * Purpose: Initialize any one given ContactsBusiness with a name, phone number, address, and business name.
	 * Arguments: String name, String phone, String address, String businessName.
	 * Returns: N/A
	 */
	protected ContactsBusiness(String name, String phone, String address, String businessName)
	{
		super(name, phone);
		this.address = address;
		this.businessName = businessName;
	}

	/* Creator: Brandon Smith
	 * Name: getInfo
	 * Type: getterMethod (Package API)
	 * Purpose: Get all of the info of the ContactsBusiness.
	 * Arguments: N/A
	 * Returns: String[] {name, phone, address, businessName}.
	 */
	protected String[] getInfo()
	{
		String[] temp = super.getInfo();
		return new String[] {temp[0], temp[1], this.address, this.businessName};
	}
}
