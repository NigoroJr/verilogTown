import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class VerilogEditor {
	static final int WIDTH = 600;
	static final int HEIGHT = 600;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame textFrame = new JFrame("Verilog Text Editor");
		textFrame.setSize(WIDTH,HEIGHT);
		textFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		//below is the menu bar code
		JMenuBar menubar = new JMenuBar();
		textFrame.setJMenuBar(menubar);
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		JMenuItem verifyMenuItem = new JMenuItem("Verify");
		JMenuItem uploadMenuItem = new JMenuItem("Upload");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem undoMenuItem = new JMenuItem("Undo");
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		JMenuItem sarMenuItem = new JMenuItem("Search and replace");
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
		
		// below is the split panel code
		final JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		splitPane.setDividerSize(2);
		splitPane.setPreferredSize(new Dimension(600,550));
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e){
				splitPane.setDividerLocation(0.7);
			}
		});
		GridBagConstraints cSplitPane = new GridBagConstraints();
		cSplitPane.gridx = 0;
		cSplitPane.gridy = 1;
		cSplitPane.fill = GridBagConstraints.BOTH;
		cSplitPane.weightx = 1;
		cSplitPane.weighty = 1;
		contentPane.add(splitPane,cSplitPane);
		
		JTextPane codeText = new JTextPane();
		setTabs(codeText,8);
		Font font = new Font("Consolas",Font.PLAIN,16);
		codeText.setFont(font);
		codeText.getDocument().addDocumentListener(new SyntaxHighlighter(codeText));
		JTextPane errorText = new JTextPane();
		errorText.setEditable(false);
		
		JScrollPane upperArea = new JScrollPane(codeText,
												ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
												ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel lowerArea = new JPanel();
		lowerArea.setLayout(new GridBagLayout());
		JScrollPane errorArea = new JScrollPane(errorText,
												ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
												ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JLabel errorLog = new JLabel("Error log");
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
		
		textFrame.setVisible(true);
		
	}
	
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
	
	protected static void addButtons(JToolBar toolBar){
		JButton saveButton = makeToolBarButton("save","Save","Save");
        toolBar.add(saveButton);
        JButton verifyButton = makeToolBarButton("verify","Verify","Verify");
        toolBar.add(verifyButton);
        JButton uploadButton = makeToolBarButton("upload","Upload","Upload");
        toolBar.add(uploadButton);
        JButton searchButton = makeToolBarButton("search","Search and replace","Search and replace");
        toolBar.add(searchButton);   
	}
}

class SyntaxHighlighter implements DocumentListener {
	private Set<String> keywords;
	private Style keywordStyle;
	private Style normalStyle;

	public SyntaxHighlighter(JTextPane editor) {
		// 准备着色使用的样式
		keywordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		StyleConstants.setForeground(keywordStyle, Color.RED);
		StyleConstants.setBold(keywordStyle, true);
		StyleConstants.setForeground(normalStyle, Color.BLACK);

		// 准备关键字
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
		// 从pos开始向前找到第一个非单词字符.
		for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);

		return pos;
	}

	
	public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {
		// 从pos开始向前找到第一个非单词字符.
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
				// 这里就是对字符进行着色
				doc.setCharacterAttributes(pos, len, style, true);
			} catch (Exception e) {}
		}
	}
}
