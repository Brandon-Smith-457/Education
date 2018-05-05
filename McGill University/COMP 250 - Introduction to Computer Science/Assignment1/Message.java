package assignment1;

public class Message {
	
	public String message;
	public int lengthOfMessage;

	public Message (String m){
		message = m;
		lengthOfMessage = m.length();
		this.makeValid();
	}
	
	public Message (String m, boolean b){
		message = m;
		lengthOfMessage = m.length();
	}
	
	/**
	 * makeValid modifies message to remove any character that is not a letter and turn Upper Case into Lower Case
	 */
	public void makeValid(){
		this.message = this.message.toLowerCase();
		String tempMessage = "";
		for (int i = 0; i < this.lengthOfMessage; i++)
		{
			if (this.message.charAt(i) >= 97 && this.message.charAt(i) <= 122)
			{
				tempMessage = tempMessage.concat(String.valueOf(this.message.charAt(i)));
			}
		}
		this.message = tempMessage;
		this.lengthOfMessage = this.message.length();
	}
	
	/**
	 * prints the string message
	 */
	public void print(){
		System.out.println(message);
	}
	
	/**
	 * tests if two Messages are equal
	 */
	public boolean equals(Message m){
		if (message.equals(m.message) && lengthOfMessage == m.lengthOfMessage){
			return true;
		}
		return false;
	}
	
	/**
	 * caesarCipher implements the Caesar cipher : it shifts all letter by the number 'key' given as a parameter.
	 * @param key
	 */
	public void caesarCipher(int key){
		//Temporary variables to hold information that won't leave the scope
		int tempChar;
		String tempString = "";
		//The value with which the key will shift the characters within the range of the alphabet
		int shift = key%26;
		//Right shift making use of both temporary variables
		if (key > 0)
		{
			for (int i = 0; i < this.lengthOfMessage; i++)
			{
				tempChar = this.message.charAt(i);
				tempChar = tempChar + shift;
				//Dealing with "overflow"
				if (tempChar > 'z')
				{
					tempChar = tempChar - 26;
				}
				tempString = tempString + (char) tempChar;
			}
		}
		//Left shift making use of both temporary variables
		else if (key < 0)
		{
			for (int i = 0; i < this.lengthOfMessage; i++)
			{
				tempChar = this.message.charAt(i);
				tempChar = tempChar + shift;
				//Dealing with "overflow"
				if (tempChar < 'a')
				{
					tempChar = tempChar + 26;
				}
				tempString = tempString + (char) tempChar;
			}
		}
		this.message = tempString;
	}
	
	public void caesarDecipher(int key){
		this.caesarCipher(- key);
	}
	
	/**
	 * caesarAnalysis breaks the Caesar cipher
	 * you will implement the following algorithm :
	 * - compute how often each letter appear in the message
	 * - compute a shift (key) such that the letter that happens the most was originally an 'e'
	 * - decipher the message using the key you have just computed
	 */
	public void caesarAnalysis(){
		//Array to keep track of the recurrence of each letter of the alphabet, 'a' at index 0, 'z' at index 25
		int[] counts = new int[26];//Java initializes each element to 0
		int tempIndex = -1;
		int tempMaxRecurrence = -1;
		//Count every character and keep track
		for (int i = 0; i < this.lengthOfMessage; i++)
		{
			counts[this.message.charAt(i) - (int) 'a']++;
		}
		//Look through the resulting array and remember which one is the largest and at what index
		for (int i = 0; i < 26; i++)
		{
			if (counts[i] >= tempMaxRecurrence)
			{
				tempMaxRecurrence = counts[i];
				tempIndex = i;
			}
		}
		//Perform the shift (4 - tempIndex because tempIndex is the character position minus 1)
		this.caesarCipher(4 - tempIndex);
	}
	
	/**
	 * vigenereCipher implements the Vigenere Cipher : it shifts all letter from message by the corresponding shift in the 'key'
	 * @param key
	 */
	public void vigenereCipher (int[] key){
		String tempString = "";
		//Variable to keep track of where in the message we are
		int i = 0;
		while (i < this.lengthOfMessage)
		{
			//Looping through the key and one by one shifting the characters accordingly
			for (int j = 0; j < key.length; j++)
			{
				//Necessary so that we don't loop passed the end of the message
				if (i == this.lengthOfMessage)
				{
					break;
				}
				int tempChar;
				int shift = key[j]%26;
				//Right shift
				if (key[j] >= 0)
				{
					tempChar = this.message.charAt(i);
					tempChar = tempChar + shift;
					if (tempChar > 'z')
					{
						tempChar = tempChar - 26;
					}
					tempString = tempString + (char) tempChar;
				}
				//Left shift
				else if (key[j] < 0)
				{
					tempChar = this.message.charAt(i);
					tempChar = tempChar + shift;
					if (tempChar < 'a')
					{
						tempChar = tempChar + 26;
					}
					tempString = tempString + (char) tempChar;
				}
				i++;
			}
		}
		this.message = tempString;
	}


