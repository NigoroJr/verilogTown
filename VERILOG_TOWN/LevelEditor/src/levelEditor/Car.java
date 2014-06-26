package levelEditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Car extends JPanel implements ActionListener
{
	/** Generated serial version ID */
	private static final long	serialVersionUID	= -1830415335894513637L;

	private int					id;
	private int[][]				allStarts;
	private int[][]				allEnds;

	private CarEditor			carEditor;

	private JLabel				carID;
	private JComboBox<String>	startPoints;
	private JComboBox<String>	endPoints;
	private JTextField			delayTextField;
	private JButton				removeButton;

	/** Creates a new Car object that contains the car ID, starting and ending
	 * coordinates, and the delay.
	 * 
	 * @param id
	 * @param start
	 *            An array that contains exactly two elements. First is the X
	 *            coordinate and the second is Y coordinate of the starting
	 *            point.
	 * @param end
	 *            An array that contains exactly two elements. First is the X
	 *            coordinate and the second is Y coordinate of the ending point.
	 * @param delay
	 *            Delay for the car to start after the game has started. */
	public Car(CarEditor ce, int id, int[][] allStarts, int[][] allEnds)
	{
		super();

		this.carEditor = ce;
		this.id = id;
		this.allStarts = allStarts;
		this.allEnds = allEnds;

		panelBuilder();
	}

	/** Adds the car ID, pull-down menu of the starting and ending points, and
	 * the text field for the delay to this JPanel. */
	private void panelBuilder()
	{
		GridBagLayout gbl = new GridBagLayout();
		this.setLayout(gbl);

		GridBagConstraints gbc = new GridBagConstraints();

		carID = new JLabel(Integer.toString(id));
		startPoints = new JComboBox<String>(getAllCoordinates(allStarts));
		endPoints = new JComboBox<String>(getAllCoordinates(allEnds));
		delayTextField = new JTextField();
		delayTextField.setPreferredSize(new Dimension(50, 30));
		removeButton = new JButton("-");
		removeButton.setPreferredSize(new Dimension(20, 20));
		removeButton.addActionListener(this);

		// Car ID
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(carID, gbc);

		// Starting point
		gbc.gridx++;
		gbl.setConstraints(startPoints, gbc);

		// Ending point
		gbc.gridx++;
		gbl.setConstraints(endPoints, gbc);

		// Delay
		gbc.gridx++;
		gbl.setConstraints(delayTextField, gbc);

		// Button
		gbc.gridx++;
		gbl.setConstraints(removeButton, gbc);

		this.add(carID);
		this.add(startPoints);
		this.add(endPoints);
		this.add(delayTextField);
		this.add(removeButton);
	}

	/** Converts the array of coordinates into String representation.
	 * 
	 * @param coordinates
	 *            Array of coordinates. Has to be size [n][2] for any n.
	 * @return Array of coordinates represented in String. */
	private String[] getAllCoordinates(int[][] coordinates)
	{
		String[] ret = new String[coordinates.length];
		for (int i = 0; i < coordinates.length; i++)
			ret[i] = String.format("%s, %s", coordinates[i][0], coordinates[i][1]);

		return ret;
	}

	/** Updates the ID for this car and also updates the text in the JLabel.
	 * 
	 * @param id
	 *            The new ID for this car. */
	public void setID(int id)
	{
		this.id = id;
		this.carID.setText(Integer.toString(id));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == removeButton)
		{
			carEditor.removeCarID(id);
		}
	}
}