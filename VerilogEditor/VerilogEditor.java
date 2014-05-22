import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
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

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Locale;

import java.io.*;
import java.nio.*;

public class VerilogEditor implements ActionListener{
	static final int WIDTH = 600;
	static final int HEIGHT = 600;
	MyTextPane codeText = null;
	MyTextPane errorText = null;
	MyUndo1 myUndoManager1 = null;
	/**
	 * @param args
	 */
	
	public VerilogEditor(){
		Locale.setDefault(Locale.ENGLISH);
		JFrame textFrame = new JFrame("Verilog Text Editor");
		textFrame.setSize(WIDTH,HEIGHT);
		textFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set the location the window will appear on the screen
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		int x = (width - WIDTH)/2;
		int y = (height - HEIGHT)/2;
		textFrame.setLocation(x, y);
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		textFrame.setContentPane(contentPane);
		
		// below is the split panel code
		final JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		splitPane.setDividerSize(2);
		splitPane.setPreferredSize(new Dimension(600,550));
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		//make the divider keep its position(in percentage to the whole window) during dragging
		splitPane.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e){
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
		
		//for keywords highlight
		codeText.getDocument().addDocumentListener(new SyntaxHighlighter(codeText));
		
		//for undo and redo
		myUndoManager1 = new MyUndo1();
		codeText.getDocument().addUndoableEditListener(myUndoManager1);
		
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
		splitPane.add(upperArea,JSplitPane.LEFT,1);
		splitPane.add(lowerArea,JSplitPane.RIGHT,2);
		
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
		
		//below is the menu bar code
		//including listener and short cut key
		JMenuBar menubar = new JMenuBar();
		textFrame.setJMenuBar(menubar);
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
		saveMenuItem.addActionListener(this);
		JMenuItem verifyMenuItem = new JMenuItem("Verify");
		verifyMenuItem.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_MASK));
		verifyMenuItem.addActionListener(this);
		JMenuItem uploadMenuItem = new JMenuItem("Upload");
		uploadMenuItem.setAccelerator(KeyStroke.getKeyStroke('U', InputEvent.CTRL_MASK));
		uploadMenuItem.addActionListener(this);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem undoMenuItem = new JMenuItem("Undo");
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_MASK));
		undoMenuItem.addActionListener(this);
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_MASK));
		redoMenuItem.addActionListener(this);
		JMenuItem sarMenuItem = new JMenuItem("Search and replace");
		sarMenuItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_MASK));
		menubar.add(fileMenu);
		menubar.add(editMenu);
		fileMenu.add(verifyMenuItem);
		fileMenu.add(uploadMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);
		editMenu.add(sarMenuItem);
		
		textFrame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new VerilogEditor();
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
                                               String altText) {
		//Look for the image.
		String imgLocation = "images/"
        + imageName
        + ".png";
		URL imageURL = VerilogEditor.class.getResource(imgLocation);
        
		//Create and initialize the button.
		JButton button = new JButton();
		button.setToolTipText(toolTipText);
        
		if (imageURL != null) {                      //image found
			button.setIcon(new ImageIcon(imageURL, altText));
		} else {                                     //no image found
			button.setText(altText);
			System.err.println("Resource not found: "
                               + imgLocation);
		}
        
		return button;
	}
	
	//add tool bar buttons and their listeners
	protected void addButtons(JToolBar toolBar){
		JButton saveButton = makeToolBarButton("save","Save","Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButtonFunction();
			}
		});
        toolBar.add(saveButton);
        JButton verifyButton = makeToolBarButton("verify","Verify","Verify");
        verifyButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		verifyButtonFunction();
        	}
        });
        toolBar.add(verifyButton);
        JButton uploadButton = makeToolBarButton("upload","Upload","Upload");
        uploadButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		uploadButtonFunction();
        	}
        });
        toolBar.add(uploadButton);
        JButton undoButton = makeToolBarButton("undo","Undo","Undo");
        undoButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		undoButtonFunction();
        	}
        });
        toolBar.add(undoButton);
        JButton redoButton = makeToolBarButton("redo","Redo","Redo");
        redoButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		redoButtonFunction();
        	}
        });
        toolBar.add(redoButton);
        JButton searchButton = makeToolBarButton("search","Search and replace","Search and replace");
        searchButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		salButtonFunction();
        	}
        });
        toolBar.add(searchButton);
	}
	
	public void actionPerformed(ActionEvent e){
		action(e);
	}
	
	//distinguish which key is pressed
	public void action(ActionEvent e){
		String str = e.getActionCommand();
		if(str.equals("Save")){
			saveButtonFunction();
		}
		else if(str.equals("Verify")){
			verifyButtonFunction();
		}
		else if(str.equals("Upload")){
			uploadButtonFunction();
		}
		else if(str.equals("Exit")){
			exitButtonFunction();
		}
		else if(str.equals("Undo")){
			undoButtonFunction();
		}
		else if(str.equals("Redo")){
			redoButtonFunction();
		}
		else if(str.equals("Search and replace")){
			salButtonFunction();
		}
	}
	
	//save
	public void saveButtonFunction(){
		StyledDocument doc = errorText.getStyledDocument();
		try{
			FileWriter out = new FileWriter("JEditorPane.txt");
            out.write(codeText.getText());
            out.close();
            doc.insertString(doc.getLength(), "Saving compelte.\n", null);
		}
		catch (Exception e1){
			System.out.println(e1);
		}
	}
	
	//verify
	public void verifyButtonFunction(){
		StyledDocument doc = codeText.getStyledDocument();
		try{
			doc.insertString(doc.getLength(), "verify button pressed\n", null );
		}
		catch (Exception e1){
			System.out.println(e1);
		}
	}
	
	//upload
	public void uploadButtonFunction(){
		StyledDocument doc = codeText.getStyledDocument();
		try{
			doc.insertString(doc.getLength(), "upload button pressed\n", null );
		}
		catch (Exception e1){
			System.out.println(e1);
		}
	}
	
	//exit
	public void exitButtonFunction(){
		
	}
	
	//undo
	public void undoButtonFunction(){
		try {
			myUndoManager1.undo();
        } catch (CannotUndoException e) {
	        Toolkit.getDefaultToolkit().beep();
        }
	}
	
	//redo
	public void redoButtonFunction(){
		try {
	        myUndoManager1.redo();
        } catch (CannotUndoException e) {
	        Toolkit.getDefaultToolkit().beep();
        }
	}
	
	//search and replace
	public void salButtonFunction(){
		
	}
}

