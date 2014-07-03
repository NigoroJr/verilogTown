import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class SearchAndReplaceDialog extends JDialog
{
	private static int WIDTH = 550;
	private static int HEIGHT = 160;
	private JTextPane codeTextTemp;
	private JLabel search = new JLabel("Search: ");
	private JLabel replace = new JLabel("Replace: ");
	private JButton find = new JButton("Search");
	private JButton rAndS = new JButton("Replace & Search");
	private JButton next = new JButton("Next");
	private JButton replaceAll = new JButton("Replace All");
	private JButton replaceButton = new JButton("Replace");
	private JCheckBox caseSensitive = new JCheckBox("<html>case<br>sensitive");
	private Highlighter hilit;
	private Highlighter.HighlightPainter painter;
	
	private JTextField targetField = new JTextField(40);
	private JTextField replaceField = new JTextField(40);
	
	private JPanel contentPane = new JPanel();
	private GridBagLayout gridBagLayout = new GridBagLayout();
	private Insets insetsButton = new Insets(9,7,5,7);
	
	private int findPosition = 0;
	
	private Font font2 = new Font("Consolas",Font.PLAIN,14);
	
	public SearchAndReplaceDialog(JFrame verilogEditor, JTextPane codeText)
	{
		super(verilogEditor, "Search and Replace",true);
		
		this.codeTextTemp = codeText;
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		hilit = new DefaultHighlighter();
		painter = new DefaultHighlighter.DefaultHighlightPainter(codeTextTemp.getSelectionColor());
		codeTextTemp.setHighlighter(hilit);
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				hilit.removeAllHighlights();
			}
		});
		
		contentPane.setLayout(gridBagLayout);
		this.setContentPane(contentPane);
		
		GridBagConstraints cSearch = new GridBagConstraints();
		cSearch.gridx = 0;
		cSearch.gridy = 0;
		cSearch.anchor = GridBagConstraints.LINE_END;
		cSearch.insets = new Insets(0,5,0,3);
		contentPane.add(search, cSearch);
		
		GridBagConstraints cReplace = new GridBagConstraints();
		cReplace.gridx = 0;
		cReplace.gridy = 1;
		cReplace.anchor = GridBagConstraints.FIRST_LINE_END;
		cReplace.insets = new Insets(0,5,10,3);
		contentPane.add(replace, cReplace);
		
		GridBagConstraints cTargetField = new GridBagConstraints();
		cTargetField.gridx = 1;
		cTargetField.gridy = 0;
		cTargetField.gridwidth = 4;
		cTargetField.anchor = GridBagConstraints.LINE_START;
		cTargetField.insets = new Insets(0,7,0,10);
		targetField.setFont(font2);
		targetField.setPreferredSize(new Dimension(1200, 22));
		targetField.setMinimumSize(new Dimension(1200, 22));
		targetField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					String str = codeTextTemp.getSelectedText();
					if(str == null || str.equals(""))
					{
						findPosition = 0;
					}
					findFunction(codeTextTemp,targetField.getText());
				}
			}
		});
		contentPane.add(targetField, cTargetField);
		
		GridBagConstraints cReplaceField = new GridBagConstraints();
		cReplaceField.gridx = 1;
		cReplaceField.gridy = 1;
		cReplaceField.gridwidth = 4;
		cReplaceField.anchor = GridBagConstraints.LINE_START;
		cReplaceField.insets = new Insets(0,7,10,10);
		replaceField.setFont(font2);
		replaceField.setPreferredSize(new Dimension(1200,22));
		replaceField.setMinimumSize(new Dimension(1200, 22));
		contentPane.add(replaceField, cReplaceField);
		
		GridBagConstraints cFind = new GridBagConstraints();
		cFind.gridx = 0;
		cFind.gridy = 2;
		cFind.insets = insetsButton;
		find.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				findPosition = 0;
				findFunction(codeTextTemp,targetField.getText());
			}
		});
		contentPane.add(find, cFind);
		
		GridBagConstraints cReplaceButton = new GridBagConstraints();
		cReplaceButton.gridx = 1;
		cReplaceButton.gridy = 2;
		cReplaceButton.insets = insetsButton;
		replaceButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String str = codeTextTemp.getSelectedText();
				if(str != null && !str.equals(""))
				{
					codeTextTemp.replaceSelection(replaceField.getText());
				}
			}
		});
		contentPane.add(replaceButton, cReplaceButton);
		
		GridBagConstraints cRAndS = new GridBagConstraints();
		cRAndS.gridx = 2;
		cRAndS.gridy = 2;
		cRAndS.insets = insetsButton;
		rAndS.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String str = codeTextTemp.getSelectedText();
				if(str != null && !str.equals(""))
				{
					codeTextTemp.replaceSelection(replaceField.getText());
				}
				if(str == null || str.equals(""))
				{
					
					findPosition = 0;
				}
				findFunction(codeTextTemp,targetField.getText());
			}
		});
		contentPane.add(rAndS, cRAndS);
		
		GridBagConstraints cNext = new GridBagConstraints();
		cNext.gridx = 3;
		cNext.gridy = 2;
		cNext.insets = insetsButton;
		next.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String str = codeTextTemp.getSelectedText();
				if(str == null || str.equals(""))
				{
					findPosition = 0;
				}
				findFunction(codeTextTemp,targetField.getText());
			}
		});
		contentPane.add(next, cNext);
		
		GridBagConstraints cReplaceAll = new GridBagConstraints();
		cReplaceAll.gridx = 4;
		cReplaceAll.gridy = 2;
		cReplaceAll.insets = insetsButton;
		replaceAll.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String str = targetField.getText();
				if(str != null && !str.equals(""))
				{
					findPosition = 0;
					replaceAllFunction(codeTextTemp, str, replaceField.getText());
				}
			}
		});
		contentPane.add(replaceAll, cReplaceAll);
		
		GridBagConstraints cCaseSensitive = new GridBagConstraints();
		cCaseSensitive.gridx = 4;
		cCaseSensitive.gridy = 0;
		cCaseSensitive.weighty = 2;
		cCaseSensitive.insets = new Insets(0,30,0,5);
		contentPane.add(caseSensitive, cCaseSensitive);
		
		this.setLocationRelativeTo(verilogEditor);
		this.setVisible(true);
		
	}
	
	public void findFunction(JTextPane textArea, String target)
	{
		hilit.removeAllHighlights();
		int i = -1;
		String textAreaText = textArea.getText();
		if(!caseSensitive.isSelected())
		{
			i = textAreaText.toLowerCase().indexOf(target.toLowerCase(), findPosition);
			System.out.println(i);
		}
		else
		{
			i = textAreaText.indexOf(target, findPosition);
		}
		if(i >= 0)
		{
			textArea.setSelectionStart(i);
			textArea.setSelectionEnd(i + target.length());
			try
			{
				hilit.addHighlight(i, i + target.length(), painter);
			} catch (BadLocationException e)
			{
				e.printStackTrace();
			}
			findPosition = i + 1;
		}
		else
		{
			if(findPosition == 0)
			return;
			else
			{
				findPosition = 0;
				findFunction(textArea, target);
			}
		}
	}
	
	public void replaceAllFunction(JTextPane textArea, String fromStr, String toStr)
	{
		int i = -1;
		String textAreaText = textArea.getText();
		if(!caseSensitive.isSelected())
		{
			i = textAreaText.toLowerCase().indexOf(fromStr.toLowerCase(), findPosition);
		}
		else
		{
			i = textAreaText.indexOf(fromStr, findPosition);
		}
		if(i >= 0)
		{
			textArea.setSelectionStart(i);
			textArea.setSelectionEnd(i + fromStr.length());
			findPosition = i + 1;
			textArea.replaceSelection(toStr);
			replaceAllFunction(textArea, fromStr, toStr);
		}
		else
		{
			return;
		}
	}
}