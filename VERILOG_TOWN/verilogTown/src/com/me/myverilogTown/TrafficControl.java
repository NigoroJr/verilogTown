package com.me.myverilogTown;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

	public void render_signal(TrafficSignal signal, verilogTownGridNode grid, float rot, SpriteBatch batch, Texture stop, Texture go, Texture left, Texture right, Texture forward)
	{
		// draw(texture, x, y, origin to rotate around x, origin to rotate around y, size of texture x, size of tecture y, scale, scale, rotate, bottom part of texture x, bottom part of texture y, top of texture x, top of texture y, flip x boolean, flip y boolean)
		if (signal == TrafficSignal.STOP)
		{
			batch.draw(stop, (grid.get_x()-1)*64, (grid.get_y()-1)*64, 32, 32, 64, 64, 0.7f, 0.7f, rot, 0, 0, 64, 64, false, false);
		}
		else if (signal == TrafficSignal.GO)
		{
			batch.draw(go, (grid.get_x()-1)*64, (grid.get_y()-1)*64, 32, 32, 64, 64, 1.0f, 1.0f, rot, 0, 0, 64, 64, false, false);
		}
		else if (signal == TrafficSignal.GO_FORWARD)
		{
			batch.draw(forward, (grid.get_x()-1)*64, (grid.get_y()-1)*64, 32, 32, 64, 64, 1.0f, 1.0f, rot, 0, 0, 64, 64, false, false);
		}
		else if (signal == TrafficSignal.GO_LEFT)
		{
			batch.draw(left, (grid.get_x()-1)*64, (grid.get_y()-1)*64, 32, 32, 64, 64, 1.0f, 1.0f, rot, 0, 0, 64, 64, false, false);
		}
		else if (signal == TrafficSignal.GO_RIGHT)
		{
			batch.draw(right, (grid.get_x()-1)*64, (grid.get_y()-1)*64, 32, 32, 64, 64, 1.0f, 1.0f, rot, 0, 0, 64, 64, false, false);
		}
	}

	public float rotate(int dir)
	{
		if (dir == NN)
		{
			return 0.0f;
		}
		else if (dir == NS)
		{
			return 180.0f;
		}
		else if (dir == NE)
		{
			return 90.0f;
		}
		else if (dir == NW)
		{
			return 270.0f;
		}

		return -45.0f;
	}

	public void render_traffic_signal(SpriteBatch batch, Texture stop, Texture go, Texture left, Texture right, Texture forward)
	{
		for (int i = 0; i < 4; i++)
		{
			if (traffic_corners[i] != null)
			{
				render_signal(signal_directions[i], traffic_corners[i], this.rotate(i), batch, stop, go, left, right, forward);
			}
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