//extend undomanager to handle the undo and redo
class MyUndo1 extends UndoManager{
	@Override
	public void undoableEditHappened(UndoableEditEvent e){
		//only if the change is not belongs to style change, it remembered by the stuck
		if(!e.getEdit().getPresentationName().equals("style change")){
			this.addEdit(e.getEdit());
		}
	}
	public void trimEdits(int from, int to){
		super.trimEdits(from,to);
	}
}



//This block of code implements the text highlight
class SyntaxHighlighter implements DocumentListener {
	private Set<String> keywords;
	private Style keywordStyle;
	private Style normalStyle;
    
	public SyntaxHighlighter(JTextPane editor) {
		//prepare the style
		keywordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		StyleConstants.setForeground(keywordStyle, Color.RED);
		StyleConstants.setBold(keywordStyle, true);
		StyleConstants.setForeground(normalStyle, Color.BLACK);
        
		//prepare the keywords
		String[] keywordsSet = new String[]{"always","assign","begin",
            "case","else","endcase","end","for","function","if",
            "input","integer","module","negedge","output","parameter",
            "posedge","real","reg","task","signed","wire","while"};
		keywords = new HashSet<String>();
		for(int i = 0; i < keywordsSet.length; i++){
			keywords.add(keywordsSet[i]);
		}
	}
    
