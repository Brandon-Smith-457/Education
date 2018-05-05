public class JudgeScore {
	public static void main(String[] args) {
	
		/*
		Brandon_Smith
		*/	
	
		//Declaring the variables for storing the judges scores. (I CHANGED IT FROM valueOf TO parseInt WITH GIULIA'S PERMISSION)
		int judge1, judge2, judge3, judge4;
		/*judge1 = Integer.valueOf(args[0]);
		judge2 = Integer.valueOf(args[1]);
		judge3 = Integer.valueOf(args[2]);
		judge4 = Integer.valueOf(args[3]);*/
		judge1 = Integer.parseInt(args[0]);
		judge2 = Integer.parseInt(args[1]);
		judge3 = Integer.parseInt(args[2]);
		judge4 = Integer.parseInt(args[3]);
		
		//Your code Starts here
		
		//Defining a general error message.
		final String ERR = "OOPS sorry, something went wrong!";
		
		//Declaring and initializing a variable to store the output of the algorithm (initialized so as to avoid compiling errors).
		double output = -2.0;
		
		//The following is a nest of if, else if, and else statements that determine the highest and lowest judge scores.
		if (judge1 >= judge2 && judge1 >= judge3 && judge1 >= judge4)
		{
			//The second layer of the nest determines the lowest judge score and then computes the average of the remaining two.
			if (judge2 <= judge3 && judge2 <= judge4)
			{
				output = (judge3 + judge4)/2.0;
			}
			else if (judge3 <= judge2 && judge3 <= judge4)
			{
				output = (judge2 + judge4)/2.0;
			}
			else if (judge4 <= judge2 && judge4 <= judge3)
			{
				output = (judge2 + judge3)/2.0;
			}
			//This else is placed here to handle any unexpected errors.  Ideally an error code system could be developed so not all the
			//errors output the same number.
			else
			{
				output = -1.0;
			}
		}
		else if (judge2 >= judge1 && judge2 >= judge3 && judge2 >= judge4)
		{
			if (judge1 <= judge3 && judge1 <= judge4)
			{
				output = (judge3 + judge4)/2.0;
			}
			else if (judge3 <= judge1 && judge3 <= judge4)
			{
				output = (judge1 + judge4)/2.0;
			}
			else if (judge4 <= judge1 && judge4 <= judge3)
			{
				output = (judge1 + judge3)/2.0;
			}
			else
			{
				output = -1.0;
			}
		}
		else if (judge3 >= judge1 && judge3 >= judge2 && judge3 >= judge4)
		{
			if (judge1 <= judge2 && judge1 <= judge4)
			{
				output = (judge2 + judge4)/2.0;
			}
			else if (judge2 <= judge1 && judge2 <= judge4)
			{
				output = (judge1 + judge4)/2.0;
			}
			else if (judge4 <= judge1 && judge4 <= judge2)
			{
				output = (judge1 + judge2)/2.0;
			}
			else
			{
				output = -1.0;
			}
		}
		else if (judge4 >= judge1 && judge4 >= judge2 && judge4 >= judge3)
		{
			if (judge1 <= judge2 && judge1 <= judge3)
			{
				output = (judge2 + judge3)/2.0;
			}
			else if (judge2 <= judge1 && judge2 <= judge3)
			{
				output = (judge1 + judge3)/2.0;
			}
			else if (judge3 <= judge1 && judge3 <= judge2)
			{
				output = (judge1 + judge2)/2.0;
			}
			else
			{
				output = -1.0;
			}
		}
		else
		{
			System.out.println(ERR);
		}
		
		//Display the result differing depending on the inputs based on the algorithm written above.
		System.out.println(output);
		
		//Your code Ends here
		
	}
}
