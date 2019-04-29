package contactsPackage;

/* Creator: Brandon Smith
 * Name: ContactsApp
 * Type: Class
 * Purpose: Contain a set of Contacts stored in 32 binary trees sorted by a hashFunction and store in hashMap
 * Notes: The largest____Length member variables are stored so that tabular formatting is possible.
 * 		  Data structure chosen is a hashMap of Contacts Trees so that store and search times will both be O(log(n/32)).
 */
public class ContactsApp {
	private int largestTypeLength = 4;				//Pre-set to 4 because TYPE has 4 characters
	private int largestNameLength = 4; 				//Pre-set to 5 because NAME has 5 characters
	private int largestBusinessNameLength = 12;		//Pre-set to 12 because businessName has 12 characters
	private int largestPhoneLength = 5; 			//Pre-set to 5 because PHONE has 5 characters
	private int largestAddressLength = 7;			//Pre-set to 7 because ADDRESS has 7 characters
	private int largestBirthdateLength = 9;			//Pre-set to 9 because BIRTHDATE has 9 characters
	private int numberOfContacts = 0;				//Keeping track of the number of contacts in the phone book for use when listing
	private int index;								//Index for recursively accumulating String[] outputs in listing all contacts
	private Contacts[] hashMap = new Contacts[32];	//Hash Map with Mod 32 centered around the upper case letters of the alphabet (32 => 'A' and 'a' map to the same hashMap[i])

	/* Creator: Brandon Smith
	 * Name: addAcquaintance
	 * Type: Method (API)
	 * Purpose: Instantiate a new ContactsAcquaintance with the input values given by user, and add the new ContactsAcquaintance to the	data structure.
	 * Arguments: String[] inputStringArr where inputStringArr[0] = name, and inputStringArr[1] = phone.
	 * Returns: N/A
	 * Notes: The largest___Length fields get updated accordingly.
	 */
	public void addAcquaintance(String[] inputStringArr)
	{
		if (inputStringArr.length == 2)
		{
			Contacts newAcquaintance = new ContactsAcquaintance(inputStringArr[0], inputStringArr[1]);
			if (inputStringArr[0].length() > this.largestNameLength)
			{
				this.largestNameLength = inputStringArr[0].length();
			}
			if (inputStringArr[1].length() > this.largestPhoneLength)
			{
				this.largestPhoneLength = inputStringArr[1].length();
			}
			if ("Acquaintance".length() > this.largestTypeLength)
			{
				this.largestTypeLength = "Acquaintance".length();
			}
			addToHashMap(newAcquaintance);
		}
		else
		{
			System.out.println("Incorrect input");
		}
	}

	/* Creator: Brandon Smith
	 * Name: addBusiness
	 * Type: Method (API)
	 * Purpose: Instantiate a new ContactsBusiness with the input values given by user, and add the new ContactsBusiness to the	data structure.
	 * Arguments: String[] inputStringArr where inputStringArr[0] = name, inputStringArr[1] = phone, inputStringArr[2] = address, and inputStringArr[3] = businessName.
	 * Returns: N/A
	 * Notes: The largest___Length fields get updated accordingly.
	 */
	public void addBusiness(String[] inputStringArr)
	{
		if (inputStringArr.length == 4)
		{
			Contacts newBusiness = new ContactsBusiness(inputStringArr[0], inputStringArr[1], inputStringArr[2], inputStringArr[3]);
			if (inputStringArr[0].length() > this.largestNameLength)
			{
				this.largestNameLength = inputStringArr[0].length();
			}
			if (inputStringArr[1].length() > this.largestPhoneLength)
			{
				this.largestPhoneLength = inputStringArr[1].length();
			}
			if (inputStringArr[2].length() > this.largestAddressLength)
			{
				this.largestAddressLength = inputStringArr[2].length();
			}
			if (inputStringArr[3].length() > this.largestBusinessNameLength)
			{
				this.largestBusinessNameLength = inputStringArr[3].length();
			}
			if ("Business".length() > this.largestTypeLength)
			{
				this.largestTypeLength = "Business".length();
			}
			addToHashMap(newBusiness);
		}
		else
		{
			System.out.println("Incorrect input");
		}
	}

	/* Creator: Brandon Smith
	 * Name: addFriend
	 * Type: Method (API)
	 * Purpose: Instantiate a new ContactsFriend with the input values given by user, and add the new ContactsFriend to the	data structure.
	 * Arguments: String[] inputStringArr where inputStringArr[0] = name, inputStringArr[1] = phone, inputStringArr[2] = address, and inputStringArr[3] = birthdate.
	 * Returns: N/A
	 * Notes: The largest___Length fields get updated accordingly.
	 */
	public void addFriend(String[] inputStringArr)
	{
		if (inputStringArr.length == 4)
		{
			Contacts newFriend = new ContactsFriend(inputStringArr[0], inputStringArr[1], inputStringArr[2], inputStringArr[3]);
			if (inputStringArr[0].length() > this.largestNameLength)
			{
				this.largestNameLength = inputStringArr[0].length();
			}
			if (inputStringArr[1].length() > this.largestPhoneLength)
			{
				this.largestPhoneLength = inputStringArr[1].length();
			}
			if (inputStringArr[2].length() > this.largestAddressLength)
			{
				this.largestAddressLength = inputStringArr[2].length();
			}
			if (inputStringArr[3].length() > this.largestBirthdateLength)
			{
				this.largestBirthdateLength = inputStringArr[3].length();
			}
			if ("Friend".length() > this.largestTypeLength)
			{
				this.largestTypeLength = "Friend".length();
			}
			addToHashMap(newFriend);
		}
		else
		{
			System.out.println("Incorrect input");
		}
	}
	
