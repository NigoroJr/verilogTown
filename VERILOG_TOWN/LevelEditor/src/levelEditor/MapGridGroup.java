package levelEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** This class groups together four tiles and create a JPanel. The group consists
 * of four tiles, namely, topLeft, topRight, bottomLeft, and bottomRight.
 * 
 * @author Naoki Mizuno */

public class MapGridGroup extends JPanel implements MouseListener
{
	public static boolean		DEBUG				= false;

	/* Corners and straight roads */
	public static final String	STRAIGHT_NS			= "STRAIGHT_NS";
	public static final String	STRAIGHT_EW			= "STRAIGHT_EW";
	public static final String	CORNER_NW			= "CORNER_NW";
	public static final String	CORNER_SW			= "CORNER_SW";
	public static final String	CORNER_NE			= "CORNER_NE";
	public static final String	CORNER_SE			= "CORNER_SE";

	/* Intersections */
	public static final String	FOUR_WAY			= "FOUR_WAY";
	/* The naming convention of three-way intersection is different from the
	 * main game program. The reason of this is because it is easier to process
	 * intersections by specifying them by where the intersection is open to.
	 * Thus, the String value accounts for this different so that when exporting
	 * to an XML file it uses the appropriate intersection name used in the
	 * game. */
	public static final String	THREE_WAY_NSE		= "THREE_WAY_NSW";
	public static final String	THREE_WAY_SEW		= "THREE_WAY_NEW";
	public static final String	THREE_WAY_NSW		= "THREE_WAY_NSE";
	public static final String	THREE_WAY_NEW		= "THREE_WAY_SEW";

	public static final String	NON_ROAD			= "NON_ROAD";

	/* Directions */
	public static final int		NIL					= 0;
	public static final int		NORTH				= 1;
	public static final int		SOUTH				= 2;
	public static final int		EAST				= 3;
	public static final int		WEST				= 4;

	private StateTracker		tracker;

	private String				type;
	// Note that these are not the same as the x, y in MapGrid class
	private int					x;
	private int					y;

	private MapGrid				topLeft;
	private MapGrid				topRight;
	private MapGrid				bottomLeft;
	private MapGrid				bottomRight;

	private boolean				isStartingPoint;
	private int					enteredFrom;
	private int					exitedFrom;

	private GridBagLayout		gbl;
	/** Generated serial version ID. */
	private static final long	serialVersionUID	= -1460428408091944849L;

	public MapGridGroup(StateTracker tracker, String type, int x, int y)
	{
		this.tracker = tracker;
		this.type = type;
		this.x = x;
		this.y = y;

		resetEnterExit();

		setBorder(BorderFactory.createLineBorder(Color.RED));
		setSize(new Dimension(2 * (MapGrid.GRID_SIZE + MapEditor.BORDER), 2 * (MapGrid.GRID_SIZE + MapEditor.BORDER)));
		setMaximumSize(getSize());
		setMinimumSize(getSize());
		setPreferredSize(getSize());

		setGrids();

		gbl = new GridBagLayout();
		setLayout(gbl);

		addGrids();

		addMouseListener(this);
	}

	public void setType(String type)
	{
		this.type = type;
		setGrids();
		removeAll();
		addGrids();
		revalidate();
	}

