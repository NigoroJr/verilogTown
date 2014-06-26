package levelEditor;

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

	public static final int		WINDOW_WIDTH		= 300;

	private int[][]				starts;
	private int[][]				ends;

	private ArrayList<Car>		cars;
	/* Used in multiple methods to remove cars */
	private JPanel				carListPanel;
	private GridBagLayout		gbl;
	private JButton				saveButton;
	private JButton				cancelButton;
	private JButton				addCarButton;

	public CarEditor(int[][] starts, int[][] ends)
	{
		this.starts = starts;
		this.ends = ends;

		gbl = new GridBagLayout();
		cars = new ArrayList<Car>();
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

		// Add to carListPanel
		readdCarsToPanel();

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

	class ClickListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == saveButton)
			{
				// TODO
			}
			else if (e.getSource() == cancelButton)
			{
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