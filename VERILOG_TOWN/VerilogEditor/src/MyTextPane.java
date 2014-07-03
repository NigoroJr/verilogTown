import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

//Pop up menu for cut, paste, and copy
public class MyTextPane extends JTextPane implements MouseListener
{
	private static final long serialVersionUID = -2308615404205560180L;
	
	private JPopupMenu pop = null; //pop up menu
	
	private JMenuItem copy = null, paste = null, cut = null; //three menu item
	
	public MyTextPane()
	{
		super();
		init();
	}
	
	private void init()
	{
		this.addMouseListener(this);
		pop = new JPopupMenu();
		pop.add(copy = new JMenuItem("Copy"));
		pop.add(paste = new JMenuItem("Paste"));
		pop.add(cut = new JMenuItem("Cut"));
		copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
		copy.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				action(e);
			}
		});
		paste.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				action(e);
			}
		});
		cut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				action(e);
			}
		});
		this.add(pop);
	}
	
	//Do something according to the menu
	public void action(ActionEvent e)
	{
		String str = e.getActionCommand();
		if (str.equals(copy.getText()))
		{
			this.copy();
		} 
		else if (str.equals(paste.getText()))
		{
			this.paste();
		} 
		else if (str.equals(cut.getText()))
		{
			this.cut();
		}
	}
	
	public JPopupMenu getPop()
	{
		return pop;
	}
	
	public void setPop(JPopupMenu pop)
	{
		this.pop = pop;
	}
	
	//Check if there is anything in the clipboard
	public boolean isClipboardString()
	{
		boolean b = false;
		Clipboard clipboard = this.getToolkit().getSystemClipboard();
		Transferable content = clipboard.getContents(this);
		try
		{
			if (content.getTransferData(DataFlavor.stringFlavor) instanceof String)
			{
				b = true;
			}
		} catch (Exception e)
		{
		}
		return b;
	}
	
	//Check if anything is been selected and thus can be copied and cut.
	public boolean isCanCopy()
	{
		boolean b = false;
		int start = this.getSelectionStart();
		int end = this.getSelectionEnd();
		if (start != end)
		b = true;
		return b;
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			copy.setEnabled(isCanCopy());
			paste.setEnabled(isClipboardString());
			cut.setEnabled(isCanCopy());
			pop.show(this, e.getX(), e.getY());
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
	}
	
}