/* FILE: bmp_info.c is provided as part of COMP 206 A2. Do not modify this file.
 *       Its purpose is to test that you have implemented bmp_open correctly for
 *       Question 1. When you're finished, the output of this program should match
 *       the examples in the assignment document.
 */

#include <stdio.h>
#include <stdlib.h>
#include "A2_bmp_helpers.h"

int main( int argc, char* argv[] ){

  if( argc < 2 ){
    printf( "Usage: $ %s <bmp_filename>\n", argv[0] );
    exit(EXIT_FAILURE);
  }
  
  unsigned int image_width, image_height, bits_per_pixel, row_padding, data_size, data_offset;
  unsigned char *img_data = NULL;
  
  // NOTE: This is the line that executes the bmp_open function. It shows how your code will be called.
  int return_code = bmp_open( argv[1], &image_width, &image_height, &bits_per_pixel, &row_padding, &data_size, &data_offset, &img_data );
    
  if( return_code ){
    printf( "bmp_info function returned an error. Cannot report file information.\n" );
    return return_code;
  }

  printf( "bmp_info for file %s:\n", argv[1] );
  printf( "  width         = %d\n", image_width );
  printf( "  height        = %d\n", image_height );
  printf( "  bpp           = %d\n", bits_per_pixel );
  printf( "  padding       = %d\n", row_padding );
  printf( "  data_offset   = %d\n", data_offset );
    
  unsigned int middle_row = image_height/2;
  unsigned int middle_col = image_height/2;
  unsigned int num_colors = bits_per_pixel/8;
  unsigned char *pixel_data = img_data + data_offset;
    
  if( img_data==NULL )
    printf( "Cannot print pixel info because NULL pointer.\n" ); 
  else{
    printf( "  The middle pixel has (X,Y)=(%d,%d) and\n", middle_row, middle_col  );
    printf( "  color (R,G,B)=(%d,%d,%d).\n",
            pixel_data[ middle_row*(image_width*num_colors+row_padding) + middle_col*num_colors + 2 ],
            pixel_data[ middle_row*(image_width*num_colors+row_padding) + middle_col*num_colors + 1 ],
            pixel_data[ middle_row*(image_width*num_colors+row_padding) + middle_col*num_colors + 0 ] );      
  }
    
  return return_code;
}