	private void addGrids()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		// Add top left
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(topLeft, gbc);
		add(topLeft);
		// Add top right
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbl.setConstraints(topRight, gbc);
		add(topRight);
		// Add bottom left
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbl.setConstraints(bottomLeft, gbc);
		add(bottomLeft);
		// Add bottom right
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbl.setConstraints(bottomRight, gbc);
		add(bottomRight);
	}

	private void setGrids()
	{
		/* Types of grid for each grid in group */
		String tl = null, tr = null, bl = null, br = null;

		switch (type)
		{
			case STRAIGHT_NS:
				tl = MapGrid.STRAIGHT_ROAD_S2S;
				tr = MapGrid.STRAIGHT_ROAD_N2N;
				bl = MapGrid.STRAIGHT_ROAD_S2S;
				br = MapGrid.STRAIGHT_ROAD_N2N;
			break;
			case STRAIGHT_EW:
				tl = MapGrid.STRAIGHT_ROAD_W2W;
				tr = MapGrid.STRAIGHT_ROAD_W2W;
				bl = MapGrid.STRAIGHT_ROAD_E2E;
				br = MapGrid.STRAIGHT_ROAD_E2E;
			break;
			case CORNER_NW:
				tl = MapGrid.CORNER_ROAD_S2W;
				tr = MapGrid.STRAIGHT_ROAD_N2N;
				bl = MapGrid.STRAIGHT_ROAD_E2E;
				br = MapGrid.CORNER_ROAD_E2N;
			break;
			case CORNER_SW:
				tl = MapGrid.STRAIGHT_ROAD_W2W;
				tr = MapGrid.CORNER_ROAD_N2W;
				bl = MapGrid.CORNER_ROAD_E2S;
				br = MapGrid.STRAIGHT_ROAD_N2N;
			break;
			case CORNER_NE:
				tl = MapGrid.STRAIGHT_ROAD_S2S;
				tr = MapGrid.CORNER_ROAD_W2N;
				bl = MapGrid.CORNER_ROAD_S2E;
				br = MapGrid.STRAIGHT_ROAD_E2E;
			break;
			case CORNER_SE:
				tl = MapGrid.CORNER_ROAD_W2S;
				tr = MapGrid.STRAIGHT_ROAD_W2W;
				bl = MapGrid.STRAIGHT_ROAD_S2S;
				br = MapGrid.CORNER_ROAD_N2E;
			break;

			case FOUR_WAY:
				tl = MapGrid.INTER_TURN_S2WS;
				tr = MapGrid.INTER_TURN_W2NW;
				bl = MapGrid.INTER_TURN_E2SE;
				br = MapGrid.INTER_TURN_N2EN;
			break;
			case THREE_WAY_NSE:
				tl = MapGrid.STRAIGHT_ROAD_S2S;
				tr = MapGrid.INTER_TURN_W2NW;
				bl = MapGrid.INTER_TURN_S2ES_STR;
				br = MapGrid.INTER_TURN_N2EN;
			break;
			case THREE_WAY_SEW:
				tl = MapGrid.INTER_TURN_W2SW_STR;
				tr = MapGrid.STRAIGHT_ROAD_W2W;
				bl = MapGrid.INTER_TURN_E2SE;
				br = MapGrid.INTER_TURN_N2EN;
			break;
			case THREE_WAY_NSW:
				tl = MapGrid.INTER_TURN_S2WS;
				tr = MapGrid.INTER_TURN_N2WN_STR;
				bl = MapGrid.INTER_TURN_E2SE;
				br = MapGrid.STRAIGHT_ROAD_N2N;
			break;
			case THREE_WAY_NEW:
				tl = MapGrid.INTER_TURN_S2WS;
				tr = MapGrid.INTER_TURN_W2NW;
				bl = MapGrid.STRAIGHT_ROAD_E2E;
				br = MapGrid.INTER_TURN_E2NE_STR;
			break;
			default:
				tl = tr = bl = br = MapGrid.NON_ROAD;
		}

		int x = this.x * 2;
		int y = this.y * 2;

		// Bottom-left grid is (0, 0)
		this.topLeft = new MapGrid(tl, x, y + 1);
		this.topRight = new MapGrid(tr, x + 1, y + 1);
		this.bottomLeft = new MapGrid(bl, x, y);
		this.bottomRight = new MapGrid(br, x + 1, y);
	}

	/** Updates the type of this grid, if necessary, according to the direction
	 * it entered and exited from. */
	public void updateGridType()
	{
		if (!tracker.isDragging())
			return;

		String prevType = type;

		if (type.startsWith("THREE_WAY"))
			checkThreeWay();
		else if (type.startsWith("STRAIGHT"))
			checkStraight();
		else if (type.startsWith("CORNER"))
			checkCorner();
		else if (type.equals(NON_ROAD))
			checkNonRoad();

		if (DEBUG && !prevType.equals(type))
			System.out.println("Grid changed to " + type);
	}

	/** Checks when the current type of this grid is a three-way intersection. */
	private void checkThreeWay()
	{
		boolean becomesFourWay = false;
		switch (type)
		{
			case THREE_WAY_NSE:
				becomesFourWay = enteredFrom == WEST || exitedFrom == WEST;
			break;
			case THREE_WAY_SEW:
				becomesFourWay = enteredFrom == NORTH || exitedFrom == NORTH;
			break;
			case THREE_WAY_NSW:
				becomesFourWay = enteredFrom == EAST || exitedFrom == EAST;
			break;
			case THREE_WAY_NEW:
				becomesFourWay = enteredFrom == SOUTH || exitedFrom == SOUTH;
			break;
		}

		if (becomesFourWay)
			setType(FOUR_WAY);
	}

	private void checkStraight()
	{
		if (type.equals(STRAIGHT_NS))
		{
			// enteredFrom == NIL but exitedFrom != NIL
			if (isStartingPoint)
			{
				if (exitedFrom == EAST)
					type = THREE_WAY_NSE;
				else if (exitedFrom == WEST)
					type = THREE_WAY_NSW;
			}
			else if (enteredFrom == EAST)
			{
				if (exitedFrom == WEST)
					type = FOUR_WAY;
				else
					type = THREE_WAY_NSE;
			}
			else if (enteredFrom == WEST)
			{
				if (exitedFrom == EAST)
					type = FOUR_WAY;
				else
					type = THREE_WAY_NSW;
			}
			else
			{
				if (exitedFrom == EAST)
					type = THREE_WAY_NSE;
				else if (exitedFrom == WEST)
					type = THREE_WAY_NSW;
			}
		}

		else if (type.equals(STRAIGHT_EW))
		{
			// enteredFrom == NIL but exitedFrom != NIL
			if (isStartingPoint)
			{
				if (exitedFrom == NORTH)
					type = THREE_WAY_NEW;
				else if (exitedFrom == SOUTH)
					type = THREE_WAY_SEW;
			}
			else if (enteredFrom == NORTH)
			{
				if (exitedFrom == SOUTH)
					type = FOUR_WAY;
				else
					type = THREE_WAY_NEW;
			}
			else if (enteredFrom == SOUTH)
			{
				if (exitedFrom == NORTH)
					type = FOUR_WAY;
				else
					type = THREE_WAY_SEW;
			}
			else
			{
				if (exitedFrom == NORTH)
					type = THREE_WAY_NEW;
				else if (exitedFrom == SOUTH)
					type = THREE_WAY_SEW;
			}
		}

		setType(type);
	}

	private void checkCorner()
	{
		/* Whether the cursor "entered" or "exited" doesn't matter in this case.
		 * Thus, they are reordered by the actual values and considered as
		 * "points" to make the comparisons simpler. The actual values are: (NIL
		 * <) NORTH < SOUTH < EAST < WEST */
		int p1 = enteredFrom < exitedFrom ? enteredFrom : exitedFrom;
		int p2 = enteredFrom < exitedFrom ? exitedFrom : enteredFrom;

		if (type.equals(CORNER_NE))
		{
			if (p1 == SOUTH && p2 == WEST)
				type = FOUR_WAY;
			else if (p1 == SOUTH || p2 == SOUTH)
				type = THREE_WAY_NSE;
			else if (p1 == WEST || p2 == WEST)
				type = THREE_WAY_NEW;
		}
		else if (type.equals(CORNER_NW))
		{
			if (p1 == SOUTH && p2 == EAST)
				type = FOUR_WAY;
			else if (p1 == SOUTH || p2 == SOUTH)
				type = THREE_WAY_NSW;
			else if (p1 == EAST || p2 == EAST)
				type = THREE_WAY_NEW;
		}
		else if (type.equals(CORNER_SE))
		{
			if (p1 == NORTH && p2 == WEST)
				type = FOUR_WAY;
			else if (p1 == NORTH || p2 == NORTH)
				type = THREE_WAY_NSE;
			else if (p1 == WEST || p2 == WEST)
				type = THREE_WAY_SEW;
		}
		else if (type.equals(CORNER_SW))
		{
			if (p1 == NORTH && p2 == EAST)
				type = FOUR_WAY;
			else if (p1 == NORTH || p2 == NORTH)
				type = THREE_WAY_NSW;
			else if (p1 == EAST || p2 == EAST)
				type = THREE_WAY_SEW;
		}

		setType(type);
	}

	private void checkNonRoad()
	{
		if (enteredFrom == exitedFrom)
			return;

		// Make it look like the cursor entered from the opposite direction
		if (isStartingPoint)
			enteredFrom = getOppositeDirection(exitedFrom);

		if (enteredFrom == NORTH)
		{
			if (exitedFrom == SOUTH)
				type = STRAIGHT_NS;
			else if (exitedFrom == EAST)
				type = CORNER_NE;
			else if (exitedFrom == WEST)
				type = CORNER_NW;
		}
		else if (enteredFrom == SOUTH)
		{
			if (exitedFrom == NORTH)
				type = STRAIGHT_NS;
			else if (exitedFrom == EAST)
				type = CORNER_SE;
			else if (exitedFrom == WEST)
				type = CORNER_SW;
		}
		else if (enteredFrom == EAST)
		{
			if (exitedFrom == NORTH)
				type = CORNER_NE;
			else if (exitedFrom == SOUTH)
				type = CORNER_SE;
			else if (exitedFrom == WEST)
				type = STRAIGHT_EW;
		}
		else if (enteredFrom == WEST)
		{
			if (exitedFrom == NORTH)
				type = CORNER_NW;
			else if (exitedFrom == SOUTH)
				type = CORNER_SW;
			else if (exitedFrom == EAST)
				type = STRAIGHT_EW;
		}

		setType(type);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// Will return the same type passed in if no change
		setType(new GridTypeSelector(type).getSelectedType());
		updateGridType();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		isStartingPoint = true;
		tracker.clicked();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		isStartingPoint = false;
		tracker.released();

		updateGridType();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		if (!tracker.isDragging())
			return;

		// Clear any previous directions
		resetEnterExit();

		enteredFrom = tracker.getEnteredFrom();
		exitedFrom = NIL;
		tracker.setPreviouslyExitedFrom(exitedFrom);
		updateGridType();

		if (DEBUG)
			printInfo("ENTER", enteredFrom);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		if (!tracker.isDragging())
			return;

		if (e.getX() <= 0)
			exitedFrom = WEST;
		else if (e.getX() >= 2 * (MapGrid.GRID_SIZE + MapEditor.BORDER))
			exitedFrom = EAST;
		else if (e.getY() <= 0)
			exitedFrom = NORTH;
		else if (e.getY() >= 2 * (MapGrid.GRID_SIZE + MapEditor.BORDER))
			exitedFrom = SOUTH;

		tracker.setPreviouslyExitedFrom(exitedFrom);

		updateGridType();

		if (DEBUG)
			printInfo("EXIT", exitedFrom);

		resetEnterExit();
	}

	private void printInfo(String action, int dir)
	{
		String s = "NIL";
		switch (dir)
		{
			case NORTH:
				s = "NORTH";
			break;
			case SOUTH:
				s = "SOUTH";
			break;
			case EAST:
				s = "EAST";
			break;
			case WEST:
				s = "WEST";
			break;
		}
		System.out.printf("%-5s (%d, %d) from %-5s Dragging: %s\n", action, x, y, s, tracker.isDragging());
	}

	/** This is used to make it seem like the cursor entered from the opposite
	 * direction when the users starts from this grid.
	 * 
	 * @param direction
	 * @return The opposite direction of <code>direction</code>. */
	private int getOppositeDirection(int direction)
	{
		switch (direction)
		{
			case NORTH:
				return SOUTH;
			case SOUTH:
				return NORTH;
			case EAST:
				return WEST;
			case WEST:
				return EAST;
			default:
				return NIL;
		}
	}

	/** Resets the direction it entered and exited from. */
	private void resetEnterExit()
	{
		enteredFrom = NIL;
		exitedFrom = NIL;
		isStartingPoint = false;
	}

	public void addElement(Document doc, Element map)
	{
		MapGrid[] grids =
		{ topLeft, topRight, bottomLeft, bottomRight };
		for (int i = 0; i < 4; i++)
		{
			Element grid = doc.createElement("grid");
			// x, y coordinates don't account for the borders, but the XML uses
			// x = 0 as the left border, y = 0 as the bottom border.
			grid.setAttribute("x", Integer.toString(grids[i].getGridX() + 1));
			grid.setAttribute("y", Integer.toString(grids[i].getGridY() + 1));

			Element type = doc.createElement("type");
			// "_STR" is used to identify which image to use for intersections
			type.appendChild(doc.createTextNode(grids[i].getType().replace("_STR", "")));

			// Add <intersection> if it's the top-left of an intersection
			if (grids[i] == topLeft && (this.type.startsWith("THREE_WAY") || this.type.equals(FOUR_WAY)))
			{
				Element intersection = doc.createElement("intersection");
				intersection.appendChild(doc.createTextNode(this.type));

				grid.appendChild(intersection);
			}

			grid.appendChild(type);
			map.appendChild(grid);
		}
	}

	public BufferedImage getBufferedImage()
	{
		int imageSize = topLeft.getOriginalImageSize();
		BufferedImage group = new BufferedImage(2 * imageSize, 2 * imageSize, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = group.createGraphics();
		g.drawImage(topLeft.getBufferedImage(), 0, 0, this);
		g.drawImage(topRight.getBufferedImage(), imageSize, 0, this);
		g.drawImage(bottomLeft.getBufferedImage(), 0, imageSize, this);
		g.drawImage(bottomRight.getBufferedImage(), imageSize, imageSize, this);

		return group;
	}

	/** Returns the size of the original image. Since the image is a square and
	 * the width and height are the same length, only the width is returned.
	 * 
	 * @return The size of the original image. */
	public int getImageSize()
	{
		return 2 * topLeft.getOriginalImageSize();
	}

	public String getType()
	{
		return type;
	}
}
