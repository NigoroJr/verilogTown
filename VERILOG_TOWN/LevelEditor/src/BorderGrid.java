import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/** Grid on the border where the user clicks to indicate the start/end.
 * BorderGrid is actually one JPanel (rather than a collection of JPanels for
 * each grid). The reason why this approach is taken is because the starting and
 * ending points can be told by looking at the most outer grid type.
 * 
 * <pre>
 *   +-----+-----+
 *   |     |     |
 *   |     |     |
 *   +-----+-----+
 * E | <-  |     |
 * S | ->  |     |
 *   +-----+-----+
 *   |     |     |
 *   |     |     |
 *   +-----+-----+
 * </pre>
 * 
 * In the above figure, it can be told that there is a starting and ending point
 * at the location marked with an "S" and "E" just from the information that the
 * adjacent grid is a STRAIGHT_EW grid.
 * 
 * @author Naoki Mizuno */
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

		int width = border == MapGrid.NORTH || border == MapGrid.SOUTH ? length * (MapGrid.GRID_SIZE / 2) : MapEditor.EDGE_SIZE;
		int height = border == MapGrid.NORTH || border == MapGrid.SOUTH ? MapEditor.EDGE_SIZE : length * (MapGrid.GRID_SIZE / 2);

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
		if (!tracker.isDragging())
			return;

		int exitedFrom = MapGrid.NIL;

		switch (border)
		{
			case MapGrid.NORTH:
				exitedFrom = MapGrid.SOUTH;
			break;
			case MapGrid.SOUTH:
				exitedFrom = MapGrid.NORTH;
			break;
			case MapGrid.EAST:
				exitedFrom = MapGrid.WEST;
			break;
			case MapGrid.WEST:
				exitedFrom = MapGrid.EAST;
			break;
		}

		tracker.setPreviouslyExitedFrom(exitedFrom);
	}
}