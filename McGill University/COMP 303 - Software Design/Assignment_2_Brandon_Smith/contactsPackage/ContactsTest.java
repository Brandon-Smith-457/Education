package contactsPackage;
import java.util.Arrays;

public class ContactsTest {
	public static void main (String[] args)
	{
		boolean acc = true;
		boolean indiv;
		System.out.println("Testing ContactsAcquaintance:");
		indiv = contactsAcquaintanceTest();
		acc = acc && indiv;
		if (indiv) System.out.println("Success\n");
		else System.out.println("Failed\n");

		System.out.println("Testing ContactsBusiness:");
		indiv = contactsBusinessTest();
		acc = acc && indiv;
		if (indiv) System.out.println("Success\n");
		else System.out.println("Failed\n");
		
		System.out.println("Testing ContactsFriend:");
		indiv = contactsFriendTest();
		acc = acc && indiv;
		if (indiv) System.out.println("Success\n");
		else System.out.println("Failed\n");
		
		System.out.println("Testing ContactsApp:");
		indiv = contactsAppTest();
		acc = acc && indiv;
		if (indiv) System.out.println("Success\n");
		else System.out.println("Failed\n");
		
		if (acc) System.out.println("This Object is fully functional");
		else System.out.println("Failure");
}
	
	public static boolean contactsAcquaintanceTest()
	{
		try
		{
			boolean acc = true;
			boolean indiv;
			String name = "Hoopla";
			String phone = "Doopla";
			ContactsAcquaintance contact = new ContactsAcquaintance(name, phone);
			
			System.out.println("Testing getName:");
			indiv = contactsAcquaintanceGetNameInfoTest(contact, name, phone);
			acc = acc && indiv;
			if (indiv) System.out.println("Success");
			else System.out.println("Failed");
			
			System.out.println("Testing getSetContactXXX:");
			indiv = contactsGetSetContactTest(contact);
			acc = acc && indiv;
			if (indiv) System.out.println("Success");
			else System.out.println("Failed");

			return acc;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean contactsBusinessTest()
	{
		try
		{
			boolean acc = true;
			boolean indiv;
			String name = "Hoopla";
			String phone = "Doopla";
			String address = "Huh";
			String businessName = "Duh";
			ContactsBusiness contact = new ContactsBusiness(name, phone, address, businessName);
			
			System.out.println("Testing getName:");
			indiv = contactsBusinessGetNameInfoTest(contact, name, phone, address, businessName);
			acc = acc && indiv;
			if (indiv) System.out.println("Success");
			else System.out.println("Failed");
			
			System.out.println("Testing getSetContactXXX:");
			indiv = contactsGetSetContactTest(contact);
			acc = acc && indiv;
			if (indiv) System.out.println("Success");
			else System.out.println("Failed");

			return acc;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean contactsFriendTest()
	{
		try
		{
			boolean acc = true;
			boolean indiv;
			String name = "Hoopla";
			String phone = "Doopla";
			String address = "Huh";
			String birthdate = "Duh";
			ContactsFriend contact = new ContactsFriend(name, phone, address, birthdate);
			
			System.out.println("Testing getName:");
			indiv = contactsFriendGetNameInfoTest(contact, name, phone, address, birthdate);
			acc = acc && indiv;
			if (indiv) System.out.println("Success");
			else System.out.println("Failed");
			
			System.out.println("Testing getSetContactXXX:");
			indiv = contactsGetSetContactTest(contact);
			acc = acc && indiv;
			if (indiv) System.out.println("Success");
			else System.out.println("Failed");

			return acc;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean contactsAcquaintanceGetNameInfoTest(Contacts contact, String name, String phone)
	{
		try
		{
			return contact.getName().equals(name) && contact.getInfo()[0].equals(name) && contact.getInfo()[1].equals(phone);
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean contactsBusinessGetNameInfoTest(Contacts contact, String name, String phone, String address, String businessName)
	{
		try
		{
			return contact.getName().equals(name) && contact.getInfo()[0].equals(name) && contact.getInfo()[1].equals(phone) && contact.getInfo()[2].equals(address) && contact.getInfo()[3].equals(businessName);
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean contactsFriendGetNameInfoTest(Contacts contact, String name, String phone, String address, String birthdate)
	{
		try
		{
			return contact.getName().equals(name) && contact.getInfo()[0].equals(name) && contact.getInfo()[1].equals(phone) && contact.getInfo()[2].equals(address) && contact.getInfo()[3].equals(birthdate);
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean contactsGetSetContactTest(Contacts contact)
	{
		try
		{
			boolean acc = contact.getContactLeft() == null && contact.getContactMiddle() == null && contact.getContactRight() == null;
			contact.setContactLeft(new ContactsAcquaintance("a", "b"));
			contact.setContactMiddle(new ContactsAcquaintance("c", "d"));
			contact.setContactRight(new ContactsAcquaintance("e", "f"));
			acc = acc && contact.getContactLeft().getName().equals("a") && contact.getContactMiddle().getName().equals("c") && contact.getContactRight().getName().equals("e");
			contact.setContactLeft(new ContactsBusiness("a", "b", "c", "d"));
			contact.setContactMiddle(new ContactsBusiness("e", "f", "g", "h"));
			contact.setContactRight(new ContactsBusiness("i", "j", "k", "l"));
			acc = acc && contact.getContactLeft().getName().equals("a") && contact.getContactMiddle().getName().equals("e") && contact.getContactRight().getName().equals("i");
			contact.setContactLeft(new ContactsFriend("a", "b", "c", "d"));
			contact.setContactMiddle(new ContactsFriend("e", "f", "g", "h"));
			contact.setContactRight(new ContactsFriend("i", "j", "k", "l"));
			acc = acc && contact.getContactLeft().getName().equals("a") && contact.getContactMiddle().getName().equals("e") && contact.getContactRight().getName().equals("i");
			return acc;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static boolean contactsAppTest()
	{
		try
		{
			boolean acc = true;
			boolean indiv = true;
			char input;
			String s;
			
			System.out.println("Testing findAndAddContact: (Should see three \"Incorrect input\")");			
			ContactsApp phoneBook = new ContactsApp();
			phoneBook.addAcquaintance(new String[] {"AG", "1"});
			phoneBook.addAcquaintance(new String[] {"AH", "2"});
			phoneBook.addAcquaintance(new String[] {"AJ", "3"});
			phoneBook.addBusiness(new String[] {"AI", "4", ".", "."});
			phoneBook.addBusiness(new String[] {"AJ", "5", ".", "."});
			phoneBook.addBusiness(new String[] {"AK", "6", ".", "."});
			phoneBook.addFriend(new String[] {"AE", "7", ".", "."});
			phoneBook.addFriend(new String[] {"AE", "8", ".", "."});
			phoneBook.addFriend(new String[] {"ADsd", "9", ".", "."});
			phoneBook.addAcquaintance(new String[] {"AF", "10"});
			phoneBook.addAcquaintance(new String[] {"AF", "11", "."});
			phoneBook.addBusiness(new String[] {"AF", "12"});
			phoneBook.addFriend(new String[] {"AF", "13"});
			ContactsApp phoneBook3 = new ContactsApp();
			for (int i = 1; i < 128; i++)
			{
				input = (char) i;
				s = Character.toString(input);
				phoneBook3.addAcquaintance(new String[] {s, "."});
				phoneBook3.addBusiness(new String[] {s, ".", ".", "."});
				phoneBook3.addFriend(new String[] {s, ".", ".", "."});
			}
			indiv = indiv && phoneBook.findContact("AG")[1].equals("1");
			indiv = indiv && phoneBook.findContact("AH")[1].equals("2");
			indiv = indiv && phoneBook.findContact("AJ")[1].equals("3");
			indiv = indiv && phoneBook.findContact("AI")[1].equals("4");
			indiv = indiv && phoneBook.findContact("AK")[1].equals("6");
			indiv = indiv && phoneBook.findContact("AE")[1].equals("7");
			indiv = indiv && phoneBook.findContact("ADsd")[1].equals("9");
			indiv = indiv && phoneBook.findContact("AF")[1].equals("10");
			acc = acc && indiv;
			if (indiv) System.out.println("Success");
			else System.out.println("Failed");

			ContactsApp phoneBook2 = new ContactsApp();
			System.out.println("Testing getLargestStringLengths:");
			phoneBook2.addFriend(new String[] {"123456789012345", "123456789012345", "123456789012345", "123456789012345"});
			indiv = phoneBook2.getLargestStringLengths()[0] == 6 && phoneBook2.getLargestStringLengths()[1] == 15 && phoneBook2.getLargestStringLengths()[2] == 15 && phoneBook2.getLargestStringLengths()[3] == 15 && phoneBook2.getLargestStringLengths()[4] == 15;
			phoneBook2.addBusiness(new String[] {"1234567890123456", "1234567890123456", "1234567890123456", "1234567890123456"});
			indiv = indiv && phoneBook2.getLargestStringLengths()[0] == 8 && phoneBook2.getLargestStringLengths()[1] == 16 && phoneBook2.getLargestStringLengths()[2] == 16 && phoneBook2.getLargestStringLengths()[3] == 16 && phoneBook2.getLargestStringLengths()[4] == 15 && phoneBook2.getLargestStringLengths()[5] == 16;
			phoneBook2.addAcquaintance(new String[] {"12345678901234567", "12345678901234567"});
			indiv = indiv && phoneBook2.getLargestStringLengths()[0] == 12 && phoneBook2.getLargestStringLengths()[1] == 17 && phoneBook2.getLargestStringLengths()[2] == 17;
			acc = acc && indiv;
			if (indiv) System.out.println("Success");
			else System.out.println("Failed");
			
			System.out.println("Testing listAllContacts:");
			indiv = Arrays.deepToString(phoneBook2.listAllContacts()).equals("[[Friend, 123456789012345, 123456789012345, 123456789012345, 123456789012345, ], [Business, 1234567890123456, 1234567890123456, 1234567890123456, , 1234567890123456], [Acquaintance, 12345678901234567, 12345678901234567, , , ]]");
			indiv = Arrays.deepToString(phoneBook.listAllContacts()).equals("[[Friend, ADsd, 9, ., ., ], [Friend, AE, 7, ., ., ], [Friend, AE, 8, ., ., ], [Acquaintance, AF, 10, , , ], [Acquaintance, AG, 1, , , ], [Acquaintance, AH, 2, , , ], [Business, AI, 4, ., , .], [Acquaintance, AJ, 3, , , ], [Business, AJ, 5, ., , .], [Business, AK, 6, ., , .]]");
			acc = acc && indiv;
			if (indiv) System.out.println("Success");
			else System.out.println("Failed");

			return acc;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