	public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {
		
		int start = indexOfWordStart(doc, pos);
		int end = indexOfWordEnd(doc, pos + len);
        
		char ch;
		while (start < end) {
			ch = getCharAt(doc, start);
			if (Character.isLetter(ch) || ch == '_') {
				start = colouringWord(doc, start);
			} else {
				SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
				++start;
			}
		}
	}
    
	
	public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {
		int wordEnd = indexOfWordEnd(doc, pos);
		String word = doc.getText(pos, wordEnd - pos);
        
		if (keywords.contains(word)) {
			
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, keywordStyle));
		} else {
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));
		}
        
		return wordEnd;
	}
    
	
	public char getCharAt(Document doc, int pos) throws BadLocationException {
		return doc.getText(pos, 1).charAt(0);
	}
    
	
	public int indexOfWordStart(Document doc, int pos) throws BadLocationException {
		//find the first non-character before "pos"
		for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);
        
		return pos;
	}
    
	
	public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {
		//find the fist non-character after "pos"
		for (; isWordCharacter(doc, pos); ++pos);
        
		return pos;
	}
    
	
	public boolean isWordCharacter(Document doc, int pos) throws BadLocationException {
		char ch = getCharAt(doc, pos);
		if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') { return true; }
		return false;
	}
    
	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}
    
	@Override
	public void insertUpdate(DocumentEvent e) {
		try {
			colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
    
	@Override
	public void removeUpdate(DocumentEvent e) {
		try {
			// 因为删除后光标紧接着影响的单词两边, 所以长度就不需要了
			colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
    
	
	private class ColouringTask implements Runnable {
		private StyledDocument doc;
		private Style style;
		private int pos;
		private int len;
        
		public ColouringTask(StyledDocument doc, int pos, int len, Style style) {
			this.doc = doc;
			this.pos = pos;
			this.len = len;
			this.style = style;
		}
        
		public void run() {
			try {
				//here is where the coloring is actually happen
				doc.setCharacterAttributes(pos, len, style, true);
			} catch (Exception e) {}
		}
	}
}

//Pop up menu for cut, paste, and copy
class MyTextPane extends JTextPane implements MouseListener{
	private static final long serialVersionUID = -2308615404205560180L;
	
	private JPopupMenu pop = null; //pop up menu
    
	private JMenuItem copy = null, paste = null, cut = null; //three menu item
    
    public MyTextPane() {
        super();
        init();
    }
    
    private void init() {
        this.addMouseListener(this);
        pop = new JPopupMenu();
        pop.add(copy = new JMenuItem("Copy"));
        pop.add(paste = new JMenuItem("Paste"));
        pop.add(cut = new JMenuItem("Cut"));
        copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
        cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        cut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        this.add(pop);
    }
    
    //Do something according to the menu
    public void action(ActionEvent e) {
        String str = e.getActionCommand();
        if (str.equals(copy.getText())) {
            this.copy();
        } else if (str.equals(paste.getText())) {
            this.paste();
        } else if (str.equals(cut.getText())) {
            this.cut();
        }
    }
    
    public JPopupMenu getPop() {
        return pop;
    }
    
    public void setPop(JPopupMenu pop) {
        this.pop = pop;
    }
    
    //Check if there is anything in the clipboard
    public boolean isClipboardString() {
        boolean b = false;
        Clipboard clipboard = this.getToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(this);
        try {
            if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {
                b = true;
            }
        } catch (Exception e) {
        }
        return b;
    }
    
    //Check if anything is been selected and thus can be copied and cut.
    public boolean isCanCopy() {
        boolean b = false;
        int start = this.getSelectionStart();
        int end = this.getSelectionEnd();
        if (start != end)
            b = true;
        return b;
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            copy.setEnabled(isCanCopy());
            paste.setEnabled(isClipboardString());
            cut.setEnabled(isCanCopy());
            pop.show(this, e.getX(), e.getY());
        }
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
}



/*
 * some code for future use, implementing the open/load function
 * FileReader in = new FileReader("JEditorPane.txt");
 char[] buffer = new char[1024];
 int n = in.read(buffer);
 String text = new String(buffer, 0, n);
 codeText.setText(text);
 in.close();
 */
