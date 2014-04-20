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

    private TrafficControl signal = null;;
    private int signal_index = -1;

    private Car car = null;

    /* Mark for BFS. Always greater than 0 */
    private int visited_mark = 0;
    /* Node that visited this node. Used for reverse traversal */
    private GridNode visited_by = null;

    /* Constructor */
    public GridNode(int x, int y, GridType type) {
        this.x = x;
        this.y = y;
        this.grid_type = type;
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

    /**
     * Returns whether this grid is a traffic signal or not.
     * 
     * @return True if this grid is a traffic signal, false if not.
     */
    public boolean isTrafficSignal() {
        return signal != null;
    }

    /**
     * Sets a car on this grid.
     * 
     * @param car
     *            The car that is currently on this grid.
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * Returns the car that is currently on the grid.
     * 
     * @return The car that is currently on this grid.
     */
    public Car getCar() {
        return car;
    }

    /**
     * Removes the car from this grid if there is one.
     */
    public void removeCar() {
        setCar(null);
    }

    /**
     * Returns the traffic signal on this grid. NO_SIGNAL is returned if there
     * is no signal on this grid.
     * 
     * @return The status of the traffic signal on this grid.
     */
    public TrafficSignalState getTrafficSignal() {
        // TODO: getSignalOn(location)
        // return signal == null ? TrafficSignal.NO_SIGNAL :
        // signal.get_signal(location);
        if (location != -1)
            return (signal.getSignalWhen(location));
        else
            return TrafficSignalState.NO_SIGNAL;
    }

    public TrafficControl getTrafficControl() {
        return signal;
    }

    /**
     * Relates this grid to the given traffic light. The <code>location</code>
     * variable is given to inform of the relative location of this grid with
     * respect to the intersection.
     * 
     * @param traffic_signal
     *            The traffic light that this grid relates to.
     * @param location
     *            The location of this grid with respect to the traffic light.
     *            Read this as "this grid is on the {location} side of
     *            {traffic_signal}".
     */
    public void setTrafficControl(TrafficControl traffic_signal, int location) {
        this.signal = traffic_signal;
        this.signal_index = index;
    }
}