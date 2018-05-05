public class Reservation {
	//private attributes to determine the name under which a client has made the reservation and the room itself that has been reserved.
	private String name;
	private Room roomReserved;
	
	//Constructor initializing a reservation with a given name and room
	public Reservation(Room availableRoom, String name)
	{
		this.name = name;
		//Equating the reference because the information of the reserved room should indeed match the information of the room itself.
		this.roomReserved = availableRoom;
	}
	
	//Method to get the name of the reservation holder
	public String getName()
	{
		return this.name;
	}
	
	//Method to get the room that has been reserved
	public Room getRoom()
	{
		return this.roomReserved;
	}
}
