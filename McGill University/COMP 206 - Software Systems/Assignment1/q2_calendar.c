#include <stdio.h>
#include <stdlib.h>

//Global variables so as to avoid the requirement for additional variables than necessary.
int monthTracker;
int dayTracker;
int firstDay;
int dayNumber;
int width;

//Function Headers
void printSeparatorLine();
void printMonthName();
void printMonthHeader();
void printDayNameHeader(int daySize);
void printOneDayName(int daySize);
void printDayNumbers(int firstDay);
void printDayRow(int firstDay);
void printOneDayNumber(int modifier);


int main (int argc, char* argv[])
{
	//Check if the correct number of arguments were provided
	if (argc == 3)
	{
		int daySize = atoi(argv[1]);
		firstDay = atoi(argv[2]);
		
		//Check to see if the inputs are correct.
		if (daySize <= 1)
		{
			printf("ERROR: Cannot print days smaller than size 2\n");
			if (firstDay <=0 || firstDay >=8)
			{
				printf("ERROR: The first day of the week must be between 1 and 7.\n");
			}
			return -1;
		}
		if (firstDay <= 0 || firstDay >= 8)
		{
			printf("ERROR: The first day of the week must be between 1 and 7.\n");
			if (daySize <= 1)
			{
				printf("ERROR: Cannot print days smaller than size 2\n");
			}
			return -1;
		}
		
		//Initializing the width of any one row of the calendar.
		width = ((daySize+3)*7 + 1);
		//MonthTracker to print a different month each time printMonthHeader() is called.
		monthTracker = 1;
		//Printing the twelve months of the year
		for (int i = 0; i < 12; i++)
		{
			printMonthHeader();
			//dayTracker to print a different dayName each time printDayRow() is called.
			dayTracker = 1;
			printDayNameHeader(daySize);
			//dayNumber initialized as such so that we can loop through incrementing dayNumber and print out spaces when dayNumber is negative and the actual day number when it's positive.
			dayNumber = 2 - firstDay;
			printDayNumbers(firstDay);
		}
		//The final separator line as by the specification.
		printSeparatorLine();
	}
	
	else
	{
		printf("Error: Incorrect number of arguments");
	}
	return 0;
}

//Function to print a separator line.
void printSeparatorLine()
{
	for (int i = 0; i < width; i++)
	{
		if (i == 0)
		{
			putchar('|');
		}
		else if (i == (width - 1))
		{
			putchar('|');
			putchar('\n');
		}
		else
		{
			putchar('-');
		}
	}
}

//Function to print the month name.
void printMonthName()
{
	int monthChars;
	char* monthName;
	
	//Check which month is to be printed, and how many characters said name will contain.
	if (monthTracker == 1)
	{
		monthName = "January";
		monthChars = 7;
		monthTracker++;
	}
	else if (monthTracker == 2)
	{
		monthName = "February";
		monthChars = 8;
		monthTracker++;
	}
	else if (monthTracker == 3)
	{
		monthName = "March";
		monthChars = 5;
		monthTracker++;
	}
	else if (monthTracker == 4)
	{
		monthName = "April";
		monthChars = 5;
		monthTracker++;
	}
	else if (monthTracker == 5)
	{
		monthName = "May";
		monthChars = 3;
		monthTracker++;
	}
	else if (monthTracker == 6)
	{
		monthName = "June";
		monthChars = 4;
		monthTracker++;
	}
	else if (monthTracker == 7)
	{
		monthName = "July";
		monthChars = 4;
		monthTracker++;
	}
	else if (monthTracker == 8)
	{
		monthName = "August";
		monthChars = 6;
		monthTracker++;
	}
	else if (monthTracker == 9)
	{
		monthName = "September";
		monthChars = 9;
		monthTracker++;
	}
	else if (monthTracker == 10)
	{
		monthName = "October";
		monthChars = 7;
		monthTracker++;
	}
	else if (monthTracker == 11)
	{
		monthName = "November";
		monthChars = 8;
		monthTracker++;
	}
	else if (monthTracker == 12)
	{
		monthName = "December";
		monthChars = 8;
		monthTracker++;
	}
	
	//Variable to keep track of where in the for loop (that prints the Month Name Row) to insert the name.
	int namePlace;
	//Four cases, odd/even width + odd/even monthChars.
	if ((width%2) == 1)
	{
		if ((monthChars%2) == 1)
		{
			namePlace = (width/2) - (monthChars/2);
		}
		else if ((monthChars%2) == 0)
		{
			namePlace = (width/2) - (monthChars/2);
		}
	}
	else if ((width%2) == 0)
	{
		if ((monthChars%2) == 1)
		{
			namePlace = (width/2) - (monthChars/2) - 1;
		}
		else if ((monthChars%2) == 0)
		{
			namePlace = (width/2) - (monthChars/2);
		}
	}
	
	//Print out the row.
	for (int i = 0; i < width; i++)
	{
		if (i == 0)
		{
			putchar('|');
		}
		else if (i == (width - 1))
		{
			putchar('|');
			putchar('\n');
		}
		else if (i == namePlace)
		{
			for (int j = 0; j < monthChars; j++)
			{
				putchar(*monthName);
				monthName++;
			}
			i = i + (monthChars - 1);
		}
		else
		{
			putchar(' ');
		}
	}
}

