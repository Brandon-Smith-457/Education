package assignment4Game;

import java.io.*;

public class Game {
	
	public static int play(InputStreamReader input){
		BufferedReader keyboard = new BufferedReader(input);
		Configuration c = new Configuration();
		int columnPlayed = 3; int player;
		
		// first move for player 1 (played by computer) : in the middle of the grid
		c.addDisk(firstMovePlayer1(), 1);
		int nbTurn = 1;
		
		while (nbTurn < 42){ // maximum of turns allowed by the size of the grid
			player = nbTurn %2 + 1;
			if (player == 2){
				columnPlayed = getNextMove(keyboard, c, 2);
			}
			if (player == 1){
				columnPlayed = movePlayer1(columnPlayed, c);
			}
			System.out.println(columnPlayed);
			c.addDisk(columnPlayed, player);
			if (c.isWinning(columnPlayed, player)){
				c.print();
				System.out.println("Congrats to player " + player + " !");
				return(player);
			}
			nbTurn++;
		}
		return -1;
	}
	
	public static int getNextMove(BufferedReader keyboard, Configuration c, int player){
		String input = "";
		int output = -1;
		//Printing the board so that playing the game is visual.
		c.print();
		//Constantly prompt the user to enter a valid integer handling improper inputs through the catch's of IOException and NumberFormatException.
		while (true)
		{
			System.out.print("Player" + player + ", Please input an integer corresponding to the column with which you wish to place your disk:");
			try
			{
				input = keyboard.readLine();
				output = Integer.parseInt(input);
			}
			catch (IOException e)
			{
				System.out.println("Please ensure that the input is a valid integer between 0 and 6.");
			}
			catch (NumberFormatException e)
			{
				System.out.println("Please ensure that the input is a valid integer between 0 and 6.");
			}
			if (output >=0 && output < 7)
			{
				if (c.available[output] == 6)
				{
					System.out.println("Please ensure that the input is a valid move.");
					output = -1;
				}
			}
			else
			{
				System.out.println("Please ensure that the input is a valid move.");
				output = -1;
			}
			if (output != -1)
			{
				break;
			}
		}
		return output;
	}
	
	public static int firstMovePlayer1 (){
		return 3;
	}
	
	public static int movePlayer1 (int columnPlayed2, Configuration c){
		//If the AI can win on the next round return the corresponding column.
		int temp = c.canWinNextRound(1);
		if (temp != -1)
		{
			return temp;
		}
		//If the AI can win in the next two turns return the corresponding column.
		temp = c.canWinTwoTurns(1);
		if (temp != -1)
		{
			return temp;
		}
		//If there is space place the ai move in the same column as the user move.
		if (c.available[columnPlayed2] != 6)
		{
			return columnPlayed2;
		}
		//Check left THEN right until finding an empty column to place the disk.
		for (int i = 1; i < 7; i++)
		{
			temp = columnPlayed2 - i;
			if (temp >= 0)
			{
				if (c.available[temp] != 6)
				{
					return temp;
				}
			}
			temp = columnPlayed2 + i;
			if (temp <= 6)
			{
				if (c.available[temp] != 6)
				{
					return temp;
				}
			}
		}
		return -1;
	}
}
