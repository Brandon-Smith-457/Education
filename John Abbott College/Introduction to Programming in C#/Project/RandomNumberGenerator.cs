using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Microsoft.VisualBasic;

namespace Random_Number_Generator
{
    public partial class randomNumberForm : Form
    {
        private const int LOWER_BOUND = 0;
        private const int UPPER_BOUND = 101;
        private const string CLEAR = "";
        int generatedN = LOWER_BOUND;
        int guessedN;
        int numberOfGuesses;
        string outputMessage;
        Random random = new Random();

        public randomNumberForm()
        {
            InitializeComponent();
        }

        private void generateButton_Click(object sender, EventArgs e)
        {
            generatedN = random.Next(LOWER_BOUND + 1, UPPER_BOUND);
            generateButton.Enabled = false;
            guessButton.Enabled = true;
            numberOfGuesses = 0;
            if (cheatCheckBox.Checked == true)
            {
                displayCheatLabel.Text = Convert.ToString(generatedN);
            }
            else
            {
                //Want nothing to happen here.
            }
        }

        private void guessButton_Click(object sender, EventArgs e)
        {
            try
            {
                if (int.TryParse(inputTextBox.Text, out guessedN))
                {
                    if (guessedN > LOWER_BOUND && guessedN < UPPER_BOUND)
                    {
                        if (guessedN == generatedN && generatedN != LOWER_BOUND)
                        {
                            numberOfGuesses++;
                            if (numberOfGuesses == 1)
                            {
                                outputMessage = "Your guess of " + Convert.ToString(guessedN) + " is correct.  You got it on your first try!!! Good job!!";
                            }
                            else
                            {
                                outputMessage = "Your guess of " + inputTextBox.Text + " is correct.  Good Job! It took you " + Convert.ToString(numberOfGuesses) + " tries!";
                            }
                            generateButton.Enabled = true;
                            guessButton.Enabled = false;
                        }
                        else if (guessedN < generatedN && generatedN != LOWER_BOUND)
                        {
                            outputMessage = "Your guess of " + inputTextBox.Text + " was wrong! You were smaller than the value!";
                            numberOfGuesses++;
                        }
                        else if (guessedN > generatedN && generatedN != LOWER_BOUND)
                        {
                            outputMessage = "Your guess of " + inputTextBox.Text + " was wrong! You were larger than the value!";
                            numberOfGuesses++;
                        }
                        else if (generatedN == LOWER_BOUND)
                        {
                            outputMessage = "Please generate a number before guessing!";
                        }
                        else
                        {
                            outputMessage = "WHAT HAVE YOU DONE?!!?!?!?";
                        }
                    }
                    else
                    {
                        outputMessage = "Please input an integer from 1 to 100!";
                    }
                }
                else
                {
                    outputMessage = "Please input an integer from 1 to 100!";
                }
            }
            catch
            {
                outputMessage = "An unexpected error has occured!";
            }

            outputLabel.Text = outputMessage;
            inputTextBox.Text = CLEAR;
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void cheatCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            try
            {
                if (cheatCheckBox.Checked == true)
                {
                    if (!(generatedN > LOWER_BOUND && generatedN < UPPER_BOUND))
                    {
                        displayCheatLabel.Text = "Generate a number first you scrub!";
                    }
                    else
                    {
                        displayCheatLabel.Text = Convert.ToString(generatedN);
                    }
                }
                else if (cheatCheckBox.Checked == false)
                {
                    displayCheatLabel.Text = CLEAR;
                }
                else
                {
                    displayCheatLabel.Text = "What just happened?";
                }
            }
            catch
            {
                displayCheatLabel.Text = "What just happened?";
            }
        }

        private void resetButton_Click(object sender, EventArgs e)
        {
            generatedN = LOWER_BOUND;
            inputTextBox.Text = CLEAR;
            outputLabel.Text = CLEAR;
            displayCheatLabel.Text = CLEAR;
            cheatCheckBox.Checked = false;
            generateButton.Enabled = true;
            guessButton.Enabled = false;
        }
    }
}
