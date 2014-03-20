package com.me.myverilogtown;

import java.util.*;

public class TrafficControl 
{
	private verilogTownGridNode traffic_corners[]; /* Each traffic point is associated with 4 corner points */
	private TrafficSignal signal_directions[]; /* four corners */
	private TrafficType t_type;

	private static final int NN = 0;
	private static final int NS = 1;
	private static final int NE = 2;
	private static final int NW = 3;

	public TrafficControl()
	{
		int i;
		
		traffic_corners = new verilogTownGridNode[4];
		signal_directions = new TrafficSignal[4];

		for (i = 0; i < 4; i++)
		{
			traffic_corners[i] = null;
			signal_directions[i] = TrafficSignal.STOP;
		}
	}
	
	public void init_fourway_traffic_signal(verilogTownGridNode N, verilogTownGridNode S, verilogTownGridNode E, verilogTownGridNode W)
	{
		t_type = TrafficType.FOUR_WAY;	

		traffic_corners[NN] = N;
		traffic_corners[NS] = S;
		traffic_corners[NE] = E;
		traffic_corners[NW] = W;

		N.add_traffic_signal(this, NN);
		S.add_traffic_signal(this, NS);
		E.add_traffic_signal(this, NE);
		W.add_traffic_signal(this, NW);
	}
	/* Note the naming idicates where the car is going to */
	public void init_nse2_traffic_signal(verilogTownGridNode N, verilogTownGridNode S, verilogTownGridNode E)
	{
		t_type = TrafficType.THREE_WAY_NES;	

		traffic_corners[NN] = N;
		traffic_corners[NS] = S;
		traffic_corners[NE] = E;

		N.add_traffic_signal(this, NN);
		S.add_traffic_signal(this, NS);
		E.add_traffic_signal(this, NE);
	}
	public void init_sew2_traffic_signal(verilogTownGridNode S, verilogTownGridNode E, verilogTownGridNode W)
	{
		t_type = TrafficType.THREE_WAY_ESW;	

		traffic_corners[NS] = S;
		traffic_corners[NE] = E;
		traffic_corners[NW] = W;

		S.add_traffic_signal(this, NS);
		E.add_traffic_signal(this, NE);
		W.add_traffic_signal(this, NW);
	}
	public void init_nsw2_traffic_signal(verilogTownGridNode N, verilogTownGridNode S, verilogTownGridNode W)
	{
		t_type = TrafficType.THREE_WAY_SWN;	

		traffic_corners[NN] = N;
		traffic_corners[NS] = S;
		traffic_corners[NW] = W;

		N.add_traffic_signal(this, NN);
		S.add_traffic_signal(this, NS);
		W.add_traffic_signal(this, NW);
	}
	public void init_new2_traffic_signal(verilogTownGridNode N, verilogTownGridNode E, verilogTownGridNode W)
	{
		t_type = TrafficType.THREE_WAY_WNE;	

		traffic_corners[NN] = N;
		traffic_corners[NW] = W;
		traffic_corners[NE] = E;

		N.add_traffic_signal(this, NN);
		E.add_traffic_signal(this, NE);
		W.add_traffic_signal(this, NW);
	}

	public TrafficSignal get_signal(int signal_index)
	{
		return this.signal_directions[signal_index];
	}

	public void set_all_signals_nsew(TrafficSignal N_Signal, TrafficSignal S_Signal, TrafficSignal E_Signal, TrafficSignal W_Signal)
	{
		signal_directions[NN] = N_Signal;
		signal_directions[NS] = S_Signal;
		signal_directions[NE] = E_Signal;
		signal_directions[NW] = W_Signal;
	}
}
