/*
The MIT License (MIT)

Copyright (c) 2014 Peter Jamieson, Naoki Mizuno, and Boyu Zhang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Highlighter;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import VerilogSimulator.Parse;

import java.text.ParseException;
import java.util.ArrayList;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Locale;
import java.io.*;
import java.nio.*;

public class VerilogEditor extends JFrame implements ActionListener
{
	static final int 						WIDTH = 800;
	static final int 						HEIGHT = 600;
	static final int 						MINWIDTH = 730;
	static final int 						MINHEIGHT = 500;
	private MyTextPane 						codeText = null;
	private MyTextPane 						errorText = null;
	private MyUndo1 						myUndoManager1 = null;
	private IntegerRangeDocumentFilter 		filterOne;
	static String 							name;
	static String 							pathOfEditorJar;
	static String 							rootPath;
	static String 							level_number;
	public File 							verilogFiles;
	private AnimationPanel 					animationPanel;
	JFormattedTextField 					simulateInput, generalSensorInput1, 
											generalSensorInput2, generalSensorInput3,
											generalSensorInput4, generalSensorInput5,
											generalSensorInput6, generalSensorInput0;
	private String 							newLine;
	private Parse 							Compiler;
	
	private long							startTime;
	private long							totalFocusTime;
	/**
	* @param args
	*/
	

	/* Constants from LocalServer class in verilogTown project */
	public static final String			LOCAL_IP_ADDRESS	= "127.0.0.1";
	public static final int				LOCAL_PORT			= 32151;
	public static final int				TYPE_USAGE_EDITOR	= 3;
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		name = args[0];
		//pathOfEditorJar = args[1];
		rootPath = args[1];
		level_number = args[2];
		new VerilogEditor();
	}
	
	public VerilogEditor()
	{
		super("Verilog Text Editor: " + name);
		Locale.setDefault(Locale.ENGLISH);
		
		if(System.getProperty("os.name").startsWith("Mac"))
			newLine = "\n";
		else
			newLine = "\n";
		
		//File verilogDir = new File(pathOfEditorJar + "VerilogFiles");
		File verilogDir = new File(rootPath + "Levels/" + "Lv" + level_number + "/" + "VerilogFiles/");
		if  (!verilogDir.exists()  && !verilogDir.isDirectory())
		
		{
			System.out.println("Directory does not exist.");

			if(verilogDir.mkdir())
				System.out.println("Directory has been created.");
			else
				System.out.println("Fail to create a directory.");
			
		} 
		else
		{
			System.out.println("Directory is already exist.");
		}
		
		this.setSize(WIDTH,HEIGHT);
		this.setMinimumSize(new Dimension(MINWIDTH,MINHEIGHT));
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		
		//set the location the window will appear on the screen
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		int x = (width - WIDTH)/2;
		int y = (height - HEIGHT)/2;
		this.setLocation(x, y);
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		this.setContentPane(contentPane);
		
		//below is the tool bar code
		JToolBar toolBar = new JToolBar("Still draggable");
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		GridBagConstraints cToolBar = new GridBagConstraints();
		cToolBar.gridx = 0;
		cToolBar.gridy = 0;
		cToolBar.fill = GridBagConstraints.BOTH;
		cToolBar.weightx = 0;
		cToolBar.weighty = 0;
		contentPane.add(toolBar,cToolBar); 
		addButtons(toolBar);
		toolBar.setBorder(BorderFactory.createEtchedBorder());
		
		// below is the split panel code
		final JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		splitPane.setDividerSize(2);
		splitPane.setPreferredSize(new Dimension(600,550));
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		//make the divider keep its position(in percentage to the whole window) during dragging
		splitPane.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				splitPane.setDividerLocation(0.7);
			}
		});
		
		//grid bag constraints for splitpane
		GridBagConstraints cSplitPane = new GridBagConstraints();
		cSplitPane.gridx = 0;
		cSplitPane.gridy = 1;
		cSplitPane.fill = GridBagConstraints.BOTH;
		cSplitPane.weightx = 1;
		cSplitPane.weighty = 1;
		contentPane.add(splitPane,cSplitPane);
		
		codeText = new MyTextPane();
		setTabs(codeText,8);
		Font font1 = new Font("Consolas",Font.PLAIN,16);
		codeText.setFont(font1);
		//line number
		codeText.setBorder(new LineNumberBorder());
		
		//non-editable text panel
		filterOne = new IntegerRangeDocumentFilter(codeText);
	    ((AbstractDocument)codeText.getDocument()).setDocumentFilter(filterOne);
		//for keywords highlight
		codeText.getDocument().addDocumentListener(new SyntaxHighlighter(codeText));
		//something strange with the JTextPane's new line character.
		//For more information see here: 
		//http://docs.oracle.com/javase/7/docs/api/javax/swing/text/DefaultEditorKit.html
		codeText.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, newLine);
		//for undo and redo
		myUndoManager1 = new MyUndo1();
		codeText.getDocument().addUndoableEditListener(myUndoManager1);
		
		//read in the already existed file or create a new file
		//verilogFiles = new File(pathOfEditorJar + "VerilogFiles/" + name + ".v");
		verilogFiles = new File(rootPath + "Levels/" + "Lv" + level_number + "/" + "VerilogFiles/" + name + ".v");
		if(!verilogFiles.exists())
		{
			try
			{
				verilogFiles.createNewFile();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		{
			try
			{
				InputStreamReader reader = new InputStreamReader(new FileInputStream(verilogFiles));
				BufferedReader br = new BufferedReader(reader);
				String line = "";
				String temp = null;
				if((temp = br.readLine()) != null)
					line = temp;
				while ((temp = br.readLine()) != null)
					line = line + newLine + temp;
				Document docCode = codeText.getDocument();
				docCode.insertString(0, line, null);
				myUndoManager1.discardAllEdits();
				//non-editable text panel
				if(codeText.getText().indexOf("count") == -1)
					filterOne.setStart(940);
				else
					filterOne.setStart(962);
				
				br.close();
				reader.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
		
		errorText = new MyTextPane();
		errorText.setEditable(false);
		Font font2 = new Font("Consolas",Font.PLAIN,12);
		errorText.setFont(font2);
		
		
		JScrollPane upperArea = new JScrollPane(codeText,
		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel lowerArea = new JPanel();
		lowerArea.setLayout(new GridBagLayout());
		
		JScrollPane errorArea = new JScrollPane(errorText,
		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JLabel errorLog = new JLabel("Error log");
		
		animationPanel = new AnimationPanel(simulateInput);
		
		
		this.setVisible(true);
		
		//grid bag constraints for errorlog and errorarea
		GridBagConstraints cErrorLog = new GridBagConstraints();
		cErrorLog.gridx = 0;
		cErrorLog.gridy = 0;
		cErrorLog.fill = GridBagConstraints.BOTH;
		cErrorLog.weightx = 0;
		cErrorLog.weighty = 0;
		lowerArea.add(errorLog,cErrorLog);
		
		GridBagConstraints cErrorArea = new GridBagConstraints();
		cErrorArea.gridx = 0;
		cErrorArea.gridy = 1;
		cErrorArea.fill = GridBagConstraints.BOTH;
		cErrorArea.weightx = 1;
		cErrorArea.weighty = 1;
		lowerArea.add(errorArea,cErrorArea);
		
		GridBagConstraints cAnimationPanel = new GridBagConstraints();
		cAnimationPanel.gridx = 2;
		cAnimationPanel.gridy = 1;
		cAnimationPanel.weightx = 0.1;
		cAnimationPanel.weighty = 1;
		lowerArea.add(animationPanel,cAnimationPanel);
		
		splitPane.add(upperArea,JSplitPane.LEFT,1);
		splitPane.add(lowerArea,JSplitPane.RIGHT,2);
		
		//below is the menu bar code
		//including listener and short cut key
		JMenuBar menubar = new JMenuBar();
		this.setJMenuBar(menubar);
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu simulationMenu = new JMenu("Simulation");
		JMenu headerMenu = new JMenu("Template");
		
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
		saveMenuItem.addActionListener(this);
		
		JMenuItem verifyMenuItem = new JMenuItem("Verify");
		verifyMenuItem.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_MASK));
		verifyMenuItem.addActionListener(this);
		/*
		JMenuItem uploadMenuItem = new JMenuItem("Upload");
		uploadMenuItem.setAccelerator(KeyStroke.getKeyStroke('U', InputEvent.CTRL_MASK));
		uploadMenuItem.addActionListener(this);
		*/
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.CTRL_MASK));
		exitMenuItem.addActionListener(this);
		
		JMenuItem undoMenuItem = new JMenuItem("Undo");
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_MASK));
		undoMenuItem.addActionListener(this);
		
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_MASK));
		redoMenuItem.addActionListener(this);
		
		JMenuItem sarMenuItem = new JMenuItem("Search and Replace");
		sarMenuItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_MASK));
		sarMenuItem.addActionListener(this);
		
		JMenuItem simulateMenuItem = new JMenuItem("Simulate");
		simulateMenuItem.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_MASK));
		simulateMenuItem.addActionListener(this);
		
		JMenuItem resetMenuItem = new JMenuItem("Reset Simulation");
		resetMenuItem.setAccelerator(KeyStroke.getKeyStroke('R',InputEvent.CTRL_MASK));
		resetMenuItem.addActionListener(this);
		
		JMenuItem comboHeaderMenuItem = new JMenuItem("Combinational");
		comboHeaderMenuItem.setAccelerator(KeyStroke.getKeyStroke('1',InputEvent.CTRL_MASK));
		comboHeaderMenuItem.addActionListener(this);
		
		JMenuItem seqHeaderMenuItem = new JMenuItem("Sequential");
		seqHeaderMenuItem.setAccelerator(KeyStroke.getKeyStroke('2',InputEvent.CTRL_MASK));
		seqHeaderMenuItem.addActionListener(this);
		
		menubar.add(fileMenu);
		menubar.add(editMenu);
		menubar.add(simulationMenu);
		menubar.add(headerMenu);
		fileMenu.add(verifyMenuItem);
		//fileMenu.add(uploadMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);
		editMenu.add(sarMenuItem);
		simulationMenu.add(simulateMenuItem);
		simulationMenu.add(resetMenuItem);
		headerMenu.add(comboHeaderMenuItem);
		headerMenu.add(seqHeaderMenuItem);
		
		/* Initialize the Parser */
		Compiler = new Parse(errorText);
		
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				//get the current text in the code area.
				String fileContent = "";
				try
				{
					InputStreamReader reader = new InputStreamReader(new FileInputStream(verilogFiles));
					BufferedReader br = new BufferedReader(reader);
					String temp = null;
					if((temp = br.readLine()) != null)
						fileContent = temp;
					while ((temp = br.readLine()) != null){
							fileContent = fileContent + newLine + temp;
					}
					br.close();
					reader.close();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
				
				
				//this block of code is for debug
				/*
				System.out.println("code text: ");
				for(int i = 0; i < codeText.getText().toCharArray().length; i++){
					if(codeText.getText().toCharArray()[i] == 0xA)
						System.out.print("N");
					else if(codeText.getText().toCharArray()[i] == 0xD)
						System.out.print("R");
					else
						System.out.print("a");
					System.out.print((int)codeText.getText().toCharArray()[i] + "\t");
				}
				System.out.println();
				System.out.println("file content: ");
				for(int i = 0; i < fileContent.toCharArray().length; i++){
					if(fileContent.toCharArray()[i] == 0xA)
						System.out.print("N");
					else if(fileContent.toCharArray()[i] == 0xD)
						System.out.print("R");
					else
						System.out.print("a");
					System.out.print((int)fileContent.toCharArray()[i] + "\t");
				}
				System.out.println();
				*/
				
				if (codeText.getText().equals(fileContent))
				{
					totalFocusTime += (System.currentTimeMillis() - startTime) / 1000;
					errorText.setText("" + totalFocusTime);
					sendEditorTime(totalFocusTime);
					dispose();
				}
				else
					closingPopFunction();
			}
		});
		
		this.addWindowFocusListener(new WindowFocusListener(){

			@Override
			public void windowGainedFocus(WindowEvent e)
			{
				startTime = System.currentTimeMillis();
			}

			@Override
			public void windowLostFocus(WindowEvent e)
			{
				totalFocusTime += (System.currentTimeMillis() - startTime) / 1000;
			}
			
		});
	}
	
	//This block of code set how many space you get when you press the "tab"
	public static void setTabs( JTextPane textPane, int charactersPerTab)
	
	{
		FontMetrics fm = textPane.getFontMetrics( textPane.getFont() );
		int charWidth = fm.charWidth( ' ' );
		int tabWidth = charWidth * charactersPerTab;
		
		TabStop[] tabs = new TabStop[50];
		
		for (int j = 0; j < tabs.length; j++)
		
		{
			int tab = j + 1;
			tabs[j] = new TabStop( tab * tabWidth );
		}
		
		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabSet);
		int length = textPane.getDocument().getLength();
		textPane.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
	}
	
	//This block of code create a button with image
	protected static JButton makeToolBarButton(String imageName,
	String toolTipText,
	String altText)
	{
		//Look for the image.
		String imgLocation = "images/"
		+ imageName
		+ ".png";
		URL imageURL = VerilogEditor.class.getResource(imgLocation);
		
		//Create and initialize the button.
		JButton button = new JButton();
		button.setToolTipText(toolTipText);
		
		if (imageURL != null)
		{					  //image found
			button.setIcon(new ImageIcon(imageURL, altText));
		} 
		else
		{									 //no image found
			button.setText(altText);
			System.err.println("Resource not found: "
			+ imgLocation);
		}
		
		return button;
	}
	
	//add tool bar buttons and their listeners
	protected void addButtons(JToolBar toolBar)
	{
		JButton saveButton = makeToolBarButton("save","Save","Save");
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveButtonFunction();
			}
		});
		toolBar.add(saveButton);
		JButton verifyButton = makeToolBarButton("verify","Verify","Verify");
		verifyButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				verifyButtonFunction();
			}
		});
		toolBar.add(verifyButton);
		/*
		JButton uploadButton = makeToolBarButton("upload","Upload","Upload");
		uploadButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				uploadButtonFunction();
			}
		});
		toolBar.add(uploadButton);
		*/
		JButton undoButton = makeToolBarButton("undo","Undo","Undo");
		undoButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				undoButtonFunction();
			}
		});
		toolBar.add(undoButton);
		JButton redoButton = makeToolBarButton("redo","Redo","Redo");
		redoButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				redoButtonFunction();
			}
		});
		toolBar.add(redoButton);
		JButton searchButton = makeToolBarButton("search","Search and replace","Search and replace");
		searchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				salButtonFunction();
			}
		});
		toolBar.add(searchButton);
		
		JButton simulateButton = makeToolBarButton("simulate", "Simulate", "Simulate");
		simulateButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e){
				simulateButtonFunction();
			}
		});
		toolBar.add(simulateButton);
		
		JButton resetButton = makeToolBarButton("reset","Reset simulation","Reset simulation");
		resetButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				resetButtonFunction();
			}
		});
		toolBar.add(resetButton);
		
		toolBar.add(new JLabel("Internal Sensors: "));
		MaskFormatter formatterInternal = null;
		try {
			formatterInternal = new MaskFormatter("########");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		formatterInternal.setValidCharacters("10");
		formatterInternal.setPlaceholderCharacter('0');
		simulateInput = new JFormattedTextField(formatterInternal);
		simulateInput.setColumns(8);
		simulateInput.addCaretListener(new CaretListener(){
			@Override
			public void caretUpdate(CaretEvent e){
				String tempStr = "";
				tempStr = simulateInput.getText();
				if(tempStr.length() != 8){
					tempStr = animationPanel.getInternalSignal();
				}
				animationPanel.setStates(tempStr);
			}
		});
		toolBar.add(simulateInput);
		
		toolBar.add(new JLabel("General Sensors(6~0): "));
		MaskFormatter formatterGeneral = null;
		try{
			formatterGeneral = new MaskFormatter("####");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		formatterGeneral.setValidCharacters("10");
		formatterGeneral.setPlaceholderCharacter('0');
		generalSensorInput0 = new JFormattedTextField(formatterGeneral);
		generalSensorInput1 = new JFormattedTextField(formatterGeneral);
		generalSensorInput2 = new JFormattedTextField(formatterGeneral);
		generalSensorInput3 = new JFormattedTextField(formatterGeneral);
		generalSensorInput4 = new JFormattedTextField(formatterGeneral);
		generalSensorInput5 = new JFormattedTextField(formatterGeneral);
		generalSensorInput6 = new JFormattedTextField(formatterGeneral);
		
		generalSensorInput0.setColumns(4);
		generalSensorInput1.setColumns(4);
		generalSensorInput2.setColumns(4);
		generalSensorInput3.setColumns(4);
		generalSensorInput4.setColumns(4);
		generalSensorInput5.setColumns(4);
		generalSensorInput6.setColumns(4);
		
		toolBar.add(generalSensorInput6);
		toolBar.add(generalSensorInput5);
		toolBar.add(generalSensorInput4);
		toolBar.add(generalSensorInput3);
		toolBar.add(generalSensorInput2);
		toolBar.add(generalSensorInput1);
		toolBar.add(generalSensorInput0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		action(e);
	}
	
	//distinguish which key is pressed
	public void action(ActionEvent e)
	{
		String str = e.getActionCommand();
		if(str.equals("Save"))
		{
			saveButtonFunction();
		}
		else if(str.equals("Verify"))
		{
			verifyButtonFunction();
		}
		else if(str.equals("Upload"))
		{
			uploadButtonFunction();
		}
		else if(str.equals("Exit"))
		{
			exitButtonFunction();
		}
		else if(str.equals("Undo"))
		{
			undoButtonFunction();
		}
		else if(str.equals("Redo"))
		{
			redoButtonFunction();
		}
		else if(str.equals("Search and Replace"))
		{
			salButtonFunction();
		}
		else if(str.equals("Simulate"))
		{
			simulateButtonFunction();
		}
		else if(str.equals("Reset Simulation")){
			resetButtonFunction();
		}
		else if(str.equals("Combinational")){
			comboHeaderButtonFunction();
		}
		else if(str.equals("Sequential")){
			seqHeaderButtonFunction();
		}
	}
	
	//save
	public void saveButtonFunction()
	{
		StyledDocument doc = errorText.getStyledDocument();
		
		try
		{
			FileWriter out = new FileWriter(verilogFiles);
			out.write(codeText.getText() + newLine);
			out.close();
			errorText.setText("Saving complete.");
		}
		catch (Exception e1)
		{
			System.out.println(e1);
		}
	}
	
	//verify
	public void verifyButtonFunction()
	{
		StyledDocument doc = codeText.getStyledDocument();
		try
		{
			FileWriter out = new FileWriter(verilogFiles);
			out.write(codeText.getText());
			out.close();
			
			/* print out what we're compiling */
			errorText.setText("Compiling "+rootPath + "Levels/" + "Lv" + level_number + "/" + "VerilogFiles/" + name + ".v");

			/* parse the base file */
			Compiler.compileFileForEditor(rootPath + "Levels/" + "Lv" + level_number + "/" + "VerilogFiles/" + name + ".v");

			if (Compiler.is_compiled_yet())
			{
				/* Reset the system - takes a double simulation */
				Compiler.sim_cycle("0", "00000000", "000000000000000000000000000000");
				Compiler.sim_cycle("1", "00000000", "000000000000000000000000000000");
				errorText.setText(errorText.getText() + "\nCompiling done!");
				animationPanel.setSimulationResults("00000");
				animationPanel.drawAnimation(animationPanel.getGraphics());
			}
		}
		catch (Exception e1)
		{
			System.out.println(e1);
		}
	}
	
	//upload
	public void uploadButtonFunction()
	{
		StyledDocument doc = codeText.getStyledDocument();
		try
		{
			doc.insertString(doc.getLength(), "upload button pressed\n", null );
		}
		catch (Exception e1)
		{
			System.out.println(e1);
		}
	}
	
	//exit
	public void exitButtonFunction()
	{
		String fileContent = "";
		try
		{
			InputStreamReader reader = new InputStreamReader(new FileInputStream(verilogFiles));
			BufferedReader br = new BufferedReader(reader);
			String temp = null;
			if((temp = br.readLine()) != null)
				fileContent = temp;
			while ((temp = br.readLine()) != null){
					fileContent = fileContent + newLine + temp;
			}
			br.close();
			reader.close();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

		if (codeText.getText().equals(fileContent))
		{
			totalFocusTime += (System.currentTimeMillis() - startTime) / 1000;
			errorText.setText("" + totalFocusTime);
			sendEditorTime(totalFocusTime);
			dispose();
		}
		else
			closingPopFunction();
	}
	
	//undo
	public void undoButtonFunction()
	{
		try
		{
			if(myUndoManager1.canUndo())
				myUndoManager1.undo();
		} catch (CannotUndoException e)
		{
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	//redo
	public void redoButtonFunction()
	{
		try
		{
			if(myUndoManager1.canRedo())
				myUndoManager1.redo();
		} catch (CannotUndoException e)
		{
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	//search and replace
	public void salButtonFunction()
	{
		new SearchAndReplaceDialog(this, codeText);
	}
	
	public void simulateButtonFunction()
	{
		//add the simulate code here
		String simulateStr = simulateInput.getText();
		String generalSensorStr = "00" + generalSensorInput6.getText() + generalSensorInput5.getText() + generalSensorInput4.getText()
								+ generalSensorInput3.getText() + generalSensorInput2.getText() + generalSensorInput1.getText()
								+ generalSensorInput0.getText();

		if (simulateStr.length() == 8 && generalSensorStr.length() == 30)
		{
			if (Compiler.is_compiled_yet())
			{
				ArrayList<Integer> output_vector_list;

				/* first sim is for the clock cycle */
				output_vector_list = Compiler.sim_cycle("1", simulateStr, generalSensorStr);
				/* first sim is for the combinational propagation */
				output_vector_list = Compiler.sim_cycle("1", simulateStr, generalSensorStr);
			
				errorText.setText("Simulation Cycle\n" + "Clock Cycle:"+output_vector_list.get(5)+ " Sensors Light: " + simulateStr+" General Sensors: " + generalSensorStr + "\nOutN Val = "+output_vector_list.get(0) + "\nOutS Val = "+output_vector_list.get(1) +"\nOutE Val = "+output_vector_list.get(2) +"\nOutW Val = "+output_vector_list.get(3)+"\nDebugVector = "+Integer.toBinaryString(output_vector_list.get(4)));
				
				animationPanel.setSimulationResults("1" + output_vector_list.get(0) + output_vector_list.get(1) + output_vector_list.get(2) + output_vector_list.get(3));
				animationPanel.drawAnimation(animationPanel.getGraphics());
			}
			else
			{
				errorText.setText("The Verilog code has not been successfully compiled yet.  Please click the check mark above and/or fix Verilog errors.");
			}
		}
		else
		{
			errorText.setText("Simulation cycle not sucessful\nMissing Simulation vector or it vector isn't 10 characters (Hexidecimal digits) long.");
		}
	}
	
	public void resetButtonFunction(){
		//put the reset simualtion code at here
		StyledDocument doc = codeText.getStyledDocument();
		try
		{
			FileWriter out = new FileWriter(verilogFiles);
			out.write(codeText.getText());
			out.close();
			
			/* print out what we're compiling */
			errorText.setText("Compiling "+rootPath + "Levels/" + "Lv" + level_number + "/" + "VerilogFiles/" + name + ".v");

			/* parse the base file */
			Compiler.compileFileForEditor(rootPath + "Levels/" + "Lv" + level_number + "/" + "VerilogFiles/" + name + ".v");

			if (Compiler.is_compiled_yet())
			{
				/* Reset the system - takes a double simulation */
				Compiler.sim_cycle("0", "00000000", "000000000000000000000000000000");
				Compiler.sim_cycle("1", "00000000", "000000000000000000000000000000");
				errorText.setText(errorText.getText() + "\nCompiling done!");
				animationPanel.setSimulationResults("00000");
				animationPanel.drawAnimation(animationPanel.getGraphics());
			}
		}
		catch (Exception e1)
		{
			System.out.println(e1);
		}
	}
	
	public void comboHeaderButtonFunction(){
		codeText.setText(readHeaderFile("header/stop_light_combo.v"));
		filterOne.setStart(940);
	}
	
	public void seqHeaderButtonFunction(){
		codeText.setText(readHeaderFile("header/stop_light_seq.v"));
		filterOne.setStart(962);
	}
	
	public void closingPopFunction(){
		String[] str = {"Content changed.", "Do you want to save this file?"};
		int selection = JOptionPane.showConfirmDialog(this, str, 
													"Save this file?", JOptionPane.YES_NO_CANCEL_OPTION);
		switch (selection)
		{
			case JOptionPane.YES_OPTION:
				saveButtonFunction();
			case JOptionPane.NO_OPTION:
				totalFocusTime += (System.currentTimeMillis() - startTime) / 1000;
				errorText.setText("" + totalFocusTime);
				sendEditorTime(totalFocusTime);
				dispose();
			break;
			case JOptionPane.CANCEL_OPTION:
				return;
		}
	}
	
	public String readHeaderFile(String fileName){
		String headerContent = "";
		try
		{
			InputStream reader = this.getClass().getResourceAsStream("/" + fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(reader));
			String temp = null;
			headerContent = br.readLine();
			while ((temp = br.readLine()) != null){
				if(System.getProperty("os.name").startsWith("Mac"))
					headerContent = headerContent + "\r" + temp;
				else
					headerContent = headerContent + "\n" + temp;
			}
			br.close();
			reader.close();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return headerContent;
	}

	public void sendEditorTime(long editorTime)
	{
		try
		{
			Socket socket = new Socket(InetAddress.getByName(LOCAL_IP_ADDRESS), LOCAL_PORT);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

			dos.writeInt(TYPE_USAGE_EDITOR);
			dos.writeLong(totalFocusTime);
			dos.flush();

			dos.close();
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			String mes = "Error communicating with local server";
			JOptionPane.showMessageDialog(null, mes, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
