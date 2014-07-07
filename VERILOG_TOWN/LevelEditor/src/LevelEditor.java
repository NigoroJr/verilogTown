

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Level editor for the game Verilog Town.
 * 
 * @author Naoki */

public class LevelEditor extends JFrame
{
	/** Generated serial version ID. */
	private static final long	serialVersionUID			= -1326785106168812433L;

	/** Environment variable set when developing. Non-zero value indicates
	 * development mode. In development mode, the directory structure is
	 * different. */
	public static final String	VERILOG_TOWN_DEVELOPMENT	= "VERILOG_TOWN_DEVELOPMENT";

	public static final String	BUTTON_OK					= "OK";
	public static final String	BUTTON_CANCEL				= "Cancel";
	public static final String	BUTTON_SELECT				= "Select";

	private JRadioButton		create;
	private JRadioButton		update;
	private JTextField			textLevelNumber;
	private JTextField			textMapSizeX;
	private JTextField			textMapSizeY;
	private JTextField			textFilePath;
	private JFileChooser		chooser;

	private int					levelNumber;
	private int					mapSizeX;
	private int					mapSizeY;

	public LevelEditor()
	{
		create = new JRadioButton("Create");
		update = new JRadioButton("Update");
		textLevelNumber = new JTextField(3);
		textMapSizeX = new JTextField(3);
		textMapSizeY = new JTextField(3);
		textFilePath = new JTextField(15);
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
		chooser.setFileFilter(filter);

		setTitle("verilogTown Level Editor");

		add(radioButtonBuilder());
		add(buttonBuilder(), BorderLayout.SOUTH);

		setMinimumSize(new Dimension(300, 280));
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/** Builds the JPanel with the radio button to select whether to create or
	 * update an existing map.
	 * 
	 * @return JPanel with the radio buttons. */
	private JPanel radioButtonBuilder()
	{
		JPanel ret = new JPanel();
		ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));

		// Make an extra JPanel so that FlowLayout can be used
		JPanel panelCreate = new JPanel();
		panelCreate.setLayout(new FlowLayout(FlowLayout.LEFT));
		// Create should be the default selected
		create.setSelected(true);
		panelCreate.add(create);

		// Make an extra JPanel so that FlowLayout can be used
		JPanel panelUpdate = new JPanel();
		panelUpdate.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelUpdate.add(update);

		ButtonGroup group = new ButtonGroup();
		group.add(create);
		group.add(update);

		ret.add(panelCreate);
		ret.add(createPanelBuilder());
		ret.add(panelUpdate);
		ret.add(fileChooserBuilder());

