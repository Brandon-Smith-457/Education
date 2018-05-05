using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Heads_or_Tails
{
    public partial class headsOrTailsForm : Form
    {
        public headsOrTailsForm()
        {
            InitializeComponent();
        }

        private void showHeadsButton_Click(object sender, EventArgs e)
        {
            headsPictureBox.Visible = true;
            //Clicking the Heads! button makes the image of Heads visible.
            tailsPictureBox.Visible = false;
            //Clicking the Heads! button makes the image of Tails invisible.
        }

        private void showTailsButton_Click(object sender, EventArgs e)
        {
            tailsPictureBox.Visible = true;
            //Clicking the Tails! button makes the image of Tails visible.
            headsPictureBox.Visible = false;
            //Clicking the Tails! button makes the image of Heads invisible.
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            this.Close();
            //Clicking the Exit button closes the form.
        }
    }
}
