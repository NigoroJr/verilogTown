package com.me.myverilogTown;

public class VerilogTownGridNode
{
    private int x; // x location assuming x = 0 is left most
    private int y; // y location assuming y = 0 is bottom most
    private GridType grid_type; // type of grid point - see GridType for what
                                // they all mean
    private VerilogTownGridNode north;
    private VerilogTownGridNode south;
    private VerilogTownGridNode east;
    private VerilogTownGridNode west;
    private int visited_mark; // mark for BFS marking
    private VerilogTownGridNode visited_by; // node that visited this node for
                                            // reverse traversal
    private TrafficControl signal;
    private int signal_index;

    /* Constructor */
    VerilogTownGridNode(int x, int y, GridType type)
    {
        this.x = x;
        this.y = y;
        this.grid_type = type;
        this.north = null;
        this.south = null;
        this.east = null;
        this.west = null;
        this.visited_mark = 0; // initalize to 0, but will always be greater
                               // than
        this.visited_by = null; // initalize to 0, but will always be greater
                                // than

        this.signal = null;
        this.signal_index = -1;
    }

    int get_x()
    {
        return this.x;
    }

    int get_y()
    {
        return this.y;
    }

    int getVisitedCount()
    {
        return this.visited_mark;
    }

    void setVisited(int mark, VerilogTownGridNode by)
    {
        this.visited_mark = mark;
        this.visited_by = by;
    }

    VerilogTownGridNode getVisitedBy()
    {
        return this.visited_by;
    }

    boolean isAlreadyVisited(int markPathCount)
    {
        if (markPathCount == this.visited_mark)
            return true;
        else
            return false;
    }

    /* Functions for initializing the pointing of different grid points */
    /* Starting Points */
    void set_START_NEDGE2S(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.START_NEDGE2S;
        this.south = dest;
    }

    void set_START_SEDGE2N(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.START_SEDGE2N;
        this.north = dest;
    }

    void set_START_EEDGE2W(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.START_EEDGE2W;
        this.west = dest;
    }

    void set_START_WEDGE2E(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.START_WEDGE2E;
        this.east = dest;
    }

    /* ending points */
    void set_END_N2NEDGE()
    {
        this.grid_type = GridType.END_N2NEDGE;
    }

    void set_END_S2SEDGE()
    {
        this.grid_type = GridType.END_S2SEDGE;
    }

    void set_END_E2EEDGE()
    {
        this.grid_type = GridType.END_E2EEDGE;
    }

    void set_END_W2WEDGE()
    {
        this.grid_type = GridType.END_W2WEDGE;
    }

    /* Straight roads */
    void set_STRAIGHT_ROAD_E2E(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.STRAIGHT_ROAD_E2E;
        this.east = dest;
    }

    void set_STRAIGHT_ROAD_N2N(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.STRAIGHT_ROAD_N2N;
        this.north = dest;
    }

    void set_STRAIGHT_ROAD_S2S(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.STRAIGHT_ROAD_S2S;
        this.south = dest;
    }

    void set_STRAIGHT_ROAD_W2W(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.STRAIGHT_ROAD_W2W;
        this.west = dest;
    }

    /* Corners with only one path */
    void set_CORNER_ROAD_W2S(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.CORNER_ROAD_W2S;
        this.south = dest;
    }

    void set_CORNER_ROAD_N2E(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.CORNER_ROAD_N2E;
        this.east = dest;
    }

    void set_CORNER_ROAD_E2S(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.CORNER_ROAD_E2S;
        this.south = dest;
    }

    void set_CORNER_ROAD_N2W(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.CORNER_ROAD_N2W;
        this.west = dest;
    }

    void set_CORNER_ROAD_S2W(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.CORNER_ROAD_S2W;
        this.west = dest;
    }

    void set_CORNER_ROAD_E2N(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.CORNER_ROAD_E2N;
        this.north = dest;
    }

    void set_CORNER_ROAD_S2E(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.CORNER_ROAD_S2E;
        this.east = dest;
    }

    void set_CORNER_ROAD_W2N(VerilogTownGridNode dest)
    {
        this.grid_type = GridType.CORNER_ROAD_W2N;
        this.north = dest;
    }

    /* intersections with 2 options */
    void set_INTER_TURN_S2WS(VerilogTownGridNode dest1,
            VerilogTownGridNode dest2)
    {
        this.grid_type = GridType.INTER_TURN_S2WS;
        this.west = dest1;
        this.south = dest2;
    }

    void set_INTER_TURN_N2EN(VerilogTownGridNode dest1,
            VerilogTownGridNode dest2)
    {
        this.grid_type = GridType.INTER_TURN_N2EN;
        this.east = dest1;
        this.north = dest2;
    }

    void set_INTER_TURN_E2SE(VerilogTownGridNode dest1,
            VerilogTownGridNode dest2)
    {
        this.grid_type = GridType.INTER_TURN_E2SE;
        this.south = dest1;
        this.east = dest2;
    }

    void set_INTER_TURN_W2NW(VerilogTownGridNode dest1,
            VerilogTownGridNode dest2)
    {
        this.grid_type = GridType.INTER_TURN_W2NW;
        this.north = dest1;
        this.west = dest2;
    }

    /* grabing destinations */
    VerilogTownGridNode getNorth()
    {
        return this.north;
    }

    VerilogTownGridNode getSouth()
    {
        return this.south;
    }

    VerilogTownGridNode getEast()
    {
        return this.east;
    }

    VerilogTownGridNode getWest()
    {
        return this.west;
    }

    /* hooks up the associated traffic signal */
    public void add_traffic_signal(TrafficControl traffic_signal, int index)
    {
        this.signal = traffic_signal;
        this.signal_index = index;
    }
}
