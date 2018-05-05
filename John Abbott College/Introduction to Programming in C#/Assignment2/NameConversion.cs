using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Name_Conversion
{
    public partial class nameConverter : Form
    {
        //Create the 4 string variables involved in the input/output.
        string title;
        string firstName;
        string middleName;
        string lastName;

        public nameConverter()
        {
            InitializeComponent();
        }

        private void allocate()
            //Created a method to allocate the string values of the 4 string variables.
        {
            //Define the string variables with regards to their corresponding input textBoxes.
            title = titleTextBox.Text;
            firstName = firstNameTextBox.Text;
            middleName = middleNameTextBox.Text;
            lastName = lastNameTextBox.Text;
        }

        private void firstLastButton_Click(object sender, EventArgs e)
        {
            allocate();
            //Call the allocate method.
            displayLabel.Text = firstName + " " + lastName;
            //Change the display label's text to the desired information.
        }

        private void lastFirstButton_Click(object sender, EventArgs e)
        {
            allocate();
            //Call the allocate method.
            displayLabel.Text = lastName + ", " + firstName;
            //Change the display label's text to the desired information.
        }

        private void titleFirstMiddleLastButton_Click(object sender, EventArgs e)
        {
            allocate();
            //Call the allocate method.
            displayLabel.Text = title+" "+firstName+" "+middleName+" "+lastName;
            //Change the display label's text to the desired information.
        }

        private void lastFirstMiddleTitleButton_Click(object sender, EventArgs e)
        {
            allocate();
            //Call the allocate method.
            displayLabel.Text = lastName + ", " + firstName + " " + middleName + ". " + title;
            //Change the display label's text to the desired information.

            if (title == "Dr." || title == "Doctor" || title == "Doc" || title == "Dr" ||
                title == "dr." || title == "doctor" || title == "doc" || title == "dr")
                //Condition: if the input for title is any iteration of Doctor, the output will display PHD.
            {
                displayLabel.Text = lastName + ", " + firstName + " " + middleName + ". " + "PHD.";
            }
            if (title == "Sir" || title == "sir")
                //Condition: if the input for title is Sir, the output will display Knight.
            {
                displayLabel.Text = lastName + ", " + firstName + " " + middleName + ". " + "Knight.";
            }
        }

        private void firstMiddleLast_Click(object sender, EventArgs e)
        {
            allocate();
            //Call the allocate method.
            displayLabel.Text = firstName + " " + middleName + " " + lastName;
            //Change the display label's text to the desired information.
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
