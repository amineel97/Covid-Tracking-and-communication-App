using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Newtonsoft.Json;

using FireSharp.Config;
using FireSharp.Interfaces;
using FireSharp.Response;
using FireSharp;
using System.Timers;
using FireSharp.EventStreaming;
using FireSharp.Extensions;
using System.Net.NetworkInformation;
using System.Threading;
using GMap.NET.MapProviders;
using GMap.NET.WindowsForms.Markers;
using GMap.NET.WindowsForms;
using GMap.NET;

namespace project_TH
{
    public partial class Form1 : Form
    {
        string cellValue = "0";


        IFirebaseConfig config = new FirebaseConfig
        {
            AuthSecret = "OzuCQun3yaO7jdwQ3AxNyYAuOGIzi12ugyrzJhZp",
            BasePath = "https://myproject-f8353-default-rtdb.firebaseio.com/"

        };

        IFirebaseClient client;
        private EventStreamResponse listner;


        // FirebaseResponse res;

        public Form1()
        {
            InitializeComponent();




        }



        private void Form1_Load(object sender, EventArgs e)
        {

            textBox1.UseSystemPasswordChar = true;

            panel4.Enabled = false;

            pictureBox1.Visible = true;
            pictureBox2.Visible = false;

            tabControl2.TabPages.Remove(tabPage_2);


            comboBox2.Enabled = false;

            comboBox1.Items.Add("All Selected");
            comboBox1.Items.Add("Hour");
            comboBox1.Items.Add("Status");

            comboBox4.Items.Add("All");
            comboBox4.Items.Add("Select Date");

            dateTimePicker1.Enabled = false;

            panel9.Enabled = false;
            panel2.Enabled = false;


            dateTimePicker1.Format = DateTimePickerFormat.Custom;
            dateTimePicker1.CustomFormat = "dd/MM/yyyy";


            comboBox3.Items.Add("Status");
            comboBox3.Items.Add("Name");
            comboBox3.Items.Add("Surname");
            comboBox3.Items.Add("All");

            comboBox5.Items.Add("Under Quarantine");
            comboBox5.Items.Add("No Current Status");
          

            textBox3.Enabled = false;
            comboBox5.Enabled = false;



            gMapControl1.MapProvider = GMapProviders.GoogleMap;
            gMapControl1.DragButton = MouseButtons.Left;
            gMapControl1.MaxZoom = 100;
            gMapControl1.Zoom = 10;
            gMapControl1.Position = new PointLatLng(50.064633, 19.944883);


           

        }


        private void loadMapMarkers()
        {
            

            GMapOverlay markersOverlay = new GMapOverlay("markers");


           
            for(int i=0; i<dataGridView1.Rows.Count; i++)
            {

                GMarkerGoogle marker = new GMarkerGoogle(new PointLatLng(double.Parse(dataGridView4.Rows[i].Cells[4].Value.ToString()), double.Parse(dataGridView4.Rows[i].Cells[5].Value.ToString())),
                GMarkerGoogleType.red);
                markersOverlay.Markers.Add(marker);
                gMapControl1.Overlays.Add(markersOverlay);
            }

       
        }


        private void button1_Click(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {

            /*   FirebaseResponse res = client.Get("Contact_Box");
               Dictionary<string, contact_Message> data = JsonConvert.DeserializeObject<Dictionary<string, contact_Message>>(res.Body.ToString());

               populate(data);
            */

        }




        private void LoadData()
        {
            FirebaseResponse res = client.Get("Contact_Box");
            Dictionary<string, contact_Message> data = JsonConvert.DeserializeObject<Dictionary<string, contact_Message>>(res.Body.ToString());



            FirebaseResponse resVacc = client.Get("vaccination_appointment");
            Dictionary<string, appointment> dataVacc = JsonConvert.DeserializeObject<Dictionary<string, appointment>>(resVacc.Body.ToString());



            FirebaseResponse repCases = client.Get("ReportedCase");
            Dictionary<string, reportedCases> repCasesData = JsonConvert.DeserializeObject<Dictionary<string, reportedCases>>(repCases.Body.ToString());



            FirebaseResponse UStatus = client.Get("User_Status");
            Dictionary<string, quarantineStauts> UStatusData = JsonConvert.DeserializeObject<Dictionary<string, quarantineStauts>>(UStatus.Body.ToString());








            populate(data);

            populateVacc(dataVacc);

            populateRepCases(repCasesData);


            populateUserStatus(UStatusData);
            // dataGridView1.Columns[0].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill;
            // dataGridView1.Columns[1].AutoSizeMode = DataGridViewAutoSizeColumnMode.DisplayedCells;
            // dataGridView1.Columns[2].AutoSizeMode = DataGridViewAutoSizeColumnMode.DisplayedCells;


            // dataGridView1.Columns["newMsg"].Visible = false;

          

        }



        private void populateRepCases(Dictionary<string, reportedCases> record)
        {

            dataGridView4.Invoke(new Action(() => dataGridView4.Rows.Clear()));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns.Clear()));