	/* Creator: Brandon Smith
	 * Name: findContact
	 * Type: Method (API)
	 * Purpose: Find the information corresponding to the first Contact in the list of Contacts that has a matching name to the inputString.  If none is found, informs user.
	 * Arguments: String inputString.  inputString: the name to compare against in the database.
	 * Returns: String[]: Contact not found -> null. Contact found -> Contact's info
	 * Notes: findContact checks the hashMap corresponding to the inputString, and if there exists elements in the given index it passes the input string to findFromTree.
	 */
	public String[] findContact(String inputString)
	{
		int hashResult = (inputString.charAt(0) - 65) % 32;
		if (hashResult < 0)
		{
			hashResult = hashResult + 32;
		}
		Contacts tempContact;
		if (this.hashMap[hashResult] == null)
		{
			return null;
		}
		tempContact = findFromTree(this.hashMap[hashResult], inputString);
		if (tempContact == null)
		{
			return null;
		}
		return tempContact.getInfo();
	}

	/* Creator: Brandon Smith
	 * Name: getLargestStringLengths
	 * Type: Method (API)
	 * Purpose: Get the max String lengths for formatting purposes.
	 * Arguments: N/A
	 * Returns: int[] set of Largest String lengths
	 */
	public int[] getLargestStringLengths()
	{
		return new int[] {this.largestTypeLength, this.largestNameLength, this.largestPhoneLength, this.largestAddressLength, this.largestBirthdateLength, this.largestBusinessNameLength};
	}

	/* Creator: Brandon Smith
	 * Name: listAllContacts
	 * Type: Method (API)
	 * Purpose: Construct a 2D array of Strings table, filling table[i] with references to String[]'s that contain all the info of a given contact, using the empty string
	 * 			if the field has no value.
	 * Arguments: N/A
	 * Returns: String[][]: Contacts exist -> String[i] = {type, name, phone, address, birthdate, businessName}, Contacts don't exist -> String[][] of size 0.
	 * Notes: Makes use of the instance's numberOfContacts field to instantiate the 2D array table.
	 * 		  Makes use of the instance's index field to fill the table's elements one by one through the recursive calls of addToReturnTable.
	 */
	public String[][] listAllContacts()
	{
		String[][] table = new String[this.numberOfContacts][];
		this.index = 0;
		//32 hashMap slots by design.
		for (int i = 0; i < 32; i++)
		{
			addToReturnTable(this.hashMap[i], table);
		}
		return table;
	}
	
	/* Creator: Brandon Smith
	 * Name: addToHashMap
	 * Type: Method (Private)
	 * Purpose: Given the newContact, compute the hash function [f(x) = (x - 65) mod 32] on the first letter of the Contacts name.  If the given index is empty, insert the
	 * 			newContact as the root of the tree.  If not, add the newContact to the existing tree.
	 * Arguments: Contacts newContact.
	 * Returns: N/A
	 * Notes: The numberOfContacts field gets updated accordingly.
	 */
	private void addToHashMap(Contacts newContact)
	{
		int hashResult = (newContact.getName().charAt(0) - 65) % 32;
		if (hashResult < 0)
		{
			hashResult = hashResult + 32;
		}
		this.numberOfContacts++;
		if (this.hashMap[hashResult] == null)
		{
			this.hashMap[hashResult] = newContact;
		}
		else
		{
			addToTree(this.hashMap[hashResult], newContact);
		}
	}
	
	/* Creator: Brandon Smith
	 * Name: addToTree
	 * Type: Method (Private)
	 * Purpose: Place the newContact left of the current contact if the newContact's name is smaller (ASCII Codes).  Place it right if the name is greater.  Place it
	 * 			in the middle if they're the same.
	 * Arguments: Contacts existingTree, Contacts newContact. existingTree is the contact at the root of the self defined tree.
	 * Returns: N/A
	 * Notes: Recursively calls itself to traverse the tree.
	 */
	private void addToTree(Contacts existingTree, Contacts newContact)
	{
		//Branch for if the Contacts' names are equivalent.
		if (rankString(newContact.getName(), existingTree.getName()) == 2)
		{
			if (existingTree.getContactMiddle() == null)
			{
				existingTree.setContactMiddle(newContact);
			}
			else
			{
				addToTree(existingTree.getContactMiddle(), newContact);
			}
		}
		//Branch for if the newContact's name is larger than the existing one.
		else if (rankString(newContact.getName(), existingTree.getName()) == 1)
		{
			if (existingTree.getContactRight() == null)
			{
				existingTree.setContactRight(newContact);
			}
			else
			{
				addToTree(existingTree.getContactRight(), newContact);
			}
		}
		//Branch for if the newContact's name is smaller than the existing one.
		else
		{
			if (existingTree.getContactLeft() == null)
			{
				existingTree.setContactLeft(newContact);
			}
			else
			{
				addToTree(existingTree.getContactLeft(), newContact);
			}
		}
	}
	
