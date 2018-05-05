package assignment4Game;

import java.io.*;

public class Tester {

	public static void main(String[] args) {
		
		System.out.println("TESTING addDisk");
		
		Configuration c = new Configuration();
		c.addDisk(0, 2);
		c.addDisk(3, 1);
		c.addDisk(1, 1);
		c.addDisk(2, 1);
		c.addDisk(3, 1);
		c.addDisk(3, 2);
		c.addDisk(3, 1);
		boolean correct = (c.board[0][0] == 2) && (c.board[1][0] == 1) && (c.board[2][0] == 1)
				&& (c.board[3][0] == 1) &&(c.board[3][1] == 1)&& (c.board[3][3] == 1)&& (c.board[3][2] == 2);
		for (int i = 0; i < 7; i++){
			correct = correct && c.available[i] < 6;
		}
		if (correct && c.spaceLeft){
			System.out.println("addDisk seems to work, test it further though.");
		}
		else{
			System.out.println("addDisk does not work.");
		}
		c = new Configuration();
		c.addDisk(0, 1);
		c.addDisk(0, 2);
		c.addDisk(0, 1);
		c.addDisk(0, 2);
		c.addDisk(0, 1);
		c.addDisk(0, 2);
		c.addDisk(1, 1);
		c.addDisk(1, 2);
		c.addDisk(1, 1);
		c.addDisk(1, 2);
		c.addDisk(1, 1);
		c.addDisk(1, 2);
		c.addDisk(2, 1);
		c.addDisk(2, 2);
		c.addDisk(2, 1);
		c.addDisk(2, 2);
		c.addDisk(2, 1);
		c.addDisk(2, 2);
		c.addDisk(3, 1);
		c.addDisk(3, 2);
		c.addDisk(3, 1);
		c.addDisk(3, 2);
		c.addDisk(3, 1);
		c.addDisk(3, 2);
		c.addDisk(4, 1);
		c.addDisk(4, 2);
		c.addDisk(4, 1);
		c.addDisk(4, 2);
		c.addDisk(4, 1);
		c.addDisk(4, 2);
		c.addDisk(5, 1);
		c.addDisk(5, 2);
		c.addDisk(5, 1);
		c.addDisk(5, 2);
		c.addDisk(5, 1);
		c.addDisk(5, 2);
		c.addDisk(6, 1);
		c.addDisk(6, 2);
		c.addDisk(6, 1);
		c.addDisk(6, 2);
		c.addDisk(6, 1);
		c.addDisk(6, 2);
		c.print();
		System.out.println(!c.spaceLeft);
		System.out.println("" + c.available[0] + c.available[1] + c.available[2] + c.available[3] + c.available[4] + c.available[5] + c.available[6]);

		System.out.println("TESTING isWinning");
		
		Configuration c2 = new Configuration();
		c2.board[0][0] = 2; c2.board[1][0] = 1; c2.board[2][0] = 1; c2.board[3][0] = 1;
		c2.board[3][1] = 1; c2.board[3][3] = 1; c2.board[3][2] = 2;
		c2.available[0] = 1; c2.available[1] = 1; c2.available[2] = 1; c2.available[3] = 4;
		correct = ! c2.isWinning(2, 1);
		
		c2.board[4][0] = 1; c2.available[4] = 1;
		correct = correct && c2.isWinning(4, 1);
		
		if (correct){
			System.out.println("isWinning seems to work, test it further though.");
		}
		else{
			System.out.println("isWinning does not work.");
		}
		c = new Configuration();
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				c.addDisk(i, 1);
			}
		}
		c.print();
		System.out.println(c.isWinning(3, 1));
		System.out.println(!c.isWinning(3, 2));
		c = new Configuration();
		c.addDisk(0, 1);
		c.addDisk(1, 2);
		c.addDisk(1, 1);
		c.addDisk(2, 2);
		c.addDisk(3, 1);
		c.addDisk(2, 2);
		c.addDisk(2, 1);
		c.addDisk(3, 2);
		c.addDisk(4, 1);
		c.addDisk(3, 2);
		c.addDisk(3, 1);
		c.addDisk(4, 2);
		c.addDisk(4, 1);
		c.addDisk(4, 2);
		c.addDisk(4, 1);
		c.addDisk(5, 2);
		c.addDisk(5, 1);
		c.addDisk(5, 2);
		c.addDisk(5, 1);
		c.addDisk(5, 2);
		c.print();
		System.out.println(c.isWinning(0, 1));
		System.out.println(c.isWinning(1, 1));
		System.out.println(c.isWinning(2, 1));
		System.out.println(c.isWinning(3, 1));
		System.out.println(c.isWinning(4, 1));
		System.out.println(c.isWinning(5, 2));
		System.out.println(!c.isWinning(5, 1));
		System.out.println(!c.isWinning(6, 1));

		System.out.println("TESTING canWinNextRound");
		
		c2.board[4][0] = 0; c2.available[4] = 0;
		correct = c2.canWinNextRound(1) == 4;
		
		c2.board[4][0] = 2; c2.available[4] = 1;
		correct = correct && c2.canWinNextRound(1) == -1;
		
		if (correct){
			System.out.println("canWinNextRound seems to work, test it further though.");
		}
		else{
			System.out.println("canWinNextRound does not work.");
		}
		
		c = new Configuration();
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				c.addDisk(i, 1);
			}
		}
		c.print();
		System.out.println(c.canWinNextRound(1));
		c.removeDisk(0);
		c.print();
		System.out.println(c.canWinNextRound(1));
		System.out.println(c.canWinNextRound(1));
		c.removeDisk(0);
		c.removeDisk(0);
		c.removeDisk(0);
		c.removeDisk(0);
		c.removeDisk(0);
		c.print();
		System.out.println(c.canWinNextRound(1));
		System.out.println(c.canWinNextRound(1));
		System.out.println(c.canWinNextRound(2));
		c.addDisk(0, 1);
		c.addDisk(0, 1);
		c.addDisk(0, 1);
		c.addDisk(0, 1);
		c.addDisk(0, 1);
		c.addDisk(0, 1);
		c.board[1][5] = 0;
		c.available[1]--;
		c.spaceLeft = true;
		c.print();
		System.out.println(c.canWinNextRound(1));
		System.out.println(c.canWinNextRound(1));
		System.out.println(c.canWinNextRound(2));
		
		System.out.println("TESTING canWinTwoTurns");
		                                              
		c2.board[0][3] = 1; c2.board[0][2] = 1; c2.board[0][1] = 2; c2.available[0] = 4;
		c2.board[2][3] = 1; c2.board[2][2] = 2; c2.board[2][1] = 1; c2.available[2] = 4;
		c2.print();
		correct = c2.canWinTwoTurns(1) == 1 && c2.canWinTwoTurns(2) == -1;
		System.out.println(c2.canWinTwoTurns(1));
		System.out.println(c2.canWinTwoTurns(2));
		
		if (correct){
			System.out.println("canWinInTwoTurns seems to work, test it further though.");
		}
		else{
			System.out.println("canWinInTwoTurns does not work.");
		}
		c = new Configuration();
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				c.addDisk(i, 1);
			}
		}
		c.print();
		System.out.println(c.canWinTwoTurns(1));
		c = new Configuration();
		c.addDisk(3, 1);
		c.addDisk(3, 2);
		c.addDisk(2, 1);
		c.addDisk(2, 2);
		c.print();
		System.out.println(c.canWinTwoTurns(1));

		System.out.println("TESTING getNextMove");
		
		// you have to uncomment what comes next when you test this method
		// you probably want to comment it again once you are done, otherwise you will be asked each time
		
		c2.board[0][4] = 1; c2.board[0][5] = 2; c2.available[0] = 6;
		c2.board[2][4] = 1; c2.board[2][5] = 2; c2.available[2] = 6;
		/*c2.print();
		int result = -1;
		try{
			InputStreamReader input = new InputStreamReader(System.in);
			BufferedReader keyboard = new BufferedReader(input);
			result = Game.getNextMove(keyboard, c2, 1);
			keyboard.close();
		}
		catch(Throwable e){
			System.out.println("something somewhere went horribly wrong");
		}
		
		System.out.println("does it return what you requested ? " + result);*/
		
		System.out.println("TESTING movePlayer1");
		
		c2.board[1][1] = 2; c2.available[1]= 2;
		c2.print();
		correct = Game.movePlayer1(2, c2) == 1;
		System.out.print(Game.movePlayer1(2, c2));
		System.out.println("Should be 1");
		
		c2.board[1][1] = 0; c2.available[1]= 1;
		c2.print();
		correct = correct && Game.movePlayer1(2, c2) == 1;
		System.out.print(Game.movePlayer1(2, c2));
		System.out.println("Should be 1");
		
		c2.board[0][3] = 2;
		c2.print();
		correct = correct && Game.movePlayer1(2, c2) == 1;
		System.out.print(Game.movePlayer1(2, c2));
		System.out.println("Should be 1");
		correct = correct && Game.movePlayer1(0, c2) == 1;
		System.out.print(Game.movePlayer1(0, c2));
		System.out.println("Should be 1");
		
		c2.board[3][4] = 2; c2.board[3][5] = 2; c2.available[3]= 6;
		c2.print();
		correct = correct && Game.movePlayer1(3, c2) == 4;
		System.out.print(Game.movePlayer1(3, c2));
		System.out.println("Should be 4");
		
		if (correct){
			System.out.println("movePlayer1 seems to work, you can now uncomment the last two lines of the tester and play against your AI !");
		}
		else{
			System.out.println("movePlayer1 does not work.");
		}
		
		// When you want to play the game, uncomment the two lines down this comment
		InputStreamReader input2 = new InputStreamReader(System.in);
		System.out.println(Game.play(input2));
		
	}

}
