#include <stdio.h>
#include <stdlib.h>

//Global Variables to make input validations easier.
int checkForNumber(char* string);
int checkFile(FILE* fp);

//Function headers.
void writeFromFileToSTDOut(FILE* fp, double a, double b);

int main(int argc, char *argv[])
{
	//Check if there are exactly three arguments.
	if (argc == 4)
	{
		//Check if the inputs for a and b are proper floating point numbers.
		if (checkForNumber(argv[2]) == 0 || checkForNumber(argv[3]) == 0)
		{
			printf("Error: bad float arg\n");
			return -1;
		}
		double a = atof(argv[2]);
		double b = atof(argv[3]);
		
		//Open the file at the given path and check if the file input is valid.
		FILE* fp = fopen(argv[1], "r");
		if (checkFile(fp) == 0)
		{
			printf("Error: bad file\n");
			return -1;
		}
		fclose(fp);
		
		//Re-open the file so that the internal pointer returns to the beginning.
		FILE* fp2 = fopen(argv[1], "r");
		writeFromFileToSTDOut(fp2, a, b);
	}
	
	else
	{
		printf("Error: Incorrect number of arguments\n");
	}
	return 0;
}

//Function to check if a string is a number. Returns 0 if it is not.
int checkForNumber(char* string)
{
	//Count to keep track of how many '.' characters are present.
	//PlaceHolder to keep track of whether or not the '-' (if present) is placed well.
	int count = 0;
	int placeHolder = 0;
	while (*string)
	{
		//Logic that ensures the only valid inputs are '-' (only in the leftmost digit), '.' (only one permitted), and the numbers '0' through '9'.
		if (!((placeHolder == 0 && (*string == 46 || (*string <= 57 && *string >= 48) || *string == 45)) || (placeHolder != 0 && (*string == 46 || (*string <= 57 && *string >= 48)))) || count >= 2)
		{
			return 0;
		}
		if (*string == 46)
		{
			count++;
		}
		placeHolder++;
		string++;
	}
	return 1;
}

//Function to check if the file is readable, and has 1 #A#, 1 #B#. Bad returns 0
int checkFile(FILE* fp)
{
	if (fp == NULL)
	{
		return 0;
	}
	
	//Check if the inputted file contains exactly one of both "#A#" and "#B#".
	else
	{
		int aCount = 0;
		int bCount = 0;
		char read;
		read = fgetc(fp);
		do
		{
			if (read == '#')
			{
				read = fgetc(fp);
				if (read == 'A')
				{
					read = fgetc(fp);
					if (read == '#')
					{
						aCount++;
						read = fgetc(fp);
					}
				}
				else if (read == 'B')
				{
					read = fgetc(fp);
					if (read == '#')
					{
						bCount++;
						read = fgetc(fp);
					}
				}
			}
			else
			{
				read = fgetc(fp);
			}
		} while (read != EOF);
		if (aCount == 1 && bCount == 1)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}

//Function to copy the contents of the file to stdout (replacing "#A#" and "#B#" with the arguments a and b.
void writeFromFileToSTDOut(FILE* fp, double a, double b)
{
	//Temp variables to avoid issues with being unable to step the internal file pointer backwards.
	char temp1;
	char temp2;
	char temp3;
	temp1 = fgetc(fp);
	do
	{
		if (temp1 == '#')
		{
			temp2 = fgetc(fp);
			if (temp2 == 'A')
			{
				temp3 = fgetc(fp);
				if (temp3 == '#')
				{
					printf("%.6f", a);
					temp1 = fgetc(fp);
				}
			}
			else if (temp2 == 'B')
			{
				temp3 = fgetc(fp);
				if (temp3 == '#')
				{
					printf("%.6f", b);
					temp1 = fgetc(fp);
				}
			}
			else
			{
				putchar(temp1);
				putchar(temp2);
				temp1 = fgetc(fp);
			}
		}
		else
		{
			putchar(temp1);
			temp1 = fgetc(fp);
		}
	} while (temp1 != EOF);
}

