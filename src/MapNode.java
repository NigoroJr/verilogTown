public class MapNode {
    private int x;
    private int y;
    private MapNode north;
    private MapNode south;
    private MapNode east;
    private MapNode west;

    MapNode(int x, int y) {
        this.x = x;
        this.y = y;
        north = null;
        south = null;
        east = null;
        west = null;
    }

    // There's probably a more elegant way to do this..
    void addRoad(String dir, MapNode dest) {
        if (dir.equals("north"))
            this.north = dest;
        if (dir.equals("south"))
            this.south = dest;
        if (dir.equals("east"))
            this.east = dest;
        if (dir.equals("west"))
            this.west = dest;
    }
}
