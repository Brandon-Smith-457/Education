/* FILE: A2_bmp_helpers.c is where you will code your answers for Assignment 2.
 * 
 * Each of the functions below can be considered a start for you. 
 *
 * You should leave all of the code as is, except for what's surrounded
 * in comments like "REPLACE EVERTHING FROM HERE... TO HERE.
 *
 * The assignment document and the header A2_bmp_headers.h should help
 * to find out how to complete and test the functions. Good luck!
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <assert.h>

int bmp_open( char* bmp_filename,        unsigned int *width, 
              unsigned int *height,      unsigned int *bits_per_pixel, 
              unsigned int *padding,     unsigned int *data_size, 
              unsigned int *data_offset, unsigned char** img_data ){

              
  // YOUR CODE FOR Q1 SHOULD REPLACE EVERYTHING FROM HERE
  //Open bmp_filename for binary reading
	FILE* fp = fopen(bmp_filename, "rb");
	if (fp != NULL)
	{
		//Check if it's a bmp file
		char b, m;
		fread(&b, 1, 1, fp);
		fread(&m, 1, 1, fp);
		if (b == 'B' && m == 'M')
		{
			//Read the size of the bmp (4 bytes) and store it in the address pointed to by data_size, and then use it to allocate heap space for the img_data
			fread(data_size, 1, sizeof(unsigned int), fp);
			*img_data = (unsigned char*) malloc(*data_size);
			//Reset the file pointer
			fclose(fp);
			fp = fopen(bmp_filename, "rb");
			//Read the full bmp file and store the data at the address pointed to by *img_data (the pointer in the parent scope (ie one stack scope below))
			fread(*img_data, 1, *data_size, fp);
			
			//Using the *img_data pointer read and store the necessary header information
			*data_offset = *((unsigned int*) (*img_data+10));
			*width = *((unsigned int*) (*img_data+18));
			*height = *((unsigned int*) (*img_data+22));
			*bits_per_pixel = *((unsigned int*) (*img_data+28));
			
			//Calculate the padding
			*padding = ((*width)*(*bits_per_pixel)/8)%4;
			if (*padding != 0)
			{
				*padding = 4-((((*width)*(*bits_per_pixel))/8)%4);
			}
		}
		else
		{
			printf("The file header is not of the BMP format\n");
			return -1;
		}
	}
	else
	{
		printf("The given file name is not a valid file\n");
		return -1;
	}
  // TO HERE
  
  return 0;  
}

// We've implemented bmp_close for you. No need to modify this function
void bmp_close( unsigned char **img_data ){

  if( *img_data != NULL ){
    free( *img_data );
    *img_data = NULL;
  }
}

int bmp_mask( char* input_bmp_filename, char* output_bmp_filename, 
              unsigned int x_min, unsigned int y_min, unsigned int x_max, unsigned int y_max,
              unsigned char red, unsigned char green, unsigned char blue )
{
  unsigned int img_width;
  unsigned int img_height;
  unsigned int bits_per_pixel;
  unsigned int data_size;
  unsigned int padding;
  unsigned int data_offset;
  unsigned char* img_data    = NULL;
  
  int open_return_code = bmp_open( input_bmp_filename, &img_width, &img_height, &bits_per_pixel, &padding, &data_size, &data_offset, &img_data ); 
  
  if( open_return_code ){ printf( "bmp_open failed. Returning from bmp_mask without attempting changes.\n" ); return -1; }
 
  // YOUR CODE FOR Q2 SHOULD REPLACE EVERYTHING FROM HERE
	//Create heap space for the img_data_mask (will contain the data that we want to output in bmp format) and then make an identical copy of the input using memcpy()
	unsigned char* img_data_mask = (unsigned char*) malloc(data_size);
	memcpy(img_data_mask, img_data, data_size);
	//Information to allow for matrix like indexing of a linear data space
	unsigned int num_colors = bits_per_pixel/8;
	//The actual image data (pixels)
	unsigned char *pixel_data = img_data_mask + data_offset;
	
	//The boundaries of i and j are < and <= respectively because the image given in the pdf (sample output) appears to follow this behaviour
	//OTHERWISE I WOULD HAVE USED <= FOR BOTH BECAUSE THE SPECIFICATIONS IN THE HEADER FILE STATES THAT BOTH BONDARIES ARE SUPPOSED TO BE INCLUSIVE!!!
	for (unsigned int i = x_min; i < x_max; i++)
	{
		for (unsigned int j = y_min; j <= y_max; j++)
		{
			//Linear data matrix esque indexing
			pixel_data[j*(img_width*num_colors+padding)+i*num_colors+2] = red;
			pixel_data[j*(img_width*num_colors+padding)+i*num_colors+1] = green;
			pixel_data[j*(img_width*num_colors+padding)+i*num_colors+0] = blue;
		}
	}
	
	//Write the output bmp to a file with name given through output_bmp_filename
	FILE* fp = fopen(output_bmp_filename, "wb");
	fwrite(img_data_mask, 1, data_size, fp);
	bmp_close(&img_data_mask);
  // TO HERE!
  
  bmp_close( &img_data );
  
  return 0;
}         

int bmp_collage( char* bmp_input1, char* bmp_input2, char* bmp_result, int x_offset, int y_offset ){

  unsigned int img_width1;
  unsigned int img_height1;
  unsigned int bits_per_pixel1;
  unsigned int data_size1;
  unsigned int padding1;
  unsigned int data_offset1;
  unsigned char* img_data1    = NULL;
  
  int open_return_code = bmp_open( bmp_input1, &img_width1, &img_height1, &bits_per_pixel1, &padding1, &data_size1, &data_offset1, &img_data1 ); 
  
  if( open_return_code ){ printf( "bmp_open failed for %s. Returning from bmp_collage without attempting changes.\n", bmp_input1 ); return -1; }
  
  unsigned int img_width2;
  unsigned int img_height2;
  unsigned int bits_per_pixel2;
  unsigned int data_size2;
  unsigned int padding2;
  unsigned int data_offset2;
  unsigned char* img_data2    = NULL;
  
  open_return_code = bmp_open( bmp_input2, &img_width2, &img_height2, &bits_per_pixel2, &padding2, &data_size2, &data_offset2, &img_data2 ); 
  
  if( open_return_code ){ printf( "bmp_open failed for %s. Returning from bmp_collage without attempting changes.\n", bmp_input2 ); return -1; }
  
  // YOUR CODE FOR Q3 SHOULD REPLACE EVERYTHING FROM HERE
	//Make sure the bits_per_pixel fields are the same
	if (bits_per_pixel1 != bits_per_pixel2)
	{
		printf("Incompatible bits_per_pixel fields");
		return -1;
	}
	//Determine the new parameters of the image width and height dependent on the offsets given
	unsigned int img_width_collage;
	unsigned int img_height_collage;
	if (x_offset >= 0)
	{
		if (img_width1 >= (img_width2 + x_offset))
		{
			img_width_collage = img_width1;
		}
		else
		{
			img_width_collage = img_width2 + x_offset;
		}
	}
	else
	{
		if (img_width2 >= (img_width1 - x_offset))
		{
			img_width_collage = img_width2;
		}
		else
		{
			img_width_collage = img_width1 - x_offset;
		}
	}
	if (y_offset >= 0)
	{
		if (img_height1 >= (img_height2 + y_offset))
		{
			img_height_collage = img_height1;
		}
		else
		{
			img_height_collage = img_height2 + y_offset;
		}
	}
	else
	{
		if (img_height2 >= (img_height1 - y_offset))
		{
			img_height_collage = img_height2;
		}
		else
		{
			img_height_collage = img_height1 - y_offset;
		}
	}
	
	//Allocating memory for the new bmp file (Taking the larger of the two data_offsets) and determining the new fields that will be changing
	unsigned int data_offset_collage;
	int temp;
	if (data_offset1 >= data_offset2)
	{
		data_offset_collage = data_offset1;
		temp = 1;
	}
	else
	{
		data_offset_collage = data_offset2;
		temp = 2;
	}
	unsigned int padding_collage = ((img_width_collage)*(bits_per_pixel1)/8)%4;
	if (padding_collage != 0)
	{
		padding_collage = 4-((((img_width_collage)*(bits_per_pixel1))/8)%4);
	}
	unsigned int data_size_collage = (((img_width_collage*bits_per_pixel1)/8 + padding_collage)*img_height_collage) + data_offset_collage;
	unsigned char* img_data_collage = (unsigned char*) malloc(data_size_collage);
	
	//Copying the header from the file with the larger data_offset
	if (temp = 1)
	{
		memcpy(img_data_collage, img_data1, data_offset_collage);
	}
	else
	{
		memcpy(img_data_collage, img_data2, data_offset_collage);
	}
	
	//Replacing the data_size, width, and height fields
	unsigned int* temp_int;
	unsigned char* temp_address;
	temp_address = img_data_collage+2;
	temp_int = (unsigned int*) temp_address;
	*temp_int = data_size_collage;
	temp_address = img_data_collage+18;
	temp_int = (unsigned int*) temp_address;
	*temp_int = img_width_collage;
	temp_address = img_data_collage+22;
	temp_int = (unsigned int*) temp_address;
	*temp_int = img_height_collage;
	
	//Getting the start and end indices for the two images in the new bmp coordinates
	unsigned int img1_start_x, img2_start_x, img1_start_y, img2_start_y, img1_end_x, img2_end_x, img1_end_y, img2_end_y;
	if (x_offset >= 0 && y_offset >=0)
	{
		img1_start_x = 0;
		img1_start_y = 0;
		img2_start_x = x_offset;
		img2_start_y = y_offset;
	}
	else if (x_offset >=0 && y_offset < 0)
	{
		img1_start_x = 0;
		img1_start_y = -y_offset;
		img2_start_x = x_offset;
		img2_start_y = 0;
	}
	else if (x_offset < 0 && y_offset >= 0)
	{
		img1_start_x = -x_offset;
		img1_start_y = 0;
		img2_start_x = 0;
		img2_start_y = y_offset;
	}
	else
	{
		img1_start_x = -x_offset;
		img1_start_y = -y_offset;
		img2_start_x = 0;
		img2_start_y = 0;
	}
	img1_end_x = img1_start_x + img_width1;
	img1_end_y = img1_start_y + img_height1;
	img2_end_x = img2_start_x + img_width2;
	img2_end_y = img2_start_y + img_height2;

	//Creating pointers to the pixel data and num_colors for linear matrix indexing
	unsigned int num_colors = bits_per_pixel1/8;
	unsigned char *pixel_data_collage = img_data_collage + data_offset_collage;
	unsigned char *pixel_data1 = img_data1 + data_offset1;
	unsigned char *pixel_data2 = img_data2 + data_offset2;
	
	//Looping through the pixels of the new bmp collage and pasting the images correctly
	for (unsigned int i = 0; i < img_width_collage; i++)
	{
		for (unsigned int j = 0; j < img_height_collage; j++)
		{
			if (i >= img2_start_x && i < img2_end_x && j >= img2_start_y && j < img2_end_y)
			{
				pixel_data_collage[j*(img_width_collage*num_colors+padding_collage)+i*num_colors+2] = pixel_data2[(j - img2_start_y)*(img_width2*num_colors+padding2)+(i - img2_start_x)*num_colors+2];
				pixel_data_collage[j*(img_width_collage*num_colors+padding_collage)+i*num_colors+1] = pixel_data2[(j - img2_start_y)*(img_width2*num_colors+padding2)+(i - img2_start_x)*num_colors+1];
				pixel_data_collage[j*(img_width_collage*num_colors+padding_collage)+i*num_colors+0] = pixel_data2[(j - img2_start_y)*(img_width2*num_colors+padding2)+(i - img2_start_x)*num_colors+0];
			}
			else if (i >= img1_start_x && i < img1_end_x && j >= img1_start_y && j < img1_end_y)
			{
				pixel_data_collage[j*(img_width_collage*num_colors+padding_collage)+i*num_colors+2] = pixel_data1[(j - img1_start_y)*(img_width1*num_colors+padding1)+(i - img1_start_x)*num_colors+2];
				pixel_data_collage[j*(img_width_collage*num_colors+padding_collage)+i*num_colors+1] = pixel_data1[(j - img1_start_y)*(img_width1*num_colors+padding1)+(i - img1_start_x)*num_colors+1];
				pixel_data_collage[j*(img_width_collage*num_colors+padding_collage)+i*num_colors+0] = pixel_data1[(j - img1_start_y)*(img_width1*num_colors+padding1)+(i - img1_start_x)*num_colors+0];
			}
			//FILL THE EMPTY SPACE WITH WHITE PIXELS
			else
			{
				pixel_data_collage[j*(img_width_collage*num_colors+padding_collage)+i*num_colors+2] = 255;
				pixel_data_collage[j*(img_width_collage*num_colors+padding_collage)+i*num_colors+1] = 255;
				pixel_data_collage[j*(img_width_collage*num_colors+padding_collage)+i*num_colors+0] = 255;
			}
		}
	}

	//Writing the output collaged image to the file with name given in bmp_result
	FILE* fp = fopen(bmp_result, "wb");
	fwrite(img_data_collage, 1, data_size_collage, fp);
  // TO HERE!
      
  bmp_close( &img_data1 );
  bmp_close( &img_data2 );
  
  return 0;
}
