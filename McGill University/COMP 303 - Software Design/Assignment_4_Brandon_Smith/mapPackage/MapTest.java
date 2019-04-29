/**
 * 
 */
package mapPackage;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * @author Brandon Smith
 *
 */
public class MapTest {
	int i, j;
	Map testMap;
	
	/**
	 * Run all of the tests and print the failures.
	 * @param args Not Used
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(MapTest.class);
		for (Failure failure : result.getFailures())
		{
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
	}
	/**
	 * Test method for {@link mapPackage.Map#isValidInitialize(int)}.
	 */
	@Test
	public void testIsValidInitialize() {
		for (i = -3; i < 4; i++)
		{
			if (i <= 0)
			{
				assertEquals(false, Map.isValidInitialize(i));
			}
			else
			{
				assertEquals(true, Map.isValidInitialize(i));
			}
		}
	}

	/**
	 * Test method for {@link mapPackage.Map#isValidMap()}.
	 */
	@Test
	public void testIsValidMap() {
		for (i = -3; i < 4; i++)
		{
			for (j = -3; j < 4; j++)
			{
				testMap = new Map (i, j, '~');
				if (i <= 0 || j <= 0)
				{
					assertEquals(false, testMap.isValidMap());
				}
				else
				{
					assertEquals(true, testMap.isValidMap());
				}
			}
		}
	}

	/**
	 * Test method for {@link mapPackage.Map#isValidCoordinate(int, java.lang.String)}.
	 */
	@Test
	public void testIsValidCoordinate() {
		testMap = new Map (3, 3, '~');
		for (i = -1; i < 5; i++)
		{
			for (j = -1; j < 5; j++)
			{
				if (i >= 1 && i < 4)
				{
					assertEquals(true, testMap.isValidCoordinate(i, "Row"));
				}
				else
				{
					assertEquals(false, testMap.isValidCoordinate(i, "Row"));
				}
				if (j >= 1 && j < 4)
				{
					assertEquals(true, testMap.isValidCoordinate(j, "Column"));
				}
				else
				{
					assertEquals(false, testMap.isValidCoordinate(j, "Column"));
				}
				assertEquals(false, testMap.isValidCoordinate(i, "Giberish"));
				assertEquals(false, testMap.isValidCoordinate(j, "Giberish"));
			}
		}
	}

	/**
	 * Test method for {@link mapPackage.Map#getMaxDimension(java.lang.String)}.
	 */
	@Test
	public void testGetMaxDimension() {
		for (i = -3; i < 4; i++)
		{
			for (j = -3; j < 4; j++)
			{
				testMap = new Map (i, j, '~');
				if (i > 0 && j > 0)
				{
					assertEquals(i, testMap.getMaxDimension("Row"));
					assertEquals(j, testMap.getMaxDimension("Column"));
				}
				else
				{
					assertEquals(0, testMap.getMaxDimension("Row"));
					assertEquals(0, testMap.getMaxDimension("Column"));
				}
				assertEquals(-1, testMap.getMaxDimension("Giberish"));
			}
		}
	}
}
