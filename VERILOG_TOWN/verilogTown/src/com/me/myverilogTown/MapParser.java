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
import java.util.ArrayList;
import java.util.Collections;

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

    private int levelNumber;
    private GridNode grids[][];
    private ArrayList<TrafficControl> trafficSignals = new ArrayList<TrafficControl>();
    private ArrayList<Intersection> intersections = new ArrayList<Intersection>();
    private Car[] cars;

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
        NodeList mapProperties = level.item(0).getChildNodes();
        Node map = null;
        Node cars = null;
        for (int i = 0; i < mapProperties.getLength(); i++) {
            Node node = mapProperties.item(i);

            if (node.getNodeName().equals("map"))
                map = node;
            else if (node.getNodeName().equals("cars"))
                cars = node;
        }

        this.levelNumber = Integer.parseInt(level.item(0).getAttributes()
                .getNamedItem("lv").getTextContent());

        // Get size of the map
        int mapSizeX = Integer.parseInt(map.getAttributes()
                .getNamedItem("size_x").getTextContent());
        int mapSizeY = Integer.parseInt(map.getAttributes()
                .getNamedItem("size_y").getTextContent());
        grids = new GridNode[mapSizeX][mapSizeY];

        readMap(map.getChildNodes());

        // Relate nodes by setting destinations
        for (int i = 0; i < grids.length; i++)
            for (int j = 0; j < grids[0].length; j++)
                setDestination(grids[i][j]);

        // After setting all the grids, set the intersections
        Collections.sort(intersections);
        for (int i = 0; i < intersections.size(); i++) {
            Intersection inter = intersections.get(i);
            setIntersection(inter.type, inter.x, inter.y);
        }

        readCars(cars.getChildNodes());
    }

    /**
     * Reads the "map" tag in the XML file.
     * 
     * @param map
     *            The child nodes of the map tag.
     */
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

            processGrid(childrenOfGrid, x, y);
        }
    }

    public void readCars(NodeList cars) {
        ArrayList<Car> carsList = new ArrayList<Car>();
        for (int i = 0; i < cars.getLength(); i++) {
            Node car = cars.item(i);

            if (car.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Node start = null;
            Node end = null;
            Node delay = null;
            for (int j = 0; j < car.getChildNodes().getLength(); j++) {
                Node n = car.getChildNodes().item(j);
                if (n.getNodeName().equals("start"))
                    start = n;
                else if (n.getNodeName().equals("end"))
                    end = n;
                else if (n.getNodeName().equals("delay"))
                    delay = n;
            }

            int s_x = Integer.parseInt(start.getAttributes()
                    .getNamedItem("x").getTextContent());
            int s_y = Integer.parseInt(start.getAttributes()
                    .getNamedItem("y").getTextContent());
            int e_x = Integer.parseInt(end.getAttributes().getNamedItem("x")
                    .getTextContent());
            int e_y = Integer.parseInt(end.getAttributes().getNamedItem("y")
                    .getTextContent());
            int delayOffset = Integer.parseInt(delay.getTextContent());

            Car c = new Car(grids[s_x][s_y], grids[e_x][e_y], delayOffset);
            carsList.add(c);
        }

        this.cars = carsList.toArray(new Car[0]);
    }

    /**
     * Processes elements of grid tags.
     * 
     * @param childrenOfGrid
     *            Elements of grid tag.
     * @param x
     *            The x coordinate of the grid.
     * @param y
     *            The y coordinate of the grid.
     */
    private void processGrid(NodeList childrenOfGrid, int x, int y) {
        // Iterate through child nodes of <grid>
        for (int j = 0; j < childrenOfGrid.getLength(); j++) {
            Node type = childrenOfGrid.item(j);

            if (type.getNodeType() == Node.ELEMENT_NODE
                    && type.getNodeName().equals("type")) {
                String gridType = type.getTextContent();

                grids[x][y] = new GridNode(x, y, getGridType(gridType));
            }
            else if (type.getNodeType() == Node.ELEMENT_NODE
                    && type.getNodeName().equals("intersection")) {
                String intersectionType = type.getTextContent();

                // Don't set intersections yet because not all the grids hasn't
                // been instantiated
                intersections.add(new Intersection(intersectionType, x, y));
            }
        }
    }

    /**
     * Given an intersection, this method sets the entering points of the
     * intersection. There are either 3 (for three-way intersections) or 4
     * (four-way) entering points. An intersection is represented by the
     * top-left grid of the four grids as shown in the figure below. The four
     * arrows represents the direction in which the cars are entering.
     * 
     * <pre>
     * <code>
     *    ||| |
     *    |v| |
     * ---+-+-+---
     *    |X| |<-
     * ---+-+-+---
     * -> | | |
     * ---+-+-+---
     *    | |^|
     *    | |||
     * </code>
     * </pre>
     * 
     * @param intersectionType
     *            The type of the intersection. For example, THREE_WAY_NSE means
     *            that the intersection is three-way, with cars <em>leaving</em>
     *            the intersection going north, south, and east.
     * @param x
     *            The x coordinate of the intersection.
     * @param y
     */
    private void setIntersection(String intersectionType, int x, int y) {
        TrafficControl signal = new TrafficControl();
        /* Traffic going North */
        GridNode n = grids[x + 1][y - 2];
        /* Traffic going South */
        GridNode s = grids[x][y + 1];
        /* Traffic going East */
        GridNode e = grids[x - 1][y - 1];
        /* Traffic going West */
        GridNode w = grids[x + 2][y];
        switch (intersectionType) {
            case "FOUR_WAY":
                signal.setNSEW(n, s, e, w);
                break;
            case "THREE_WAY_NSE":
                signal.setNSE(n, s, e);
                break;
            case "THREE_WAY_SEW":
                signal.setSEW(s, e, w);
                break;
            case "THREE_WAY_NSW":
                signal.setNSW(n, s, w);
                break;
            case "THREE_WAY_NEW":
                signal.setNEW(n, e, w);
                break;
        }
        trafficSignals.add(signal);
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
     * Takes in the String and returns a GridType.
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

    /**
     * Returns the level number of this map.
     * 
     * @return The level number of this map.
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Returns the map parsed from the XML file.
     * 
     * @return The map as a list of GridNode.
     */
    public GridNode[][] getGridArray() {
        return grids;
    }

    /**
     * Returns an array of all the intersections in this map.
     * 
     * @return The list of intersections.
     */
    public TrafficControl[] getTrafficControls() {
        TrafficControl[] ret = new TrafficControl[0];
        return trafficSignals.toArray(ret);
    }

    /**
     * Returns the array of the cars.
     * 
     * @return The array of the cars that appear in this lever.
     */
    public Car[] getCars() {
        return cars;
    }

    /**
     * Tiny class that holds information about an intersection while parser
     * reads the XML. Later, after all the grids are read, grids are hooked up
     * to the intersections.
     * 
     * @author Naoki
     * 
     */
    private class Intersection implements Comparable<Intersection> {
        String type;
        int x;
        int y;

        Intersection(String type, int x, int y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }

        /**
         * Order the intersections from top-left to bottom-right.
         * Note that top-left is [map.length - 1][0] and bottom-left is [0][0]
         */
        @Override
        public int compareTo(Intersection that) {
            if (that.y == this.y)
                return this.x - that.x;
            else
                return that.y - this.y;
        }
    }
}