	/**
	 * vigenereDecipher deciphers the message given the 'key' according to the Vigenere Cipher
	 * @param key
	 */
	public void vigenereDecipher (int[] key){
		String tempString = "";
		int i;
		//Inverting the keys
		for (i = 0; i < key.length; i++)
		{
			key[i] = key[i]*-1;
		}
		i = 0;
		//Apply the exact same algorithm as the cipher with the inverted keys
		while (i < this.lengthOfMessage)
		{
			for (int j = 0; j < key.length; j++)
			{
				if (i == this.lengthOfMessage)
				{
					break;
				}
				int tempChar;
				int shift = key[j]%26;
				//Right shift
				if (key[j] >= 0)
				{
					tempChar = this.message.charAt(i);
					tempChar = tempChar + shift;
					if (tempChar > 'z')
					{
						tempChar = tempChar - 26;
					}
					tempString = tempString + (char) tempChar;
				}
				//Left shift
				else if (key[j] < 0)
				{
					tempChar = this.message.charAt(i);
					tempChar = tempChar + shift;
					if (tempChar < 'a')
					{
						tempChar = tempChar + 26;
					}
					tempString = tempString + (char) tempChar;
				}
				i++;
			}
		}
		this.message = tempString;
	}
	
	/**
	 * transpositionCipher performs the transition cipher on the message by reorganizing the letters and eventually adding characters
	 * @param key
	 */
	public void transpositionCipher (int key){
		//Determine the number of rows required for your characterMatrix
		int numberOfRows = -1;
		if (this.lengthOfMessage%key == 0)
		{
			numberOfRows = this.lengthOfMessage/key;
		}
		else
		{
			numberOfRows = this.lengthOfMessage/key + 1;
		}
		char[][] characterMatrix = new char[numberOfRows][key];
		//Variable to track the message index
		int k = 0;
		//Fill the matrix in the way specified by the instructions
		for (int i = 0; i < numberOfRows; i++)
		{
			for (int j = 0; j < key; j++)
			{
				//If we've looped passed the end of the message store the '*' character
				if (!(k < this.lengthOfMessage))
				{
					characterMatrix[i][j] = '*';
				}
				else
				{
					characterMatrix[i][j] = this.message.charAt(k);
				}
				k++;
			}
		}
		String tempString = "";
		//Build the string in the way specified by the instructions
		for (int j = 0; j < key; j++)
		{
			for (int i = 0; i < numberOfRows; i++)
			{
				tempString = tempString + characterMatrix[i][j];
			}
		}
		this.message = tempString;
		this.lengthOfMessage = this.message.length();
	}
	
	/**
	 * transpositionDecipher deciphers the message given the 'key'  according to the transition cipher.
	 * @param key
	 */
	public void transpositionDecipher (int key){
		//Checks made for rigor's sake (we technically already know the row size is lengthOfMessage/key for the correct key)
		int numberOfRows = -1;
		if (this.lengthOfMessage%key == 0)
		{
			numberOfRows = this.lengthOfMessage/key;
		}
		else
		{
			numberOfRows = this.lengthOfMessage/key + 1;
		}
		char[][] characterMatrix = new char[numberOfRows][key];
		//Variable to track the message index
		int k = 0;
		//Build the matrix in the same order with which we took the characters out from the cipher matrix
		for (int j = 0; j < key; j++)
		{
			for (int i = 0; i < numberOfRows; i++)
			{
				//The '*'s are already in the message so no accounting for them necessary
				characterMatrix[i][j] = this.message.charAt(k);
				k++;
			}
		}
		String tempString = "";
		//Take the characters in the same order as how the cipher builds the matrix.
		for (int i = 0; i < numberOfRows; i++)
		{
			for (int j = 0; j < key; j++)
			{
				//Only take the character if it is not a '*'
				if (characterMatrix[i][j] != '*')
				{
					tempString = tempString + characterMatrix[i][j];
				}
			}
		}
		this.message = tempString;
		this.lengthOfMessage = this.message.length();
	}
	
}