//Function to print a month header.
void printMonthHeader()
{
	printSeparatorLine();
	printMonthName();
	printSeparatorLine();
}

//Function to print the days of the week.
void printDayNameHeader(int daySize)
{
	putchar('|');
	//Seven equal length segments of printing day names.
	for (int i = 0; i < 7; i++)
	{
		printOneDayName(daySize);
		dayTracker++;
	}
	printSeparatorLine();
}

//Function to print a single segment of the days of the week.
void printOneDayName(int daySize)
{
	char* dayName;
	int dayChars;
	
	//Checking the name and length similar to month method above.
	if (dayTracker == 1)
	{
		dayName = "Sunday";
		dayChars = 6;
	}
	else if (dayTracker == 2)
	{
		dayName = "Monday";
		dayChars = 6;
	}
	else if (dayTracker == 3)
	{
		dayName = "Tuesday";
		dayChars = 7;
	}
	else if (dayTracker == 4)
	{
		dayName = "Wednesday";
		dayChars = 9;
	}
	else if (dayTracker == 5)
	{
		dayName = "Thursday";
		dayChars = 8;
	}
	else if (dayTracker == 6)
	{
		dayName = "Friday";
		dayChars = 6;
	}
	else if (dayTracker == 7)
	{
		dayName = "Saturday";
		dayChars = 8;
	}
	
	//Loop over the number of characters contained in one segment.
	for (int i = 0; i < (width-1)/7; i++)
	{
		//First char (after the '|') must be a ' '.
		if (i == 0)
		{
			putchar(' ');
		}
		//If daySize is smaller than or equal to dayChars and the loop has not exceeded daySize, print the dayName up to daySize characters.
		else if (daySize <= dayChars && i < daySize)
		{
			for (int j = 0; j < daySize; j++)
			{
				putchar(*dayName);
				dayName++;
			}
			i = i + (daySize - 1);
		}
		//If daySize is larger than dayChars and the loop has not exceeded dayChars, print the dayName up to dayChars characters.
		else if (daySize > dayChars && i < dayChars)
		{
			for (int j = 0; j < dayChars; j++)
			{
				putchar(*dayName);
				dayName++;
			}
			i = i + (dayChars - 1);
		}
		else if (i == ((width-1)/7 - 1))
		{
			putchar('|');
			//If the day is a Saturday, print a new line character at the end.
			if (dayTracker == 7)
			{
				putchar('\n');
			}
		}
		//Every other space (up to the end of the for loop) is filled with ' '.
		else
		{
			putchar(' ');
		}
	}
}

//Function to print all the day numbers.
void printDayNumbers(int firstDay)
{
	//If the user chose the first day to be sunday, then we will be printing 6 rows.
	if (firstDay == 7)
	{
		for (int i = 0; i < 6; i++)
		{
			printDayRow(firstDay);
		}
	}
	//Otherwise we will only be printing 5 rows.
	else
	{
		for (int i = 0; i < 5; i++)
		{
			printDayRow(firstDay);
		}
	}
}

//Function to print a single row of the day numbers.
void printDayRow(int firstDay)
{
	putchar('|');
	//Each row, after the first ('|'), is split into 7 segments similarly to above.
	for (int i = 0; i < 7; i++)
	{
		//If the dayNumber (initialized in main) is less than one then the segment will be a bunch of spaces ending in a '|'.
		if (dayNumber < 1)
		{
			printOneDayNumber(0);
			dayNumber++;
		}
		//The same if dayNumber is greater than thirty.
		else if (dayNumber > 30)
		{
			printOneDayNumber(0);
			dayNumber++;
		}
		//For dayNumber 1 to 30, print the actual number and fill in the rest with spaces.
		else
		{
			printOneDayNumber(1);
			dayNumber++;
		}
	}
	putchar('\n');
}

//Function to print a single segment of the day numbers.
void printOneDayNumber(int modifier)
{
	//Case for only printing spaces.
	if (modifier == 0)
	{
		for (int i = 0; i < (width-1)/7; i++)
		{
			if (i == ((width-1)/7 - 1))
			{
				putchar('|');
			}
			else
			{
				putchar(' ');
			}
		}
	}
	//Case for printing the actual numbers.
	else if (modifier == 1)
	{
		//Looping through the length of the segment.
		for (int i = 0; i < (width-1)/7; i++)
		{
			//end with a '|'.
			if (i == ((width-1)/7 - 1))
			{
				putchar('|');
			}
			//After a space at the start, print the number, and if the number is two digits increment the loop by 1.
			else if (i == 1)
			{
				if (dayNumber >= 10)
				{
					printf("%d", dayNumber);
					i++;
				}
				else
				{
					printf("%d", dayNumber);
				}
			}
			//Everything else is a space.
			else
			{
				putchar(' ');
			}
		}
		
		//Checking to see if the last day of the month was a Saturday, if so then the first day of the next month must be Sunday.
		if (firstDay == 7)
		{
			firstDay = 1;
		}
		//Increment the day to make sure that the next month starts on the next weekday.
		else
		{
			firstDay++;
		}
	}
}




