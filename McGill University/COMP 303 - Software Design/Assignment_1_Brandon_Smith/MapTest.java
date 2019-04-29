package mapPackage;

public class MapTest {
	private static int i, j;
	private static Map myMap;

	public static void main (String[] args)
	{
		isValidInitializeTest();
		mapTest();
		getMaxDimensionTest();
		isValidMapTest();
		isValidCoordinateTest();
		updateMapTest();
	}
	
	private static void isValidInitializeTest()
	{
		System.out.println("Test case for method <<isValidInitialize(int input)>> :");
		for (i = -3; i < 4; i++)
		{
			System.out.println("Arguments: " + i + ". Result: " + Map.isValidInitialize(i));
		}
	}
	
	private static void mapTest()
	{
		System.out.println("\nTest case for constructor method <<Map(int maxRows, int maxColumns, char base)>> and method <<printMap()>>");
		for (i = -3; i < 4; i++)
		{
			for (j = -3; j < 4; j++)
			{
				myMap = new Map(i, j, '~');
				System.out.println("Arguments: " + i + ", " + j + ". Result:");
				myMap.printMap();
			}
		}
	}
	
	private static void getMaxDimensionTest()
	{
		System.out.println("\nTest case for method <<getMaxDimension(String dimension)>>");
		for (i = -3; i < 4; i++)
		{
			for (j = -3; j < 4; j++)
			{
				myMap = new Map (i, j, '~');
				System.out.println("Arguments: " + i + ", " + j + ", \"Row\". Result:" + myMap.getMaxDimension("Row"));
				System.out.println("Arguments: " + i + ", " + j + ", \"Column\". Result:" + myMap.getMaxDimension("Column"));
				System.out.println("Arguments: " + i + ", " + j + ", \"Giberish\". Result:" + myMap.getMaxDimension("Giberish"));
			}
		}
	}
	
	private static void isValidMapTest()
	{
		System.out.println("\nTest case for method <<isValidMap()>>");
		for (i = -3; i < 4; i++)
		{
			for (j = -3; j < 4; j++)
			{
				myMap = new Map (i, j, '~');
				System.out.println("Arguments: " + i + ", " + j + ". Result:" + myMap.isValidMap());
			}
		}
	}
	
	private static void isValidCoordinateTest()
	{
		System.out.println("\nTest case for method <<isValidCoordinate(int input, String dimension)>>\n3x3 Map instantiated.");
		myMap = new Map (3, 3, '~');
		for (i = -1; i < 5; i++)
		{
			for (j = -1; j < 5; j++)
			{
				System.out.println("Arguments: " + i + ", \"Row\". Result:" + myMap.isValidCoordinate(i, "Row"));
				System.out.println("Arguments: " + i + ", \"Giberish\". Result:" + myMap.isValidCoordinate(i, "Giberish"));
				System.out.println("Arguments: " + j + ", \"Column\". Result:" + myMap.isValidCoordinate(j, "Column"));
				System.out.println("Arguments: " + j + ", \"Giberish\". Result:" + myMap.isValidCoordinate(j, "Giberish"));
			}
		}
	}
	
	private static void updateMapTest()
	{
		System.out.println("\nTest case for method <<updateMap(int row, int column, char character)>>\n3x3 Map instantiated.");
		myMap = new Map (3, 3, '~');
		for (i = -1; i < 5; i++)
		{
			for (j = -1; j < 5; j++)
			{
				System.out.println("Arguments: " + i + ", " + j + " \'G\'. Result:" + myMap.updateMap(i, j, 'G'));
				myMap.printMap();
				System.out.println("Arguments: " + i + ", " + j + " \'#\'. Result:" + myMap.updateMap(i, j, '#'));
				myMap.printMap();
				System.out.println("Arguments: " + i + ", " + j + " \'~\'. Result:" + myMap.updateMap(i, j, '~'));
				myMap.printMap();
				System.out.println("Arguments: " + i + ", " + j + " \'N\'. Result:" + myMap.updateMap(i, j, 'N'));
				myMap.printMap();
			}
		}
	}
}
