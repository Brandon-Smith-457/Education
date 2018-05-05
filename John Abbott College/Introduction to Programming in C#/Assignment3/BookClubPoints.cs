using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Book_Club_Points
{
    public partial class bookClubForm : Form
    {
        //declaring the points and clear constants
        private const int ZERO = 0;
        private const int FIVE = 5;
        private const int FIFTEEN = 15;
        private const int THIRTY = 30;
        private const int SIXTY = 60;
        private const string CLEAR = "";

        //declaring int variables
        int inputN;
        int outputN;

        //declaring string variables
        string input;
        string output;

        public bookClubForm()
        {
            InitializeComponent();
        }

        private void calculateButton_Click(object sender, EventArgs e)
        {
            //Do this in the case of no error
            try
            {
                //Converting the string input to a int input
                input = inputTextBox.Text;
                inputN = int.Parse(input);

                //Check that the input is positive or zero
                if (inputN >= 0)
                {
                    //Check if the input is zero
                    if (inputN == 0)
                    {
                        //Display the correct number of points for zero books sold
                        outputN = ZERO;
                        output = Convert.ToString(outputN);
                        outputLabel.Text = output + " Points";
                    }
                    
                    //Check if the input is 1
                    else if (inputN == 1)
                    {
                        //Display the correct number of points for one books sold
                        outputN = FIVE;
                        output = Convert.ToString(outputN);
                        outputLabel.Text = output + " Points";
                    }

                    //Check if the input is 2
                    else if (inputN == 2)
                    {
                        //Display the correct number of points for two books sold
                        outputN = FIFTEEN;
                        output = Convert.ToString(outputN);
                        outputLabel.Text = output + " Points";
                    }

                    //Check if the input is 3
                    else if (inputN == 3)
                    {
                        //Display the correct number of points for three books sold
                        outputN = THIRTY;
                        output = Convert.ToString(outputN);
                        outputLabel.Text = output + " Points";
                    }

                    //Check if the input is greater than or equal to 4
                    else if (inputN >= 4)
                    {
                        //Display the correct number of points for four books sold
                        outputN = SIXTY;
                        output = Convert.ToString(outputN);
                        outputLabel.Text = output + " Points";
                    }
                }
                
                //This is in the case that the number inputed is not positive or zero
                else
                {
                    output = "You sold books?";
                    outputLabel.Text = output;
                }
            }
            //Do this in the case of errors or exceptions
            catch
            {
                output = "Be Realistic!!";
                outputLabel.Text = output;
                inputTextBox.Text = CLEAR;
            }
       }

        private void exitButton_Click(object sender, EventArgs e)
        {
            //close the application
            this.Close();
        }
    }
}
