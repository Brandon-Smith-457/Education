using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace TempConversion
{
    public partial class tempConversionForm : Form
    {
        //Stating all constants and strings in class for code cleanliness.
        private const double cToFahrenheitConstant = 1.8;
        //Using Decimal for consistency with the rest of the program (I understand that this value could be considered as a Double).
        private const double fToCelsiusConstant = 0.5555555555555555555555555556;
        //Celsius constant rounded to the 28'th decimal place beause the decimal variable is accurate the 28 decimal places.
        private const int tempConversionConstant32 = 32;
        string input;
        string answer;
        const string clear = "";
        const string direct = "Please enter a number";

        public tempConversionForm()
        {
            InitializeComponent();
        }

        private void celFahrConversionButton_Click(object sender, EventArgs e)
        {
            if (inputTextBox.Text == clear)
            //Condition where the input is left empty.
            {
                outputLabel.Text = direct;
                //Give proper directions in the output Label.
            }

            else
            {
                try
                //Used to exclude these functionalities in the presence of an error/exception.
                {
                    double inputNumber = double.Parse(inputTextBox.Text);
                    //Converting string to a stored decimal information.

                    double fAnswer = cToFahrenheitConstant * inputNumber + tempConversionConstant32;
                    //Using the constants for celsius to fahrenheit declared in the Class, conversion calculations are done.

                    answer = Convert.ToString(Math.Round(fAnswer, 1));
                    //Defining String variable answer as the string representation of the answer: fAnswer, rounded to one decimal place.

                    input = Convert.ToString(Math.Round(inputNumber, 1));
                    //Converting the input back to a string variable rounded to one decimal place.

                    outputLabel.Text = input + " celsius is " + answer + " fahrenheit.";
                    //Changing the text of the output label to the answer.
                    //I KNOW THE FOLLOWING WOULD WORK IN PLACE OF Convert.ToString/Math.Round: outputLabel.Text = inputNumber.ToString("n1") + " celsius is " + fAnswer.ToString("n1") + " fahrenheit.";
                }
                catch
                //Used to store and execute code in the case of an error/exception.
                {
                    MessageBox.Show("Please use numbers and decimal places only (or use smaller magnitudes)");
                    //Displays the message as a pop-up.

                    inputTextBox.Text = clear;
                    //Changing the input text to the empty value(ie. erasing the incorrect input text).

                    outputLabel.Text = clear;
                    //Changing the output text to the empty value(ie. erasing the no longer valid conversion).
                }
            }
        }

        private void FahrCelConversionButton_Click(object sender, EventArgs e)
        {
            if (inputTextBox.Text == clear)
                //Condition where the input is left empty.
            {
                outputLabel.Text = direct;
                //Give proper directions in the output Label.
            }
            else
            {
                try
                //Used to exclude these functionalities in the presence of an error/exception.
                {
                    double inputNumber = double.Parse(inputTextBox.Text);
                    //Converting string to a stored decimal information.

                    double cAnswer = (inputNumber - tempConversionConstant32) * fToCelsiusConstant;
                    //Using the constants for fahrenheit to celsius declared in the Class, conversion calculations are done.

                    answer = Convert.ToString(Math.Round(cAnswer, 1));
                    //Defining String variable answer as the string representation of the answer: cAnswer, rounded to one decimal place.

                    input = Convert.ToString(Math.Round(inputNumber, 1));
                    //Converting the input back to a string variable rounded to one decimal place.

                    outputLabel.Text = input + " fahrenheit is " + answer + " celsius.";
                    //Changing the text of the output label to the answer.
                    //I KNOW THE FOLLOWING WOULD WORK IN PLACE OF Convert.ToString/Math.Round: outputLabel.Text = inputNumber.ToString("n1") +" fahrenheit is "+ cAnswer.ToString("n1") +" celsius.";
                }
                catch
                //Used to store and execute code in the case of an error/exception.
                {
                    MessageBox.Show("Please use numbers and decimal places only (or use smaller magnitudes)");
                    //Displays the message as a pop-up.

                    inputTextBox.Text = clear;
                    //Changing the input text to the empty value(ie. erasing the incorrect input text).

                    outputLabel.Text = clear;
                    //Changing the output text to the empty value(ie. erasing the no longer valid conversion).
                }
            }
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            this.Close();
            //Closes the form.
        }


    }
}
