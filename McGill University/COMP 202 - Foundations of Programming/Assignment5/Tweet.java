//Brandon Smith

import java.util.HashSet;
import java.io.*;

public class Tweet {
	private String userAccount;
	private String date;
	private String time;
	private String message;
	public static HashSet<String> stopWords;
	
	//Constructor
	public Tweet(String userAccount, String date, String time, String message)
	{
		this.userAccount = userAccount;
		this.date = date;
		this.time = time;
		this.message = message;
	}
	
	//Method to check if a tweet's method is valid
	public boolean checkMessage()
	{
		//Handling the case where stopWords has yet to be initialized
		if (stopWords == null)
		{
			throw new NullPointerException("Error checking the stopWords database: The file of stopWords has not been loaded yet!");
		}
		int stopWordCount = 0;
		//Separating the message into an array of words by the space character
		String[] words = this.message.split(" ");
		for (int i = 0; i < words.length; i++)
		{
			//Counting all stopWords in the message
			if (checkForStopWord(words[i]))
			{
				stopWordCount++;
			}
		}
		if ((words.length - stopWordCount) >= 1 && (words.length - stopWordCount) <= 15)
		{
			return true;
		}
		return false;
	}
	
	//A method that returns true if the given word is one of the stopWords (also takes into account the limited punctuation we're considering)
	private static boolean checkForStopWord(String word)
	{
		if (word.charAt(word.length() - 1) == ',' || word.charAt(word.length() - 1) == '.' || word.charAt(word.length() - 1) == ';' || word.charAt(word.length() - 1) == ':')
		{
			String temp = "";
			for (int i = 0; i < word.length() - 1; i++)
			{
				temp += word.charAt(i);
			}
			//Ignoring upper/lower cases
			temp = temp.toLowerCase();
			return stopWords.contains(temp);
		}
		else
		{
			return stopWords.contains(word);
		}
	}
	
	//Get Methods and the toString() method
	public String getDate()
	{
		return this.date;
	}
	
	
	public String getTime()
	{
		return this.time;
	}
	
	
	public String getMessage()
	{
		return this.message;
	}
	
	
	public String getUserAccount()
	{
		return this.userAccount;
	}
	
	
	public String toString()
	{
		return this.userAccount + "\t" + this.date + "\t" + this.time + "\t" + this.message + "\n";
	}
	
	//Method to check if this tweet was posted before the comparitiveTweet
	public boolean isBefore(Tweet comparitiveTweet)
	{
		//Splitting up the date of both tweets into 3 separate integers and storing them in two int arrays
		String[] dateString = this.date.split("-");
		String[] comparitiveDateString = comparitiveTweet.date.split("-");
		int[] dateInt = new int[dateString.length];
		int[] comparitiveDateInt = new int[comparitiveDateString.length];
		for (int i = 0; i < dateInt.length; i++)
		{
			dateInt[i] = Integer.parseInt(dateString[i]);
			comparitiveDateInt[i] = Integer.parseInt(comparitiveDateString[i]);
		}
		//The following conditional ladder checks, starting from the largest time quantity, whether or not this tweet is smaller than the comparitiveTweet
		//Check if the Year is equal in both tweets
		if (dateInt[0] == comparitiveDateInt[0])
		{
			//Check if the Month is equal in both tweets
			if (dateInt[1] == comparitiveDateInt[1])
			{
				//Check if the Day is equal in both tweets
				if (dateInt[2] == comparitiveDateInt[2])
				{
					//Splitting up the time of both tweets into 3 separate integers and storing them in two int arrays
					String[] timeString = this.time.split(":");
					String[] comparitiveTimeString = comparitiveTweet.time.split(":");
					int[] timeInt = new int[timeString.length];
					int[] comparitiveTimeInt = new int[comparitiveTimeString.length];
					for (int i = 0; i < timeInt.length; i++)
					{
						timeInt[i] = Integer.parseInt(timeString[i]);
						comparitiveTimeInt[i] = Integer.parseInt(comparitiveTimeString[i]);
					}
					//Check if the hour is equal in both tweets
					if (timeInt[0] == comparitiveTimeInt[0])
					{
						//Check if the minutes are equal in both tweets
						if (timeInt[1] == comparitiveTimeInt[1])
						{
							//Check if the seconds are equal in both tweets
							if (timeInt[2] == comparitiveTimeInt[2])
							{
								return false;
							}
							//Check if the seconds are smaller in this tweet
							else if (timeInt[2] < comparitiveTimeInt[2])
							{
								return true;
							}
							//Do this if the seconds are larger in this tweet
							else
							{
								return false;
							}
						}
						//Check if the minutes are smaller in this tweet
						else if (timeInt[1] < comparitiveTimeInt[1])
						{
							return true;
						}
						//Do this if the minutes are larger in this tweet
						else
						{
							return false;
						}
					}
					//Check if the hour is smaller in this tweet
					else if (timeInt[0] < comparitiveTimeInt[0])
					{
						return true;
					}
					//Do this if the hour is larger in this tweet
					else
					{
						return false;
					}
				}
				//Check if the day is smaller in this tweet
				else if (dateInt[2] < comparitiveDateInt[2])
				{
					return true;
				}
				//Do this if the day is larger in this tweet
				else
				{
					return false;
				}
			}
			//Check if the month is smaller in this tweet
			else if (dateInt[1] < comparitiveDateInt[1])
			{
				return true;
			}
			//Do this if the month is larger in this tweet
			else
			{
				return false;
			}
		}
		//Check if the year is smaller in this tweet
		else if (dateInt[0] < comparitiveDateInt[0])
		{
			return true;
		}
		//Do this if the year is larger in this tweet
		else
		{
			return false;
		}
	}
	
	//Method to read the stopWords from a file.  IOException propagated up to where the "input" is done
	public static void loadStopWords(String fileName) throws IOException
	{
		//HashSet cause you you don't want duplicates and don't care about order
		stopWords = new HashSet<String>();
		//Declaring the Objects required to read from a file
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		//Add words to the HashSet one line at a time until the end wherein the reader returns null and breaks the loop
		while (true)
		{
			String fileInput = br.readLine();
			if (fileInput == null)
			{
				break;
			}
			//Ignoring Upper/Lower cases
			fileInput = fileInput.toLowerCase();
			stopWords.add(fileInput);
		}
		//Close the readers so as to avoid memory leak problems
		br.close();
		fr.close();
	}
}
