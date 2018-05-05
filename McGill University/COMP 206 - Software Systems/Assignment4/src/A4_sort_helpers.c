#include "A4_sort_helpers.h"

// Function: read_all() 
// Provided to read an entire file, line by line.
// No need to change this one.
void read_all( char *filename ){
    
    FILE *fp = fopen( filename, "r" );
    int curr_line = 0;
	
    while( curr_line < MAX_NUMBER_LINES && 
           fgets( text_array[curr_line], MAX_LINE_LENGTH, fp ) )
    {
        curr_line++;
    }
	
    text_array[curr_line][0] = '\0';
    fclose(fp);
}

// Function: read_by_letter() 
// Provided to read only the lines of a file starting with first_letter.
// No need to change this one.
void read_by_letter( char *filename, char first_letter ){

    FILE *fp = fopen( filename, "r" );
    int curr_line = 0;
    text_array[curr_line][0] = '\0';
	
    while( fgets( text_array[curr_line], MAX_LINE_LENGTH, fp ) ){
        if( text_array[curr_line][0] == first_letter ){
            curr_line++;
        }

        if( curr_line == MAX_NUMBER_LINES ){
            sprintf( buf, "ERROR: Attempted to read too many lines from file.\n" );
            write( 1, buf, strlen(buf) );
            break;
        }
    }
	
    text_array[curr_line][0] = '\0';
    fclose(fp);
}

// YOU COMPLETE THIS ENTIRE FUNCTION FOR Q1.
void sort_words( ){
	char temp1[MAX_LINE_LENGTH]; //Temp to keep track of the current element to be sorted
	char temp2[MAX_LINE_LENGTH]; //Temp to allow for the swap of elements in text_array witout loss of data
	int i = 0;
    while (text_array[i][0] != '\0')
	{
		strcpy(temp1, text_array[i]); //Element to be sorted in temp1
		int j = i - 1; //First index we need to check (iteration backwards)
		while (strcmp(temp1, text_array[j]) < 0 && j >=0) //Check if the element to be sorted is lower in the alphabet than element j
		{
			//Swap element j with j+1 if element to be sorted is lower in alphabet
			strcpy(temp2, text_array[j+1]);
			strcpy(text_array[j+1], text_array[j]);
			strcpy(text_array[j], temp2);
			j--;
		}
		i++;
	}
}

// YOU COMPLETE THIS ENTIRE FUNCTION FOR Q2.
//27 semaphores
sem_t* s[27];

int initialize( ){
    sem_unlink("Brandon's a"); //Ensure that Brandon's a is not an already existing semaphore
	s[0] = sem_open("Brandon's a", O_CREAT, 0666, 1); //Initialize the semaphore with 1 to ensure that it is the first that runs
	for (int i = 1; i < 26; i++)
	{
		char temp[12]; //Do the same for the rest of the semaphores b - z
		sprintf(temp, "Brandon's %c", (char)('a' + i));
		sem_unlink(temp);
		s[i] = sem_open(temp, O_CREAT, 0666, 0);
	}
	sem_unlink("Brandon's Finalize"); //A semaphore to control the main process
	s[26] = sem_open("Brandon's Finalize", O_CREAT, 0666, 0);
    return 0;
}

// YOU MUST COMPLETE THIS FUNCTION FOR Q2 and Q3.   
int process_by_letter( char* input_filename, char first_letter ){
    // For Q2, keep the following 2 lines in your solution (maybe not at the start).
    // Add lines above or below to ensure the "This process will sort..." lines
    // are printed in the right order (alphabetical).
	int i = first_letter - 'a'; //Index based on the character passed through
	sem_wait(s[i]); //All semaphores increment down and so all processes stop except for the 'a' at first
	read_by_letter(input_filename, first_letter); //Read all the 'a' words into text_array
	sort_words(); //Sort all of the words currently in text_array
	FILE* fp; //Temp file to transfer information between processes
	if (first_letter == 'a')
	{
		fp = fopen("outputs/temp.txt", "w"); //"w" for the 'a' so that any existing temp.txt gets overwritten
	}
	else
	{
		fp = fopen("outputs/temp.txt", "a");
	}
	int j = 0;
	while( text_array[j][0] != '\0' )
	{
		fputs(text_array[j], fp); //Putting text_array info into the file
		j++;
	}
	fclose(fp);
	if (i < 26)
	{
		//When 'a' is done tell 'b' to go, so on and so forth ('z' tells finalize to occur)
		sem_post(s[i + 1]);
	}

    // For Q3, uncomment the following 2 lines and integrate them with your overall solution.
    // read_by_letter( input_filename, first_letter );
    // sort_words( );

    return 0;
}

// YOU COMPLETE THIS ENTIRE FUNCTION FOR Q2 and Q3.
int finalize( ){
    // For Q2, keep the following 2 lines in your solution (maybe not at the start).
    // Add lines above or below to ensure the "Sorting complete!" line
    // is printed at the very end, after all letter lines.
	sem_wait(s[26]); //Wait unti 'z' is finished executing
	read_all("outputs/temp.txt"); //Read from the temp.txt created in the sort process
	int curr_line = 0;
	while( text_array[curr_line][0] != '\0' ) //Write all of the sorted data to standard out
	{
		sprintf( buf, "%s",  text_array[curr_line] );
		write(1,buf,strlen(buf));
		curr_line++;
	}
    sprintf( buf, "Sorting complete!\n" );
    write( 1, buf, strlen(buf) );

    // For Q3, come up with a way to accumulate the sorted results from each
    // letter process and print the overall sorted values to standard out.
    // You are not allowed to read from the input file, or call sort_words
    // directly from this function.

    return 0;
}

