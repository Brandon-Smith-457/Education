using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Company__Discount
{
    public partial class companyDiscountForm : Form
    {
        //Declare all constants for percentages, cost, and the CLEAR
        private const double ZERO_PERCENT = 0;
        private const double TWENTY_PERCENT = 0.20;
        private const double THIRTY_PERCENT = 0.30;
        private const double FOURTY_PERCENT = 0.40;
        private const double FIFTY_PERCENT = 0.50;
        private const double COST = 100;
        private const string CLEAR = "";

        //Declare all variables
        string input;
        string outputSavings;
        string outputCost;
        //int used to catch any decimal inputs (unrealistic)
        int inputN;
        double outputSavingsN;
        double outputCostN;

        public companyDiscountForm()
        {
            InitializeComponent();
        }

        private void calculateButton_Click(object sender, EventArgs e)
        {
            //Try this first
            try
            {
                //Converting the user input
                input = inputTextBox.Text;
                inputN = int.Parse(input);

                //Do this if the input is between or equal to 0 and 9
                if (inputN >= 0 && inputN <= 9)
                {
                    //Calculate the amount of money saved and the amount of money spent with no discount
                    //(double) <----- Used to serve as a reminder that the calculation is not all doubles
                    // without the (double) it would still work
                    outputSavingsN = (double)inputN * COST * ZERO_PERCENT;
                    outputCostN = (double)inputN * COST - outputSavingsN;
                    outputSavings = Convert.ToString(Math.Round(outputSavingsN, 2));
                    outputCost = Convert.ToString(Math.Round(outputCostN, 2));
                    outputSavingsLabel.Text = "$ " + outputSavings;
                    outputTotalLabel.Text = "$ " + outputCost;
                }
                //Do this if the input is between or equal to 10 and 19
                else if (inputN >= 10 && inputN <= 19)
                {
                    //Calculate the amount of money saved and the amount of money spent with 20% discount
                    //(double) <----- Used to serve as a reminder that the calculation is not all doubles
                    // without the (double) it would still work
                    outputSavingsN = (double)inputN * COST * TWENTY_PERCENT;
                    outputCostN = (double)inputN * COST - outputSavingsN;
                    outputSavings = Convert.ToString(Math.Round(outputSavingsN, 2));
                    outputCost = Convert.ToString(Math.Round(outputCostN, 2));
                    outputSavingsLabel.Text = "$ " + outputSavings;
                    outputTotalLabel.Text = "$ " + outputCost;
                }
                //Do thhis if the input is between or equal to 20 and 49
                else if (inputN >= 20 && inputN <= 49)
                {
                    //Calculate the amount of money saved and the amount of money spent with 30% discount
                    //(double) <----- Used to serve as a reminder that the calculation is not all doubles
                    // without the (double) it would still work
                    outputSavingsN = (double)inputN * COST * THIRTY_PERCENT;
                    outputCostN = (double)inputN * COST - outputSavingsN;
                    outputSavings = Convert.ToString(Math.Round(outputSavingsN, 2));
                    outputCost = Convert.ToString(Math.Round(outputCostN, 2));
                    outputSavingsLabel.Text = "$ " + outputSavings;
                    outputTotalLabel.Text = "$ " + outputCost;
                }
                //Do this if the input is between or equal to 50 and 99
                else if (inputN >= 50 && inputN <= 99)
                {
                    //Calculate the amount of money saved and the amount of money spent with 40% discount
                    //(double) <----- Used to serve as a reminder that the calculation is not all doubles
                    // without the (double) it would still work
                    outputSavingsN = (double)inputN * COST * FOURTY_PERCENT;
                    outputCostN = (double)inputN * COST - outputSavingsN;
                    outputSavings = Convert.ToString(Math.Round(outputSavingsN, 2));
                    outputCost = Convert.ToString(Math.Round(outputCostN, 2));
                    outputSavingsLabel.Text = "$ " + outputSavings;
                    outputTotalLabel.Text = "$ " + outputCost;
                }
                //Do this if the input is greater than or equal to 100
                else if (inputN >= 100)
                {
                    //Calculate the amount of money saved and the amount of money spent with 50% discount
                    //(double) <----- Used to serve as a reminder that the calculation is not all doubles
                    // without the (double) it would still work
                    outputSavingsN = (double)inputN * COST * FIFTY_PERCENT;
                    outputCostN = (double)inputN * COST - outputSavingsN;
                    outputSavings = Convert.ToString(Math.Round(outputSavingsN, 2));
                    outputCost = Convert.ToString(Math.Round(outputCostN, 2));
                    outputSavingsLabel.Text = "$ " + outputSavings;
                    outputTotalLabel.Text = "$ " + outputCost;
                }
                //Do this in the case of any other viable input (negative numbers)
                else
                {
                    //Indicate to the user that he/she can't get a discount while selling to the company
                    inputTextBox.Text = CLEAR;
                    outputSavingsLabel.Text = "You Sold to us?";
                    outputTotalLabel.Text = "Re-think your input";
                }
            }
            //Do this in the case of errors or exceptions (non-numerical inputs)
            catch
            {
                //Indicate to the user that he/she needs to input numbers
                inputTextBox.Text = CLEAR;
                outputSavingsLabel.Text = "Be Realistic";
                outputTotalLabel.Text = "Re-think your input";
            }
        }
        private void exitButton_Click(object sender, EventArgs e)
        {
            //Close the application
            this.Close();
        }

    }
}
