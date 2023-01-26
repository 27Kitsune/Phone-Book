//Importing various libraries.
import java.util.Vector;
import java.util.HashSet;
import java.util.Iterator;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

//Class PhoneInfo, to store the main information of contacts.
class PhoneInfo implements Serializable
{
	String name;
	String phoneNumber;
	String notes;
	String date;
	String gender;

	//Constructor.
	public PhoneInfo(String name, String num, String note, String date, String gender)
	{
		this.name = name;
		phoneNumber = num;
		this.notes = note;
		this.date = date;
		this.gender = gender;
	}

	//Display contact information.
	public void showPhoneInfo()
	{
		System.out.println("name: "+name);
		System.out.println("phone: "+phoneNumber);
		System.out.println("Note: "+ notes);
		System.out.println("Birthday Date: "+ date);
	}

	//Return string values of stored data.
	public String toString()
	{
		return "Name: " + name + '\n' + "Phone: " + phoneNumber +'\n'+ "Note: " + notes + '\n' + "Birthday Date: " + date + '\n' + "Gender: " + gender + '\n';
	}

	//Return the hashcode value of the name variable.
	public int hashCode()
	{
		return name.hashCode();
	}

	//To compare the names for stored contact.
	public boolean equals(Object obj)
	{
		PhoneInfo cmp = (PhoneInfo)obj;
		if (name.compareTo(cmp.name) == 0)
			return true;
		else
			return false;
	}
}

//Extension of PhoneInfo, for university student entries.
class PhoneUnivInfo extends PhoneInfo
{
	String major;
	int year;

	//Constructor.
	public PhoneUnivInfo(String name, String num, String note, String date, String gender, String major, int year)
	{
		super(name, num, note, date, gender);
		this.major=major;
		this.year=year;
	}

	//Display university contact information.
	public void showPhoneInfo()
	{
		super.showPhoneInfo();
		System.out.println("Major: "+major);
		System.out.println("Year: "+year);
	}

	//Return string values of university contact information.
	public String toString()
	{
		return super.toString()
				+"Major: "+major+'\n'+"Year: "+year+'\n';
	}
}

//Extension of PhoneInfo, for company workers.
class PhoneCompanyInfo extends PhoneInfo
{
	String company;

	//Constructor.
	public PhoneCompanyInfo(String name, String num, String note, String date, String gender, String company)
	{
		super(name, num, note, date, gender);
		this.company = company;
	}

	//Display contact information of company workers.
	public void showPhoneInfo()
	{
		super.showPhoneInfo();
		System.out.println("Company: "+company);
	}

	//Return string values of stored data.
	public String toString()
	{
		return super.toString()
				+"Company: "+company+'\n';
	}
}

//To manage data of PhoneBook.
class PhoneBookManager
{
	//Open text file and create new HashSet based on class PhoneInfo.
	private final File dataFile = new File("src/datafile.txt");
	HashSet<PhoneInfo> infoStorage = new HashSet<PhoneInfo>();
	static PhoneBookManager inst = null;

	//Create manager instance.
	public static PhoneBookManager createManagerInst()
	{
		if(inst == null)
			inst = new PhoneBookManager();
		return inst;
	}

	//Constructor.
	private PhoneBookManager()
	{
		readFromFile();
	}

	//To search for a certain data record based on name from PhoneInfo.
	public String searchData(String name)
	{
		PhoneInfo info = search(name);
		if(info == null)
			return null;
		else
			return info.toString();
	}

	//Delete data based on name and phone number in PhoneInfo.
	public boolean deleteData(String name)
	{
		Iterator<PhoneInfo> itr=infoStorage.iterator();
		while(itr.hasNext())
		{
			PhoneInfo curInfo=itr.next();
			if(name.compareTo(curInfo.name) == 0 || name.compareTo(curInfo.phoneNumber) == 0) //Improvement 1
			{
				itr.remove();
				return true;
			}
		}
		return false;
	}

	//Function to search data in PhoneInfo hashset based on name and phone number.
	private PhoneInfo search(String name)
	{
		Iterator<PhoneInfo> itr = infoStorage.iterator();
		while(itr.hasNext())
		{
			PhoneInfo curInfo=itr.next();
			if(name.compareTo(curInfo.name) == 0 || name.compareTo(curInfo.phoneNumber) == 0) //Improvement 1
				return curInfo;
		}
		return null;
	}

