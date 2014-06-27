package levelEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** Map editor for the level.
 * 
 * @author Naoki */

public class MapEditor extends JDialog
{
	/** Generated serial version ID. */
	private static final long	serialVersionUID	= -115622713904200668L;

	// Development only
	public static final String	LEVEL_FILE_LOCATION	= "levels/lv%d/";
	// public static final String LEVEL_FILE_LOCATION = "../../../levels/lv%d/";
	public static final String	LEVEL_FILE_NAME		= "level.xml";
	/** Size of edge rows/columns in pixels */
	public static final int		EDGE_SIZE			= 15;
	/** Width of the border in pixels */
	public static final int		BORDER				= 1;

	private StateTracker		tracker;
	private File				xmlFile;
	private int					levelNumber;
	private int					sizeX;
	private int					sizeY;
	private MapGridGroup[][]	gridGroups;
	/** ArrayList of start and end coordinates. startsEnds[0] has the starting
	 * coordinates and startsEnds[1] has the ending coordinates. */
	private ArrayList<int[]>	starts;
	private ArrayList<int[]>	ends;

	/** Constructor for creating a new level.
	 * 
	 * @param levelNumber
	 * @param sizeX
	 *            Size of the map in the X direction.
	 * @param sizeY
	 *            Size of the map in the Y direction. */
	public MapEditor(int levelNumber, int sizeX, int sizeY)
	{
		this.levelNumber = levelNumber;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.tracker = new StateTracker();
		this.gridGroups = new MapGridGroup[sizeY / 2][sizeX / 2];
		starts = new ArrayList<int[]>();
		ends = new ArrayList<int[]>();

		// Create directory (if it doesn't exist)
		new File(String.format(LEVEL_FILE_LOCATION, levelNumber)).mkdirs();
		xmlFile = new File(String.format(LEVEL_FILE_LOCATION + LEVEL_FILE_NAME, levelNumber));

		add(mapBuilder());
		add(buttonsBuilder(), BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);
		// setResizable(false);
		setTitle("Edit Level " + levelNumber);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setVisible(true);
	}

	/** Constructor for updating an existing level file.
	 * 
	 * @param xmlFile
	 *            File object of the XML file. */
	public MapEditor(File xmlFile)
	{
		this.xmlFile = xmlFile;
		// TODO: parse existing xml
	}

	/** Builds the JPanel with the grids and the borders.
	 * 
	 * @return JPanel with the grids and the borders. */
	private JPanel mapBuilder()
	{
		JPanel mapPanel = new JPanel();
		Dimension size = new Dimension(sizeX * (MapGrid.GRID_SIZE + BORDER) + 2 * (EDGE_SIZE + 2 * BORDER), sizeY * (MapGrid.GRID_SIZE + BORDER) + 2 * (EDGE_SIZE + 2 * BORDER));
		mapPanel.setPreferredSize(size);
		mapPanel.setMinimumSize(size);

		JPanel gridsPanel = gridsBuilder();
		BorderGrid northBorder = new BorderGrid(tracker, MapGridGroup.NORTH, sizeX);
		BorderGrid southBorder = new BorderGrid(tracker, MapGridGroup.SOUTH, sizeX);
		BorderGrid eastBorder = new BorderGrid(tracker, MapGridGroup.EAST, sizeY);
		BorderGrid westBorder = new BorderGrid(tracker, MapGridGroup.WEST, sizeY);

		GridBagLayout gbl = new GridBagLayout();
		mapPanel.setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();

		// Map
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(gridsPanel, gbc);

		// North border
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(northBorder, gbc);

		// South border
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(southBorder, gbc);

		// West border
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbl.setConstraints(westBorder, gbc);

		// East border
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbl.setConstraints(eastBorder, gbc);

		mapPanel.add(gridsPanel);
		// Add borders
		mapPanel.add(northBorder);
		mapPanel.add(southBorder);
		mapPanel.add(eastBorder);
		mapPanel.add(westBorder);

		return mapPanel;
	}

	private JPanel gridsBuilder()
	{
		JPanel grids = new JPanel();
		grids.setMinimumSize(new Dimension(
		// Account for border
		sizeX * (MapGrid.GRID_SIZE + BORDER), sizeY * (MapGrid.GRID_SIZE + BORDER)));

		GridBagLayout gbl = new GridBagLayout();
		grids.setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();

		for (int i = 0; i < sizeX / 2; i++)
		{
			for (int j = 0; j < sizeY / 2; j++)
			{
				gbc.gridx = i;
				gbc.gridy = j;

				MapGridGroup g = new MapGridGroup(tracker, MapGridGroup.NON_ROAD, i, sizeY / 2 - 1 - j);
				gbl.setConstraints(g, gbc);
				grids.add(g);
				gridGroups[j][i] = g;
			}
		}

		return grids;
	}

