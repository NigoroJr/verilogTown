package com.me.myverilogTown;

import java.util.*;
import com.badlogic.gdx.Gdx;

public class Car
{
	private verilogTownGridNode start_point;
	private verilogTownGridNode end_point;
	private int start_time;
	private boolean is_crashed;
	private verilogTownGridNode location;
	private int x;
	private int y;
	private verilogTownGridNode next_spot;
	private Stack<verilogTownGridNode> path;

	public Car(verilogTownGridNode start, verilogTownGridNode end, int starting_time, verilogTownMap level)
	{
		this.start_point = start;
		this.end_point = end;
		this.is_crashed = false;
		this.start_time = starting_time;

		/* intialize path from start to finish */
		this.path = level.findPath(this.start_point, this.end_point);
		/* debugging to see if path is good */
		level.showPath(this.path);
	}
}
