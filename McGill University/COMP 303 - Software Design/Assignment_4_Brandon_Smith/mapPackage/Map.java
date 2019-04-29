package mapPackage;

/**
 * <h1>Map Class</h1>The Map class contains the data of a specific 2D character map and the methods to initialize,
 * and update the characters of the map.  It also contains a method for input validation.
 * 
 * @author Brandon Smith
 * @version 1.0
 * @since 2018-10-18
 */
public class Map {
	private int maxRows;
	private int maxColumns;
	private char[][] map;
	
	/**
	 * Validate whether or not a given int is strictly greater than 0.
	 * <br><span style="margin-left:2em">
	 * <b>Note:</b> Public modifier so that validation can be done immediately when inputed by user in MapMain.</span>
	 * @param input int:<br><span style="margin-left:2em">
	 * Input to validate.</span>
	 * @return boolean:<br><span style="margin-left:2em">
	 * true - condition is met</span><br><span style="margin-left:2em">
	 * false - condition has not been met.</span>
	 */
	public static boolean isValidInitialize(int input)
	{
		return (input > 0);
	}
	
	/**
	 * Validate whether or not 2D character matrix has been initialized with the correct number of rows and columns.
	 * <br><span style="margin-left:2em">
	 * <b>Note:</b> Public modifier so that if validation fails in MapMain, the program may be terminated.</span>
	 * @return boolean:<br><span style="margin-left:2em">
	 * true - 2D character matrix has been initialized with the correct number of rows and columns.</span><br><span style="margin-left:2em">
	 * false - 2D character matrix has not been initialized with the correct number of rows and columns.</span>
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
	
	/**
	 * Validate whether or not a given integer input exists within the range of valid coordinates for any given dimension
	 * given as a String (Row, Column).
	 * <br><span style="margin-left:2em">
	 * <b>Note:</b> Public modifier so that if validation fails in MapMain, the program may ask for another integer.</span>
	 * @param input int:<br><span style="margin-left:2em">
	 * Index to validate.</span>
	 * @param dimension String:<br><span style="margin-left:2em">
	 * "Row" - Check if input is a valid index for the rows.</span><br><span style="margin-left:2em">
	 * "Column" - Check if input is a valid index for the columns.</span>
	 * @return boolean:<br><span style="margin-left:2em">
	 * true - input is a valid index</span><br><span style="margin-left:2em">
	 * false - dimension is not a recognized input.</span><br><span style="margin-left:2em">
	 * false - input is an invalid index.</span>
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
			return false;
		}
	}
	
	/**
	 * If maxRows and maxColumns are integers greater than 0, assign their values to the current instance's maxRows and maxColumns,
	 * then construct a 2D character matrix of dimensions maxRows and maxColumns.  Initialize every element in the matrix to the inputed base
	 * char.  Print error messages if any of these steps fail.
	 * @param maxRows int:<br><span style="margin-left:2em">
	 * Number of rows the map will contain.</span>
	 * @param maxColumns int:<br><span style="margin-left:2em">
	 * Number of columns the map will contain.</span>
	 * @param base character:<br><span style="margin-left:2em">
	 * Initial state of the map.</span>
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
	}
	
	/**
	 * Let other classes access the maxRows and maxColumns values for any given dimension given as a String (Row, Column).
	 * Arguments: String dimension.<br><span style="margin-left:2em">
	 * <b>Note:</b> Getter method.</span>
	 * @param dimension String:<br><span style="margin-left:2em">
	 * "Row" - Request the number of rows.</span><br><span style="margin-left:2em">
	 * "Column" - Request the number of columns.</span>
	 * @return int:<br><span style="margin-left:2em">
	 * maxRows - dimension is "Row".</span><br><span style="margin-left:2em">
	 * maxColumns - dimension is "Column".</span><br><span style="margin-left:2em">
	 * -1 - dimension is neither "Row" nor "Column".</span>
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
			return -1;
		}
	}
	
	/**
	 * Change the character at a given index.<br><span style="margin-left:2em">
	 * <b>Note:</b> Public modifier so that the map might be updated using values inputed through MapMain, and actual indeces are 1 smaller than the
	 * requested input format.</span>
	 * @param row int:<br><span style="margin-left:2em">
	 * Row index.</span>
	 * @param column int:<br><span style="margin-left:2em">
	 * Column index.</span>
	 * @param character char:<br><span style="margin-left:2em">
	 * Character to insert and given coordinate.</span>
	 * @return boolean:<br><span style="margin-left:2em">
	 * true - map coordinate was successfully overwritten with the inputed character.</span><br><span style="margin-left:2em">
	 * false - map was unsuccessfully updated.</span>
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
	
	/**
	 * Display the 2D character matrix to standard out.<br><span style="margin-left:2em">
	 * <b>Note:</b> Public modifier so that the map might be printed from MapMain.</span>
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
