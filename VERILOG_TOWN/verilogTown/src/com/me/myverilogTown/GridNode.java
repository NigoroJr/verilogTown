package com.me.myverilogTown;

public class GridNode {
    /* Bottom-left is (x, y) = (0, 0) */
    private int x;
    private int y;

    private GridType grid_type;
    private GridNode north;
    private GridNode south;
    private GridNode east;
    private GridNode west;

    private TrafficControl signal;
    private int signal_index;

    /* Mark for BFS. Always greater than 0 */
    private int visited_mark = 0;
    /* Node that visited this node. Used for reverse traversal */
    private GridNode visited_by = null;

    /* Constructor */
    public GridNode(int x, int y, GridType type) {
        this.x = x;
        this.y = y;
        this.grid_type = type;
        this.north = null;
        this.south = null;
        this.east = null;
        this.west = null;

        this.signal = null;
        this.signal_index = -1;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getVisitedCount() {
        return this.visited_mark;
    }

    public void setVisited(int mark, GridNode by) {
        this.visited_mark = mark;
        this.visited_by = by;
    }

    public GridNode getVisitedBy() {
        return this.visited_by;
    }

    public boolean isAlreadyVisited(int markPathCount) {
        return markPathCount == this.visited_mark;
    }

    /**
     * Sets the grid that this grid points to. In other words, sets the next
     * grid that cars on this grid can move to.
     * 
     * @param dest
     *            The next movable grid from this grid.
     */
    public void setDestination(GridNode dest) {
        switch (grid_type) {
            // Grids that go North
            case START_SEDGE2N:
            case STRAIGHT_ROAD_N2N:
            case CORNER_ROAD_E2N:
            case CORNER_ROAD_W2N:
                this.north = dest;
                break;
            // Grids that go South
            case START_NEDGE2S:
            case STRAIGHT_ROAD_S2S:
            case CORNER_ROAD_W2S:
            case CORNER_ROAD_E2S:
                this.south = dest;
                break;
            // Grids that go West
            case START_EEDGE2W:
            case STRAIGHT_ROAD_W2W:
            case CORNER_ROAD_N2W:
            case CORNER_ROAD_S2W:
                this.west = dest;
                break;
            // Grids that go East
            case START_WEDGE2E:
            case STRAIGHT_ROAD_E2E:
            case CORNER_ROAD_N2E:
            case CORNER_ROAD_S2E:
                this.east = dest;
                break;
            // END grids and intersections
            default:
                break;
        }
    }

    /**
     * Sets the next movable grid for intersections.
     * 
     * @param dest1
     *            First movable grid from this grid.
     * @param dest2
     *            Second movable grid from this grid.
     */
    public void setDestination(GridNode dest1, GridNode dest2) {
        switch (grid_type) {
            case INTER_TURN_S2WS:
                this.west = dest1;
                this.south = dest2;
                break;
            case INTER_TURN_N2EN:
                this.east = dest1;
                this.north = dest2;
                break;
            case INTER_TURN_E2SE:
                this.south = dest1;
                this.east = dest2;
                break;
            case INTER_TURN_W2NW:
                this.north = dest1;
                this.west = dest2;
                break;
            default:
                break;
        }
    }

    /**
     * Returns the grid north of this grid.
     * 
     * @return The grid in the north.
     */
    public GridNode getNorth() {
        return this.north;
    }

    /**
     * Returns the grid south of this grid.
     * 
     * @return The grid in the south.
     */
    public GridNode getSouth() {
        return this.south;
    }

    /**
     * Returns the grid east of this grid.
     * 
     * @return The grid in the east.
     */
    public GridNode getEast() {
        return this.east;
    }

    /**
     * Returns the grid west of this grid.
     * 
     * @return The grid in the west.
     */
    public GridNode getWest() {
        return this.west;
    }

    /* hooks up the associated traffic signal */
    public void addTrafficSignal(TrafficControl traffic_signal, int index) {
        this.signal = traffic_signal;
        this.signal_index = index;
    }
}