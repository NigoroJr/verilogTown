package com.me.myverilogTown;

import java.util.*;

public class TrafficControl
{
    private GridNode traffic_corners[]; /*
                                                    * Each traffic point is
                                                    * associated with 4 corner
                                                    * points
                                                    */
    private TrafficSignal signal_directions[]; /* four corners */
    private TrafficType t_type;

    private static final int NN = 0;
    private static final int NS = 1;
    private static final int NE = 2;
    private static final int NW = 3;

    public TrafficControl()
    {
        int i;

        traffic_corners = new GridNode[4];
        signal_directions = new TrafficSignal[4];

        for (i = 0; i < 4; i++)
        {
            traffic_corners[i] = null;
            signal_directions[i] = TrafficSignal.STOP;
        }
    }

    public void init_fourway_traffic_signal(GridNode N,
            GridNode S, GridNode E, GridNode W)
    {
        t_type = TrafficType.FOUR_WAY;

        traffic_corners[NN] = N;
        traffic_corners[NS] = S;
        traffic_corners[NE] = E;
        traffic_corners[NW] = W;

        N.addTrafficSignal(this, NN);
        S.addTrafficSignal(this, NS);
        E.addTrafficSignal(this, NE);
        W.addTrafficSignal(this, NW);
    }

    /* Note the naming indicates where the car is going to */
    public void init_nes_traffic_signal(GridNode N,
            GridNode S, GridNode E)
    {
        t_type = TrafficType.THREE_WAY_NES;

        traffic_corners[NN] = N;
        traffic_corners[NS] = S;
        traffic_corners[NE] = E;

        N.addTrafficSignal(this, NN);
        S.addTrafficSignal(this, NS);
        E.addTrafficSignal(this, NE);
    }

    public void init_esw_traffic_signal(GridNode S,
            GridNode E, GridNode W)
    {
        t_type = TrafficType.THREE_WAY_ESW;

        traffic_corners[NS] = S;
        traffic_corners[NE] = E;
        traffic_corners[NW] = W;

        S.addTrafficSignal(this, NS);
        E.addTrafficSignal(this, NE);
        W.addTrafficSignal(this, NW);
    }

    public void init_swn_traffic_signal(GridNode N,
            GridNode S, GridNode W)
    {
        t_type = TrafficType.THREE_WAY_SWN;

        traffic_corners[NN] = N;
        traffic_corners[NS] = S;
        traffic_corners[NW] = W;

        N.addTrafficSignal(this, NN);
        S.addTrafficSignal(this, NS);
        W.addTrafficSignal(this, NW);
    }

    public void init_wne_traffic_signal(GridNode N,
            GridNode E, GridNode W)
    {
        t_type = TrafficType.THREE_WAY_WNE;

        traffic_corners[NN] = N;
        traffic_corners[NW] = W;
        traffic_corners[NE] = E;

        N.addTrafficSignal(this, NN);
        E.addTrafficSignal(this, NE);
        W.addTrafficSignal(this, NW);
    }

}
