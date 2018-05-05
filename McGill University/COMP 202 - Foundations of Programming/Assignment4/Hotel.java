//Importing NoSuchElementException cause not automatically imported in the basic java package
import java.util.NoSuchElementException;

public class Hotel {
	//Private attributes for the name of the hotel, an array of all the Room(s) in the hotel, and an array of all the Reservation(s)
	//made with the Hotel.
	private String name;
	private Room[] rooms;
	private Reservation[] reservations;
	
	//Constructor initialize the name of the hotel and the array of Rooms available in the hotel
	public Hotel(String name, Room[] rooms)
	{
		this.name = name;
		//THIS IS JUST INCASE YOU WANNA USE THE SAME ARRAY OF ROOMS TO INITIALIZE MULTIPLE HOTELS (Hence fairly pointless in this program)
		Room[] tempRooms = new Room[rooms.length];
		for (int i = 0; i < rooms.length; i++)
		{
			tempRooms[i] = rooms[i];
		}
		this.rooms = tempRooms;
	}
	
	//A method to add a reservation to the array of reservations
	private void addReservation(Reservation newReserve)
	{
		Reservation[] tempReserve;
		//This is to avoid nullPointerException's
		if (this.reservations == null)
		{
			tempReserve = new Reservation[1];
			tempReserve[0] = newReserve;
			this.reservations = tempReserve;
		}
		else
		{
			//ONCE MORE POINTLESS (It would be useful if the hotel wanted to keep records of the reservations they have had)
			tempReserve = new Reservation[this.reservations.length + 1];
			for (int i = 0; i < this.reservations.length; i++)
			{
				tempReserve[i] = this.reservations[i];
			}
			tempReserve[this.reservations.length] = newReserve;
			this.reservations = tempReserve;
		}
	}
	
	//A method to remove a reservation from the array of reservations
	private void removeReservation(String name, String type) throws NoSuchElementException
	{
		//variable to keep track of whether or not the reservation was found
		boolean doesntExist = true;
		//Once more avoiding null pointer exceptions
		if (this.reservations == null)
		{
			throw new NoSuchElementException("A reservation under the given name and type does not exist.");
		}
		else
		{
			//Iterate over the length of the reservations array
			for (int i = 0; i < this.reservations.length; i++)
			{
				//Check if the inputed name and type match the name and type of the reservation at element i
				if (this.reservations[i].getName().equalsIgnoreCase(name) && this.reservations[i].getRoom().getType().equalsIgnoreCase(type))
				{
					//THIS TIME IT HAS A POINT!!  Creating an array of Reservation one element smaller than the current
					Reservation[] tempReserve = new Reservation[this.reservations.length - 1];
					//Fill the new array with all the reservations but skip the one that we want to remove
					for (int j = 0; j < this.reservations.length - 1; j++)
					{
						if (j < i)
						{
							tempReserve[j] = this.reservations[j];
						}
						else
						{
							tempReserve[j] = this.reservations[j+1];
						}
					}
					this.reservations[i].getRoom().changeAvailability();
					this.reservations = tempReserve;
					doesntExist = false;
					break;
				}
			}
			if (doesntExist)
			{
				throw new NoSuchElementException("A reservation under the given name and type does not exist.");
			}
		}
	}
	
	//A method to create a reservation
	public void createReservation(String name, String type)
	{
		//Call the findAvailableRoom method on the Room class to find an available room of the inputed type
		Room foundRoom = Room.findAvailableRoom(this.rooms, type);
		if (foundRoom == null)
		{
			System.out.println("Regrettably there are no rooms of the type with which you are looking for.");
		}
		else
		{
			Reservation newReserve = new Reservation(foundRoom, name);
			foundRoom.changeAvailability();
			//this. used to indicate that the method is being called on the object that the current execution is "in"
			this.addReservation(newReserve);
			System.out.println("The resrevation process was completed successfully.");
		}
	}
	
	//A method to cancel a reservation under a given name and room type
	public void cancelReservation(String name, String type)
	{
		try
		{
			//Once again referring to the object we are "in" at this point in the execution
			this.removeReservation(name, type);
			System.out.println("The cancelation process was completed successfully.");
		}
		catch (NoSuchElementException e)
		{
			System.out.println("No reservations under the given name and type of room has been made.");
		}
	}
	
	//A method to print the invoice for any given name
	public void printInvoice(String name)
	{
		//Avoiding nullPointerException's
		if (this.reservations == null)
		{
			System.out.println("You have no reservations with us, resulting in a total amount due of : 0.0 dollars");
		}
		else
		{
			double totalPrice = 0.0;
			//This variable is going to be used to display the breakdown of all the reservations a person has
			String breakdownPrices = "";
			for (int i = 0; i < this.reservations.length; i++)
			{
				//if a reservation matches the inputed name
				if (this.reservations[i].getName().equalsIgnoreCase(name))
				{
					totalPrice += this.reservations[i].getRoom().getPrice();
					breakdownPrices += this.reservations[i].getRoom().getType() + "\t" + this.reservations[i].getRoom().getPrice() + "\n";
				}
			}
			if (totalPrice == 0.0)
			{
				System.out.println("You have no reservations with us, resulting in a total amount due of : 0.0 dollars");
			}
			else
			{
				System.out.println("You have the following reservations :\n" + breakdownPrices + "For a total amount due of : " + totalPrice);
			}
		}
		
	}
	
	//The toString method for printing the available rooms for a given hotel
	public String toString()
	{
		String s = this.name + "\n";
		int doubleCounter = 0;
		int queenCounter = 0;
		int kingCounter = 0;
		if (this.rooms == null)
		{
			s = "Unfortunately, there are no rooms available in this hotel.";
		}
		else
		{
			for (int i = 0; i < this.rooms.length; i++)
			{
				if (this.rooms[i].getAvailability())
				{
					if (this.rooms[i].getType().equalsIgnoreCase("double"))
					{
						doubleCounter++;
					}
					else if (this.rooms[i].getType().equalsIgnoreCase("queen"))
					{
						queenCounter++;
					}
					else if (this.rooms[i].getType().equalsIgnoreCase("king"))
					{
						kingCounter++;
					}
				}
			}
			s += "The following are the availabilities we currently have.\nDouble bed : " + doubleCounter + "\nQueen bed : " + queenCounter + "\nKing bed : " + kingCounter;
		}
		return s;
	}
	
	
	
	
	
}
