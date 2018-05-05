package assignment4Game;

public class Configuration {
	
	public int[][] board;
	public int[] available;
	boolean spaceLeft;
	
	public Configuration(){
		board = new int[7][6];
		available = new int[7];
		spaceLeft = true;
	}
	
	public void print(){
		System.out.println("| 0 | 1 | 2 | 3 | 4 | 5 | 6 |");
		System.out.println("+---+---+---+---+---+---+---+");
		for (int i = 0; i < 6; i++){
			System.out.print("|");
			for (int j = 0; j < 7; j++){
				if (board[j][5-i] == 0){
					System.out.print("   |");
				}
				else{
					System.out.print(" "+ board[j][5-i]+" |");
				}
			}
			System.out.println();
		}
	}
	
	public void addDisk (int index, int player){
		//Adding the disk and updating the row index for the given column.
		this.board[index][this.available[index]] = player;
		this.available[index]++;
		//Checking to see if we're out of space.
		int spaceCount = 0;
		for (int i = 0; i < 7; i++)
		{
			spaceCount += this.available[i];
		}
		if (spaceCount == 42)
		{
			this.spaceLeft = false;
		}
	}
	
	public boolean isWinning (int lastColumnPlayed, int player){
		//Need to taverse the board from the given start point.
		//Three operations: 1) Vertical scan, 2) Horizontal scan, 3) Diagonal Scan.
		//1) Vertical scan must scan incrementing up until it hits either the boundary of the board or the incorrect disk type.
		//   Then, it must scan incrementing down until it hits either the boundary of the board or the incorrect disk type.
		//   If the count of correct disk types is >= 4, then we return true.  Else we move on the Horizontal scan.
		//2) Horizontal scan must do the exact same thing incrementing right (positive), then left (negative).  Same returns
		//3) Diagonal scan must do the same thing except incrementing both indices at each step.
		//Only if we reach the end of the method do we return false.
		int winCount = 0;
		//Vertical Scan Positive
		int i = lastColumnPlayed;
		int j = this.available[lastColumnPlayed] - 1;
		while (i >= 0 && i < 7 && j >= 0 && j < 6)
		{
			if (this.board[i][j] == player)
			{
				j++;
				winCount++;
			}
			else
			{
				break;
			}
		}
		//Vertical Scan Negative
		j = this.available[lastColumnPlayed] - 2;
		while (i >= 0 && i < 7 && j >= 0 && j < 6)
		{
			if (this.board[i][j] == player)
			{
				j--;
				winCount++;
			}
			else
			{
				break;
			}
		}
		if (winCount >= 4)
		{
			return true;
		}
		
		winCount = 0;
		//Horizontal Scan Positive
		i = lastColumnPlayed;
		j = this.available[lastColumnPlayed] - 1;
		while (i >= 0 && i < 7 && j >= 0 && j < 6)
		{
			if (this.board[i][j] == player)
			{
				i++;
				winCount++;
			}
			else
			{
				break;
			}
		}
		//Horizontal Scan Negative
		i = lastColumnPlayed - 1;
		while (i >= 0 && i < 7 && j >= 0 && j < 6)
		{
			if (this.board[i][j] == player)
			{
				i--;
				winCount++;
			}
			else
			{
				break;
			}
		}
		if (winCount >= 4)
		{
			return true;
		}

		winCount = 0;
		//Diagonal North East Scan Positive
		i = lastColumnPlayed;
		j = this.available[lastColumnPlayed] - 1;
		while (i >= 0 && i < 7 && j >= 0 && j < 6)
		{
			if (this.board[i][j] == player)
			{
				i++;
				j++;
				winCount++;
			}
			else
			{
				break;
			}
		}
		//Diagonal South West Scan Negative
		i = lastColumnPlayed - 1;
		j = this.available[lastColumnPlayed] - 2;
		while (i >= 0 && i < 7 && j >= 0 && j < 6)
		{
			if (this.board[i][j] == player)
			{
				i--;
				j--;
				winCount++;
			}
			else
			{
				break;
			}
		}
		if (winCount >= 4)
		{
			return true;
		}

		winCount = 0;
		//Diagonal North West Scan Positive
		i = lastColumnPlayed;
		j = this.available[lastColumnPlayed] - 1;
		while (i >= 0 && i < 7 && j >= 0 && j < 6)
		{
			if (this.board[i][j] == player)
			{
				i--;
				j++;
				winCount++;
			}
			else
			{
				break;
			}
		}
		//Diagonal South East Scan Negative
		i = lastColumnPlayed + 1;
		j = this.available[lastColumnPlayed] - 2;
		while (i >= 0 && i < 7 && j >= 0 && j < 6)
		{
			if (this.board[i][j] == player)
			{
				i++;
				j--;
				winCount++;
			}
			else
			{
				break;
			}
		}
		if (winCount >= 4)
		{
			return true;
		}

		return false;
	}
	
	//Helper method to remove a disk off the top of any given column.
	public void removeDisk(int column)
	{
		this.available[column]--;
		this.board[column][this.available[column]] = 0;
		this.spaceLeft = true;
	}
	
	public int canWinNextRound (int player){
		//If there is space left
		if (this.spaceLeft)
		{
			for (int k = 0; k < 7; k++)
			{
				//For every column that is not full add a temporary disk and check to see if that move would result in a win
				if (this.available[k] != 6)
				{
					this.addDisk(k, player);
					if (this.isWinning(k, player))
					{
						this.removeDisk(k);
						return k;
					}
					this.removeDisk(k);
				}
			}
		}
		return -1;
	}
	
	public int canWinTwoTurns (int player){
		//If there is space left
		if (this.spaceLeft)
		{
			//wins keeps track of how many opponent moves would result in a win for the player
			int wins = 0;
			int opponent = 0;
			if (player == 1)
			{
				opponent = 2;
			}
			else
			{
				opponent = 1;
			}
			//The following nested for loops check every possible two moves that can be made (starting from 0) and if there is a
			//First move that resulted in 7 wins increments then this move guarantees victory for player.
			for (int i = 0; i < 7; i++)
			{
				wins = 0;
				for (int j = 0; j < 7; j++)
				{
					if (this.available[i] != 6)
					{
						this.addDisk(i, player);
						if (this.available[j] != 6)
						{
							if (this.canWinNextRound(opponent) == -1)
							{
								this.addDisk(j, opponent);
								int temp = this.canWinNextRound(player);
								if (temp != -1)
								{
									wins++;
								}
								this.removeDisk(j);
							}
						}
						else
						{
							wins++;
						}
						this.removeDisk(i);
					}
				}
				if (wins == 7)
				{
					return i;
				}
			}
		}
		return -1;
}
	
}
