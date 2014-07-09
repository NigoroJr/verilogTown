import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/** Car editor for the level.
 * 
 * @author Naoki Mizuno */

public class CarEditor extends JDialog
{
	/** Generated serial version ID. */
	private static final long	serialVersionUID	= -2100886388622797805L;

	public static final int		WINDOW_WIDTH		= 400;

	private int[][]				starts;
	private int[][]				ends;

	private ArrayList<Car>		cars;
	/** Cars that were given to the constructor. Used to compare whether there
	 * the cars were edited or not */
	private ArrayList<Car>		givenCars;
	/* Used in multiple methods to remove cars */
	private JPanel				carListPanel;
	private GridBagLayout		gbl;
	private JButton				saveButton;
	private JButton				cancelButton;
	private JButton				addCarButton;

	public CarEditor(ArrayList<Car> cars, int[][] starts, int[][] ends)
	{
		this.cars = cars;
		this.givenCars = new ArrayList<Car>();
		this.starts = starts;
		this.ends = ends;

		for (int i = 0; i < cars.size(); i++)
		{
			// Update the CarEditor that each car belongs to
			this.cars.get(i).setCarEditor(this);
			this.cars.get(i).setID(i);
			// Deep copy given cars
			Car copy = cars.get(i).clone();
			copy.setCarEditor(this);
			copy.setID(i);
			this.givenCars.add(copy);
		}

		gbl = new GridBagLayout();
		carListPanel = new JPanel();
		carListPanel.setLayout(gbl);
		saveButton = new JButton("Save");
		cancelButton = new JButton("Cancel");
		addCarButton = new JButton("Add a Car");
		saveButton.addActionListener(new ClickListener());
		cancelButton.addActionListener(new ClickListener());
		addCarButton.addActionListener(new ClickListener());

		// "Add car" button
		JPanel addCarPanel = new JPanel();
		addCarPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addCarPanel.add(addCarButton);

		// Panel with all the cars
		JPanel panel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		panel.setLayout(gbl);

		// Add cars in ArrayList to carListPanel
		readdCarsToPanel();

		// Make carListPanel scrollable
		JViewport viewPort = new JViewport();
		viewPort.setView(carListPanel);
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		/* Important! */
		scrollPane.setMinimumSize(new Dimension(WINDOW_WIDTH, 600));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setViewport(viewPort);
		scrollPane.getVerticalScrollBar().setUnitIncrement(5);
		gbl.setConstraints(scrollPane, gbc);
		panel.add(scrollPane);

		this.add(addCarPanel, BorderLayout.NORTH);
		this.add(panel);
		this.add(buttonsBuilder(), BorderLayout.SOUTH);
		this.setSize(new Dimension(WINDOW_WIDTH, 600));
		this.setMinimumSize(new Dimension(WINDOW_WIDTH, 600));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setModalityType(ModalityType.DOCUMENT_MODAL);
		this.setVisible(true);
	}

	/** Creates a JPanel with "save" and "cancel" buttons.
	 * 
	 * @return JPanel with save and cancel buttons. */
	private JPanel buttonsBuilder()
	{
		JPanel panel = new JPanel();

		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.add(saveButton);
		panel.add(cancelButton);

		return panel;
	}

	/** Re-adds the cars in the ArrayList to the panel. */
	private void readdCarsToPanel()
	{
		carListPanel.removeAll();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.weighty = 0;
		gbc.gridy = 0;

		Iterator<Car> it = cars.iterator();
		while (it.hasNext())
		{
			Car c = it.next();
			gbl.setConstraints(c, gbc);
			carListPanel.add(c);
			gbc.gridy++;
		}
		repaint();
		revalidate();
	}

	/** Removes one car from the list.
	 * 
	 * @param id
	 *            ID of the car to be removed. */
	public void removeCarID(int id)
	{
		cars.remove(id);
		for (int i = 0; i < cars.size(); i++)
			cars.get(i).setID(i);
		readdCarsToPanel();
	}

	/** Does a linear search to check whether there are no cars coming from the
	 * same intersections at the same time. If there is, this method will show a
	 * pop-up message and return false to the calling method. This method also
	 * checks whether all the JTextField is filled and displays a message if
	 * not.
	 * 
	 * @return True if starting points and delays are valid, false if not. */
	private boolean checkStartAndDelayValidity()
	{
		ArrayList<String> invalidCars = new ArrayList<String>();
		for (int i = 0; i < cars.size(); i++)
		{
			Car car1 = cars.get(i);
			for (int j = i + 1; j < cars.size(); j++)
			{
				Car car2 = cars.get(j);

				int delayCar1, delayCar2;
				try
				{
					delayCar1 = car1.getDelay();
					delayCar2 = car2.getDelay();
				}
				catch (EmptyTextFieldException e)
				{
					String message = "Please input delay for all cars";
					JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);
					return false;
				}

				if (car1.getStart()[0] == car2.getStart()[0] && car1.getStart()[1] == car2.getStart()[1] && delayCar1 == delayCar2)
					invalidCars.add(String.format("Car %d and %d", i, j));
			}
		}

		if (invalidCars.isEmpty())
			return true;

		// Show an error message
		String message = "The following cars start at the same place at the same time:\n";
		for (int i = 0; i < invalidCars.size(); i++)
			message += invalidCars.get(i) + "\n";
		JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);

		return false;
	}

	class ClickListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			/* Note: This doesn't actually save to XML */
			if (e.getSource() == saveButton)
			{
				if (checkStartAndDelayValidity())
					dispose();
			}
			else if (e.getSource() == cancelButton)
			{
				// Reset to the initial list
				cars.clear();
				for (int i = 0; i < givenCars.size(); i++)
					cars.add(givenCars.get(i));
				dispose();
			}
			else if (e.getSource() == addCarButton)
			{
				// Add new car to end of the list
				cars.add(new Car(CarEditor.this, cars.size(), starts, ends));
				readdCarsToPanel();
			}
		}
	}
}