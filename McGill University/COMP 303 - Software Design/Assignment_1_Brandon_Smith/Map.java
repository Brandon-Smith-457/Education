package mapPackage;

public class Map {
	private int maxRows;
	private int maxColumns;
	private char[][] map;
	
	/* Creator: Brandon Smith
	 * Purpose: Validate whether or not a given int is strictly greater than 0.
	 * Arguments: int input.
	 * Returns: boolean true if condition is met, false otherwise.
	 * Notes: Public modifier so that validation can be done immediately when inputed by user in MapMain.
	 */
	public static boolean isValidInitialize(int input)
	{
		return (input > 0);
	}
	
	/* Creator: Brandon Smith
	 * Purpose: Validate whether or not 2D character matrix has been initialized with the correct number of rows and columns.
	 * Arguments: N/A.
	 * Returns: boolean true if condition is met, false otherwise.
	 * Notes: Public modifier so that if validation fails in MapMain, the program may be terminated.
	 */
	public boolean isValidMap()
	{
		try
		{
			return (this.map.length == this.maxRows && this.map[0].length == this.maxColumns);
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	/* Creator: Brandon Smith
	 * Purpose: Validate whether or not a given integer input exists within the range of valid coordinates for any given dimension
	 * given as a String (Row, Column).
	 * Arguments: int input, String dimension.
	 * Returns: boolean true if condition is met, false otherwise.
	 * Notes: Public modifier so that if validation fails in MapMain, the program may ask for another integer.
	 */
	public boolean isValidCoordinate(int input, String dimension)
	{
		if (dimension.equals("Row"))
		{
			return (input <= this.maxRows && input > 0);
		}
		else if (dimension.equals("Column"))
		{
			return (input <= this.maxColumns && input > 0);
		}
		else
		{
			System.out.println("Invalid dimension, accepts only \"Row\" or \"Column\".  Returning false.");
			return false;
		}
	}
	
	/* Creator: Brandon Smith
	 * Purpose: If maxRows and maxColumns are integers greater than 0, assign their values to the current instance's maxRows and maxColumns,
	 * then construct a 2D character matrix of dimensions maxRows and maxColumns.  Initialize every element in the matrix to the inputed base
	 * char.  Print error messages if any of these steps fail.
	 * Arguments: int maxRows, int maxColumns, char base.
	 * Returns: N/A.
	 */
	public Map(int maxRows, int maxColumns, char base)
	{
		if (isValidInitialize(maxRows) && isValidInitialize(maxColumns))
		{
			this.maxRows = maxRows;
			this.maxColumns = maxColumns;
			try
			{
				this.map = new char[this.maxRows][this.maxColumns];
				for (maxRows = 0; maxRows < this.maxRows; maxRows++)
				{
					for (maxColumns = 0; maxColumns < this.maxColumns; maxColumns++)
					{
						this.map[maxRows][maxColumns] = base;
					}
				}
			}
			catch (OutOfMemoryError e)
			{
				System.out.println("Out of memory exception.");
			}
			catch (Exception e)
			{
				System.out.println("Unhandled exception.");
			}
		}
		
		else
		{
			System.out.println("The inputs for maxRows and maxColumns were not valid and no map was instantiated");
		}
	}
	
	/* Creator: Brandon Smith
	 * Purpose: Let other classes access the maxRows and maxColumns values for any given dimension given as a String (Row, Column).
	 * Arguments: String dimension.
	 * Returns: int maxRows or int maxColumns depending on the String passed through the arguments.  -1 if the wrong string was passed.
	 * Notes: Getter method.
	 */
	public int getMaxDimension(String dimension)
	{
		if (dimension.equals("Row"))
		{
			return this.maxRows;
		}
		else if (dimension.equals("Column"))
		{
			return this.maxColumns;
		}
		else
		{
			System.out.println("Invalid dimension, accepts only \"Row\" or \"Column\".  Returning -1.");
			return -1;
		}
	}
	
	/* Creator: Brandon Smith
	 * Purpose: To change the character at a given index.
	 * Arguments: int row, int column, char character.
	 * Returns: boolean true if the coordinate is successfully overwritten with the inputed character, otherwise returns false.
	 * Notes: public modifier so that the map might be updated using values inputed through MapMain, and actual indeces are 1 smaller than the
	 * requested input format.
	 */
	public boolean updateMap(int row, int column, char character)
	{
		try
		{
			this.map[row - 1][column - 1] = character;
			return true;
		}
		catch (IndexOutOfBoundsException e)
		{
			System.out.println("Invalid Row or Column.  Map failed to update.");
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Unhandled Exception");
			return false;
		}
	}
	
	/* Creator: Brandon Smith
	 * Purpose: To display the 2D character matrix to standard out.
	 * Arguments: N/A.
	 * Returns: N/A.
	 * Notes: public modifier so that the map might be printed from MapMain.
	 */
	public void printMap()
	{
		int row, column;
		for (row = 0; row < this.maxRows; row++)
		{
			for (column = 0; column < this.maxColumns; column++)
			{
				System.out.print(this.map[row][column]);
			}
			System.out.print("\n");
		}
	}
}
