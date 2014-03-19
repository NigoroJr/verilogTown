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
    public static final String fileName = "test.xml";

    private verilogTownGridNode gridArray[][];

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

        NodeList map = doc.getElementsByTagName("map");
        int mapSizeX = Integer.parseInt(map.item(0).getAttributes()
                .getNamedItem("size_x").getTextContent());
        int mapSizeY = Integer.parseInt(map.item(0).getAttributes()
                .getNamedItem("size_y").getTextContent());

        gridArray = new verilogTownGridNode[mapSizeX][mapSizeY];

        readMap(map);
    }

    public void readMap(NodeList map) {
        for (int i = 0; i < map.getLength(); i++) {

            Node grid = map.item(i);
            NodeList childrenOfGrid = grid.getChildNodes();

            // Iterate through child nodes of <grid>
            for (int j = 0; j < childrenOfGrid.getLength(); j++) {
                Node type = childrenOfGrid.item(j);

                if (type.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) type;

                    int x = Integer.parseInt(element.getAttribute("x"));
                    int y = Integer.parseInt(element.getAttribute("y"));
                    String gridType = element.getElementsByTagName("type")
                            .item(0).getTextContent();

                    gridArray[x][y] =
                            new verilogTownGridNode(x, y, getGridType(gridType));
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

    public verilogTownGridNode[][] getGridArray() {
        return gridArray;
    }
}
