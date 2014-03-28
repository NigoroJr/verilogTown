package com.me.myverilogTown;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
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
    public static final String fileName = "../../samples/sample_map.xml";

    private VerilogTownGridNode gridArray[][];

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
        // NodeList map = doc.getElementsByTagName("map");
        Node map = level.item(0).getChildNodes().item(1);
        Node car = level.item(0).getChildNodes().item(3);

        int levelNum = Integer.parseInt(level.item(0).getAttributes()
                .getNamedItem("lv").getTextContent());

        // Get size of the map
        int mapSizeX = Integer.parseInt(map.getAttributes()
                .getNamedItem("size_x").getTextContent());
        int mapSizeY = Integer.parseInt(map.getAttributes()
                .getNamedItem("size_y").getTextContent());
        gridArray = new VerilogTownGridNode[mapSizeX][mapSizeY];

        readMap(map.getChildNodes());

        for (int i = 0; i < gridArray.length; i++) {
            for (int j = 0; j < gridArray[0].length; j++) {
                System.out.print(gridArray[i][j].get_x());
            }
            System.out.println();
        }
    }

    public void readMap(NodeList map) {
        for (int i = 0; i < map.getLength(); i++) {

            Node grid = map.item(i);

            if (grid.getNodeType() == Node.TEXT_NODE)
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

                    gridArray[x][y] =
                            new VerilogTownGridNode(x, y, getGridType(gridType));
                }
            }
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

    public VerilogTownGridNode[][] getGridArray() {
        return gridArray;
    }
}
