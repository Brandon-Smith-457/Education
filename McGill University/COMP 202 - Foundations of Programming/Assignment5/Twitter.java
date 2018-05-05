//Brandon Smith

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class Twitter {
	private ArrayList<Tweet> tweets;
	
	//Constructor
	public Twitter()
	{
		this.tweets = new ArrayList<Tweet>();
	}
	
	//Reading, initializing, and sorting the ArrayList of tweets.  IOException propagated up to where the "input" is done
	public void loadDB(String fileName) throws IOException
	{
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		//Add words to the ArrayList one line at a time until the end wherein the reader returns null and breaks the loop
		while (true)
		{
			String tweetLine = br.readLine();
			if (tweetLine == null)
			{
				break;
			}
			String[] tweetInfo = tweetLine.split("\t");
			Tweet tempTweet = new Tweet(tweetInfo[0], tweetInfo[1], tweetInfo[2], tweetInfo[3]);
			//Making sure the tweets are valid
			if (tempTweet.checkMessage())
			{
				this.tweets.add(tempTweet);
			}
		}
		//Closing to avoid memory leaks
		br.close();
		fr.close();
		//Sort the ArrayList
		sortTwitter();
	}
	
	//Method to sort the ArrayList of this instance/Object
	private void sortTwitter()
	{
		//Index of where the unsorted section starts
		int unsortedIndex = 0;
		for (int i = 0; i < this.tweets.size(); i++)
		{
			//If the unsorted section is simply the last element we're done
			if (unsortedIndex == (this.tweets.size() - 1))
			{
				break;
			}
			//check each element from the unsortedIndex until the last element and keep track of which tweet and corresponding index is the smallest
			Tweet tempSmallest = this.tweets.get(unsortedIndex);
			int tempSmallestIndex = unsortedIndex;
			for (int j = unsortedIndex; j < this.tweets.size(); j++)
			{
				if (this.tweets.get(j).isBefore(tempSmallest))
				{
					tempSmallest = this.tweets.get(j);
					tempSmallestIndex = j;
				}
			}
			//Swap the smallest element with the element in the place of the unsortedIndex
			if (tempSmallestIndex != unsortedIndex)
			{
				this.tweets.remove(tempSmallestIndex);
				this.tweets.add(tempSmallestIndex, this.tweets.get(unsortedIndex));
				this.tweets.remove(unsortedIndex);
				this.tweets.add(unsortedIndex, tempSmallest);
			}
			unsortedIndex++;
		}
	}
	
	//Get methods
	public int getSizeTwitter()
	{
		return this.tweets.size();
	}
	
	
	public Tweet getTweet(int index)
	{
		return this.tweets.get(index);
	}
	
	//Method to print the valid tweets in the proper format
	public String printDB()
	{
		String s = "";
		for (int i = 0; i < this.tweets.size(); i++)
		{
			s += this.tweets.get(i).toString();
		}
		return s;
	}
	
	//Method to give a selection of tweets between the date of two tweets
	public ArrayList<Tweet> rangeTweets(Tweet tweet1, Tweet tweet2)
	{
		if (tweet1.isBefore(tweet2))
		{
			return rangeTweetsFunction(tweet1, tweet2);
		}
		else if (tweet2.isBefore(tweet1))
		{
			return rangeTweetsFunction(tweet2, tweet1);
		}
		else
		{
			return rangeTweetsFunction(tweet1, tweet2);
		}
	}
	
	//This method contains the behaviour of rangeTweets because rangeTweets was used to figure out which input was the earlier tweet
	private ArrayList<Tweet> rangeTweetsFunction(Tweet tweet1, Tweet tweet2)
	{
		ArrayList<Tweet> tempList = new ArrayList<Tweet>();
		for (int i = 0; i < this.tweets.size(); i++)
		{
			//Add the earliest tweet we inputed to the list we want to return
			if (this.tweets.get(i) == tweet1)
			{
				tempList.add(this.tweets.get(i));
			}
			//Add the latest tweet we inputed to the list we want to return
			else if (this.tweets.get(i) == tweet2)
			{
				tempList.add(this.tweets.get(i));
			}
			//Add all the tweets in between the two to the list we want to return
			else if (this.tweets.get(i).isBefore(tweet2) && !this.tweets.get(i).isBefore(tweet1))
			{
				tempList.add(this.tweets.get(i));
			}
		}
		return tempList;
	}
	
	//Save the valid tweets of this instance to a file.  IOException propagated up to where the "input" is done.
	public void saveDB(String fileName) throws IOException
	{
		FileWriter fw = new FileWriter(fileName);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(this.printDB());
		bw.close();
		fw.close();
	}
	
	//Determining the most frequent word in the list of tweets
	public String trendingTopic()
	{
		//A map to keep track of the frequency of each word
		HashMap<String,Integer> wordFrequency = new HashMap<String,Integer>();
		for (int i = 0; i < this.tweets.size(); i++)
		{
			//Add all the words from the message to a hashset (thus excluding duplicates in any one message)
			HashSet<String> words = new HashSet<String>();
			String[] tempMessage = this.tweets.get(i).getMessage().split(" ");
			for (int j = 0; j < tempMessage.length; j++)
			{
				tempMessage[j] = tempMessage[j].toLowerCase();
				words.add(tempMessage[j]);
			}
			//Adding the words to the hashmap
			for (String word : words)
			{
				//Removing any punctuation appended to the end of the words
				if (word.charAt(word.length() - 1) == ',' || word.charAt(word.length() - 1) == '.' || word.charAt(word.length() - 1) == ';' || word.charAt(word.length() - 1) == ':')
				{
					String temp = "";
					for (int j = 0; j < word.length() - 1; j++)
					{
						temp += word.charAt(j);
					}
					word = temp;
				}
				//Checking if the words are not one of the stopWords
				if (!Tweet.stopWords.contains(word))
				{
					//Adding the ones that are not to the hashmap and keeping track of the frequency
					if (wordFrequency.get(word) == null)
					{
						wordFrequency.put(word, 1);
					}
					else
					{
						wordFrequency.put(word, (wordFrequency.get(word) + 1));
					}
				}
			}
		}
		//Finding the key with max value in our hashmap
		String maxFrequencyWord = "";
		int maxFrequency = 0;
		for (String word : wordFrequency.keySet())
		{
			if (wordFrequency.get(word) > maxFrequency)
			{
				maxFrequency = wordFrequency.get(word);
				maxFrequencyWord = word;
			}
		}
		return "The word with the largest Frequency is: " + maxFrequencyWord + ", recurring " + maxFrequency + " times.";
	}
		
	public static void main(String[] args)
	{
		try
		{
			Twitter myTwitter = new Twitter();
			Tweet.loadStopWords("stopWords.txt");
			myTwitter.loadDB("tweets.txt");
			System.out.println("Example 1\nPlease comment out \"Tweet.loadStopWords(\"stopWords.txt\");\" to view the proper nullPointerException.");
			System.out.println("\nExample 2");
			System.out.println("The number of valid tweets is: " + myTwitter.getSizeTwitter());
			System.out.println("\nExample 3");
			System.out.println(myTwitter.printDB());
			System.out.println("Example 4");
			for (int i = 0; i < myTwitter.rangeTweets(myTwitter.getTweet(4), myTwitter.getTweet(2)).size(); i++)
			{
				System.out.print(myTwitter.rangeTweets(myTwitter.getTweet(4), myTwitter.getTweet(2)).get(i));
			}
			System.out.println("\nExample 5");
			System.out.println(myTwitter.trendingTopic());
			System.out.println("\nExample 6\nPlease see project folder for the output file \"validTweets.txt\"\nNote that if you open this file with notepad the proper file structure will not be maintained.");
			System.out.println("This is because I used the backslash n character, as opposed to backslash r backslash n, in keeping with the format of the supplied files.");
			myTwitter.saveDB("validTweets.txt");
		}
		catch (IOException e)
		{
			System.out.println("Please ensure the validity of the files and their paths.");
		}
	}
}