	//Function to store data entries in a .txt file.
	public boolean storeToFile(String FileName)
	{
		try
		{
			FileWriter file = new FileWriter(FileName);
			BufferedWriter bw = new BufferedWriter(file);

			Iterator<PhoneInfo> itr = infoStorage.iterator();
			while(itr.hasNext()) {
				bw.write(String.valueOf(itr.next()));
				bw.newLine();
			}
			bw.close();
			return true;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	//Read from a .txt file. Currently, this function does not work properly.
	public void readFromFile()
	{
		if(dataFile.exists() == false) {
			return;
		}
		try
		{
			FileInputStream file = new FileInputStream(dataFile);
			ObjectInputStream in = new ObjectInputStream(file);
			while(true)
			{
				PhoneInfo info = (PhoneInfo)in.readObject();
				if(info == null)
					break;
				infoStorage.add(info);
			}
			in.close();
		}
		catch(IOException e)
		{
			return;
		}
		catch(ClassNotFoundException e)
		{
			return;
		}
	}
}

//Handles the events when a search is queried. It involves the usage of GUI.
class SearchEventHandler implements ActionListener
{
	JTextField searchField;
	JTextArea textArea;

	//Constructor.
	public SearchEventHandler(JTextField field, JTextArea area)
	{
		searchField=field;
		textArea=area;
	}

	//Action that will be performed once the user clicks search on the GUI.
	public void actionPerformed(ActionEvent e)
	{
		String name = searchField.getText();
		PhoneBookManager manager=PhoneBookManager.createManagerInst();
		String srchResult = manager.searchData(name);
		if(srchResult == null)
		{
			textArea.append("Search Failed: info does not exist.\n");
		}
		else
		{
			textArea.append("Search Completed:\n");
			textArea.append(srchResult);
			textArea.append("\n");
		}
	}
}

//Handles the events when an add is queried. It involves the usage of GUI.
class AddEventHandler implements ActionListener
{
	JTextField name;
	JTextField phone;
	JTextField major;
	JTextField year;
	JTextField company;
	JTextField note;
	JTextField BD;
	JRadioButton gender;
	String x;
	JTextArea text;
	Vector<String> inputList = new Vector<String>();

	boolean isAdded;

	PhoneInfo info;

	//Constructor.
	public AddEventHandler(JTextField nameField, JTextField phoneField, JTextField noteField, JTextField BDField, JRadioButton button, JTextField majorField, JTextField yearField, JTextArea textArea)
	{
		name = nameField;
		phone = phoneField;
		note = noteField;
		BD = BDField;
		gender = button;
		major = majorField;
		year = yearField;
		text = textArea;
	}

	//Action that is performed when the add button is clicked.
	public void actionPerformed(ActionEvent e)
	{
		if(gender.isSelected())
		{
			x = "Male";
		}
		else
		{
			x = "Female";
		}
		PhoneBookManager manager = PhoneBookManager.createManagerInst();
		if(major.getText().equals("") == false && year.getText().equals("") == true)
		{
			company = major;
			info = new PhoneCompanyInfo(name.getText(), phone.getText(), note.getText(), BD.getText(), x, company.getText());
			isAdded = manager.infoStorage.add(info);
		}
		else if(major.getText().equals("") == false && year.getText().equals("") == false)
		{
			//Improvement 4
			try
			{
				info = new PhoneUnivInfo(name.getText(), phone.getText(), note.getText(), BD.getText(), x, major.getText(), Integer.parseInt(year.getText()));
				isAdded = manager.infoStorage.add(info);
			}

			catch (Exception x)
			{
				text.append("Update Failed, year must be a number.\n");
				return;
			}
		}
		else
		{
			info = new PhoneInfo(name.getText(), phone.getText(), note.getText(), BD.getText(), x);
			isAdded = manager.infoStorage.add(info);
		}

		if(isAdded)
		{
			text.append("Update Completed.\n");
		}
		else
		{
			text.append("Update Failed: info already exist.\n");
		}
	}
}

//Handles the events when a print is queried. It involves the usage of GUI.
class PrintEventHandler implements ActionListener
{
	JTextField printField;
	JTextArea textArea;

	//Constructor.
	public PrintEventHandler(JTextField field, JTextArea area)
	{
		printField = field;
		textArea = area;
	}

	//Action that is performed when the print button is clicked.
	public void actionPerformed(ActionEvent e)
	{
		String filename = printField.getText();
		PhoneBookManager manager = PhoneBookManager.createManagerInst();
		boolean isPrinted = manager.storeToFile(filename);
		if(isPrinted)
			textArea.append("Print Completed.\n");
		else
			textArea.append("Error Printing your data \n");
	}
}

//Handles the events when a delete request is queried. It involves the usage of GUI.
class DeleteEventHandler implements ActionListener
{
	JTextField delField;
	JTextArea textArea;

	//Constructor.
	public DeleteEventHandler(JTextField field, JTextArea area)
	{
		delField = field;
		textArea = area;
	}

	//Action that is taken when the delete button is clicked.
	public void actionPerformed(ActionEvent e)
	{
		String name = delField.getText();
		PhoneBookManager manager = PhoneBookManager.createManagerInst();
		boolean isDeleted = manager.deleteData(name);
		if(isDeleted)
			textArea.append("Remove Completed.\n");
		else
			textArea.append("Remove Failed: info does not exist.\n");
	}
}

//Design and layout of GUI.
class MainFrame extends JFrame
{
	JTextField srchField = new JTextField(15);
	JButton srchBtn = new JButton("SEARCH");

	JButton addBtn = new JButton("ADD");
	JRadioButton rbtn1 = new JRadioButton("General");
	JRadioButton rbtn2 = new JRadioButton("University");
	JRadioButton rbtn3 = new JRadioButton("Company");
	JRadioButton rbtn4 = new JRadioButton("Male");
	JRadioButton rbtn5 = new JRadioButton("Female");
	ButtonGroup buttonGroup = new ButtonGroup();
	ButtonGroup buttonGroup2 = new ButtonGroup();

	JLabel nameLabel = new JLabel("NAME");
	JTextField nameField = new JTextField(15);
	JLabel phoneLabel = new JLabel("PHONE NUMBER");
	JTextField phoneField = new JTextField(15);
	JLabel noteLabel = new JLabel("NOTE");
	JTextField noteField = new JTextField(15);
	JLabel dateLabel = new JLabel("BD");
	JTextField dateField = new JTextField(15);
	JLabel majorLabel = new JLabel("MAJOR");
	JTextField majorField = new JTextField(15);
	JLabel yearLabel = new JLabel("YEAR");
	JTextField yearField = new JTextField(15);
	JTextField printField = new JTextField(15);
	JButton printButton = new JButton("PRINT(.TXT)");
	JTextField delField = new JTextField(15);
	JButton delBtn = new JButton("DEL");

	JTextArea textArea = new JTextArea(10, 25);

	//Creating the GUI.
	public MainFrame(String title)
	{
		super(title);
		setBounds(100, 200, 330, 450);
		setSize(1600,550);
		setLayout(new GridLayout(0,2,0,0));
		Border border = BorderFactory.createEtchedBorder();

		Border srchBorder = BorderFactory.createTitledBorder(border, "Search");
		JPanel srchPanel = new JPanel();
		srchPanel.setBorder(srchBorder);
		srchPanel.setLayout(new FlowLayout());
		srchPanel.add(srchField);
		srchPanel.add(srchBtn);

		// customize color define
		Color c1 = new Color(255,204,0);
		Color c2 = new Color(255, 255, 204);

		// customize search button background
		srchPanel.setBackground(c1);
		ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("search.jpg"));
		srchBtn.setIcon(img);
		srchBtn.setPreferredSize(new Dimension(50,23));

		Border printBorder = BorderFactory.createTitledBorder(border,"Print");
		JPanel printPanel = new JPanel();
		printPanel.setBorder(printBorder);
		printPanel.setLayout(new FlowLayout());
		printPanel.add(printField);
		printPanel.add(printButton);
		printPanel.setBackground(c1); // customize panel color background

		Border addBorder=BorderFactory.createTitledBorder(border, "Add");
		JPanel addPanel = new JPanel();
		addPanel.setBorder(addBorder);
		addPanel.setLayout(new FlowLayout());

		// customize add button background
		addPanel.setBackground(c1);
		ImageIcon img2 = new ImageIcon(ClassLoader.getSystemResource("add.jpg"));
		addBtn.setIcon(img2);
		addBtn.setPreferredSize(new Dimension(50,23));

		// customize font
		nameLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
		noteLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
		majorLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
		yearLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
		phoneLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
		dateLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
		srchField.setFont(new Font("Monospaced", Font.BOLD, 15));
		srchField.setPreferredSize(new Dimension(500,23));
		nameField.setFont(new Font("Monospaced", Font.BOLD, 15));
		noteField.setFont(new Font("Monospaced", Font.BOLD, 15));
		majorField.setFont(new Font("Monospaced", Font.BOLD, 15));
		yearField.setFont(new Font("Monospaced", Font.BOLD, 15));
		phoneField.setFont(new Font("Monospaced", Font.BOLD, 15));
		rbtn1.setFont(new Font("Arial", Font.BOLD, 16));
		rbtn2.setFont(new Font("Arial", Font.BOLD, 16));
		rbtn3.setFont(new Font("Arial", Font.BOLD, 16));
		nameField.setBackground(c2);
		srchField.setBackground(c2);
		noteField.setBackground(c2);
		majorField.setBackground(c2);
		yearField.setBackground(c2);
		phoneField.setBackground(c2);
		delField.setBackground(c2);
		printField.setBackground(c2);
		dateField.setBackground(c2);

		JPanel addInputPanel = new JPanel();
		addInputPanel.setLayout(new GridLayout(0,2,5,5));

		buttonGroup.add(rbtn1);
		buttonGroup.add(rbtn2);
		buttonGroup.add(rbtn3);

		// customize button background
		rbtn1.setBackground(c1);
		rbtn2.setBackground(c1);
		rbtn3.setBackground(c1);
		addInputPanel.setBackground(c1);

		addPanel.add(rbtn1);
		addPanel.add(rbtn2);
		addPanel.add(rbtn3);
		addPanel.add(addBtn);

		addInputPanel.add(nameLabel);
		addInputPanel.add(nameField);
		addInputPanel.add(phoneLabel);
		addInputPanel.add(phoneField);
		addInputPanel.add(noteLabel);
		addInputPanel.add(noteField);
		addInputPanel.add(dateLabel);
		addInputPanel.add(dateField);

		buttonGroup2.add(rbtn4);
		buttonGroup2.add(rbtn5);

		addInputPanel.add(rbtn4);
		addInputPanel.add(rbtn5);

		addInputPanel.add(majorLabel);
		addInputPanel.add(majorField);
		addInputPanel.add(yearLabel);
		addInputPanel.add(yearField);

		majorLabel.setVisible(false);
		majorField.setVisible(false);
		yearLabel.setVisible(false);
		yearField.setVisible(false);

		rbtn1.setSelected(true);
		addPanel.add(addInputPanel);

		rbtn1.addItemListener(
				new ItemListener()
				{
					//For PhoneInfo.
					public void itemStateChanged(ItemEvent e)
					{
						if(e.getStateChange() == ItemEvent.SELECTED)
						{
							majorLabel.setVisible(false);
							majorField.setVisible(false);
							yearLabel.setVisible(false);
							yearField.setVisible(false);
							majorField.setText("");
							yearField.setText("");
						}
					}
				}
		);

		rbtn2.addItemListener(
				new ItemListener()
				{
					//For PhoneUnivInfo.
					public void itemStateChanged(ItemEvent e)
					{
						if(e.getStateChange() == ItemEvent.SELECTED)
						{
							majorLabel.setVisible(true);
							majorLabel.setText("MAJOR");
							majorField.setVisible(true);
							yearLabel.setVisible(true);
							yearField.setVisible(true);
						}
					}
				}
		);

		rbtn3.addItemListener(
				new ItemListener()
				{
					//For PhoneCompanyInfo.
					public void itemStateChanged(ItemEvent e)
					{
						if(e.getStateChange() == ItemEvent.SELECTED)
						{
							majorLabel.setVisible(true);
							majorLabel.setText("COMPANY");
							majorField.setVisible(true);
							yearLabel.setVisible(false);
							yearField.setVisible(false);
							yearField.setText("");
						}
					}
				}
		);

		Border delBorder = BorderFactory.createTitledBorder(border, "Delete");
		JPanel delPanel = new JPanel();
		delPanel.setBorder(delBorder);
		delPanel.setLayout(new FlowLayout());
		delPanel.add(delField);
		delPanel.add(delBtn);
		delPanel.setBackground(c1); // customize delete panel background

		JScrollPane scrollTextArea = new JScrollPane(textArea);
		Border textBorder=BorderFactory.createTitledBorder(border, "Information Board");
		scrollTextArea.setBorder(textBorder);
		// customize information panel background
		scrollTextArea.setBackground(c1);
		textArea.setBackground(c2);

		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new BorderLayout());
		actionPanel.add(srchPanel, BorderLayout.NORTH);
		actionPanel.add(printPanel, BorderLayout.BEFORE_LINE_BEGINS);
		actionPanel.add(addPanel, BorderLayout.CENTER);
		actionPanel.add(delPanel, BorderLayout.SOUTH);

		add(actionPanel);
		add(scrollTextArea);

		srchBtn.addActionListener(new SearchEventHandler(srchField, textArea));
		addBtn.addActionListener(new AddEventHandler(nameField, phoneField, noteField, dateField, rbtn4, majorField, yearField, textArea));
		printButton.addActionListener(new PrintEventHandler(printField, textArea));
		delBtn.addActionListener(new DeleteEventHandler(delField, textArea));

		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}

//Setting up the GUI.
class PhoneBook extends JFrame implements ActionListener
{
	static JLabel l1, l2, l3, l4;
	static JTextField tf;
	static JPasswordField pf;
	static JPanel p1, p2, p3;
	static JButton bt1;
	JButton bt2;
	Font f1, f2;
	PhoneBook()
	{
		super("Login Phonebook");
		setLocation(400,300);
		setSize(530,250);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		f1 = new Font(Font.DIALOG,Font.BOLD,25);
		f2 = new Font("Aharoni",Font.BOLD,15);

		l1 = new JLabel("Welcome to Phonebook");
		l2 = new JLabel("Username");
		l3 = new JLabel("Password");

		l1.setHorizontalAlignment(JLabel.CENTER);
		l1.setFont(f1);
		l2.setFont(f2);
		l3.setFont(f2);

		tf = new JTextField(30);
		pf = new JPasswordField(15);

		tf.setFont(f2);
		pf.setFont(f2);

		bt1 = new JButton("Login");
		bt2 = new JButton("Cancel");
		bt1.addActionListener(this);
		bt2.addActionListener(this);

		bt1.setFont(f2);
		bt2.setFont(f2);
		bt1.setPreferredSize(new Dimension(100, 50));
		bt2.setPreferredSize(new Dimension(100, 50));

		ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("PBremove.PNG"));
		Image img2 = img.getImage().getScaledInstance(250,200,Image.SCALE_DEFAULT);
		ImageIcon img3 = new ImageIcon(img2);
		l4 = new JLabel(img3);

