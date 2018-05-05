using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Colour_Mixer
{
    public partial class colourMixerForm : Form
    {
        //Declare the colour variables
        Color colour;
        Color originalColour;

        public colourMixerForm()
        {
            InitializeComponent();
            //Define the original background
            originalColour = this.BackColor;
        }

        private void colourMixerForm_Load(object sender, EventArgs e)
        {
            //Start the form with all radio buttons unchecked and remove them from the tab index initially
            this.redRadioButton1.TabStop = false;
            this.redRadioButton2.TabStop = false;
            this.blueRadioButton1.TabStop = false;
            this.blueRadioButton2.TabStop = false;
            this.yellowRadioButton1.TabStop = false;
            this.yellowRadioButton2.TabStop = false;
        }

        private void mixButton_Click(object sender, EventArgs e)
        {
            //Try just in case of unexpected errors
            try
            {
                //For the following if/else if statements define colour to be the proper mix the two checked radiobuttons.
                if (redRadioButton1.Checked && redRadioButton2.Checked)
                {
                    colour = Color.Red;
                }
                else if (blueRadioButton1.Checked && blueRadioButton2.Checked)
                {
                    colour = Color.Blue;
                }
                else if (yellowRadioButton1.Checked && yellowRadioButton2.Checked)
                {
                    colour = Color.Yellow;
                }
                else if (redRadioButton1.Checked && blueRadioButton2.Checked || redRadioButton2.Checked && blueRadioButton1.Checked)
                {
                    colour = Color.Purple;
                }
                else if (redRadioButton1.Checked && yellowRadioButton2.Checked || redRadioButton2.Checked && yellowRadioButton1.Checked)
                {
                    colour = Color.Orange;
                }
                else if (blueRadioButton1.Checked && yellowRadioButton2.Checked || blueRadioButton2.Checked && yellowRadioButton1.Checked)
                {
                    colour = Color.Green;
                }
                //Check to make sure that two buttons are checked (one from either side)
                else if (!blueRadioButton1.Checked && !blueRadioButton2.Checked && !redRadioButton1.Checked && !redRadioButton2.Checked && !yellowRadioButton1.Checked && !yellowRadioButton2.Checked
                      || blueRadioButton1.Checked && !blueRadioButton2.Checked && !redRadioButton1.Checked && !redRadioButton2.Checked && !yellowRadioButton1.Checked && !yellowRadioButton2.Checked
                      || !blueRadioButton1.Checked && blueRadioButton2.Checked && !redRadioButton1.Checked && !redRadioButton2.Checked && !yellowRadioButton1.Checked && !yellowRadioButton2.Checked
                      || !blueRadioButton1.Checked && !blueRadioButton2.Checked && redRadioButton1.Checked && !redRadioButton2.Checked && !yellowRadioButton1.Checked && !yellowRadioButton2.Checked
                      || !blueRadioButton1.Checked && !blueRadioButton2.Checked && !redRadioButton1.Checked && redRadioButton2.Checked && !yellowRadioButton1.Checked && !yellowRadioButton2.Checked
                      || !blueRadioButton1.Checked && !blueRadioButton2.Checked && !redRadioButton1.Checked && !redRadioButton2.Checked && yellowRadioButton1.Checked && !yellowRadioButton2.Checked
                      || !blueRadioButton1.Checked && !blueRadioButton2.Checked && !redRadioButton1.Checked && !redRadioButton2.Checked && !yellowRadioButton1.Checked && yellowRadioButton2.Checked)
                {
                    MessageBox.Show("Please Select the two colours you wish to mix");
                }
                //Display the defined colour variable as the background
                this.BackColor = colour;
            }
            //Do this in case of unexpected errors
            catch
            {
                MessageBox.Show("An unexpected error has occured, please revise your input or restart the application");
            }
        }

        private void resetButton_Click(object sender, EventArgs e)
        {
            //Uncheck all radiobuttons and return the background to its original colour
            redRadioButton1.Checked = false;
            redRadioButton2.Checked = false;
            blueRadioButton1.Checked = false;
            blueRadioButton2.Checked = false;
            yellowRadioButton1.Checked = false;
            yellowRadioButton2.Checked = false;
            colour = originalColour;
            this.BackColor = originalColour;
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            //Close the application
            this.Close();
        }
    }
}
