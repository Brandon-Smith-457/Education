using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Exam_Score
{
    public partial class examScoresForm : Form
    {
        //Define the constants for the acceptable inputs/Clear
        private const string CLEAR = "";
        private const string ANSWER_A = "A";
        private const string ANSWER_a = "a";
        private const string ANSWER_B = "B";
        private const string ANSWER_b = "b";
        private const string ANSWER_C = "C";
        private const string ANSWER_c = "c";
        private const string ANSWER_D = "D";
        private const string ANSWER_d = "d";

        //Define the constants for the size, the passing requirements, and the perfect score
        private const int SIZE = 10;
        private const int PASSING_GRADE = 7;
        private const int PERFECT = 10;

        //Declare a bool variable to help with exceptions (see further down in code)
        bool isValid;

        //Declare string variable for the display of incorrect answers
        string wrong;

        //Declare int variables for the rewarded points and the index of the array
        int points;
        int index;
        public examScoresForm()
        {
            InitializeComponent();
        }

        private void calculateButton_Click(object sender, EventArgs e)
        {
            //Try in case of error
            try
            {
                //Define the array of user inputs
                string[] answersArray = new string[SIZE];
                answersArray[0] = inputTextBox1.Text;
                answersArray[1] = inputTextBox2.Text;
                answersArray[2] = inputTextBox3.Text;
                answersArray[3] = inputTextBox4.Text;
                answersArray[4] = inputTextBox5.Text;
                answersArray[5] = inputTextBox6.Text;
                answersArray[6] = inputTextBox7.Text;
                answersArray[7] = inputTextBox8.Text;
                answersArray[8] = inputTextBox9.Text;
                answersArray[9] = inputTextBox10.Text;

                //Initialize the points to be zero and the wrong questions to be none and the exception bool to be false
                points = 0;
                wrong = "";
                isValid = false;

                //Start a loop from array index 0 and continue until index = the array size
                for (index = 0; index < SIZE; index++)
                {
                    //Test multiple different cases for the inputs
                    switch (answersArray[index])
                    {
                        //input of A is correct
                        case ANSWER_A:
                            points = points + 1;
                            break;

                        //input of a is correct
                        case ANSWER_a:
                            points = points + 1;
                            break;

                        //input of B is incorrect
                        case ANSWER_B:
                            //Add the question number that was incorrect to the list of wrong answered questions
                            wrong = wrong + Convert.ToString(index + 1) + ", ";
                            break;

                        //input of b is incorrect
                        case ANSWER_b:
                            //Add the question number that was incorrect to the list of wrong answered questions
                            wrong = wrong + Convert.ToString(index + 1) + ", ";
                            break;

                        //input of C is incorrect
                        case ANSWER_C:
                            //Add the question number that was incorrect to the list of wrong answered questions
                            wrong = wrong + Convert.ToString(index + 1) + ", ";
                            break;

                        //input of c is incorrect
                        case ANSWER_c:
                            //Add the question number that was incorrect to the list of wrong answered questions
                            wrong = wrong + Convert.ToString(index + 1) + ", ";
                            break;

                        //input of D is incorrect
                        case ANSWER_D:
                            //Add the question number that was incorrect to the list of wrong answered questions
                            wrong = wrong + Convert.ToString(index + 1) + ", ";
                            break;

                        //input of d is incorrect
                        case ANSWER_d:
                            //Add the question number that was incorrect to the list of wrong answered questions
                            wrong = wrong + Convert.ToString(index + 1) + ", ";
                            break;

                        //Do this if the input is incorrect
                        default:
                            MessageBox.Show("Please use only the characters 'A,a,B,b,C,c,D,d'");
                            points = 0;
                            //index = SIZE will stop the loop
                            index = SIZE;
                            //Change the boolean variable to true
                            isValid = true;
                            break;
                    }
                }
                //If you pass but aren't perfect
                if (points >= PASSING_GRADE && points < PERFECT)
                {
                    outputScoreLabel.Text = "You scored " + Convert.ToString(points) + " out of 10!  You Passed!!!";
                    outputWrongAnswersLabel.Text = "Questions: " + wrong + "were wrong.";
                }
                //If you are perfect
                else if (points == PERFECT)
                {
                    outputScoreLabel.Text = "You scored " + Convert.ToString(points) + " out of 10!  Perfect!!!";
                    outputWrongAnswersLabel.Text = "You did no wrong!";
                }
                //If you have inputted an incorrect input (isValid is true so this will run)
                else if (isValid)
                {
                    outputScoreLabel.Text = CLEAR;
                    outputWrongAnswersLabel.Text = CLEAR;
                }
                //If you fail
                else
                {
                    outputScoreLabel.Text = "You scored " + Convert.ToString(points) + " out of 10!  You failed!!!";
                    outputWrongAnswersLabel.Text =  "Questions: " + wrong + "were wrong.";
                }
            }
            //In case errors occurred
            catch
            {
                outputScoreLabel.Text = "Please use only the characters 'A,a,B,b,C,c,D,d'!!";
            }
        }

        private void clearButton_Click(object sender, EventArgs e)
        {
            //Clear everything
            inputTextBox1.Text = CLEAR;
            inputTextBox2.Text = CLEAR;
            inputTextBox3.Text = CLEAR;
            inputTextBox4.Text = CLEAR;
            inputTextBox5.Text = CLEAR;
            inputTextBox6.Text = CLEAR;
            inputTextBox7.Text = CLEAR;
            inputTextBox8.Text = CLEAR;
            inputTextBox9.Text = CLEAR;
            inputTextBox10.Text = CLEAR;
            outputScoreLabel.Text = CLEAR;
            outputWrongAnswersLabel.Text = CLEAR;
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            //Close the application
            this.Close();
        }
    }
}
