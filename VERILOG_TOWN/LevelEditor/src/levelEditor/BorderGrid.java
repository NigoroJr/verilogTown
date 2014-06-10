package levelEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/** Grid on the border where the user clicks to indicate the start/end.
 * 
 * @author Naoki */
public class BorderGrid extends JPanel implements MouseListener
{

	/** Generated Serial version ID. */
	private static final long	serialVersionUID	= -6991390977124264503L;

	private StateTracker		tracker;
	private int					border;

	public BorderGrid(StateTracker tracker, int border, int length)
	{
		this.tracker = tracker;
		this.border = border;

		this.setBorder(BorderFactory.createLineBorder(Color.RED));

		addMouseListener(this);

		int width = border == MapGridGroup.NORTH || border == MapGridGroup.SOUTH ? length * (MapGrid.GRID_SIZE + MapEditor.BORDER) : MapEditor.EDGE_SIZE + 2 * MapEditor.BORDER;
		int height = border == MapGridGroup.NORTH || border == MapGridGroup.SOUTH ? MapEditor.EDGE_SIZE + 2 * MapEditor.BORDER : length * (MapGrid.GRID_SIZE + MapEditor.BORDER);

		setPreferredSize(new Dimension(width, height));
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		tracker.clicked();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		tracker.released();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		int exitedFrom = MapGridGroup.NIL;

		switch (border)
		{
			case MapGridGroup.NORTH:
				exitedFrom = MapGridGroup.SOUTH;
			break;
			case MapGridGroup.SOUTH:
				exitedFrom = MapGridGroup.NORTH;
			break;
			case MapGridGroup.EAST:
				exitedFrom = MapGridGroup.WEST;
			break;
			case MapGridGroup.WEST:
				exitedFrom = MapGridGroup.EAST;
			break;
		}

		tracker.setPreviouslyExitedFrom(exitedFrom);

		if (MapGridGroup.DEBUG)
			printInfo("EXIT", exitedFrom);
	}

	private void printInfo(String state, int dir)
	{
		String s = "NIL";
		switch (dir)
		{
			case MapGridGroup.NORTH:
				s = "NORTH";
			break;
			case MapGridGroup.SOUTH:
				s = "SOUTH";
			break;
			case MapGridGroup.EAST:
				s = "EAST";
			break;
			case MapGridGroup.WEST:
				s = "WEST";
			break;
		}
		System.out.printf("%-5s border from %-5s Dragging: %s\n", state, s, tracker.isDragging());
	}
}
