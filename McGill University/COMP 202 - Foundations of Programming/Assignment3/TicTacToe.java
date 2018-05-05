/* Brandon Smith */
import java.util.Scanner;
import java.util.Random;

public class TicTacToe
{
	public static void main(String[] args)
	{
		play();
	}	
	
	//Method to create a new empty board and initialize each element to the space character.
	public static char[][] createBoard(int n)
	{
		char[][] board = new char[n][n];
		
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				board[i][j] = ' ';
			}
		}
		return board;
	}
	
	
	//Method to display the board.
	public static void displayBoard(char[][] board)
	{
		for (int i = 0; i < (2*board.length + 1); i++)
		{
			if (i%2 == 0)
			{
				//See helper methods, below all required methods. Prints a line of +-+-+-....
				printPlusMinusLine(2*board.length + 1);
			}
			else
			{
				//See helper methods, below all required methods. Prints a line of |c1|c2|c3... where c1,c2,etc. are the characters in the given row of the board.
				printBoardRow(2*board.length + 1, board[(i-1)/2]);
			}
		}
	}
	
	
	//Method to input a character to the board at given indices x and y.
	public static void writeOnBoard(char[][] board, char input, int x, int y)
	{
		if (x >= board.length || y>= board.length || x < 0 || y < 0)
		{
			throw new IllegalArgumentException("The cell corresponding to x and y does not exist (Out of bounds).");
		}
		else if (board[x][y] != ' ')
		{
			throw new IllegalArgumentException("The cell corresponding to x and y already contains a character.");
		}
		else
		{
			board[x][y] = input;
		}
	}
	
	
	//Method to receive input from the user, check if the user's move is correct, and call writeOnBoard.
	public static void getUserMove(char[][] board)
	{
		Scanner scan = new Scanner(System.in);
		String rawInput;
		String[] tokenizedInput;
		int x;
		int y;
		
		System.out.println("Please input two integers corresponding to the cell within which you wish to put your x. Example: Row 1 Column 2 would be \"0 1\".");
		while (true)
		{
			rawInput = scan.nextLine();
			//See helper methods, below all required methods. Separating the raw input by space characters.
			tokenizedInput = tokenize(rawInput);
			//Check if the tokenized input contains more than or less than 2 elements.
			if (tokenizedInput.length != 2)
			{
				System.out.println("Please ensure that your input is two positive integers between 0 and " + (board.length - 1) + " (inclusive) and separated by a space.");
			}
			//See helper methods, below all required methods. Check if both elements in the tokenized input are valid integers.
			else if (tryParseInt(tokenizedInput[0]) && tryParseInt(tokenizedInput[1]))
			{
				x = Integer.parseInt(tokenizedInput[0]);
				y = Integer.parseInt(tokenizedInput[1]);
				if (x < 0 || y < 0 || x >= board.length || y >= board.length)
				{
					System.out.println("Please ensure that your input is two positive integers between 0 and " + (board.length - 1) + " (inclusive) and separated by a space.");
				}
				else if (board[x][y] != ' ')
				{
					System.out.println("Please ensure that the desired cell is not already occupied");
				}
				else
				{
					writeOnBoard(board, 'x', x, y);
					break;
				}
			}
			else
			{
				System.out.println("Please ensure that your input is two positive integers between 0 and " + (board.length - 1) + " (inclusive) and separated by a space.");
			}
		}
	}
	
	
	//Method that checks for an obvious move that the AI can make.
	public static boolean checkForObviousMove(char[][] board)
	{
		char[] column = new char[board.length];
		//See helper methods, below all required methods. Store the two diagonals into arrays.
		char[] diagonalTopLeft = getTopLeftDiagonal(board);
		char[] diagonalTopRight = getTopRightDiagonal(board);
		/*
		The temp variables will hold the row and column indices of the space character in any row, column, or diagonal
		wherein the USER would win in the next move.
		*/
		int rowNumberTemp = -1;
		int columnNumberTemp = -1;
		
		/*
		Check for win conditions in all the columns and rows for both the user and the AI, and if the AI can win, carry out the move and return true.
		If the user would win, store the indices where the AI would move.
		*/
		for (int i = 0; i < board.length; i++)
		{
			//See helper methods, below all required methods. Store column at index i in array column.
			column = getColumn(board, i);
			//See helper methods, below all required methods. Check if column at index i has all but one cell containing 'o' and the last cell containing ' '.
			if (checkArray(column, 'o'))
			{
				//See helper methods, below all required methods. The index i is the column index, and getEmptyCell returns an integer that corresponds to the row index.
				writeOnBoard(board, 'o', getEmptyCell(column), i);
				return true;
			}
			else if (checkArray(column, 'x'))
			{
				rowNumberTemp = getEmptyCell(column);
				columnNumberTemp = i;
			}
			if (checkArray(board[i], 'o'))
			{
				//The index i is the row index in this case, hence the position in writeOnBoard.
				writeOnBoard(board, 'o', i, getEmptyCell(board[i]));
				return true;
			}
			else if (checkArray(board[i], 'x'))
			{
				rowNumberTemp = i;
				columnNumberTemp = getEmptyCell(board[i]);
			}
		}
		/*
		Check for win conditions in both diagonals for both the user and the AI, and if the AI can win, carry out the move and return true.
		If the user would win, store the indices where the AI would move.
		*/
		if (checkArray(diagonalTopLeft, 'o'))
		{
			writeOnBoard(board, 'o', getEmptyCell(diagonalTopLeft), getEmptyCell(diagonalTopLeft));
			return true;
		}
		else if (checkArray(diagonalTopLeft, 'x'))
		{
			rowNumberTemp = getEmptyCell(diagonalTopLeft);
			columnNumberTemp = getEmptyCell(diagonalTopLeft);
		}
		if (checkArray(diagonalTopRight, 'o'))
		{
			writeOnBoard(board, 'o', getEmptyCell(diagonalTopRight), ((diagonalTopRight.length - 1) - getEmptyCell(diagonalTopRight)));
			return true;
		}
		else if (checkArray(diagonalTopRight, 'x'))
		{
			rowNumberTemp = getEmptyCell(diagonalTopRight);
			columnNumberTemp = ((diagonalTopRight.length - 1) - getEmptyCell(diagonalTopRight));
		}
		//If this is executed then there was no obvious AI victory. Check if the user obvious win variables were changed from -1, if so do the AI move and return true.
		if (rowNumberTemp != -1 && columnNumberTemp != -1)
		{
			writeOnBoard(board, 'o', rowNumberTemp, columnNumberTemp);
			return true;
		}
		//There was no obvious move to be made.
		else
		{
			return false;
		}
	}
	
	
	//A Method that generates a move for the AI.
	public static void getAIMove(char[][] board)
	{
		int x;
		int y;
		Random randomIndex = new Random();
		System.out.println("The AI has made it's move!");
		//Carry out the following if there was no obvious move (IF THERE WAS AN OBVIOUS MOVE, THE checkForObviousMove(...) CALL WOULD CALL writeOnBoard(...)).
		if (!checkForObviousMove(board))
		{
			//Keep generating random indices for the board until the corresponding cell contains the space character.
			while (true)
			{
				x = randomIndex.nextInt(board.length);
				y = randomIndex.nextInt(board.length);
				if (board[x][y] == ' ')
				{
					break;
				}
			}
			writeOnBoard(board, 'o', x, y);
		}
	}
	
	
	//A method to check if there is a winner for any given state of the board. Returns the character that corresponds to the winner.
	public static char checkForWinner(char[][] board)
	{
		char[] column = new char[board.length];
		//See helper methods, below all required methods.
		char[] diagonalTopLeft = getTopLeftDiagonal(board);
		char[] diagonalTopRight = getTopRightDiagonal(board);
		
		//Check all columns and rows for win conditions of both 'x' and 'o'.
		for (int i = 0; i < board.length; i++)
		{
			//See helper methods, below all required methods.
			column = getColumn(board, i);
			//See helper methods, below all required methods.
			if (checkArrayForWin(column) == 'x')
			{
				return 'x';
			}
			else if (checkArrayForWin(column) == 'o')
			{
				return 'o';
			}
			if (checkArrayForWin(board[i]) == 'x')
			{
				return 'x';
			}
			else if (checkArrayForWin(board[i]) == 'o')
			{
				return 'o';
			}
		}
		//Check the diagonals for win condition of both 'x' and 'o'.
		if (checkArrayForWin(diagonalTopLeft) == 'x')
		{
			return 'x';
		}
		else if (checkArrayForWin(diagonalTopLeft) == 'o')
		{
			return 'o';
		}
		if (checkArrayForWin(diagonalTopRight) == 'x')
		{
			return 'x';
		}
		else if (checkArrayForWin(diagonalTopRight) == 'o')
		{
			return 'o';
		}
		//If there is no winner return the space character.
		else
		{
		return ' ';
		}
	}
	
	//PLAY TIC TAC TOE!!!
	public static void play()
	{
		System.out.println("Hello! Welcome to TicTacToe! Please input your name so this can get personal!");
		Scanner scan = new Scanner(System.in);
		String playerName = scan.nextLine();
		
		System.out.println("Now, " + playerName + ", please input the dimension of the board you want to play on.");
		String dimension;
		int n;
		char[][] board;
		while (true)
		{
			dimension = scan.nextLine();
			//See helper methods, below all required methods. Check to see if the user inputed dimension is a valid integer.
			if (tryParseInt(dimension))
			{
				n = Integer.parseInt(dimension);
				//It's impossible for the person who moves first to lose at 1x1 or 2x2 TicTacToe.
				if (n > 2)
				{
					board = createBoard(n);
					break;
				}
				else
				{
					System.out.println("Please ensure that your input is larger than 2");
				}
			}
			else
			{
				System.out.println("Please ensure that your input is an integer");
			}
		}
		
		displayBoard(board);
		
		Random coin = new Random();
		//Turn token helps for flipping back and forth between the user move and the AI move.
		int turnToken = coin.nextInt(2);
		System.out.print("The result of the coinflip to decide who goes first is : ");
		if (turnToken == 0)
		{
			System.out.println("AI's Move");
		}
		else
		{
			System.out.println(playerName + "'s Move");
		}
		
		String winner = "";
		for (int i = 0; i < n*n; i++)
		{
			if (turnToken == 0)
			{
				getAIMove(board);
				turnToken = 1;
				displayBoard(board);
				System.out.println();
			}
			else
			{
				getUserMove(board);
				turnToken = 0;
				displayBoard(board);
				System.out.println();
			}
			if (checkForWinner(board) == 'x')
			{
				winner = playerName;
				break;
			}
			else if (checkForWinner(board) == 'o')
			{
				winner = "AI";
				break;
			}
		}
		if (winner.equals("AI"))
		{
			System.out.println("The match ended with the AI being victorious!");
		}
		else if (winner.equals(playerName))
		{
			System.out.println("The match ended with " + playerName + " being victorious!");
		}
		else
		{
			System.out.println("The match ended in a draw!");
		}
		/*
		Close the scanner so as to avoid "resource leak" warnings.  HOWEVER I have a scanner in getUserMove, and if I close that scanner it throws a NoSuchElement
		Exception the next time that a System.in Scanner object is used (Research has told me that it's because it closes the System.in stream so it's impossible
		to re-initialize a scanner.
		*/
		scan.close();
	}
	
	
	//ALL HELPER METHODS DOWN HERE!
	
	
	//Method to print a line of +-+-+-+-....
	public static void printPlusMinusLine(int n)
	{
		for (int i = 0; i < n; i++)
		{
			if (i%2 == 0)
			{
				System.out.print('+');
			}
			else
			{
			System.out.print('-');
			}
		}
		System.out.println();
	}
	
	
	//Method to print |c1|c2|c3...
	public static void printBoardRow(int n, char[] boardRow)
	{
		for (int i = 0; i < n; i++)
		{
			if (i%2 == 0)
			{
				System.out.print('|');
			}
			else
			{
			System.out.print(boardRow[(i-1)/2]);
			}
		}
		System.out.println();
	}
	
	
	//Method to check if the input from the user is an integer.
	public static boolean tryParseInt(String input)
	{
		try
		{
			Integer.parseInt(input);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
	
	
	//Method to return the number of words in any given string (Separation is a space)(Referenced from Midterm Fall 2017).
	public static int numberOfWords(String s)
	{
        int count = 1; 
        for(int i = 0; i < s.length(); i++)
        {
            if (s.charAt(i) == ' ')
            {
                count++;
            }
        }
        return count;
    }
	
	
	//Method to separate a string by space characters (Referenced from Midterm Fall 2017)
	public static String[] tokenize(String s)
	{
        String[] output = new String[numberOfWords(s)];
        int count = 0;
        for (int i = 0; i < output.length; i++)
        {
            String word = "";
            for (int j = count; j < s.length(); j++) {
                if (s.charAt(j) != ' ')
                {
                    word += s.charAt(j);
                }
                else
                {
                    count = j + 1;
                    break;
                }
            }
            output[i] = word;
        }
        return output;
    }
	
	
	//Method to return the column at any given index of a 2D array.
	public static char[] getColumn(char[][] board, int columnNumber)
	{
		char[] column = new char[board.length];
		for (int i = 0; i < column.length; i++)
		{
			column[i] = board[i][columnNumber];
		}
		return column;
	}
	
	
	//Method to return the diagonal of an NxN array that connects the top left to the bottom right. The top left is element 0.
	public static char[] getTopLeftDiagonal(char[][] board)
	{
		char[] diagonal = new char[board.length];
		for (int i = 0; i < board.length; i++)
		{
			diagonal[i] = board[i][i];
		}
		return diagonal;
	}
	
	
	//Method to return the diagonal of an NxN array that connects the top right to the bottom left. The top right is element 0.
	public static char[] getTopRightDiagonal(char[][] board)
	{
		char[] diagonal = new char[board.length];
		for (int i = 0; i < board.length; i++)
		{
			diagonal[i] = board[i][(diagonal.length - 1) - i];
		}
		return diagonal;
	}
	
	
	//Method to check all the elements of a 1D array and determine if there is (array.length - 1) of any one specific character in it.
	public static boolean checkArray(char[] arr, char c)
	{
		int cCount = 0;
		int spaceCount = 0;
		//Counting the number of characters c, and the number of spaces in an array of characters
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i] == ' ')
			{
				spaceCount++;
			}
			else if (arr[i] == c)
			{
				cCount++;
			}
		}
		//If the spaceCount is 0 then there is no move to be had there, so do not bother checking anything and return false.
		if (spaceCount != 0)
		{
			if (cCount == (arr.length - 1))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	
	//Method to determine the location of a space character (If multiple exist it will return the last-most one).
	public static int getEmptyCell(char[] arr)
	{
		int index = -1;
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i] == ' ')
			{
				index = i;
			}
		}
		return index;
	}
	
	
	//A method to check an array for a winner.
	public static char checkArrayForWin(char[] arr)
	{
		int xCount = 0;
		int oCount = 0;
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i] == 'x')
			{
				xCount++;
			}
			else if (arr[i] == 'o')
			{
				oCount++;
			}
		}
		if (xCount == arr.length)
		{
			return 'x';
		}
		else if (oCount == arr.length)
		{
			return 'o';
		}
		else
		{
			return ' ';
		}
	}
}