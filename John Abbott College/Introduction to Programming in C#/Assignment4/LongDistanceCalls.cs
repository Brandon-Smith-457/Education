using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Long_Distance_Calls
{
    public partial class longDistanceCallsForm : Form
    {
        //Declare all constants and variables
        private const string CLEAR = "";
        private const double DAYTIME_RATE = 0.07;
        private const double EVENING_RATE = 0.12;
        private const double OFF_PEAK_RATE = 0.05;
        string rateIndicator;
        double inputN;
        double outputN;

        public longDistanceCallsForm()
        {
            InitializeComponent();
        }

        private void longDistanceCallsForm_Load(object sender, EventArgs e)
        {
            //Initialize the form with all radio buttons unchecked and removes them from tab list at start
            this.daytimeRadioButton.TabStop = false;
            this.eveningRadioButton.TabStop = false;
            this.offPeakRadioButton.TabStop = false;
        }

        private void calculateButton_Click(object sender, EventArgs e)
        {
            //Try just in case of unexpected errors
            try
            {
                //Make sure the input is positive
                if (double.TryParse(inputTextBox.Text, out inputN))
                {
                    if (inputN < 0)
                    {
                        outputLabel.Text = "Negative time does not exist";
                    }
                    else
                    {
                        //Calculate the charges and define the rateIndicator for the selected radio button
                        if (daytimeRadioButton.Checked)
                        {
                            outputN = inputN * DAYTIME_RATE;
                            rateIndicator = " at a rate of $0.07/min.";
                        }
                        else if (eveningRadioButton.Checked)
                        {
                            outputN = inputN * EVENING_RATE;
                            rateIndicator = " at a rate of $0.12/min.";
                        }
                        else if (offPeakRadioButton.Checked)
                        {
                            outputN = inputN * OFF_PEAK_RATE;
                            rateIndicator = " at a rate of $0.05/min.";
                        }
                        //Do this if all radiobuttons are unchecked
                        else if (!daytimeRadioButton.Checked
                              || !eveningRadioButton.Checked
                              || !offPeakRadioButton.Checked)
                        {
                            outputN = 0;
                            rateIndicator = " Please select a rate!";
                        }
                        //Display the charges and the rateIndicator
                        outputLabel.Text = outputN.ToString("C") + rateIndicator;
                    }
                }
                //Do this in the case of invalid inputs
                else
                {
                    outputLabel.Text = "Please use Real numbers!";
                }
            }
            //Do this in the case of unexcpected errors
            catch
            {
                MessageBox.Show("An unexpected error has occurred, please revise your inputs");
            }
        }

        private void clearButton_Click(object sender, EventArgs e)
        {
            //Uncheck all the radiobuttons and clear the input and outputs
            daytimeRadioButton.Checked = false;
            eveningRadioButton.Checked = false;
            offPeakRadioButton.Checked = false;
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
