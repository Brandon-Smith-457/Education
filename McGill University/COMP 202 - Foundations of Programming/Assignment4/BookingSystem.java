import java.util.Scanner;
import java.util.Random;

public class BookingSystem {
    
    private static String[] typeOfRooms = {"double","queen","king"};
    private static Random r = new Random(123);
    
    //returns a random String from the above array. 
    private static String getRandomType(){
        int index = r.nextInt(typeOfRooms.length);
        return typeOfRooms[index];
    }
    //returns a random number of rooms between 5 and 50.
    private static int getRandomNumberOfRooms(){
        return r.nextInt(50)+1;
    }
    //End of provided code. 
    
    //Class variables created for ease of future use in multiple fields
    private static String type;
    private static String userName;
    private static Scanner scan = new Scanner(System.in);

    //Method to convert the user input into a Name and a Type
    private static void tokenize(String input)
    {
		userName = "";
		type = "";
		System.out.println("Please input your full name, followed by the room type (Double, Queen, or King) separated by \",\". Do not use any commas in your name! (Ex: \"John Smith, King\")");
		input = scan.nextLine();
		
		//int to keep track of what step in the process each iteration of the loop should be carrying out
		int nextAction = 0;
		//A for loop that adds all the characters up to the comma into the class variable userName, and then adds the remaining characters
		//(excluding any space characters) to the class variable type.
		for (int i = 0; i < input.length(); i++)
		{
			if (nextAction == 0)
			{
				if (input.charAt(i) != ',')
				{
					userName += input.charAt(i);
				}
				else if (input.charAt(i) == ',')
				{
					nextAction++;
				}
			}
			else if (nextAction == 1)
			{
				if (input.charAt(i) != ' ')
				{
					type += input.charAt(i);
				}
			}
		}
    }
    
    public static void main(String[] args){
        //Student Name: Brandon Smith
        //Student Number: 260 738 831
        //Your code goes here.
    	
    	//Get the user to input the name of the hotel
    	System.out.println("Please input the name of the hotel you wish to interact with.");
    	String hotelName = scan.nextLine();
    	//An array of Room with a random size
    	int numberOfRooms = getRandomNumberOfRooms();
    	Room[] rooms = new Room[numberOfRooms];
    	//Initialize each Room to a random type
    	for (int i = 0; i < numberOfRooms; i++)
    	{
    		rooms[i] = new Room(getRandomType());
    	}
    	//Create the hotel and store it in a variable
    	Hotel workingHotel = new Hotel(hotelName, rooms);
    	
    	//Main menu interface
    	String input;
    	while (true)
    	{
    		//Main menu display
    		System.out.println("**********************************************************************************************************************************");
    		System.out.println("Welcome to " + hotelName + ". Please choose one of the following options :\n1. Make a reservation\n2. Cancel a reservation\n3. See an invoice\n4. See the hotel info\n5. Exit the booking system");
    		System.out.println("**********************************************************************************************************************************");
    		
    		input = scan.nextLine();
    		
    		//Conditional statements that check if the STRING input of the user match any of the 5 options.
    		if (input.equalsIgnoreCase("1") || input.equalsIgnoreCase("Make a reservation"))
    		{
    			while (true)
    			{
    				//Split the input into userName and type
    				tokenize(input);
    				//Check if the type is one of the possible three
        			if (type.equalsIgnoreCase("double") || type.equalsIgnoreCase("queen") || type.equalsIgnoreCase("king"))
        			{
        				break;
        			}
        			else
        			{
        				System.out.println("The desired type does not match any of the three possible room types.");
        			}
    			}
    			//Call the create Reservation method on the object workingHotel found in the Hotel class
    			workingHotel.createReservation(userName, type);
    		}
    		
    		
    		else if (input.equalsIgnoreCase("2") || input.equalsIgnoreCase("Cancel a reservation"))
    		{
    			while (true)
    			{
    				//Split the input into userName and type
    				tokenize(input);
    				//Check if the type matches one of the possible 3
        			if (type.equalsIgnoreCase("double") || type.equalsIgnoreCase("queen") || type.equalsIgnoreCase("king"))
        			{
        				break;
        			}
        			else
        			{
        				System.out.println("The desired type does not match any of the three possible room types.");
        			}
    			}
    			//Call the cancelReservation method on the object workingHotel found in the Hotel class
    			workingHotel.cancelReservation(userName, type);
    		}
    		
    		
    		else if (input.equalsIgnoreCase("3") || input.equalsIgnoreCase("See an invoice"))
    		{
    			System.out.println("Please enter the name under which you have reserved your room(s)");
    			userName = scan.nextLine();
    			//Call the method printInvoice on the object workingHotel found in the Hotel class
    			workingHotel.printInvoice(userName);
    		}
    		
    		
    		else if (input.equalsIgnoreCase("4") || input.equalsIgnoreCase("See the hotel info"))
    		{
    			//Print the information of workingHotel (indirectly calling workingHotel.toString())
    			System.out.println(workingHotel);
    		}
    		
    		
    		else if (input.equalsIgnoreCase("5") || input.equalsIgnoreCase("Exit the booking system"))
    		{
    			//Breaking out of the loop and reaching the end of the main method
    			System.out.println("Please come back again!");
    			break;
    		}
    		
    		//If the input matches none of the options then do nothing and return to the beginning of the loop (re-display the main menu
    		//and await another input from the user)
    		else
    		{
    			
    		}
       	}
    	//Close the scanner to avoid leakage
    	scan.close();
    }
}