//A lot of imports
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.speech.Central; 
import javax.speech.Engine;
import javax.speech.synthesis.Synthesizer; 
import javax.speech.synthesis.SynthesizerModeDesc; 
import javax.speech.synthesis.SynthesizerProperties; 
import javax.speech.synthesis.Voice; 

/**
 * 
 * @author Dion de Jong
 * @version November 24, 2013
 * This program will take in a recipe for chocolate chip
 * cookies and read the instructions to the user at their command.
 * The user can press a button for previous current or next instruction,
 * and listen to the computer speak these instructions. 
 */


//main class
public class RecipeReader extends JFrame implements ActionListener{

	//initialize all of the main things you will need for the panel
	private JButton PrevInstruct;
	private JButton CurInstruct;
	private JButton NextInstruct;
	private JButton Exit;
	private JFormattedTextField Instruct;

	public static String[] InstructionsList = new String[25];
	int i = 0; 

	private static String input;
	private static Synthesizer synthesizer;
	private static Voice voice;
	
	//constructor, creates the basic container and uses the initialize method. 
	public RecipeReader()
	{
		super("Recipe Reader");
		this.setSize(600,400);
		this.initialize();
		this.setVisible(true); 	
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	//the method that creates the array that takes the file and takes each line of string and adds to an array with a max length of 25. 
	public static void MakeArray() throws IOException
	{
		//read a file called CookieRecipe.txt
		FileReader fileReader = new FileReader("CookieRecipe.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		//for loop that adds each line to the array
		for (int j = 0; j < InstructionsList.length; j++)
		if((bufferedReader.readLine()) != null)
		{
			InstructionsList[j] = (bufferedReader.readLine());
		}
		bufferedReader.close();
		
	}

	//initialize method. This will create all the buttons and panels necessary for the graphics area of this program. 
	public void initialize()
	{
		//Create the content pane that will hold everything.
		Container c = this.getContentPane(); 
		c.setLayout(null);
		c.setBackground(Color.blue);
		c.setForeground(new Color(0,0,0));
		c.setVisible(true);

		//create the panel that holds the buttons, it sits inside your content pane. 
		JPanel ButtonPanel = new JPanel(); 
		ButtonPanel.setLayout(new FlowLayout()); 
		ButtonPanel.setVisible(true); 
		ButtonPanel.setBackground(Color.gray);
		c.add(ButtonPanel); 

		//create the separate buttons
		this.PrevInstruct = new JButton();
		this.PrevInstruct.setBounds(10, 250, 150, 50);
		this.PrevInstruct.setActionCommand("PrevInstruct");
		this.PrevInstruct.addActionListener(this);
		this.PrevInstruct.setText("Previous Instruction");
		c.add(PrevInstruct); 

		this.CurInstruct = new JButton();
		this.CurInstruct.setBounds(160, 250, 150, 50);
		this.CurInstruct.setActionCommand("CurInstruct");
		this.CurInstruct.addActionListener(this);
		this.CurInstruct.setText("Current Instruction");
		c.add(CurInstruct); 

		this.NextInstruct = new JButton();
		this.NextInstruct.setBounds(310, 250, 150, 50);
		this.NextInstruct.setActionCommand("NextInstruct");
		this.NextInstruct.addActionListener(this);
		this.NextInstruct.setText("Next Instruction");
		c.add(NextInstruct); 

		this.Exit = new JButton();
		this.Exit.setBounds(460, 250, 100, 50);
		this.Exit.setActionCommand("Exit");
		this.Exit.addActionListener(this);
		this.Exit.setText("Exit");
		c.add(Exit); 

		//displays the current instruction. CANT BE EDITED
		this.Instruct = new JFormattedTextField();
		this.Instruct.setColumns(1);
		this.Instruct.setBounds(100, 50, 400, 35);
		this.Instruct.setText("");
		this.Instruct.setVisible(true);
		this.Instruct.setEditable(false);
		c.add(Instruct); 
	}

	//what happens when the mouse clicks something is determined by this method. 
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		//a variable source holds the thing that is pressed. 
		String source = e.getActionCommand();	

		try 
		{
			//create the voice thingy that speaks. 
			String synthesizerName = System.getProperty
					("synthesizerName", "Unlimited domain FreeTTS Speech Synthesizer from Sun Labs");
			// Create a new SynthesizerModeDesc that will match the FreeTTS
			// Synthesizer.
			SynthesizerModeDesc desc = new SynthesizerModeDesc(
					null,          // engine name
					"general",     // mode name
					Locale.US,     // locale
					null,          // running
					null);         // voice
			synthesizer = Central.createSynthesizer(desc);

			// create the voice
			String voiceName = System.getProperty("voiceName", "kevin16");
			voice = new Voice
					(voiceName, Voice.GENDER_DONT_CARE, Voice.AGE_DONT_CARE, null);

			// get it ready to speak
			synthesizer.allocate();
			synthesizer.resume();
			synthesizer.getSynthesizerProperties().setVoice(voice);


			//if the previous instruction button is pressed, this is what happens.
			if(source.equals("PrevInstruct"))
			{
				//if i is 0, there is no previous point in the array
				if (i == 0)
				{
					JOptionPane.showMessageDialog(null, "There is no previous instruction");
				}
				//otherwise move back one space in the array and speak that instruction
				else 
				{
					i--; 
					synthesizer.speakPlainText(InstructionsList[i], null);
				}
			}

			//if the current instruction button is pressed, this is what happens.
			if(source.equals("CurInstruct"))
			{
				//say the current instruction 
				synthesizer.speakPlainText(InstructionsList[i], null);
			}

			//if the next instruction button is pressed, this is what happens.
			if(source.equals("NextInstruct"))
			{
				i++; 
				//System.out.print(i); 
				//if this is the last line in the array, there are no more instructions. 
				if ( InstructionsList[i] == null)
				{
					JOptionPane.showMessageDialog(null, "There are no further instructions");
				}
				else 
				{
					synthesizer.speakPlainText(InstructionsList[i], null);
				}
			}

			//close if the exit button is pressed
			if(source.equals("Exit"))
			{
				System.exit(0);
			}

			//set the text field to the current value of the array
			this.Instruct.setText(InstructionsList[i]);
		}
		//catch some sort of exception that I'm not sure about. 
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}



	//main method
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		MakeArray(); 
		//create an object of the calculator to be use. 
		RecipeReader Cookie = new RecipeReader(); 
	}
}

//for hw 10, find item_price in the code of walmart
//unit_price for barnes and noble 
//new BufferedReader(Input StreamReader)(InputStream)