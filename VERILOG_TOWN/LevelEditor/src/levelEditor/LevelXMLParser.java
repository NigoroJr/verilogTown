package levelEditor;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LevelXMLParser
{
	private int					levelNumber;
	private int					mapSizeX;
	private int					mapSizeY;
	private String[][]			grids;
	private ArrayList<int[]>	starts;
	private ArrayList<int[]>	ends;
	private ArrayList<Car>		cars;

	public LevelXMLParser(File xmlFile)
	{
		this.starts = new ArrayList<int[]>();
		this.ends = new ArrayList<int[]>();
		this.cars = new ArrayList<Car>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try
		{
			builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		doc.getDocumentElement().normalize();

		NodeList level = doc.getElementsByTagName("level");
		NodeList mapProperties = level.item(0).getChildNodes();
		Node map = null;
		Node cars = null;
		for (int i = 0; i < mapProperties.getLength(); i++)
		{
			Node node = mapProperties.item(i);

			if (node.getNodeName().equals("map"))
				map = node;
			else if (node.getNodeName().equals("cars"))
				cars = node;
		}

		this.levelNumber = Integer.parseInt(level.item(0).getAttributes().getNamedItem("lv").getTextContent());

		// Get size of the map
		mapSizeX = Integer.parseInt(map.getAttributes().getNamedItem("size_x").getTextContent());
		mapSizeY = Integer.parseInt(map.getAttributes().getNamedItem("size_y").getTextContent());

		// Set appropriate grids size in MapEditor once reading in the XML
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int optimalGridWidth = (screenSize.width - 2 * MapEditor.EDGE_SIZE) / mapSizeX;
		// Account for the task bar, title bar, and buttons
		int optimalGridHeight = (screenSize.height - 2 * MapEditor.EDGE_SIZE - 200) / mapSizeY;
		int min = Math.min(optimalGridWidth, optimalGridHeight);
		if (min < MapGrid.DEFAULT_GRID_SIZE)
			MapGrid.GRID_SIZE = min;
		else
			MapGrid.GRID_SIZE = MapGrid.DEFAULT_GRID_SIZE;

		grids = new String[mapSizeX - 2][mapSizeY - 2];
		initGrids(grids);

		readMap(map.getChildNodes());

		readCars(cars.getChildNodes());
	}

	/** Initializes all the grids to NON_ROADS so that there will be no null grid
	 * after reading the XML.
	 * 
	 * @param grids */
	private void initGrids(String[][] grids)
	{
		for (int x = 0; x < mapSizeX - 2; x++)
			for (int y = 0; y < mapSizeY - 2; y++)
				grids[x][y] = MapGrid.NON_ROAD;
	}

	private void readMap(NodeList map)
	{
		for (int i = 0; i < map.getLength(); i++)
		{

			Node grid = map.item(i);

			if (grid.getNodeType() != Node.ELEMENT_NODE)
				continue;

			int x = Integer.parseInt(grid.getAttributes().getNamedItem("x").getTextContent());
			int y = Integer.parseInt(grid.getAttributes().getNamedItem("y").getTextContent());

			NodeList childrenOfGrid = grid.getChildNodes();
			processGrid(childrenOfGrid, x, y);
		}
	}

	private void processGrid(NodeList childrenOfGrid, int x, int y)
	{
		// Iterate through child nodes of <grid>
		for (int j = 0; j < childrenOfGrid.getLength(); j++)
		{
			Node type = childrenOfGrid.item(j);

			if (type.getNodeType() == Node.ELEMENT_NODE && type.getNodeName().equals("type"))
			{
				String gridType = type.getTextContent();
				if (gridType.startsWith("START"))
					starts.add(new int[]
					{ x, y });
				else if (gridType.startsWith("END"))
					ends.add(new int[]
					{ x, y });
				else
					// -1 from both because the x and y coordinates that are
					// written in the XML accounts for the borders. However, we
					// just want to consider the insides so make the bottom-left
					// grid (x, y) = (0, 0)
					grids[x - 1][y - 1] = gridType;
			}
		}
	}

	private void readCars(NodeList cars)
	{
		for (int i = 0; i < cars.getLength(); i++)
		{
			Node car = cars.item(i);

			if (car.getNodeType() != Node.ELEMENT_NODE)
				continue;

			Node start = null;
			Node end = null;
			Node delay = null;
			for (int j = 0; j < car.getChildNodes().getLength(); j++)
			{
				Node n = car.getChildNodes().item(j);
				if (n.getNodeName().equals("start"))
					start = n;
				else if (n.getNodeName().equals("end"))
					end = n;
				else if (n.getNodeName().equals("delay"))
					delay = n;
			}

			int s_x = Integer.parseInt(start.getAttributes().getNamedItem("x").getTextContent());
			int s_y = Integer.parseInt(start.getAttributes().getNamedItem("y").getTextContent());
			int e_x = Integer.parseInt(end.getAttributes().getNamedItem("x").getTextContent());
			int e_y = Integer.parseInt(end.getAttributes().getNamedItem("y").getTextContent());
			int delayOffset = Integer.parseInt(delay.getTextContent());

			/* CarEditor can be null because it gets updated as soon as it goes
			 * into the CarEditor constructor. Also, allStarts and allEnds are
			 * empty because they are regenerated when creating a new CarEditor
			 * object. */
			Car c = new Car(null, i, starts.toArray(new int[][]
			{}), ends.toArray(new int[][]
			{}), new int[]
			{ s_x, s_y }, new int[]
			{ e_x, e_y }, delayOffset);
			this.cars.add(c);
		}
	}

	public int getLevelNumber()
	{
		return levelNumber;
	}

	/** Returns the size of the map EXCLUDING the borders.
	 * 
	 * @return Number of grids in a row excluding the borders. */
	public int getSizeX()
	{
		return mapSizeX - 2;
	}

	/** Returns the size of the map EXCLUDING the borders.
	 * 
	 * @return Number of grids in a column excluding the borders. */
	public int getSizeY()
	{
		return mapSizeY - 2;
	}

	public MapGridGroup[][] getGridGroups()
	{
		MapGridGroup[][] gridGroups = new MapGridGroup[getSizeX() / 2][getSizeY() / 2];
		for (int x = 0; x < getSizeX() / 2; x++)
		{
			for (int y = 0; y < getSizeY() / 2; y++)
			{
				String gridGroupType = getGridGroupType(grids[2 * x][2 * y + 1], grids[2 * x + 1][2 * y + 1], grids[2 * x][2 * y], grids[2 * x + 1][2 * y]);
				gridGroups[x][y] = new MapGridGroup(null, gridGroupType, x, y);
			}
		}
		return gridGroups;
	}

	private String getGridGroupType(
			String topLeft,
			String topRight,
			String bottomLeft,
			String bottomRight)
	{
		String gridGroupType = "";
		// Straight
		if (topLeft.equals(MapGrid.STRAIGHT_ROAD_S2S) && topRight.equals(MapGrid.STRAIGHT_ROAD_N2N) && bottomLeft.equals(MapGrid.STRAIGHT_ROAD_S2S) && bottomRight.equals(MapGrid.STRAIGHT_ROAD_N2N))
		{
			gridGroupType = MapGridGroup.STRAIGHT_NS;
		}
		else if (topLeft.equals(MapGrid.STRAIGHT_ROAD_W2W) && topRight.equals(MapGrid.STRAIGHT_ROAD_W2W) && bottomLeft.equals(MapGrid.STRAIGHT_ROAD_E2E) && bottomRight.equals(MapGrid.STRAIGHT_ROAD_E2E))
		{
			gridGroupType = MapGridGroup.STRAIGHT_EW;
		}
		// Corner
		else if (topLeft.equals(MapGrid.CORNER_ROAD_S2W) && topRight.equals(MapGrid.STRAIGHT_ROAD_N2N) && bottomLeft.equals(MapGrid.STRAIGHT_ROAD_E2E) && bottomRight.equals(MapGrid.CORNER_ROAD_E2N))
		{
			gridGroupType = MapGridGroup.CORNER_NW;
		}
		else if (topLeft.equals(MapGrid.STRAIGHT_ROAD_S2S) && topRight.equals(MapGrid.CORNER_ROAD_W2N) && bottomLeft.equals(MapGrid.CORNER_ROAD_S2E) && bottomRight.equals(MapGrid.STRAIGHT_ROAD_E2E))
		{
			gridGroupType = MapGridGroup.CORNER_NE;
		}
		else if (topLeft.equals(MapGrid.STRAIGHT_ROAD_W2W) && topRight.equals(MapGrid.CORNER_ROAD_N2W) && bottomLeft.equals(MapGrid.CORNER_ROAD_E2S) && bottomRight.equals(MapGrid.STRAIGHT_ROAD_N2N))
		{
			gridGroupType = MapGridGroup.CORNER_SW;
		}
		else if (topLeft.equals(MapGrid.CORNER_ROAD_W2S) && topRight.equals(MapGrid.STRAIGHT_ROAD_W2W) && bottomLeft.equals(MapGrid.STRAIGHT_ROAD_S2S) && bottomRight.equals(MapGrid.CORNER_ROAD_N2E))
		{
			gridGroupType = MapGridGroup.CORNER_SE;
		}
		// Three-way
		else if (topLeft.equals(MapGrid.INTER_TURN_S2WS) && topRight.equals(MapGrid.INTER_TURN_W2NW) && bottomLeft.equals(MapGrid.INTER_TURN_E2SE) && bottomRight.equals(MapGrid.STRAIGHT_ROAD_N2N))
		{
			gridGroupType = MapGridGroup.THREE_WAY_NSW;
		}
		else if (topLeft.equals(MapGrid.INTER_TURN_S2WS) && topRight.equals(MapGrid.INTER_TURN_W2NW) && bottomLeft.equals(MapGrid.STRAIGHT_ROAD_E2E) && bottomRight.equals(MapGrid.INTER_TURN_N2EN))
		{
			gridGroupType = MapGridGroup.THREE_WAY_NEW;
		}
		else if (topLeft.equals(MapGrid.INTER_TURN_S2WS) && topRight.equals(MapGrid.STRAIGHT_ROAD_W2W) && bottomLeft.equals(MapGrid.INTER_TURN_E2SE) && bottomRight.equals(MapGrid.INTER_TURN_N2EN))
		{
			gridGroupType = MapGridGroup.THREE_WAY_SEW;
		}
		else if (topLeft.equals(MapGrid.STRAIGHT_ROAD_S2S) && topRight.equals(MapGrid.INTER_TURN_W2NW) && bottomLeft.equals(MapGrid.INTER_TURN_E2SE) && bottomRight.equals(MapGrid.INTER_TURN_N2EN))
		{
			gridGroupType = MapGridGroup.THREE_WAY_NSE;
		}
		// Four-way
		else if (topLeft.equals(MapGrid.INTER_TURN_S2WS) && topRight.equals(MapGrid.INTER_TURN_W2NW) && bottomLeft.equals(MapGrid.INTER_TURN_E2SE) && bottomRight.equals(MapGrid.INTER_TURN_N2EN))
		{
			gridGroupType = MapGridGroup.FOUR_WAY;
		}
		else
		{
			gridGroupType = MapGridGroup.NON_ROAD;
		}

		return gridGroupType;
	}

	public ArrayList<int[]> getStarts()
	{
		return starts;
	}

	public ArrayList<int[]> getEnds()
	{
		return ends;
	}

	public ArrayList<Car> getCars()
	{
		return cars;
	}
}
