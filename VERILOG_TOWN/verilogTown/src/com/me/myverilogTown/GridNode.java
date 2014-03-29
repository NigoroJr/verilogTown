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

    public GridType getType() {
        return grid_type;
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

    public void setNorth(GridNode dest) {
        this.north = dest;
    }

    public void setSouth(GridNode dest) {
        this.south = dest;
    }

    public void setWest(GridNode dest) {
        this.west = dest;
    }

    public void setEast(GridNode dest) {
        this.east = dest;
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