		return ret;
	}

	/** Builds the JPanel for the "create" option. This panel contains the level
	 * number and the size of the map.
	 * 
	 * @return JPanel containing the JTextField for the level number and the map
	 *         size. */
	private JPanel createPanelBuilder()
	{
		JPanel ret = new JPanel();
		ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));

		FlowLayout fl = new FlowLayout(FlowLayout.RIGHT);

		JPanel levelNumber = new JPanel();
		levelNumber.setLayout(fl);
		levelNumber.add(new JLabel("Level Number"));
		levelNumber.add(textLevelNumber);

		JPanel mapSizeX = new JPanel();
		mapSizeX.setLayout(fl);
		mapSizeX.add(new JLabel("Map Size (X)"));
		mapSizeX.add(textMapSizeX);

		JPanel mapSizeY = new JPanel();
		mapSizeY.setLayout(fl);
		mapSizeY.add(new JLabel("Map Size (Y)"));
		mapSizeY.add(textMapSizeY);

		ret.add(levelNumber);
		ret.add(mapSizeX);
		ret.add(mapSizeY);

		return ret;
	}

	/** Builds the JPanel for selecting a file.
	 * 
	 * @return JPanel with the path to the file and the select button. */
	private JPanel fileChooserBuilder()
	{
		JPanel ret = new JPanel();
		ret.setLayout(new FlowLayout(FlowLayout.CENTER));

		ret.add(textFilePath);
		JButton select = new JButton(BUTTON_SELECT);
		select.addActionListener(new ButtonListener());
		ret.add(select);

		return ret;
	}

	/** Builds the JPanel with the OK and Cancel buttons.
	 * 
	 * @return JPanel with the OK and cancel buttons. */
	private JPanel buttonBuilder()
	{
		JPanel ret = new JPanel();

		JButton ok = new JButton(BUTTON_OK);
		JButton cancel = new JButton(BUTTON_CANCEL);

		ButtonListener al = new ButtonListener();
		ok.addActionListener(al);
		cancel.addActionListener(al);

		ret.add(ok);
		ret.add(cancel);

		return ret;
	}

	class ButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			JButton button = (JButton) event.getSource();
			switch (button.getText())
			{
				case BUTTON_OK:
					try
					{
						clickedOK();
					}
					catch (FileNotFoundException e)
					{
						String mes = String.format("File: %s not found", textFilePath.getText());
						JOptionPane.showMessageDialog(null, mes, "File not found", JOptionPane.ERROR_MESSAGE);
					}
					catch (EmptyTextFieldException e)
					{
						String mes = "Text fields must not be empty";
						JOptionPane.showMessageDialog(null, mes, "Empty text field(s)", JOptionPane.ERROR_MESSAGE);
					}
				break;
				case BUTTON_CANCEL:
					dispose();
					System.exit(0);
				break;
				case BUTTON_SELECT:
					int returnVal = chooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						String path = chooser.getSelectedFile().getPath();
						textFilePath.setText(path);

						update.setSelected(true);
					}
			}
		}

		/** Called when OK button is clicked.
		 * 
		 * @throws EmptyTextFieldException
		 *             When create is selected but there is an empty field in
		 *             the JTextField. Level number and the map size is required
		 *             to create a new map.
		 * @throws FileNotFoundException
		 *             When a file is specified but the file is not found. */
		private void clickedOK()
				throws EmptyTextFieldException,
				FileNotFoundException
		{
			if (create.isSelected())
			{

				if (textLevelNumber.getText().isEmpty() || textMapSizeX.getText().isEmpty() || textMapSizeY.getText().isEmpty())
					throw new EmptyTextFieldException();

				levelNumber = Integer.parseInt(textLevelNumber.getText());
				mapSizeX = Integer.parseInt(textMapSizeX.getText());
				mapSizeY = Integer.parseInt(textMapSizeY.getText());

				// Don't allow odd or negative number size
				if (mapSizeX % 2 != 0 || mapSizeY % 2 != 0 || mapSizeX <= 0 || mapSizeY <= 0)
				{
					String mes = "Both map sizes must be positive even numbers!";
					JOptionPane.showMessageDialog(null, mes, "Invalid map size", JOptionPane.ERROR_MESSAGE);
					return;
				}

				setVisible(false);
				new MapEditor(levelNumber, mapSizeX, mapSizeY);
				setVisible(true);
			}
			else if (update.isSelected())
			{
				String xmlFilePath = textFilePath.getText();

				if (!new File(xmlFilePath).exists())
					throw new FileNotFoundException();

				setVisible(false);
				// GRID_SIZE is set in the MapEditor after reading the map size
				new MapEditor(xmlFilePath);
				setVisible(true);
			}
		}
	}

	/** Returns the root path of this program. Exact copy of
	 * VerilogTown.getRootPath(). It is assumed that this program is on the same
	 * level as verilogTown and verilogEditor.
	 * 
	 * @return The root path of this program. */
	public static String getRootPath()
	{
		String path = null;
		try
		{
			path = LevelEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			// Path can be a filename when executing a jar file. (filename/../)
			// doesn't work.
			path = new File(path).getParent() + "/../";
			// Development environment has different directory structure than
			// that when releasing
			if (isDevelopment())
				path += "../";
			/* getCanonicalPath() returns a path containing "\", which doesn't
			 * work (even on Windows) when passing the path as a command line
			 * argument. Thus, regular expression <code>\\\b</code> is used to
			 * substitute '\' to '/'. */
			path = new File(path).getCanonicalPath().replaceAll("\\\\\\b", "/");
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return path;
	}

	public static boolean isDevelopment()
	{
		String env = System.getenv(VERILOG_TOWN_DEVELOPMENT);
		return env != null && !env.equals("0");
	}

	public static void main(String[] args)
	{
		new LevelEditor();
	}
}
