using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Card_Identifier
{
    public partial class cardIdentifierForm : Form
    {
        public cardIdentifierForm()
        {
            InitializeComponent();
        }

        private void exodiaHeadPictureBox_Click(object sender, EventArgs e)
        {
            displayLabel.Text = "Exodia, The Forbidden One (Head)";
            //Clicking the head image will change the text of the label to "Exodia, The Forbidden One (Head)".
        }

        private void leftArmPictureBox_Click(object sender, EventArgs e)
        {
            displayLabel.Text = "Exodia's Left Arm";
            //Clicking the left arm image will change the text of the label to "Exodia's Left Arm".
        }

        private void leftLegPictureBox_Click(object sender, EventArgs e)
        {
            displayLabel.Text = "Exodia's Left Leg";
            //Clicking the left leg image will change the text of the label to "Exodia's Left Leg".
        }

        private void rightArmPictureBox_Click(object sender, EventArgs e)
        {
            displayLabel.Text = "Exodia's Right Arm";
            //Clicking the right arm image will change the text of the label to "Exodia's Right Arm".
        }

        private void rightLegPictureBox_Click(object sender, EventArgs e)
        {
            displayLabel.Text = "Exodia's Right Leg";
            //Clicking the right leg image will change the text of the label to "Exodia's Right Leg".
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            this.Close();
            //Clicking the Exit button will close the form.
        }
    }
}
