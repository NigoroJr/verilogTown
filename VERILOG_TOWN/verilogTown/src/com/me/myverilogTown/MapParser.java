package com.me.myverilogTown;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class MapParser {
    /*
     * The CWD is...
     * 
     * |
     * +-- verilogTown
     * | |
     * | +-- HERE!
     * |
     * +-- verilogTown-android
     * |
     * +-- ...
     */
    public static final String fileName = "../../samples/first_map.xml";

    private GridNode grids[][];

    public MapParser() {
        this(fileName);
    }

    public MapParser(String fileName) {
        File file = new File(fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(file);
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // optional, but recommended
        // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        NodeList level = doc.getElementsByTagName("level");
        Node map = level.item(0).getChildNodes().item(1);
        Node intersection = level.item(0).getChildNodes().item(3);
        Node car = level.item(0).getChildNodes().item(5);

        int levelNum = Integer.parseInt(level.item(0).getAttributes()
                .getNamedItem("lv").getTextContent());

        // Get size of the map
        int mapSizeX = Integer.parseInt(map.getAttributes()
                .getNamedItem("size_x").getTextContent());
        int mapSizeY = Integer.parseInt(map.getAttributes()
                .getNamedItem("size_y").getTextContent());
        grids = new GridNode[mapSizeX][mapSizeY];

        // TODO: init() to NON_ROAD??
        readMap(map.getChildNodes());

        // Relate nodes by setting destinations
        for (int i = 0; i < grids.length; i++)
            for (int j = 0; j < grids[0].length; j++)
                setDestination(grids[i][j]);

        // DEBUG
        for (int j = 0; j < grids[0].length; j++) {
            for (int i = 0; i < grids.length; i++) {
                System.out.print(grids[i][j].getType().toString().substring(0, 1));
            }
            System.out.println();
        }
    }

    public void readMap(NodeList map) {
        for (int i = 0; i < map.getLength(); i++) {

            Node grid = map.item(i);

            if (grid.getNodeType() != Node.ELEMENT_NODE)
                continue;

            int x = Integer.parseInt(grid.getAttributes()
                    .getNamedItem("x").getTextContent());
            int y = Integer.parseInt(grid.getAttributes().
                    getNamedItem("y").getTextContent());

            NodeList childrenOfGrid = grid.getChildNodes();

            // Iterate through child nodes of <grid>
            for (int j = 0; j < childrenOfGrid.getLength(); j++) {
                Node type = childrenOfGrid.item(j);

                if (type.getNodeType() == Node.ELEMENT_NODE) {
                    String gridType = type.getTextContent();

                    grids[x][y] =
                            new GridNode(x, y, getGridType(gridType));
                }
            }
        }
    }

    /**
     * Sets the destination of the given node, according to its type. This
     * method assumes that the grids array is populated with nodes read from the
     * XML file.
     * 
     * @param node
     *            Node to set the destination.
     */
    public void setDestination(GridNode node) {
        int x = node.getX();
        int y = node.getY();

        // Grids other than END grids and intersections
        switch (node.getType()) {
            default:
                break;
            // Grids that go North
            case START_SEDGE2N:
            case STRAIGHT_ROAD_N2N:
            case CORNER_ROAD_E2N:
            case CORNER_ROAD_W2N:
                node.setNorth(grids[x][y + 1]);
                break;
            // Grids that go South
            case START_NEDGE2S:
            case STRAIGHT_ROAD_S2S:
            case CORNER_ROAD_W2S:
            case CORNER_ROAD_E2S:
                node.setSouth(grids[x][y - 1]);
                break;
            // Grids that go West
            case START_EEDGE2W:
            case STRAIGHT_ROAD_W2W:
            case CORNER_ROAD_N2W:
            case CORNER_ROAD_S2W:
                node.setWest(grids[x - 1][y]);
                break;
            // Grids that go East
            case START_WEDGE2E:
            case STRAIGHT_ROAD_E2E:
            case CORNER_ROAD_N2E:
            case CORNER_ROAD_S2E:
                node.setEast(grids[x + 1][y]);
                break;
        }

        // Intersections have 2 possible destinations
        switch (node.getType()) {
            case INTER_TURN_S2WS:
                node.setWest(grids[x - 1][y]);
                node.setSouth(grids[x][y - 1]);
                break;
            case INTER_TURN_N2EN:
                node.setEast(grids[x + 1][y]);
                node.setNorth(grids[x][y + 1]);
                break;
            case INTER_TURN_E2SE:
                node.setSouth(grids[x][y - 1]);
                node.setEast(grids[x + 1][y]);
                break;
            case INTER_TURN_W2NW:
                node.setNorth(grids[x][y + 1]);
                node.setWest(grids[x - 1][y]);
                break;
            default:
                break;
        }
    }

    /**
     * Takes in the String and returns a GridType
     * 
     * @param gridType
     *            The type of the grid in String.
     * @return Type of the grid in GridType.
     */
    public GridType getGridType(String gridType) {
        GridType ret = GridType.NON_ROAD;
        switch (gridType) {
            case "START_NEDGE2S":
                ret = GridType.START_NEDGE2S;
                break;
            case "START_SEDGE2N":
                ret = GridType.START_SEDGE2N;
                break;
            case "START_EEDGE2W":
                ret = GridType.START_EEDGE2W;
                break;
            case "START_WEDGE2E":
                ret = GridType.START_WEDGE2E;
                break;

            // End
            case "END_S2SEDGE":
                ret = GridType.END_S2SEDGE;
                break;
            case "END_N2NEDGE":
                ret = GridType.END_N2NEDGE;
                break;
            case "END_W2WEDGE":
                ret = GridType.END_W2WEDGE;
                break;
            case "END_E2EEDGE":
                ret = GridType.END_E2EEDGE;
                break;

            // Straight roads
            case "STRAIGHT_ROAD_N2N":
                ret = GridType.STRAIGHT_ROAD_N2N;
                break;
            case "STRAIGHT_ROAD_S2S":
                ret = GridType.STRAIGHT_ROAD_S2S;
                break;
            case "STRAIGHT_ROAD_E2E":
                ret = GridType.STRAIGHT_ROAD_E2E;
                break;
            case "STRAIGHT_ROAD_W2W":
                ret = GridType.STRAIGHT_ROAD_W2W;
                break;

            // Corners
            case "CORNER_ROAD_W2S":
                ret = GridType.CORNER_ROAD_W2S;
                break;
            case "CORNER_ROAD_N2E":
                ret = GridType.CORNER_ROAD_N2E;
                break;
            case "CORNER_ROAD_E2S":
                ret = GridType.CORNER_ROAD_E2S;
                break;
            case "CORNER_ROAD_N2W":
                ret = GridType.CORNER_ROAD_N2W;
                break;
            case "CORNER_ROAD_S2W":
                ret = GridType.CORNER_ROAD_S2W;
                break;
            case "CORNER_ROAD_E2N":
                ret = GridType.CORNER_ROAD_E2N;
                break;
            case "CORNER_ROAD_S2E":
                ret = GridType.CORNER_ROAD_S2E;
                break;
            case "CORNER_ROAD_W2N":
                ret = GridType.CORNER_ROAD_W2N;
                break;

            // Intersections
            case "INTER_TURN_N2EN":
                ret = GridType.INTER_TURN_N2EN;
                break;
            case "INTER_TURN_W2NW":
                ret = GridType.INTER_TURN_W2NW;
                break;
            case "INTER_TURN_S2WS":
                ret = GridType.INTER_TURN_S2WS;
                break;
            case "INTER_TURN_E2SE":
                ret = GridType.INTER_TURN_E2SE;
                break;

            // Non-roads
            case "NON_ROAD":
                ret = GridType.NON_ROAD;
                break;
        }

        return ret;
    }

    public GridNode[][] getGridArray() {
        return grids;
    }
}
