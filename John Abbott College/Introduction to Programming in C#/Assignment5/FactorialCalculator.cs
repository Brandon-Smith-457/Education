using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Factorial_Calculator
{
    public partial class factorialForm : Form
    {
        //Define and declare necessary constants and variables
        private const string CLEAR = "";
        int inputN;
        //Double in order to allow for greater accuracy in the calculated result (int would lose accuracy at 13 factorial).
        double i;

        public factorialForm()
        {
            InitializeComponent();
        }

        private void calculateButton_Click(object sender, EventArgs e)
        {
            try
            {
                //Attempt to parse the user input (allocate to int value: inputN if possible)
                if (int.TryParse(inputTextBox.Text, out inputN))
                {
                    //Check greater than 0 and at most 170 (maximum that can be calculated)
                    if (inputN > 0 && inputN <= 170)
                    {
                        //Do a loop where i starts at one and the inputN decreases by one as long as it is greater than 1
                        for (i = 1; inputN >= 1; inputN--)
                        {
                            //Carry out the looped multiplication (constantly re-allocating i)
                            i = i * inputN;
                        }
                        //Display the value of i (the factorial calculated value)
                        outputLabel.Text = Convert.ToString(i);
                    }
                    //Zero factorial is 1 by convention
                    else if (inputN == 0)
                    {
                        outputLabel.Text = "1";
                    }
                    //If the input is too large
                    else if (inputN >= 171)
                    {
                        outputLabel.Text = "We apologize but you have exceeded the maximum capabilities of our program!";
                    }
                    //In case of negative numbers
                    else
                    {
                        outputLabel.Text = "Please input a positive integer!";
                    }
                }
                //In case of incorrect inputs (letters or decimals)
                else
                {
                    outputLabel.Text = "PLEASE INPUT A POSITIVE INTEGER!";
                }
            }
            //Just in case
            catch
            {
                outputLabel.Text = "Unexpected errors have occurred!";
            }
        }

        private void clearButton_Click(object sender, EventArgs e)
        {
            //Clear the inputs/outputs
            inputTextBox.Text = CLEAR;
            outputLabel.Text = CLEAR;
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            //Close the application
            this.Close();
        }
    }
}