            dataGridView4.Invoke(new Action(() => dataGridView4.Columns.Add("ID", "id")));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns.Add("CaseInfo", "CaseInfo")));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns.Add("CaseStatus", "CaseStatus")));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns.Add("FamilyMember", "FamilyMember")));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns.Add("Latitude", "Latitude")));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns.Add("Longitude", "Longitude")));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns.Add("REP_name", "REP_name")));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns.Add("REP_surname", "REP_surname")));

            dataGridView4.Invoke(new Action(() => dataGridView4.Columns[0].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns[1].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns[2].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns[3].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns[4].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns[5].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns[6].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView4.Invoke(new Action(() => dataGridView4.Columns[7].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));

                                                                                                                            
            foreach (var item in record)
            {
                dataGridView4.Invoke(new Action(() => dataGridView4.Rows.Add(item.Key, item.Value.CaseInfo, item.Value.CaseStatus, item.Value.FamilyMember, item.Value.Latitude, item.Value.Longitude, item.Value.REP_name, item.Value.REP_surname)));


            }



        }


        private void populate(Dictionary<string, contact_Message> record)
        {

            dataGridView1.Invoke(new Action(() => dataGridView1.Rows.Clear()));
            dataGridView1.Invoke(new Action(() => dataGridView1.Columns.Clear()));



            dataGridView1.Invoke(new Action(() => dataGridView1.Columns.Add("ID", "id")));
            dataGridView1.Invoke(new Action(() => dataGridView1.Columns.Add("Full_name", "full_name")));
            dataGridView1.Invoke(new Action(() => dataGridView1.Columns.Add("Message", "message")));
            dataGridView1.Invoke(new Action(() => dataGridView1.Columns.Add("NewMsg", "Notification")));

            dataGridView1.Invoke(new Action(() => dataGridView1.Columns[0].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView1.Invoke(new Action(() => dataGridView1.Columns[1].AutoSizeMode = DataGridViewAutoSizeColumnMode.AllCells));
            dataGridView1.Invoke(new Action(() => dataGridView1.Columns[2].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView1.Invoke(new Action(() => dataGridView1.Columns[3].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));


            foreach (var item in record)
            {
                dataGridView1.Invoke(new Action(() => dataGridView1.Rows.Add(item.Key, item.Value.Full_name, item.Value.Message, item.Value.newMsg)));
            }



            //----





        }



        //populate vaccincation  data

        private void populateVacc(Dictionary<string, appointment> record)
        {

            dataGridView2.Invoke(new Action(() => dataGridView2.Rows.Clear()));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns.Clear()));


            dataGridView2.Invoke(new Action(() => dataGridView2.Columns.Add("ID", "id")));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns.Add("Name", "name")));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns.Add("Surname", "surname")));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns.Add("Date", "date")));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns.Add("Hour", "hour")));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns.Add("Phone", "phone")));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns.Add("Status", "status")));

            dataGridView2.Invoke(new Action(() => dataGridView2.Columns[0].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns[1].AutoSizeMode = DataGridViewAutoSizeColumnMode.AllCells));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns[2].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns[3].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns[4].AutoSizeMode = DataGridViewAutoSizeColumnMode.AllCells));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns[5].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView2.Invoke(new Action(() => dataGridView2.Columns[6].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));


            foreach (var item in record)
            {
                dataGridView2.Invoke(new Action(() => dataGridView2.Rows.Add(item.Key, item.Value.name, item.Value.surname, item.Value.appointment_Date, item.Value.hour, item.Value.phone, item.Value.appointment_accepted)));

            }



            datagridView2_Visibility_elements();
        }



        private void populateUserStatus(Dictionary<string, quarantineStauts> record)
        {

            dataGridView3.Invoke(new Action(() => dataGridView3.Rows.Clear()));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns.Clear()));



            dataGridView3.Invoke(new Action(() => dataGridView3.Columns.Add("ID", "id")));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns.Add("Name", "name")));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns.Add("Surname", "surname")));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns.Add("Status", "status")));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns.Add("Days", "days")));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns.Add("Info", "info")));


            dataGridView3.Invoke(new Action(() => dataGridView3.Columns[0].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns[1].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns[2].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns[3].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns[4].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));
            dataGridView3.Invoke(new Action(() => dataGridView3.Columns[5].AutoSizeMode = DataGridViewAutoSizeColumnMode.Fill));


            foreach (var item in record)
            {
                dataGridView3.Invoke(new Action(() => dataGridView3.Rows.Add(item.Key, item.Value.name, item.Value.surname, item.Value.Current_status, item.Value.Quarantine_days, item.Value.Reminder_info)));

            }








        }


        async void SetListener()
        {

            EventStreamResponse response = await client.OnAsync("Contact_Box", added: (s, args, context) =>
            {
                Console.WriteLine(args.Data);

            },





        changed: (s, args, context) =>
        {
            Console.WriteLine(args.Data);



          //  MessageBox.Show("changed cont");


            FirebaseResponse res = client.Get("Contact_Box");
            Dictionary<string, contact_Message> data = JsonConvert.DeserializeObject<Dictionary<string, contact_Message>>(res.Body.ToString());
            populate(data);

            showLastLines();






            System.Threading.Tasks.Task.Factory.StartNew(() =>
            {
                Thread.Sleep(500);
                this.Invoke(new Action(() =>
                datatxtRefresh()




                    ));
            });

        },

        removed: (s, args, context) =>
         {
             //LoadData();
             Console.WriteLine(args);
             //   MessageBox.Show("removed");
         });



        }





        async void SetListenerQuarantine()
        {

            EventStreamResponse response = await client.OnAsync("User_Status", added: (s, args, context) =>
            {
                Console.WriteLine(args.Data);

            },





        changed: (s, args, context) =>
        {
            Console.WriteLine(args.Data);



            //  MessageBox.Show("changed cont");



            FirebaseResponse UStatus = client.Get("User_Status");
            Dictionary<string, quarantineStauts> UStatusData = JsonConvert.DeserializeObject<Dictionary<string, quarantineStauts>>(UStatus.Body.ToString());
 
            populateUserStatus(UStatusData);





            System.Threading.Tasks.Task.Factory.StartNew(() =>
            {
                Thread.Sleep(500);
                this.Invoke(new Action(() =>
                datatxtRefresh()




                    ));
            });

        },

        removed: (s, args, context) =>
        {
            //LoadData();
            Console.WriteLine(args);
            //   MessageBox.Show("removed");
        });



        }

        async void SetVaccListener()
        {

            EventStreamResponse responseVacc = await client.OnAsync("vaccination_appointment", added: (s, args, context) =>
            {
                Console.WriteLine(args.Data);

            },





        changed: (s, args, context) =>
        {
            Console.WriteLine(args.Data);



            //    MessageBox.Show("changed vacc");


         //   dataGridView2.ClearSelection();

            FirebaseResponse resVacc = client.Get("vaccination_appointment");
            Dictionary<string, appointment> dataVacc = JsonConvert.DeserializeObject<Dictionary<string, appointment>>(resVacc.Body.ToString());
            populateVacc(dataVacc);





   

            //  richTextBox1.Text = "";
            //   richTextBox1.Text = dataGridView1.Rows[rowIndex].Cells[2].Value.ToString();
            //    richTextBox1.Text = dataGridView1.

            System.Threading.Tasks.Task.Factory.StartNew(() =>
            {
                Thread.Sleep(400);
                this.Invoke(new Action(() =>
                datatxtRefresh()




                    ));
            });










            


        },

        removed: (s, args, context) =>
        {
            //LoadData();
            Console.WriteLine(args);
            //   MessageBox.Show("removed");
        });



        }




        private void datatxtRefresh()
        {
            string searchingFor = label10.Text;
            int rowIndex = 0;
            foreach (DataGridViewRow row in dataGridView1.Rows)
            {
                foreach (DataGridViewCell cell in row.Cells)
                {
                    if (cell.Value.ToString() == searchingFor)
                        rowIndex = row.Index;
                }
            }


            richTextBox1.Text = dataGridView1.Rows[rowIndex].Cells[2].Value.ToString();
            showLastLines();
        }

        private async void button3_Click(object sender, EventArgs e)
        {

            if (string.IsNullOrWhiteSpace(richTextBox2.Text) || richTextBox2.Text == "")
            {

            }
            else {

                int rowindex = dataGridView1.CurrentCell.RowIndex;
                //    int columnindex = dataGridView1.CurrentCell.ColumnIndex;

                //-------------------------

                /*      if (dataGridView1.SelectedCells.Count > 0)
                      {
                          int selectedrowindex = dataGridView1.SelectedCells[0].RowIndex;
                         // DataGridViewRow selectedRow = dataGridView1.Rows[selectedrowindex];
                         // cellValue = Convert.ToString(selectedRow.Cells["id"].Value);
                      }
                */
                //---------------------------


                var Contact_Message = new contact_Message
                {
                    Full_name = label11.Text,
                    Message = richTextBox1.Text + " \n" + "• Admin: " + richTextBox2.Text,
                    newMsg = "No"
                };

                string loc_id = label10.Text;

                FirebaseResponse response = await client.UpdateAsync("Contact_Box/" + loc_id, Contact_Message);

                contact_Message c_Rsult = response.ResultAs<contact_Message>();
                //MessageBox.Show("Data Updated");


                richTextBox2.Text = "";


                
          
            }






            //--------------------search And Select after datagridview been refreshed------------------------






        }



        private void showLastLines()
        {
            richTextBox1.Invoke(new Action(() => richTextBox1.SelectionStart = richTextBox1.Text.Length));

            richTextBox1.Invoke(new Action(() => richTextBox1.ScrollToCaret()));
            // scroll it automatically

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        { }

        private void dataGridView1_CellEnter(object sender, DataGridViewCellEventArgs e)
        {
            /*  if (e.ColumnIndex == 0 || e.ColumnIndex == 1 || e.ColumnIndex == 2)
              {
                  string value = dataGridView1.Rows[e.RowIndex].Cells[2].Value.ToString();
                  richTextBox1.Text = value;
              }*/
        }

        private void monthCalendar1_DateChanged(object sender, DateRangeEventArgs e)
        {

        }

        async void LiveCall()
        {
            while (true)
            {
                FirebaseResponse res = client.Get("Contact_Box");
            }
        }

        private void button8_Click(object sender, EventArgs e)
        {

        }

        private void openFileDialog1_FileOk(object sender, CancelEventArgs e)
        {

        }

        private void tabControl1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private async void button1_Click_1(object sender, EventArgs e)
        {

            DialogResult dialogResult = MessageBox.Show("Delete all the conversation with the user?", "Clear conversation", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);
            if (dialogResult == DialogResult.Yes)
            {

                int rowindex = dataGridView1.CurrentCell.RowIndex;
                var Contact_Message = new contact_Message
                {
                    Full_name = dataGridView1.Rows[rowindex].Cells[1].Value.ToString(),
                    Message = "",
                    newMsg = "No"
                };

                string loc_id = dataGridView1.Rows[rowindex].Cells[0].Value.ToString();

                FirebaseResponse response = await client.UpdateAsync("Contact_Box/" + loc_id, Contact_Message);
                contact_Message c_Rsult = response.ResultAs<contact_Message>();




            }
            else if (dialogResult == DialogResult.No)
            {

            }





        }

        public static bool IsAvailableNetworkActive()
        {
            // only recognizes changes related to Internet adapters
            if (System.Net.NetworkInformation.NetworkInterface.GetIsNetworkAvailable())
            {
                // however, this will include all adapters -- filter by opstatus and activity
                NetworkInterface[] interfaces = System.Net.NetworkInformation.NetworkInterface.GetAllNetworkInterfaces();
                return (from face in interfaces
                        where face.OperationalStatus == OperationalStatus.Up
                        where (face.NetworkInterfaceType != NetworkInterfaceType.Tunnel) && (face.NetworkInterfaceType != NetworkInterfaceType.Loopback)
                        select face.GetIPv4Statistics()).Any(statistics => (statistics.BytesReceived > 0) && (statistics.BytesSent > 0));
            }

            return false;
        }


        private void button5_Click(object sender, EventArgs e)
        {
            if (IsAvailableNetworkActive())
            {

                dataGridView1.RowHeadersVisible = false;


                client = new FireSharp.FirebaseClient(config);

                if (client != null)
                {

                    //  MessageBox.Show("Connection is established");

                    pictureBox2.Visible = true;
                    pictureBox1.Visible = false;

                    button5.BackColor = Color.Green;
                    label5.ForeColor = Color.Green;
                    label5.Text = "Connected";
                    panel4.Enabled = true;
                    button5.Enabled = false;
                    //    panel4.BackColor = Color.Green;


                }

            }

            if (!IsAvailableNetworkActive())
            {
                MessageBox.Show("Please make sure you are connected to Internet");
            }

        }



        private async void button6_Click(object sender, EventArgs e)
        {
            if(textBox2.Text == "" && textBox1.Text == "")
            {


                tabControl2.TabPages.Remove(tabPage_1);
                tabControl2.TabPages.Add(tabPage_2);

                LoadData();
                SetListener();
                SetVaccListener();
                SetListenerQuarantine();
            }







        }


        private void LoadDashboard()
        {
            int i = 0;

            foreach (DataGridViewRow Myrow in dataGridView1.Rows)
            {            //Here 2 cell is target value and 1 cell is Volume
                if (dataGridView1.Rows[Myrow.Index].Cells[3].Value.ToString() == "Yes")// Or your condition 
                {
                    Myrow.DefaultCellStyle.BackColor = Color.Green;
                    i++;

                }
                else
                {

                }
            }


        }

        private void tabPage_1_Click(object sender, EventArgs e)
        {

        }
        private async void button2_Click_1(object sender, EventArgs e)
        {
          

                int rowindex = dataGridView2.CurrentCell.RowIndex;

                var appointment = new appointment
                {
                    appointment_Date = dataGridView2.Rows[rowindex].Cells[3].Value.ToString(),
                    appointment_accepted = "approved",
                    hour = dataGridView2.Rows[rowindex].Cells[4].Value.ToString(),
                    name = dataGridView2.Rows[rowindex].Cells[1].Value.ToString(),
                    phone = dataGridView2.Rows[rowindex].Cells[5].Value.ToString(),
                    surname = dataGridView2.Rows[rowindex].Cells[2].Value.ToString()


                };




                string loc_id = dataGridView2.Rows[rowindex].Cells[0].Value.ToString();

                FirebaseResponse response = await client.UpdateAsync("vaccination_appointment/" + loc_id, appointment);
                appointment c_Rsult = response.ResultAs<appointment>();


            //   dataGridView2.Rows.Clear();
            //   populateVacc(dataVacc);

            //MessageBox.Show("Done!");

         //   refreshVacc();
        }

        private async void button4_Click(object sender, EventArgs e)
        {
         

                try
            {
                FirebaseResponse resVacc = client.Get("vaccination_appointment");
                Dictionary<string, appointment> dataVacc = JsonConvert.DeserializeObject<Dictionary<string, appointment>>(resVacc.Body.ToString());
                //----------------------------------------------------------------------------------------------------



                int rowindex = dataGridView2.CurrentCell.RowIndex;

                var appointment = new appointment
                {
                    appointment_Date = dataGridView2.Rows[rowindex].Cells[3].Value.ToString(),
                    appointment_accepted = "disapproved",
                    hour = dataGridView2.Rows[rowindex].Cells[4].Value.ToString(),
                    name = dataGridView2.Rows[rowindex].Cells[1].Value.ToString(),
                    phone = dataGridView2.Rows[rowindex].Cells[5].Value.ToString(),
                    surname = dataGridView2.Rows[rowindex].Cells[2].Value.ToString()


                };




                string loc_id = dataGridView2.Rows[rowindex].Cells[0].Value.ToString();

                FirebaseResponse response = await client.UpdateAsync("vaccination_appointment/" + loc_id, appointment);
                appointment c_Rsult = response.ResultAs<appointment>();


                //      dataGridView2.Rows.Clear();
                //     populateVacc(dataVacc);

                //  MessageBox.Show("Done!")

          //      refreshVacc();
            }
            catch (Exception ex) { }
        

    }

        

        private void comboBox1_SelectionChangeCommitted(object sender, EventArgs e)
        {
            if (comboBox1.SelectedItem.ToString() == "Hour")
            {
                comboBox2.Enabled = true;
                comboBox2.Items.Clear();
                comboBox2.Items.Add("10:30 AM");
                comboBox2.Items.Add("13:00 PM");
                comboBox2.Items.Add("16:30 PM");
                panel3.Enabled = false;
            }

            if (comboBox1.SelectedItem.ToString() == "All Selected")
            {
                comboBox2.Enabled = false;
                comboBox2.Items.Clear();
                panel3.Enabled = false;
            }

            if (comboBox1.SelectedItem.ToString() == "Status")
            {
                comboBox2.Enabled = true;
                comboBox2.Items.Clear();
                comboBox2.Items.Add("pending");
                comboBox2.Items.Add("approved");
                comboBox2.Items.Add("disapproved");
                panel3.Enabled = false;
            }
         //   dataGridView2.ClearSelection();

        }

        private void dataGridView1_Enter(object sender, EventArgs e)
        {

        }

        private void dataGridView1_RowEnter(object sender, DataGridViewCellEventArgs e)
        {
          /*  if (e.ColumnIndex == 0 || e.ColumnIndex == 1 || e.ColumnIndex == 2)
            {
                string value = dataGridView1.Rows[e.RowIndex].Cells[2].Value.ToString();
                richTextBox1.Text = value;
            }*/
        }

        private void button14_Click(object sender, EventArgs e)
        {
            

            int rowindex = dataGridView1.CurrentCell.RowIndex;

            string userID = dataGridView1.Rows[rowindex].Cells[0].Value.ToString();
            string Fullname = dataGridView1.Rows[rowindex].Cells[1].Value.ToString();
            string Message = dataGridView1.Rows[rowindex].Cells[2].Value.ToString();

            label10.Text = userID;
            label11.Text = Fullname;

            richTextBox1.Text = Message;

            panel9.Enabled = true;
            panel2.Enabled = true;
        }

        private void dataGridView1_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            foreach (DataGridViewRow Myrow in dataGridView1.Rows)
            {            //Here 2 cell is target value and 1 cell is Volume
                if (dataGridView1.Rows[Myrow.Index].Cells[3].Value.ToString() == "Yes")// Or your condition 
                {
                    Myrow.DefaultCellStyle.BackColor = Color.Green;

             
                }
                else
                {
                    
                }
            }
        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
               if(comboBox1.SelectedItem.ToString() == "Hour")
              {

                for (int i = 0; i < dataGridView2.Rows.Count; i++)
                {
                    dataGridView2.Rows[i].Visible = false;
                }

                    for (int i = 0; i < dataGridView2.Rows.Count; i++)
                {
                    if (dataGridView2.Rows[i].Cells[4].Value.ToString() == comboBox2.Text)
                    {
                      //  dataGridView2.Rows[i].Selected = true;
                        dataGridView2.Rows[i].Visible = true;
                        break;
                    }
                    else
                    {
                       // dataGridView2.Rows[i].Selected = false;
                        dataGridView2.Rows[i].Visible = false;

                    }
                }

                panel3.Enabled = false;
            }



            if (comboBox1.SelectedItem.ToString() == "Status")
            {

                for (int i = 0; i < dataGridView2.Rows.Count; i++)
                {
                    dataGridView2.Rows[i].Visible = false;
                }

                for (int i = 0; i < dataGridView2.Rows.Count; i++)
                {
                    if (dataGridView2.Rows[i].Cells[6].Value.ToString() == comboBox2.Text)
                    {
                        //  dataGridView2.Rows[i].Selected = true;
                        dataGridView2.Rows[i].Visible = true;
                        break;
                    }
                    else
                    {
                        // dataGridView2.Rows[i].Selected = false;
                        dataGridView2.Rows[i].Visible = false;

                    }
                }
              

            }
        //    dataGridView2.ClearSelection();

        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {

            if (comboBox1.SelectedItem.ToString() == "All Selected")
            {
                for (int i = 0; i < dataGridView2.Rows.Count; i++)
                {
                    dataGridView2.Rows[i].Visible = false;
                }



                for (int i = 0; i < dataGridView2.Rows.Count; i++)
                {
                    dataGridView2.Rows[i].Visible = true;
                }

                comboBox2.Text = "Select";
                comboBox2.Enabled = false;
             //   dataGridView2.ClearSelection();

            }

            datagridView2_Visibility_elements();
        }

        private async void button7_Click(object sender, EventArgs e)
        {
            DateTime dTP2 = dateTimePicker2.Value;
            DateTime dTP3 = dateTimePicker3.Value;

            TimeSpan tspan = dTP3 - dTP2;
            int differenceInDays = tspan.Days;
            if (differenceInDays > 0)
            {

                DialogResult dialogResult = MessageBox.Show("Apply Quarantine?", "Confirmation", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);
                if (dialogResult == DialogResult.Yes)
                {




                    string differenceAsString = differenceInDays.ToString();

                    int rowindex = dataGridView3.CurrentCell.RowIndex;

                    var qStauts = new quarantineStauts
                    {

                        name = dataGridView3.Rows[rowindex].Cells[1].Value.ToString(),
                        surname = dataGridView3.Rows[rowindex].Cells[2].Value.ToString(),
                        Current_status = "Under Quarantine",
                        Quarantine_days = differenceAsString,
                        Reminder_info = richTextBox3.Text,
                        StartingDate = "starting from: " + dateTimePicker2.Value.ToString()
                    };

                    string loc_id = dataGridView3.Rows[rowindex].Cells[0].Value.ToString();

                    FirebaseResponse response = await client.UpdateAsync("User_Status/" + loc_id, qStauts);
                    quarantineStauts c_Rsult = response.ResultAs<quarantineStauts>();

                    richTextBox3.Text = "";
                    refreshDatagridView3();
                }
                if (dialogResult == DialogResult.No) { }
            }
            else { MessageBox.Show("Wrong Date has been taken"); }

       
        }

        private async void button11_Click(object sender, EventArgs e)
        {
             

            int rowindex = dataGridView3.CurrentCell.RowIndex;

            var qStauts = new quarantineStauts
            {

                name = dataGridView3.Rows[rowindex].Cells[1].Value.ToString(),
                surname = dataGridView3.Rows[rowindex].Cells[2].Value.ToString(),
                Current_status = "No Current Status",
                Quarantine_days = "0",
                Reminder_info = "No Information",

            };

            string loc_id = dataGridView3.Rows[rowindex].Cells[0].Value.ToString();

            FirebaseResponse response = await client.UpdateAsync("User_Status/" + loc_id, qStauts);
            quarantineStauts c_Rsult = response.ResultAs<quarantineStauts>();
        }

        private void tabPage6_Click(object sender, EventArgs e)
        {

        }

        private void button10_Click(object sender, EventArgs e)
        {

        

        }

        private async void button12_Click(object sender, EventArgs e)
        {
            string updtsInfo = richTextBox4.Text;
            string updateTitle = textBox4.Text;

            var updts = new updates
            {
                Title = updateTitle,
                Info = updtsInfo
            };

            FirebaseResponse response = await client.UpdateAsync("updates", updts);
            updates c_Rsult = response.ResultAs<updates>();


            richTextBox4.Clear();
        }

        private async void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
         

        }

        private void button16_Click(object sender, EventArgs e)
        {
            tabControl2.TabPages.Add(tabPage_1);
            tabControl2.TabPages.Remove(tabPage_2);

        }

        private void button15_Click(object sender, EventArgs e)
        {

        }

        private async void button13_Click(object sender, EventArgs e)
        {

            DialogResult result = MessageBox.Show("Do you want to clear last updates?", "Confirmation", MessageBoxButtons.YesNo);
            if (result == DialogResult.Yes)
            {
                var updts = new updates
                {
                    Info = "No Current Updates",
                };

                FirebaseResponse response = await client.UpdateAsync("updates", updts);
                updates c_Rsult = response.ResultAs<updates>();


                richTextBox4.Clear();
            }
            
            else
            {
               
            }

        }

        private void button17_Click(object sender, EventArgs e)
        {
         
                MessageBox.Show("Case ID: " + dataGridView2.CurrentRow.Cells[0].Value.ToString() + "\n" +
                                "Name: " + dataGridView2.CurrentRow.Cells[1].Value.ToString() + "\n" +
                                "Surname: " + dataGridView2.CurrentRow.Cells[2].Value.ToString() + "\n" +
                                "Appointment Date: " + dataGridView2.CurrentRow.Cells[3].Value.ToString() + "\n" +
                                "Appointment Hour: " + dataGridView2.CurrentRow.Cells[4].Value.ToString() + "\n" +
                                "Phone: " + dataGridView2.CurrentRow.Cells[5].Value.ToString() + "\n" +
                                "Status: " + dataGridView2.CurrentRow.Cells[6].Value.ToString() + "\n");
          
          
        
        }

        private void dateTimePicker1_CloseUp(object sender, EventArgs e)
        {
           
            

            for (int i = 0; i < dataGridView2.Rows.Count; i++)
            {
                dataGridView2.Rows[i].Visible = false;
            }

            for (int i = 0; i < dataGridView2.Rows.Count; i++)
            {
                if (dataGridView2.Rows[i].Cells[3].Value.ToString() == dateTimePicker1.Text)
                {
                    //  dataGridView2.Rows[i].Selected = true;
                    dataGridView2.Rows[i].Visible = true;
                    break;
                }
                else
                {
                    // dataGridView2.Rows[i].Selected = false;
                    dataGridView2.Rows[i].Visible = false;

                }
            }

            panel3.Enabled = false;
        }

        private void comboBox4_SelectionChangeCommitted(object sender, EventArgs e)
        {
            if(comboBox4.SelectedItem.ToString() == "All")
            {
                 
                    for (int i = 0; i < dataGridView2.Rows.Count; i++)
                    {
                        dataGridView2.Rows[i].Visible = false;
                     
                }


                    for (int i = 0; i < dataGridView2.Rows.Count; i++)
                    {
                        dataGridView2.Rows[i].Visible = true;
                    

                }

                    comboBox2.Text = "Select";
                    comboBox2.Enabled = false;
                    panel3.Enabled = false;

                clearAndDisableButtons();
            }

            if (comboBox4.SelectedItem.ToString() == "Select Date")
            {
                dateTimePicker1.Enabled = true;


                panel3.Enabled = false;
                clearAndDisableButtons();
            }
        }

        private void tabPage_2_Click(object sender, EventArgs e)
        {

        }

        private void tabPage3_Click(object sender, EventArgs e)
        {

        }

        private void button21_Click(object sender, EventArgs e)
        {
            
        }

        private void button21_Click_1(object sender, EventArgs e)
        {
            //----
            int i = 0;

            foreach (DataGridViewRow Myrow in dataGridView1.Rows)
            {            //Here 2 cell is target value and 1 cell is Volume
                if (dataGridView1.Rows[Myrow.Index].Cells[3].Value.ToString() == "Yes")// Or your condition 
                {
                    Myrow.DefaultCellStyle.BackColor = Color.Green;
                    i++;
                }

                else
                {

                }
            }



        }

 
        private void comboBox3_SelectionChangeCommitted(object sender, EventArgs e)
        {
            if (comboBox3.SelectedItem.ToString() == "All")
            {

                for (int i = 0; i < dataGridView2.Rows.Count; i++)
                {
                    dataGridView3.Rows[i].Visible = false;

                }


                for (int i = 0; i < dataGridView3.Rows.Count; i++)
                {
                    dataGridView3.Rows[i].Visible = true;


                }

             //   dataGridView3.ClearSelection();
                textBox3.Enabled = false;
                comboBox5.Enabled = false;
            }

            //-----------------

            if (comboBox3.SelectedItem.ToString() == "Name")
            {
                comboBox5.Enabled = false;
                textBox3.Enabled = true;
            }

            if (comboBox3.SelectedItem.ToString() == "Surname")
            {
                comboBox5.Enabled = false;
                textBox3.Enabled = true;
            }

            if (comboBox3.SelectedItem.ToString() == "Status")
            {
                comboBox5.Enabled = true;
                textBox3.Enabled = false;
            }

            textBox3.Text = "";
            clearAndDisableButtons();
        }

        private void button18_Click(object sender, EventArgs e)
        {
            refreshDatagridView2();
         //   refreshVacc();
        }

       /* private void refreshVacc()
        {

            //dataGridView2.Columns.Clear();
            dataGridView2.Rows.Clear();
            
            FirebaseResponse resVacc = client.Get("vaccination_appointment");
            Dictionary<string, appointment> dataVacc = JsonConvert.DeserializeObject<Dictionary<string, appointment>>(resVacc.Body.ToString());
            populateVacc(dataVacc);

        }
       */

        private void button21_Click_2(object sender, EventArgs e)
        {

          
             
                MessageBox.Show("ID: " + dataGridView3.CurrentRow.Cells[0].Value.ToString() + "\n" +
                       "Name: " + dataGridView3.CurrentRow.Cells[1].Value.ToString() + "\n" +
                       "Surname: " + dataGridView3.CurrentRow.Cells[2].Value.ToString() + "\n" +
                       "Status: " + dataGridView3.CurrentRow.Cells[3].Value.ToString() + "\n" +
                       "Days: " + dataGridView3.CurrentRow.Cells[4].Value.ToString() + "\n" +
                       "Info: " + dataGridView3.CurrentRow.Cells[5].Value.ToString());
           
           
        }

        private async void button8_Click_1(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Cancel User Quarantine?", "Quarantine", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);
            if (dialogResult == DialogResult.Yes)
            {
                DateTime dTP2 = dateTimePicker2.Value;
                DateTime dTP3 = dateTimePicker3.Value;

                TimeSpan tspan = dTP3 - dTP2;

                int differenceInDays = tspan.Days;

                string differenceAsString = differenceInDays.ToString();

                int rowindex = dataGridView3.CurrentCell.RowIndex;

                var qStauts = new quarantineStauts
                {

                    name = dataGridView3.Rows[rowindex].Cells[1].Value.ToString(),
                    surname = dataGridView3.Rows[rowindex].Cells[2].Value.ToString(),
                    Current_status = "No Current Status",
                    Quarantine_days = "0",
                    Reminder_info = "no current info",
                    StartingDate = ""
                };
     
                string loc_id = dataGridView3.Rows[rowindex].Cells[0].Value.ToString();

                FirebaseResponse response = await client.UpdateAsync("User_Status/" + loc_id, qStauts);
                quarantineStauts c_Rsult = response.ResultAs<quarantineStauts>();

                richTextBox3.Text = "";

                refreshDatagridView3();
            }
            if(dialogResult == DialogResult.No){
            
            }
        }

        private void button19_Click(object sender, EventArgs e)
        {
            refreshDatagridView3();


        }

        private void refreshDatagridView3()
        {
            dataGridView3.Rows.Clear();
           // dataGridView3.Columns.Clear();

            FirebaseResponse UStatus = client.Get("User_Status");
            Dictionary<string, quarantineStauts> UStatusData = JsonConvert.DeserializeObject<Dictionary<string, quarantineStauts>>(UStatus.Body.ToString());

            populateUserStatus(UStatusData);


            clearAndDisableButtons();
        }

        private void button9_Click(object sender, EventArgs e)
        {
            dataGridView2.Rows[0].Selected = true;
        }

        private void dataGridView3_RowEnter(object sender, DataGridViewCellEventArgs e)
        {
            button8.Enabled = true;
            button7.Enabled = true;
            button21.Enabled = true;
        }

        private void clearAndDisableButtons()
        {
            dataGridView3.ClearSelection();
            button8.Enabled = false;
            button7.Enabled = false;
            button21.Enabled = false;
        }

        private void comboBox5_SelectionChangeCommitted(object sender, EventArgs e)
        {
            
             for (int i = 0; i < dataGridView3.Rows.Count; i++)
                {
                    dataGridView3.Rows[i].Visible = false;
                }

                for (int i = 0; i < dataGridView3.Rows.Count; i++)
                {
                    if (dataGridView3.Rows[i].Cells[3].Value.ToString() == comboBox5.Text)
                    {
                        //  dataGridView2.Rows[i].Selected = true;
                        dataGridView3.Rows[i].Visible = true;
                        //   break;
                    }
                    else
                    {
                        // dataGridView2.Rows[i].Selected = false;
                        dataGridView3.Rows[i].Visible = false;

                    }
                }

           
        }

        private void textBox3_TextChanged(object sender, EventArgs e)
        {

            if (comboBox3.Text == "Surname")
            {
                
                    

                for (int i = 0; i < dataGridView3.Rows.Count; i++)
                {
                    dataGridView3.Rows[i].Visible = false;
                }

                for (int i = 0; i < dataGridView3.Rows.Count; i++)
                {
                    if (dataGridView3.Rows[i].Cells[2].Value.ToString().StartsWith(textBox3.Text))
                    {
                        //  dataGridView2.Rows[i].Selected = true;
                        dataGridView3.Rows[i].Visible = true;
                     //   break;
                    }
                    else
                    {
                        // dataGridView2.Rows[i].Selected = false;
                        dataGridView3.Rows[i].Visible = false;

                    }
                }

            }



            if (comboBox3.Text == "Name")
            {
             

                for (int i = 0; i < dataGridView3.Rows.Count; i++)
                {
                    dataGridView3.Rows[i].Visible = false;
                }

                for (int i = 0; i < dataGridView3.Rows.Count; i++)
                {
                    if (dataGridView3.Rows[i].Cells[1].Value.ToString().StartsWith(textBox3.Text))
                    {
                        //  dataGridView2.Rows[i].Selected = true;
                        dataGridView3.Rows[i].Visible = true;
                      //  break;
                    }
                    else
                    {
                        // dataGridView2.Rows[i].Selected = false;
                        dataGridView3.Rows[i].Visible = false;

                    }
                }

            }

        }

        private void dataGridView2_RowEnter(object sender, DataGridViewCellEventArgs e)
        {
            panel3.Enabled = true;
        }





        private void refreshDatagridView2()
        {
            dataGridView2.Rows.Clear();
           
            // dataGridView3.Columns.Clear();

            FirebaseResponse resVacc = client.Get("vaccination_appointment");
            Dictionary<string, appointment> dataVacc = JsonConvert.DeserializeObject<Dictionary<string, appointment>>(resVacc.Body.ToString());

            populateVacc(dataVacc);

            dataGridView2.ClearSelection();
            panel3.Enabled = false;


            datagridView2_Visibility_elements();
        }

        private void datagridView2_Visibility_elements()
        {
            for (int i = 0; i < dataGridView2.Rows.Count; i++)
            {
                dataGridView2.Rows[i].Visible = false;
            }

            for (int i = 0; i < dataGridView2.Rows.Count; i++)
            {
                if (dataGridView2.Rows[i].Cells[6].Value.ToString() == "no booked appointment")
                {
                    //  dataGridView2.Rows[i].Selected = true;
                    dataGridView2.Rows[i].Visible = false;
                    //   break;
                }
                else
                {
                    // dataGridView2.Rows[i].Selected = false;
                    dataGridView2.Rows[i].Visible = true;

                }
            }
        }

        private void comboBox2_KeyPress(object sender, KeyPressEventArgs e)
        {
            e.Handled = true;
        }

        private void comboBox4_KeyPress(object sender, KeyPressEventArgs e)
        {
            e.Handled = true;
        }

        private void comboBox1_KeyPress(object sender, KeyPressEventArgs e)
        {
            e.Handled = true;
        }

        private void comboBox3_KeyPress(object sender, KeyPressEventArgs e)
        {
            e.Handled = true;
        }

        private void comboBox5_KeyPress(object sender, KeyPressEventArgs e)
        {
            e.Handled = true;
        }

        private void dateTimePicker1_KeyPress(object sender, KeyPressEventArgs e)
        {
            e.Handled = true;
        }

        private void dateTimePicker2_KeyPress(object sender, KeyPressEventArgs e)
        {
            e.Handled = true;
        }

        private void dateTimePicker3_KeyPress(object sender, KeyPressEventArgs e)
        {
            e.Handled = true;
        }

        private void button10_Click_1(object sender, EventArgs e)
        {

        }

        private void button9_Click_1(object sender, EventArgs e)
        {

        }

        private void button9_Click_2(object sender, EventArgs e)
        {
            loadMapMarkers();
        }
    }
}