	/* Creator: Brandon Smith
	 * Name: rankString
	 * Type: Method (Private)
	 * Purpose: Given two strings, determines if s1 is larger than, equal to, or smaller than s2.
	 * Arguments: String s1, String s2.
	 * Returns: int: If s1 is larger than s2 -> 1, If s1 is smaller than s2 -> 0, If s1 is equal to s2 -> 2.
	 */
	private int rankString(String s1, String s2)
	{
		//First check all the letters up to the end of the shortest string
		for (int i = 0; i < s1.length() && i < s2.length(); i++)
		{
			if (s1.charAt(i) > s2.charAt(i))
			{
				return 1;
			}
			else if (s1.charAt(i) < s2.charAt(i))
			{
				return 0;
			}
		}
		//Knowing that all the letters of s1 and s2 are the same up to the shortest's length determine the result based on the length of the strings
		if (s1.length() == s2.length())
		{
			return 2;
		}
		else if (s1.length() > s2.length())
		{
			return 1;
		}
		return 0;
	}
	
	/* Creator: Brandon Smith
	 * Name: findFromTree
	 * Type: Method (Private)
	 * Purpose: Searches through a given existingTree and compares each contact's name with the inputString in order to find a contact with the same name.
	 * Arguments: Contacts existingTree, String inputString.  existingTree is the root of the tree to search.
	 * Returns: Contacts: Contact not found -> Null, Contact found -> Contacts' info.
	 */
	private Contacts findFromTree(Contacts existingTree, String inputString)
	{
		int rankStringResult = rankString(inputString, existingTree.getName());
		//If both the current Contact is the one we are looking for
		if (rankStringResult == 2)
		{
			return existingTree;
		}
		//If the current contact is "less" than the one we are looking for
		else if (rankStringResult == 1)
		{
			if (existingTree.getContactRight() == null)
			{
				return null;
			}
			else
			{
				return findFromTree(existingTree.getContactRight(), inputString);
			}
		}
		//If the current contact is "more" than the one we are looking for
		else
		{
			if (existingTree.getContactLeft() == null)
			{
				return null;
			}
			else
			{
				return findFromTree(existingTree.getContactLeft(), inputString);
			}
		}
	}

	/* Creator: Brandon Smith
	 * Name: addToReturnTable
	 * Type: Method (Private)
	 * Purpose: Recursively traverse all elements of the tree contact, adding String[] to table in-order so that elements print in alphabetical order.
	 * Arguments: Contacts contact, String[][]acc.  contact = the current root of the tree, acc = the 2D string array meant to accumulate the resulting table.
	 * Returns: N/A
	 * Notes: Print before traversing down the middle so that same-name contacts are accumulated in the order with which they were added.
	 */
	private void addToReturnTable(Contacts contact, String[][] acc)
	{
		//Handling the calls on the hashMap indices that are not filled
		if (contact == null)
		{
			return;
		}
		if (contact.getContactLeft() != null)
		{
			addToReturnTable(contact.getContactLeft(), acc);
		}
		
		acc[this.index] = formatForListAll(contact);
		this.index++;
		
		if (contact.getContactMiddle() != null)
		{
			addToReturnTable(contact.getContactMiddle(), acc);
		}
		if (contact.getContactRight() != null)
		{
			addToReturnTable(contact.getContactRight(), acc);
		}
	}
	
	/* Creator: Brandon Smith
	 * Name: formatForListAll
	 * Type: Method (Private)
	 * Purpose: Checking the instance of the given Contacts contact, and filling out the empty fields with "" where applicable (by the definition of the inputted info)
	 * Arguments: Contacts contact.
	 * Returns: String[] {type, name, phone, address, birthdate, businessName}.
	 */
	private String[] formatForListAll(Contacts contact)
	{
		String type, address, birthdate, businessName;
		String[] contactInfo = contact.getInfo();
		
		if (contact.getClass().getName().equals("contactsPackage.ContactsAcquaintance"))
		{
			type = "Acquaintance";
			address = "";
			birthdate = "";
			businessName = "";
			return new String[] {type, contactInfo[0], contactInfo[1], address, birthdate, businessName};
		}
		else if (contact.getClass().getName().equals("contactsPackage.ContactsBusiness"))
		{
			type = "Business";
			birthdate = "";
			return new String[] {type, contactInfo[0], contactInfo[1], contactInfo[2], birthdate, contactInfo[3]};
		}
		else if (contact.getClass().getName().equals("contactsPackage.ContactsFriend"))
		{
			type = "Friend";
			businessName = "";
			return new String[] {type, contactInfo[0], contactInfo[1], contactInfo[2], contactInfo[3], businessName};
		}
		return null;
	}
}
