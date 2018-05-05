public class ISBN {

	public static void main(String[] args) {
	
		/*
		Brandon_Smith
		*/
	
		//Declaring the variable to represent the ISBN number
        int n = Integer.parseInt(args[0]);
        
        
    	//Your code Starts here
        
        /*Declaring unchangeable variables that dictate:
          the message shown if the input is too large or too small,
          the output of d1 if it is 10.*/
        final String INPUT_ERR = "Please ensure the only input is a 4 digit base ten integer!";
        final String D10_OUT = "X";
        
        /*Declaring:
          four integers to hold the ISBN digits,
          an integer to hold the sum,
          and an integer for the output.*/
        int d0,d1,d2,d3;
        int sum;
        int output;
        
        //Checking if the inputed integer is within the range [1000,9999].
        if(n >= 1000 && n <= 9999)
        {
        	//Storing each digit (from right to left) of the inputed ISBN.
        	d0 = n%10;
        	n = n/10;
        	d1 = n%10;
        	n = n/10;
        	d2 = n%10;
        	n = n/10;
        	d3 = n%10;
            
            //Determining the sum of the ISBN multiplication process and the number that makes the ISBN a multiple of 11.
            sum = 5*d3 + 4*d2 + 3*d1 + 2*d0;
            output = 11-(sum % 11);
            
            //Handling the cases where the output is 10 and where the remainder is zero (causing the output to be 11).
            if(output == 10)
            {
            	System.out.println(D10_OUT);
            }
            else if(output == 11)
            {
            	System.out.println(0);
            }
            else
            {
                System.out.println(output);
            }
        }
        
        //Code carried out if the inputed integer is not within the given range.
        else
        {
        	System.out.println(INPUT_ERR);
        }
        
    	//Your code Ends here
	}

}
