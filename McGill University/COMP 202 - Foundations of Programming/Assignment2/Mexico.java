/*
Brandon_Smith
*/

public class Mexico {
	
	public static void main(String[] args)
	{		
		//Declare and initialize the buy in and the bet.
		double buy = Double.parseDouble(args[0]);
		double pay = Double.parseDouble(args[1]);
		
		//Call the method to play Mexico with the given inputs for the buy in and the bet.
		playMexico(buy, pay);
	}
	
	//Method that returns an int and takes no input.
	public static int diceRoll()
	{
		/*Declaring the variable to hold the dice roll. +1.0 is used because the type casting from double to int will cause the values
		  to be truncated (rounded down) and so the possible int values will be from 0 to 6.
		  0 to 0.999999999... will result in a dice roll of 0 which doesn't exist, so the +1.0 brings it up to 1.
		  The value of 6 will now give you seven, so it is required to
		  make a conditional statement for the insignificantly probable case of getting exactly 6.*/
		int d = (int)(Math.random()*6.0 + 1.0);
		if(d == 7)
		{
			return (d-1);
		}
		return d;
	}
	
	
	//Method that returns an int and takes two int's as input.
	public static int getScore(int d1, int d2)
	{
		/*Initialize score to avoid compiling errors (Alternatively I could have made the else if into an else but I find it easier
		  to read when you have the two cases explicitly written)*/
		int score = 0;
		if (d1 >= d2)
		{
			score = d1*10 + d2;
		}
		else if (d2 >= d1)
		{
			score = d2*10 + d1;
		}
		return score;
	}
	
	
	//Method that returns an int and takes a String as input.
	public static int playOneRound(String name)
	{
		//Two variables to hold the dice rolls and a third variable to hold the score output from the getScore method.
		int d1 = diceRoll();
		int d2 = diceRoll();
		int score = getScore(d1,d2);
		System.out.println(name + " rolled:  " + d1 + " " + d2 + "\n"
						  +name + "'s score is:  " + score);
		return score;
	}
	
	
	//Method that returns a String and takes these : String, int, String, int : as input.
	public static String getWinner(int s1, int s2)
	{
		//Assigning the point weight of each possible score for s1 and s2. 7 special cases, so only 7 conditional statements for s1 and s2.
		if 		(s1 == 21) s1 = 72;
		else if (s1 == 66) s1 = 71;
		else if (s1 == 55) s1 = 70;
		else if (s1 == 44) s1 = 69;
		else if (s1 == 33) s1 = 68;
		else if (s1 == 22) s1 = 67;
		else if (s1 == 11) s1 = 66;
		
		if 		(s2 == 21) s2 = 72;
		else if (s2 == 66) s2 = 71;
		else if (s2 == 55) s2 = 70;
		else if (s2 == 44) s2 = 69;
		else if (s2 == 33) s2 = 68;
		else if (s2 == 22) s2 = 67;
		else if (s2 == 11) s2 = 66;
				
		//Evaluating which score has the higher point weight.
		if 		(s1 == s2) 	return "tie";
		else if (s1 > s2) 	return "Giulia";
		else if (s2 > s1) 	return "David";
		else 				return "Error";
	}
	
	
	//Method that returns a boolean and takes two boolean's as input.
	private static boolean canPlay(double buy, double pay)
	{
		if 		(buy <= 0 || pay <= 0) 	return false;
		else if (pay > buy) 			return false;
		else if (buy%pay != 0) 			return false;
		else 							return true;
	}
	
	
	//Method that returns nothing but takes these : String, String, double, double : as input.
	private static void playMexico(double buy, double pay)
	{
		//Check if the buy-in and bet are good using the canPlay method.
		if (canPlay(buy,pay))
		{
			//Creating two variables to hold the amount of money each person has left at the start of each line.
			double fund1 = buy;
			double fund2 = buy;
			//Int to keep track of the round#
			int i = 1;
			
			//Loop the following chunk of code as long as both player's still have funds.
			while (fund1 > 0 && fund2 > 0)
			{
				//Round#
				System.out.println("Round " + i + "\n");
				i++;
				
				//Display the funds.
				System.out.println("Funds: " + "Giulia" + " = " + fund1 + "; " + "David" + " = " + fund2 + ".");
				
				//Call the playOneRound method for player one then player 2.
				int s1 = playOneRound("Giulia");
				int s2 = playOneRound("David");
				
				//Determine the winner using getWinner method.
				String winner = getWinner(s1, s2);
				
				//Check if the round resulted in a tie.
				if (winner.equals("tie"))
				{
					System.out.println("This round resulted in a tie\n");
				}
				//Display the winner of the round and then calculate the funds after the loser pays the bet.
				else
				{
					System.out.println(winner + " wins this round!\n");
					if (winner.equals("Giulia"))
					{
						fund2 = fund2 - pay;
					}
					else if (winner.equals("David"))
					{
						fund1 = fund1 - pay;
					}
					else
					{
						System.out.println("An Error has occurred");
					}
				}
			}
			//After all rounds are played, display which of the two players has won the game.
			if (fund1 == 0)
			{
				System.out.println("David" + " won the game, making: " + (buy) + " dollars!");
			}
			else if (fund2 == 0)
			{
				System.out.println("Giulia" + " won the game, making: " + (buy) + " dollars!");
			}
			else
			{
				System.out.println("An Error has occured #2");
			}
		}
		//Code for when the buy-in and the bet are not correct.
		else
		{
			System.out.println("Please re-run the program and ensure that the buy in is a multiple of the bet and that they are both positive numbers (greater than 0)");
		}
	}
}
