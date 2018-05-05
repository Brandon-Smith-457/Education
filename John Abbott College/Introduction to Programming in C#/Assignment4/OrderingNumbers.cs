using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Ordering_Numbers
{
    public partial class orderingNumbersForm : Form
    {
        //Defining the empty string constant
        private const string CLEAR = "";

        //Declaring all necessary variables
        int smallest;
        int middle;
        int largest;
        int input1N;
        int input2N;
        int input3N;

        public orderingNumbersForm()
        {
            InitializeComponent();
        }

        private void descendingButton_Click(object sender, EventArgs e)
        {
            //Using try catch method as well as TryParse just in case any unforseen errors occur
            try
            {
                //Check if all three inputs are integer values
                if (int.TryParse(inputTextBox1.Text, out input1N)
                    && int.TryParse(inputTextBox2.Text, out input2N)
                    && int.TryParse(inputTextBox3.Text, out input3N))
                {
                    //The following if's and else statements determine the largest/middle/smallest
                    if (input1N > input2N && input2N > input3N)
                    {
                        smallest = input3N;
                        middle = input2N;
                        largest = input1N;
                    }
                    else if (input1N > input3N && input3N > input2N)
                    {
                        smallest = input2N;
                        middle = input3N;
                        largest = input1N;
                    }
                    else if (input2N > input1N && input1N > input3N)
                    {
                        smallest = input3N;
                        middle = input1N;
                        largest = input2N;
                    }
                    else if (input2N > input3N && input3N > input1N)
                    {
                        smallest = input1N;
                        middle = input3N;
                        largest = input2N;
                    }
                    else if (input3N > input2N && input2N > input1N)
                    {
                        smallest = input1N;
                        middle = input2N;
                        largest = input3N;
                    }
                    else if (input3N > input1N && input1N > input2N)
                    {
                        smallest = input2N;
                        middle = input1N;
                        largest = input3N;
                    }
                    //Do this if there are duplicate numbers
                    else if (input1N == input2N || input1N == input3N || input2N == input3N)
                    {
                        MessageBox.Show("Make them all unique");
                        smallest = 0;
                        middle = 0;
                        largest = 0;
                    }
                    //Display the result in the correct order
                    outputLabel.Text = Convert.ToString(largest) + ", " + Convert.ToString(middle) + ", " + Convert.ToString(smallest) + ".";
                }
                //Do this if the input is left blank
                else if (inputTextBox1.Text == "" || inputTextBox2.Text == "" || inputTextBox3.Text == "")
                {
                    outputLabel.Text = "Please input an integer in all";
                }
                //Do this if the input is invalid
                else
                {
                    outputLabel.Text = "Please use integer values";
                }
            }
            catch
            {
                MessageBox.Show("There has been an error, please ensure that the input is valid");
            }
        }

        private void ascendingButton_Click(object sender, EventArgs e)
        {
            //Using try catch method as well as try parse just in case any unforseen errors occur
            try
            {
                //Check if all three inputs are integer values
                if (int.TryParse(inputTextBox1.Text, out input1N)
                    && int.TryParse(inputTextBox2.Text, out input2N)
                    && int.TryParse(inputTextBox3.Text, out input3N))
                {
                    //The following if's and else statements determine the smallest/middle/largest
                    if (input1N < input2N && input2N < input3N)
                    {
                        smallest = input1N;
                        middle = input2N;
                        largest = input3N;
                    }
                    else if (input1N < input3N && input3N < input2N)
                    {
                        smallest = input1N;
                        middle = input3N;
                        largest = input2N;
                    }
                    else if (input2N < input1N && input1N < input3N)
                    {
                        smallest = input2N;
                        middle = input1N;
                        largest = input3N;
                    }
                    else if (input2N < input3N && input3N < input1N)
                    {
                        smallest = input2N;
                        middle = input3N;
                        largest = input1N;
                    }
                    else if (input3N < input2N && input2N < input1N)
                    {
                        smallest = input3N;
                        middle = input2N;
                        largest = input1N;
                    }
                    else if (input3N < input1N && input1N < input2N)
                    {
                        smallest = input3N;
                        middle = input1N;
                        largest = input2N;
                    }
                    //Do this if there are duplicate numbers
                    else if (input1N == input2N || input1N == input3N || input2N == input3N)
                    {
                        MessageBox.Show("Make them all unique");
                        smallest = 0;
                        middle = 0;
                        largest = 0;
                    }
                    //Display the result in the correct order
                    outputLabel.Text = outputLabel.Text = Convert.ToString(smallest) + ", " + Convert.ToString(middle) + ", " + Convert.ToString(largest) + ".";
                }
                //Do this if the input is left blank
                else if (inputTextBox1.Text == "" || inputTextBox2.Text == "" || inputTextBox3.Text == "")
                {
                    outputLabel.Text = "Please input an integer in all";
                }
                //Do this if the input is invalid
                else
                {
                    outputLabel.Text = "Please use integer values";
                }
            }
            catch
            {
                MessageBox.Show("There has been an error, please ensure that the input is valid");
            }
        }

        private void clearButton_Click(object sender, EventArgs e)
        {
            //Clear the inputs and output
            inputTextBox1.Text = CLEAR;
            inputTextBox2.Text = CLEAR;
            inputTextBox3.Text = CLEAR;
            outputLabel.Text = CLEAR;
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            //Close the application
            this.Close();
        }
    }
}
