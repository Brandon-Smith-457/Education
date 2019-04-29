package mapPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MapMain {
	public static void main(String[] args)
	{
		Map myMap;
		int row, column;
		char character;

		System.out.print("Welcome to map\nPlease input the maximum number of rows:");
		row = userInputInitializeInt();
		System.out.print("Please input the maximum number of columns:");
		column = userInputInitializeInt();
		myMap = new Map(row, column, '~');

		if (!myMap.isValidMap())
		{
			System.out.println("The object Map was improperly initialized.  Terminating program.");
			return;
		}
		System.out.println("Map has been created.\nPlease add an object to the map (~ for water, G for grass, # for tree).");
		
		while (true)
		{
			System.out.print("Row:");
			row = userInputCoordInt(myMap, "Row");
			System.out.print("Column:");
			column = userInputCoordInt(myMap, "Column");
			
			System.out.print("Character:");
			while (true)
			{
				character = userInputChar();
				if (character == 'G' || character == '#' || character == '~')
				{
					break;
				}
				System.out.println("Invalid character! It must be either ~ or G or #.");
			}
			if (myMap.updateMap(row, column, character)) 				//Updating the map, and if the map is updated correctly do the following.
			{
				System.out.print("Your " + character + " was added to " + row + ", " + column + " in the map.\n"
						+ "Would you like to enter another character? (Y/N):");
				while (true)
				{
					character = userInputChar();
					if (character == 'Y' || character == 'N')
					{
						break;
					}
					System.out.println("Invalid character! It must be either Y or N.");
				}
				if (character == 'N')
				{
					break;
				}
			}
		}
		myMap.printMap();
	}
	
	/* Creator: Brandon Smith
	 * Purpose: Read a line from the user, parse the line into an integer larger than 0, and if no problems occur throughout the process, return
	 * the integer.  In the case of problems, provide a system message, and then re-prompt the user for input.
	 * Arguments: N/A.
	 * Returns: int userInt, a valid integer.
	 */
	private static int userInputInitializeInt()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int userInt;
		String input;

		while (true)
		{
			try
			{
				input = br.readLine();
				userInt = Integer.parseInt(input);
				if (Map.isValidInitialize(userInt))	//Check if the input is larger than 0.
				{
					return userInt;
				}
				System.out.println("Please input an integer value greater than 0.");
			}
			catch (IOException e)
			{
				System.out.println("IOException has occured.");
			}
			catch (NumberFormatException e)
			{
				System.out.println("Please input an integer value greater than 0.");
			}
			catch (Exception e)
			{
				System.out.println("Unhandled Exception.");
			}
		}
	}
	
	/* Creator: Brandon Smith
	 * Purpose: Read a line from the user, parse the line into an integer, and check if it lies within the valid range of possible coordinates.
	 * If no problems occur throughout the process, return the integer.  In the case of problems, provide a system message, and then re-prompt
	 * the user for input.
	 * Arguments: Map myMap, String dimension.
	 * Returns: int userInt, a valid integer.
	 */
	private static int userInputCoordInt(Map myMap, String dimension) //Map object and String dimension passed to check the validity of the coordinates.
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int inputInt;
		String inputString;

		while (true)
		{
			try
			{
				inputString = br.readLine();
				inputInt = Integer.parseInt(inputString);
				if (myMap.isValidCoordinate(inputInt, dimension))	//Check if the input is within the possible range of coordinates.
				{
					return inputInt;
				}
				System.out.println("Please input an integer value greater than or equal to 1 and less than or equal to " + myMap.getMaxDimension(dimension) + ".");
			}
			catch (IOException e)
			{
				System.out.println("IOException has occured.");
			}
			catch (NumberFormatException e)
			{
				System.out.println("Please input an integer value greater than 0.");
			}
			catch (Exception e)
			{
				System.out.println("Unhandled Exception.");
			}
		}
	}
	
	/* Creator: Brandon Smith
	 * Purpose: Read a line from the user, convert the String into a character array, and check if it is only a single character.  If so then
	 * return it.  If no problems occur throughout the process, return the character.  In the case of problems, provide a system message, and
	 * then re-prompt the user for input.
	 * Arguments: N/A.
	 * Returns: char userInput, a valid character.
	 */
	private static char userInputChar()
	{
		BufferedReader br;
		String tempStr;
		char[] userInput;
		
		while (true)
		{
			try
			{
				br = new BufferedReader(new InputStreamReader(System.in));
				tempStr = br.readLine();
				userInput = tempStr.toCharArray();
				if (userInput.length == 1)
				{
					return userInput[0];
				}
				System.out.println("Please ensure that the input is only a single character.");
			}
			catch (IOException e)
			{
				System.out.println("IOException has occured.");
			}
			catch (Exception e)
			{
				System.out.println("Unhandled Exception.");
			}
		}
	}
}
