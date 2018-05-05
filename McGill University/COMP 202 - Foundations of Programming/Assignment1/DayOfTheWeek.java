public class DayOfTheWeek {

	public static void main(String[] args) {
	
		/*
		Brandon_Smith
		*/
	
		//Declaring the variables for year(y), month(m), day(d)
		int y,m,d;
		//Initialisation of the variables y,m and d with the input arguments (I CHANGED IT FROM valueOf TO parseInt WITH GIULIA'S PERMISSION)
		/*y = Integer.valueOf(args[0]);
		m = Integer.valueOf(args[1]);
		d = Integer.valueOf(args[2]);*/
		y = Integer.parseInt(args[0]);
		m = Integer.parseInt(args[1]);
		d = Integer.parseInt(args[2]);
		//Declaring the auxiliary variables used in the formula.
		int y0, m0, d0, x;
		
		
		//Your code Starts here
		
		//Initializing all output messages.
		final String SUN = "SUNDAY";
		final String MON = "MONDAY";
		final String TUE = "TUESDAY";
		final String WED = "WEDNESDAY";
		final String THU = "THURSDAY";
		final String FRI = "FRIDAY";
		final String SAT = "SATURDAY";
		final String ERR1 = "Please input a valid date";
		final String ERR2 = "Something went wrong";
		
		//Making sure the input for month and day are both valid.
		if(m > 0 && m < 13 && d > 0 && d < 32)
		{
			//Calculating the values of y0, m0, d0, and x.
			y0 = y - (14-m)/12;
			x = y0 + y0/4 - y0/100 + y0/400;
			m0 = m + (12*((14-m)/12))-2;
			d0 = (d + x + 31*m0/12)%7;
				
			//Outputting the correct message for the 7 different possible values of d0 and the exceptions (ERRORS).
			if(d0 == 0)
			{
				System.out.println(SUN);
			}
			else if(d0 == 1)
			{
				System.out.println(MON);
			}
			else if(d0 == 2)
			{
				System.out.println(TUE);
			}
			else if(d0 == 3)
			{
				System.out.println(WED);
			}
			else if(d0 == 4)
			{
				System.out.println(THU);
			}
			else if(d0 == 5)
			{
				System.out.println(FRI);
			}
			else if(d0 == 6)
			{
				System.out.println(SAT);
			}
			else
			{
				System.out.println(ERR2);
			}
		}
		//Code if m or d are false.
		else
		{
			System.out.println(ERR1);
		}
		//Your code Ends here
	}

}
