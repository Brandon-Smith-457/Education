/*
 * Assignment1.cpp
 *
 *  Created on: Feb 6, 2019
 *      Author: Brandon Smith
 */

#include <iostream>
#include <string>

using namespace std;

void countLetter();
void convertPhonetic();
void factorial();
void enhancedFactorial();

int main() {
	countLetter();
	convertPhonetic();
	factorial();
	enhancedFactorial();
	return 0;
}

void countLetter() {
	string sentence, letter;
	int number = 0;

	cout << "Question 1, countLetter():" << endl;
	cout << "Please enter a string : ";
	//getline better than just using cin << because we're entering from a command LINE.
	getline(cin, sentence);
	//do while to ensure that users can only proceed if they enter a single character
	do {
		cout << "Please enter a single character: ";
		getline(cin, letter);
	} while (letter.length() != 1);

	//Three different conditional blocks to deal with lower case letters, upper case letters, and any other characters. (not restricting the user
	//to any specific character set)
	char test = letter[0];
	if (test >= 'a' && test <= 'z') {
		for (char c : sentence) {
			if (c == test || c == (test - 32)) number++;
		}
	}
	else if (test >= 'A' && test <= 'Z') {
		for (char c : sentence) {
			if (c == test || c == (test + 32)) number++;
		}
	}
	else {
		for (char c : sentence) {
			if (c == test) number++;
		}
	}
	cout << "The character " << letter << " is repeated " << number << " times in your sentence." << endl;
}

void convertPhonetic() {
	string word, output;

	cout << "Question 2, convertPhonetic():" << endl;
	//do while loop to ensure the user inputs only a single word.
	do {
		cout << "Please enter a single word: ";
		getline(cin, word);
		//I know this is inefficient because it's possible you have a VERY long string with a space at the end and then suddenly you'll have compiled
		//a massive output string only to null it and break, HOWEVER, I figure on average if a user is going to put a space into the program it will
		//be after the first word which on average would be 7 or 8 characters.
		for (char c : word) {
			if (c == 'a' || c == 'A') output += "Alfa";
			else if (c == 'b' || c == 'B') output += "Bravo";
			else if (c == 'c' || c == 'C') output += "Charlie";
			else if (c == 'd' || c == 'D') output += "Delta";
			else if (c == 'e' || c == 'E') output += "Echo";
			else if (c == 'f' || c == 'F') output += "Foxtrot";
			else if (c == 'g' || c == 'G') output += "Golf";
			else if (c == 'h' || c == 'H') output += "Hotel";
			else if (c == 'i' || c == 'I') output += "India";
			else if (c == 'j' || c == 'J') output += "Juliett";
			else if (c == 'k' || c == 'K') output += "Kilo";
			else if (c == 'l' || c == 'L') output += "Lima";
			else if (c == 'm' || c == 'M') output += "Mike";
			else if (c == 'n' || c == 'N') output += "November";
			else if (c == 'o' || c == 'O') output += "Oscar";
			else if (c == 'p' || c == 'P') output += "Papa";
			else if (c == 'q' || c == 'Q') output += "Quebec";
			else if (c == 'r' || c == 'R') output += "Romeo";
			else if (c == 's' || c == 'S') output += "Sierra";
			else if (c == 't' || c == 'T') output += "Tango";
			else if (c == 'u' || c == 'U') output += "Uniform";
			else if (c == 'v' || c == 'V') output += "Victor";
			else if (c == 'w' || c == 'W') output += "Whiskey";
			else if (c == 'x' || c == 'X') output += "X-ray";
			else if (c == 'y' || c == 'Y') output += "Yankee";
			else if (c == 'z' || c == 'Z') output += "Zulu";
			else {
				output = "";
				break;
			}
			output += " ";
		}
	} while (output == "");
	cout << output << endl;
}

/*
 * Question 3:
 * a) A tail recursive function is extraordinarily useful as it allows us to save on stack space as well as results in more efficient recursive
 * algorithms.  It does this because instead of building up our result as we return from one function call to the previous, we build it up as we delve
 * deeper into the recursion (done usually through an accumulator).  Therefore, at the end of our recursive calls, instead of stepping back one call at a time, we already have the final
 * result and can jump (goto / jal) straight to the original function caller.
 *
 * b) A number of functional programming languages are purely based on recursive programming, and therefore the idea of tail-recursivity is EXTREMELY
 * important when practicing functional programming.  There is a fairly well defined understanding of tail-recursion and using a concept commonly
 * referred to as continuations, any recursive program can be made to be tail-recursive.  A continuation is the idea of building up the result of a
 * recursive call within a function, so that by the last recursive call, the function has already built up the final answer and so the recursion can
 * simply jump back to the original caller and give it the continuation function itself.
 */

//Actual tail recursive algorithm
long long int tailFactorial(int n, long long int acc) {
	if (n == 0 || n == 1) return acc;
	return tailFactorial(n-1, n*acc);
}

void factorial() {
	string userInput;
	int input, acc = 1;

	cout << "Question 4, factorial():" << endl;
	do {
		cout << "Please enter an integer: ";
		getline(cin, userInput);
		//Try catch block for integer parsing
		try {
			input = stoi(userInput);
			if (input >= 0)	break;
		} catch (const invalid_argument &ia) {
			cout << "Not a valid integer!" << endl;
		}
	} while (1);
	cout << "The factorial of " << userInput << " is " << tailFactorial(input, acc) << endl;
}

//Actual tail recursive algorithm
long long int tailEnhancedFactorial(int n, int *memo, long long int acc) {
	if (n == 0 || n == 1) return acc;
	if (n == 2) return memo[2]*acc;
	if (n == 3) return memo[3]*acc;
	if (n == 4) return memo[4]*acc;
	if (n == 5) return memo[5]*acc;
	if (n == 6) return memo[6]*acc;
	return tailFactorial(n-1, n*acc);
}

void enhancedFactorial() {
	string userInput;
	int input, acc = 1;
	//7 element array for memoization on only the first few factorials.
	int memo[] = {1, 1, 2, 6, 24, 120, 720};

	cout << "Question 5, enhancedFactorial():" << endl;
	do {
		cout << "Please enter a positive integer: ";
		getline(cin, userInput);
		//Try catch for integer parsing
		try {
			input = stoi(userInput);
			if (input >= 0) break;
		} catch (const invalid_argument &ia) {
			cout << "Not a valid integer!" << endl;
		}
	} while (1);
	cout << "The factorial of " << userInput << " is " << tailEnhancedFactorial(input, memo, acc) << endl;
}