	private JPanel buttonsBuilder()
	{
		JPanel buttons = new JPanel();

		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));

		final JButton carEditor = new JButton("Edit Cars");
		final JButton export = new JButton("Export");
		final JButton cancel = new JButton("Cancel");

		ActionListener clickListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == export)
					exportXML();
				else if (e.getSource() == cancel)
					dispose();
				else if (e.getSource() == carEditor)
				{
					// Update starting and ending points
					if (!populateStartsEnds()) {
						JOptionPane.showMessageDialog(null, "Can't have intersections on the edge!", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}

					int[][] startCoords = new int[0][];
					int[][] endCoords = new int[0][];
					startCoords = starts.toArray(startCoords);
					endCoords = ends.toArray(endCoords);
					new CarEditor(startCoords, endCoords);
				}
			}
		};

		carEditor.addActionListener(clickListener);
		export.addActionListener(clickListener);
		cancel.addActionListener(clickListener);
		buttons.add(carEditor);
		buttons.add(export);
		buttons.add(cancel);

		return buttons;
	}

	/** Populates the startsEnds array with the starting and ending coordinates. */
	private boolean populateStartsEnds()
	{
		starts.clear();
		ends.clear();
		return checkNorthStartsEnds() && checkSouthStartsEnds() && checkEastStartsEnds() && checkWestStartsEnds();
	}

	private boolean checkNorthStartsEnds()
	{
		// Look at north border
		for (int i = 0; i < sizeX / 2; i++)
		{
			String type = gridGroups[0][i].getType();
			// Naming convention difference. See MapGridGroup
			Pattern p = Pattern.compile("THREE_WAY_(?![^S]{3})");
			Matcher m = p.matcher(type);
			if (m.find() || type.equals(MapGridGroup.FOUR_WAY))
				return false;

			if (type.equals(MapGridGroup.STRAIGHT_NS) || type.startsWith("CORNER_N"))
			{
				// gridGroups doesn't account for the borders, but the XML uses
				// x = 0 as the left border, y = 0 as the bottom border.
				int x = 2 * i + 1;
				int y = sizeY + 1;
				starts.add(new int[]
				{ x, y });
				ends.add(new int[]
				{ x + 1, y });
			}
		}
		return true;
	}

	private boolean checkSouthStartsEnds()
	{
		// Look at south border
		for (int i = 0; i < sizeX / 2; i++)
		{
			String type = gridGroups[sizeY / 2 - 1][i].getType();
			// Naming convention difference. See MapGridGroup
			Pattern p = Pattern.compile("THREE_WAY_(?![^N]{3})");
			Matcher m = p.matcher(type);
			if (m.find() || type.equals(MapGridGroup.FOUR_WAY))
				return false;

			if (type.equals(MapGridGroup.STRAIGHT_NS) || type.startsWith("CORNER_S"))
			{
				// gridGroups doesn't account for the borders, but the XML uses
				// x = 0 as the left border, y = 0 as the bottom border.
				int x = 2 * i + 1;
				int y = 0;
				starts.add(new int[]
				{ x + 1, y });
				ends.add(new int[]
				{ x, y });
			}
		}
		return true;
	}

	private boolean checkEastStartsEnds()
	{
		// Look at east border
		for (int i = 0; i < sizeY / 2; i++)
		{
			String type = gridGroups[i][sizeX / 2 - 1].getType();
			// Naming convention difference. See MapGridGroup
			Pattern p = Pattern.compile("THREE_WAY_(?![^W]{3})");
			Matcher m = p.matcher(type);
			if (m.find() || type.equals(MapGridGroup.FOUR_WAY))
				return false;

			if (type.equals(MapGridGroup.STRAIGHT_EW) || type.matches("CORNER_.E"))
			{
				// gridGroups doesn't account for the borders, but the XML uses
				// x = 0 as the left border, y = 0 as the bottom border.
				int x = sizeX + 1;
				int y = sizeY - (2 * i + 1);
				starts.add(new int[]
				{ x, y + 1 });
				ends.add(new int[]
				{ x, y });
			}
		}
		return true;
	}

	private boolean checkWestStartsEnds()
	{
		// Look at west border
		for (int i = 0; i < sizeY / 2; i++)
		{
			String type = gridGroups[i][0].getType();
			// Naming convention difference. See MapGridGroup
			Pattern p = Pattern.compile("THREE_WAY_(?![^E]{3})");
			Matcher m = p.matcher(type);
			if (m.find() || type.equals(MapGridGroup.FOUR_WAY))
				return false;

			if (type.equals(MapGridGroup.STRAIGHT_EW) || type.matches("CORNER_.W"))
			{
				// gridGroups doesn't account for the borders, but the XML uses
				// x = 0 as the left border, y = 0 as the bottom border.
				int x = 0;
				int y = sizeY - (2 * i + 1);
				starts.add(new int[]
				{ x, y });
				ends.add(new int[]
				{ x, y + 1 });
			}
		}
		return true;
	}

	public void exportXML()
	{
		Document doc = null;
		try
		{
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			doc = f.newDocumentBuilder().newDocument();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		Element level = doc.createElement("level");
		level.setAttribute("lv", Integer.toString(levelNumber));

		Element map = doc.createElement("map");
		// Add the invisible border grids
		map.setAttribute("size_x", Integer.toString(sizeX + 2));
		map.setAttribute("size_y", Integer.toString(sizeY + 2));

		// Add start/end points
		boolean noIntersectionOnEdge = addStartEnd(doc, map);
		if (!noIntersectionOnEdge)
		{
			JOptionPane.showMessageDialog(null, "Can't have intersections on the edge! Lever is not saved.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		for (int i = 0; i < gridGroups.length; i++)
			for (int j = 0; j < gridGroups[0].length; j++)
				gridGroups[i][j].addElement(doc, map);

		level.appendChild(map);

		// TODO: car info

		doc.appendChild(level);
		// Write out
		Transformer transformer = null;
		try
		{
			transformer = TransformerFactory.newInstance().newTransformer();
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerFactoryConfigurationError e)
		{
			e.printStackTrace();
		}
		DOMSource source = new DOMSource(doc);
		try
		{
			PrintWriter pw = new PrintWriter(new File("foo.xml"));
			StreamResult stream = new StreamResult(pw);
			transformer.transform(source, stream);
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		// Export image
		File imageFile = new File(String.format("level%02d.png", levelNumber));
		try
		{
			ImageIO.write(getBufferedImage(), "PNG", imageFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/** Adds the START and END grid types to the XML by looking at the grid types
	 * of the outer grids. When there is an intersection on the edge, it does
	 * not add the starts and ends.
	 * 
	 * @param doc
	 *            The Document object to create the elements from.
	 * @param map
	 *            The Element object to add the elements to.
	 * @return True if there is no intersection on the edge, false if it does. */
	private boolean addStartEnd(Document doc, Element map)
	{
		if (!populateStartsEnds())
			return false;

		if (starts.size() != ends.size())
			return false;

		for (int i = 0; i < starts.size(); i++)
		{
			int x, y;
			// Add starting point
			x = starts.get(i)[0];
			y = starts.get(i)[1];
			Element start = doc.createElement("grid");
			start.setAttribute("x", Integer.toString(x));
			start.setAttribute("y", Integer.toString(y));
			Element ts = doc.createElement("type");
			ts.appendChild(doc.createTextNode(getStartEndType(x, y, true)));
			start.appendChild(ts);

			// Add ending point
			x = ends.get(i)[0];
			y = ends.get(i)[1];
			Element end = doc.createElement("grid");
			end.setAttribute("x", Integer.toString(x));
			end.setAttribute("y", Integer.toString(y));
			Element te = doc.createElement("type");
			te.appendChild(doc.createTextNode(getStartEndType(x, y, false)));
			end.appendChild(te);

			map.appendChild(start);
			map.appendChild(end);
		}

		return true;
	}

	private String getStartEndType(int x, int y, boolean isStart)
	{
		int maxX = sizeX + 1;
		int maxY = sizeY + 1;

		if (x == 0)
		{
			if (isStart)
				return MapGrid.START_WEDGE2E;
			else
				return MapGrid.END_W2WEDGE;
		}
		else if (y == 0)
		{
			if (isStart)
				return MapGrid.START_SEDGE2N;
			else
				return MapGrid.END_S2SEDGE;
		}
		else if (x == maxX)
		{
			if (isStart)
				return MapGrid.START_EEDGE2W;
			else
				return MapGrid.END_E2EEDGE;
		}
		else if (y == maxY)
		{
			if (isStart)
				return MapGrid.START_NEDGE2S;
			else
				return MapGrid.END_N2NEDGE;
		}

		// Shouldn't come here
		return null;
	}

	public BufferedImage getBufferedImage()
	{
		int imageSize = gridGroups[0][0].getImageSize();
		BufferedImage map = new BufferedImage(sizeX / 2 * imageSize, sizeY / 2 * imageSize, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = map.createGraphics();
		for (int i = 0; i < sizeX / 2; i++)
		{
			for (int j = 0; j < sizeY / 2; j++)
			{
				int xCoord = i * imageSize;
				int yCoord = j * imageSize;

				g.drawImage(gridGroups[j][i].getBufferedImage(), xCoord, yCoord, this);
			}
		}

		return map;
	}

	/* Only for development */
	public static void main(String[] args)
	{
		// new MapEditor(2, 10, 12);
		new MapEditor(2, 20, 20);
		// new MapEditor(2, 6, 8);
	}
}
