public class Room {
	//private attributes to determine the type, price, and availability of any given room object
	private String type;
	private double price;
	private boolean availability;
	
	//Constructor to initialize the price and the availability of a room object given a type
	public Room(String type)
	{
		//Ensure the type input matches one of the three possibilities
		if (type.equalsIgnoreCase("double") || type.equals("queen") || type.equalsIgnoreCase("king"))
		{
			this.type = type;
			if (this.type.equalsIgnoreCase("double"))
			{
				this.price = 90.00;
			}
			else if (this.type.equalsIgnoreCase("queen"))
			{
				this.price = 110.00;
			}
			else if (this.type.equalsIgnoreCase("king"))
			{
				this.price = 150.00;
			}
			this.availability = true;
		}
		else
		{
			throw new IllegalArgumentException("No room of such type can be created.");
		}
	}
	
	//Method to get the type of the room
	public String getType()
	{
		return this.type;
	}
	
	//Method to get the price of the room
	public double getPrice()
	{
		return this.price;
	}
	
	//Method to get the availability of the room
	public boolean getAvailability()
	{
		return this.availability;
	}
	
	//Method to change the availability of the room
	public void changeAvailability()
	{
		if (this.availability == true)
		{
			this.availability = false;
		}
		else if (this.availability == false)
		{
			this.availability = true;
		}
	}
	
	//Method to find an available room in an array of rooms
	//STATIC because a single Room doesn't have an array of rooms to find an Available room from
	public static Room findAvailableRoom(Room[] rooms, String type)
	{
		for (int i = 0; i < rooms.length; i++)
		{
			if (rooms[i].type.equalsIgnoreCase(type) && rooms[i].availability == true)
			{
				return rooms[i];
			}
		}
		return null;
	}
}
