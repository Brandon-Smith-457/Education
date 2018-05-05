using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Workshop
{
    public partial class workshopForm : Form
    {
        //Define the list box index constants
        private const int INDEX_ZERO = 0;
        private const int INDEX_ONE = 1;
        private const int INDEX_TWO = 2;
        private const int INDEX_THREE = 3;

        //Define the registration fees
        private const int OWD_FEE = 200;
        private const int EOFS_FEE = 100;
        private const int PPP_FEE = 100;
        private const int NPM_FEE = 150;

        //Define the location rates
        private const int MONT_T_RATE = 75;
        private const int QUE_RATE = 95;
        private const int MTL_RATE = 0;

        //Define the empty string
        private const string CLEAR = "";

        //Declare necessary variables
        int selectedWorkshop;
        int selectedLocation;
        int days;
        int registrationFee;
        int lodgingRate;
        int lodgingFee;
        int total;

        public workshopForm()
        {
            InitializeComponent();
        }

        private void calculateButton_Click(object sender, EventArgs e)
        {
            //Just in case
            try
            {
                //Define the test variables
                selectedWorkshop = workshopListBox.SelectedIndex;
                selectedLocation = locationListBox.SelectedIndex;

                //Will start testing the selectedWorkshop variable
                switch (selectedWorkshop)
                {
                    //Check if the first option is clicked, If true then define the first option's fee and duration then exit the test.
                    case INDEX_ZERO:
                        registrationFee = OWD_FEE;
                        days = 3;
                        break;

                    //Check if the second option is clicked, If true then define the second option's fee and duration then exit the test.
                    case INDEX_ONE:
                        registrationFee = EOFS_FEE;
                        days = 1;
                        break;

                    //Check if the third option is clicked, If true then define the third option's fee and duration then exit the test.
                    case INDEX_TWO:
                        registrationFee = PPP_FEE;
                        days = 1;
                        break;

                    //Check if the fourth option is clicked, If true then define the fourth option's fee and duration then exit the test.
                    case INDEX_THREE:
                        registrationFee = NPM_FEE;
                        days = 1;
                        break;

                    //If no selected workshop
                    default:
                        registrationFee = 0;
                        days = 0;
                        MessageBox.Show("Please Select a Workshop!!");
                        break;
                }

                //Will start testing the selectedLocation variable
                switch (selectedLocation)
                {
                    //Check if the first option is clicked, If true then define the first option's rate then exit the test.
                    case INDEX_ZERO:
                        lodgingRate = MONT_T_RATE;
                        break;

                    //Check if the second option is clicked, If true then define the second option's rate then exit the test.
                    case INDEX_ONE:
                        lodgingRate = QUE_RATE;
                        break;

                    //Check if the third option is clicked, If true then define the third option's rate then exit the test.
                    case INDEX_TWO:
                        lodgingRate = MTL_RATE;
                        break;

                    //If no location is selected
                    default:
                        lodgingRate = 0;
                        MessageBox.Show("Please Select a Location!!");
                        break;
                }
                //If both of the listboxes are selected
                if (!(selectedLocation == -1) && !(selectedWorkshop == -1))
                {
                    //Calculate and display all necessary values
                    lodgingFee = lodgingRate * days;
                    total = registrationFee + lodgingFee;

                    outputLodgingLabel.Text = lodgingFee.ToString("c");

                    outputRegistrationLabel.Text = registrationFee.ToString("c");

                    outputTotalLabel.Text = total.ToString("c");
                }
                //If there are unchecked listboxes
                else
                {
                    outputRegistrationLabel.Text = CLEAR;
                    outputLodgingLabel.Text = CLEAR;
                    outputTotalLabel.Text = CLEAR;
                }
            }
            //Just in case
            catch
            {
                MessageBox.Show("Unexpected Errors Have Occurred!");
                outputRegistrationLabel.Text = CLEAR;
                outputLodgingLabel.Text = CLEAR;
                outputTotalLabel.Text = CLEAR;
                workshopListBox.SelectedIndex = -1;
                locationListBox.SelectedIndex = -1;
            }

        }

        private void clearButton_Click(object sender, EventArgs e)
        {
            //Clear the inputs/outputs and unselect any options from both list boxes
            outputRegistrationLabel.Text = CLEAR;
            outputLodgingLabel.Text = CLEAR;
            outputTotalLabel.Text = CLEAR;
            workshopListBox.SelectedIndex = -1;
            locationListBox.SelectedIndex = -1;
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            //Close the application
            this.Close();
        }
    }
}