		p1 = new JPanel();
		p1.setLayout(new GridLayout(4,2,10,20));
		JLabel emptyLabel = new JLabel();
		emptyLabel.setPreferredSize(new Dimension(10,2));
		JLabel emptyLabel2 = new JLabel();
		emptyLabel2.setPreferredSize(new Dimension(10,2));
		p1.add(emptyLabel);
		p1.add(emptyLabel2);
		p1.add(l2);
		p1.add(tf);
		p1.add(l3);
		p1.add(pf);
		p1.add(bt1);
		p1.add(bt2);

		p2 = new JPanel();
		p2.setLayout(new GridLayout(1,5,10,20));
		p2.add(l1);

		p3 = new JPanel();
		p3.setLayout(new GridLayout(1,1,10,20));
		p3.add(l4);

		setLayout(new BorderLayout(0,0));

		add(p2,"North");
		add(p3,"East");
		add(p1,"Center");

		// customize color define
		Color c1 = new Color(255, 204, 0);
		Color c2 = new Color(255, 255, 204);
		l1.setFont(new Font("Monospaced", Font.BOLD, 30));
		l2.setFont(new Font("Monospaced", Font.BOLD, 15));
		l3.setFont(new Font("Monospaced", Font.BOLD, 15));
		tf.setBackground(c2);
		pf.setBackground(c2);
		p1.setBackground(c1);
		p2.setBackground(c1);
		p3.setBackground(c1);

	}

	//Main.
	public static void main(String[] args)
	{
		new PhoneBook().setVisible(true);
	}
	String username,password;

	//For login purposes.
	@Override
	public void actionPerformed(ActionEvent e)
	{


		if(e.getSource() == bt1)
		{
			username = tf.getText();
			password = pf.getText();
			if(username.equals("a") && password.equals("1"))
			{
				setVisible(false);
				PhoneBookManager manager = PhoneBookManager.createManagerInst();
				MainFrame winFrame = new MainFrame("Group 18: Phone Book Management System");
			}
			else
				setVisible(true);
		}
		if(e.getSource() == bt2)
			System.exit(0);
	}

}